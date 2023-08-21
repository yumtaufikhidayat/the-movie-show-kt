# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

##---------------Begin: proguard configuration for Gson  ----------
# Gson uses generic type information stored in a class file when working with fields. Proguard
# removes such information by default, so configure it to keep all of it.
-keepattributes Signature

# For using GSON @Expose annotation
-keepattributes *Annotation*

# Gson specific classes
-dontwarn sun.misc.**
#-keep class com.google.gson.stream.** { *; }

# Application classes that will be serialized/deserialized over Gson
-keep class com.google.gson.examples.android.model.** { <fields>; }

# Prevent proguard from stripping interface information from TypeAdapter, TypeAdapterFactory,
# JsonSerializer, JsonDeserializer instances (so they can be used in @JsonAdapter)
-keep class * extends com.google.gson.TypeAdapter
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer

# Prevent R8 from leaving Data object members always null
-keepclassmembers,allowobfuscation class * {
  @com.google.gson.annotations.SerializedName <fields>;
}

# Retain generic signatures of TypeToken and its subclasses with R8 version 3.0 and higher.
-keep,allowobfuscation,allowshrinking class com.google.gson.reflect.TypeToken
-keep,allowobfuscation,allowshrinking class * extends com.google.gson.reflect.TypeToken

##---------------End: proguard configuration for Gson  ----------

# Retrofit does reflection on generic parameters. InnerClasses is required to use Signature and
# EnclosingMethod is required to use InnerClasses.
-keepattributes Signature, InnerClasses, EnclosingMethod

# Retrofit does reflection on method and parameter annotations.
-keepattributes RuntimeVisibleAnnotations, RuntimeVisibleParameterAnnotations

# Keep annotation default values (e.g., retrofit2.http.Field.encoded).
-keepattributes AnnotationDefault

# Retain service method parameters when optimizing.
-keepclassmembers,allowshrinking,allowobfuscation interface * {
    @retrofit2.http.* <methods>;
}

# Ignore annotation used for build tooling.
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement

# Ignore JSR 305 annotations for embedding nullability information.
-dontwarn javax.annotation.**

# Guarded by a NoClassDefFoundError try/catch and only used when on the classpath.
-dontwarn kotlin.Unit

# Top-level functions that can only be used by Kotlin.
-dontwarn retrofit2.KotlinExtensions
-dontwarn retrofit2.KotlinExtensions$*

# With R8 full mode, it sees no subtypes of Retrofit interfaces since they are created with a Proxy
# and replaces all potential values with null. Explicitly keeping the interfaces prevents this.
-if interface * { @retrofit2.http.* <methods>; }
-keep,allowobfuscation interface <1>

# Keep inherited services.
-if interface * { @retrofit2.http.* <methods>; }
-keep,allowobfuscation interface * extends <1>

# With R8 full mode generic signatures are stripped for classes that are not
# kept. Suspend functions are wrapped in continuations where the type argument
# is used.
-keep,allowobfuscation,allowshrinking class kotlin.coroutines.Continuation

# R8 full mode strips generic signatures from return types if not kept.
-if interface * { @retrofit2.http.* public *** *(...); }
-keep,allowoptimization,allowshrinking,allowobfuscation class <3>

# keep retrofit and okhttp
-keep class retrofit2.** { *; }
-keep class okhttp3.internal.** { *; }
-dontwarn okhttp3.internal.**
-dontwarn retrofit2.**
-keepclasseswithmembers class * {
    @retrofit2.http.* <methods>;
}

-keepattributes SourceFile,LineNumberTable        # Keep file names and line numbers.
-keep public class * extends java.lang.Exception  # Optional: Keep custom exceptions.

# keep gson serialize
-keepclassmembers,allowobfuscation class * {
  @com.google.gson.annotations.SerializedName <fields>;
}

-keepnames @dagger.hilt.android.lifecycle.HiltViewModel class * extends androidx.lifecycle.ViewModel
-keep @dagger.hilt.* class * { *; }

-keep class dagger.hilt.** { *; }
-keepclasseswithmembernames class * { @dagger.hilt.* <methods>; }
-keepclasseswithmembernames class * { @dagger.hilt.* <fields>; }

###### dagger 2.0 stuff
-keep class dagger.** { *; }
-keep interface dagger.** { *; }
-keepnames class com.taufik.**
-keepclassmembers class * {
    @javax.inject.Inject <methods>;
    @javax.inject.Inject <fields>;
    @javax.inject.Inject <init>(...);
}

# Preserve Dagger Hilt annotations
-keepattributes *Annotation*
-keepclassmembers class * {
    @dagger.hilt.* <fields>;
}

# Preserve Dagger Hilt generated code
-keep class androidx.hilt.* { *; }
-keep class dagger.hilt.* { *; }
-keep class dagger.hilt.android.** { *; }
-keep class dagger.hilt.android.internal.** { *; }
-keep class dagger.hilt.processor.** { *; }
-keep class javax.inject.* { *; }
-keep class javax.annotation.* { *; }

# keep POJO class from obfuscating
-keep class com.taufik.themovieshow.model.response.common.reviews.* { *; }
-keep class com.taufik.themovieshow.model.response.movie.cast.* { *; }
-keep class com.taufik.themovieshow.model.response.movie.detail.* { *; }
-keep class com.taufik.themovieshow.model.response.movie.discover.* { *; }
-keep class com.taufik.themovieshow.model.response.movie.nowplayingupcoming.* { *; }
-keep class com.taufik.themovieshow.model.response.movie.similar.* { *; }
-keep class com.taufik.themovieshow.model.response.movie.trending.* { *; }
-keep class com.taufik.themovieshow.model.response.movie.video.* { *; }
-keep class com.taufik.themovieshow.model.response.tvshow.cast.* { *; }
-keep class com.taufik.themovieshow.model.response.tvshow.detail.* { *; }
-keep class com.taufik.themovieshow.model.response.tvshow.discover.* { *; }
-keep class com.taufik.themovieshow.model.response.tvshow.popularairingtoday.* { *; }
-keep class com.taufik.themovieshow.model.response.tvshow.similar.* { *; }
-keep class com.taufik.themovieshow.model.response.tvshow.trending.* { *; }
-keep class com.taufik.themovieshow.model.response.tvshow.video.* { *; }