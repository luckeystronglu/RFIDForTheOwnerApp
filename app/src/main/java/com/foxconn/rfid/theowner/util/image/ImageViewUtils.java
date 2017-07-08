package com.foxconn.rfid.theowner.util.image;

import java.io.FileNotFoundException;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.media.ThumbnailUtils;
import android.net.Uri;

public class ImageViewUtils
{
  public static Bitmap getImageThumbnail(String imagePath, int width, int height)
  {
    Bitmap bitmap = null;
    BitmapFactory.Options options = new BitmapFactory.Options();
    options.inJustDecodeBounds = true;

    bitmap = BitmapFactory.decodeFile(imagePath, options);

    int h = options.outHeight;
    int w = options.outWidth;
    int beWidth = w / width;
    int beHeight = h / height;
    int be = 1;
    if (beWidth < beHeight)
      be = beWidth;
    else {
      be = beHeight;
    }
    if (be <= 0) {
      be = 1;
    }
    options.inSampleSize = be;

    options.inJustDecodeBounds = false;
    bitmap = BitmapFactory.decodeFile(imagePath, options);

    bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height, 
      2);
    return bitmap;
  }

  public static Bitmap getImageThumbnail(Uri imageuri, int width, int height, Context context) throws FileNotFoundException
  {
    Bitmap bitmap = null;
    BitmapFactory.Options options = new BitmapFactory.Options();
    options.inJustDecodeBounds = true;

    ContentResolver contentResolver = context.getContentResolver();
    bitmap = BitmapFactory.decodeStream(
      contentResolver.openInputStream(imageuri), null, options);

    int h = options.outHeight;
    int w = options.outWidth;
    int beWidth = w / width;
    int beHeight = h / height;
    int be = 1;
    if (beWidth < beHeight)
      be = beWidth;
    else {
      be = beHeight;
    }
    if (be <= 0) {
      be = 1;
    }
    options.inSampleSize = be;

    options.inJustDecodeBounds = false;
    bitmap = BitmapFactory.decodeStream(
      contentResolver.openInputStream(imageuri), new Rect(), options);

    bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height, 
      2);
    return bitmap;
  }

  public static Bitmap rotate(Bitmap b, int degrees)
  {
    if ((degrees != 0) && (b != null)) {
      Matrix m = new Matrix();
      m.setRotate(degrees, 
        b.getWidth() / 2.0F, b.getHeight() / 2.0F);
      try {
        Bitmap b2 = Bitmap.createBitmap(
          b, 0, 0, b.getWidth(), b.getHeight(), m, true);
        if (b != b2) {
          b.recycle();
          b = b2;
        }
      }
      catch (OutOfMemoryError localOutOfMemoryError) {
      }
    }
    return b;
  }
}