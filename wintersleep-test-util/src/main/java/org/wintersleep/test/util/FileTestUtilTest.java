package org.wintersleep.test.util;

import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertEquals;

public class FileTestUtilTest {

    @Test
    public void changeExtension() {
        File origFile = new File("file.originalextension");
        File newFile = FileTestUtil.changeExtension(origFile, "newextension");
        assertEquals(new File("file.newextension"), newFile);
    }

    @Test
    public void changeExtensionOfFileNameWithoutExtension() {
        File origFile = new File("file");
        File newFile = FileTestUtil.changeExtension(origFile, "newextension");
        assertEquals(new File("file.newextension"), newFile);
    }
}
