package com.github.nikolaybespalov.winswmavenplugin.rc;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class RcFileTest {
    private RcFile rcFile;
    private FileInfo fileInfo;

    @Before
    public void setUp() {
        rcFile = new RcFile();
        rcFile.setLang(1);
        rcFile.setSubLang(2);
        rcFile.setIcon("myicon.ico");
        rcFile.setFileInfo(fileInfo = new FileInfo());
    }

    @Test
    public void testGetLang() {
        assertEquals(1, rcFile.getLang());
    }

    @Test
    public void testGetSubLang() {
        assertEquals(2, rcFile.getSubLang());
    }

    @Test
    public void testGetIcon() {
        assertEquals("myicon.ico", rcFile.getIcon());
    }

    @Test
    public void testGetVersionInfo() {
        assertEquals(fileInfo, rcFile.getFileInfo());
    }
}