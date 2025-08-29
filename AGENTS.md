# AGENTS Instructions

## Build and Test

1. Configure Maven with the proxy. Create
   `~/.m2/settings.xml` with the following content:

   ```bash
   cat > ~/.m2/settings.xml <<'EOF'
   <settings>
     <proxies>
       <proxy>
         <id>codexProxy</id>
         <active>true</active>
         <protocol>http</protocol>
         <host>proxy</host>
         <port>8080</port>
       </proxy>
     </proxies>
   </settings>
   EOF
   ```
2. To make sure the generated code compiles without running the tests, run the following command; it will take about a minute, so please be patient:
   ```bash
    mvn --no-transfer-progress -B install -DskipTests --file pom.xml
   ```

## Implementing new features

- Always add new, fitting use-cases to ui-test-base when implementing new features.
    - Cover the feature’s expected behavior and edge cases.

- Provide respective implementations for both stacks: jOOQ and JPA as to be seen in the other tests

- Add a standalone route to the configurations in examples/jpa-sqlite-example and examples/jooq-sqlite-example for the new feature. If you modified an existing feature, please update the existing example.