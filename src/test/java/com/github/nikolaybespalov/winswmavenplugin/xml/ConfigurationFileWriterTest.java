package com.github.nikolaybespalov.winswmavenplugin.xml;

import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;

import static org.junit.Assert.assertEquals;

public class ConfigurationFileWriterTest {
    @Test
    public void testWriteTo() throws IOException {
        ConfigurationFile configurationFile = new ConfigurationFile();
        configurationFile.setId("id");
        configurationFile.setName("name");
        configurationFile.setDescription("description");
        configurationFile.setExecutable("executable");
        configurationFile.setArguments("arguments");

        ConfigurationFileWriter configurationFileWriter = new ConfigurationFileWriter(configurationFile);

        StringWriter stringWriter = new StringWriter();

        configurationFileWriter.writeTo(stringWriter);

        assertEquals("" +
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "\n" +
                "<service>\n" +
                "  <id>id</id>\n" +
                "  <name>name</name>\n" +
                "  <description>description</description>\n" +
                "  <executable>executable</executable>\n" +
                "  <arguments>arguments</arguments>\n" +
                "</service>\n", stringWriter.toString());
    }

    @Test
    public void testWriteToWithDefaults() throws IOException {
        ConfigurationFile configurationFile = new ConfigurationFile();
        configurationFile.setId("id");
        configurationFile.setName("name");
        configurationFile.setExecutable("executable");
        configurationFile.setArguments("arguments");

        ConfigurationFileWriter configurationFileWriter = new ConfigurationFileWriter(configurationFile);

        StringWriter stringWriter = new StringWriter();

        configurationFileWriter.writeTo(stringWriter);

        assertEquals("" +
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "\n" +
                "<service>\n" +
                "  <id>id</id>\n" +
                "  <name>name</name>\n" +
                "  <executable>executable</executable>\n" +
                "  <arguments>arguments</arguments>\n" +
                "</service>\n", stringWriter.toString());
    }
}
