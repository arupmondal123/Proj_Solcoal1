
package com.at.solcoal.adapter;

import java.io.File;
import java.util.ArrayList;

import com.at.solcoal.activity.ProductAddPictureActivity;
import com.at.solcoal.activity.ProductAddProductActivity;
import com.at.solcoal.constants.AppConstant;
import com.at.solcoal.data.ImageUri;
import com.at.solcoal.model.M1;
import com.squareup.picasso.Picasso;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;


public class GridViewAdapterImage extends BaseAdapter
{
	private Activity				_activity;

	private ArrayList<M1>			imageFilePathList	= new ArrayList<M1>();

	private int						imageWidth;

	private ArrayList<ImageView>	imageViewList		= new ArrayList<ImageView>();

	private String					from_				= "";

	public GridViewAdapterImage(Activity activity, ArrayList<M1> filePaths, int imageWidth, String from_)
	{
		this._activity = activity;
		this.imageFilePathList = filePaths;
		this.imageWidth = imageWidth;
		clearImageList();
		/* change */
		this.from_ = from_;
		/*/change */
	}

	@Override
	public int getCount()
	{
		if (this.imageFilePathList.size() < AppConstant.GRIDVIEW_COUNT)
		{
			return this.imageFilePathList.size();
		}
		else
		{
			return AppConstant.GRIDVIEW_COUNT;
		}
	}

	@Override
	public Object getItem(int position)
	{
		return this.imageFilePathList.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		ImageView imageView;
		if (convertView == null)
		{
			imageView = new ImageView(_activity);
		}
		else
		{
			imageView = (ImageView) convertView;
		}
		imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
		imageView.setLayoutParams(new GridView.LayoutParams(imageWidth, imageWidth));
		Picasso.with(_activity).load(Uri.fromFile(new File(imageFilePathList.get(position).toString()))).noFade()
				.resize(170, 170).centerCrop().into(imageView);
		imageView.setOnClickListener(new OnImageClickListener(position));
		/**/imageViewList.add(imageView);
		return imageView;
	}

	private void clearImageList()
	{
		try
		{
			imageViewList.clear();
		}
		catch (Exception e)
		{}
	}

	private void clearImageOnClickListener()
	{
		for (ImageView img : imageViewList)
		{
			img.setOnClickListener(null);
		}
	}

	class OnImageClickListener implements OnClickListener
	{
		int _postion;

		// constructor
		public OnImageClickListener(int position)
		{
			this._postion = position;
		}

		@Override
		public void onClick(View v)
		{
			clearImageOnClickListener();
			ImageUri.images.put(imageFilePathList.get(_postion).toString(), AppConstant.Gallery1);

			if (from_.equals("ProductListActivity"))
			{
				startProductAddProductActivity();
			}
			else
			{
				_activity.onBackPressed();
			}

		}
	}

	private void startProductAddProductActivity()
	{

		Intent intent = new Intent(_activity, ProductAddProductActivity.class);
		// TODO
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
		intent.putExtra("from_", "ProductAddPictureActivity");
		_activity.startActivity(intent);
	}

}
