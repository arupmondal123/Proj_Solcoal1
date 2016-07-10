
package com.at.solcoal.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.applozic.mobicomkit.api.account.user.MobiComUserPreference;
import com.applozic.mobicomkit.contact.AppContactService;
import com.applozic.mobicomkit.feed.TopicDetail;
import com.applozic.mobicomkit.uiwidgets.async.ApplzoicConversationCreateTask;
import com.applozic.mobicomkit.uiwidgets.conversation.ConversationUIService;
import com.applozic.mobicomkit.uiwidgets.conversation.activity.ConversationActivity;
import com.applozic.mobicommons.people.channel.Conversation;
import com.applozic.mobicommons.people.contact.Contact;
import com.at.solcoal.R;
import com.at.solcoal.animation.ANIM;
import com.at.solcoal.constants.AppConstant;
import com.at.solcoal.data.ImageUri;
import com.at.solcoal.data.ProductEdit;
import com.at.solcoal.data.ProductLocation;
import com.at.solcoal.model.Product;
import com.at.solcoal.model.UserInfo;
import com.at.solcoal.utility.NI;
import com.at.solcoal.utility.SessionManager;
import com.at.solcoal.utility.SharedPreferenceUtility;
import com.at.solcoal.utility.Toast;
import com.at.solcoal.utility.Typefaces;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.google.android.gms.maps.model.LatLng;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.image.SmartImageView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityOptions;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListPopupWindow;
import android.widget.TextView;


public class ProductDetailsActivity extends Activity
{

	private Context						context				= null;

	private Activity					activity			= null;

	private Typeface					tf					= null;

	private ImageView					menu				= null;

	// private List<String> imageUris = null;
	private LinearLayout				imageview_ll		= null;

	private LinearLayout				menu_ll				= null;

	private SmartImageView				image1				= null;

	private ProgressDialog				pd					= null;

	private Bitmap						topBitmap			= null;

	private TextView					product_name		= null;

	private TextView					product_price		= null;

	private TextView					product_details		= null;

	private TextView					product_category	= null;

	private TextView					likes_textview		= null;

	private TextView					comments_textview	= null;

	private TextView					product_owner_name	= null;

	private String[]					menuItem			= new String[2];

	private ListPopupWindow				listPopupWindow;

	int									h					= 200;

	private ProgressDialog				dialog				= null;

	private String						DETAILS_PAGE_TYPE	= "";

	private String						productId			= "";

	private String						productOwnerId		= "";

	private int							menuWidth;

	private UserInfo					userInfo			= null;

	private String						seller_id;

	private String						user_id;

	/* change */ private LinearLayout	chat_btn_ll			= null;

	/* change */ private LinearLayout	chatButton			= null;
	public static final String TAKE_ORDER = "startChat";

	private String ownerEmail =null;

	private String ownerName =null;

	SharedPreferences		sharedpreferences;

	private String userIdStr;

	private String linkChat = "";

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		// setContentView(R.layout.act_p_details_guest_view);
		setContentView(R.layout.a___act_p_details_guest_view);

		context = ProductDetailsActivity.this;
		activity = ProductDetailsActivity.this;
		chat_btn_ll = (LinearLayout) findViewById(R.id.chat_btn_ll);
		chat_btn_ll.setVisibility(View.GONE);
		menuWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, AppConstant.MENU_WIDTH,
				getResources().getDisplayMetrics());

		DETAILS_PAGE_TYPE = getIntent().getExtras().getString(AppConstant.DETAILS_PAGE_TYPE);
		productId = getIntent().getExtras().getString(AppConstant.P_ID);
		userInfo = SharedPreferenceUtility.getUserInfo(context);




		/*
		 * 
		 * 
		 * 
		 * */
		chatButton = (LinearLayout) findViewById(R.id.chat_btn);

		chatButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v)
			{
				if (SessionManager.isUserLoggedIn(ProductDetailsActivity.this))
				{
					chatButtonClicked();

				}
				else
				{
					Intent intent = new Intent(ProductDetailsActivity.this, SigninActivity.class);
					// TODO
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(intent);
				}
			}
		});

		/*
		 * 
		 * 
		 * 
		 * */

		if (DETAILS_PAGE_TYPE.equals(AppConstant.WITH_Previous_Data))
		{
			showProductDetailsWithPreviousData();
		}
		else if (DETAILS_PAGE_TYPE.equals(AppConstant.SHOW_DETAILS_FROM_LANDING_PAGE))
		{
			fetchPDetails();
		}

		try
		{
			((TextView) findViewById(R.id.chat_btn_text))
					.setTypeface(Typeface.createFromAsset(getAssets(), "Roboto-Thin.ttf"));
		}
		catch (Exception e)
		{

		}


		/*buildSupportContactData();
		sharedpreferences = getSharedPreferences(AppConstant.LOGIN_PREFERENCE, Context.MODE_PRIVATE);

		if(sharedpreferences!= null) {
			userIdStr = sharedpreferences.getString(AppConstant.LOGIN_PREFERENCE_ID,null);
			if(userIdStr == null) {
				Intent intent = new Intent(this, SigninActivity.class);
				//startActivity(intent);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
				startActivity(intent);
				finish();
				return;
			}
		}*/



	}

	private void showProductDetailsWithPreviousData()
	{
		// setUI();
		fetchPDetails();
	}

	private void fetchPDetails()
	{
		String action = "product_get_by_id";

		dialog = new ProgressDialog(context, R.style.ProgressDialogTheme);
		dialog.setProgressStyle(android.R.style.Widget_ProgressBar_Large);
		dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		// dialog.setMessage(getResources().getString(R.string.please_wait___));
		dialog.setIndeterminate(true);
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();
		com.at.solcoal.web.AsyncWebClient asyncWebClient = new com.at.solcoal.web.AsyncWebClient();
		asyncWebClient.SetUrl(AppConstant.URL);

		RequestParams reqParam = new RequestParams();
		reqParam.add("action", action);
		reqParam.add("product_id", productId);

		asyncWebClient.post(reqParam, new JsonHttpResponseHandler() {

			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(int statusCode, org.apache.http.Header[] headers, org.json.JSONObject jsr)
			{
				try
				{
					Product product = null;

					if (jsr.getString("response_code").equals("1"))
					{
						JSONArray pArray = jsr.getJSONArray("data");

						int l = pArray.length();

						if (l > 0)
						{
							JSONObject obj = pArray.getJSONObject(0);
							product = new Product();

							product.setProduct_id(obj.getString("product_id"));
							product.setTitle(obj.getString("title"));
							product.setDescription(obj.getString("description"));
							product.setPrice(obj.getString("price"));
							product.setCategory(obj.getString("category"));
							product.setIs_new(obj.getString("is_new"));
							product.setLatitude(obj.getString("latitude"));
							product.setLongitude(obj.getString("longitude"));
							product.setProduct_owner_id(obj.getString("user_id"));

							/* getting the seller id here to be used by sinch */
							seller_id = obj.getString("user_id");

							ownerName = obj.getString("user_email_id");
							ownerEmail = obj.getString("user_email_id");
							if (ownerName != null && ownerName.indexOf("@") > -1)
							{
								ownerName = ownerName.substring(0, ownerName.indexOf("@"));
							}
							else
							{
								ownerName = "Owner";
							}
							product.setProduct_owner_name(ownerName);

							JSONArray imageJA = obj.getJSONArray("images");
							ArrayList<String> images = new ArrayList<String>();

							String link = "";

							for (int j = 0; j < imageJA.length(); j++)
							{
								try
								{
									link = imageJA.getJSONObject(j).getString("prod_img_link").trim();

									linkChat = link;
									//Toast.showSmallToast(context, linkChat);
								}
								catch (Exception e)
								{
									link = "";
								}

								images.add(link);
							}

							product.setImages(images);
							product.setDistance(obj.getString("distance"));

							ProductEdit.product = product;

							setUI();

							double lat = Double.parseDouble(product.getLatitude());
							double longi = Double.parseDouble(product.getLongitude());

							/**/
							ProductLocation.setLatLng(new LatLng(lat, longi));

						}
						else
						{
							Toast.showSmallToast(context, getResources().getString(R.string.no_data_found));
							startLandingPage();
						}

					}
					else
					{
						Toast.showSmallToast(context, "Error!!!");
						dialog.dismiss();
					}

				}
				catch (JSONException e)
				{
					Log.e("errr", e.getMessage());
				}
				finally
				{
					dialog.dismiss();
				}
			}

			@Override
			public void onFailure(int statusCode, org.apache.http.Header[] headers, String responseString,
					Throwable throwable)
			{
				super.onFailure(statusCode, headers, responseString, throwable);
				dialog.dismiss();
				Toast.showNetworkErrorMsgToast(context);
			}

			;
		});

	}

	private void chatButtonClicked()
	{
		/* fetch user id */
		SharedPreferences sharedpreferences = getSharedPreferences(AppConstant.LOGIN_PREFERENCE, Context.MODE_PRIVATE);
		if (sharedpreferences != null && (sharedpreferences.getString(AppConstant.LOGIN_PREFERENCE_ID, null) != null))
		{
			user_id = sharedpreferences.getString(AppConstant.LOGIN_PREFERENCE_ID, null);
			if (user_id == null)
			{
				Intent intent = new Intent(ProductDetailsActivity.this, SigninActivity.class);
				intent.putExtra(AppConstant.AFTER_LOGIN_ACTION, "after_login_action");
				startActivity(intent);
			}
			else
			{
				startChat();
				//openLoginActivity(user_id);
			}
		}
	}

	private void openLoginActivity(String user_id)
	{
		if (!seller_id.equals(0))
		{
			Intent loginActivity = new Intent(this, LoginActivity.class);
			loginActivity.putExtra("userId", user_id);
			loginActivity.putExtra("sellerId", seller_id);
			startActivity(loginActivity);
		}
	}

	public void startLandingPage()
	{

		Intent intent = new Intent(ProductDetailsActivity.this, ProductListActivity.class);
		ProductDetailsActivity.this.finish();
		startActivity(intent);
		ANIM.onStartActivity(activity);
	}

	private void setUI()
	{
		tf = Typefaces.get(context, AppConstant.app_font);

		menuItem[0] = getResources().getString(R.string.edit);
		menuItem[1] = getResources().getString(R.string.delete);

		menu = (ImageView) findViewById(R.id.menu);

		product_name = (TextView) findViewById(R.id.product_name);
		product_price = (TextView) findViewById(R.id.product_price);
		product_details = (TextView) findViewById(R.id.product_details);
		product_category = (TextView) findViewById(R.id.product_category);
		likes_textview = (TextView) findViewById(R.id.likes_textview);
		comments_textview = (TextView) findViewById(R.id.comments_textview);
		menu_ll = (LinearLayout) findViewById(R.id.menu_ll);
		product_owner_name = (TextView) findViewById(R.id.h_txt_1);

		setTypefaces();

		product_name.setText(ProductEdit.product.getTitle());
		product_details.setText(ProductEdit.product.getDescription());
		// product_details.setText(Html.fromHtml(pDetails[1]));
		product_price.setText(getResources().getString(R.string.rupee_sign) + " " + ProductEdit.product.getPrice());
		product_category.setText(ProductEdit.product.getCategory());
		product_owner_name.setText("@" + ProductEdit.product.getProduct_owner_name());

		/* change */
		try
		{
			((TextView) findViewById(R.id.name_initial))
					.setText(NI.getInitial(ProductEdit.product.getProduct_owner_name().toUpperCase()));
		}
		catch (Exception e)
		{

		}
		/*
		 * 
		 * 
		 * 
		 * 
		 * */
		productOwnerId = ProductEdit.product.getProduct_owner_id();

		View.OnClickListener onClick = new View.OnClickListener() {
			@Override
			public void onClick(View v)
			{

				loadOwnerDetails();
			}
		};
		((LinearLayout) findViewById(R.id.ll_product_owner_header)).setOnClickListener(onClick);

		// addImage(imageUris);

		imageSlider();
		backBtn();
		if (productOwnerId != null && userInfo != null)
		{
			if (productOwnerId.equals(userInfo.getId()))
			{
				((LinearLayout) findViewById(R.id.ll_product_edit)).setVisibility(View.VISIBLE);
				setMenu();
				
			}/* change */
			else
			{
				/*
				 * 
				 * 
				 * 
				 * */
				chat_btn_ll.setVisibility(View.VISIBLE);
			}/* /change */
		}
		else
		{
			/*
			 * 
			 * 
			 * 
			 * */
			chat_btn_ll.setVisibility(View.VISIBLE);
		}
	}

	private void loadOwnerDetails()
	{
		Intent intentLocal = new Intent(ProductDetailsActivity.this, UserProductList.class);
		intentLocal.putExtra("owner_id", productOwnerId);
		intentLocal.putExtra("userInfo", userInfo);
		startActivity(intentLocal);
	}

	private void setMenu()
	{
		listPopupWindow = new ListPopupWindow(ProductDetailsActivity.this);
		listPopupWindow.setAdapter(new ArrayAdapter(ProductDetailsActivity.this, R.layout.list_item, menuItem));

		listPopupWindow.setAnchorView(menu_ll);
		listPopupWindow.setWidth(menuWidth);
		listPopupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);

		listPopupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.style_menu));
		listPopupWindow.setModal(true);
		listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
			{
				if (menuItem[arg2].equals(getResources().getString(R.string.edit)))
				{
					editProduct();
				}
				else if (menuItem[arg2].equals(getResources().getString(R.string.delete)))
				{
					(new DELETE_PRODUCT_DIALOG(context)).show();
				}

				listPopupWindow.dismiss();
			}

		});

		menu_ll.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v)
			{
				listPopupWindow.show();
			}
		});
	}

	/* EDIT Product */
	private void editProduct()
	{
		Intent intent = new Intent(ProductDetailsActivity.this, ProductAddProductActivity.class);
		intent.putExtra(AppConstant.ADD_EDIT, AppConstant.EDIT);
		intent.putStringArrayListExtra(AppConstant.PRODUCT_IMAGES, ProductEdit.product.getImages());
		startActivity(intent);
		ProductDetailsActivity.this.finish();
		ANIM.onStartActivity(activity);
	}

	/* Delete Product */
	private void deleteProduct()
	{
		deleteProductWSC();
	}

	private void deleteProductWSC()
	{
		// Toast.showSmallToast(context, "productId : " + productId);

		String action = "product_delete";
		dialog = new ProgressDialog(context, R.style.ProgressDialogTheme);
		dialog.setProgressStyle(android.R.style.Widget_ProgressBar_Large);
		// dialog.setMessage(getResources().getString(R.string.please_wait___));
		dialog.setIndeterminate(true);
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();
		com.at.solcoal.web.AsyncWebClient asyncWebClient = new com.at.solcoal.web.AsyncWebClient();
		asyncWebClient.SetUrl(AppConstant.URL);

		RequestParams reqParam = new RequestParams();
		reqParam.add("action", action);
		reqParam.add("product_id", productId);

		asyncWebClient.get(reqParam, new JsonHttpResponseHandler() {
			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(int statusCode, org.apache.http.Header[] headers, org.json.JSONObject jsr)
			{
				String response_code = null;
				String response_message = null;

				try
				{
					response_code = jsr.getString("response_code");
					response_message = jsr.getString("response_message");
					Toast.showSmallToast(context, response_message);
					if (response_code.equals("1"))
					{
						onBackPressed();
					}

				}
				catch (JSONException e)
				{
					Log.e("errr", e.getMessage());
				}
				finally
				{
					dialog.dismiss();
				}
			};

			@Override
			public void onFailure(int statusCode, org.apache.http.Header[] headers, String responseString,
					Throwable throwable)
			{
				super.onFailure(statusCode, headers, responseString, throwable);
				dialog.dismiss();
				Toast.showNetworkErrorMsgToast(context);
			};
		});

	}

	private void setTypefaces()
	{
		// ((TextView) findViewById(R.id.h_txt_1)).setTypeface(tf);
		// ((TextView) findViewById(R.id.h_txt_2)).setTypeface(tf);
		//
		// product_name.setTypeface(tf);
		// product_price.setTypeface(tf);
		// product_details.setTypeface(tf);
		// product_category.setTypeface(tf);
		// likes_textview.setTypeface(tf);
		// comments_textview.setTypeface(tf);
	}

	private void backBtn()
	{
		((LinearLayout) findViewById(R.id.handle)).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v)
			{
				onBackPressed();
			}
		});
	}

	@Override
	public void onBackPressed()
	{
		super.onBackPressed();
		clearImageSelection();
		// imageview_ll.removeAllViews();
		// Intent intent = new Intent(ProductDetailsActivity.this,
		// ProductListPage.class);
		// ProductDetailsActivity.this.finish();
		// startActivity(intent);
		// ANIM.onBack(ProductDetailsActivity.this);
	}

	public void clearImageSelection()
	{
		/**/Log.e("ProductAddActivity _ clearImageSelection()", " clearImageSelection()");
		try
		{
			ProductEdit.product.clear();
		}
		catch (Exception e)
		{

		}

		try
		{
			ImageUri.images.clear();
		}
		catch (Exception e)
		{

		}
	}

	private int pos;

	private void imageSlider()
	{
		if (ProductEdit.product.getImages().size() > 0)
		{
			SliderLayout slider = (SliderLayout) findViewById(R.id.slider);

			if (ProductEdit.product.getImages().size() <= 1)
			{
				slider.stopAutoCycle();
			}

			for (String path : ProductEdit.product.getImages())
			{
				pos = ProductEdit.product.getImages().indexOf(path);

				TextSliderView textSliderView = new TextSliderView(this);

				// initialize a SliderLayout
				textSliderView.image(path).setScaleType(BaseSliderView.ScaleType.CenterCrop)
						.setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {

							@Override
							public void onSliderClick(BaseSliderView slider)
							{
								// (new FULL_IMAGE(context, pos)).show();
								Intent intent = new Intent(ProductDetailsActivity.this, FullPictureActivity.class);
								//ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(mActivity,image,"picture");
								intent.putExtra("pos", "");
								startActivity(intent);
								ANIM.onStartActivity(activity);
							}
						});

				// add your extra information
				// textSliderView.getBundle().putString("extra", "");

				slider.addSlider(textSliderView);
			}
			slider.setPresetTransformer(SliderLayout.Transformer.Default);
			slider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
			// PagerIndicator pagerIndicator = (PagerIndicator)
			// findViewById(R.id.custom_indicator);
			// pagerIndicator.setDefaultIndicatorColor(Color.WHITE, Color.GRAY);
			// slider.setCustomIndicator(pagerIndicator);
			slider.setCustomAnimation(new DescriptionAnimation());
			slider.setDuration(5000);

		}
	}

	class FULL_IMAGE extends Dialog
	{
		private Context	ctx	= null;

		private int		i;

		public FULL_IMAGE(Context context, int position)
		{
			super(context);

			this.ctx = context;
			this.i = position;
		}

		@Override
		protected void onCreate(Bundle savedInstanceState)
		{
			super.onCreate(savedInstanceState);
			requestWindowFeature(Window.FEATURE_NO_TITLE);
			setContentView(R.layout.act_full_image);

			SliderLayout slider = (SliderLayout) findViewById(R.id.slider);

			for (String path : ProductEdit.product.getImages())
			{
				TextSliderView textSliderView = new TextSliderView(ctx);

				// initialize a SliderLayout
				textSliderView.image(new File(path)).setScaleType(BaseSliderView.ScaleType.CenterInside);

				// add your extra information
				textSliderView.getBundle().putString("extra", "");

				slider.addSlider(textSliderView);
			}

			slider.setPresetTransformer(SliderLayout.Transformer.Default);
			// slider.setPresetIndicator(SliderLayout.PresetIndicators.Left_Top);
			// PagerIndicator pagerIndicator = (PagerIndicator)
			// findViewById(R.id.custom_indicator);
			// pagerIndicator.setDefaultIndicatorColor(Color.WHITE, Color.GRAY);
			// slider.setCustomIndicator(pagerIndicator);
			slider.setCustomAnimation(new DescriptionAnimation());
			slider.setId(i);
			slider.setDuration(5000);
		}
	}

	class DELETE_PRODUCT_DIALOG extends Dialog
	{
		public DELETE_PRODUCT_DIALOG(Context context)
		{
			super(context);
		}

		@Override
		protected void onCreate(Bundle savedInstanceState)
		{
			super.onCreate(savedInstanceState);
			requestWindowFeature(Window.FEATURE_NO_TITLE);
			setContentView(R.layout.dialog_delete_product);
			getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);

			((LinearLayout) findViewById(R.id.ok)).setOnTouchListener(new View.OnTouchListener() {

				@Override
				public boolean onTouch(View v, MotionEvent event)
				{
					if (event.getAction() == MotionEvent.ACTION_UP)
					{
						deleteProduct();

						v.setPressed(false);
					}
					else if (event.getAction() == MotionEvent.ACTION_DOWN)
					{
						v.setPressed(true);
					}
					else if (event.getAction() == MotionEvent.ACTION_CANCEL)
					{
						v.setPressed(false);
					}

					return true;
				}
			});
			((LinearLayout) findViewById(R.id.cancel)).setOnTouchListener(new View.OnTouchListener() {

				@Override
				public boolean onTouch(View v, MotionEvent event)
				{
					if (event.getAction() == MotionEvent.ACTION_UP)
					{
						dismiss();

						v.setPressed(false);
					}
					else if (event.getAction() == MotionEvent.ACTION_DOWN)
					{
						v.setPressed(true);
					}
					else if (event.getAction() == MotionEvent.ACTION_CANCEL)
					{
						v.setPressed(false);
					}

					return true;
				}
			});
		}

	}

	public void startChat() {
		Conversation conversation = buildConversation();
		ApplzoicConversationCreateTask applzoicConversationCreateTask;

		ApplzoicConversationCreateTask.ConversationCreateListener conversationCreateListener =  new ApplzoicConversationCreateTask.ConversationCreateListener() {
			@Override
			public void onSuccess(Integer conversationId, Context context) {
				//Log.i(TAG,"ConversationID is:"+conversationId);
				Intent startChatIntent = new Intent(context, ConversationActivity.class);
				startChatIntent.putExtra(TAKE_ORDER, true);
				startChatIntent.putExtra(ConversationUIService.CONTEXT_BASED_CHAT,true);
				startChatIntent.putExtra(ConversationUIService.USER_ID,ownerEmail);
				startChatIntent.putExtra(ConversationUIService.DISPLAY_NAME,ownerName);
				startChatIntent.putExtra(ConversationUIService.PRODUCT_IMAGE_URL,linkChat);


				startChatIntent.putExtra(ConversationUIService.DEFAULT_TEXT, "Hello I am interested in your product, Can we chat?");
				startChatIntent.putExtra(ConversationUIService.CONVERSATION_ID,conversationId);
				startActivity(startChatIntent);

			}

			@Override
			public void onFailure(Exception e, Context context) {

			}
		};
		applzoicConversationCreateTask =  new ApplzoicConversationCreateTask(ProductDetailsActivity.this,conversationCreateListener,conversation);
		applzoicConversationCreateTask.execute((Void)null);

	}

	private Conversation buildConversation() {

		Conversation conversation = new Conversation();

		conversation.setUserId(ownerEmail);
		conversation.setTopicId("Topic#Id#Test");

		TopicDetail topic = new TopicDetail();
		//Toast.showSmallToast(context, linkChat);
		topic.setTitle(ProductEdit.product.getTitle());
		topic.setSubtitle("Price :" + ProductEdit.product.getPrice());
		topic.setLink(linkChat);
		topic.setKey1("Price");
		topic.setValue1(ProductEdit.product.getPrice());
		topic.setKey2("");
		topic.setValue2("");

		conversation.setSenderSmsFormat(MobiComUserPreference.getInstance(this).getUserId(), "SENDER SMS  FORMAT");
		conversation.setReceiverSmsFormat(ownerEmail, "RECEIVER SMS FORMAT");
		conversation.setTopicDetail(topic.getJson());
		return conversation;
	}


	private void buildSupportContactData() {
		Context context = getApplicationContext();
		AppContactService appContactService = new AppContactService(context);
		// avoid each time update ....
			Contact contact = new Contact();
			contact.setUserId("romi.mondal@gmail.com");
			contact.setFullName("R Mondal");
			contact.setContactNumber("9836219901");
			contact.setImageURL("R.drawable.ic_launcher");
			contact.setEmailId("romi.mondal@gmail.com");
			appContactService.add(contact);

	}

}
