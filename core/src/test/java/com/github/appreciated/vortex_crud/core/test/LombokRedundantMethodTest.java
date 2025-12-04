package com.github.appreciated.vortex_crud.core.test;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.nodeTypes.NodeWithAnnotations;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ReturnStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.expr.AssignExpr;
import com.github.javaparser.ast.expr.FieldAccessExpr;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.fail;

public class LombokRedundantMethodTest {

    @Test
    public void verifyTestLogicDetectsRedundancy() {
        String source = "import lombok.Data;\n" +
                "@Data\n" +
                "public class Redundant {\n" +
                "    private String field;\n" +
                "    public String getField() { return field; }\n" +
                "}";
        List<String> failures = new ArrayList<>();
        checkSource(source, "Redundant.java", failures);
        if (failures.isEmpty()) fail("Should detect redundancy");
    }

    @Test
    public void ensureNoRedundantLombokMethods() throws IOException {
        List<String> failures = new ArrayList<>();
        File currentDir = new File(".");
        File repoRoot = currentDir.getParentFile();
        if (repoRoot == null || !new File(repoRoot, "pom.xml").exists()) {
             repoRoot = currentDir;
        }

        try (Stream<Path> walk = Files.walk(repoRoot.toPath())) {
            walk.filter(p -> p.toString().endsWith(".java"))
                .filter(p -> !p.toString().contains(File.separator + "target" + File.separator))
                .filter(p -> !p.toString().contains(File.separator + "test" + File.separator))
                .filter(p -> !p.toString().contains("src" + File.separator + "test"))
                .forEach(path -> {
                    try {
                        checkSource(Files.readString(path), path.toString(), failures);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
        }

        if (!failures.isEmpty()) {
            fail("Found redundant Lombok methods:\n" + String.join("\n", failures));
        }
    }

    private void checkSource(String source, String fileName, List<String> failures) {
        try {
            CompilationUnit cu = StaticJavaParser.parse(source);
            cu.findAll(ClassOrInterfaceDeclaration.class).forEach(c -> checkClass(c, fileName, failures));
        } catch (Exception e) {
            // Ignore parse errors
        }
    }

    private void checkClass(ClassOrInterfaceDeclaration c, String fileName, List<String> failures) {
        boolean isData = hasAnnotation(c, "Data");
        boolean isValue = hasAnnotation(c, "Value");
        boolean classGetter = hasAnnotation(c, "Getter");
        boolean classSetter = hasAnnotation(c, "Setter");

        boolean classGetterSuppressed = isAccessLevelNone(c, "Getter");
        boolean classSetterSuppressed = isAccessLevelNone(c, "Setter");

        c.getFields().forEach(fieldDecl -> {
            if (fieldDecl.isStatic()) return;

            boolean fieldGetter = hasAnnotation(fieldDecl, "Getter");
            boolean fieldSetter = hasAnnotation(fieldDecl, "Setter");

            boolean getterSuppressed = isAccessLevelNone(fieldDecl, "Getter");
            boolean setterSuppressed = isAccessLevelNone(fieldDecl, "Setter");

            boolean effectivelyHasGetter;
            if (fieldGetter) {
                effectivelyHasGetter = !getterSuppressed;
            } else {
                effectivelyHasGetter = (isData || isValue || (classGetter && !classGetterSuppressed));
            }

            boolean effectivelyHasSetter;
            if (fieldSetter) {
                effectivelyHasSetter = !setterSuppressed;
            } else {
                effectivelyHasSetter = ((isData && !isValue) || (classSetter && !classSetterSuppressed));
            }

            if (fieldDecl.isFinal()) effectivelyHasSetter = false;

            for (VariableDeclarator var : fieldDecl.getVariables()) {
                String fieldName = var.getNameAsString();
                String typeName = var.getTypeAsString();

                if (effectivelyHasGetter) checkGetter(c, fieldName, typeName, fileName, failures);
                if (effectivelyHasSetter) checkSetter(c, fieldName, fileName, failures);
            }
        });
    }

    private boolean isAccessLevelNone(NodeWithAnnotations<?> node, String annotationName) {
        return node.getAnnotations().stream()
            .filter(a -> {
                String n = a.getNameAsString();
                return n.equals(annotationName) || n.endsWith("." + annotationName);
            })
            .anyMatch(ann -> {
                if (ann.isSingleMemberAnnotationExpr()) {
                    return ann.asSingleMemberAnnotationExpr().getMemberValue().toString().contains("AccessLevel.NONE");
                } else if (ann.isNormalAnnotationExpr()) {
                    return ann.asNormalAnnotationExpr().getPairs().stream()
                            .anyMatch(pair -> pair.getValue().toString().contains("AccessLevel.NONE"));
                }
                return false;
            });
    }

    private void checkGetter(ClassOrInterfaceDeclaration c, String fieldName, String typeName, String fileName, List<String> failures) {
        String baseName = capitalize(fieldName);
        String getterName = "get" + baseName;
        String isName = "is" + baseName;

        List<MethodDeclaration> candidates = c.getMethodsByName(getterName);
        if (candidates.isEmpty() && (typeName.equals("boolean") || typeName.equals("Boolean"))) {
             candidates = c.getMethodsByName(isName);
        }

        for (MethodDeclaration m : candidates) {
            if (m.getAnnotations().stream().anyMatch(a -> !a.getNameAsString().equals("Override"))) continue;

            if (isTrivialGetter(m, fieldName)) {
                failures.add(fileName + ": " + c.getNameAsString() + "." + m.getNameAsString() + " is redundant");
            }
        }
    }

    private void checkSetter(ClassOrInterfaceDeclaration c, String fieldName, String fileName, List<String> failures) {
        String setterName = "set" + capitalize(fieldName);
        List<MethodDeclaration> candidates = c.getMethodsByName(setterName);

        for (MethodDeclaration m : candidates) {
             if (m.getAnnotations().stream().anyMatch(a -> !a.getNameAsString().equals("Override"))) continue;

             if (isTrivialSetter(m, fieldName)) {
                 failures.add(fileName + ": " + c.getNameAsString() + "." + m.getNameAsString() + " is redundant");
             }
        }
    }

    private boolean isTrivialGetter(MethodDeclaration m, String fieldName) {
        Optional<BlockStmt> body = m.getBody();
        if (body.isEmpty()) return false;
        if (body.get().getStatements().size() != 1) return false;

        Statement stmt = body.get().getStatements().get(0);
        if (!(stmt instanceof ReturnStmt)) return false;

        ReturnStmt ret = (ReturnStmt) stmt;
        if (ret.getExpression().isEmpty()) return false;

        if (ret.getExpression().get().isNameExpr()) {
             return ret.getExpression().get().asNameExpr().getNameAsString().equals(fieldName);
        } else if (ret.getExpression().get().isFieldAccessExpr()) {
             FieldAccessExpr fa = ret.getExpression().get().asFieldAccessExpr();
             return fa.getNameAsString().equals(fieldName) && fa.getScope().isThisExpr();
        }
        return false;
    }

    private boolean isTrivialSetter(MethodDeclaration m, String fieldName) {
        Optional<BlockStmt> body = m.getBody();
        if (body.isEmpty()) return false;
        if (body.get().getStatements().size() != 1) return false;

        Statement stmt = body.get().getStatements().get(0);
        if (!(stmt instanceof ExpressionStmt)) return false;
        ExpressionStmt es = (ExpressionStmt) stmt;

        if (!es.getExpression().isAssignExpr()) return false;
        AssignExpr assign = es.getExpression().asAssignExpr();
        if (assign.getOperator() != AssignExpr.Operator.ASSIGN) return false;

        if (!assign.getTarget().isFieldAccessExpr() && !assign.getTarget().isNameExpr()) return false;

        String targetName = "";
        if (assign.getTarget().isNameExpr()) targetName = assign.getTarget().asNameExpr().getNameAsString();
        if (assign.getTarget().isFieldAccessExpr()) {
            FieldAccessExpr fa = assign.getTarget().asFieldAccessExpr();
            if (!fa.getScope().isThisExpr()) return false;
            targetName = fa.getNameAsString();
        }

        if (!targetName.equals(fieldName)) return false;

        if (m.getParameters().size() != 1) return false;
        String paramName = m.getParameters().get(0).getNameAsString();

        if (!assign.getValue().isNameExpr()) return false;
        return assign.getValue().asNameExpr().getNameAsString().equals(paramName);
    }

    private boolean hasAnnotation(NodeWithAnnotations<?> c, String name) {
        return c.getAnnotations().stream().anyMatch(a -> {
            String n = a.getNameAsString();
            return n.equals(name) || n.endsWith("." + name);
        });
    }

    private String capitalize(String s) {
        if (s == null || s.isEmpty()) return s;
        return Character.toUpperCase(s.charAt(0)) + s.substring(1);
    }
}
