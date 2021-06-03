import org.jetbrains.kotlin.utils.addToStdlib.cast
import java.text.SimpleDateFormat
import java.util.*

plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("android.extensions")
    kotlin("kapt")
    id("com.google.firebase.crashlytics")
    id("com.google.firebase.firebase-perf")
    id("com.google.android.gms.oss-licenses-plugin")
    id("com.novoda.build-properties") version "0.4.1"
    id("com.google.gms.google-services")
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
    compileSdk = 30
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
        resConfigs("en") // TODO: Don't restrict to just en
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
            // See https://issuetracker.google.com/issues/186806256
            // isShrinkResources = true // Shrink resources to reduce APK size
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
            signingConfig = signingConfigs["release"]
            // Can be accessed with BuildConfig.BUILD_TIME
            buildConfigField("long", "BUILD_TIME", "${System.currentTimeMillis()}L")
            extra["currentVariant"] = "release"
        }
        create("nightly").apply {
            // See https://issuetracker.google.com/issues/186798050#comment4
            // Nightly releases
            // initWith(getByName("release"))

            // To be removed once AS 7.0 Beta 1 is released

            isMinifyEnabled = true // Enable minification
            isShrinkResources = true // Shrink resources to reduce APK size
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
            signingConfig = signingConfigs["release"]
            // Can be accessed with BuildConfig.BUILD_TIME
            buildConfigField("long", "BUILD_TIME", "${System.currentTimeMillis()}L")

            // End to be removed

            applicationIdSuffix = ".nightly"
            versionNameSuffix = "-NIGHTLY-$buildTimeString"
            extra["currentVariant"] = "nightly"
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

    lint {
        textReport = isRunningOnActions
        textOutput("stdout")
        isAbortOnError = false
        baseline(file("lint-baseline.xml"))
    }

    if (isRunningOnActions && envProperties.isNotEmpty()) {
        // Configure keystore
        signingConfigs["release"].storePassword = envProperties["APP_KEYSTORE_PASSWORD"]?.string
        signingConfigs["release"].keyAlias = envProperties["APP_KEYSTORE_ALIAS"]?.string
        signingConfigs["release"].keyPassword = envProperties["APP_KEYSTORE_ALIAS_PASSWORD"]?.string
    } else if (secretsProperties.isNotEmpty()) {
        // Building locally
        signingConfigs["release"].storePassword = secretsProperties["keystore_password"]?.string
        signingConfigs["release"].keyAlias = secretsProperties["keystore_alias"]?.string
        signingConfigs["release"].keyPassword =
            secretsProperties["keystore_alias_password"]?.string
    }
}

dependencies {
    implementation(fileTree(mapOf("include" to listOf("*.jar"), "dir" to "libs")))
    // Test dependencies
    androidTestImplementation(deps.test.android.espressoCore)
    androidTestImplementation(deps.test.android.testCore)
    androidTestImplementation(deps.test.android.runner)
    androidTestImplementation(deps.test.android.rules)
    testImplementation(deps.test.mockitoCore)
    testImplementation(deps.test.junit)

    // AndroidX dependencies
    implementation(deps.android.androidx.coreKtx)
    implementation(deps.android.androidx.appCompat)
    implementation(deps.android.androidx.browser)
    implementation(deps.android.androidx.constraintLayout)
    implementation(deps.android.androidx.materialComponents)
    implementation(deps.android.androidx.preferenceKtx)
    implementation(deps.android.androidx.recyclerView)
    implementation(deps.android.androidx.recyclerViewSelection)
    implementation(deps.android.androidx.swipeRefreshLayout)
    implementation(deps.android.androidx.workRuntimeKtx)

    // Firebase dependencies
    implementation(platform(deps.firebase.bom))
    implementation(deps.firebase.analyticsKtx)
    implementation(deps.firebase.authKtx)
    implementation(deps.firebase.playServicesAuth)
    implementation(deps.firebase.crashlyticsKtx)
    implementation(deps.firebase.dynamicLinksKtx)
    implementation(deps.firebase.firestoreKtx)
    implementation(deps.firebase.installationsKtx)
    implementation(deps.firebase.messagingKtx)
    implementation(deps.firebase.perfKtx)
    implementation(deps.firebase.guava)

    // Other dependencies
    implementation(deps.misc.about)
    implementation(deps.misc.appUpdater)
    implementation(deps.misc.gson)
    implementation(deps.misc.imagePicker)
    implementation(deps.licenses.ossLicenses)
    // TODO: Check if this library is still needed
    implementation(deps.misc.streamSupport) {
        because("we need a backported library for Java 8 streams")
    }
    implementation(deps.misc.takisoftPreferencex)

    // DeepLinkDispatch dependencies
    implementation(deps.deepLink.deepLinkDispatch)
    kapt(deps.deepLink.processor)

    // Glide dependencies
    implementation(deps.glide.glide)
    kapt(deps.glide.compiler)

    // Markwon dependencies
    // See https://noties.io/Markwon for more info
    implementation(deps.markwon.core)
    implementation(deps.markwon.editor)
    implementation(deps.markwon.ext.strikethrough)
    implementation(deps.markwon.ext.tables)
    implementation(deps.markwon.ext.tasklist)
    implementation(deps.markwon.html)
    implementation(deps.markwon.imageGlide)
    implementation(deps.markwon.linkify)
    implementation(deps.markwon.syntaxHighlight)
    // See https://github.com/noties/Markwon/issues/148#issuecomment-508003794
    configurations.all {
        exclude("org.jetbrains", "annotations-java5")
    }

    // Kotlin dependencies
    implementation(deps.kotlin.stdlibJdk8)
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
