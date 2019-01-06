package de.highbyte_le.semver

import org.gradle.testkit.runner.GradleRunner
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Specification

import static org.gradle.testkit.runner.TaskOutcome.SUCCESS

/**
 */
class SemVerPluginTest extends Specification {
    @Rule TemporaryFolder testProjectDir = new TemporaryFolder()
    File buildFile
    File semverFile
    File semverFile2

    def setup() {
        buildFile = testProjectDir.newFile('build.gradle')
        buildFile << """
            plugins {
                id 'de.highbyte_le.semver'
            }
        """

        semverFile = testProjectDir.newFile('.semver')
        semverFile << """
---
:major: 1
:minor: 2
:patch: 3
:special: ''
:metadata: ''
        """
        semverFile2 = testProjectDir.newFile('.semver2')
        semverFile2 << """
---
:major: 3
:minor: 2
:patch: 1
:special: 'SNAPSHOT'
:metadata: ''
        """
    }

    def "read default semver file"() {
        buildFile << """
            task printVersion {
                doLast { println(project.version) }
            }
            printVersion.dependsOn readSemVer
        """

        when:
        def result = GradleRunner.create()
                .withProjectDir(testProjectDir.root)
                .withArguments('printVersion')
                .withPluginClasspath()
                .build()

        then:
        result.output.contains("1.2.3")
        result.task(":readSemVer").outcome == SUCCESS
    }

    def "can successfully configure semver path through extension and verify it"() {
        buildFile << """
            semver {
                semverPath = project.layout.projectDirectory.file('.semver2')
            }
            task printVersion {
                doLast { println(project.version) }
            }
            printVersion.dependsOn readSemVer
        """

        when:
        def result = GradleRunner.create()
                .withProjectDir(testProjectDir.root)
                .withArguments('printVersion')
                .withPluginClasspath()
                .build()

        then:
        result.output.contains("3.2.1-SNAPSHOT")
        result.task(":readSemVer").outcome == SUCCESS
    }

    def "can successfully configure version property through extension and verify it"() {
        buildFile << """
            semver {
                versionProperty = 'foo'
            }
            task printVersion {
                doLast { println(project.foo) }
            }
            printVersion.dependsOn readSemVer
        """

        when:
        def result = GradleRunner.create()
                .withProjectDir(testProjectDir.root)
                .withArguments('printVersion')
                .withPluginClasspath()
                .build()

        then:
        result.output.contains("1.2.3")
        result.task(":readSemVer").outcome == SUCCESS
        result.task(":printVersion").outcome == SUCCESS
    }
}
