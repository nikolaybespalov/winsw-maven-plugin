package com.github.nikolaybespalov.winswmavenplugin.xml;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import java.io.IOException;
import java.io.Writer;

public class ConfigurationFileWriter {
    private final ConfigurationFile configurationFile;

    public ConfigurationFileWriter(ConfigurationFile configurationFile) {
        this.configurationFile = configurationFile;
    }

    public void writeTo(Writer writer) throws IOException {
        Document document = DocumentHelper.createDocument();
        Element root = document.addElement("service");

        root.addElement("id")
                .addText(configurationFile.getId());

        root.addElement("name")
                .addText(configurationFile.getName());

        if (configurationFile.getDescription() != null) {
            root.addElement("description")
                    .addText(configurationFile.getDescription());
        }

        root.addElement("executable")
                .addText(configurationFile.getExecutable());

        root.addElement("arguments")
                .addText(configurationFile.getArguments());

        XMLWriter xmlWriter = new XMLWriter(writer, OutputFormat.createPrettyPrint());
        xmlWriter.write(document);
        xmlWriter.close();
    }
}
