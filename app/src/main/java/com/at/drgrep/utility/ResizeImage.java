package com.at.drgrep.utility;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.util.Log;

public class ResizeImage
{
	String Rot = "0";

	public ResizeImage(File f)
	{
		try
		{
			ExifInterface exif = new ExifInterface(f.getAbsolutePath());
			Rot = exif.getAttribute(ExifInterface.TAG_ORIENTATION);
			Log.w(" TAG_ORIENTATION  ", exif.getAttribute(ExifInterface.TAG_ORIENTATION) + "");
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			// e.printStackTrace();
		}

		Bitmap selectedBitmap = resize(Drawable.createFromPath(f.getAbsolutePath()));
		saveBitmapToFile(f, selectedBitmap);

		
		try
		{
			ExifInterface exif = new ExifInterface(f.getAbsolutePath());
			exif.setAttribute(ExifInterface.TAG_ORIENTATION, Rot);
			exif.saveAttributes();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			// e.printStackTrace();
		}

		selectedBitmap.recycle();
	}

	public Bitmap drawableToBitmap(Drawable pd)
	{
		BitmapDrawable drawable = (BitmapDrawable) pd;
		Bitmap bitmap = drawable.getBitmap();
		return bitmap;
	}

	public void saveBitmapToFile(File dir, Bitmap bm)
	{
		File imageFile = dir;
		FileOutputStream fos = null;
		try
		{
			fos = new FileOutputStream(imageFile);
			bm.compress(Bitmap.CompressFormat.JPEG, 100, fos);
			fos.close();
		} catch (IOException e)
		{
		}
	}

	private Bitmap resize(Drawable image)
	{
		Bitmap b = ((BitmapDrawable) image).getBitmap();
		float odH = b.getHeight();
		float odW = b.getWidth();
		Log.w(" odH ", odH + "");
		Log.w(" odW ", odW + "");
		float newHeight = 500;
		float newWidth = 500;
		if (odH > odW)
		{
			newWidth = odW * (newHeight / odH);
		} else
		{
			newHeight = odH * (newWidth / odW);
		}

		Log.w(" newHeight ", newHeight + "");
		Log.w(" newWidth ", newWidth + "");

		Bitmap bitmapResized = Bitmap.createScaledBitmap(b, (int) newWidth, (int) newHeight, false);
		return bitmapResized;
	}
}
