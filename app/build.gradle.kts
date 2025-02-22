import com.novoda.buildproperties.Entry
import java.time.Instant
import java.time.format.DateTimeFormatter

plugins {
    com.android.application
    org.jetbrains.kotlin.android
    kotlin("plugin.serialization")

    alias(libs.plugins.firebase.crashlytics)
    alias(libs.plugins.firebase.perf)
    alias(libs.plugins.googleServices)

    alias(libs.plugins.buildProperties)

    id("com.mikepenz.aboutlibraries.plugin")

    com.google.devtools.ksp
    com.google.dagger.hilt.android

    com.edricchan.studybuddy.library.common
    com.edricchan.studybuddy.common.`android-compose`
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
        val formatter = DateTimeFormatter.ISO_INSTANT
        return formatter.format(Instant.now())
    }

kotlin {
    jvmToolchain(17)
}

android {
    namespace = "com.edricchan.studybuddy"

    defaultConfig {
        applicationId = "com.edricchan.studybuddy"
        targetSdk = 34
        versionCode = 10
        versionName = "1.1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        androidResources.localeFilters += "en" // TODO: Don't restrict to just en

        vectorDrawables.useSupportLibrary = true
    }

    signingConfigs {
        maybeCreate("release").apply {
            storeFile = rootProject.file("studybuddy.jks")
        }
    }

    buildTypes {
        val debug by existing {
            isDebuggable = true // Allow app to be debuggable
            applicationIdSuffix = ".debug"

            // Reduce amount of time needed to compile app in debug mode
            // Can be accessed with BuildConfig.BUILD_TIME
            buildConfigField("Long", "BUILD_TIME", "0L")
        }

        val release by existing {
            isMinifyEnabled = true // Enable minification
            isShrinkResources = true // Shrink resources to reduce APK size
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
            signingConfig = signingConfigs["release"]

            // Can be accessed with BuildConfig.BUILD_TIME
            buildConfigField("long", "BUILD_TIME", "${System.currentTimeMillis()}L")
        }

        val nightly by registering {
            // Nightly releases
            initWith(release.get())

            applicationIdSuffix = ".nightly"
            versionNameSuffix = "-NIGHTLY-$buildTimeString"

            matchingFallbacks += listOf(release.name, debug.name)
        }

        val benchmark by registering {
            initWith(release.get())

            applicationIdSuffix = ".benchmark"
            matchingFallbacks += release.name
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-benchmark-rules.pro"
            )
            isDebuggable = false
        }
    }

    buildFeatures {
        viewBinding = true
        buildConfig = true
    }

    compileOptions.isCoreLibraryDesugaringEnabled = true

    testOptions {
        unitTests.isIncludeAndroidResources = true
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

    signingConfigs.getByName("release") {
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
    // Project dependencies
    implementation(projects.ui.common)
    implementation(projects.ui.insets)
    implementation(projects.ui.theming.compose)
    implementation(projects.ui.theming.views)
    implementation(projects.ui.preference)
    implementation(projects.ui.widgets.views)
    implementation(projects.ui.widgets.modalBottomSheet)
    implementation(projects.core.auth.gms)
    implementation(projects.core.compat.navigation)
    implementation(projects.core.deeplink)
    implementation(projects.core.resources)
    implementation(projects.core.settings.tasks)
    implementation(projects.core.settings.updates)
    implementation(projects.data.common)
    implementation(projects.data.serialization.android)
    implementation(projects.utils.devMode)
    implementation(projects.utils.network)
    implementation(projects.utils.recyclerview)
    implementation(projects.utils.web)
    implementation(projects.exts.android)
    implementation(projects.exts.androidx.compose)
    implementation(projects.exts.androidx.fragment)
    implementation(projects.exts.androidx.preference)
    implementation(projects.exts.androidx.preferenceFiles)
    implementation(projects.exts.androidx.viewBinding)
    implementation(projects.exts.common)
    implementation(projects.exts.datetime)
    implementation(projects.exts.firebase.core)
    implementation(projects.exts.firebase.temporal)
    implementation(projects.exts.markwon)
    implementation(projects.exts.material)
    implementation(projects.features.auth)
    implementation(projects.features.help)
    implementation(projects.features.settings)
    implementation(projects.features.tasks)

    // Test dependencies
    androidTestImplementation(libs.bundles.androidx.test)
    testImplementation(libs.mockito.core)
    testImplementation(libs.junit)

    coreLibraryDesugaring(libs.android.desugar.nio)

    // AndroidX dependencies
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.browser)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.materialComponents)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
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
    implementation(libs.flowPreferences)
    implementation(libs.migration)

    // Dagger dependencies
    implementation(libs.dagger.hilt.android)
    ksp(libs.dagger.hilt.compiler)

    // DeepLinkDispatch dependencies
    implementation(libs.deepLinkDispatch.core)
    ksp(libs.deepLinkDispatch.processor)

    // Markwon dependencies
    // See https://noties.io/Markwon for more info
    implementation(libs.bundles.markwon)
    // See https://github.com/noties/Markwon/issues/148#issuecomment-508003794
    configurations.configureEach {
        exclude("org.jetbrains", "annotations-java5")
    }

    // Kotlin dependencies
    implementation(libs.kotlinx.coroutines.android)
}

ksp {
    // See https://github.com/airbnb/DeepLinkDispatch#incremental-annotation-processing for
    // more info
    // Add support for documenting deep links
    // See https://github.com/airbnb/DeepLinkDispatch#generated-deep-links-documentation for
    // more info
    arg("deepLinkDoc.output", deps.build.deepLink.getDocOutput(rootDir))
}
