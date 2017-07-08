package com.foxconn.rfid.theowner.util.file;

import java.io.File;



import android.content.Intent;
import android.net.Uri;


public class OpenLocalFiles 
{
	public  final static String ALLOWED_URI_CHARS = "@#&=*+-_.,:!?()/~'%";
//	exsample
//	 button_doc.setOnClickListener(new OnClickListener() {
//	 		@Override
//	 		public void onClick(View v) {
//	 			Intent intent =  OpenLocalFiles.getWordFileIntent("sdcard/JAVA.doc");
//	 			startActivity(intent);
//	 		}
//	 	       });
//	
	//android��ȡһ�����ڴ�HTML�ļ���intent
	public static Intent getHtmlFileIntent(String Path)
	{
		File file = new File(Path);
		Uri uri = Uri.parse(file.toString()).buildUpon().encodedAuthority("com.android.htmlfileprovider").scheme("content").encodedPath(file.toString()).build();
		Intent intent = new Intent("android.intent.action.VIEW");
		intent.setDataAndType(uri, "text/html");
		return intent;
	}
	//android��ȡһ�����ڴ�ͼƬ�ļ���intent
	public static Intent getImageFileIntent(String Path)
	{
		File file = new File(Path);
		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		Uri uri = Uri.fromFile(file);
		intent.setDataAndType(uri, "image/*");
		return intent;
	}
	//android��ȡһ�����ڴ�PDF�ļ���intent
	public static Intent getPdfFileIntent(String Path){

//		File file = new File(Uri.encode(Path,ALLOWED_URI_CHARS));
		File file = new File(Path);
		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		Uri  uri= Uri.fromFile(file);
		
		intent.setDataAndType(uri, "application/pdf");
		return intent;
	}
	//android��ȡһ�����ڴ��ı��ļ���intent
	public static Intent getTextFileIntent(String Path)
	{ 
		File file = new File(Path);
		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		Uri uri = Uri.fromFile(file);
		intent.setDataAndType(uri, "text/plain");
		return intent;
	}
	public static Intent getSwfFileIntent(String Path)
	{ 
		File file = new File(Path);
		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		Uri uri = Uri.fromFile(file);
		intent.setDataAndType(uri, "flash/*");
		return intent;
	}

	//android��ȡһ�����ڴ���Ƶ�ļ���intent
	public static Intent getAudioFileIntent(String Path)
	{
		File file = new File(Path);
		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.putExtra("oneshot", 0);
		intent.putExtra("configchange", 0);
		Uri uri = Uri.fromFile(file);
		intent.setDataAndType(uri, "audio/*");
		return intent;
	}
	//android��ȡһ�����ڴ���Ƶ�ļ���intent
	public static Intent getVideoFileIntent(String Path)
	{
		File file = new File(Path);
		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.putExtra("oneshot", 0);
		intent.putExtra("configchange", 0);
		Uri uri = Uri.fromFile(file);
		intent.setDataAndType(uri, "video/*");
		return intent;
	}


	//android��ȡһ�����ڴ�CHM�ļ���intent
	public static Intent getChmFileIntent(String Path)
	{
		File file = new File(Path);
		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		Uri uri = Uri.fromFile(file);
		intent.setDataAndType(uri, "application/x-chm");
		return intent;
	}


	//android��ȡһ�����ڴ�Word�ļ���intent
	public static Intent getWordFileIntent(String Path)
	{
		File file = new File(Path);
		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		Uri uri = Uri.fromFile(file);
		intent.setDataAndType(uri, "application/msword");
		return intent;
	}
	public static Intent getWordXFileIntent(String Path)
	{
		File file = new File(Path);
		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		Uri uri = Uri.fromFile(file);
		intent.setDataAndType(uri, "application/vnd.openxmlformats-officedocument.wordprocessingml.document");
		return intent;
	}
	//android��ȡһ�����ڴ�Excel�ļ���intent
	public static Intent getExcelFileIntent(String Path)
	{
		File file = new File(Path);
		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		Uri uri = Uri.fromFile(file);
		intent.setDataAndType(uri, "application/vnd.ms-excel");
		return intent;
	}
	public static Intent getExcelXFileIntent(String Path)
	{
		File file = new File(Path);
		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		Uri uri = Uri.fromFile(file);
		intent.setDataAndType(uri, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
		return intent;
	}
	//android��ȡһ�����ڴ�PPT�ļ���intent
	public static Intent getPPTFileIntent(String Path)
	{
		File file = new File(Path);
		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		Uri uri = Uri.fromFile(file);
		intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
		return intent;
	}
	//android��ȡһ�����ڴ�apk�ļ���intent
	public static Intent getApkFileIntent(String Path)
	{
		File file = new File(Path);
		Intent intent = new Intent(); 
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
		intent.setAction(android.content.Intent.ACTION_VIEW); 
		intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive"); 
		return intent;
	}
}
