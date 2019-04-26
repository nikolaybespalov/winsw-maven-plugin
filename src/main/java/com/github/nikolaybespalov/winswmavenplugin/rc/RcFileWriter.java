package com.github.nikolaybespalov.winswmavenplugin.rc;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.Writer;

public class RcFileWriter {
    private RcFile rcFile;

    public RcFileWriter(RcFile rcFile) {
        this.rcFile = rcFile;
    }

    public void writeTo(Writer writer) throws IOException {
        writeHeader(writer);
        writer.append("\n");
        writer.append("\n");

        writeVersionInfo(writer);
        writer.append("\n");
        writer.append("\n");

        writeIcon(writer);
    }

    private void writeHeader(Writer writer) throws IOException {
        writer.append("LANGUAGE ").append(String.valueOf(rcFile.getLang())).append(", ").append(String.valueOf(rcFile.getSubLang()));
    }

    private void writeVersionInfo(Writer writer) throws IOException {
        FileInfo fi = rcFile.getFileInfo();

        writer.append("1 VERSIONINFO");
        writer.append("\n");
        writer.append(" FILEVERSION ").append(prepareVersion(fi.getFileVersion()));
        writer.append("\n");
        writer.append(" PRODUCTVERSION ").append(prepareVersion(fi.getProductVersion()));
        writer.append("\n");
        writer.append(" FILEFLAGSMASK 0x0");
        writer.append("\n");
        writer.append(" FILEOS 0x4");
        writer.append("\n");
        writer.append(" FILETYPE 0x1");
        writer.append("\n");
        writer.append("BEGIN");
        writer.append("\n");
        writer.append("  BLOCK ").append("\"StringFileInfo\"");
        writer.append("\n");
        writer.append("  BEGIN");
        writer.append("\n");
        writer.append("    BLOCK ").append("\"000004b0\"");
        writer.append("\n");
        writer.append("    BEGIN");
        writer.append("\n");
        if (fi.getComments() != null) {
            writer.append("      VALUE ").append("\"Comments\", ").append("\"").append(fi.getComments()).append("\"");
            writer.append("\n");
        }
        if (fi.getCompanyName() != null) {
            writer.append("      VALUE ").append("\"CompanyName\", ").append("\"").append(fi.getCompanyName()).append("\"");
            writer.append("\n");
        }
        if (fi.getFileDescription() != null) {
            writer.append("      VALUE ").append("\"FileDescription\", ").append("\"").append(fi.getFileDescription()).append("\"");
            writer.append("\n");
        }
        writer.append("      VALUE ").append("\"FileVersion\", ").append("\"").append(fi.getTxtFileVersion()).append("\"");
        writer.append("\n");
        if (fi.getInternalName() != null) {
            writer.append("      VALUE ").append("\"InternalName\", ").append("\"").append(fi.getInternalName()).append("\"");
            writer.append("\n");
        }
        if (fi.getCopyright() != null) {
            writer.append("      VALUE ").append("\"LegalCopyright\", ").append("\"").append(fi.getCopyright()).append("\"");
            writer.append("\n");
        }
        if (fi.getTrademarks() != null) {
            writer.append("      VALUE ").append("\"LegalTrademarks\", ").append("\"").append(fi.getTrademarks()).append("\"");
            writer.append("\n");
        }
        if (fi.getOriginalFilename() != null) {
            writer.append("      VALUE ").append("\"OriginalFilename\", ").append("\"").append(fi.getOriginalFilename()).append("\"");
            writer.append("\n");
        }
        if (fi.getProductName() != null) {
            writer.append("      VALUE ").append("\"ProductName\", ").append("\"").append(fi.getProductName()).append("\"");
            writer.append("\n");
        }
        writer.append("      VALUE ").append("\"ProductVersion\", ").append("\"").append(fi.getTxtProductVersion()).append("\"");
        writer.append("\n");
        writer.append("    END");
        writer.append("\n");
        writer.append("  END");
        writer.append("\n");
        writer.append("  BLOCK ").append("\"VarFileInfo\"");
        writer.append("\n");
        writer.append("  BEGIN");
        writer.append("\n");
        writer.append("    VALUE ").append("\"Translation\", 0x0, 0x04b0");
        writer.append("\n");
        writer.append("  END");
        writer.append("\n");
        writer.append("END");
    }

    private void writeIcon(Writer writer) throws IOException {
        if (rcFile.getIcon() == null || rcFile.getIcon().equals("")) {
            return;
        }

        writer.append("1 ICON DISCARDABLE ").append("\"").append(FilenameUtils.separatorsToUnix(rcFile.getIcon())).append("\"");
    }

    private String prepareVersion(String version) {
        return complementVersion(version.replaceAll("-SNAPSHOT", "").replaceAll("\\.", ", "));
    }

    private String complementVersion(String version) {
        StringBuilder versionBuilder = new StringBuilder(version);

        while (StringUtils.countMatches(versionBuilder, ',') < 3) {
            versionBuilder.append(", 0");
        }

        return versionBuilder.toString();
    }
}
