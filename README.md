# STRV-Weather-Android-application
This is the simple Android weather app that shows current weather and forecast from openweathermap.org. It was developed as part of the interview process.

Screenshots:
[Screenshots](https://goo.gl/photos/euY74suQBQk6ULGY6)


Features
========

The app shows current weather and forecast based on users current location. 
It has settings where user can set the units of temperature or distance. 
Also user can set what kind of images want to show:
- Static images based on current weather (saves data usage)
- Dynamic images loaded from Flickr API, based on current weather 
- Dynamic images loaded from Flickr API, based on current weather and approximate location
- Dynamic images loaded from Flickr API, based on currently selected location
Users also can search for location and add them as favourite for future easy access. 

Possible feature updates:
- Swipe to delete favourited location
- Touch & hold to change position of the location in the list
- Widget
- Add bing images and other free image services
- Handle flickr unavaiable servers
- Custom photos of various type of weather on different kind of season of the year


WeatherConfig.java
------------------

This is the main configuration file and there are some important constants: addresses to API endpoints, API keys to 3rd party services etc. Make sure that all constants are set up properly.


build.gradle
------------

This is the main build script and there are 4 important constants for defining version code and version name.

* VERSION\_MAJOR
* VERSION\_MINOR
* VERSION\_PATCH
* VERSION\_BUILD

See [Versioning Your Applications](http://developer.android.com/tools/publishing/versioning.html#appversioning) in Android documentation for more info.

There are also a build config fields in this script. Check "buildTypes" configuration and make sure that all fields are set up properly for debug and release. It is very important to correctly set these true/false switches before building the APK.

* LOGS - true for showing logs
* DEV\_API - true for development API endpoint

**Important:** Following configuration should be used for release build type, intended for publishing on Google Play:

```groovy
buildConfigField "boolean", "LOGS", "false"
buildConfigField "boolean", "DEV_API", "false"
``` 


Dependencies
============

* [Volley](https://android.googlesource.com/platform/frameworks/volley/)
* [OrmLite](http://ormlite.com/)
* [Recyclerview-animators](https://github.com/wasabeef/recyclerview-animators)
* [Google Play Services](http://developer.android.com/google/play-services/index.html)
* [AppCompat](https://developer.android.com/reference/android/support/v7/appcompat/package-summary.html)
* [Android Support Library v4](http://developer.android.com/tools/extras/support-library.html)
* [GSON](http://code.google.com/p/google-gson/)
* [FloatingActionButton](https://github.com/makovkastar/FloatingActionButton)
* [ParallaxPagerTransformer](https://github.com/xgc1986/ParallaxPagerTransformer)
* [CircleIndicator](https://github.com/ongakuer/CircleIndicator)

Testing
=======

* Test app on different Android versions (handset, tablet)
* Test overdraws
* Test offline/empty/progress states
* Test migration from old version to the new one
* Test slow internet connection
* Test reboot (boot receivers, alarms, services)
* Test analytics tracking
* Monkey test (fast clicking, changing orientation)


Publishing
==========

* Set proper versions in the main build script
* Check build config fields in the main build script
* Update text info in changelog/about/help
* Add analytics events for new features
* Set Android key hash on developers.facebook.com


Developed by
============

Stepan Sanda
