# AGENTS Instructions

## Build and Test

1. Configure Maven with the GitHub Packages repository and proxy. Create
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

     <profiles>
       <profile>
         <id>github</id>
         <repositories>
           <repository>
             <id>github</id>
             <url>https://maven.pkg.github.com/mycompany/*</url>
             <snapshots>
               <enabled>true</enabled>
             </snapshots>
           </repository>
         </repositories>
       </profile>
     </profiles>
     <activeProfiles>
       <activeProfile>github</activeProfile>
     </activeProfiles>

     <servers>
       <server>
         <id>github</id>
         <username>${env.GITHUB_ACTOR}</username>
         <password>${env.GITHUB_TOKEN}</password>
       </server>
     </servers>
   </settings>
   EOF
   ```

   Add `GITHUB_ACTOR` and `GITHUB_TOKEN` as environment secrets in Codex so
   Maven can authenticate to GitHub Packages.

2. Run Maven from the repository root:

   ```bash
   mvn clean install
   ```

   This command builds all modules and executes their tests.
