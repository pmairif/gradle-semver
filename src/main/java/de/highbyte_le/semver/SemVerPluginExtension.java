package de.highbyte_le.semver;

import org.gradle.api.Project;
import org.gradle.api.file.ProjectLayout;
import org.gradle.api.file.RegularFileProperty;
import org.gradle.api.provider.Property;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.InputFile;

public class SemVerPluginExtension {
	private RegularFileProperty semverPath;

	/**
	 * the name of the property to set
	 */
	private Property<String> versionProperty;

	public SemVerPluginExtension(Project project) {
		final ProjectLayout layout = project.getLayout();
		semverPath = layout.fileProperty();
		semverPath.set(layout.getProjectDirectory().file(".semver"));	//default value

		versionProperty = project.getObjects().property(String.class);
	}

	@InputFile
	public RegularFileProperty getSemverPath() {
		return semverPath;
	}

	public void setSemverPath(RegularFileProperty semverPath) {
		this.semverPath = semverPath;
	}

	@Input
	public Property<String> getVersionProperty() {
		return versionProperty;
	}

	public void setVersionProperty(Property<String> versionProperty) {
		this.versionProperty = versionProperty;
	}
}
