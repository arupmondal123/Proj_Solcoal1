package com.at.drgrep.utility;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import com.at.drgrep.model.M1;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Point;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

public class IMAGE_UTIL
{
	private static final String TAG = "IMAGE_UTIL";

	public static ArrayList<M1> getAllShownGalleryImagesPath(Activity activity)
	{
		Uri uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
		String[] projection = { MediaColumns.DATA, MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
				MediaStore.Images.Media.DATE_TAKEN, MediaStore.Images.Media.SIZE, MediaStore.Images.Media.ORIENTATION };
		Cursor cursor = activity.getContentResolver().query(uri, projection, null, null, null);
		int column_index_data = cursor.getColumnIndexOrThrow(projection[0]);
		int column_index_folder_name = cursor.getColumnIndexOrThrow(projection[1]);
		int column_index_date_taken = cursor.getColumnIndexOrThrow(projection[2]);
		int column_index_size = cursor.getColumnIndexOrThrow(projection[3]);
		int column_index_orientation = cursor.getColumnIndexOrThrow(projection[4]);

		ArrayList<M1> listOfAllImages = new ArrayList<M1>();
		String absolutePathOfImage = null;
		long image_size = 0L;
		int orientation = 0;

		try
		{
			Log.i(TAG + "_Folder : ", cursor.getString(column_index_folder_name));

		} catch (Exception e)
		{
			Log.e(TAG + "_Error!", e.getMessage());
		}

		while (cursor.moveToNext())
		{
			absolutePathOfImage = cursor.getString(column_index_data);
			image_size = (new File(absolutePathOfImage)).length();
			orientation = cursor.getInt(column_index_orientation);

			try
			{
				if (image_size > 0L)
				{
					listOfAllImages.add(new M1(absolutePathOfImage, cursor.getLong(column_index_date_taken), image_size,
							orientation));
				}

			} catch (Exception e)
			{
				Log.e("Error!", e.getMessage());
			}
		}

		cursor.close();

		Collections.reverse(listOfAllImages);

		return listOfAllImages;
	}

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


	public static String getPath(Activity activity,Uri uri) {
		if( uri == null ) {
			return null;
		}
		String[] projection = { MediaStore.Images.Media.DATA };
		Cursor cursor = activity.getContentResolver().query(uri, projection, null, null, null);
		if( cursor != null ){
			int column_index = cursor
					.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();
			return cursor.getString(column_index);
		}
		return uri.getPath();
	}



	public static String getActualFilePathFromTempUri(Activity activity, Uri imageUri) {
		/**/
		getImageOrientationFromUri(imageUri);

		String[] projection = {MediaStore.Images.Media.DATA};

		Cursor cursor = null;
		try {
			//Uri newUri = handleImageUri(imageUri);
			cursor = activity.getContentResolver().query(imageUri, projection, null, null, null);
			Log.i(TAG + "_cursor = ", "activity.getContentResolver().query(imageUri, projection, null, null, null);");

		} catch (Exception e) {
			cursor = activity.managedQuery(imageUri, projection, null, null, null);
			Log.i(TAG + "_cursor = ", "activity.managedQuery(imageUri, projection, null, null, null);");
		}

		int column_index_data = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
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
		
		public static Bitmap getCompressedBitmap(String imagePath) throws FileNotFoundException
		{
			Bitmap original = BitmapFactory.decodeFile(imagePath);
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			original.compress(Bitmap.CompressFormat.JPEG, 50, out);
//			Bitmap decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));
			return BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));
		}

	public static FileInputStream getFileInputStream(Context context, String imagePath)
			throws FileNotFoundException, IOException, OutOfMemoryError
	{
		int rotation = 0;
		try
		{
			ExifInterface exif = new ExifInterface(imagePath);
			rotation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
		} catch (IOException e)
		{
		} catch (Exception e)
		{
		}

		Bitmap bitmap = rotateBitmap(BitmapFactory.decodeFile(imagePath), rotation);

		ByteArrayOutputStream bos = new ByteArrayOutputStream();

		bitmap.compress(CompressFormat.JPEG, 25, bos);

		byte[] bitmapdata = bos.toByteArray();
		
		try
		{
			bitmap.recycle();
			bitmap = null;
		} catch (Exception e)
		{
		}

		File imageFile = new File(context.getCacheDir(), getfileNameFromPath(imagePath));
		imageFile.createNewFile();

		FileOutputStream fos = new FileOutputStream(imageFile);

		fos.write(bitmapdata);
		fos.flush();
		fos.close();
		
		return new FileInputStream(imageFile);
	}

	public static Bitmap rotateBitmap(Bitmap bitmap, int orientation)
	{
		Matrix matrix = new Matrix();
		switch (orientation)
		{
			case ExifInterface.ORIENTATION_NORMAL:
				return bitmap;
			case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
				matrix.setScale(-1, 1);
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				matrix.setRotate(180);
				break;
			case ExifInterface.ORIENTATION_FLIP_VERTICAL:
				matrix.setRotate(180);
				matrix.postScale(-1, 1);
				break;
			case ExifInterface.ORIENTATION_TRANSPOSE:
				matrix.setRotate(90);
				matrix.postScale(-1, 1);
				break;
			case ExifInterface.ORIENTATION_ROTATE_90:
				matrix.setRotate(90);
				break;
			case ExifInterface.ORIENTATION_TRANSVERSE:
				matrix.setRotate(-90);
				matrix.postScale(-1, 1);
				break;
			case ExifInterface.ORIENTATION_ROTATE_270:
				matrix.setRotate(-90);
				break;
			default:
				return bitmap;
		}

		try
		{
			Bitmap bmRotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
			bitmap.recycle();
			return bmRotated;
		} catch (OutOfMemoryError e)
		{
			return bitmap;
		} catch (Exception e)
		{
			return bitmap;
		}
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
