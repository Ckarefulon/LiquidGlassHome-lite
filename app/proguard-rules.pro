# 展示用仓库：不混淆类名/方法名，让崩溃堆栈直接可读，但仍保留 R8 的
# 代码裁剪与资源裁剪（体积主要靠这层）。

# 保留 Kotlin 元数据与序列化生成的伴生对象
-keepattributes *Annotation*, InnerClasses, Signature, EnclosingMethod, RuntimeVisibleAnnotations, AnnotationDefault
-keepclassmembers class **$$serializer { *; }
-keep class **$$serializer { *; }
-keepclassmembers class kotlinx.serialization.json.** { *; }

# Compose / Miuix 通过反射访问的点
-keep class top.yukonga.miuix.kmp.** { *; }

# 不混淆（堆栈可读）
-dontobfuscate

# 去掉 Log 的调试级别（release 不需要 verbose/debug）
-assumenosideeffects class android.util.Log {
    public static int v(...);
    public static int d(...);
}
