plugins {
    com.android.application
    org.jetbrains.kotlin.android
    kotlin("plugin.serialization")

    alias(libs.plugins.firebase.crashlytics)
    alias(libs.plugins.firebase.perf)
    alias(libs.plugins.googleServices)

    id("com.mikepenz.aboutlibraries.plugin.android")

    com.google.devtools.ksp
    com.google.dagger.hilt.android

    com.edricchan.studybuddy.library.common
    com.edricchan.studybuddy.common.`android-compose`
    com.edricchan.studybuddy.application
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

            // Can be accessed with BuildConfig.BUILD_TIME
            buildConfigField("long", "BUILD_TIME", "${System.currentTimeMillis()}L")
        }

        val nightly by registering {
            // Nightly releases
            initWith(release.get())

            applicationIdSuffix = ".nightly"
            versionNameSuffix = studybuddyApp.metadata.buildInstant.map { "-NIGHTLY-$it" }.get()

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
}

dependencies {
    // Project dependencies
    implementation(projects.ui.common)
    implementation(projects.ui.insets)
    implementation(projects.ui.theming.compose)
    implementation(projects.ui.theming.views)
    implementation(projects.ui.preference.compose)
    implementation(projects.ui.widgets.compose)
    implementation(projects.ui.widgets.views)
    implementation(projects.ui.widgets.modalBottomSheet)
    implementation(projects.core.auth.gms)
    implementation(projects.core.compat.navigation)
    implementation(projects.core.deeplink)
    implementation(projects.core.resources)
    implementation(projects.core.resources.icons)
    implementation(projects.core.settings.tasks)
    implementation(projects.core.settings.tasks.resources)
    implementation(projects.core.settings.tracking)
    implementation(projects.core.settings.updates)
    implementation(projects.core.settings.updates.resources)
    implementation(projects.data.common)
    implementation(projects.data.serialization.android)
    implementation(projects.utils.android)
    implementation(projects.utils.androidx.core)
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

    // Compose dependencies
    implementation(libs.androidx.lifecycle.viewmodel.compose)

    // Firebase dependencies
    implementation(platform(libs.firebase.bom))
    implementation(libs.bundles.firebase)

    // IO/Network dependencies
    implementation(libs.coil)
    implementation(libs.coil.network.ktor3)
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
