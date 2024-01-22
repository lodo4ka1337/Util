# Util
&emsp;A Java project that builds utility JAR-file util-1.0.jar that filters the contents of input files and prints type statistics about each data type.<br />
## Supported data types
- Integer
- Float
- String
## Project build info
- Java version: 8
- Project build system: Maven 4.0.0
- Third-party libraries:
   - Apache Commons-CLI 1.6.0<br />
  ```
  <dependency>
      <groupId>commons-cli</groupId>
      <artifactId>commons-cli</artifactId>
      <version>1.6.0</version>
  </dependency>
  ```
- Plug-ins:
  - Maven Shade Plug-in 3.2.0<br />
  ```
  <plugin>
      <groupId>org.apache.maven.plugins</groupId>
      <artifactId>maven-shade-plugin</artifactId>
      <version>3.2.0</version>
      <executions>
          <execution>
              <phase>package</phase>
              <goals>
                  <goal>shade</goal>
              </goals>
              <configuration>
                  <transformers>
                      <transformer
                              implementation="org.apache.maven.plugins.shade.resource.ManifestResourceTransformer">
                          <mainClass>lodo4ka.Main</mainClass>
                      </transformer>
                  </transformers>
              </configuration>
          </execution>
      </executions>
  </plugin>
  ```
  - Maven JAR Plug-in 3.3.0<br />
  ```
  <plugin>
      <groupId>org.apache.maven.plugins</groupId>
      <artifactId>maven-jar-plugin</artifactId>
      <configuration>
          <archive>
              <manifest>
                  <mainClass>lodo4ka.Main</mainClass>
                  <addClasspath>true</addClasspath>
                  <classpathPrefix>data/lib/</classpathPrefix>
              </manifest>
          </archive>
      </configuration>
  </plugin>
  ```
  - Maven Dependency Plug-in 3.6.0<br />
  ```
  <plugin>
      <artifactId>maven-dependency-plugin</artifactId>
      <executions>
          <execution>
              <phase>install</phase>
              <goals>
                  <goal>copy-dependencies</goal>
              </goals>
              <configuration>
                  <outputDirectory>${project.build.directory}/data/lib</outputDirectory>
              </configuration>
          </execution>
      </executions>
  </plugin>
  ```
## util-1.0.jar usage
### File location
&emsp;util-1.0.jar is located at Util/target folder.
### Update
&emsp;In order to make changes to and update util-1.0.jar, change the Util/src/main/java/lodo4ka/Util.java class and type this command in terminal:
   ```
   mvn clean package
   ```
&emsp;Maven will rebuild the file and it will be ready for use.
### Preparatory configuration (optional)
&emsp;It is recommended to put util-1.0.jar and input files in a working directory, so cmd commands would be shorter.
### Command Line Interface
&emsp;If you have "util-1.0.jar" and "in1.txt", "in2.txt" input files in a working directory, then you can type something like this:
   ```
   java -jar util-1.0.jar in1.txt in2.txt
   ```
&emsp;This command filters input from files "in1.txt" and "in2.txt" and writes output to "integers.txt", "floats.txt", "strings.txt" files, creating them in a working directory.<br />
&emsp;Usage template and all available options are listed here:
   ```
   usage: util.jar [OPTIONS] <INPUT_FILES>              
    -o,--output <arg>   Output path                     
    -p,--prefix <arg>   Output files prefix             
    -a,--append         Append to output files          
    -s,--short          Short type statistics in console
    -f,--full           Full type statistics in console 
    -h,--help           Application usage help 
   ```
