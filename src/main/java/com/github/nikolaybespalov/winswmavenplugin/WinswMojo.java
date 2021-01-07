package com.github.nikolaybespalov.winswmavenplugin;

import com.github.nikolaybespalov.winswmavenplugin.rc.FileInfo;
import com.github.nikolaybespalov.winswmavenplugin.rc.RcFile;
import com.github.nikolaybespalov.winswmavenplugin.rc.RcFileWriter;
import com.github.nikolaybespalov.winswmavenplugin.xml.ConfigurationFile;
import com.github.nikolaybespalov.winswmavenplugin.xml.ConfigurationFileWriter;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.SystemUtils;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.BuildPluginManager;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.eclipse.aether.repository.RemoteRepository;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.Arrays;

import static org.twdata.maven.mojoexecutor.MojoExecutor.*;

@Mojo(name = "winsw", defaultPhase = LifecyclePhase.PREPARE_PACKAGE)
@SuppressWarnings("unused")
public class WinswMojo extends AbstractMojo {
    @Parameter(defaultValue = "${project}", readonly = true)
    @SuppressWarnings("unused")
    private MavenProject mavenProject;

    @Parameter(defaultValue = "${session}", readonly = true)
    @SuppressWarnings("unused")
    private MavenSession mavenSession;

    @Component
    @SuppressWarnings("unused")
    private BuildPluginManager pluginManager;

    @Parameter(defaultValue = "${project.build.directory}", required = true)
    @SuppressWarnings("unused")
    private File outputDirectory;

    @Parameter(defaultValue = "2.2.0")
    @SuppressWarnings("unused")
    private String winswVersion;

    @Parameter(defaultValue = "${project.build.finalName}.exe")
    @SuppressWarnings("unused")
    private String executableFileName;

    @Parameter
    @SuppressWarnings("unused")
    private File executableFilePath;

    @Parameter(defaultValue = "${project.build.finalName}.xml")
    @SuppressWarnings("unused")
    private String configurationFileName;

    @Parameter
    @SuppressWarnings("unused")
    private File configurationFilePath;

    @Parameter
    private ConfigurationFile configurationFile;

    @Parameter
    @SuppressWarnings("unused")
    private RcFile rcFile;

    @Override
    public void execute() throws MojoExecutionException {
        if (!outputDirectory.exists()) {
            if (!outputDirectory.mkdirs()) {
                throw new MojoExecutionException("Could not create " + outputDirectory);
            }
        }

        processConfigurationFile();

        processExeFile();
    }

    private void processConfigurationFile() throws MojoExecutionException {
        if (configurationFilePath == null) {
            createConfigurationFile();
        } else {
            copyConfigurationFile();
        }
    }

    private void createConfigurationFile() throws MojoExecutionException {
        getLog().debug("Creating configuration file");

        if (configurationFile == null) {
            configurationFile = new ConfigurationFile();
        }

        if (configurationFile.getId() == null) {
            configurationFile.setId(mavenProject.getArtifactId());
        }

        if (configurationFile.getName() == null) {
            configurationFile.setName(mavenProject.getName());
        }

        if (configurationFile.getExecutable() == null) {
            configurationFile.setExecutable("java");
        }

        if (configurationFile.getArguments() == null) {
            configurationFile.setArguments("-jar " + mavenProject.getBuild().getFinalName() + ".jar");
        }

        File file = new File(outputDirectory, configurationFileName);

        try (FileWriter fileWriter = new FileWriter(file)) {
            ConfigurationFileWriter configurationFileWriter = new ConfigurationFileWriter(configurationFile);

            configurationFileWriter.writeTo(fileWriter);
        } catch (IOException e) {
            throw new MojoExecutionException("Failed to write configuration file " + file, e);
        }
    }

    private void copyConfigurationFile() throws MojoExecutionException {
        copyFileToOutputDirectory(configurationFilePath, configurationFileName);
    }

    private void processExeFile() throws MojoExecutionException {
        File exeFile;
        File resultExeFile;

        if (executableFilePath != null) {
            exeFile = copyExecutableFile();
        } else {
            try {
                exeFile = downloadWinswBinArtifact();
            } catch (IOException e) {
                throw new MojoExecutionException("Failed to download winsw.exe", e);
            }
        }

        resultExeFile = exeFile;

        File rcFile;

        try {
            rcFile = writeRcFile();
        } catch (IOException e) {
            throw new MojoExecutionException("Failed to write rc file", e);
        }

        if (rcFile != null) {
            File resFile;

            try {
                resFile = buildResFile(rcFile);
            } catch (IOException | InterruptedException e) {
                throw new MojoExecutionException("Failed to build res file", e);
            }

            try {
                resultExeFile = mergeResFileAndExeFile(resFile, exeFile);
            } catch (IOException | InterruptedException e) {
                throw new MojoExecutionException("Failed to merge exe and res file", e);
            }

            getLog().info("The file " + resultExeFile + " was successfully processed.");
        }

        File destExeFile = new File(outputDirectory, executableFileName);

        if (!resultExeFile.equals(destExeFile)) {
            try {
                getLog().debug("Copying result exe file");

                getLog().debug(destExeFile.toString());

                FileUtils.copyFile(resultExeFile, destExeFile);
            } catch (IOException e) {
                throw new MojoExecutionException("Failed to copy exe file", e);
            }
        }
    }

    private File copyExecutableFile() throws MojoExecutionException {
        //return copyFileToOutputDirectory(executableFile, executableFileName);

        if (!executableFilePath.exists()) {
            throw new MojoExecutionException("File not found " + executableFilePath);
        }

        File outputFile = new File(outputDirectory, executableFileName);

        if (!outputFile.exists()) {
            try {
                FileUtils.copyFile(executableFilePath, outputFile);
            } catch (IOException e) {
                throw new MojoExecutionException("Failed to copy file " + executableFilePath, e);
            }
        }

        return outputFile;
    }

    private File downloadWinswBinArtifact() throws IOException, MojoExecutionException {
        getLog().debug("Downloading winsw.exe file");

        Path path = Files.createTempFile("winsw", ".exe");

        getLog().debug(path.toString());

        RemoteRepository winswRepository = new RemoteRepository.Builder("winsw", "default", "http://repo.jenkins-ci.org/releases/").build();

        mavenProject.getRemoteProjectRepositories().add(winswRepository);

        try {
            executeMojo(
                    plugin(
                            groupId("com.googlecode.maven-download-plugin"),
                            artifactId("download-maven-plugin"),
                            version("1.4.1")
                    ),
                    goal("artifact"),
                    configuration(
                            element(name("groupId"), "com.sun.winsw"),
                            element(name("artifactId"), "winsw"),
                            element(name("version"), winswVersion),
                            element(name("type"), "exe"),
                            element(name("classifier"), "bin"),
                            element(name("outputDirectory"), path.toFile().getParentFile().getAbsolutePath()),
                            element(name("outputFileName"), path.toFile().getName())
                    ),
                    executionEnvironment(
                            mavenProject,
                            mavenSession,
                            pluginManager
                    )
            );
        } finally {
            mavenProject.getRemoteProjectRepositories().remove(winswRepository);
        }

        return path.toFile();
    }

    private File extractFile(String name) throws IOException {
        String resourceName = "/bin";

        if (SystemUtils.IS_OS_WINDOWS) {
            resourceName += "/win32-x86/" + name + ".exe";
        } else if (SystemUtils.IS_OS_MAC_OSX) {
            resourceName += "/macosx-x86/" + name;
        } else if (SystemUtils.IS_OS_LINUX) {
            resourceName += "/linux-x86-64/" + name;
        } else {
            throw new IOException("Unsupported platform");
        }

        getLog().debug("Extraction " + resourceName);

        try (InputStream is = getClass().getResourceAsStream(resourceName)) {
            if (is == null) {
                throw new IOException("Failed to getResourceAsStream for " + resourceName);
            }

            Path path = Files.createTempFile(name, ".exe");

            if (!SystemUtils.IS_OS_WINDOWS) {
                Files.setPosixFilePermissions(path, PosixFilePermissions.fromString("rwxr-xr-x"));
            }

            getLog().debug(path.toString());

            FileUtils.copyInputStreamToFile(is, path.toFile());

            return path.toFile();
        }
    }

    private File writeRcFile() throws IOException {
        getLog().debug("Writing rc file");

        Path path = Files.createTempFile("winsw", ".rc");

        getLog().debug(path.toString());

        if (rcFile == null) {
            return null;
        }

        if (rcFile.getFileInfo() == null) {
            rcFile.setFileInfo(new FileInfo());
        }

        if (rcFile.getFileInfo().getFileVersion() == null) {
            rcFile.getFileInfo().setFileVersion(mavenProject.getVersion());
        }

        if (rcFile.getFileInfo().getProductVersion() == null) {
            rcFile.getFileInfo().setProductVersion(mavenProject.getVersion());
        }

        if (rcFile.getFileInfo().getTxtFileVersion() == null) {
            rcFile.getFileInfo().setTxtFileVersion(mavenProject.getVersion());
        }

        if (rcFile.getFileInfo().getInternalName() == null) {
            rcFile.getFileInfo().setInternalName(mavenProject.getName());
        }

        if (rcFile.getFileInfo().getOriginalFilename() == null) {
            rcFile.getFileInfo().setOriginalFilename(executableFileName);
        }

        if (rcFile.getFileInfo().getProductName() == null) {
            rcFile.getFileInfo().setProductName(mavenProject.getName());
        }

        if (rcFile.getFileInfo().getTxtProductVersion() == null) {
            rcFile.getFileInfo().setTxtProductVersion(mavenProject.getVersion());
        }

        RcFileWriter rcFileWriter = new RcFileWriter(rcFile);

        try (Writer fileWriter = new FileWriter(path.toFile())) {
            rcFileWriter.writeTo(fileWriter);
        }

        return path.toFile();
    }

    private File buildResFile(File rcFile) throws IOException, InterruptedException {
        getLog().debug("Making res file");

        File windresFile = extractFile("windres");

        Path path = Files.createTempFile("winsw", ".o");

        getLog().debug(path.toString());

        // windres --preprocessor=type|cat -J rc -O coff -F pe-i386 INPUT.rc OUTPUT.o
        executeCommand(windresFile.getAbsolutePath(),
                SystemUtils.IS_OS_WINDOWS ? "--preprocessor=type" : "--preprocessor=cat",
                "-O",
                "coff",
                "-F",
                "pe-i386",
                rcFile.getAbsolutePath(),
                path.toFile().getAbsolutePath());

        return path.toFile();
    }

    private File mergeResFileAndExeFile(File resFile, File exeFile) throws IOException, InterruptedException {
        getLog().debug("Merging res and exe files");

        File ldFile = extractFile("ld");

        Path path = Files.createTempFile("winsw-merged", ".exe");

        getLog().debug(path.toString());

        // ld INPUT.o INPUT.exe -o OUTPUT.exe
        executeCommand(ldFile.getAbsolutePath(),
                "-mi386pe",
                "--oformat",
                "pei-i386",
                "--dynamicbase",
                "--nxcompat",
                "--no-seh",
                "-s",
                resFile.getAbsolutePath(),
                exeFile.getAbsolutePath(),
                "-o",
                path.toFile().getAbsolutePath());

        return path.toFile();
    }

    private void executeCommand(String... args) throws IOException, InterruptedException {
        getLog().debug(Arrays.toString(args));

        ProcessBuilder pb = new ProcessBuilder(args);

        pb.redirectErrorStream(true);

        Process process = pb.start();

        String output;

        try (InputStream is = process.getInputStream()) {
            output = IOUtils.toString(is, Charset.defaultCharset());
        }

        int exitCode = process.waitFor();

        if (exitCode != 0) {
            throw new IOException(output);
        }
    }

    private void copyFileToOutputDirectory(File filePath, String fileName) throws MojoExecutionException {
        getLog().debug("Copying file " + filePath);

        executeMojo(
                plugin(
                        groupId("org.apache.maven.plugins"),
                        artifactId("maven-resources-plugin"),
                        version("3.2.0")
                ),
                goal("copy-resources"),
                configuration(
                        element(name("outputDirectory"), outputDirectory.getAbsolutePath()),
                        element(name("resources"),
                                element("resource",
                                        element("directory", filePath.getParent()),
                                        element("include", filePath.getName()),
                                        element("filtering", "true")))
                ),
                executionEnvironment(
                        mavenProject,
                        mavenSession,
                        pluginManager
                )
        );

        File outputFile = new File(outputDirectory, fileName);

        try {
            FileUtils.moveFile(new File(outputDirectory, filePath.getName()), outputFile);
        } catch (IOException e) {
            throw new MojoExecutionException("Failed to move file " + outputFile, e);
        }

//        if (!filePath.exists()) {
//            throw new MojoExecutionException("File not found " + filePath);
//        }
//
//        File outputFile = new File(outputDirectory, fileName);
//
//        if (!outputFile.exists()) {
//            try {
//                FileUtils.copyFile(filePath, outputFile);
//            } catch (IOException e) {
//                throw new MojoExecutionException("Failed to copy file " + filePath, e);
//            }
//        }
//
//        return outputFile;
    }
}
