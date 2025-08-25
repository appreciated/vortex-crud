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
2. To make sure the generated code compiles without running the tests, run the following command:
   ```bash
    mvn --no-transfer-progress -B install -DskipTests --file pom.xml
   ```

3. To run the test and ui-test, run the following maven command:

   ```bash
    xvfb-run mvn --no-transfer-progress -B verify --file pom.xml
   ```

   This command builds all modules and executes their tests. Be very patient with this command. It takes at least two
   minutes to complete for all modules.
