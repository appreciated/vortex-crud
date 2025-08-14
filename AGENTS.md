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

2. To run the build and test, make sure to run maven from the repository root the following way:

   ```bash
    xvfb-run mvn --no-transfer-progress -B package --file pom.xml
   ```

   This command builds all modules and executes their tests. Be very patient with this command. It takes at least two
   minutes to complete for all modules.
