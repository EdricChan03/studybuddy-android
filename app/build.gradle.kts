import com.novoda.buildproperties.Entry
import java.text.SimpleDateFormat
import java.util.*

plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    kotlin("plugin.serialization")
    alias(libs.plugins.firebase.crashlytics)
    alias(libs.plugins.firebase.perf)
    alias(libs.plugins.googleServices)
    id("com.google.android.gms.oss-licenses-plugin")
    alias(libs.plugins.buildProperties)
    id("com.google.dagger.hilt.android")
}

buildProperties {
    create("env") {
        using(System.getenv() as Map<String, Any>?)
    }
    create("secrets") {
        if (rootProject.file("secret_keys.properties").exists()) {
            using(rootProject.file("secret_keys.properties"))
        } else {
            println(
                "Secret keys properties file does not exist. Setting `secrets`" +
                    "build property as empty."
            )
            using(mapOf())
        }
    }
}

val envProperties: Map<String, Entry> = buildProperties["env"].asMap()
val secretsProperties: Map<String, Entry> = buildProperties["secrets"].asMap()

val buildTimeString: String
    get() {
        val format = SimpleDateFormat("yyyy-MM-dd-HHmmss")
        return format.format(Date())
    }

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

android {
    namespace = "com.edricchan.studybuddy"

    defaultConfig {
        applicationId = "com.edricchan.studybuddy"
        // TODO: Bump targetSdk
        targetSdk = 30
        versionCode = 10
        versionName = "1.1.0"
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
        }

        release {
            isMinifyEnabled = true // Enable minification
            isShrinkResources = true // Shrink resources to reduce APK size
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
            signingConfig = signingConfigs["release"]

            // Can be accessed with BuildConfig.BUILD_TIME
            buildConfigField("long", "BUILD_TIME", "${System.currentTimeMillis()}L")
        }

        create("nightly").apply {
            // Nightly releases
            initWith(getByName("release"))

            applicationIdSuffix = ".nightly"
            versionNameSuffix = "-NIGHTLY-$buildTimeString"

            matchingFallbacks += listOf("release", "debug")
        }
    }

    buildFeatures {
        viewBinding = true
        buildConfig = true
    }

    compileOptions {
        isCoreLibraryDesugaringEnabled = true

        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    testOptions {
        unitTests.isIncludeAndroidResources = true
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    // GitHub Actions always sets GITHUB_ACTIONS to true when running the workflow.
    // See https://help.github.com/en/actions/automating-your-workflow-with-github-actions/using-environment-variables#default-environment-variables
    // for more info.
    val isRunningOnActions = envProperties["GITHUB_ACTIONS"]?.boolean ?: false

    lint {
        textReport = isRunningOnActions
        sarifReport = isRunningOnActions
        abortOnError = false
        baseline = file("lint-baseline.xml")
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

    // Project dependencies
    implementation(projects.ui.common)
    implementation(projects.ui.theming.views)
    implementation(projects.ui.preference)
    implementation(projects.ui.widgets.views)
    implementation(projects.core.deeplink)
    implementation(projects.data.serialization.android)
    implementation(projects.utils.recyclerview)
    implementation(projects.exts.androidx.preference)
    implementation(projects.exts.androidx.preferenceFiles)
    implementation(projects.feature.help)

    // Test dependencies
    androidTestImplementation(libs.bundles.androidx.test)
    testImplementation(libs.mockito.core)
    testImplementation(libs.junit)

    coreLibraryDesugaring(libs.android.desugar)

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
    implementation(libs.bundles.ktor)
    implementation(libs.kotlinx.serialization.json)

    // Other dependencies
    implementation(libs.materialAbout)
    implementation(libs.appUpdater)
    implementation(libs.imagePicker)
    implementation(libs.playServices.ossLicenses.core)

    // Dagger dependencies
    implementation(libs.dagger.hilt.android)
    kapt(libs.dagger.hilt.compiler)

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
    implementation(libs.kotlinx.coroutines.android)
}

kapt {
    correctErrorTypes = true
    arguments {
        // See https://github.com/airbnb/DeepLinkDispatch#incremental-annotation-processing for
        // more info
        // Add support for documenting deep links
        // See https://github.com/airbnb/DeepLinkDispatch#generated-deep-links-documentation for
        // more info
        arg("deepLinkDoc.output", deps.build.deepLink.getDocOutput(rootDir))
    }
    javacOptions {
        // Increase the max count of errors from annotation processors.
        // Default is 100.
        option("-Xmaxerrs", 500)
    }
}
