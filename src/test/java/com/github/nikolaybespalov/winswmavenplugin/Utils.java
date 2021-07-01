package com.github.nikolaybespalov.winswmavenplugin;

import com.sun.jna.Memory;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.Version;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.PointerByReference;
import org.apache.commons.io.IOUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class Utils {
    public static String f() {
        return "Hello Groovy";
    }

    public static String getXmlValue(File file, String q) throws DocumentException {
        SAXReader reader = new SAXReader();
        Document document = reader.read(file);

        String v = document.valueOf(q);

        if (v.isEmpty()) {
            return null;
        }

        return v;
    }

    public static String getFileInfoValue(File file, String section) {
        IntByReference dwDummy = new IntByReference();
        dwDummy.setValue(0);
        int fileVersionInfoSize = Version.INSTANCE.GetFileVersionInfoSize(file.getAbsolutePath(), dwDummy);

        assert fileVersionInfoSize > 0;

        Pointer lpData = new Memory(fileVersionInfoSize);

        boolean fileVersionInfoResult =
                Version.INSTANCE.GetFileVersionInfo(
                        file.getAbsolutePath(), 0, fileVersionInfoSize, lpData);

        assert fileVersionInfoResult;

        StringBuilder subBlock = new StringBuilder();
        subBlock.append("\\StringFileInfo\\");
        subBlock.append("0000");
        subBlock.append("04b0");
        subBlock.append("\\");
        subBlock.append(section);

        PointerByReference lpBuffer = new PointerByReference();
        IntByReference dwBytes = new IntByReference();

        boolean verQueryVal = Version.INSTANCE.VerQueryValue(
                lpData, subBlock.toString(), lpBuffer, dwBytes);

        if (!verQueryVal) {
            return null;
        }

        byte[] description =
                lpBuffer.getValue().getByteArray(0, (dwBytes.getValue() - 1) * 2);

        return new String(description, StandardCharsets.UTF_16LE);
    }

    public static Process execute(File executableFile, String command) throws IOException, InterruptedException {
        Process p = new ProcessBuilder(executableFile.getAbsolutePath(), command).start();

        p.waitFor();

        String stderr = IOUtils.toString(p.getErrorStream(), Charset.defaultCharset());
        String stdout = IOUtils.toString(p.getInputStream(), Charset.defaultCharset());

        System.out.println(stderr);
        System.out.println(stdout);

        return p;
    }
}
