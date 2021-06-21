package com.github.nikolaybespalov.winswmavenplugin.rc;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.Writer;

public class RcFileWriter {
    private final RcFile rcFile;

    public RcFileWriter(RcFile rcFile) {
        this.rcFile = rcFile;
    }

    public void writeTo(Writer writer) throws IOException {
        writeHeader(writer);
        writer.append("\n\n");

        writeVersionInfo(writer);
        writer.append("\n\n");

        writeIcon(writer);
    }

    private void writeHeader(Writer writer) throws IOException {
        writer.append(String.format("LANGUAGE %s, %s", rcFile.getLang(), rcFile.getSubLang()));
    }

    private void writeVersionInfo(Writer writer) throws IOException {
        FileInfo fi = rcFile.getFileInfo();

        writer.append("1 VERSIONINFO\n");
        writer.append(String.format(" FILEVERSION %s\n", prepareVersion(fi.getFileVersion())));
        writer.append(String.format(" PRODUCTVERSION %s\n", prepareVersion(fi.getProductVersion())));
        writer.append(" FILEFLAGSMASK 0x0\n");
        writer.append(" FILEOS 0x4\n");
        writer.append(" FILETYPE 0x1\n");
        writer.append("BEGIN\n");
        writer.append("  BLOCK ").append("\"StringFileInfo\"\n");
        writer.append("  BEGIN\n");
        writer.append("    BLOCK ").append("\"000004b0\"\n");
        writer.append("    BEGIN\n");

        writeStringValue(writer, "Comments", fi.getComments());
        writeStringValue(writer, "CompanyName", fi.getCompanyName());
        writeStringValue(writer, "FileDescription", fi.getFileDescription());
        writeStringValue(writer, "FileVersion", fi.getTxtFileVersion());
        writeStringValue(writer, "InternalName", fi.getInternalName());
        writeStringValue(writer, "LegalCopyright", fi.getCopyright());
        writeStringValue(writer, "LegalTrademarks", fi.getTrademarks());
        writeStringValue(writer, "OriginalFilename", fi.getOriginalFilename());
        writeStringValue(writer, "ProductName", fi.getProductName());
        writeStringValue(writer, "ProductVersion", fi.getTxtProductVersion());

        writer.append("    END\n");
        writer.append("  END\n");

        writer.append("  BLOCK \"VarFileInfo\"\n");
        writer.append("  BEGIN\n");
        writer.append("    VALUE \"Translation\", 0x0, 0x04b0\n");
        writer.append("  END\n");
        writer.append("END");
    }

    private void writeIcon(Writer writer) throws IOException {
        if (StringUtils.isEmpty(rcFile.getIcon())) {
            return;
        }

        writer.append(String.format("1 ICON DISCARDABLE \"%s\"", FilenameUtils.separatorsToUnix(rcFile.getIcon())));
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

    private void writeStringValue(Writer writer, String key, String value) throws IOException {
        if (value == null) {
            return;
        }

        writer.append(String.format("      VALUE \"%s\", \"%s\"", key, value));
        writer.append("\n");
    }
}
