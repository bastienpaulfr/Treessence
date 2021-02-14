[![Release](https://jitpack.io/v/bastienpaulfr/Treessence.svg)](https://jitpack.io/#bastienpaulfr/Treessence)
[![Build Status](https://travis-ci.org/bastienpaulfr/Treessence.svg?branch=master)](https://travis-ci.org/bastienpaulfr/Treessence)

# Treessence
Some trees for Timber lib

## Set up

**build.gradle**

```
repositories {
    jcenter()
    maven { url "https://jitpack.io" }
}

dependencies {
    //Treessence does not include Timber
    implementation 'com.jakewharton.timber:timber:4.7.1'
    implementation 'com.github.bastienpaulfr:Treessence:1.0.0'
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

#### SystemLogTree

An implementation of `Timber.Tree` which logs into `System.out`

```java
Timber.plant(new SystemLogTree());
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

### Crash reporting

#### SentryBreadcrumbTree

An implementation of `Timber.Tree` which stores breadcrumb to Sentry instance. Breadcrumbs are then sent with an event.

You need to add sentry dependency to use this tree.

```java
Timber.plant(new SentryBreadcrumbTree(Log.DEBUG));
```

#### SentryEventTree

An implementation of `Timber.Tree` which sends Sentry events. It is useful for sending errors or logs that are not coming so often.
Otherwise your sentry instance will be flooded !

You need to add sentry dependency to use this tree.

```java
Timber.plant(new SentryEventTree(Log.ERROR));
```

You can also add a filter for SentryEvent

```java
// This will send an event to Sentry only if priority exceeds "ERROR" level and class name starts with "Sentry"
TagFilter filter = new TagFilter("Sentry.*");
SentryEventTree tree = new SentryEventTree(Log.INFO).withFilter(TagFilter);
Timber.plant(tree);
```

### Custom formatting

It is possible to use a custom formatter with trees.

```java
SystemLogTree tree = new SystemLogTree();
tree.setFormatter(LogcatFormatter.INSTANCE);
```

For timezone issue, a custom timezone can be set

```java
// Create object that will provide timestamp string
TimeStamper timeStamper = new TimeStamper("MM-dd HH:mm:ss:SSS", TimeZone.getTimeZone("GMT+2"))
// Set time stamper to LogcatFormatter
LogcatFormatter.INSTANCE.setTimeStamper(timeStamper);
// Formatter can be set to a Tree
...
```

## DSL

Since version 1.0, there is a dsl to configure Timber in Applications

### Starting Timber

```kotlin
//Create a TimberApplication to configure Timber.
startTimber {

}

```

### Adding trees


```
startTimber {
    // Add a Timber.DebugTree
    debugTree()


    // Add a Tree that log only from Log.INFO level
    releaseTree()

    // Add a simple tree with a filter and a formatter
    tree(
        { s: String, t: Throwable? ->
        // Log goes through this method and it is up to you to write those where you want
        }
    ) {
        level = Log.INFO

        filter(NoFilter())

        filter { prio, tag, m, t ->
            false
        }

        formatter { prio, tag, message ->
            ""
        }
    }

    // Add a Tree that log into logcat
    logcatTree {
        level = Log.INFO

        filter(NoFilter())

        filter { prio, tag, m, t ->
            false
        }

        formatter { prio, tag, message ->
            ""
        }
    }

    // Add a tree that log into a file
    fileTree {
        level = Log.INFO
        fileName = "myfile"
        dir = temporaryFolder.newFolder().absolutePath
        sizeLimit = 10
        fileLimit = 1
        appendToFile = true

        filter { prio, tag, m, t ->
            false
        }

        formatter { prio, tag, message ->
            ""
        }
    }

    // Add a tree that log using System.out.print()
    systemTree {
        level = Log.INFO

        filter(NoFilter())

        filter { prio, tag, m, t ->
            false
        }

        formatter { prio, tag, message ->
            ""
        }
    }

    // Add a tree that is throwing an exception when Timber.e() is called
    throwErrorTree {
        level = Log.INFO

        filter(NoFilter())

        filter { prio, tag, m, t ->
            false
        }

        formatter { prio, tag, message ->
            ""
        }
    }

    // Add a tree that put breadcrumb into Sentry API
    sentryBreadCrumbTree {
        level = Log.VERBOSE

        filter(TagFilter("^(?!.*(Sentry|App)).*"))

        filter { prio, tag, m, t ->
            false
        }

        formatter { prio, tag, message ->
            ""
        }
    }

    // Add a tree that sends an event to sentry on each log.
    sentryEventTree {
        level = Log.ERROR

        filter(TagFilter("^(?!.*(Sentry|App)).*"))

        filter { prio, tag, m, t ->
            false
        }

        formatter { prio, tag, message ->
            ""
        }
    }

    // Add a tree that is logging into a TextView
    textViewTree {
        level = Log.INFO
        append = true

        filter(NoFilter())

        filter { prio, tag, m, t ->
            false
        }

        formatter { prio, tag, message ->
            ""
        }
    }
}
```

### Uproot all trees

```kotlin
stopTimber()
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
