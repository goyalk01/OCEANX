# ProGuard/R8 Configuration for FreshCart Grocery Delivery App
# This file configures code shrinking, obfuscation, and optimization for release builds

# =============================================================================
# GENERAL RULES
# =============================================================================

# Keep line numbers for crash reporting
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile

# Keep custom application classes
-keep class com.oceanx.freshcart.** { *; }
-keep interface com.oceanx.freshcart.** { *; }

# Keep all enums
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# Keep classes with native methods
-keepclasseswithmembernames class * {
    native <methods>;
}

# =============================================================================
# ANDROID FRAMEWORK
# =============================================================================

# Keep Android framework classes
-keep class android.** { *; }
-keep interface android.** { *; }
-dontwarn android.**

# Keep Android resources
-keep class **.R$* { *; }
-keep class **.BuildConfig { *; }

# =============================================================================
# ANDROIDX LIBRARIES
# =============================================================================

# Navigation Component
-keep class androidx.navigation.** { *; }
-keepclassmembers class * implements androidx.navigation.NavArgs {
    public static *** fromBundle(android.os.Bundle);
}

# Room Database
-keep class androidx.room.** { *; }
-keep @androidx.room.Entity class * { *; }
-keep @androidx.room.Dao class * { *; }
-keep @androidx.room.Database class * { *; }
-dontwarn androidx.room.paging.**

# ViewModel & LiveData
-keep class androidx.lifecycle.** { *; }
-keep class androidx.lifecycle.ViewModel { *; }
-keepclassmembers class androidx.lifecycle.ViewModel {
    <init>();
}

# RecyclerView & CardView
-keep class androidx.recyclerview.** { *; }
-keep class androidx.cardview.** { *; }

# ViewPager2
-keep class androidx.viewpager2.** { *; }

# ConstraintLayout
-keep class androidx.constraintlayout.** { *; }

# =============================================================================
# MATERIAL DESIGN 3
# =============================================================================

-keep class com.google.android.material.** { *; }
-dontwarn com.google.android.material.**

# Keep Material Components
-keep interface com.google.android.material.** { *; }
-keep public class com.google.android.material.R$* {
    public static <fields>;
}

# =============================================================================
# KOTLIN & COROUTINES
# =============================================================================

# Kotlin
-keepclassmembers class kotlin.Metadata {
    public <methods>;
}

# Keep Kotlin specific annotations
-keepattributes *Annotation*
-keep class kotlin.** { *; }
-keep class kotlinx.** { *; }
-dontwarn kotlin.**
-dontwarn kotlinx.**

# Coroutines
-keep class kotlinx.coroutines.** { *; }
-keep interface kotlinx.coroutines.** { *; }
-dontwarn kotlinx.coroutines.**

# =============================================================================
# GLIDE (Image Loading)
# =============================================================================

-keep class com.bumptech.glide.** { *; }
-keep public enum com.bumptech.glide.load.ImageHeaderParser$** {
    **[] $VALUES;
    public *;
}
-keep class * extends com.bumptech.glide.module.AppGlideModule {
    public <init>();
}
-keep class * extends com.bumptech.glide.load.model.ModelLoader {
    public <init>();
}
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
    **[] $VALUES;
    public *;
}

# =============================================================================
# PRESERVE MODEL CLASSES (Domain & Data Models)
# =============================================================================

-keep class com.oceanx.freshcart.domain.model.** { *; }
-keep class com.oceanx.freshcart.data.model.** { *; }
-keep class com.oceanx.freshcart.data.local.entity.** { *; }

# Keep all data classes and sealed classes
-keepclassmembers class * {
    *** copy(...);
    *** component*(...);
}

# =============================================================================
# REMOVE LOGGING IN RELEASE
# =============================================================================

# Remove Log.d, Log.v, Log.i calls
-assumenosideeffects class android.util.Log {
    public static *** d(...);
    public static *** v(...);
    public static *** i(...);
}

# =============================================================================
# OPTIMIZATION SETTINGS
# =============================================================================

# Use aggressive optimization
-optimizationpasses 5
-allowaccessmodification

# =============================================================================
# ADDITIONAL SAFETY
# =============================================================================

# Suppress warnings for safe-to-ignore issues
-dontwarn javax.**
-dontwarn org.w3c.dom.**
-dontwarn org.xml.sax.**
-dontwarn com.sun.**
