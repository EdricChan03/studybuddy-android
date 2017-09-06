**NOTE**: This document is for developers and  the curious.
<a name="latest-commits"></a>

View the latest commits [here](https://github.com/Chan4077/StudyBuddy/commits/master)

# Download
- [Download the latest APK](https://github.com/Chan4077/StudyBuddy-builds/raw/master/release/com.edricchan.studybuddy-v1.0.0-beta.751.apk)
- [View previous releases](https://github.com/Chan4077/StudyBuddy-builds/tree/master/release)
- [Latest changelog](#latest-changelog)
# Notes
The apk file you are downloading is a beta and may break at any time. Please open a new issue if that is the case and add steps for reproducing the issue.

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
4. Accept the permissions.
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

# [v1.0.0-beta.795 melon-house (2017-08-24)](https://github.com/Chan4077/StudyBuddy/compare/v1.0.0-beta.751...v1.0.0-beta.795)
## Changes
- Change the app intro library from `app-intro` to [`material-intro`](https://github.com/heinrichreimer/material-intro), which has a better UI and has an example app showcasing the library
- Add a new repo to get the latest APK from, repo is under the [Download](#download) section. :arrow_right: The new repo was meant for since there is no mobile view for the releases page.
# [v1.0.0-beta.751 car-arithmetic (2017-08-17)](https://github.com/Chan4077/StudyBuddy/compare/v1.0.0-beta.533...v1.0.0-beta.751)
## Changes
- Add `com.github.apl-devs:appintro` dependency
  - For more info, check out this [Github repo](https://github.com/apl-devs/AppIntro)
- Add a new activity: `IntroActivity`
- Make intro show only once
- Add preference to show app intro (Under `Versions & About` > `App Intro`)
- Add drawables (SVGs) for app intro
- Add more strings

# [v1.0.0-beta.533 field-orange (2017-08-15)](https://github.com/Chan4077/StudyBuddy/compare/v1.0.0-beta.520...v1.0.0-beta.533)


## Changes
- Add `com.android.support:customtabs` dependency
- Update `Preference` xml in `pref_versions.xml` to be self-closed with click handlers in the Activity java file

# [v1.0.0-beta.520 flower-scissors (2017-08-15)](https://github.com/Chan4077/StudyBuddy/compare/v1.0.0-beta.203...v1.0.0-beta.520)
## Changes
- Fix typo `MyFirstGamw` > `MyFirstGame`
- Update current status section
- Add more dependencies
  ```gradle
  compile 'com.google.firebase:firebase-storage:11.0.4'
  compile 'com.google.firebase:firebase-database:11.0.4'
  compile 'com.google.firebase:firebase-auth:11.0.4'
  compile 'com.google.android.gms:play-services-auth:11.0.4'
  compile 'com.github.javiersantos:AppUpdater:2.6.3'
  ```
- Update `google-services.json`
- Add [permissions](#permissions)
- Add `AboutDialog`
- Rename `GeneralPreferenceFragment` to `ExperimentalPreferenceFragment` and update preference XML files
- Add `VersionPreferenceFragment`
- Add drawables
- Add another `maven` url:
  ```gradle
  maven { url "https://jitpack.io" }
  ```

# [v1.0.0-beta.203 jelly-sack (2017-08-14)](https://github.com/Chan4077/StudyBuddy/compare/dd76f58...v1.0.0-beta.203)
## Changes
- Update `.gitignore`
- Add `README.md`
- Update the `minSdkVersion` to `23`
- Update `com.android.design` libraries to the latest version
- Change theme to dark
- Use white icons instead of black (dark theme)