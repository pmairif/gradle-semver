package de.highbyte_le.semver;

import org.gradle.api.Project;
import org.gradle.testfixtures.ProjectBuilder;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.Properties;

import static org.junit.Assert.assertEquals;

public class ReadSemVerTest {
    private ReadSemVer semVer;

    @Before
    public void setUp() {
        Project project = ProjectBuilder.builder().withProjectDir(new File("test-data")).build();
        semVer = new ReadSemVer(project);
    }

    @Test
    public void testVersionString() {
        Properties properties = new Properties();
        properties.setProperty("major", "1");
        properties.setProperty("minor", "0");
        properties.setProperty("patch", "2");

        assertEquals("1.0.2", ReadSemVer.versionString(properties));
    }

    @Test
    public void testVersionStringWithSpecial() {
        Properties properties = new Properties();
        properties.setProperty("major", "2");
        properties.setProperty("minor", "1");
        properties.setProperty("patch", "0");
        properties.setProperty("special", "SNAPSHOT");

        assertEquals("2.1.0-SNAPSHOT", ReadSemVer.versionString(properties));
    }

    @Test
    public void readProperties() throws IOException {
        String semver = "---\n" +
                ":major: 0\n" +
                ":minor: 1\n" +
                ":patch: 2\n" +
                ":special: 'SNAPSHOT'\n" +
                ":metadata: ''\n";

        final Properties expected = new Properties();
        expected.setProperty("major", "0");
        expected.setProperty("minor", "1");
        expected.setProperty("patch", "2");
        expected.setProperty("special", "SNAPSHOT");

        try (BufferedReader reader = new BufferedReader(new StringReader(semver))) {
            final Properties properties = ReadSemVer.readProperties(reader);
            assertEquals(expected, properties);
        }
    }

    @Test
    public void readSemVerFile() throws IOException {
        File semverFile = new File("test-data/semver-sample");
        assertEquals("9.0.10", ReadSemVer.read(semverFile));
    }

    @Test
    public void readSemVerString() throws IOException {
        assertEquals("9.0.10", semVer.read("semver-sample"));
    }

    @Test
    public void readSemVerFileNotFound() throws IOException {
        File semverFile = new File("test-data/not-present");	//file does not exists
        assertEquals("", ReadSemVer.read(semverFile));
    }
}
