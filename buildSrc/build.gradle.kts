plugins {
    `kotlin-dsl`
}

repositories {
    jcenter()
}

// Workaround for https://issuetracker.google.com/issues/166468915
dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.4.0")
}
