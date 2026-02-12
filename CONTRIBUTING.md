# Contributing guide

Hi, welcome in this guide. I'm glad you are willing to help make this app better!

## Android guidelines

Contributors may read
Android [Excellent Experiences](https://developer.android.com/quality/excellent) guide to provide
great features:

- [Guide to app architecture](https://developer.android.com/topic/architecture)
- [Design for Android](https://developer.android.com/design/ui)
- [Secure your Android apps](https://developer.android.com/security)
- [Build high-quality apps and games](https://developer.android.com/quality)

In addition, contributors must do their best to
follow [Android Architecture Best Practices](https://developer.android.com/topic/architecture/recommendations).

## Architecture

## Coding style and practices

Make sure you have [EditorConfig](https://www.jetbrains.com/help/idea/editorconfig.html) plugin
enabled, so you can directly benefit from the coding style [config file](.editorconfig).

Here are some additional practices which I'd like you to follow:

- avoid chaining functions calls, as it makes debugging and reading harder.

```kotlin
// DO
val factory = getFactoryOf(Protocol.SFTP)
val connections = factory.getConnections()
// DON'T
val connections = getFactoryOf(Protocol.SFTP).getConnections()
```

## Support a new protocol

It should be as easy as:

- duplicating an already supported protocol folder
- picking a library which will handle protocol communications
- calling library methods for each operation (listing, downloading, pushing files)
- mapping protocol to abstract types
- designing UI to fill out data

Let's take an example, assuming SFTP is supported and we now want to support FTP.

1. Duplicate [protocols/sftp](app/src/main/java/chiogros/trante/protocols/sftp) folder
   to [protocols/ftp](app/src/main/java/chiogros/trante/protocols/ftp)
2. Pick a library that brings
   a [FTP client from Maven Repository](https://mvnrepository.com/search?q=ftp), such
   as [Camel FTP](https://mvnrepository.com/artifact/org.apache.camel/camel-ftp).
   Pick the latest
   version ([4.17.0](https://mvnrepository.com/artifact/org.apache.camel/camel-ftp/4.17.0) at this
   time) or a version compatible with other libraries of the app (you'll know about this during app
   building).
   In [build.gradle.kts](app/build.gradle.kts), in `dependencies` section, add the library:

```groovy
dependencies {
    implementation("org.apache.camel:camel-ftp:4.17.0")
}
```

3. 