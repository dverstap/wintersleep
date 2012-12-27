#!/usr/bin/env python

import sys
import os
import zipfile

from optparse import OptionParser

class Version99Generator(object):

    def __init__(self):
        self.version = "99.0-does-not-exist"

    def main(self):
        self._parse()
        self._createDirs()
        self._makeJar()

    def _parse(self):
        parser = OptionParser()
        parser.add_option("--repo-dir", dest="repoDir",
                          default="/udir/stappend/maven/repositories/99.0-does-not-exist")
        (self.options, self.args) = parser.parse_args()
        if len(self.args) != 2:
            self._usage()
            sys.exit(-1)
        self.groupId = self.args[0]
        self.artifactId = self.args[1]

    def _createDirs(self):
        self.targetDir = os.path.join(self.options.repoDir,
                                      self.groupId,
                                      self.artifactId,
                                      "99.0-does-not-exist")
        if not os.path.exists(self.targetDir):
            os.makedirs(self.targetDir)

    def _makeJar(self):
        name = "%s-%s.jar" % (self.artifactId, self.version)
        zout = zipfile.ZipFile(os.path.join(self.targetDir, name),
                               mode="w",
                               compression=zipfile.ZIP_DEFLATED)
        zout.writestr('META-INF/MANIFEST.MF',
                      'Manifest-Version: 1.0\n')
        zout.close()

    def _usage(self):
        print("Usage: %s [--repo-dir=<repodir>] <groupId> <artifactId>" % (sys.argv[0]))

if __name__ == "__main__":
    Version99Generator().main()
