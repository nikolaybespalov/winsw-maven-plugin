package com.github.nikolaybespalov.winswmavenplugin.rc;

public class RcFile {
    private int lang;
    private int subLang;
    private String icon;
    private FileInfo fileInfo;

    public int getLang() {
        return lang;
    }

    public void setLang(int lang) {
        this.lang = lang;
    }

    public int getSubLang() {
        return subLang;
    }

    public void setSubLang(int subLang) {
        this.subLang = subLang;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public FileInfo getFileInfo() {
        return fileInfo;
    }

    public void setFileInfo(FileInfo fileInfo) {
        this.fileInfo = fileInfo;
    }
}
