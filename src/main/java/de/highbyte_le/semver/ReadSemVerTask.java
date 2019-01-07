package de.highbyte_le.semver;

import org.gradle.api.DefaultTask;
import org.gradle.api.file.RegularFile;
import org.gradle.api.file.RegularFileProperty;
import org.gradle.api.provider.Property;
import org.gradle.api.tasks.TaskAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class ReadSemVerTask extends DefaultTask {
	private static final Logger logger =  LoggerFactory.getLogger(ReadSemVerTask.class);

	private RegularFileProperty semverPath = getProject().getLayout().fileProperty();

	/**
	 * the name of the property to set
	 */
	private Property<String> versionProperty;

	@TaskAction
	public void applyVersion() throws IOException {
		final String version = ReadSemVer.read(semverPath.get().getAsFile());
		if (version != null && !version.isEmpty()) {
			logger.info("version "+version);

			if (versionProperty != null && versionProperty.isPresent()) {	//set named property
				final String propName = versionProperty.get();
				logger.debug("setting property "+propName);
				getProject().getExtensions().add(propName, version);
			}
			else {	//set project version
				logger.debug("setting project version");
				getProject().setVersion(version);
			}
		}
	}

	void setSemverPath(RegularFileProperty semverPath) {
		this.semverPath = semverPath;
	}

	void setSemverPath(RegularFile semverPath) {
		this.semverPath.set(semverPath);
	}

	/**
	 * the name of the project extra property that is set with the read version
	 */
	void setVersionProperty(Property<String> versionProperty) {
		this.versionProperty = versionProperty;
	}
}
