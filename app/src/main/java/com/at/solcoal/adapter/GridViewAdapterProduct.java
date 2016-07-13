
package com.at.solcoal.adapter;

import com.at.solcoal.R;
import com.at.solcoal.activity.ProductDetailsActivity;
import com.at.solcoal.activity.ProductListActivity;
import com.at.solcoal.animation.ANIM;
import com.at.solcoal.constants.AppConstant;
import com.at.solcoal.data.GridViewAdapterProductData;
import com.at.solcoal.data.ProductConciseList;
import com.at.solcoal.model.Product_Concise;
import com.at.solcoal.utility.StringSizeLimit;
import com.squareup.picasso.Picasso;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


public class GridViewAdapterProduct extends BaseAdapter
{
	private Activity		activity		= null;

	private Context			context			= null;

	private int				imageWidth;

	// private ArrayList<Product_Concise> productConciseList = new
	// ArrayList<Product_Concise>();
	// private ArrayList<OnImageClickListener> imageOnImageClickListenerList =
	// new ArrayList<OnImageClickListener>();
	// private ArrayList<ImageView> imagelist = new ArrayList<ImageView>();
	private String			rupeeSign		= "";

	/* change */
	private ProductListActivity	productListActivity	= null;
	/* /change */

	// public GridViewAdapterProduct(Activity activity,
	// ArrayList<Product_Concise> productConciseList2, int imageWidth)
	// {
	// this.activity = activity;
	// this.imageWidth = imageWidth;
	// this.productConciseList = productConciseList2;
	// this.context = activity.getApplicationContext();
	// rupeeSign = context.getResources().getString(R.string.rupee_sign);
	// clearImageList();
	// }
	public GridViewAdapterProduct(Activity activity, int imageWidth, ProductListActivity productListActivity)
	{
		this.activity = activity;
		this.imageWidth = imageWidth;
		// this.productConciseList = productConciseList2;
		this.context = activity.getApplicationContext();
		rupeeSign = context.getResources().getString(R.string.rupee_sign);
		clearImageList();
		/* change */
		this.productListActivity = productListActivity;
		/* /change */
	}

	@Override
	public int getCount()
	{
		return ProductConciseList.productConciseList.size();
	}

	@Override
	public Product_Concise getItem(int position)
	{
		return ProductConciseList.productConciseList.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		H viewHolder = null;
		if (convertView == null)
		{
			convertView = LayoutInflater.from(context).inflate(R.layout.item_layout, null);
			viewHolder = new H();
			viewHolder.l_layout = (LinearLayout) convertView.findViewById(R.id.l_layout);
			viewHolder.image = (ImageView) convertView.findViewById(R.id.image);
			//viewHolder.image.setLayoutParams(new LinearLayout.LayoutParams(imageWidth, imageWidth));
			viewHolder.p_name = (TextView) convertView.findViewById(R.id.p_name);
			viewHolder.p_price = (TextView) convertView.findViewById(R.id.p_price);
			convertView.setTag(viewHolder);
		}
		else
		{
			viewHolder = (H) convertView.getTag();
		}
		if (!getItem(position).getProd_img_link().equals(""))
		{
			Picasso.with(context).load(getItem(position).getProd_img_link()).noFade().resize(170, 170).centerCrop()
					.into(viewHolder.image);
		}
		viewHolder.onclick = new OnImageClickListener(getItem(position).getProduct_id());
		viewHolder.l_layout.setOnClickListener(viewHolder.onclick);
		viewHolder.image.setOnClickListener(viewHolder.onclick);
		Product_Concise pC = getItem(position);
		viewHolder.p_price.setText(rupeeSign + " " + pC.getPrice());
		viewHolder.p_name.setText(
				(new StringSizeLimit()).getLimitedString(pC.getTitle(), AppConstant.PRODUCT_SINGLE_TITLE_CHAR_LIMIT));
		/**/GridViewAdapterProductData.imageOnImageClickListenerList.add(viewHolder.onclick);
		/**/GridViewAdapterProductData.imagelist.add(viewHolder.image);
		// view.setLayoutParams(new GridView.LayoutParams(imageWidth,
		// (int)((imageWidth/2)*3)));
		return convertView;
	}

	private void clearImageList()
	{
		try
		{
			GridViewAdapterProductData.imagelist.clear();
		}
		catch (Exception e)
		{}
		try
		{
			GridViewAdapterProductData.imageOnImageClickListenerList.clear();
		}
		catch (Exception e)
		{}
	}

	private void clearImageOnClickListener()
	{
		for (ImageView img : GridViewAdapterProductData.imagelist)
		{
			img.setOnClickListener(null);
		}
	}

	private void revokeImageOnClickListenerOnProductGridView()
	{
		for (int i = 0; i < GridViewAdapterProductData.imagelist.size(); i++)
		{
			GridViewAdapterProductData.imagelist.get(i)
					.setOnClickListener(GridViewAdapterProductData.imageOnImageClickListenerList.get(i));
		}
	}

	class H
	{
		LinearLayout			l_layout;

		ImageView				image;

		TextView				p_name;

		TextView				p_price;

		OnImageClickListener	onclick;
	}

	public class OnImageClickListener implements OnClickListener
	{
		String _product_id;

		public OnImageClickListener(String product_id)
		{
			this._product_id = product_id;
		}

		@Override
		public void onClick(View v)
		{
			/* change */
			if (productListActivity.isDrawerOpen())
			{
				productListActivity.closeDrawer();
			}
			else
			{
				clearImageOnClickListener();
				Intent intent = new Intent(context, ProductDetailsActivity.class);
				intent.putExtra(AppConstant.DETAILS_PAGE_TYPE, AppConstant.SHOW_DETAILS_FROM_LANDING_PAGE);
				intent.putExtra(AppConstant.P_ID, _product_id);
				// activity.finish();
				activity.startActivity(intent);
				//ANIM.onStartActivity(activity);
			}
			/* /change */

		}
	}
}
