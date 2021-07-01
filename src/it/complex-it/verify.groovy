import org.apache.commons.lang3.SystemUtils

import static com.github.nikolaybespalov.winswmavenplugin.Utils.execute
import static com.github.nikolaybespalov.winswmavenplugin.Utils.getFileInfoValue
import static com.github.nikolaybespalov.winswmavenplugin.Utils.getXmlValue

//noinspection GroovyAssignabilityCheck
File executableFile = new File(basedir, 'target/complex-it-1.0-SNAPSHOT.exe')

assert executableFile.exists()

if (SystemUtils.IS_OS_WINDOWS) {
    assert 'A IT verifying the complex use case.' == getFileInfoValue(executableFile, 'Comments')
    assert 'complex-it' == getFileInfoValue(executableFile, 'InternalName')
    assert 'complex-it' == getFileInfoValue(executableFile, 'ProductName')
    assert 'Nikolay Bespalov' == getFileInfoValue(executableFile, 'CompanyName')
    assert '© 2019 Nikolay Bespalov All Rights Reserved' == getFileInfoValue(executableFile, 'LegalCopyright')
    assert '1.0-SNAPSHOT' == getFileInfoValue(executableFile, 'ProductVersion')
    assert 'A IT verifying the complex use case.' == getFileInfoValue(executableFile, 'FileDescription')
    assert null == getFileInfoValue(executableFile, 'LegalTrademarks')
    assert '1.0-SNAPSHOT' == getFileInfoValue(executableFile, 'FileVersion')
    assert 'complex-it-1.0-SNAPSHOT.exe' == getFileInfoValue(executableFile, 'OriginalFilename')

    assert execute(executableFile, 'install').exitValue() == 0
    assert execute(executableFile, 'start').exitValue() == 0
    assert execute(executableFile, 'stop').exitValue() == 0
    assert execute(executableFile, 'uninstall').exitValue() == 0
}

File configurationFile = new File(basedir, 'target/complex-it-1.0-SNAPSHOT.xml')

assert configurationFile.exists()

assert 'complex-it' == getXmlValue(configurationFile, '//service/id')
assert 'complex-it' == getXmlValue(configurationFile, '//service/name')
assert 'A IT verifying the complex use case.' == getXmlValue(configurationFile, '//service/description')
assert 'java' == getXmlValue(configurationFile, '//service/executable')
assert '-jar complex-it-1.0-SNAPSHOT.jar' == getXmlValue(configurationFile, '//service/arguments')
