// Project-level build configuration
plugins {
    id("com.android.application") version "8.5.0" apply false
    id("com.android.library") version "8.5.0" apply false
    kotlin("android") version "2.0.0" apply false
    id("androidx.navigation.safeargs.kotlin") version "2.7.6" apply false
        kotlin("kapt") version "2.0.0" apply false
}

// Shared version constants for all modules
extra.apply {
    set("compileSdkVersion", 34)
    set("minSdkVersion", 21)
    set("targetSdkVersion", 34)
    set("buildToolsVersion", "34.0.0")
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
