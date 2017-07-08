package com.foxconn.rfid.theowner.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.os.Process;
import android.util.Log;
import android.widget.Toast;

@SuppressLint({ "SimpleDateFormat", "ShowToast", "SdCardPath" })
public class CrashHandler
  implements Thread.UncaughtExceptionHandler
{
  public static final String TAG = "CrashHandler";
  private Thread.UncaughtExceptionHandler mDefaultHandler;
  private static CrashHandler INSTANCE = new CrashHandler();
  private Context mContext;
  @SuppressWarnings({ "unchecked", "rawtypes" })
private Map<String, String> infos = new HashMap();

  private DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");

  public static CrashHandler getInstance()
  {
    return INSTANCE;
  }

  public void init(Context context)
  {
    this.mContext = context;

    this.mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();

    Thread.setDefaultUncaughtExceptionHandler(this);
  }

  public void uncaughtException(Thread thread, Throwable ex)
  {
    if ((!(handleException(ex))) && (this.mDefaultHandler != null))
    {
      this.mDefaultHandler.uncaughtException(thread, ex);
    } else {
      try {
        Thread.sleep(3000L);
      } catch (InterruptedException e) {
        Log.e("CrashHandler", "error : ", e);
      }

      Process.killProcess(Process.myPid());
      System.exit(1);
    }
  }

  private boolean handleException(Throwable ex)
  {
    if (ex == null) {
      return false;
    }

    new Thread()
    {
      public void run() {
        Looper.prepare();
        Toast.makeText(CrashHandler.this.mContext, "很抱歉,程序出现异常,即将退出.", 1).show();
        Looper.loop(); }
    }
    .start();

    collectDeviceInfo(this.mContext);

    saveCrashInfo2File(ex);
    return true;
  }

  public void collectDeviceInfo(Context ctx)
  {
    try
    {
      PackageManager pm = ctx.getPackageManager();
      PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(), 1);
      if (pi != null) {
      String  versionName = (pi.versionName == null) ? "null" : pi.versionName;
       int  versionCode = pi.versionCode;
        this.infos.put("versionName", versionName);
        this.infos.put("versionCode",String.valueOf(versionCode));
      }
    } catch (PackageManager.NameNotFoundException e) {
      Log.e("CrashHandler", "an error occured when collect package info", e);
    }
    Field[] fields = Build.class.getDeclaredFields();
    Field[] arrayOfField1;
    int versionCode = (arrayOfField1 = fields).length;
    for (int versionName = 0; versionName < versionCode; ++versionName) 
    { Field field = arrayOfField1[versionName];
      try {
        field.setAccessible(true);
        this.infos.put(field.getName(), field.get(null).toString());
        Log.d("CrashHandler", field.getName() + " : " + field.get(null));
      } catch (Exception e) {
        Log.e("CrashHandler", "an error occured when collect crash info", e);
      }
    }
  }

  @SuppressWarnings("rawtypes")
private String saveCrashInfo2File(Throwable ex)
  {
    StringBuffer sb = new StringBuffer();
    for (Map.Entry entry : this.infos.entrySet()) {
      String key = (String)entry.getKey();
      String value = (String)entry.getValue();
      sb.append(key + "=" + value + "\n");
    }

    Writer writer = new StringWriter();
    PrintWriter printWriter = new PrintWriter(writer);
    ex.printStackTrace(printWriter);
    Throwable cause = ex.getCause();
    while (cause != null) {
      cause.printStackTrace(printWriter);
      cause = cause.getCause();
    }
    printWriter.close();
    String result = writer.toString();
    sb.append(result);
    try {
      long timestamp = System.currentTimeMillis();
      String time = this.formatter.format(new Date());
      String fileName = "crash-" + time + "-" + timestamp + ".log";
      if (Environment.getExternalStorageState().equals("mounted")) {
        String path = "/sdcard/crash/";
        File dir = new File(path);
        if (!(dir.exists())) {
          dir.mkdirs();
        }
        FileOutputStream fos = new FileOutputStream(path + fileName);
        fos.write(sb.toString().getBytes());
        fos.close();
      }
      return fileName;
    } catch (Exception e) {
      Log.e("CrashHandler", "an error occured while writing file...", e);
    }
    return null;
  }
}