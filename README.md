# instapush-kt

A Kotlin wrapper for the Instapush API.

![](https://i.imgur.com/LzTWLnR.png)

## Installation

### Gradle

Add the jitpack repository to the repositories block, like so:

```groovy
repositories {
    jcenter()
    maven { url 'https://jitpack.io' }
}
```

Then simply add ``compile 'com.github.UniquePassive:instapush-kt:-SNAPSHOT'`` to the dependencies block like so:

```groovy
dependencies {
    compile 'com.github.UniquePassive:instapush-kt:-SNAPSHOT'
}
```

## Usage

```kt
val ip = Instapush(APP_ID, APP_SECRET)
ip.push("welcome", Pair("name", "UniquePassive"))
```
