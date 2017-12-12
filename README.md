[![Download](https://api.bintray.com/packages/bastienpaulfr/maven/Treessence/images/download.svg) ](https://bintray.com/bastienpaulfr/maven/Treessence/_latestVersion)
[![Build Status](https://travis-ci.org/bastienpaulfr/Treessence.svg?branch=master)](https://travis-ci.org/bastienpaulfr/Treessence)

# Treessence
Some trees for Timber lib

## Set up

**build.gradle**

```
repositories {
    jcenter()
    maven { url "https://dl.bintray.com/bastienpaulfr/maven" }
}

dependencies {
    //Treessence does not include Timber
    implementation 'com.jakewharton.timber:timber:4.6.0'
    implementation 'fr.bipi.treessence:treessence:0.1.0'
}
```

### Usage

  1. Checkout [Timber](https://github.com/JakeWharton/timber)
  2. Add any trees you like

## Essences of tree

### Console

#### ThrowErrorTree

An implementation of `Timber.Tree` which throws `Error` when priority of
log is exceeded the limit. Useful for development or test environment.

```java
// Will throw an error if Timber.e() is used
Timber.plant(new ThrowErrorTree(Log.ERROR));
```

### Crash reporting

#### CrashlyticsLogTree

An implementation of `Timber.Tree` which sends log to Crashlytics.

You need to add crashlytics dependency to use this tree.

```java
// Send log to crashlytics for logs starting from warning
Timber.plant(new CrashlyticsLogTree(Log.WARN));
```

#### CrashlyticsLogExceptionTree

Same as above but use `Crashlytics.logException` instead of `Crashlytics.log()`

```java
// Send log to crashlytics for logs starting from warning
Timber.plant(new CrashlyticsLogExceptionTree(Log.ERROR));
```

### File Logging

#### FileLoggerTree

An implementation of `Timber.Tree` which sends log into a circular file.
`java.util.logging.Logger` is used for logging

```java
Tree t = new FileLoggerTree.Builder()
                     .withFileName("file%g.log")
                     .withDirName("my/dir")
                     .withSizeLimit(20000)
                     .withFileLimit(3)
                     .withMinPriority(Log.DEBUG)
                     .appendToFile(true)
                     .build();
Timber.plant(t);
```

## Licence

   Copyright [2017] Bastien PAUL

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
   