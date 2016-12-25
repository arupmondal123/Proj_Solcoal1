package com.at.drgrep.activity;

import java.io.File;

import com.at.drgrep.R;
import com.at.drgrep.animation.ANIM;
import com.at.drgrep.constants.AppConstant;
import com.at.drgrep.data.ImageUri;
import com.at.drgrep.utility.IMAGE_UTIL;
import com.at.drgrep.utility.Toast;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class Congratulations extends Activity
{
	private Context context = null;
	private Activity activity = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.act_congratulations);
		
		activity  = Congratulations.this;
		
		
		((ImageView) findViewById(R.id.cross)).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v)
			{
				startLandingPage();
			}
		});
		
		((LinearLayout)findViewById(R.id.add_more_picture)).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v)
			{
				startProductUploadPage();
			}
		});
	}
	
	@Override
	public void onBackPressed()
	{
		startLandingPage();
	}
	
	private void startLandingPage()
	{
		
		Intent intent = new Intent(Congratulations.this, ProductListActivity.class);
		intent.putExtra(AppConstant.START_APP, AppConstant.START_APP_FIRST_TIME);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		Congratulations.this.finish();
		startActivity(intent);
	}
	
	
	public void startProductUploadPage()
	{
		//takePhoto();
		Intent intent = new Intent(Congratulations.this, ProductAddProductActivity.class);
		intent.putExtra(AppConstant.ADD_EDIT, AppConstant.ADD);
		// ProductListPage.this.finish();
		startActivity(intent);
		ANIM.onStartActivity(Congratulations.this);

	}
	
	/* */
	private static final int	CAPTURE_IMAGE	= 1;
	private Uri					imageUri		= null;

	private void takePhoto()
	{
		String fileName = "random1234567.jpg";
		ContentValues values = new ContentValues();
		values.put(MediaStore.Images.Media.TITLE, fileName);
		values.put(MediaStore.Images.Media.DESCRIPTION, "Image capture by camera");
		imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
		intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
		/**/ values.clear();

		startActivityForResult(intent, CAPTURE_IMAGE);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == CAPTURE_IMAGE)
		{
			String str = IMAGE_UTIL.getActualFilePathFromTempUri(activity, imageUri);

			if (resultCode == RESULT_OK)
			{
				ImageUri.images.put(str, AppConstant.Camera);
				startAddproduct();
			} else if (resultCode == RESULT_CANCELED)
			{
				clearImageUrisAndDeleteBlankFile(str);
				
				Toast.showSmallToast(context, getResources().getString(R.string.photo_was_not_taken));
			} else
			{
				clearImageUrisAndDeleteBlankFile(str);
				Toast.showSmallToast(context, getResources().getString(R.string.photo_was_not_taken));
			}
		}
	}

	private void startAddproduct()
	{
		Intent intent = new Intent(Congratulations.this, ProductAddProductActivity.class);
		intent.putExtra(AppConstant.ADD_EDIT, AppConstant.ADD);
		Congratulations.this.finish();
		startActivity(intent);
		ANIM.onStartActivity(activity);
	}

	private void clearImageUrisAndDeleteBlankFile(String str)
	{
		try
		{
			ImageUri.images.clear();
		} catch (Exception e)
		{

		}
		
		try
		{
			(new File(str)).delete();
		} catch (Exception e)
		{
			Log.e("(new File(str)).delete();", e.getMessage());
		}
	}
	
	/* */
}
