package com.github.nikolaybespalov.winswmavenplugin.xml;

import org.apache.maven.plugins.annotations.Parameter;

public class ConfigurationFile {
    /**
     * Put something informative here that a regular user can understand.
     */
    @Parameter(defaultValue = "asdasdasd")
    private String id;
    private String name;
    private String description;
    private String executable;
    private String arguments;

    public ConfigurationFile() {
    }

    public ConfigurationFile(String id, String name, String description, String executable, String arguments) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.executable = executable;
        this.arguments = arguments;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getExecutable() {
        return executable;
    }

    public void setExecutable(String executable) {
        this.executable = executable;
    }

    public String getArguments() {
        return arguments;
    }

    public void setArguments(String arguments) {
        this.arguments = arguments;
    }
}
