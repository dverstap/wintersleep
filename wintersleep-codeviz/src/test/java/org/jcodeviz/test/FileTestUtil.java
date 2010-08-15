/*
 * Copyright 2009 Davy Verstappen.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jcodeviz.test;

import static junit.framework.Assert.assertTrue;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

public abstract class FileTestUtil {

    public static File getTargetDir(Class testClass) {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        URL url = cl.getResource(testClass.getName().replace('.', '/') + ".class");
        if (url == null) {
            throw new IllegalArgumentException("class file not found for: " + testClass);
        }
        File classFile;
        try {
            classFile = new File(url.toURI());
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException(e);
        }
        if (!classFile.exists()) {
            throw new IllegalStateException(classFile + " does not exist.");
        }
        File dir = classFile.getParentFile();
        while (dir != null) {
            if (dir.getName().equals("target")) {
                return dir;
            }
            dir = dir.getParentFile();
        }
        throw new IllegalStateException("Could not find 'target' dir as a parent of " + classFile);
    }

    @SuppressWarnings({"ResultOfMethodCallIgnored"})
    public static File makeOutputDir(Class testClass) {
        File targetDir = getTargetDir(testClass);
        File result = new File(targetDir, "test-diagrams");
        result.mkdirs();
        return result;
    }

    public static void assertCreated(File file) {
        assertTrue("image file not created:" + file, file.exists());
        assertTrue(file + " size is " + file.length(), file.length() > 0);
    }

}