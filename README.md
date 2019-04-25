# Winsw Maven Plugin

[![Build Status](https://travis-ci.org/nikolaybespalov/winsw-maven-plugin.svg?branch=master)](https://travis-ci.org/nikolaybespalov/winsw-maven-plugin)

Maven plugin for [winsw](https://github.com/kohsuke/winsw).

## Description
WinSW is an executable binary, which can be used to wrap and manage a custom process as a Windows service.

The Maven plugin for WinSW lets you download the WinSW executable and generate XML configuration file as part of the Maven build process.

## Adding plugin to pom.xml

Using the Maven plugin, you specify the Winsw configuration in your POM. 
The format of this configuration is very similar to the standard Winsw XML format.

```xml
    <configurationFile>
        <id>myapp</id>
        <name>My App</name>
        <description>My App Description</description>
        <executable>java</executable>
        <arguments>-jar ${build.finalName}.jar</arguments>
    </configurationFile>
```

Winsw plugin also provides the ability to modify file information and add icon.

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

As an example you can use the following

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

If you have any questions, please register a ticket!

Enjoy!