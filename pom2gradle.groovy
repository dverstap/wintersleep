#! /usr/bin/env groovy

def project = new XmlSlurper().parse(new File('pom.xml'))
def versionNodes = project.properties.children().findAll { it.name().endsWith('.version') }
def versions = [:]
for (versionNode in versionNodes) {
  versions[versionNode.name()] = versionNode.text()
}

println "apply plugin: 'java'"
println ""
println "dependencies {"

//for (dependency in project.dependencyManagement.dependencies.dependency) {
for (dependency in project.dependencies.dependency) {
  switch (dependency.scope) {
    case '':
      scope = 'compile'
      break
    case 'test':
      scope = 'testCompile'
      break
    default:
      scope = dependency.scope
  }
  print "  ${scope} '${dependency.groupId.text()}:${dependency.artifactId.text()}"
//  def version = dependency.version.text()
//  if (version.startsWith('${')) {
//    print versions[version[2..-2]]
//  }
//  else {
//    print version
//  }
  print '\''
  if (dependency.exclusions.children().size() != 0) {
    println ' {'
    for (exclusion in dependency.exclusions.exclusion) {
      println "    exclude group:'${exclusion.groupId.text()}', module:'${exclusion.artifactId.text()}'"
    }
    print '  }'
  }
  println ''
}

println "}"
