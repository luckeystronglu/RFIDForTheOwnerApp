package com.foxconn.rfid.theowner.base;

import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;
import android.support.multidex.MultiDexApplication;
import android.util.Log;

import com.baidu.mapapi.SDKInitializer;
import com.foxconn.rfid.theowner.util.logort.LogUtils;

import net.tsz.afinal.FinalDb.DbUpdateListener;


/**
 * BaseApplication
 * @author Tom
 * @date 2014/11/24 11:40
 *
 */
public class BaseApplication extends MultiDexApplication implements DbUpdateListener {

	private  final String TAG = BaseApplication.class.getName();
	private static BaseApplication instance;

	public  ServiceConnection mServiceConnection;
	public  String UUID;
	public  int CONNECTED = -1;
	public boolean isChangeLanguage=false;

	//用来决定push msg 的跳转
	public boolean isPushDialogShow=false;
	public boolean needGoToMessageCenter=false;
	public int MessageType=1;
	public String getbyidflag = "";

	@Override
	protected void attachBaseContext(Context base) {
		super.attachBaseContext(base);

	}

	@Override
	public void onCreate() {
		LogUtils.logMessage(TAG, "onCreate()...");
		super.onCreate();
		// 在使用 SDK 各组间之前初始化 context 信息，传入 ApplicationContext
		//SDKInitializer.initialize(this);
		//集成SMSSDK
		instance = this;
		SDKInitializer.initialize(getApplicationContext());
		mServiceConnection = new ServiceConnection() {
			public void onServiceConnected(ComponentName className, IBinder rawBinder) {
				System.out.println("bluetooth service start");
				try {

				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			public void onServiceDisconnected(ComponentName classname) {
				System.out.println("bluetooth service come end");

			}
		};
/*
			//显示图象选项
			DisplayImageOptions options = new DisplayImageOptions.Builder()
//			.showStubImage(R.drawable.ic_launcher)    // 设置图片下载期间显示的图片
//			.showImageForEmptyUri(R.drawable.ic_launcher)// 设置图片Uri为空或是错误的时候显示的图片
//			.showImageOnFail(R.drawable.ic_launcher)// 设置图片加载或解码过程中发生错误显示的图片
			.cacheInMemory(false) // 设置下载的图片是否缓存在内存中
			.cacheOnDisk(true)
			 //设置下载的图片是否缓存在SD卡中
//			.displayer(new RoundedBitmapDisplayer(20)) // 设置成圆角图片
			.build();  // 创建配置过得DisplayImageOption对象

			//用法:ImageLoader.getInstance().displayImage(url, imageView, options);
			//我被坑的一次居然还要这样用,:图像加载程序配置
			ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
			    this)
			.defaultDisplayImageOptions(options)   //设置默认的显示图片选择
			    .threadPriority(Thread.NORM_PRIORITY - 2)  //线程数量,默认5个
			    .denyCacheImageMultipleSizesInMemory()   //否认在内存中缓存图像多种尺寸
			    .discCacheFileNameGenerator(new Md5FileNameGenerator()) //设置盘缓存文件名的生产者
			    .tasksProcessingOrder(QueueProcessingType.LIFO).build(); //设置任务订单处理(队列处理类型)
			ImageLoader.getInstance().init(config);  */


	}
	public static BaseApplication getInstance() {
		return instance;
	}
	@Override
	public void onLowMemory() {
		super.onLowMemory();

	}
	@Override
	public void onTerminate() {
		// TODO Auto-generated method stub
		super.onTerminate();
	}
	//改变系统语言
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		Log.e("MyApplication","onConfigurationChanged");
		super.onConfigurationChanged(newConfig);
	}
	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
		// TODO Auto-generated method stub


	}
}
