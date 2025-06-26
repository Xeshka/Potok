# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.

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
-keepattributes LineNumberTable,SourceFile

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

# ===== HILT =====
-dontwarn com.google.dagger.hilt.**
-keep class dagger.hilt.** { *; }
-keep class javax.inject.** { *; }
-keep class * extends dagger.hilt.android.lifecycle.HiltViewModel { *; }

# ===== RETROFIT & OKHTTP =====
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepattributes Signature
-keepattributes Exceptions
-keepclasseswithmembers class * {
    @retrofit2.http.* <methods>;
}

-dontwarn okhttp3.**
-dontwarn okio.**
-dontwarn javax.annotation.**
-dontwarn org.conscrypt.**
-dontwarn org.bouncycastle.**
-dontwarn org.openjsse.**

# ===== KOTLINX SERIALIZATION =====
-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.AnnotationsKt
-dontwarn kotlinx.serialization.**

-keep,includedescriptorclasses class ru.kolesnik.potok.**$$serializer { *; }
-keepclassmembers class ru.kolesnik.potok.** {
    *** Companion;
}
-keepclasseswithmembers class ru.kolesnik.potok.** {
    kotlinx.serialization.KSerializer serializer(...);
}

# ===== ROOM =====
-keep class * extends androidx.room.RoomDatabase
-keep @androidx.room.Entity class *
-dontwarn androidx.room.paging.**

# ===== COMPOSE =====
-keep class androidx.compose.** { *; }
-dontwarn androidx.compose.**

# ===== COROUTINES =====
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}
-keepclassmembers class kotlinx.coroutines.** {
    volatile <fields>;
}

# ===== МОДЕЛИ ДАННЫХ =====
-keep class ru.kolesnik.potok.core.model.** { *; }
-keep class ru.kolesnik.potok.core.network.model.** { *; }
-keep class ru.kolesnik.potok.core.database.entitys.** { *; }

# ===== NAVIGATION =====
-keep class * extends androidx.navigation.Navigator
-keep class androidx.navigation.** { *; }

# ===== ОБЩИЕ ПРАВИЛА =====
-keepclassmembers,allowobfuscation class * {
    @com.google.gson.annotations.SerializedName <fields>;
}

# Сохраняем enum'ы
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# Reflection для Hilt
-keepclassmembers class * {
    @dagger.hilt.android.qualifiers.* *;
}

# ===== FIREBASE (если используется) =====
-keep class com.google.firebase.** { *; }
-dontwarn com.google.firebase.**

# ===== SSL/TLS =====
-keep class nl.altindag.ssl.** { *; }
-dontwarn nl.altindag.ssl.**

# ===== ОТЛАДОЧНАЯ ИНФОРМАЦИЯ =====
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile