package de.highbyte_le.semver;

import org.gradle.api.Project;
import org.gradle.api.file.RegularFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReadSemVer {
    private final Project project;

    public ReadSemVer(Project project) {
        this.project = project;
    }

    public String read() throws IOException {
        return read(".semver");
    }

    public String read(String semverFile) throws IOException {
        final RegularFile file = project.getLayout().getProjectDirectory().file(semverFile);
        return read(file.getAsFile());
    }

    public static String read(File semverFile) throws IOException {
        if (!semverFile.exists())
            return "";

        try (BufferedReader br = new BufferedReader(new FileReader(semverFile))) {
            Properties props = readProperties(br);
            return versionString(props);
        }
    }

    static String versionString(Properties props) {
        String ver = String.format("%s.%s.%s", props.get("major"), props.get("minor"), props.get("patch"));

        if (props.containsKey("special"))
            ver += "-" + props.get("special");
        return ver;
    }

    static Properties readProperties(BufferedReader reader) throws IOException {
        String line;
        Properties props = new Properties();
        while ((line = reader.readLine()) != null) {
            Pattern p = Pattern.compile(":([a-z]+): '?([^']+)'?");
            Matcher m = p.matcher(line);
            if (m.matches()) {
                String val = m.group(2).trim();
                if (val.length() > 0)
                    props.put(m.group(1), val);
            }
        }

        return props;
    }
}
