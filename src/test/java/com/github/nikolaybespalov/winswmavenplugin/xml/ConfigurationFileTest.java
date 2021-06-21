package com.github.nikolaybespalov.winswmavenplugin.xml;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ConfigurationFileTest {

    ConfigurationFile configurationFile;

    @Before
    public void setUp() {
        configurationFile = new ConfigurationFile();
        configurationFile.setId("id");
        configurationFile.setName("name");
        configurationFile.setDescription("description");
        configurationFile.setExecutable("executable");
        configurationFile.setArguments("arguments");
    }

    @Test
    public void getId() {
        assertEquals("id", configurationFile.getId());
    }

    @Test
    public void getName() {
        assertEquals("name", configurationFile.getName());
    }

    @Test
    public void getDescription() {
        assertEquals("description", configurationFile.getDescription());
    }

    @Test
    public void getExecutable() {
        assertEquals("executable", configurationFile.getExecutable());
    }

    @Test
    public void getArguments() {
        assertEquals("arguments", configurationFile.getArguments());
    }

}