<p align="center"><img src="art/Logotype/Logotype512.png" alt="Studdy Buddy Logo" height="60px"></p>

<div align="center">
[![GitHub Actions Status](https://img.shields.io/github/workflow/status/EdricChan03/StudyBuddy-android/CI?label=CI&logo=github&style=flat-square)](https://github.com/EdricChan03/StudyBuddy-android/actions?query=workflow%3ACI)
[![CodeFactor](https://www.codefactor.io/repository/github/edricchan03/studybuddy-android/badge?style=flat-square)](https://www.codefactor.io/repository/github/edricchan03/studybuddy-android)
[![App license](https://img.shields.io/github/license/EdricChan03/StudyBuddy-android?style=flat-square)](https://github.com/EdricChan03/StudyBuddy-android/blob/master/LICENSE)
[![Dependabot status](https://flat.badgen.net/dependabot/EdricChan03/StudyBuddy-android?icon=dependabot)](https://dependabot.com)
</div>

<div align="center">
[![Latest app release](https://img.shields.io/github/v/release/EdricChan03/StudyBuddy-android?include_prereleases&sort=semver&style=flat-square)](https://github.com/EdricChan03/StudyBuddy-android/releases/latest)
</div>

## About the app

The purpose of this app is to help teens, especially students, with their studies.
It contains features that most of the other related apps include but are created from the perspective of a teenager.

The app was created in 2017 and has since been worked on.

## Software used

This app uses the following software:

Software | Description | Links
---|---|---
Firebase (Analytics, Authentication, Crashlytics, Cloud Messaging, Firestore) | Firebase is mainly used for the authentication and database backend, as well as cloud notifications and logs events when the app unexpectedly crashes. | [Firebase](https://firebase.google.com/)

## Libraries used

For more info about the list of libraries used, check out the `build.gradle` files ([root `build.gradle`](build.gradle), [app `build.gradle`](app/build.gradle)).

---

## Downloads

Download the latest build [here](https://github.com/EdricChan03/StudyBuddy-builds/blob/master/release/com.edricchan.studybuddy-v1.0.0-rc.503.apk).

Older versions can be accessed [here](https://github.com/EdricChan03/StudyBuddy-builds/blob/master/release).

The changelog for each version can also be viewed in [CHANGELOG.md](/CHANGELOG.md).

## Screenshots

_COMING SOON! (Screenshots will be moved to a separate file)_

## Building the source code

If you would like to build the source code, follow these steps:
1. Clone the project locally by either clicking the `Clone or download` > `Download ZIP` or by running the following commands:
   ```bash
   cd path/to/your/desired-location
   git clone https://github.com/EdricChan03/StudyBuddy-android.git
   ```
2. Run the following in your terminal:
   
   macOS/Linux:
   ```bash
   ./gradlew assembleDebug
   ```
   Windows:
   ```shell
   gradlew.bat assembleDebug
   ```
3. The APK should be signed with the debug key and will be available at `app/build/outputs/apk`. See [Build a debug APK](https://developer.android.com/studio/build/building-cmdline.html#DebugMode) for more info.

## Contributing

_COMING SOON (There'll soon be a contributing guide - stay tuned!)_

## Contributors

I would like to thank the following people for their contributions to this project:

Contributor | Reason
---|---
<a href="https://github.com/Yasujizr" target="_blank">@Yasujizr</a> | Creating logo/notification icons
