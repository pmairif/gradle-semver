package de.highbyte_le.semver;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

public class SemVerPlugin implements Plugin<Project> {
	@Override
	public void apply(Project project) {
		SemVerPluginExtension extension = project.getExtensions().create("semver", SemVerPluginExtension.class, project);
		project.getTasks().create("readSemVer", ReadSemVerTask.class, (task) -> {
			task.setSemverPath(extension.getSemverPath());
			task.setVersionProperty(extension.getVersionProperty());
		});
	}
}
