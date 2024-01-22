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
