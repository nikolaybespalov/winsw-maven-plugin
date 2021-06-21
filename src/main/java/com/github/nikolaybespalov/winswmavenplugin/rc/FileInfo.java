package com.github.nikolaybespalov.winswmavenplugin.rc;

import org.apache.maven.plugins.annotations.Parameter;

public class FileInfo {
    /**
     * Put something informative here that a regular user can understand.
     */
    @Parameter(defaultValue = "asdasdasd")
    private String fileVersion;
    private String productVersion;
    private String comments;
    private String companyName;
    private String fileDescription;
    private String txtFileVersion;
    private String internalName;
    private String copyright;
    private String trademarks;
    private String originalFilename;
    private String productName;
    private String txtProductVersion;

    public FileInfo(String fileVersion, String productVersion, String txtFileVersion, String internalName, String originalFilename, String productName, String txtProductVersion) {
        this.fileVersion = fileVersion;
        this.productVersion = productVersion;
        this.txtFileVersion = txtFileVersion;
        this.internalName = internalName;
        this.originalFilename = originalFilename;
        this.productName = productName;
        this.txtProductVersion = txtProductVersion;
    }

    public String getFileVersion() {
        return fileVersion;
    }

    public void setFileVersion(String fileVersion) {
        this.fileVersion = fileVersion;
    }

    public String getProductVersion() {
        return productVersion;
    }

    public void setProductVersion(String productVersion) {
        this.productVersion = productVersion;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getFileDescription() {
        return fileDescription;
    }

    public void setFileDescription(String fileDescription) {
        this.fileDescription = fileDescription;
    }

    public String getTxtFileVersion() {
        return txtFileVersion;
    }

    public void setTxtFileVersion(String txtFileVersion) {
        this.txtFileVersion = txtFileVersion;
    }

    public String getInternalName() {
        return internalName;
    }

    public void setInternalName(String internalName) {
        this.internalName = internalName;
    }

    public String getCopyright() {
        return copyright;
    }

    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }

    public String getTrademarks() {
        return trademarks;
    }

    public void setTrademarks(String trademarks) {
        this.trademarks = trademarks;
    }

    public String getOriginalFilename() {
        return originalFilename;
    }

    public void setOriginalFilename(String originalFilename) {
        this.originalFilename = originalFilename;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getTxtProductVersion() {
        return txtProductVersion;
    }

    public void setTxtProductVersion(String txtProductVersion) {
        this.txtProductVersion = txtProductVersion;
    }
}
