
package com.at.solcoal.activity;

import java.io.File;

import com.at.solcoal.R;
import com.at.solcoal.adapter.GridViewAdapterImage;
import com.at.solcoal.animation.ANIM;
import com.at.solcoal.constants.AppConstant;
import com.at.solcoal.data.ImageUri;
import com.at.solcoal.utility.GridViewWithHeaderAndFooter;
import com.at.solcoal.utility.IMAGE_UTIL;
import com.at.solcoal.utility.Toast;
import com.at.solcoal.utility.Typefaces;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;


public class ProductAddPictureActivity extends Activity
{
	private Context						context					= null;

	private Activity					activity				= null;

	private ProductAddPictureActivity	cameraActivity			= null;

	private Typeface					tf						= null;

	private GridViewWithHeaderAndFooter	gridView				= null;

	private GridViewAdapterImage		adapter					= null;

	// private ArrayList<M1> imagePaths = null;
	private int							w						= 170;

	/**/
	private static final int			CAPTURE_IMAGE			= 1;

	private static final int			LOAD_IMAGE_FROM_GALLERY	= 2;

	private Uri							imageUri				= null;
	// private
	// ProgressDialog
	// pDialog =
	// null;

	private String						from					= "";

	/**/
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.act_add_pictures);
		context = ProductAddPictureActivity.this;
		activity = ProductAddPictureActivity.this;
		cameraActivity = ProductAddPictureActivity.this;
		tf = Typefaces.get(context, AppConstant.app_font);
		// setTypeFaces();
		gridView = (GridViewWithHeaderAndFooter) findViewById(R.id.grid_view);
		// imagePaths = new
		// ArrayList<M1>(IMAGE_UTIL.getAllShownGalleryImagesPath(activity));
		
		/*
		 * 
		 * 
		 * 
		 * 
		 * */
		
		/* change */
		try
		{
			from = getIntent().getExtras().getString("from_");
		}
		catch (Exception e)
		{
			from = "";
		}
		/* /change */
		
		/*
		 * 
		 * 
		 * 
		 * 
		 * */

		InitializeGridLayout();
		back();
		takePhoto();
		fromGallery();

		/*
		 * 
		 * 
		 * 
		 * 
		 * */

	}

	private void takePhoto()
	{
		((RelativeLayout) findViewById(R.id.take_photo)).setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event)
			{
				if (event.getAction() == MotionEvent.ACTION_UP)
				{
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
					v.setPressed(false);
				}
				if (event.getAction() == MotionEvent.ACTION_DOWN)
				{
					v.setPressed(true);
				}
				if (event.getAction() == MotionEvent.ACTION_CANCEL)
				{
					v.setPressed(false);
				}
				return true;
			}
		});
	}

	private void fromGallery()
	{
		((RelativeLayout) findViewById(R.id.select_from_gallery)).setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event)
			{
				if (event.getAction() == MotionEvent.ACTION_UP)
				{
					startActivityToShowLocalImages();
					v.setPressed(false);
				}
				if (event.getAction() == MotionEvent.ACTION_DOWN)
				{
					v.setPressed(true);
				}
				if (event.getAction() == MotionEvent.ACTION_CANCEL)
				{
					v.setPressed(false);
				}
				return true;
			}
		});
	}

	// private void startActivityToShowImages()
	// {
	// /* this activity will show local and web images */
	// Intent intent = new Intent(Intent.ACTION_PICK,
	// android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
	// startActivityForResult(intent, LOAD_IMAGE_FROM_GALLERY);
	// }

	private void startActivityToShowLocalImages()
	{
		/* this activity will only show local images */
		/*Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
		startActivityForResult(Intent.createChooser(intent, "Complete action using"), LOAD_IMAGE_FROM_GALLERY);*/

		Intent i = new Intent(
				Intent.ACTION_PICK,
				android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
		);
		startActivityForResult(i, LOAD_IMAGE_FROM_GALLERY);

	}

	private void back()
	{
		((LinearLayout) findViewById(R.id.handle)).setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event)
			{
				if (event.getAction() == MotionEvent.ACTION_UP)
				{
					onBackPressed();
					v.setPressed(false);
				}
				if (event.getAction() == MotionEvent.ACTION_DOWN)
				{
					v.setPressed(true);
				}
				if (event.getAction() == MotionEvent.ACTION_CANCEL)
				{
					v.setPressed(false);
				}
				return true;
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == CAPTURE_IMAGE)
		{
			String str = IMAGE_UTIL.getActualFilePathFromTempUri(activity, imageUri);
			//String str = IMAGE_UTIL.getPath(activity, imageUri);

			if (resultCode == RESULT_OK)
			{
				ImageUri.images.put(str, AppConstant.Camera);
				if (from.equals("ProductListActivity"))
				{
					startProductAddProductActivity();
				}
				else
				{
					onBackPressed();
				}
			}
			else if (resultCode == RESULT_CANCELED)
			{
				try
				{
					(new File(str)).delete();
				}
				catch (Exception e)
				{
					Log.e("(new File(str)).delete();", e.getMessage());
				}
				Toast.showSmallToast(context, getResources().getString(R.string.photo_was_not_taken));
			}
			else
			{
				try
				{
					(new File(str)).delete();
				}
				catch (Exception e)
				{
					Log.e("(new File(str)).delete();", e.getMessage());
				}
				Toast.showSmallToast(context, getResources().getString(R.string.photo_was_not_taken));
			}
		}
		else if (requestCode == LOAD_IMAGE_FROM_GALLERY && resultCode == RESULT_OK && null != data)
		{
			String str = IMAGE_UTIL.getActualFilePathFromTempUri(activity, data.getData());
			//Toast.showSmallToast(context, "str="+str);
			long file_length = (new File(str)).length();
			if (file_length > 2 * 1024 * 1024) // 2 mb
			{
				Toast.showSmallToast(context, "Image Size too big.");
			}
			else if (file_length > 0L)
			{
				ImageUri.images.put(str, AppConstant.Gallery2);
				if (from.equals("ProductListActivity"))
				{
					startProductAddProductActivity();
				}
				else
				{
					onBackPressed();
				}
			}
		}
	}

	private void startProductAddProductActivity()
	{

		Intent intent = new Intent(ProductAddPictureActivity.this, ProductAddProductActivity.class);
		// TODO
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
		intent.putExtra("from_", "ProductAddPictureActivity");
		startActivity(intent);
	}

	@Override
	public void onBackPressed()
	{
		ProductAddPictureActivity.this.finish();
		ANIM.onBack(activity);
	}

	private void InitializeGridLayout()
	{
		int NUM_OF_COLUMNS = 3;
		int GRID_PADDING = 5; // Gridview image padding in dp
		Resources r = getResources();
		float padding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, GRID_PADDING, r.getDisplayMetrics());
		w = (int) ((IMAGE_UTIL.getScreenWidth(context) - ((NUM_OF_COLUMNS + 1) * padding)) / NUM_OF_COLUMNS);
		gridView.setNumColumns(NUM_OF_COLUMNS);
		gridView.setColumnWidth(w);
		gridView.setStretchMode(GridView.NO_STRETCH);
		gridView.setPadding((int) padding, (int) padding, (int) padding, (int) padding);
		gridView.setHorizontalSpacing((int) padding);
		gridView.setVerticalSpacing((int) padding);
		{
			LinearLayout ll_ = (LinearLayout) findViewById(R.id.ll_);
		}
		LinearLayout ll = new LinearLayout(context);
		int p = (int) getDpFromPx(112);
		ll.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, p));
		gridView.addHeaderView(ll);
		adapter = new GridViewAdapterImage(ProductAddPictureActivity.this,
				IMAGE_UTIL.getAllShownGalleryImagesPath(activity), w, from);
		gridView.setAdapter(adapter);
	}

	public float getDpFromPx(float dp)
	{
		return dp * getResources().getDisplayMetrics().density;
	}
}
