# winsw-maven-plugin

Maven plugin for [winsw](https://github.com/kohsuke/winsw).

```xml
<plugin>
    <groupId>com.github.nikolaybespalov</groupId>
    <artifactId>winsw-maven-plugin</artifactId>
    <version>${winsw.maven.plugin.version}</version>
    <executions>
        <execution>
            <id>process-winsw-stuff</id>
            <phase>prepare-package</phase>
            <goals>
                <goal>winsw</goal>
            </goals>
            <configuration>
                <outputDirectory>${project.build.directory}</outputDirectory>
                <winswVersion>2.2.0</winswVersion>
                <executableFileName>${project.build.finalName}.exe</executableFileName>
                <configurationFileName>${project.build.finalName}.xml</configurationFileName>
                <configurationFile>
                    <id>${project.artifactId}</id>
                    <name>${project.name}</name>
                    <description>${project.description}</description>
                    <executable>java</executable>
                    <arguments>-jar ${build.finalName}.jar</arguments>
                </configurationFile>
                <rcFile>
                    <icon>${project.basedir}/${project.name}.ico</icon>
                    <fileInfo>
                        <fileVersion>${project.version}</fileVersion>
                        <productVersion>${project.version}</productVersion>
                        <comments>${project.description}</comments>
                        <companyName>${organization.name}</companyName>
                        <copyright>© ${inceptionYear} ${organization.name} All Rights Reserved</copyright>
                    </fileInfo>
                </rcFile>
            </configuration>
        </execution>
    </executions>
</plugin>
```