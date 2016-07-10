package com.at.solcoal.utility;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import com.at.solcoal.model.M1;
import com.squareup.picasso.Picasso;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.Bitmap.CompressFormat;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

public class IMAGE_UTIL2
{
	private static final String TAG = "IMAGE_UTIL";

//	public static ArrayList<M1> getAllShownGalleryImagesPath(Activity activity)
//	{
//		Uri uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
//		String[] projection = { MediaColumns.DATA, MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
//				MediaStore.Images.Media.DATE_TAKEN, MediaStore.Images.Media.SIZE };
//		Cursor cursor = activity.getContentResolver().query(uri, projection, null, null, null);
//		int column_index_data = cursor.getColumnIndexOrThrow(projection[0]);
//		int column_index_folder_name = cursor.getColumnIndexOrThrow(projection[1]);
//		int date_taken = cursor.getColumnIndexOrThrow(projection[2]);
//		int size = cursor.getColumnIndexOrThrow(projection[3]);
//
//		ArrayList<M1> listOfAllImages = new ArrayList<M1>();
//		String absolutePathOfImage = null;
//
//		try
//		{
//			Log.i(TAG + "_Folder : ", cursor.getString(column_index_folder_name));
//
//		} catch (Exception e)
//		{
//			Log.e(TAG + "_Error!", e.getMessage());
//		}
//
//		while (cursor.moveToNext())
//		{
//			absolutePathOfImage = cursor.getString(column_index_data);
//
//			try
//			{
//				if ((new File(absolutePathOfImage)).length() > 0L)
//				{
//					listOfAllImages.add(new M1(absolutePathOfImage, cursor.getLong(date_taken)));
//				}
//
//			} catch (Exception e)
//			{
//				Log.e("Error!", e.getMessage());
//			}
//		}
//
//		Collections.reverse(listOfAllImages);
//
//		return listOfAllImages;
//	}
	
	public static String getPathFromUri(Context context, Uri uri)
	{
		String[] filePathColumn = { MediaStore.Images.Media.DATA };

		// Get the cursor
		Cursor cursor = context.getContentResolver().query(uri, filePathColumn, null, null, null);
		// Move to first row
		cursor.moveToFirst();

		int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
		String imgPath = cursor.getString(columnIndex);
		cursor.close();

		return imgPath;
	}

	public static String getActualFilePathFromTempUri(Activity activity, Uri imageUri)
	{
		/**/Log.e(TAG + "_Image _ getActualFilePathFromTempUri()", imageUri.toString());
		/**/getImageOrientationFromUri(imageUri);
		
		String[] projection = { MediaStore.Images.Media.DATA };

		Cursor cursor = null;
		try
		{
			cursor = activity.getContentResolver().query(imageUri, projection, null, null, null);
			Log.i(TAG + "_cursor = ", "activity.getContentResolver().query(imageUri, projection, null, null, null);");

		} catch (Exception e)
		{
			cursor = activity.managedQuery(imageUri, projection, null, null, null);
			Log.i(TAG + "_cursor = ", "activity.managedQuery(imageUri, projection, null, null, null);");
		}

		int column_index_data = cursor.getColumnIndexOrThrow(projection[0]);
		cursor.moveToFirst();
		String path = cursor.getString(column_index_data);

		return path;
	}

	public static int getScreenWidth(Context _context)
	{
		int columnWidth;
		WindowManager wm = (WindowManager) _context.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();

		final Point point = new Point();
		try
		{
			display.getSize(point);
		} catch (java.lang.NoSuchMethodError ignore)
		{ // Older device
			point.x = display.getWidth();
			point.y = display.getHeight();
		}
		columnWidth = point.x;
		return columnWidth;
	}

	// public static Bitmap getCompressedBitmap(String imagePath) throws
	// FileNotFoundException
	// {
	// Bitmap original = BitmapFactory.decodeStream(new FileInputStream(new
	// File(imagePath)));
	// ByteArrayOutputStream out = new ByteArrayOutputStream();
	// original.compress(Bitmap.CompressFormat.JPEG, 100, out);
	// // Bitmap decoded = BitmapFactory.decodeStream(new
	// // ByteArrayInputStream(out.toByteArray()));
	// return BitmapFactory.decodeStream(new
	// ByteArrayInputStream(out.toByteArray()));
	// }

	public static void getImageOrientationFromUri(Uri uri)
	{
		/**/
		{
			String Rot = "0";
			try
			{
				ExifInterface exif = new ExifInterface(uri.toString());
				Rot = exif.getAttribute(ExifInterface.TAG_ORIENTATION);

				Log.e(TAG + "_getFileInputStream()_exif.getAttribute(ExifInterface.TAG_ORIENTATION) : ", Rot + "...");

				// exif.setAttribute(ExifInterface.TAG_ORIENTATION, Rot);
				// exif.saveAttributes();
			} catch (IOException e)
			{
			} catch (Exception e)
			{
			}
		}
		/**/
	}

	public static FileInputStream getFileInputStream(Context context, String imagePath)
			throws FileNotFoundException, IOException, OutOfMemoryError
	{
		Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		bitmap.compress(CompressFormat.JPEG, 25, bos);
		byte[] bitmapdata = bos.toByteArray();

		/**/
		{
			String Rot = "0";
			try
			{
				ExifInterface exif = new ExifInterface(imagePath);
				Rot = exif.getAttribute(ExifInterface.TAG_ORIENTATION);

				Log.e(TAG + "_getFileInputStream()_exif.getAttribute(ExifInterface.TAG_ORIENTATION) ",
						exif.getAttribute(ExifInterface.TAG_ORIENTATION) + "...");

				exif.setAttribute(ExifInterface.TAG_ORIENTATION, Rot);
				exif.saveAttributes();
			} catch (IOException e)
			{
			} catch (Exception e)
			{
			}
		}
		/**/
		File imageFile = new File(context.getCacheDir(), getfileNameFromPath(imagePath));
		imageFile.createNewFile();
		FileOutputStream fos = new FileOutputStream(imageFile);
		fos.write(bitmapdata);
		fos.flush();
		fos.close();

		try
		{
			bitmap.recycle();
			bitmap = null;
		} catch (Exception e)
		{
		}

		return new FileInputStream(imageFile);
	}

	public static String getfileNameFromPath(String imagePath)
	{
		// return
		// imagePath.substring(imagePath.lastIndexOf(System.getProperty("line.separator"))+1);
		return "temp_image_file.jpg";
	}
	


	// public static Bitmap decodeFile(String filePath, int WIDTH, int HIGHT)
	// {
	// try
	// {
	// File f = new File(filePath);
	//
	// BitmapFactory.Options o = new BitmapFactory.Options();
	// o.inJustDecodeBounds = true;
	// BitmapFactory.decodeStream(new FileInputStream(f), null, o);
	//
	// final int REQUIRED_WIDTH = WIDTH;
	// final int REQUIRED_HIGHT = HIGHT;
	// int scale = 1;
	// while (o.outWidth / scale / 2 >= REQUIRED_WIDTH && o.outHeight / scale /
	// 2 >= REQUIRED_HIGHT)
	// scale *= 2;
	//
	// BitmapFactory.Options o2 = new BitmapFactory.Options();
	// o2.inSampleSize = scale;
	// return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
	// } catch (FileNotFoundException e)
	// {
	// e.printStackTrace();
	// }
	// return null;
	// }
}
