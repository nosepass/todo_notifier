Todo Notifier
=====================

This is mainly an example of unit and integration testing an MVP android app that uses [Kotlin],
[Dagger], [Rx], and [Cupboard]. The app itself shows a nagger in the Android notification bar every X minutes, where
X is a setting stored in SQLite. I chose SQLite instead of SharedPreferences solely so I could play
around with asynchronus apis.

##Setup
Aside from what build.gradle has taken care of, you need a few plugins in Android Studio.

##Challenges
###Kotlin
* Dagger modules and component classes must be Java, since the Dagger annotation processing runs
before Kotlin classes are compiled. Parts of Application had to be in Java as well, since Dagger
initialization also needed to be in Java.
* Dagger cannot access Kotlin fields, so you have to annotate setters with @Inject instead.
* Kotlin cannot be used for unit tests, the test runner can't see the classes created by kotlin-gradle
for some reason.
* Static initializers do not exist in Kotlin, and were the suggested way to configure Cupboard. I
had to use an alternate method of registering model classes for Cupboard.

###Structuring Dagger for mocking
With Dagger 2 you have to create alternate components and modules for testing, since you can't
 extend and override the behavior of your original modules. These were was placed in src/debug for
 use by both the unit and instrumentation tests.

### Unit Testing
I decided to not use the unitTests.returnDefaultValues flag in my build.gradle. This forces me to
 mock any Android classes I use, lest they throw a RuntimeException. I was taken by surprise when
 libraries such as RxAndroid used the Android api indirectly and in a difficult to mock way, such
 as the AndroidSchedulers class.