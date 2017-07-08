package com.foxconn.rfid.theowner.util.image;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.Base64;

public class ImageUtils
{
  public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight)
  {
    int height = options.outHeight;
    int width = options.outWidth;

    int inSampleSize = 1;
    if ((height > reqHeight) || (width > reqWidth))
    {
      int heightRatio = Math.round(height / reqHeight);
      int widthRatio = Math.round(width / reqWidth);

      inSampleSize = (heightRatio < widthRatio) ? heightRatio : widthRatio;
    }
    return inSampleSize;
  }

  public static Bitmap getImageThumbnail(String imagePath, int width, int height)
  {
    Bitmap bitmap = null;
    BitmapFactory.Options options = new BitmapFactory.Options();
    options.inJustDecodeBounds = true;

    bitmap = BitmapFactory.decodeFile(imagePath, options);
    options.inSampleSize = calculateInSampleSize(options, width, height);

    options.inJustDecodeBounds = false;
    bitmap = BitmapFactory.decodeFile(imagePath, options);

    bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height, 2);
    return bitmap;
  }

  public static Bitmap getImageThumbnail(Uri imageuri, int width, int height, Context context)
    throws FileNotFoundException
  {
    Bitmap bitmap = null;
    BitmapFactory.Options options = new BitmapFactory.Options();
    options.inJustDecodeBounds = true;

    ContentResolver contentResolver = context.getContentResolver();
    bitmap = BitmapFactory.decodeStream(contentResolver.openInputStream(imageuri), null, options);
    options.inSampleSize = calculateInSampleSize(options, width, height);

    options.inJustDecodeBounds = false;
    bitmap = BitmapFactory.decodeStream(contentResolver.openInputStream(imageuri), new Rect(), options);

    bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height, 2);
    return bitmap;
  }

  public static Bitmap getImageThumbnail(Resources res, int resId, int reqWidth, int reqHeight)
  {
    BitmapFactory.Options options = new BitmapFactory.Options();
    options.inJustDecodeBounds = true;
    BitmapFactory.decodeResource(res, resId, options);

    options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

    options.inJustDecodeBounds = false;
    return BitmapFactory.decodeResource(res, resId, options);
  }

  public static int readPictureDegree(String path)
  {
    int degree = 0;
    try {
      ExifInterface exifInterface = new ExifInterface(path);
      int orientation = exifInterface.getAttributeInt("Orientation", 
        1);
      switch (orientation) {
      case 6:
        degree = 90;
        break;
      case 3:
        degree = 180;
        break;
      case 8:
        degree = 270;
      case 4:
      case 5:
      case 7: } } catch (IOException e) {
      e.printStackTrace();
    }
    return degree;
  }

  public static Bitmap rotaingImageView(Bitmap bitmap, int degrees)
  {
    Matrix m = new Matrix();
    m.setRotate(degrees, bitmap.getWidth() / 2.0F, bitmap.getHeight() / 2.0F);
    try {
      Bitmap bm = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, true);
      if (bitmap != bm) {
        bitmap.recycle();
        bitmap = bm;
      }
    }
    catch (OutOfMemoryError localOutOfMemoryError) {
    }
    return bitmap;
  }

  @SuppressWarnings("deprecation")
public static String getBitmapPath(Activity context, Uri uri)
  {
    String[] proj = { "_data" };

    Cursor cursor = context.managedQuery(uri, proj, null, null, null);

    int column_index = cursor.getColumnIndexOrThrow("_data");

    cursor.moveToFirst();

    return cursor.getString(column_index);
  }

  public boolean isPicture(String fileName)
  {
    String tmpName = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
    String[] imgeArray = { "bmp", "dib", "gif", "jfif", "jpe", "jpeg", "jpg", "png", "tif", "tiff", "ico" };
    for (int i = 0; i < imgeArray.length; ++i) {
      if (tmpName.equalsIgnoreCase(imgeArray[i])) {
        return true;
      }
    }
    return false;
  }

  @SuppressWarnings("unused")
public boolean isImage(File imageFile)
  {
    if (!(imageFile.exists())) {
      return false;
    }

    BitmapFactory.Options options = new BitmapFactory.Options();
    Bitmap mBitmap = BitmapFactory.decodeFile(imageFile.toString(), options);

    return ((options.outWidth > 0) && (options.outHeight > 0));
  }

  public static Bitmap scaleImg(Bitmap bm, int newWidth, int newHeight)
  {
    int width = bm.getWidth();
    int height = bm.getHeight();

    float scaleWidth = newWidth / width;
    float scaleHeight = newHeight / height;

    Matrix matrix = new Matrix();
    matrix.postScale(scaleWidth, scaleHeight);

    Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
    return newbm;
  }

  public static Bitmap scaleImg(Bitmap bm, int maxWidth)
  {
    int width = bm.getWidth();
    int height = bm.getHeight();

    float scaleWidth = maxWidth / width;

    Matrix matrix = new Matrix();
    matrix.postScale(scaleWidth, scaleWidth);

    Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
    return newbm;
  }

  public static Bitmap drawableToBitmap(Drawable drawable) {
    Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), 
      (drawable.getOpacity() != -1) ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
    Canvas canvas = new Canvas(bitmap);

    drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
    drawable.draw(canvas);
    return bitmap;
  }

  public Bitmap watermarkBitmap(Bitmap primevalImg, Bitmap waterImg, String title)
  {
    if (primevalImg == null) {
      return null;
    }
    int w = primevalImg.getWidth();
    int h = primevalImg.getHeight();

    Bitmap newb = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
    Canvas cv = new Canvas(newb);
    cv.drawBitmap(primevalImg, 0.0F, 0.0F, null);
    Paint paint = new Paint();

    if (waterImg != null) {
      int ww = waterImg.getWidth();
      int wh = waterImg.getHeight();
      paint.setAlpha(50);
      cv.drawBitmap(waterImg, w - ww + 5, h - wh + 5, paint);
    }

    if (title != null) {
      String familyName = "宋体";
      Typeface font = Typeface.create(familyName, 1);
      TextPaint textPaint = new TextPaint();
      textPaint.setColor(-65536);
      textPaint.setTypeface(font);
      textPaint.setTextSize(22.0F);

      StaticLayout layout = new StaticLayout(title, textPaint, w, StaticLayout.Alignment.ALIGN_NORMAL, 1.0F, 0.0F, true);
      layout.draw(cv);
    }

    cv.save(31);
    cv.restore();
    return newb;
  }

  public void saveFile(Bitmap bm, String filePathStr)
  {
    File filePath = new File(filePathStr.substring(0, filePathStr.lastIndexOf("/")));
    File fileName = new File(filePathStr);
    try {
      if (!(filePath.exists())) {
        filePath.mkdirs();
        fileName.createNewFile();
      } else {
        fileName.delete();
        fileName.createNewFile();
      }
      File myCaptureFile = new File(filePathStr);
      BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
      bm.compress(Bitmap.CompressFormat.JPEG, 80, bos);
      bos.flush();
      bos.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  /**
   * 转换图片成圆形
   * @param bitmap 传入Bitmap对象
   * @return
   */
  public Bitmap toRoundBitmap(Bitmap bitmap) {
          int width = bitmap.getWidth();
          int height = bitmap.getHeight();
          float roundPx;
          float left,top,right,bottom,dst_left,dst_top,dst_right,dst_bottom;
          if (width <= height) {
                  roundPx = width / 2;
                  top = 0;
                  bottom = width;
                  left = 0;
                  right = width;
                  height = width;
                  dst_left = 0;
                  dst_top = 0;
                  dst_right = width;
                  dst_bottom = width;
          } else {
                  roundPx = height / 2;
                  float clip = (width - height) / 2;
                  left = clip;
                  right = width - clip;
                  top = 0;
                  bottom = height;
                  width = height;
                  dst_left = 0;
                  dst_top = 0;
                  dst_right = height;
                  dst_bottom = height;
          }
           
          Bitmap output = Bitmap.createBitmap(width,
                          height, Config.ARGB_8888);
          Canvas canvas = new Canvas(output);
           
          final int color = 0xff424242;
          final Paint paint = new Paint();
          final Rect src = new Rect((int)left, (int)top, (int)right, (int)bottom);
          final Rect dst = new Rect((int)dst_left, (int)dst_top, (int)dst_right, (int)dst_bottom);
          final RectF rectF = new RectF(dst);

          paint.setAntiAlias(true);
           
          canvas.drawARGB(0, 0, 0, 0);
          paint.setColor(color);
          canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

          paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
          canvas.drawBitmap(bitmap, src, dst, paint);
          return output;
  }
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  /**
	 * bitmap转为base64
	 * @param bitmap
	 * @return
	 */
	public static String bitmapToBase64(Bitmap bitmap) {

		String result = null;
		ByteArrayOutputStream baos = null;
		try {
			if (bitmap != null) {
				baos = new ByteArrayOutputStream();
				bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

				baos.flush();
				baos.close();

				byte[] bitmapBytes = baos.toByteArray();
				result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (baos != null) {
					baos.flush();
					baos.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * base64转为bitmap
	 * @param base64Data
	 * @return
	 */
	public static Bitmap base64ToBitmap(String base64Data) {
		byte[] bytes = Base64.decode(base64Data, Base64.DEFAULT);
		return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
	}

  
  
  
  
}