<p align="center"><img src="art/Logotype/Logotype512.png" alt="Studdy Buddy" height="60px"></p>

# Table of contents
- [Study Buddy](#study-buddy)
  - [Purpose](#purpose)
  - [Open source](#open-source)
- [Downloads](#downloads)
- [Current status](#current-status)
- [Web app](#web-app)
- [Screenshots](#screenshots)
  - [Login Activity](#login-activity)
  - [Main Activity](#main-activity)
  - [Settings](#settings)
- [Contributing](#contributing)

# Study Buddy

Welcome to Study Buddy!

## Purpose

The purpose of this app is to help teens, especially students, with their studies.

## Open source

This is a personal project that I created and is 100% open-source. It also uses the following dependencies (see [build.gradle](app/build.gradle) for all the dependencies):

---

# Downloads

Download the latest build [here](https://github.com/Chan4077/StudyBuddy-builds/blob/master/release/com.edricchan.studybuddy-v1.0.0-rc.503.apk).

Older versions can be accessed [here](https://github.com/Chan4077/StudyBuddy-builds/blob/master/release).

The changelog for each version can also be viewed in [CHANGELOG.md](/CHANGELOG.md)

# Current status

Currently, the project is in **RC (Release candidate)**.

To download the latest pre-release, go to [Releases](https://github.com/Chan4077/StudyBuddy/releases).

# Web app

If you want to access the web version, please head to <http://studybuddy-e5f46.firebaseapp.com>. The source code for the web app is available [here](https://github.com/Chan4077/StudyBuddy-web).

# Roadmap

- [ ] Improve navigation UX
- [x] Add working update functionality
- [ ] Revamp/Redesign app
- [ ] Add working chat, tip and calendar
- [ ] Improve authentication logic
- [ ] Change whole project to use Kotlin
- [ ] Release v1.0.0

# Screenshots

## Login Activity

Login Activity | Login > Forgot password | Register Activity
---|---|---
[![Login Activity][art-login-activity]][art-login-activity] | [![Login > Forgot password][art-login-forgot-password]][art-login-forgot-password] | [![Register Activity][art-register-activity]][art-register-activity]

## Main Activity

Main Activity | Main Activity > Dialog (Add new task)
---|---
[![Main Activity][art-main-activity]][art-main-activity] | [![Main Activity > Dialog (Add new task)][art-main-activity-new-task]][art-main-activity-new-task]

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

Logo-Notification Icon Design: <a href="https://github.com/Yasujizr" target="_blank">@Yasujizr</a>

[art-login-activity]: art/screenshots-v2/sign_in.png
[art-login-forgot-password]: art/screenshots-v2/forgot_password.png
[art-main-activity]: art/screenshots-v2/home.png
[art-main-activity-new-task]: art/screenshots-v2/new_task.png
[art-register-activity]: art/screenshots-v2/register.png
[art-settings-experimental]: art/screenshots-v2/settings_experimental.png
[art-settings-notifications-oreo]: art/screenshots-v2/settings_notifications_oreo.png
[art-settings-notifications-pre-oreo]: art/screenshots-v2/settings_notifications_pre_oreo.png
