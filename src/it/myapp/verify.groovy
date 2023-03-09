import org.apache.commons.lang3.SystemUtils

import static com.github.nikolaybespalov.winswmavenplugin.Utils.*

//noinspection GroovyAssignabilityCheck
File executableFile = new File(basedir, 'target/myapp-1.2.3.4.exe')

assert executableFile.exists()

if (SystemUtils.IS_OS_WINDOWS) {
    assert 'My App Description' == getFileInfoValue(executableFile, 'FileDescription')
    assert 'My App' == getFileInfoValue(executableFile, 'ProductName')
    assert 'John Doe' == getFileInfoValue(executableFile, 'CompanyName')
    assert '© 2019 John Doe All Rights Reserved' == getFileInfoValue(executableFile, 'LegalCopyright')
    assert '1.2.3.4' == getFileInfoValue(executableFile, 'ProductVersion')
    assert null == getFileInfoValue(executableFile, 'LegalTrademarks')
    assert '1.2.3.4' == getFileInfoValue(executableFile, 'FileVersion')

    assert execute(executableFile, 'install').exitValue() == 0
    assert execute(executableFile, 'uninstall').exitValue() == 0
}

File configurationFile = new File(basedir, 'target/myapp-1.2.3.4.xml')

assert configurationFile.exists()

assert 'myapp' == getXmlValue(configurationFile, '//service/id')
assert 'My App' == getXmlValue(configurationFile, '//service/name')
assert 'My App Description' == getXmlValue(configurationFile, '//service/description')
assert 'java' == getXmlValue(configurationFile, '//service/executable')
assert '-jar myapp-1.0-SNAPSHOT.jar' == getXmlValue(configurationFile, '//service/arguments')
