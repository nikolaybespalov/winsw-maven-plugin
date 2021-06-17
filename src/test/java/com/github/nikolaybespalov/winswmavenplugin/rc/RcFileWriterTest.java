package com.github.nikolaybespalov.winswmavenplugin.rc;

import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;

import static org.junit.Assert.assertEquals;

public class RcFileWriterTest {
    @Test
    public void testWriteTo() throws IOException {
        RcFile rcFile = new RcFile();
        rcFile.setLang(1);
        rcFile.setSubLang(2);
        rcFile.setIcon("java.ico");
        FileInfo fileInfo;
        rcFile.setFileInfo(fileInfo = new FileInfo());
        fileInfo.setFileVersion("1.2.3.4");
        fileInfo.setProductVersion("5.6.7.8");
        fileInfo.setComments("Comments");
        fileInfo.setCompanyName("Company Name");
        fileInfo.setFileDescription("File description");
        fileInfo.setTxtFileVersion("1.2.3.4");
        fileInfo.setInternalName("Internal Name");
        fileInfo.setCopyright("Copyright");
        fileInfo.setTrademarks("Trademarks");
        fileInfo.setOriginalFilename("Original Filename");
        fileInfo.setProductName("Product Name");
        fileInfo.setTxtProductVersion("5.6.7.8");

        RcFileWriter rcFileWriter = new RcFileWriter(rcFile);

        StringWriter stringWriter = new StringWriter();

        rcFileWriter.writeTo(stringWriter);

        assertEquals("" +
                "LANGUAGE 1, 2\n" +
                "\n" +
                "1 VERSIONINFO\n" +
                " FILEVERSION 1, 2, 3, 4\n" +
                " PRODUCTVERSION 5, 6, 7, 8\n" +
                " FILEFLAGSMASK 0x0\n" +
                " FILEOS 0x4\n" +
                " FILETYPE 0x1\n" +
                "BEGIN\n" +
                "  BLOCK \"StringFileInfo\"\n" +
                "  BEGIN\n" +
                "    BLOCK \"000004b0\"\n" +
                "    BEGIN\n" +
                "      VALUE \"Comments\", \"Comments\"\n" +
                "      VALUE \"CompanyName\", \"Company Name\"\n" +
                "      VALUE \"FileDescription\", \"File description\"\n" +
                "      VALUE \"FileVersion\", \"1.2.3.4\"\n" +
                "      VALUE \"InternalName\", \"Internal Name\"\n" +
                "      VALUE \"LegalCopyright\", \"Copyright\"\n" +
                "      VALUE \"LegalTrademarks\", \"Trademarks\"\n" +
                "      VALUE \"OriginalFilename\", \"Original Filename\"\n" +
                "      VALUE \"ProductName\", \"Product Name\"\n" +
                "      VALUE \"ProductVersion\", \"5.6.7.8\"\n" +
                "    END\n" +
                "  END\n" +
                "  BLOCK \"VarFileInfo\"\n" +
                "  BEGIN\n" +
                "    VALUE \"Translation\", 0x0, 0x04b0\n" +
                "  END\n" +
                "END\n" +
                "\n" +
                "1 ICON DISCARDABLE \"java.ico\"", stringWriter.toString());
    }

    @Test
    public void testWriteToWithDefaults() throws IOException {
        RcFile rcFile = new RcFile();
        FileInfo fileInfo;
        rcFile.setFileInfo(fileInfo = new FileInfo());
        fileInfo.setFileVersion("1.2");
        fileInfo.setProductVersion("5.6");

        RcFileWriter rcFileWriter = new RcFileWriter(rcFile);

        StringWriter stringWriter = new StringWriter();

        rcFileWriter.writeTo(stringWriter);

        assertEquals("" +
                "LANGUAGE 0, 0\n" +
                "\n" +
                "1 VERSIONINFO\n" +
                " FILEVERSION 1, 2, 0, 0\n" +
                " PRODUCTVERSION 5, 6, 0, 0\n" +
                " FILEFLAGSMASK 0x0\n" +
                " FILEOS 0x4\n" +
                " FILETYPE 0x1\n" +
                "BEGIN\n" +
                "  BLOCK \"StringFileInfo\"\n" +
                "  BEGIN\n" +
                "    BLOCK \"000004b0\"\n" +
                "    BEGIN\n" +
                "    END\n" +
                "  END\n" +
                "  BLOCK \"VarFileInfo\"\n" +
                "  BEGIN\n" +
                "    VALUE \"Translation\", 0x0, 0x04b0\n" +
                "  END\n" +
                "END\n" +
                "\n", stringWriter.toString());
    }
}