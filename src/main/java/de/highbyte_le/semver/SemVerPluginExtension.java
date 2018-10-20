package de.highbyte_le.semver;

import org.gradle.api.Project;
import org.gradle.api.file.ProjectLayout;
import org.gradle.api.file.RegularFileProperty;
import org.gradle.api.tasks.InputFile;

public class SemVerPluginExtension {
	private RegularFileProperty semverPath;

	public SemVerPluginExtension(Project project) {
		final ProjectLayout layout = project.getLayout();
		semverPath = layout.fileProperty();
		semverPath.set(layout.getProjectDirectory().file(".semver"));	//default value
	}

	@InputFile
	public RegularFileProperty getSemverPath() {
		return semverPath;
	}

	public void setSemverPath(RegularFileProperty semverPath) {
		this.semverPath = semverPath;
	}
}
