package com.at.solcoal.utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class BITMAP
{
	public static Bitmap filePathToBitmap(String _path) throws FileNotFoundException
	{
		Bitmap bitmap = null;
		File f = new File(_path);
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inPreferredConfig = Bitmap.Config.ARGB_8888;
		bitmap = BitmapFactory.decodeStream(new FileInputStream(f), null, options);
		return bitmap;
	}
	public static Bitmap fileToBitmap(File file) throws FileNotFoundException
	{
		return filePathToBitmap(file.getAbsolutePath());
	}
}
