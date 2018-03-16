# About
Welcome to Study Buddy! The purpose of this app is to help students who are teens with their studies. This is a personal project that I created and is 100% open-source.

# Current status
Currently, the project is in **RC**.
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
# Building the source code
If you would like to build the source code, follow these steps:
1. Install the Canary version of Android Studio which adds Android O support. [More info](https://developer.android.com/studio/preview/index.html) _(Note: This version uses Canary Beta 2)_
2. Download the project by either clicking the `Clone or download` > `Download ZIP` or by running the following commands:
   ```bash
   cd path/to/your/desired-location
   git clone https://github.com/Chan4077/StudyBuddy.git
   ```

# Contributing
If you would like to contribute, please go ahead and submit a pull request explaining your changes to the code and also make sure that you open an issue beforehand.

[art-login-activity]: art/screenshots/login_1.png
[art-login-forgot-password]: art/screenshots/login_forgot_password.png
[art-main-activity]: art/screenshots/main_1.png
[art-main-activity-dialog-add-task]: art/screenshots/main_dialog_add_task.png
[art-settings-experimental]: art/screenshots/settings_experimental.png
[art-settings-notifications-oreo]: art/screenshots/settings_notifications_oreo.png
[art-settings-notifications-pre-oreo]: art/screenshots/settings_notifications_pre_oreo.png