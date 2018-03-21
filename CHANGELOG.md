<a name="latest-commits"></a>

View the latest commits [here](https://github.com/Chan4077/StudyBuddy/commits/master).

The current release is [`v1.0.0-rc.503`](#v1.0.0-rc.503)

# Links
- [Download the latest APK](https://github.com/Chan4077/StudyBuddy-builds/raw/master/release/com.edricchan.studybuddy-v1.0.0-rc.503.apk)
- [View previous releases](https://github.com/Chan4077/StudyBuddy-builds/tree/master/release)
- [Latest changelog](#latest-changelog)
# Notes
The apk file you are downloading is a beta/release candidate and may break at any time. Please open a new issue if that is the case and add steps for reproducing the issue. You may also include a screenshot and a logcat if you have `adb`.

# Permissions
```xml
<uses-permission android:name="android.permission.INTERNET"/>
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
```
These two permissions are required for `AppUpdater` to check for updates.
# Installing
1. Allow APKs from unknown sources. > [Here's an article from HowToGeek showing how to do that](https://www.howtogeek.com/howto/41082/install-non-market-apps-on-your-android-device/)
2. Download the apk file below.
3. Install it by opening it.
4. Done!

# License
```
MIT License

Copyright (c) 2017 Edric Chan

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```
---
<a name="latest-changelog"></a>

<a name="v1.0.0-rc.503"></a>

# [v1.0.0-rc.503 buyer-hotel (2018-03-22) <sub><sup>RC</sup></sub>](https://github.com/Chan4077/StudyBuddy/compare/v1.0.0-rc.023...v1.0.0-rc.503)
## Highlights
- Added adaptive icon support for shortcuts on devices running API `26` and later! You can view the commit [here](https://github.com/Chan4077/StudyBuddy/commit/d963121bbcdd9b78f237fff39689cab5f05c0510).
- Notification settings are now different where its based on whether you are running the app on a device running API `25` and older, or `26` and later.

  This means that devices running Android Oreo will see settings which will take users to the specific notification category in the system Settings app where users can configure the setting of the notification channel.

---

## Docs
- **readme**: Add updated screenshots of the app

## Features
- **shortcuts**: add adaptive icon support ([`d963121`](https://github.com/Chan4077/StudyBuddy/commit/d963121bbcdd9b78f237fff39689cab5f05c0510))
- **shortcuts**: add settings shortcut ([`1305893`](https://github.com/Chan4077/StudyBuddy/commit/1305893b76f21906cd0608283c2d4571850e3e02))

## Bug/Typo fixes
- **strings**: `uncategorized` -> `uncategorised` ([`dd85b80`](https://github.com/Chan4077/StudyBuddy/commit/dd85b8013785084baa026d462f9bcfd967d4be74))
- **settings**: use `ContextCompat#getColor` instead of `getResources#getColor` ([`ad5a050`](https://github.com/Chan4077/StudyBuddy/commit/ad5a050a39aec37e3913a3d2042380f0810cfe72))
- **proguard**: add rules for OkHttp ([`b0fd373`](https://github.com/Chan4077/StudyBuddy/commit/b0fd373764b555a4ed4b859f33a7cd03f9d8dde4))

## Libraries
- **deps**: bump FirebaseSDK to `12.0.0` ([`b3457cc`](https://github.com/Chan4077/StudyBuddy/commit/b3457cc2f1065363eaa0dc8e82fb64f1b16a5442))
- **gradle**: upgrade gradle to `4.7` ([`93e03d8`](https://github.com/Chan4077/StudyBuddy/commit/93e03d8ae049313884f95364f0fc1ed65eff9c67))

## Maintainance
- **main-activity**: move content from fragment to main actvity ([`b687f1d`](https://github.com/Chan4077/StudyBuddy/commit/b687f1db7da832de05f806535e6e79c9a89187cf)), ([`c08dfbf`](https://github.com/Chan4077/StudyBuddy/commit/c08dfbfec9074f0af173f917a5dfb66d106fb6bc))
- **manifest**: add `intent-filter` for when user taps on settings in app's info ([`7dbac7a`](https://github.com/Chan4077/StudyBuddy/commit/7dbac7a98b20d27d2edd6dcf6d37a6e4f56556a3))
- **main-activity**: remove bottom navigation ([`560ab35`](https://github.com/Chan4077/StudyBuddy/commit/560ab355b5f18c171e8e3f8af4211d3fa64f4c44))
- **settings-experimental**: add switch for notifications ([`6693ec1`](https://github.com/Chan4077/StudyBuddy/commit/6693ec111703c0039b8f99b727c2c893c8ea4c61))
- **settings-notifications**: deprecate method introduced in [`c9c06af`](https://github.com/Chan4077/StudyBuddy/commit/c9c06afc4ed75ed98dbc0a11cf4edc457568e8aa) ([`7aceb9e`](https://github.com/Chan4077/StudyBuddy/commit/7aceb9eaa8bfd6465aed27d4f8686ad35aa9aec2))
- **settings-notifications**: make checkbox update UI ([`c9c06af`](https://github.com/Chan4077/StudyBuddy/commit/c9c06afc4ed75ed98dbc0a11cf4edc457568e8aa))
- **icons**: use `colorControlNormal` for fill colour ([`635cd94`](https://github.com/Chan4077/StudyBuddy/commit/635cd94fbc54273de8f59c23eb0ec122c1159f37))
- **screenshots**: move old screenshots to `art` folder ([`cd05912`](https://github.com/Chan4077/StudyBuddy/commit/cd0591213f0177981c94e41896b9aa628cff3c2b))
- **settings**: reword more options ([`9858d76`](https://github.com/Chan4077/StudyBuddy/commit/9858d76bc9c81a5cafe7b495da7838108c41bbfd))
- **icons**: add xml headers ([`672c998`](https://github.com/Chan4077/StudyBuddy/commit/672c9985f48f17f9545521b5dfb3c831fc3725fc))
- **settings-notification**: dynamically generate settings ([`470bdbe`](https://github.com/Chan4077/StudyBuddy/commit/470bdbe50c5586fc0ea7d3b2dee85e5e0535e02a))
- **build.gradle**: bump `versionCode` & `versionName` ([`acdd4f2`](https://github.com/Chan4077/StudyBuddy/commit/acdd4f270311a217c58da99f77afe2382ceca97c))

## Refactoring
- **styles**: remove extra line ([`bb2a0db`](https://github.com/Chan4077/StudyBuddy/commit/bb2a0dbed6465ed963645687ed4301c06691eaad))
- **strings***: ([`185ca41`](https://github.com/Chan4077/StudyBuddy/commit/185ca412c398ed18d25daa132c38c4e2000a1af9)), ([`3d9c551`](https://github.com/Chan4077/StudyBuddy/commit/3d9c551205e0a87404a40c128dac392515abd108))
- correctly indent files with tab character ([`bdd5243`](https://github.com/Chan4077/StudyBuddy/commit/bdd5243a99bf94536b6b718d9cadb8f46bbc21b1))

*Note: This bullet point lists all commits which change the naming of strings. This is to reduce the number of lines in the changelog file.

## Art
- **art**: add app adaptive icon sketch file ([`f310e39`](https://github.com/Chan4077/StudyBuddy/commit/f310e39b2985396e468abac5577c3f6d8f5d8f1c))

<a name="v1.0.0-rc.023"></a>

# [v1.0.0-rc.023 retiree-facility (2017-12-13) <sub><sup>RC</sup></sub>](https://github.com/Chan4077/StudyBuddy/compare/v1.0.0-beta.795...v1.0.0-rc.023)
## Highlights
- Finally! After a few months, the app icon for this app has officially changed! You can view the commit for it [here](https://github.com/Chan4077/StudyBuddy/commit/829dfdbbbcae094dee6cabf40b4177a18e274a99).
- Changed the way the changelog looks like to match Angular's changelog
- This is the first release candidate for StudyBuddy! :smile: Now that the UI looks cool enough to exit beta (along with the stuff behind), issues are welcome to help me squash some bugs that I haven't noticed before.

## Docs
- **changelog**: Use new changelog style (you can see it right now!) (similar to Angular's changelog style)
- **StudyBuddy-builds**: Add new readme

## Features
- **icons**: Change to new app icon (Feedback welcome, created with [Sketch](https://sketchapp.com)) ([`829dfdb`](https://github.com/Chan4077/StudyBuddy/commit/829dfdbbbcae094dee6cabf40b4177a18e274a99))
- **settings**: Add new toggle to enable/disable calendar (WIP) ([`6650231`](https://github.com/Chan4077/StudyBuddy/commit/66502310f7b8c4a724c899fa9a7aa9bf9ab7a631))
- **settings**: Show the current app's version ([`8f95dde`](https://github.com/Chan4077/StudyBuddy/commit/8f95dde2bb77c67105fa7027734b7596595c7f28))
- **settings**: Add a new setting to set the update channel (WIP) ([`1235e57`](https://github.com/Chan4077/StudyBuddy/commit/1235e5729523908836146290957872ee77a8ad69))
- **layout**: Use side navigation instead of bottom navigation ([`c4da84b`](https://github.com/Chan4077/StudyBuddy/commit/c4da84bb2b8f21ff5a0feaf4013ca84836751125))
- **settings**: Add help menu item ([`183535a`](https://github.com/Chan4077/StudyBuddy/commit/183535a5977d655a30983d87be8b0a4866fb34dc))

## Bug fixes
- **settings**: Remove "Enable" prefix ([`ff3d0e8`](https://github.com/Chan4077/StudyBuddy/commit/ff3d0e8b96c837e6f6c6f4ebc79753664b4ed941))
- **app-intro**: Change activity prefix to use `My` ([`23f1f5e`](https://github.com/Chan4077/StudyBuddy/commit/23f1f5ef93fdabb0fe41b256c611f4f20f81fca1))

## Misc
- **deps**: Add FirebaseUI to dependencies ([`725fdb1`](https://github.com/Chan4077/StudyBuddy/commit/725fdb1dba0ce795698c1b4515b87ee04ab134d6))

<a name="v1.0.0-beta.795"></a>

# [v1.0.0-beta.795 melon-house (2017-08-24) <sub><sup>BETA</sup></sub>](https://github.com/Chan4077/StudyBuddy/compare/v1.0.0-beta.751...v1.0.0-beta.795)
## Docs
- **screenshots**: Add screenshots

## Features
- **icons**: Update icons (onboarding SVG, calendar, clipboard_check) ([`ec3dbfa`](https://github.com/Chan4077/StudyBuddy/commit/ec3dbfab387fb218d0d7e7345fddb03bc3240d46))

## Misc
- **dependencies**: Change the app intro library
- **colour-scheme**: Update colours ([`bad501e7`](https://github.com/Chan4077/StudyBuddy/commit/bad501e771567c536eaa27f3b9da7ddac46c7526))
- Add a new repo to get the latest APK from, repo is under the [Download](#download) section

## Notes
Erm... There's no release APK for this...

# [v1.0.0-beta.751 car-arithmetic (2017-08-17) <sub><sup>BETA</sup></sub>](https://github.com/Chan4077/StudyBuddy/compare/v1.0.0-beta.533...v1.0.0-beta.751)
## Changes
- Add an app intro
- Add screenshots

# [v1.0.0-beta.533 field-orange (2017-08-15) <sub><sup>BETA</sup></sub>](https://github.com/Chan4077/StudyBuddy/compare/v1.0.0-beta.520...v1.0.0-beta.533)
## Changes
- Add custom tabs.

# [v1.0.0-beta.520 flower-scissors (2017-08-15) <sub><sup>BETA</sup></sub>](https://github.com/Chan4077/StudyBuddy/compare/v1.0.0-beta.203...v1.0.0-beta.520)
## Changes
- Add firebase support.
- Add `AppUpdater` support.

# [v1.0.0-beta.203 jelly-sack (2017-08-14) <sub><sup>BETA</sup></sub>](https://github.com/Chan4077/StudyBuddy/compare/dd76f58...v1.0.0-beta.203)
## Changes
- Officially introducing `StudyBuddy`! :tada:
- Add tooltips everywhere (from Android Oreo)
- Add new task/ remove task, FABs, dialogs and snackbars
- Add license file