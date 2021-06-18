import com.sun.jna.Memory
import com.sun.jna.Pointer
import com.sun.jna.platform.win32.Version
import com.sun.jna.ptr.IntByReference
import com.sun.jna.ptr.PointerByReference
import org.apache.commons.lang3.SystemUtils
import org.dom4j.Document
import org.dom4j.io.SAXReader

import java.nio.charset.StandardCharsets

File executableFile = new File(basedir, 'target/complex-it3-1.0-SNAPSHOT.exe')

assert executableFile.exists()

if (SystemUtils.IS_OS_WINDOWS) {
    assert 'A IT verifying the complex use case (classifier: net4).' == getFileInfoValue(executableFile, 'Comments')
    assert 'complex-it3' == getFileInfoValue(executableFile, 'InternalName')
    assert 'complex-it3' == getFileInfoValue(executableFile, 'ProductName')
    assert 'Nikolay Bespalov' == getFileInfoValue(executableFile, 'CompanyName')
    assert '© 2019 Nikolay Bespalov All Rights Reserved' == getFileInfoValue(executableFile, 'LegalCopyright')
    assert '1.0-SNAPSHOT' == getFileInfoValue(executableFile, 'ProductVersion')
    assert 'A IT verifying the complex use case (classifier: net4).' == getFileInfoValue(executableFile, 'FileDescription')
    assert null == getFileInfoValue(executableFile, 'LegalTrademarks')
    assert '1.0-SNAPSHOT' == getFileInfoValue(executableFile, 'FileVersion')
    assert 'complex-it3-1.0-SNAPSHOT.exe' == getFileInfoValue(executableFile, 'OriginalFilename')

    def proc = (executableFile.getAbsolutePath() + ' install').execute()
    proc.waitForOrKill(10000)
    assert proc.exitValue() == 0

    def proc2 = (executableFile.getAbsolutePath() + ' uninstall').execute()
    proc2.waitForOrKill(10000)
    assert proc2.exitValue() == 0
}

File configurationFile = new File(basedir, 'target/complex-it3-1.0-SNAPSHOT.xml')

assert configurationFile.exists()

assert 'complex-it3' == getXmlValue(configurationFile, '//service/id')
assert 'complex-it3' == getXmlValue(configurationFile, '//service/name')
assert 'A IT verifying the complex use case (classifier: net4).' == getXmlValue(configurationFile, '//service/description')
assert 'java' == getXmlValue(configurationFile, '//service/executable')
assert '-jar complex-it3-1.0-SNAPSHOT.jar' == getXmlValue(configurationFile, '//service/arguments')

static def getXmlValue(File file, String q) {
    SAXReader reader = new SAXReader()
    Document document = reader.read(file)

    String v = document.valueOf(q)

    if (v == "") {
        return null
    }

    return v
}

static def getFileInfoValue(File file, String section) {
    IntByReference dwDummy = new IntByReference()
    dwDummy.setValue(0)
    int fileVersionInfoSize = Version.INSTANCE.GetFileVersionInfoSize(file.getAbsolutePath(), dwDummy)

    assert fileVersionInfoSize > 0

    Pointer lpData = new Memory(fileVersionInfoSize)

    boolean fileVersionInfoResult =
            Version.INSTANCE.GetFileVersionInfo(
                    file.getAbsolutePath(), 0, fileVersionInfoSize, lpData)

    assert fileVersionInfoResult

    StringBuilder subBlock = new StringBuilder()
    subBlock.append("\\StringFileInfo\\")
    subBlock.append("0000")
    subBlock.append("04b0")
    subBlock.append("\\")
    subBlock.append(section)

    PointerByReference lpBuffer = new PointerByReference()
    IntByReference dwBytes = new IntByReference()

    boolean verQueryVal = Version.INSTANCE.VerQueryValue(
            lpData, subBlock.toString(), lpBuffer, dwBytes)

    if (!verQueryVal) {
        return null
    }

    byte[] description =
            lpBuffer.getValue().getByteArray(0, (dwBytes.getValue() - 1) * 2)

    return new String(description, StandardCharsets.UTF_16LE)
}
