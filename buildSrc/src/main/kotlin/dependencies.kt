@file:Suppress("ClassName")

import java.io.File

@Suppress("HardCodedStringLiteral")
object deps {
    object versions {
        const val androidxTest = "1.3.0"
        const val kotlin = "1.4.0"
        const val markwon = "4.5.1"
    }

    object build {
        object deepLink {
            const val customAnnotations = "com.edricchan.studybuddy.annotations.AppDeepLink," +
                    "com.edricchan.studybuddy.annotations.WebDeepLink"
            const val incremental = "true"
            private const val outputSuffix = "docs/deeplinks.md"
            fun getDocOutput(dir: File): String {
                // Interestingly, the `toString` method of `java.io.File` actually invokes
                // the `getPath` method so we don't have to convert it to a path explicitly.
                return "$dir/$outputSuffix"
            }
        }
        object repositories {
            const val jitpack = "https://jitpack.io"
        }
    }

    object android {
        object androidx {
            const val appCompat = "androidx.appcompat:appcompat:1.3.0-alpha02"
            const val browser = "androidx.browser:browser:1.3.0-alpha05"
            // Only needed for com.github.daniel-stoneuk:material-about-library
            const val cardView = "androidx.cardview:cardview:1.0.0"
            const val constraintLayout = "androidx.constraintlayout:constraintlayout:2.0.0"
            const val coreKtx = "androidx.core:core-ktx:1.5.0-alpha02"
            const val emojiAppCompat = "androidx.emoji:emoji-appcompat:1.2.0-alpha01"
            const val materialComponents = "com.google.android.material:material:1.3.0-alpha02"
            const val preferenceKtx = "androidx.preference:preference-ktx:1.1.1"
            const val recyclerView = "androidx.recyclerview:recyclerview:1.2.0-alpha05"
            const val recyclerViewSelection = "androidx.recyclerview:recyclerview-selection:1.1.0-rc01"
            const val swipeRefreshLayout = "androidx.swiperefreshlayout:swiperefreshlayout:1.2.0-alpha01"
            const val workRuntimeKtx = "androidx.work:work-runtime-ktx:2.5.0-alpha01"
        }

        const val gradlePlugin = "com.android.tools.build:gradle:4.2.0-alpha08"
    }

    object deepLink {
        const val deepLinkDispatch = "com.airbnb:deeplinkdispatch:5.2.0"
        const val processor = "com.airbnb:deeplinkdispatch-processor:5.2.0"
    }

    object firebase {
        const val analyticsKtx = "com.google.firebase:firebase-analytics-ktx:17.5.0"
        const val authKtx = "com.google.firebase:firebase-auth-ktx:19.3.2"
        const val playServicesAuth = "com.google.android.gms:play-services-auth:18.0.0"
        const val crashlytics = "com.google.firebase:firebase-crashlytics:17.2.1"
        const val crashlyticsPlugin = "com.google.firebase:firebase-crashlytics-gradle:2.0.0"
        const val dynamicLinksKtx = "com.google.firebase:firebase-dynamic-links-ktx:19.1.0"
        const val firestoreKtx = "com.google.firebase:firebase-firestore-ktx:21.5.0"
        const val messaging = "com.google.firebase:firebase-messaging:20.2.4"
        const val perf = "com.google.firebase:firebase-perf:19.0.8"
        const val perfPlugin = "com.google.firebase:perf-plugin:1.3.1"
        const val guava = "com.google.guava:guava:29.0-android"
        const val gradlePlugin = "com.google.gms:google-services:4.3.3"
    }

    object glide {
        const val glide = "com.github.bumptech.glide:glide:4.11.0"
        const val compiler = "com.github.bumptech.glide:compiler:4.11.0"
    }

    object kotlin {
        const val gradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${versions.kotlin}"
        const val stdlibJdk8 = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:${versions.kotlin}"
    }

    object licenses {
        const val ossLicenses = "com.google.android.gms:play-services-oss-licenses:17.0.0"
        const val gradlePlugin = "com.google.android.gms:oss-licenses-plugin:0.10.2"
    }

    object markwon {
        private const val markwon_version = versions.markwon
        private const val package_prefix = "io.noties.markwon"
        const val core = "$package_prefix:core:$markwon_version"
        const val editor = "$package_prefix:editor:$markwon_version"
        object ext {
            private const val ext_package_prefix = "$package_prefix:ext"
            const val strikethrough = "$ext_package_prefix-strikethrough:$markwon_version"
            const val tables = "$ext_package_prefix-tables:$markwon_version"
            const val tasklist = "$ext_package_prefix-tasklist:$markwon_version"
        }
        const val html = "$package_prefix:html:$markwon_version"
        const val imageGlide = "$package_prefix:image-glide:$markwon_version"
        const val linkify = "$package_prefix:linkify:$markwon_version"
        const val syntaxHighlight = "$package_prefix:syntax-highlight:$markwon_version"
    }

    object misc {
        const val about = "com.github.daniel-stoneuk:material-about-library:3.2.0-rc01"
        const val appUpdater = "com.github.javiersantos:AppUpdater:2.7"
        const val gson = "com.google.code.gson:gson:2.8.6"
        const val imagePicker = "com.github.esafirm.android-image-picker:imagepicker:2.3.0"
        const val streamSupport = "net.sourceforge.streamsupport:streamsupport:1.7.2"
        const val takisoftPreferencex = "com.takisoft.preferencex:preferencex:1.1.0"
    }

    object test {
        object android {
            const val espressoCore = "androidx.test.espresso:espresso-core:3.3.0"
            const val testCore = "androidx.test:core:${versions.androidxTest}"
            const val runner = "androidx.test:runner:${versions.androidxTest}"
            const val rules = "androidx.test:rules:${versions.androidxTest}"
        }
        const val junit = "junit:junit:4.13"
        const val mockitoCore = "org.mockito:mockito-core:3.3.3"
    }
}