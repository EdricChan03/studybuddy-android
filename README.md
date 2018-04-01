# Table of contents
- [Study Buddy](#study-buddy)
  - [Purpose](#purpose)
  - [Open source!](#open-source)
- [Downloads](#downloads)
- [Dependencies](#dependencies)
- [Current status](#current-status)
- [Web app](#web-app)
- [TODO](#todo)
- [Screenshots](#screenshots)
  - [Login Activity](#login-activity)
  - [Main Activity](#main-activity)
  - [Settings](#settings)
- [Building the source code](#building-the-source-code)
- [Contributing](#contributing)
# Study Buddy
Welcome to Study Buddy!

## Purpose
The purpose of this app is to help teens, especially students, with their studies.
## Open source!
This is a personal project that I created and is 100% open-source. It also uses the following dependencies (see [build.gradle](app/build.gradle) for all the dependencies):

---

# Downloads
Download the latest build [here](https://github.com/Chan4077/StudyBuddy-builds/blob/master/release/com.edricchan.studybuddy-v1.0.0-rc.503.apk).

Older versions can be accessed [here](https://github.com/Chan4077/StudyBuddy-builds/blob/master/release).

The changelog for each version can also be viewed in [CHANGELOG.md](/CHANGELOG.md)

---
<!-- Should I just delete this whole section? :P -->
# Dependencies
<details>
<summary>View dependencies</summary>

## Android
Dependency | Version | Description | Website
---|---|---|---
`com.android.support:support-v4` | `28.0.0-alpha1` | Android Support Library | -
`com.android.support:support-annotations` | `28.0.0-alpha1` | Android Support Annotations Library | -
`com.android.support:appcompat-v7` | `28.0.0-alpha1` | Android AppCompat Support Library (Support for Material Design for devices running pre-Lollipop) | -
`com.android.support.constraint:constraint-layout` | `1.0.2` | Android Constraint Layout library | -
`com.android.support:design` | `28.0.0-alpha1` | Android Design Support Library | -
`com.android.support:cardview-v7` | `28.0.0-alpha1` | Android CardView Support Library (For apps needing to implement a card-like interface) | -
`com.androud.support:customtabs` | `28.0.0-alpha1` | Chrome Custom Tabs | -

## Firebase/Google
Dependency | Version | Description | Website
---|---|---|---
`com.google.firebase:firebase-storage` | `12.0.0` | Firebase Storage SDK | -
`com.google.firebase:firebase-firestore` | `12.0.0` | Firebase Cloud Firestore SDK | - 
`com.google.firebase:firebase-auth` | `12.0.0` | Firebase Authentication SDK | -
`com.google.firebase:firebase-messaging` | `12.0.0` | Firebase Cloud Messaging SDK | -
`com.google.android.gms:play-services-auth` | `12.0.0` | Google Play Services Authentication SDK (To be used with Firebase Auth SDK) | -

## Other
Dependency | Version | Description | Website
---|---|---|---
`com.github.javiersantos:AppUpdater` | `2.6.5` | Provides a way to update the app | -
`com.heinrichreimersoftware:material-intro` | `1.6.2` | App's intro | -
</details>

# Current status
Currently, the project is in **RC (Release candidate)**.
To get the latest pre-release, go to [Releases](https://github.com/Chan4077/StudyBuddy/releases).

# Web app
If you want to access the web version, please head to <http://studybuddy-e5f46.firebaseapp.com>. The source code for the web app is available [here](https://github.com/Chan4077/StudyBuddy-web).

# TODO
- Add calendar view
- Add different views (Use `Fragment` etc.)
- Add this app to the play store
- Implement live syncing to and from the database
- Add stuff planned for this year

# Screenshots

## Login Activity

Login Activity | Login > Forgot password
---|---
[![Login Activity][art-login-activity]][art-login-activity] | [![Login > Forgot password][art-login-forgot-password]][art-login-forgot-password]

## Main Activity

Main Activity | Main Activity > Dialog (Add new task)
---|---
[![Main Activity][art-main-activity]][art-main-activity] | [![Main Activity > Dialog (Add new task)][art-main-activity-dialog-add-task]][art-main-activity-dialog-add-task]
## Settings
Settings > Experimental | Settings > Notifications (Oreo devices) | Settings > Notifications (Pre-Oreo devices)
---|---|---
[![Settings > Experimental][art-settings-experimental]][art-settings-experimental] | [![Settings > Notifications (Oreo devices)][art-settings-notifications-oreo]][art-settings-notifications-oreo] | [![Settings > Notifications (Pre-Oreo devices)][art-settings-notifications-pre-oreo]][art-settings-notifications-pre-oreo]
---
# Building the source code (debug only)
If you would like to build the source code, follow these steps:
1. Clone the project locally by either clicking the `Clone or download` > `Download ZIP` or by running the following commands:
   ```bash
   cd path/to/your/desired-location
   git clone https://github.com/Chan4077/StudyBuddy.git
   ```
2. Run the following in your terminal:
   
   macOS:
   ```bash
   ./gradlew assembleDebug
   ```
   Windows:
   ```shell
   gradlew.bat assembleDebug
   ```
3. The APK should be signed with the debug key and is available at `app/build/outputs/apk`. See [Build a debug APK](https://developer.android.com/studio/build/building-cmdline.html#DebugMode) for more info.

# Contributing
If you would like to contribute, please go ahead and submit a pull request explaining your changes to the code and also make sure that you open an issue beforehand.

[art-login-activity]: art/screenshots/login_1.png
[art-login-forgot-password]: art/screenshots/login_forgot_password.png
[art-main-activity]: art/screenshots/main_1.png
[art-main-activity-dialog-add-task]: art/screenshots/main_dialog_add_task.png
[art-settings-experimental]: art/screenshots/settings_experimental.png
[art-settings-notifications-oreo]: art/screenshots/settings_notifications_oreo.png
[art-settings-notifications-pre-oreo]: art/screenshots/settings_notifications_pre_oreo.png