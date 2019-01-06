package de.highbyte_le.semver;

import org.gradle.api.Project;
import org.gradle.api.file.RegularFileProperty;
import org.gradle.api.provider.Property;
import org.gradle.testfixtures.ProjectBuilder;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.Properties;

import static org.junit.Assert.assertEquals;

public class ReadSemVerTaskTest {

	private ReadSemVerTask task;

	private Project project;

	@Before
	public void setUp() {
		project = ProjectBuilder.builder().build();
		task = project.getTasks().create("readSemVer", ReadSemVerTask.class);
	}

	@Test
	public void testVersionString() {
		Properties properties = new Properties();
		properties.setProperty("major", "1");
		properties.setProperty("minor", "0");
		properties.setProperty("patch", "2");

		assertEquals("1.0.2", task.versionString(properties));
	}

	@Test
	public void testVersionStringWithSpecial() {
		Properties properties = new Properties();
		properties.setProperty("major", "2");
		properties.setProperty("minor", "1");
		properties.setProperty("patch", "0");
		properties.setProperty("special", "SNAPSHOT");

		assertEquals("2.1.0-SNAPSHOT", task.versionString(properties));
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
			final Properties properties = task.readProperties(reader);
			assertEquals(expected, properties);
		}
	}

	@Test
	public void readSemVerFile() throws IOException {
		File semverFile = new File("test-data/semver-sample");
		assertEquals("9.0.10", task.readSemVer(semverFile));
	}

	@Test
	public void readSemVerFileNotFound() throws IOException {
		File semverFile = new File("test-data/not-present");	//file does not exists
		assertEquals("", task.readSemVer(semverFile));
	}

	@Test
	public void applyVersion() throws IOException {
		final RegularFileProperty fileProperty = project.getLayout().fileProperty();
		//gradle project directory is somewhere - we need an absolute file path to get the one from this project
		fileProperty.set(new File("test-data/semver-sample").getAbsoluteFile());
		task.setSemverPath(fileProperty);

		task.applyVersion();

		assertEquals("9.0.10", project.getVersion().toString());
	}

	@Test
	public void applyExtraProperty() throws IOException {
		final RegularFileProperty fileProperty = project.getLayout().fileProperty();
		//gradle project directory is somewhere - we need an absolute file path to get the one from this project
		fileProperty.set(new File("test-data/semver-sample").getAbsoluteFile());
		task.setSemverPath(fileProperty);

		final Property<String> propertyName = project.getObjects().property(String.class);
		propertyName.set("foo");
		task.setVersionProperty(propertyName);

		task.applyVersion();

		assertEquals("9.0.10", project.getProperties().get("foo").toString());
		assertEquals("unspecified", project.getVersion().toString());	//project version keeps unchanged
	}
}
