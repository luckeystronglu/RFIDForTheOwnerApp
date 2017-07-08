-optimizationpasses 5          # 指定代码的压缩级别

-dontusemixedcaseclassnames   # 是否使用大小写混合

-dontpreverify           # 混淆时是否做预校验

-verbose                # 混淆时是否记录日志

-dontskipnonpubliclibraryclasses #指定不去忽略非公共库的类的成员

-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*  # 混淆时所采用的算法

-keep public class * extends android.app.Activity      # 保持哪些类不被混淆
-keep public class * extends android.app.Application   # 保持哪些类不被混淆
-keep public class * extends android.app.Service       # 保持哪些类不被混淆
-keep public class * extends android.content.BroadcastReceiver  # 保持哪些类不被混淆
-keep public class * extends android.content.ContentProvider    # 保持哪些类不被混淆
-keep public class * extends android.app.backup.BackupAgentHelper # 保持哪些类不被混淆
-keep public class * extends android.preference.Preference        # 保持哪些类不被混淆
-keep public class com.android.vending.licensing.ILicensingService    # 保持哪些类不被混淆


-keepattributes *Annotation* #保护代码中的Annotation不被混淆

-keepattributes Signature #避免混淆泛型

#生成映射文件
-verbose
-printmapping proguardMapping.txt
#抛出出异常的行号
-keepattributes SourceFile,LineNumberTable

-keepclasseswithmembernames class * {  # 保持 native 方法不被混淆
    native <methods>;
}


-keepclasseswithmembers class * extends android.view.View{# 保持自定义控件类不被混淆
  *** get*();
  void set*(***);
  public <init>(android.content.Context);
   public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
-keepclassmembers class * extends android.app.Activity { # 保留在activity的方法中参数是view的方法
    public void *(android.view.View);
}
-keepclassmembers enum * {     # 保持枚举 enum 类不被混淆
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
-keep class * implements android.os.Parcelable { # 保持 Parcelable 不被混淆
    public static final android.os.Parcelable$Creator *;
}

#保持support包不被混淆
-keep public class * extends android.support.**

#保持 Serializable 不被混淆
-keepnames class * implements java.io.Serializable
-keepclassmembers class * implements java.io.Serializable{

 static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    !static !transient <fields>;
    !private <fields>;
    !private <methods>;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

#保持R下的所有类及其方法，都不能被混淆
#不混淆资源类
-keepclassmembers class **.R$* {
    public static <fields>;
}

#保持onXXEvent的回调，不能被混淆
-keepclassmembers class *{
  void *(**On*Event);
}

#自定义部分 start

-keep interface android.bluetooth.** {*;}
-dontwarn  android.bluetooth.**
-keep class  android.** {*;}
-dontwarn android.**
-keep class  com.samsung.** {*;}
-dontwarn  com.samsung.**
-keep class vi.com.gdi.bgl.android.**{ *; }
-keep class net.tsz.afinal.**{ *; }
-keep class com.alipay.**{ *; }
-keep class org.codehaus.jackson.**{ *; }
-keep class cn.sharesdk.**{ *; }
-keep class com.tencent.mm.**{ *; }
-keep class com.google.zxing.**{ *; }
-keep class m.framework.**{ *; }
-keep class com.androidquery.**{ *; }
-keep class com.unionpay.**{ *; }
-keep class com.google.gson.**{ *; }
-keep class com.baoyz.swipmenulistview.**{ *; }
-keep class com.ta.utdid2.**{ *; }
-keep class com.ut.device.**{ *; }
-keep class org.codehaus.jackson.**{ *; }
-keep class com.alipay.mobilesecuritysdk.**{ *; }


-keep class vi.com.gdi.bgl.android.**{*;}
-keep interface com.baidu.** {*;}
-keep class  com.baidu.** {*;}
-dontwarn com.baidu.**

-dontwarn com.sina.**
-keep class com.sina.**{*;}

-keep class com.uuhelper.Application.** { *; }
-keep class net.sourceforge.zbar.** { *; }
-keep class com.android.vending.licensing.ILicensingService

-keep class android.support.v4.** { *; }
-keep interface android.support.v4.** { *; }

-keep class com.tencent.** { *; }

-keep interface com.google.** {*;}
-keep class com.google.**{ *; }
-dontwarn com.google.**

-keep class sun.misc.Unsafe { *; }

-keep class  com.sina.weibo.sdk.** {*;}
-keep class  com.sina.weibo.sdk.** {*;}

-keep interface javax.** {*;}
-keep class  javax.** {*;}
-dontwarn javax.**

-keep interface java.** {*;}
-keep class   java.** {*;}
-dontwarn  java.**

-keep interface com.fasterxml.**{*;}
-keep class  com.fasterxml.** {*;}
-dontwarn com.fasterxml.**

-keep interface com.juts.**  {*;}
-keep class  com.juts.** {*;}
-dontwarn com.juts.**

-keep interface net.sf.** {*;}
-keep class  net.sf.** {*;}
-dontwarn net.sf.**

-keep interface junit.** {*;}
-keep class  junit.** {*;}
-dontwarn junit.**

-keep interface org.** {*;}
-keep class  org.** {*;}
-dontwarn org.**

-keep interface com.sun.** {*;}
-keep class  com.sun.** {*;}
-dontwarn com.sun.**


#-keep interface com.tom.customviews.** {*;}
#-keep class  com.tom.customviews.** {*;}
#-dontwarn com.tom.customviews.**

-keep class com.foxconn.rfid.theowner.model.**{*;}
-keep class com.yzh.rfidbike.app.**{*;}
-keep class com.mob.**{*;}
-keep class com.handmark.**{*;}
-keep class com.akexorcist.**{*;}
-keep class com.bumptech.**{*;}
-keep class cn.smssdk.**{*;}
-keep class cn.sharesdk.**{*;}
