# Contributing guide

Hi, welcome in this guide. I'm glad you are willing to help make this app better!

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

As [recommended](https://developer.android.com/topic/architecture#recommended-app-arch), app is
split in 3 layers:
![3 layers of an app](https://developer.android.com/static/topic/libraries/architecture/images/mad-arch-overview.png)

In short:

- UI layer: displays application data on the screen
- Domain layer: commonly called _use cases_
- Data layer: holds application data

Data goes from the more persistent layer (data layer) to the most volatile layer (UI layer): this
way, the data layer is the only place where data remains, so it is the only source of truth.

To update data stored in the data layer: UI layer calls use cases from the domain layer (
e.g. [UpdateConnection](app/src/main/java/chiogros/trante/domain/UpdateConnectionUseCase.kt)) which
then requests data layer to update data accordingly.

This pattern is
called [Unidirectional data flow](https://developer.android.com/topic/architecture#unidirectional-data-flow).

### UI layer

There are 2 UI:

- [screens](app/src/main/java/chiogros/trante/ui/ui/screens) shown in the app, basically
- [Storage Access Framework (SAF)](app/src/main/java/chiogros/trante/ui/saf), entrypoint for remote
  access from the system's file manager

### Domain layer

Here are:

- use cases UI layer may call to get/update data from/to the data layer
- adapters to convert data from/to UI/data layer. It avoids using data layer types in UI layer, and
  vice versa

### Data layer

There are 2 data sources. So, you may think it
breaks [Single Source Of Truth (SSOT)](https://developer.android.com/topic/architecture#single-source-of-truth)
principle, but it doesn't.

The data sources are:

- [room](app/src/main/java/chiogros/trante/data/room), in-device SQLite database where app's data is
  stored (connections, IP addresses, credentials, etc.)
- [network](app/src/main/java/chiogros/trante/data/network), gateway to access files stored on
  remote storage on the network.

Each data source is organized with:

- a repository, which handles data coming from local/remote data sources (see next bullet points)
  then forward them to the domain layer
- a local data source, such as Room storage or cached data coming from remote data source
- a remote data source, data from remote servers

## Support a new protocol

It should be as easy as:

- duplicating an already supported protocol folder
- picking a library which will handle protocol communications
- calling library methods for each operation (listing, downloading, pushing files)
- mapping protocol to abstract types
- designing UI to fill out data

Let's take an example, assuming SFTP is supported, and we now want to support FTP.

1. Duplicate [protocols/sftp](app/src/main/java/chiogros/trante/protocols/sftp) folder
   to [protocols/ftp](app/src/main/java/chiogros/trante/protocols/ftp)
2. Pick a library that brings
   any FTP client from [Maven Repository](https://mvnrepository.com/search?q=ftp), such
   as [Camel FTP](https://mvnrepository.com/artifact/org.apache.camel/camel-ftp).
   Pick the latest
   version ([4.17.0](https://mvnrepository.com/artifact/org.apache.camel/camel-ftp/4.17.0) at this
   time) or a version compatible with other libraries of the app (if you manage to compile the app,
   then it's compatible with).
   In [build.gradle.kts](app/build.gradle.kts), in `dependencies` section, add the library:
    ```groovy
    dependencies {
        implementation("org.apache.camel:camel-ftp:4.17.0")
    }
    ```
3. todo
