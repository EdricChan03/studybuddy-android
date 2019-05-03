<a name="1.0.0-rc.543"></a>
# [1.0.0-rc.543 application-presence](https://github.com/EdricChan03/StudyBuddy/compare/v1.0.0-rc.503...v1.0.0-rc.543) (2018-04-23)

## Highlights

- Added initial support for FCM! The following working features are:
  - Notification channel
  - Notification body + title
  - Notification actions

### Bug Fixes

* **interface-notification:** add extra property ([1eff242](https://github.com/EdricChan03/StudyBuddy/commit/1eff242))


### Features

* **app-icon:** version 4 of app icon ([4f8c50a](https://github.com/EdricChan03/StudyBuddy/commit/4f8c50a))
* **deps:** add crashlytics support ([f934736](https://github.com/EdricChan03/StudyBuddy/commit/f934736))
* add initial implementation of shared helper ([4e966b5](https://github.com/EdricChan03/StudyBuddy/commit/4e966b5))
* **drawable:** add icons for notifications ([dd68b35](https://github.com/EdricChan03/StudyBuddy/commit/dd68b35))
* **fcm:** add notifications support ([e9416c7](https://github.com/EdricChan03/StudyBuddy/commit/e9416c7))
* **icons:** version 5 of app icons ([ab9280b](https://github.com/EdricChan03/StudyBuddy/commit/ab9280b))
* **shared-helper:** add more strings for actions ([cceda35](https://github.com/EdricChan03/StudyBuddy/commit/cceda35))
* **shortcuts:** add games shortcut ([66618e8](https://github.com/EdricChan03/StudyBuddy/commit/66618e8))
* **styles:** customize `navigationBarColor` ([2f04727](https://github.com/EdricChan03/StudyBuddy/commit/2f04727))
* add notification actions support :tada: ([9f9476e](https://github.com/EdricChan03/StudyBuddy/commit/9f9476e))



<a name="1.0.0-rc.503"></a>
# [1.0.0-rc.503 buyer-hotel](https://github.com/EdricChan03/StudyBuddy/compare/v1.0.0-beta.795...v1.0.0-rc.503) (2018-03-21)

## Highlights

- Added adaptive icon support for shortcuts on devices running API `26` and later! You can view the commit 
[here](https://github.com/EdricChan03/StudyBuddy/commit/d963121bbcdd9b78f237fff39689cab5f05c0510).
- Notification settings are now different where its based on whether you are running the app on a device running API `25` and older, or `26` and later.

  This means that devices running Android Oreo will see settings which will take users to the specific notification category in the system Settings app where users can 
configure the setting of the notification channel.


### Bug Fixes

* **main:** remove bottom nav ([ebf5bd0](https://github.com/EdricChan03/StudyBuddy/commit/ebf5bd0))
* **manifest:** set main activity as default ([661f716](https://github.com/EdricChan03/StudyBuddy/commit/661f716))
* **menu:** Show menu items only if there is room ([1f3ecf8](https://github.com/EdricChan03/StudyBuddy/commit/1f3ecf8))
* **proguard:** add rules for okhttp ([b0fd373](https://github.com/EdricChan03/StudyBuddy/commit/b0fd373))
* **register:** remove forgot password link ([8327c8f](https://github.com/EdricChan03/StudyBuddy/commit/8327c8f))
* **register:** removed wrong link ([0d18855](https://github.com/EdricChan03/StudyBuddy/commit/0d18855))
* \'uncategorised\' spelt wrong ([dd85b80](https://github.com/EdricChan03/StudyBuddy/commit/dd85b80))
* downgrade android support libraries to 27.0.2 ([497fb86](https://github.com/EdricChan03/StudyBuddy/commit/497fb86))
* fix obsolete notices about `implementation` ([1350142](https://github.com/EdricChan03/StudyBuddy/commit/1350142))
* Update prefix of IntroActivity ([23f1f5e](https://github.com/EdricChan03/StudyBuddy/commit/23f1f5e))
* **settings:** check if list is empty ([97daf87](https://github.com/EdricChan03/StudyBuddy/commit/97daf87))
* **settings:** set dependency after preference is added to preference screen ([8ee0ab7](https://github.com/EdricChan03/StudyBuddy/commit/8ee0ab7))
* **settings:** use `ContextCompat#getColor` instead of `getResources#getColor` ([ad5a050](https://github.com/EdricChan03/StudyBuddy/commit/ad5a050))


### Features

* **deps:** Add FirebaseUI ([725fdb1](https://github.com/EdricChan03/StudyBuddy/commit/725fdb1))
* **icons:** add music and vibrate icons ([b7b0247](https://github.com/EdricChan03/StudyBuddy/commit/b7b0247))
* **icons:** introduce v2 of adaptive icons ([c951277](https://github.com/EdricChan03/StudyBuddy/commit/c951277))
* **icons:** version 3 of icons ([bdda6b9](https://github.com/EdricChan03/StudyBuddy/commit/bdda6b9))
* **settings:** add ability for preferences to save (notification fragment) ([82dc98f](https://github.com/EdricChan03/StudyBuddy/commit/82dc98f))
* **settings:** Add another switch for calendar ([6650231](https://github.com/EdricChan03/StudyBuddy/commit/6650231))
* **shortcuts:** add adaptive icon support :tada: ([d963121](https://github.com/EdricChan03/StudyBuddy/commit/d963121))
* introduce new adaptive icons :tada: ([6c53b11](https://github.com/EdricChan03/StudyBuddy/commit/6c53b11))
* **shortcuts:** add settings shortcut ([1305893](https://github.com/EdricChan03/StudyBuddy/commit/1305893))
* Add a list preference for the update channel ([1235e57](https://github.com/EdricChan03/StudyBuddy/commit/1235e57))
* add art folder containing screenshots ([96c1547](https://github.com/EdricChan03/StudyBuddy/commit/96c1547))
* Add new launcher icons :tada: ([829dfdb](https://github.com/EdricChan03/StudyBuddy/commit/829dfdb))
* add notification settings option ([72776ac](https://github.com/EdricChan03/StudyBuddy/commit/72776ac))
* Bump version [2] ([4ab0436](https://github.com/EdricChan03/StudyBuddy/commit/4ab0436))

<a name="1.0.0-rc.023"></a>
# [1.0.0-rc.023 retiree-facility](https://github.com/EdricChan03/StudyBuddy/compare/v1.0.0-beta.795...v1.0.0-rc.023) (2017-12-13)

## Highlights
- Finally! After a few months, the app icon for this app has officially changed! You can view the commit for it 
[here](https://github.com/EdricChan03/StudyBuddy/commit/829dfdbbbcae094dee6cabf40b4177a18e274a99).
- Changed the way the changelog looks like to match Angular's changelog
- This is the first release candidate for StudyBuddy! :smile: Now that the UI looks cool enough to exit beta (along with the stuff behind), issues are welcome to help me 
squash some bugs that I haven't noticed before.

### Docs
- **changelog**: Use new changelog style (you can see it right now!) (similar to Angular's changelog style)
- **StudyBuddy-builds**: Add new readme

### Features
- **icons**: Change to new app icon (Feedback welcome, created with [Sketch](https://sketchapp.com)) 
([`829dfdb`](https://github.com/EdricChan03/StudyBuddy/commit/829dfdbbbcae094dee6cabf40b4177a18e274a99))
- **settings**: Add new toggle to enable/disable calendar (WIP) ([`6650231`](https://github.com/EdricChan03/StudyBuddy/commit/66502310f7b8c4a724c899fa9a7aa9bf9ab7a631))
- **settings**: Show the current app's version ([`8f95dde`](https://github.com/EdricChan03/StudyBuddy/commit/8f95dde2bb77c67105fa7027734b7596595c7f28))
- **settings**: Add a new setting to set the update channel (WIP) ([`1235e57`](https://github.com/EdricChan03/StudyBuddy/commit/1235e5729523908836146290957872ee77a8ad69))
- **layout**: Use side navigation instead of bottom navigation ([`c4da84b`](https://github.com/EdricChan03/StudyBuddy/commit/c4da84bb2b8f21ff5a0feaf4013ca84836751125))
- **settings**: Add help menu item ([`183535a`](https://github.com/EdricChan03/StudyBuddy/commit/183535a5977d655a30983d87be8b0a4866fb34dc))

### Bug fixes
- **settings**: Remove "Enable" prefix ([`ff3d0e8`](https://github.com/EdricChan03/StudyBuddy/commit/ff3d0e8b96c837e6f6c6f4ebc79753664b4ed941))
- **app-intro**: Change activity prefix to use `My` ([`23f1f5e`](https://github.com/EdricChan03/StudyBuddy/commit/23f1f5ef93fdabb0fe41b256c611f4f20f81fca1))

### Misc
- **deps**: Add FirebaseUI to dependencies ([`725fdb1`](https://github.com/EdricChan03/StudyBuddy/commit/725fdb1dba0ce795698c1b4515b87ee04ab134d6))



<a name="1.0.0-beta.795"></a>
# [1.0.0-beta.795](https://github.com/EdricChan03/StudyBuddy/compare/v1.0.0-beta.751...v1.0.0-beta.795) (2017-09-06)


### Features

* Add new icons ([ec3dbfa](https://github.com/EdricChan03/StudyBuddy/commit/ec3dbfa))



<a name="1.0.0-beta.751"></a>
# [1.0.0-beta.751](https://github.com/EdricChan03/StudyBuddy/compare/v1.0.0-beta.533...v1.0.0-beta.751) (2017-08-17)


### Features

* Add app intro ([de4ae82](https://github.com/EdricChan03/StudyBuddy/commit/de4ae82))



<a name="1.0.0-beta.533"></a>
# [1.0.0-beta.533](https://github.com/EdricChan03/StudyBuddy/compare/v1.0.0-beta.520...v1.0.0-beta.533) (2017-08-15)


### Features

* Add ChromeCustomTabs ([c35b372](https://github.com/EdricChan03/StudyBuddy/commit/c35b372))



<a name="1.0.0-beta.520"></a>
# [1.0.0-beta.520](https://github.com/EdricChan03/StudyBuddy/compare/v1.0.0-beta.203...v1.0.0-beta.520) (2017-08-15)


### Features

* Add firebase ([96e903a](https://github.com/EdricChan03/StudyBuddy/commit/96e903a))



<a name="1.0.0-beta.203"></a>
# [1.0.0-beta.203](https://github.com/EdricChan03/StudyBuddy/compare/2379683...v1.0.0-beta.203) (2017-08-14)


### Features

* Add tooltips ([2379683](https://github.com/EdricChan03/StudyBuddy/commit/2379683))
* **license:** Add license ([f807c7c](https://github.com/EdricChan03/StudyBuddy/commit/f807c7c))



