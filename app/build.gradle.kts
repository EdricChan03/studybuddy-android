import org.jetbrains.kotlin.utils.addToStdlib.cast
import java.text.SimpleDateFormat
import java.util.*

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    alias(libs.plugins.firebase.crashlytics)
    alias(libs.plugins.firebase.perf)
    alias(libs.plugins.googleServices)
    id("com.google.android.gms.oss-licenses-plugin")
    alias(libs.plugins.buildProperties)
}

buildProperties {
    create("env") {
        using(System.getenv() as Map<String, Any>?)
    }
    create("secrets") {
        if (rootProject.file("secret_keys.properties").exists()) {
            using(rootProject.file("secret_keys.properties"))
        } else {
            println("Secret keys properties file does not exist. Setting `secrets`" +
                    "build property as empty.")
            using(mapOf())
        }
    }
    // Version metadata
    create("versions") {
        using(rootProject.file("versions.properties"))
    }
}

val envProperties = buildProperties["env"].asMap()
val versionsProperties = buildProperties["versions"].asMap()
val secretsProperties = buildProperties["secrets"].asMap()

extra.apply {
    // See the Semver guide for more info
    // Adapted from https://medium.com/@maxirosson/versioning-android-apps-d6ec171cfd82
    // Allows the user to specify the versionMajor via the command-line (See below for an example)
    set(
        "versionMajor", project.properties["versionMajor"] as Int? ?:
            versionsProperties["version_major"]?.int
    )
    // Allows the user to specify the versionMinor via the command-line (See below for an example)
    set(
        "versionMinor", project.properties["versionMinor"] as Int? ?:
            versionsProperties["version_minor"]?.int
    )
    // Allows the user to specify the versionPatch via the command-line (See below for an example)
    set(
        "versionPatch", project.properties["versionPatch"] as Int? ?:
            versionsProperties["version_patch"]?.int
    )
    // Allows the user to specify the versionClassifier via the command-line
    // Example: ./gradlew <task> -PversionClassifier=nightly
    set("versionClassifier", project.properties["versionClassifier"])
    // Allows the user to specify the currentVariant via the command-line  (See below for an example)
    set("currentVariant", project.properties["currentVariant"])
}

val buildTimeString: String
    get() {
        val format = SimpleDateFormat("yyyy-MM-dd-HHmmss")
        return format.format(Date())
    }

fun generateVersionCodeBuildVariant(): Int {
    // Default is 1 to represent release variant
    // 0: Debug variant
    // 1: Release variant
    // 10: Nightly variant
    var result = 1
    when (extra["currentVariant"]) {
        "debug" -> {
            result = 0
        }
        "nightly" -> {
            result = 10
        }
    }
    println("Generated version code (build variant): $result")
    return result
}

fun generateVersionCodeClassifier(): Int {
    // Default is 0 to represent no version classifier
    var result = 0
    when (extra["versionClassifier"]) {
        "alpha" -> {
            result = 1
        }
        "beta" -> {
            result = 2
        }
        "rc" -> {
            result = 3
        }
        "nightly" -> {
            result = 10
        }
    }
    println("Generated version code (version classifier): $result")
    return result
}

fun generateVersionCode(): Int {
    /*
     * Returns <build-variant type as number>-<version classifier>-<major>-<minor>-<patch>
     * E.g. 01-10-01-01-00, with the following config:
     * - release build variant
     * - nightly classifier
     * - version major: 1
     * - version minor: 1
     * - version patch: 0
     */
    return (generateVersionCodeBuildVariant() * 100000000 + generateVersionCodeClassifier() * 1000000
            + extra["versionMajor"].cast<Int>() * 10000 + extra["versionMinor"].cast<Int>() * 100
            + extra["versionPatch"].cast<Int>())
}

fun generateVersionName(): String {
    return "${extra["versionMajor"]}.${extra["versionMinor"]}.${extra["versionPatch"]}"
}

android {
    compileSdk = 31
    defaultConfig {
        applicationId = "com.edricchan.studybuddy"
        minSdk = 21
        targetSdk = 30
        // versionCode 9
        versionCode = generateVersionCode()
        println("Generated version code: ${generateVersionCode()}")
        versionName = generateVersionName()
        println("Generated version name: ${generateVersionName()}")
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        resourceConfigurations += "en" // TODO: Don't restrict to just en
        multiDexEnabled = true
        // Enable support library for vector drawables
        vectorDrawables.useSupportLibrary = true
    }

    signingConfigs {
        maybeCreate("release").apply {
            storeFile = rootProject.file("studybuddy.jks")
        }
    }

    buildTypes {
        debug {
            isDebuggable = true // Allow app to be debuggable
            applicationIdSuffix = ".debug"

            // Reduce amount of time needed to compile app in debug mode
            // Can be accessed with BuildConfig.BUILD_TIME
            buildConfigField("Long", "BUILD_TIME", "0L")
            extra["currentVariant"] = "debug"
        }

        release {
            isMinifyEnabled = true // Enable minification
            isShrinkResources = true // Shrink resources to reduce APK size
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
            signingConfig = signingConfigs["release"]
            // Can be accessed with BuildConfig.BUILD_TIME
            buildConfigField("long", "BUILD_TIME", "${System.currentTimeMillis()}L")
            extra["currentVariant"] = "release"
        }

        create("nightly").apply {
            // Nightly releases
            initWith(getByName("release"))

            applicationIdSuffix = ".nightly"
            versionNameSuffix = "-NIGHTLY-$buildTimeString"
            extra["currentVariant"] = "nightly"

            matchingFallbacks += listOf("release", "debug")
        }
    }

    buildFeatures {
        viewBinding = true
    }

    compileOptions {
        sourceCompatibility(JavaVersion.VERSION_11)
        targetCompatibility(JavaVersion.VERSION_11)
    }

    testOptions {
        unitTests.isIncludeAndroidResources = true
    }

    kotlinOptions {
        jvmTarget = "11"
    }

    // GitHub Actions always sets GITHUB_ACTIONS to true when running the workflow.
    // See https://help.github.com/en/actions/automating-your-workflow-with-github-actions/using-environment-variables#default-environment-variables
    // for more info.
    val isRunningOnActions = envProperties["GITHUB_ACTIONS"]?.boolean ?: false

    // TODO: Revert back to "lint" once https://issuetracker.google.com/issues/196209595 is resolved
    @kotlin.Suppress("DEPRECATION")
    lintOptions {
        textReport = isRunningOnActions
        textOutput("stdout")
        isAbortOnError = false
        baseline(file("lint-baseline.xml"))
    }

    signingConfigs["release"].apply {
        if (isRunningOnActions && envProperties.isNotEmpty()) {
            // Configure keystore
            storePassword = envProperties["APP_KEYSTORE_PASSWORD"]?.string
            keyAlias = envProperties["APP_KEYSTORE_ALIAS"]?.string
            keyPassword = envProperties["APP_KEYSTORE_ALIAS_PASSWORD"]?.string
        } else if (secretsProperties.isNotEmpty()) {
            // Building locally
            storePassword = secretsProperties["keystore_password"]?.string
            keyAlias = secretsProperties["keystore_alias"]?.string
            keyPassword =
                secretsProperties["keystore_alias_password"]?.string
        }
    }
}

dependencies {
    implementation(fileTree(mapOf("include" to listOf("*.jar"), "dir" to "libs")))
    implementation(project(":ui:preference"))

    // Test dependencies
    androidTestImplementation(libs.bundles.androidx.test)
    testImplementation(libs.mockito.core)
    testImplementation(libs.junit)

    // AndroidX dependencies
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.browser)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.materialComponents)
    implementation(libs.androidx.preference.ktx)
    implementation(libs.androidx.recyclerview.core)
    implementation(libs.androidx.recyclerview.selection)
    implementation(libs.androidx.swiperefreshlayout)
    implementation(libs.androidx.work.runtime.ktx)

    // Firebase dependencies
    implementation(platform(libs.firebase.bom))
    implementation(libs.bundles.firebase)

    // IO/Network dependencies
    implementation(libs.coil)
    implementation(libs.moshi.core)
    kapt(libs.moshi.kotlinCodegen)

    // Other dependencies
    implementation(libs.materialAbout)
    implementation(libs.appUpdater)
    implementation(libs.imagePicker)
    implementation(libs.playServices.ossLicenses.core)

    // DeepLinkDispatch dependencies
    implementation(libs.deepLinkDispatch.core)
    kapt(libs.deepLinkDispatch.processor)

    // Markwon dependencies
    // See https://noties.io/Markwon for more info
    implementation(libs.bundles.markwon)
    // See https://github.com/noties/Markwon/issues/148#issuecomment-508003794
    configurations.all {
        exclude("org.jetbrains", "annotations-java5")
    }

    // Kotlin dependencies
    implementation(libs.kotlin.stdlib.jdk8)
    implementation(libs.kotlinx.coroutines.android)
}

kapt {
    arguments {
        // See https://github.com/airbnb/DeepLinkDispatch#incremental-annotation-processing for
        // more info
        arg("deepLink.incremental", deps.build.deepLink.incremental)
        arg("deepLink.customAnnotations", deps.build.deepLink.customAnnotations)
        // Add support for documenting deep links
        // See https://github.com/airbnb/DeepLinkDispatch#generated-deep-links-documentation for
        // more info
        arg("deepLinkDoc.output", deps.build.deepLink.getDocOutput(rootDir))
    }
}
