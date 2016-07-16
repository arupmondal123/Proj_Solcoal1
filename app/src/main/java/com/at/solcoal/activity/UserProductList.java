
package com.at.solcoal.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.media.Image;
import android.support.design.widget.FloatingActionButton;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.at.solcoal.R;
import com.at.solcoal.adapter.GridViewAdapterProductForUser;
import com.at.solcoal.constants.AppConstant;
import com.at.solcoal.data.ProductConciseList;
import com.at.solcoal.model.Product_Concise;
import com.at.solcoal.model.UserInfo;
import com.at.solcoal.utility.NI;
import com.at.solcoal.utility.SharedPreferenceUtility;
import com.at.solcoal.utility.Toast;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class UserProductList extends Activity
{

	private Context							context					= null;

	private Activity						activity				= null;

	private ProgressDialog					progressDialog			= null;

	private GridView						gridView				= null;

	private int								imageWidth;

	private GridViewAdapterProductForUser	productGridViewAdapter	= null;

	private UserInfo						userInfo				= null;

	private UserInfo						ownerInfo				= null;

	private String							ownerId					= null;

	private RelativeLayout					lower_relative_layout	= null;

	private FloatingActionButton fab;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.act_user_product_list);
		setContentView(R.layout.a___act_user_product_list);
		context = UserProductList.this;
		activity = UserProductList.this;
		/* change */
		lower_relative_layout = (RelativeLayout) findViewById(R.id.lower_relative_layout);
		lower_relative_layout.setVisibility(View.GONE);
		/* change */

//		userInfo = (UserInfo) getIntent().getSerializableExtra("userInfo");
		userInfo = SharedPreferenceUtility.getUserInfo(UserProductList.this);
		
		ownerId = getIntent().getStringExtra("owner_id");
		if (ownerId != null)
		{
			fetchOwnerAndProductListDetails();
		}
		else
		{
			LinearLayout editProfile = ((LinearLayout) findViewById(R.id.view_edit_profile_upl));
			((TextView) findViewById(R.id.profile_text_)).setText("Edit Profile");
			setLowerRelativeLayoutVisible();
			editProfile.setVisibility(View.VISIBLE);
			((TextView) findViewById(R.id.username_upl)).setText(userInfo.getName());
			((TextView) findViewById(R.id.username_upl)).setVisibility(View.VISIBLE);

			try
			{
				((TextView) findViewById(R.id.name_initial)).setText(NI.getInitial(userInfo.getName().toUpperCase()));
			}
			catch (Exception e)
			{

			}

			fetchProductConciseList(userInfo.getId());
			editProfile.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v)
				{
					landOnUserDetails(true);
				}
			});

		}

	}

	private void landOnUserDetails(boolean isLoggedInUser)
	{

		String edit_view_profile = ((TextView) findViewById(R.id.profile_text_)).getText().toString().trim();

		Intent intentLocal = new Intent(UserProductList.this, UserLoginInfo.class);
		if (isLoggedInUser || (userInfo != null && ownerInfo != null && userInfo.getId().equals(ownerInfo.getId())))
		{
			intentLocal.putExtra("userInfo", userInfo);

			intentLocal.putExtra(AppConstant.USER_INFO_ACTION, AppConstant.USER_INFO_USER);
		}
		else
		{
			intentLocal.putExtra("userInfo", ownerInfo);
			intentLocal.putExtra(AppConstant.USER_INFO_ACTION, AppConstant.USER_INFO_OTHER_USER);
		}
		
//		intentLocal.putExtra("userInfo___", (UserInfo) getIntent().getSerializableExtra("userInfo"));
		intentLocal.putExtra("owner_id___", getIntent().getStringExtra("owner_id"));
		intentLocal.putExtra("edit_view_profile", edit_view_profile);
		startActivity(intentLocal);
	}

	private void fetchOwnerAndProductListDetails(UserInfo ownerInfo)
	{

		((TextView) findViewById(R.id.username_upl)).setText(ownerInfo.getName());
		((TextView) findViewById(R.id.username_upl)).setVisibility(View.VISIBLE);

		/*
		 * 
		 * 
		 * 
		 * */
		try
		{
			((TextView) findViewById(R.id.name_initial)).setText(NI.getInitial(ownerInfo.getName().toUpperCase()));
		}
		catch (Exception e)
		{}

		/*
		 * 
		 * 
		 * 
		 * */

		LinearLayout viewProfile = ((LinearLayout) findViewById(R.id.view_edit_profile_upl));
		if (userInfo != null && ownerInfo != null && userInfo.getId().equals(ownerInfo.getId()))
		{
			((TextView) findViewById(R.id.profile_text_)).setText("Edit Profile");
			setLowerRelativeLayoutVisible();

		}
		else
		{
			((TextView) findViewById(R.id.profile_text_)).setText("View Profile");
		}

		viewProfile.setVisibility(View.VISIBLE);
		viewProfile.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v)
			{
				landOnUserDetails(false);
			}
		});
		fetchProductConciseList(ownerId);

	}

	private void setLowerRelativeLayoutVisible()
	{
		lower_relative_layout.setVisibility(View.VISIBLE);

		((LinearLayout) findViewById(R.id.view_all_ll)).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v)
			{

			}
		});

		((FloatingActionButton) findViewById(R.id.add_product_floating_button)).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				startProductAddPictureActivity();
			}
		});
	}
	

	protected void startProductAddPictureActivity()
	{
		Intent intent = new Intent(UserProductList.this, ProductAddPictureActivity.class);
		
		
		// TODO
		
		intent.putExtra("from_", "ProductListPage");
		startActivity(intent);
	}

	private void fetchOwnerAndProductListDetails()
	{
		com.at.solcoal.web.AsyncWebClient asyncWebClient = new com.at.solcoal.web.AsyncWebClient();
		asyncWebClient.SetUrl(AppConstant.URL);
		RequestParams reqParam = new RequestParams();
		reqParam.add("action", "user_get_by_id");
		reqParam.add("user_id", ownerId);
		asyncWebClient.post(reqParam, new JsonHttpResponseHandler() {
			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(int statusCode, org.apache.http.Header[] headers, JSONObject jsr)
			{
				try
				{
					String responseCode = jsr.getString("response_code");
					if (responseCode.equals("1"))
					{
						JSONObject obj = jsr.getJSONObject("data");
						ownerInfo = new UserInfo();
						ownerInfo.setId(obj.getString("user_id"));
						ownerInfo.setName(obj.getString("user_name"));
						ownerInfo.setEmail(obj.getString("user_email_id"));
						ownerInfo.setGender(obj.getString("user_sex"));
						// ownerInfo.setImageUrl(obj.getString("user_image_link"));
						fetchOwnerAndProductListDetails(ownerInfo);
					}
					else
					{
						//Toast.showSmallToast(context, "Error!!!");
					}
				}
				catch (JSONException e)
				{
					Log.e("errr", e.getMessage());
				}
			}

			;

			@Override
			public void onFailure(int statusCode, org.apache.http.Header[] headers, String responseString,
					Throwable throwable)
			{
				super.onFailure(statusCode, headers, responseString, throwable);
				Toast.showNetworkErrorMsgToast(context);
			}

			;
		});

	}

	private void fetchProductConciseList(String userIdStr)
	{
		ProductConciseList.clearForUser();
		gridView = (GridView) findViewById(R.id.grid_view_upl);
		initializeGridLayout();
		progressDialog = new ProgressDialog(context, R.style.ProgressDialogTheme);
		progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Large);
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressDialog.setIndeterminate(true);
		progressDialog.setCanceledOnTouchOutside(false);
		progressDialog.show();
		com.at.solcoal.web.AsyncWebClient asyncWebClient = new com.at.solcoal.web.AsyncWebClient();
		asyncWebClient.SetUrl(AppConstant.URL);
		RequestParams reqParam = new RequestParams();
		reqParam.add("action", "product_get_by_user_id");
		if (ownerId != null)
		{
			reqParam.add("user_id", ownerId);
		}
		else
		{
			reqParam.add("user_id", userIdStr);
		}

		asyncWebClient.post(reqParam, new JsonHttpResponseHandler() {
			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(int statusCode, org.apache.http.Header[] headers, JSONObject jsr)
			{
				try
				{
					String responseCode = jsr.getString("response_code");
					if (responseCode.equals("1"))
					{
						JSONArray pArray = jsr.getJSONArray("data");
						int l = pArray.length();
						if (l > 0)
						{
							JSONObject obj = null;
							String productId = "";
							String title = "";
							String price = "";
							String link = "";
							String distance = "";
							String userId = "";
							for (int i = 0; i < l; i++)
							{
								obj = pArray.getJSONObject(i);
								try
								{
									productId = obj.getString("product_id");
								}
								catch (Exception e)
								{
									productId = "";
								}
								try
								{
									title = obj.getString("title");
								}
								catch (Exception e)
								{
									title = "";
								}
								try
								{
									price = obj.getString("price");
								}
								catch (Exception e)
								{
									price = "";
								}
								try
								{
									link = obj.getJSONArray("images").getJSONObject(0).getString("prod_img_link");
								}
								catch (Exception e)
								{
									link = "";
								}
								try
								{
									distance = obj.getString("distance");
								}
								catch (Exception e)
								{
									distance = "";
								}
								try
								{
									userId = obj.getString("user_id");
								}
								catch (Exception e)
								{
									userId = "";
								}
								ProductConciseList.productConciseListForUser
										.add(new Product_Concise(productId, title, price, link, distance, userId));
							}
							if (ProductConciseList.productConciseListForUser.size() > 0)
							{
								setAdaptertoGridView();
							}
							else
							{
								Toast.showSmallToast(context, "No Data found.");
							}
						}
						else
						{
							Toast.showSmallToast(context, "No Data found.");
						}
					}
					else
					{
						Toast.showSmallToast(context, "Error!!!");
						progressDialog.dismiss();
					}
				}
				catch (JSONException e)
				{
					Log.e("errr", e.getMessage());
				}
				finally
				{
					progressDialog.dismiss();
				}
			}

			;

			@Override
			public void onFailure(int statusCode, org.apache.http.Header[] headers, String responseString,
					Throwable throwable)
			{
				super.onFailure(statusCode, headers, responseString, throwable);
				progressDialog.dismiss();
				Toast.showNetworkErrorMsgToast(context);
			}

			;
		});
	}

	private void setAdaptertoGridView()
	{
		productGridViewAdapter = new GridViewAdapterProductForUser(activity, imageWidth);
		gridView.setAdapter(productGridViewAdapter);
	}

	private void initializeGridLayout()
	{
		int NUM_OF_COLUMNS = 2; // Number of columns of Grid View
		int GRID_PADDING1 = 12; // Gridview image padding in dp
		int GRID_PADDING2 = 12; // Gridview image padding in dp
		float padding1 = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, GRID_PADDING1,
				getResources().getDisplayMetrics());
		float padding2 = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, GRID_PADDING2,
				getResources().getDisplayMetrics());
		/**/
		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		int width = size.x;
		int height = size.y;
		/**/
		// imageWidth = (int) ((Image.getScreenWidth(context) - ((NUM_OF_COLUMNS
		// + 1) * padding1)) / NUM_OF_COLUMNS);
		imageWidth = ((int) ((width - ((NUM_OF_COLUMNS + 1) * padding2)) / NUM_OF_COLUMNS));
		gridView.setNumColumns(NUM_OF_COLUMNS);
		gridView.setColumnWidth(imageWidth);
		gridView.setStretchMode(GridView.NO_STRETCH);
		gridView.setPadding((int) padding1, (int) padding1, (int) padding1, (int) padding1);
		gridView.setHorizontalSpacing((int) padding2);
		gridView.setVerticalSpacing((int) padding2);
	}

}
