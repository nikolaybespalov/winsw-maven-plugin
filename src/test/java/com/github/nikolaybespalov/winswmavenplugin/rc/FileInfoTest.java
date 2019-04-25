package com.github.nikolaybespalov.winswmavenplugin.rc;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class FileInfoTest {
    private FileInfo fileInfo;

    @Before
    public void setUp() {
        fileInfo = new FileInfo();
        fileInfo.setFileVersion("1.2.3.4");
    }

    @Test
    public void testGetFileVersion() {
        assertEquals("1.2.3.4", fileInfo.getFileVersion());
    }
}