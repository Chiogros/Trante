# Contributing guide

Hi, welcome in this guide. I'm glad you are willing to help make this app better!

## Guidelines

### Architecture

Contributors may read
Android [Excellent Experiences](https://developer.android.com/quality/excellent) guide to provide
great features:

- [Guide to app architecture](https://developer.android.com/topic/architecture)
- [Design for Android](https://developer.android.com/design/ui)
- [Secure your Android apps](https://developer.android.com/security)
- [Build high-quality apps and games](https://developer.android.com/quality)

In addition, contributors must do their best to
follow [Android Architecture Best Practices](https://developer.android.com/topic/architecture/recommendations).

### Coding style and practices

Make sure you have [EditorConfig](https://www.jetbrains.com/help/idea/editorconfig.html) plugin
enabled, so you can directly benefit from the coding style [config file](.editorconfig).

Here are some additional practices which I'd like you to follow:

- do not chain functions calls, as it makes debugging harder.
- since Kotlin Smart-cast may fail in some scenarios, using `!!` is only allowed when a comment
  describes which functions are checking for the value nullity.

## QA

### _I want to support a new protocol_

Sure!
