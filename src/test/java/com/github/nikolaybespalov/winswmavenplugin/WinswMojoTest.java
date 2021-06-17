package com.github.nikolaybespalov.winswmavenplugin;

import org.apache.maven.plugin.testing.AbstractMojoTestCase;

import java.io.File;

public class WinswMojoTest extends AbstractMojoTestCase {
    protected void setUp() throws Exception {
        super.setUp();
    }

    public void testMojoGoal() throws Exception {
        File testPom = new File(getBasedir(),
                "src/test/resources/unit/basic-test/basic-test-plugin-config.xml");

        WinswMojo mojo = (WinswMojo) lookupMojo("winsw", testPom);

        assertNotNull(mojo);
    }
}