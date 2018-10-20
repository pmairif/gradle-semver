# gradle-semver

Gradle plugin that reads semantic versioning file ('.semver') generated by semver utility 
([original project][1] or this [fork][2] for example)
and sets the project version.

Simply apply the plugin to your gradle build and set dependency from the jar task 
or another task that needs the project version to be set:

```gradle
plugins {
    id 'de.highbyte_le.semver'
}

task printVersion {
    doLast {
        println(project.version)
    }
}
printVersion.dependsOn readSemVer

```

For a java build that would be:
```gradle
plugins {
    id 'java'
    id 'de.highbyte_le.semver'
}

jar.dependsOn readSemVer
```

The .semver file is searched in the project directory. This is configurable.
If you want to use that from the parent directory:
```gradle
semver {
    semverPath = project.layout.projectDirectory.file('../.semver')
}
```
 
## License

The code is distributed under the terms of the Apache License (ALv2). See the [`LICENSE`][3] file.

[1]: https://github.com/flazz/semver
[2]: https://github.com/haf/semver
[3]: https://github.com/pmairif/gradle-semver/blob/master/LICENSE
