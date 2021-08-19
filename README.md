# WinSW Maven Plugin

[![GitHub Workflow Status](https://img.shields.io/github/workflow/status/nikolaybespalov/winsw-maven-plugin/Build?label=Build)](https://github.com/nikolaybespalov/winsw-maven-plugin/actions?query=workflow%3ABuild)
[![GitHub Workflow Status](https://img.shields.io/github/workflow/status/nikolaybespalov/winsw-maven-plugin/Deploy?label=Deploy)](https://github.com/nikolaybespalov/winsw-maven-plugin/actions?query=workflow%3ADeploy)
[![Maven Central](https://img.shields.io/maven-central/v/com.github.nikolaybespalov/winsw-maven-plugin.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22com.github.nikolaybespalov%22%20AND%20a:%22winsw-maven-plugin%22)
[![Codacy coverage](https://img.shields.io/codacy/coverage/76a37ca267664b63bb71d5cd79b8df25?color=GREEN)](https://app.codacy.com/gh/nikolaybespalov/winsw-maven-plugin)
[![Codacy grade](https://img.shields.io/codacy/grade/76a37ca267664b63bb71d5cd79b8df25)](https://app.codacy.com/gh/nikolaybespalov/winsw-maven-plugin)

Maven plugin for [winsw](https://github.com/kohsuke/winsw).

## Description
WinSW is an executable binary, which can be used to wrap and manage a custom process as a Windows service.

The Maven plugin for WinSW lets you download the WinSW executable and generate XML configuration file as part of the Maven build process.

## Adding plugin to pom.xml

Using the Maven plugin, you specify the WinSW configuration in your POM or use an existing one. 
The format of this configuration is very similar to the standard WinSW XML format.

```xml
  <configurationFile>
    <id>myapp</id>
    <name>My App</name>
    <description>My App Description</description>
    <executable>java</executable>
    <arguments>-jar ${project.build.finalName}.jar</arguments>
  </configurationFile>
```

WinSW plugin also provides the ability to modify exe file information and add an icon. The plugin supports Linux, macOS and Windows.

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
     <!-- <winswRepo>jar</winswRepo>, if 'https://repo.jenkins-ci.org/releases/' is unreachable -->
            <winswVersion>2.9.0</winswVersion>
     <!-- <winswClassifier>net4</winswClassifier>, 'bin' by default -->
     <!-- <executableFilePath>...</executableFilePath>, If executable file already exists -->
          <executableFileName>${project.build.finalName}.exe</executableFileName>
          <configurationFileName>${project.build.finalName}.xml</configurationFileName>
          <configurationFile>
            <id>${project.artifactId}</id>
            <name>${project.name}</name>
            <description>${project.description}</description>
            <executable>java</executable>
            <arguments>-jar ${project.build.finalName}.jar</arguments>
          </configurationFile>
     <!-- <configurationFilePath>...</configurationFilePath>, If WinSW Configuration File already exists -->
          <rcFile>
            <icon>${project.basedir}/${project.name}.ico</icon>
            <fileInfo>
              <fileVersion>${project.version}</fileVersion>
              <productVersion>${project.version}</productVersion>
              <comments>${project.description}</comments>
              <companyName>${project.organization.name}</companyName>
              <copyright>© ${project.inceptionYear} ${project.organization.name} All Rights Reserved</copyright>
            </fileInfo>
          </rcFile>
        </configuration>
      </execution>
    </executions>
  </plugin>
```

Don't forget about MainClass

```xml
  <plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-jar-plugin</artifactId>
    <configuration>
      <archive>
        <manifest>
          <mainClass>HelloWorld</mainClass>
        </manifest>
      </archive>
    </configuration>
  </plugin>
```

If you have any questions, please register a ticket!

Enjoy!

## Many thanks

[pe_tools](https://github.com/avast/pe_tools) is a great tools without which the possibilities would be limited!
