#! /usr/bin/env groovy

def project = new XmlSlurper().parse(new File('pom.xml'))

for (dependency in project.dependencyManagement.dependencies.dependency) {
  def groupId = dependency.groupId.text()
  def artifactId = dependency.artifactId.text()
  def version = dependency.version.text()
  println "'${groupId}:${artifactId}:${version}',"
}
