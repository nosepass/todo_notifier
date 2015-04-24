Todo Notifier
=====================

This is mainly an example of unit and integration testing an MVP android app that uses [Kotlin](http://kotlinlang.org/docs/tutorials/kotlin-android.html),
[Dagger 2](http://google.github.io/dagger/), [Rx](https://github.com/ReactiveX/RxAndroid), and [Cupboard](https://bitbucket.org/qbusict/cupboard). The app itself shows a nagger in the Android notification bar every X minutes, where
X is a setting stored in SQLite. I chose SQLite instead of SharedPreferences solely so I could play
around with asynchronus apis.

##Setup
Aside from what build.gradle has taken care of, you need the Kotlin plugin in Android Studio. I am
using Android Studio 1.2 for the unit testing support.

##Challenges
###Kotlin
* Dagger modules and component classes must be Java, since the Dagger annotation processing runs
before Kotlin classes are compiled. Parts of Application had to be in Java as well, since Dagger
initialization also needed to be in Java.
* Dagger cannot access Kotlin fields, so you have to create and annotate setters with @Inject instead.
* Using @Named Dagger dependencies needed another variation, with @Named being on the parameter of
 the setter.
* Kotlin cannot be used for unit tests, the test runner can't see the classes created by kotlin-gradle
for some reason.
* Static initializers do not exist in Kotlin, and were the suggested way to configure Cupboard. I
had to use an alternate method of registering model classes for Cupboard.
* Mocking Kotlin classes with Mockito doesn't work right. When you call
 when(mock(SomeOpenKotlin.class)).someMethod(), Mockito is supposed to prevent the invocation of
 someMethod and just use it to declare mocking behavior. However the kotlin implementation of the
 someMethod always gets called for some reason. A workaround is to define an interface/trait and
 mock that instead of the concrete implementation. Strangely enough, when an interface is available,
 you are able to mock the concrete class as well.

###Structuring Dagger for mocking
With Dagger 2 you have to create alternate components and modules for testing, since you can't
 extend and override the behavior of your original modules. These were was placed in src/debug for
 use by both the unit and instrumentation tests. However you need to maintain the debug versions in
 lockstep with the originals.

### Unit Testing
I decided to not use the unitTests.returnDefaultValues flag in my build.gradle. This forces me to
 mock any Android classes I use, lest they throw a RuntimeException. I was taken by surprise when
 libraries such as RxAndroid used the Android api indirectly and in a difficult to mock way, such
 as the AndroidSchedulers class.