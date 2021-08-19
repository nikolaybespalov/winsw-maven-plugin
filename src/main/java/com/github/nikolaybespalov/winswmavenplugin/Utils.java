package com.github.nikolaybespalov.winswmavenplugin;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import sun.net.www.protocol.file.FileURLConnection;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class Utils {
    public static void copyResourcesRecursively(URL originUrl, File destination) throws IOException {
        URLConnection urlConnection = originUrl.openConnection();

        if (urlConnection instanceof JarURLConnection) {
            copyJarResourcesRecursively(destination, (JarURLConnection) urlConnection);
        } else if (urlConnection instanceof FileURLConnection) {
            FileUtils.copyDirectory(new File(originUrl.getPath()), destination);
        } else {
            throw new IOException("URLConnection[" + urlConnection.getClass().getSimpleName() +
                    "] is not a recognized/implemented connection type.");
        }
    }

    public static void copyJarResourcesRecursively(File destination, JarURLConnection jarConnection) throws IOException {
        JarFile jarFile = jarConnection.getJarFile();
        Enumeration<JarEntry> entries = jarFile.entries();

        while (entries.hasMoreElements()) {
            JarEntry entry = entries.nextElement();

            if (entry.getName().startsWith(jarConnection.getEntryName())) {
                String fileName = StringUtils.removeStart(entry.getName(), jarConnection.getEntryName());

                if (!entry.isDirectory()) {
                    InputStream entryInputStream = null;

                    try {
                        entryInputStream = jarFile.getInputStream(entry);
                        FileUtils.copyInputStreamToFile(entryInputStream, new File(destination, fileName));
                    } finally {
                        IOUtils.closeQuietly(entryInputStream, null);
                    }
                }
            }
        }
    }
}
