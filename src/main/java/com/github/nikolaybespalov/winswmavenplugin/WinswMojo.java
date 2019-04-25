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

import static org.twdata.maven.mojoexecutor.MojoExecutor.*;

@Mojo(name = "winsw", defaultPhase = LifecyclePhase.PREPARE_PACKAGE)
public class WinswMojo extends AbstractMojo {
    @Parameter(defaultValue = "${project}", readonly = true)
    private MavenProject mavenProject;

    @Parameter(defaultValue = "${session}", readonly = true)
    private MavenSession mavenSession;

    @Component
    private BuildPluginManager pluginManager;

    @Parameter(defaultValue = "${project.build.directory}", required = true)
    private File outputDirectory;

    @Parameter(defaultValue = "2.2.0")
    private String winswVersion;

    @Parameter(defaultValue = "${project.build.finalName}.exe")
    private String executableFileName;

    @Parameter(defaultValue = "${project.build.finalName}.xml")
    private String configurationFileName;

    @Parameter
    private File configurationFilePath;

    @Parameter
    private ConfigurationFile configurationFile;

    @Parameter
    private RcFile rcFile;

    @Override
    public void execute() throws MojoExecutionException {
        if (!outputDirectory.exists()) {
            outputDirectory.mkdirs();
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
        getLog().debug("Copying configuration file " + configurationFilePath);

        if (!configurationFilePath.exists()) {
            throw new MojoExecutionException("File not found " + configurationFilePath);
        }

        File outputConfigurationFile = new File(outputDirectory, configurationFileName);

        if (!outputConfigurationFile.exists()) {
            try {
                FileUtils.copyFile(configurationFilePath, outputConfigurationFile);
            } catch (IOException e) {
                throw new MojoExecutionException("Failed to copy file " + configurationFilePath, e);
            }
        }
    }

    private void processExeFile() throws MojoExecutionException {
        File rcFile;

        try {
            rcFile = writeRcFile();
        } catch (IOException e) {
            throw new MojoExecutionException("Failed to write rc file", e);
        }

        File resFile;

        try {
            resFile = buildResFile(rcFile);
        } catch (IOException | InterruptedException e) {
            throw new MojoExecutionException("Failed to build res file", e);
        }

        File exeFile;

        try {
            exeFile = downloadWinswBinArtifact();
        } catch (IOException e) {
            throw new MojoExecutionException("Failed to remove .rsrc section", e);
        }

        File resultExeFile;

        try {
            resultExeFile = mergeResFileAndExeFile(resFile, exeFile);
        } catch (IOException | InterruptedException e) {
            throw new MojoExecutionException("Failed to merge exe and res file", e);
        }

        File destExeFile = new File(outputDirectory, executableFileName);

        try {
            FileUtils.copyFile(resultExeFile, destExeFile);
        } catch (IOException e) {
            throw new MojoExecutionException("Failed to copy exe res file", e);
        }

        getLog().info("The file " + destExeFile + " was successfully created.");
    }

    private File downloadWinswBinArtifact() throws IOException, MojoExecutionException {
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
        } else {
            throw new IOException("Unsupported platform");
        }

        try (InputStream is = getClass().getResourceAsStream(resourceName)) {
            if (is == null) {
                throw new IOException("Failed to getResourceAsStream for " + resourceName);
            }

            Path path = Files.createTempFile(name, ".exe");

            FileUtils.copyInputStreamToFile(is, path.toFile());

            return path.toFile();
        }
    }

    private File writeRcFile() throws IOException {
        Path path = Files.createTempFile("winsw", ".rc");

        getLog().debug(path.toString());

        if (rcFile == null) {
            rcFile = new RcFile();
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
        File windresFile = extractFile("windres");

        Path path = Files.createTempFile("winsw", ".o");

        getLog().debug(path.toString());

        // windres --preprocessor=type -J rc -O coff -F pe-i386 INPUT.rc OUTPUT.o
        executeCommand(windresFile.getAbsolutePath(),
                "--preprocessor=type",
                "-O",
                "coff",
                "-F",
                "pe-i386",
                rcFile.getAbsolutePath(),
                path.toFile().getAbsolutePath());

        return path.toFile();
    }

    private File mergeResFileAndExeFile(File resFile, File exeFile) throws IOException, InterruptedException {
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
                "--unique=.rsrc",
                resFile.getAbsolutePath(),
                exeFile.getAbsolutePath(),
                "-o",
                path.toFile().getAbsolutePath());

        return path.toFile();
    }

    private static void executeCommand(String... args) throws IOException, InterruptedException {
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
}
