import org.apache.commons.lang3.SystemUtils

import static com.github.nikolaybespalov.winswmavenplugin.Utils.*

//noinspection GroovyAssignabilityCheck
File executableFile = new File(basedir, 'target/complex-it2-1.0-SNAPSHOT.exe')

assert executableFile.exists()

if (SystemUtils.IS_OS_WINDOWS) {
    assert 'A IT verifying the complex use case (existing config xml file).' == getFileInfoValue(executableFile, 'Comments')
    assert 'complex-it2' == getFileInfoValue(executableFile, 'InternalName')
    assert 'complex-it2' == getFileInfoValue(executableFile, 'ProductName')
    assert 'Nikolay Bespalov' == getFileInfoValue(executableFile, 'CompanyName')
    assert '© 2019 Nikolay Bespalov All Rights Reserved' == getFileInfoValue(executableFile, 'LegalCopyright')
    assert '1.0-SNAPSHOT' == getFileInfoValue(executableFile, 'ProductVersion')
    assert 'A IT verifying the complex use case (existing config xml file).' == getFileInfoValue(executableFile, 'FileDescription')
    assert null == getFileInfoValue(executableFile, 'LegalTrademarks')
    assert '1.0-SNAPSHOT' == getFileInfoValue(executableFile, 'FileVersion')
    assert 'complex-it2-1.0-SNAPSHOT.exe' == getFileInfoValue(executableFile, 'OriginalFilename')

    assert execute(executableFile, 'install').exitValue() == 0
    assert execute(executableFile, 'uninstall').exitValue() == 0
}

File configurationFile = new File(basedir, 'target/complex-it2-1.0-SNAPSHOT.xml')

assert configurationFile.exists()

assert 'myapp' == getXmlValue(configurationFile, '//service/id')
assert 'My App' == getXmlValue(configurationFile, '//service/name')
assert 'My App Description' == getXmlValue(configurationFile, '//service/description')
assert 'java' == getXmlValue(configurationFile, '//service/executable')
assert '-jar complex-it2-1.0-SNAPSHOT.jar' == getXmlValue(configurationFile, '//service/arguments')
