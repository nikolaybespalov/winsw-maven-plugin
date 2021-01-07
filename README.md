# WinSW Maven Plugin

![Build Status](https://github.com/nikolaybespalov/winsw-maven-plugin/workflows/Build%20and%20Deploy/badge.svg)
![Maven](https://img.shields.io/maven-central/v/com.github.nikolaybespalov/winsw-maven-plugin.svg)
![Coverage](https://img.shields.io/codacy/coverage/bd1c44ba93be430cba9cfe1c20c4e179.svg)
[![Codacy Badge](https://app.codacy.com/project/badge/Grade/568936682652455b8874b47af2fd6fde)](https://www.codacy.com/gh/nikolaybespalov/winsw-maven-plugin/dashboard?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=nikolaybespalov/winsw-maven-plugin&amp;utm_campaign=Badge_Grade)

Maven plugin for [winsw](https://github.com/kohsuke/winsw).

## Description
WinSW is an executable binary, which can be used to wrap and manage a custom process as a Windows service.

The Maven plugin for WinSW lets you download the WinSW executable and generate XML configuration file as part of the Maven build process.

## Adding plugin to pom.xml

Using the Maven plugin, you specify the WinSW configuration in your POM. 
The format of this configuration is very similar to the standard WinSW XML format.

```xml
    <configurationFile>
        <id>myapp</id>
        <name>My App</name>
        <description>My App Description</description>
        <executable>java</executable>
        <arguments>-jar ${build.finalName}.jar</arguments>
    </configurationFile>
```

WinSW plugin also provides the ability to modify file information and add an icon.

```xml
    <rcFile>
        <icon>myapp.ico</icon>
        <fileInfo>
            <fileVersion>1.2.3.4</fileVersion>
            <productVersion>1.2.3.4</productVersion>
            <comments>My App Description</comments>
            <companyName>John Doe</companyName>
            <copyright>© 2019 John Doe All Rights Reserved</copyright>
        </fileInfo>
    </rcFile>
```

## Example

As an example, you can use the following

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
                    <!-- or use exists WinSW Executable File <executableFilePath>...</executableFilePath> -->
                    <executableFileName>${project.build.finalName}.exe</executableFileName>
                    <configurationFileName>${project.build.finalName}.xml</configurationFileName>
                    <configurationFile>
                        <id>${project.artifactId}</id>
                        <name>${project.name}</name>
                        <description>${project.description}</description>
                        <executable>java</executable>
                        <arguments>-jar ${build.finalName}.jar</arguments>
                    </configurationFile>
                    <!-- or use exists WinSW Configuration File <configurationFilePath>...</configurationFilePath> -->
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

If you have any questions, please register a ticket!

Enjoy!