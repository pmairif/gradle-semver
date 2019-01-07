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
