package com.at.drgrep.activity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.at.drgrep.R;
import com.at.drgrep.animation.ANIM;
import com.at.drgrep.constants.AppConstant;
import com.at.drgrep.data.ImageUri;
import com.at.drgrep.data.ProdCategory;
import com.at.drgrep.data.ProductEdit;
import com.at.drgrep.data.ProductLocation;
import com.at.drgrep.data.UserLocation;
import com.at.drgrep.dialog.MessageDialog;
import com.at.drgrep.model.Product;
import com.at.drgrep.utility.BITMAP;
import com.at.drgrep.utility.KEYBOARD;
import com.at.drgrep.utility.Network;
import com.at.drgrep.utility.ResizeImage;
import com.at.drgrep.utility.Toast;
import com.at.drgrep.utility.Typefaces;
import com.at.drgrep.web.AsyncWebClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.image.SmartImageView;
import com.squareup.picasso.Picasso;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListPopupWindow;
import android.widget.RelativeLayout;

public class ProductAddProductActivity2 extends Activity
{
	private Context				context				= null;
	private Activity			activity			= null;
	private String				TAG;
	private Typeface			tf					= null;

	private EditText			title_et			= null;
	private EditText			description_et		= null;
	private EditText			currency_et			= null;
	private EditText			price_et			= null;
	private EditText			category_et			= null;

	private RelativeLayout		nu					= null;
	private EditText			new_et				= null;
	private ImageView			new_used			= null;

	private ImageView			p_image				= null;
	private ImageView			cat_image			= null;

	private ImageView			google_map			= null;

	private LinearLayout		h_linear_layout		= null;
	private LinearLayout		saveLayout			= null;

	private static final int	hw					= 170;

	private ProgressDialog		dialog				= null;
	private String				PRODUCT_ADD_EDIT	= "ADD";

	private int					listPopUpWidth		= 302;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.act_main);

		context = ProductAddProductActivity2.this;
		activity = ProductAddProductActivity2.this;
		TAG = "ProductAddActivity";

		listPopUpWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, AppConstant.LIST_POPUP_WIDTH,
				getResources().getDisplayMetrics());

		tf = Typefaces.get(context, AppConstant.app_font);

		title_et = (EditText) findViewById(R.id.title_et);
		description_et = (EditText) findViewById(R.id.description_et);
		currency_et = ((EditText) findViewById(R.id.currency_et));
		price_et = ((EditText) findViewById(R.id.price_et));
		// et_cat = (EditText) findViewById(R.id.et_cat);
		category_et = (EditText) findViewById(R.id.category_et);

		nu = (RelativeLayout) findViewById(R.id.nu);
		new_et = (EditText) findViewById(R.id.new_et);
		google_map = (ImageView) findViewById(R.id.google_map);

		saveLayout = (LinearLayout) findViewById(R.id.save);
		saveLayout.setTag(AppConstant.ADD);

		try
		{
			PRODUCT_ADD_EDIT = getIntent().getExtras().getString(AppConstant.ADD_EDIT).trim();

		} catch (Exception e)
		{
		}

		backBtn();
		saveBtn();
		category();

		if (Network.isConnected(context))
		{
			getCategory();
		} else
		{
			Toast.showNetworkErrorMsgToast(context);
		}

		nu();
		image();
		add_pic();
		footer();

		((LinearLayout) findViewById(R.id.profile)).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v)
			{
				// startActivity(new Intent(ProductUploadActivity.this,
				// LandingActivity.class));
				// ANIM.onStartActivity(activity);
			}
		});

		// price_et.setFilters(new InputFilter[] { new
		// DecimalDigitsInputFilter(14, 2) });

		price_et.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count)
			{
				c();
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after)
			{
				c();
			}

			private String current = "";

			@Override
			public void afterTextChanged(Editable s)
			{
				{
					if (!s.toString().matches("^\\$(\\d{1,3}(\\,\\d{3})*|(\\d+))(\\.\\d{2})?$"))
					{
						String userInput = "" + s.toString().replaceAll("[^\\d]", "");
						StringBuilder cashAmountBuilder = new StringBuilder(userInput);

						while (cashAmountBuilder.length() > 3 && cashAmountBuilder.charAt(0) == '0')
						{
							cashAmountBuilder.deleteCharAt(0);
						}
						while (cashAmountBuilder.length() < 3)
						{
							cashAmountBuilder.insert(0, '0');
						}
						cashAmountBuilder.insert(cashAmountBuilder.length() - 2, '.');

						price_et.removeTextChangedListener(this);
						price_et.setText(cashAmountBuilder.toString());

						price_et.setTextKeepState(
								getResources().getString(R.string.rupee_sign) + cashAmountBuilder.toString());
						// getResources().getString(R.string.rupee_sign) +
						Selection.setSelection(price_et.getText(), cashAmountBuilder.toString().length() + 1);

						price_et.addTextChangedListener(this);
					}
				}

				c();
			}

			private void c()
			{
				if (true)
				{
					currency_et.setText("");
				}
				if (price_et.getText().toString().trim().length() > 0)
				{
					currency_et.setText(getResources().getString(R.string.inr));
				}
				if (price_et.getText().toString().trim().equals(getResources().getString(R.string.rupee__)))
				{
					currency_et.setText("");
				}
			}
		});

		((ImageView) findViewById(R.id.globe)).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v)
			{
				Toast.showImplementationPending(context);
			}
		});

		/**/ /**/

		if (PRODUCT_ADD_EDIT.equals(AppConstant.EDIT))
		{
			/**/Log.e(TAG + "_AppConstant.ADD_EDIT", "PRODUCT_ADD_EDIT.equals(AppConstant.EDIT)");
			saveLayout.setTag(AppConstant.EDIT);
			fillUIData();

		} else
		{
			/**/Log.e(TAG + "_AppConstant.ADD_EDIT", "PRODUCT_ADD_EDIT.equals(AppConstant.ADD)");
			ProductEdit.product = null;
			ProductLocation.setLatLng(UserLocation.getUserPreferredLatLng());
			loadAllLocalImages();
		}

		/**/ /**/

	}

	private void startCongratulationsActivity()
	{
		Intent intent = new Intent(ProductAddProductActivity2.this, Congratulations.class);
		ProductAddProductActivity2.this.finish();
		startActivity(intent);
		ANIM.comeDown(activity);
	}

	private void fillUIData()
	{
		/* PRODUCT_ADD_EDIT.equals(AppConstant.EDIT) */

		/**/Log.e(TAG + "_fillUIData()", ProductEdit.product.getTitle());

		title_et.setText(ProductEdit.product.getTitle());
		description_et.setText(ProductEdit.product.getDescription());
		price_et.setText(ProductEdit.product.getPrice());
		category_et.setText(ProductEdit.product.getCategory());
		setCat_Checked(true);
		if (ProductEdit.product.getIs_new().equals("0"))
		{
			new_used.setImageDrawable(getResources().getDrawable(R.drawable.check_off));
			new_used.setTag(R.drawable.check_off);
			new_et.setText("");

		} else if (ProductEdit.product.getIs_new().equals("1"))
		{
			new_used.setImageDrawable(getResources().getDrawable(R.drawable.check_on));
			new_used.setTag(R.drawable.check_on);
			new_et.setText(getResources().getString(R.string.new_));
		}

		/* Setting default Product LatLong */
		double lat = UserLocation.getUserPreferredLatLng().latitude;
		double longi = UserLocation.getUserPreferredLatLng().longitude;

		try
		{
			lat = Double.parseDouble(ProductEdit.product.getLatitude());
		} catch (Exception e)
		{
		}

		try
		{
			longi = Double.parseDouble(ProductEdit.product.getLongitude());
		} catch (Exception e)
		{
		}

		// /**/
		// ProductLocation.setLatLng(new LatLng(lat, longi));

		/* /Setting Product LatLong */

		// ArrayList<String> imageList = ProductEdit.product.getImages();

		for (String url : ProductEdit.product.getImages())
		{
			loadImageFromWEB(url.trim());
		}
	}

	public void loadImageFromWEB(String url)
	{
		addImageToHSV(url, AppConstant.WEB);
	}

	private void footer()
	{
		google_map.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v)
			{
				startGoogleMapActivity();
			}
		});
	}

	private void startGoogleMapActivity()
	{
		Intent intent = new Intent(ProductAddProductActivity2.this, GoogleMapActivity.class);
		intent.putExtra("lat", "");
		intent.putExtra("long", "");

		if (PRODUCT_ADD_EDIT.equals(AppConstant.ADD))
		{
			intent.putExtra(AppConstant.MAP_JOB_TYPE, AppConstant.PRODUCT_ADD);

		} else if (PRODUCT_ADD_EDIT.equals(AppConstant.EDIT))
		{
			intent.putExtra(AppConstant.MAP_JOB_TYPE, AppConstant.PRODUCT_EDIT);
		}

		startActivity(intent);
		ANIM.onStartActivity(activity);
	}

	private void add_pic()
	{
		((LinearLayout) findViewById(R.id.add_more_picture)).setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event)
			{
				if (event.getAction() == MotionEvent.ACTION_UP)
				{
					startActivity(new Intent(ProductAddProductActivity2.this, ProductAddPictureActivity.class));
					ANIM.onStartActivity(activity);
					v.setPressed(false);
				} else if (event.getAction() == MotionEvent.ACTION_DOWN)
				{
					v.setPressed(true);
				} else if (event.getAction() == MotionEvent.ACTION_CANCEL)
				{
					v.setPressed(false);
				}
				return true;
			}
		});
	}

	private void image()
	{
		h_linear_layout = (LinearLayout) findViewById(R.id.h_linear_layout);
	}

	private void addImageToHSV(String filePath, String type)
	{
		/**/Log.i(TAG + " _ addImageToHSV() _uri", filePath);
		/**/Log.i(TAG + " _ addImageToHSV() _ type", type);

		if (type.equals(AppConstant.WEB))
		{
			View view = LayoutInflater.from(context).inflate(R.layout.single_image_view, null);

			final FrameLayout image_frame = (FrameLayout) view.findViewById(R.id.image_frame_single);

			image_frame.setTag(filePath);

			ImageView imageView_close = (ImageView) view.findViewById(R.id.imageView_close);
			SmartImageView image = (SmartImageView) view.findViewById(R.id.image);
			{

				float rotation = 0f;
				try
				{
					Picasso.with(ProductAddProductActivity2.this).load(Uri.parse(filePath)).rotate(rotation).noFade()
							.resize(hw, hw).centerCrop().into(image);
				} catch (Exception e)
				{
					e.printStackTrace();
				}
			}
			imageView_close.setTag(image_frame);

			imageView_close.setOnTouchListener(new View.OnTouchListener() {

				@Override
				public boolean onTouch(View v, MotionEvent event)
				{
					if (event.getAction() == MotionEvent.ACTION_UP)
					{
						/* WEB REMOVE */
						(new REMOVE_IMAGE_FROM_WEB_DIALOG(context, (FrameLayout) v.getTag())).show();

						v.setPressed(false);
					} else if (event.getAction() == MotionEvent.ACTION_DOWN)
					{
						v.setPressed(true);
					} else if (event.getAction() == MotionEvent.ACTION_CANCEL)
					{
						v.setPressed(false);
					}
					return true;
				}
			});

			h_linear_layout.addView(view);

		} else if (!(type.equals(AppConstant.Gallery2) && ((new File(filePath)).length() <= 0L)))
		{
			View view = LayoutInflater.from(context).inflate(R.layout.single_image_view, null);

			final FrameLayout image_frame = (FrameLayout) view.findViewById(R.id.image_frame_single);
			image_frame.setTag(filePath);
			ImageView imageView_close = (ImageView) view.findViewById(R.id.imageView_close);
			SmartImageView image = (SmartImageView) view.findViewById(R.id.image);
			{
				float rotation = 0f;

				try
				{
					/**/Log.e(TAG + "_ AppConstant.Gallery2", "image.setImageUrl(filePath);" + filePath);

					Picasso.with(ProductAddProductActivity2.this).load(Uri.fromFile(new File(filePath))).rotate(rotation)
							.noFade().resize(hw, hw).centerCrop().into(image);
				} catch (Exception e)
				{
					e.printStackTrace();
				}
			}
			imageView_close.setTag(image_frame);

			imageView_close.setOnTouchListener(new View.OnTouchListener() {

				@Override
				public boolean onTouch(View v, MotionEvent event)
				{
					if (event.getAction() == MotionEvent.ACTION_UP)
					{
						(new REMOVE_IMAGE_DIALOG(context, (FrameLayout) v.getTag())).show();
						v.setPressed(false);
					} else if (event.getAction() == MotionEvent.ACTION_DOWN)
					{
						v.setPressed(true);
					} else if (event.getAction() == MotionEvent.ACTION_CANCEL)
					{
						v.setPressed(false);
					}
					return true;
				}

			});

			h_linear_layout.addView(view);

		}
	}

	private void nu()
	{
		new_used = (ImageView) findViewById(R.id.new_used);
		new_used.setImageDrawable(getResources().getDrawable(R.drawable.check_on));
		new_used.setTag(R.drawable.check_on);

		View.OnTouchListener onTouch = new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event)
			{
				if (event.getAction() == MotionEvent.ACTION_UP)
				{
					if ((Integer) new_used.getTag() == R.drawable.check_on)
					{
						new_used.setImageDrawable(getResources().getDrawable(R.drawable.check_off));
						new_used.setTag(R.drawable.check_off);
						new_et.setText("");
					} else
					{
						new_used.setImageDrawable(getResources().getDrawable(R.drawable.check_on));
						new_used.setTag(R.drawable.check_on);
						new_et.setText(getResources().getString(R.string.new_));
					}
					v.setPressed(false);
				} else if (event.getAction() == MotionEvent.ACTION_DOWN)
				{
					v.setPressed(true);
				} else if (event.getAction() == MotionEvent.ACTION_CANCEL)
				{
					v.setPressed(false);
				}
				return true;
			}
		};

		new_used.setOnTouchListener(onTouch);
		nu.setOnTouchListener(onTouch);
		new_et.setOnTouchListener(onTouch);
	}

	private void category()
	{
		cat_image = (ImageView) findViewById(R.id.cat_image);
		cat_image.setImageDrawable(getResources().getDrawable(R.drawable.check_off));
		cat_image.setTag(R.drawable.check_off);

		View.OnTouchListener onTouch = new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event)
			{

				KEYBOARD.hideSoftKeyboard(activity);

				if (event.getAction() == MotionEvent.ACTION_UP)
				{
					if ((Integer) cat_image.getTag() == R.drawable.check_on)
					{
						setCat_Checked(false);
					} else
					{
						// (new CAT(context)).show();
						showCategoryList();
					}

					v.setPressed(false);
				} else if (event.getAction() == MotionEvent.ACTION_DOWN)
				{
					v.setPressed(true);
				} else if (event.getAction() == MotionEvent.ACTION_CANCEL)
				{
					v.setPressed(false);
				}
				return true;
			}
		};

		cat_image.setOnTouchListener(onTouch);
		category_et.setOnTouchListener(onTouch);
	}

	public void setCat_Checked(boolean b)
	{
		if (b)
		{
			cat_image.setImageDrawable(getResources().getDrawable(R.drawable.check_on));
			cat_image.setTag(R.drawable.check_on);
		} else
		{
			cat_image.setImageDrawable(getResources().getDrawable(R.drawable.check_off));
			cat_image.setTag(R.drawable.check_off);
			category_et.setText("");
		}
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

	protected void showBackConfirmationDialog()
	{
		(new BackDialog(context)).show();

	}

	private boolean isAnyChangesMade()
	{
		// check if any changes have been made
		// Code TODO_

		Product product = new Product();
		product.setTitle(title_et.getText().toString());
		product.setDescription(description_et.getText().toString().trim());
		product.setPrice(getEnteredProductPrice());
		product.setCategory(category_et.getText().toString());

		if ((Integer) new_used.getTag() == R.drawable.check_on)
		{
			product.setIs_new("1");
		} else
		{
			product.setIs_new("0");
		}
		product.setLatitude(ProductLocation.getLatLng().latitude + "");
		product.setLongitude(ProductLocation.getLatLng().longitude + "");

		ArrayList<String> imagelist = new ArrayList<String>();
		imagelist.addAll(ProductEdit.product.getImages());
		imagelist.addAll(ImageUri.images.keySet());
		product.setImages(imagelist);

		return ProductEdit.product.isSameProduct(product);
	}

	private boolean isFieldsAreEmpty()
	{
		if (!title_et.getText().toString().trim().equals(""))
		{
			return false;
		}

		if (!description_et.getText().toString().trim().equals(""))
		{
			return false;
		}

		if (!description_et.getText().toString().trim().equals(""))
		{
			return false;
		}

		if (!category_et.getText().toString().trim().equals(""))
		{
			return false;
		}

		try
		{
			if (ProductEdit.product.getImages().size() != 0)
			{
				return false;
			}
		} catch (Exception e)
		{

		}

		try
		{
			if ((new ArrayList<String>(ImageUri.images.keySet())).size() != 0)
			{
				return false;
			}
		} catch (Exception e)
		{

		}

		return true;
	}

	private String getEnteredProductPrice()
	{
		return price_et.getText().toString().substring(1).trim();
	}

	@Override
	public void onBackPressed()
	{
		if (PRODUCT_ADD_EDIT.equals(AppConstant.EDIT))
		{
			if (isAnyChangesMade()) // check if any changes have been made
			{
				showBackConfirmationDialog();
			} else
			{
				clearImageSelectionAndStartLandingPage();
			}
		} else if (PRODUCT_ADD_EDIT.equals(AppConstant.ADD))
		{
			if (isFieldsAreEmpty()) // check if fields are empty
			{
				clearImageSelectionAndStartLandingPage();
			} else
			{
				showBackConfirmationDialog();
			}
		}
	}

	private void saveBtn()
	{
		saveLayout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v)
			{
				// Toast.showSmallToast(context,
				// price_et.getText().toString()+".");
				// Toast.showSmallToast(context,
				// getResources().getString(R.string.rupee__));
				// Log.i("price_et.getText().toString()+\".\"",
				// price_et.getText().toString()+".");

				if (title_et.getText().toString().trim().length() <= 0)
				{
					showAlert(getResources().getString(R.string.please_fill_in_));
				} else if (description_et.getText().toString().trim().length() <= 0)
				{
					showAlert(getResources().getString(R.string.please_fill_in_));
				} else if (category_et.getText().toString().length() <= 0)
				{
					showAlert(getResources().getString(R.string.please_fill_in_));
				} else if (price_et.getText().toString().length() <= 0)
				{
					// showAlert(getResources().getString(R.string.please_fill_in_));
					showAlert(getResources().getString(R.string.price_must_be_));
				} else if (Double.parseDouble(getEnteredProductPrice()) < 1.0d)
				{
					showAlert(getResources().getString(R.string.price_must_be_));
				} else if ((((String) saveLayout.getTag()).equals(AppConstant.ADD)) && (ImageUri.images.isEmpty()))
				{
					showAlert(getResources().getString(R.string.please_add___image));
				} else if ((((String) saveLayout.getTag()).equals(AppConstant.EDIT))
						&& ((ProductEdit.product.getImages().size() == 0) && (ImageUri.images.isEmpty())))
				{
					showAlert(getResources().getString(R.string.please_add___image));
				} else
				{
					String tag = (String) saveLayout.getTag();
					/**/Log.e(TAG + "_saveBtn()_onClick()_(String) saveLayout.getTag()", tag);
					(new SaveDialog(context, tag)).show();

				}
			}
		});

	}

	private void showAlert(String msg)
	{
		MessageDialog dialog = new MessageDialog(context, msg);
		dialog.show();
	}

	class BackDialog extends Dialog
	{
		private Context ctx = null;

		public BackDialog(Context context)
		{
			super(context);
			this.ctx = context;
		}

		@Override
		protected void onCreate(Bundle savedInstanceState)
		{
			super.onCreate(savedInstanceState);
			requestWindowFeature(Window.FEATURE_NO_TITLE);
			setContentView(R.layout.dialog_back);
			getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);

			((LinearLayout) findViewById(R.id.keep)).setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v)
				{
					dismiss();
				}
			});

			((LinearLayout) findViewById(R.id.discard)).setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v)
				{
					dismiss();

					if (PRODUCT_ADD_EDIT.equals(AppConstant.EDIT))
					{
						startProductDetailsActivityWithPreviousData();

					} else
					{
						clearImageSelectionAndStartLandingPage();
					}
				}

			});
		}
	}

	private void startProductDetailsActivityWithPreviousData()
	{
		Intent intent = new Intent(context, ProductDetailsActivity.class);
		intent.putExtra(AppConstant.DETAILS_PAGE_TYPE, AppConstant.WITH_Previous_Data);
		intent.putExtra(AppConstant.P_ID, ProductEdit.product.getProduct_id());
		activity.finish();
		activity.startActivity(intent);
		ANIM.onStartActivity(activity);
	}

	private void clearImageSelectionAndStartLandingPage()
	{
		clearImageSelection();
		startLandingPage();
	}

	class SaveDialog extends Dialog
	{
		private Context	ctx			= null;
		private String	ADD_EDIT	= "";

		public SaveDialog(Context context)
		{
			super(context);
			this.ctx = context;
		}

		public SaveDialog(Context context, String ADD_EDIT)
		{
			super(context);
			this.ctx = context;
			this.ADD_EDIT = ADD_EDIT;
		}

		@Override
		protected void onCreate(Bundle savedInstanceState)
		{
			super.onCreate(savedInstanceState);
			requestWindowFeature(Window.FEATURE_NO_TITLE);
			setContentView(R.layout.dialog_save);
			getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);
			//
			((LinearLayout) findViewById(R.id.dialog_save)).setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v)
				{
					UI(ADD_EDIT);
					dismiss();
				}

			});

			((LinearLayout) findViewById(R.id.change_location)).setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v)
				{
					startGoogleMapActivity();

				}
			});
		}
	}

	private ListPopupWindow listPopupWindow = null;

	private void showCategoryList()
	{
		final ArrayList<String> products = new ArrayList<String>(ProdCategory.getCategory().keySet());
		Collections.sort(products);
		listPopupWindow = new ListPopupWindow(ProductAddProductActivity2.this);
		listPopupWindow.setAdapter(new ArrayAdapter(ProductAddProductActivity2.this, R.layout.list_item, products));

		listPopupWindow.setAnchorView(category_et);

		listPopupWindow.setWidth(listPopUpWidth);
		listPopupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);

		listPopupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.style_menu));
		listPopupWindow.setModal(true);
		listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
			{
				listPopupWindow.dismiss();
				String cat_ = products.get(arg2);

				if (ProdCategory.getCategory().get(cat_).size() != 0)
				{
					showSubCategoryList(cat_);
				} else
				{
					setCategoryText(cat_);
				}
			}
		});

		listPopupWindow.show();
	}

	private ListPopupWindow listPopupWindow2 = null;

	private void showSubCategoryList(final String cat)
	{
		final ArrayList<String> products = new ArrayList<String>(ProdCategory.getCategory().get(cat));

		listPopupWindow2 = new ListPopupWindow(ProductAddProductActivity2.this);
		listPopupWindow2.setAdapter(new ArrayAdapter(ProductAddProductActivity2.this, R.layout.list_item, products));

		listPopupWindow2.setAnchorView(category_et);
		listPopupWindow2.setWidth(listPopUpWidth);
		listPopupWindow2.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);

		listPopupWindow2.setBackgroundDrawable(getResources().getDrawable(R.drawable.style_menu));
		listPopupWindow2.setModal(true);
		listPopupWindow2.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
			{
				setCategoryText(cat, products.get(arg2));

				listPopupWindow2.dismiss();
			}
		});

		listPopupWindow2.show();
	}

	private void setCategoryText(String category)
	{
		setCategoryText(category, "");
	}

	private void setCategoryText(String category, String subCategory)
	{
		if (!subCategory.equals(""))
		{
			category_et.setText(
					((new StringBuilder()).append("# ").append(category).append(", ").append("# ").append(subCategory))
							.toString());
		} else
		{
			category_et.setText(((new StringBuilder()).append("# ").append(category)).toString());
		}

		setCat_Checked(true);
	}

	class REMOVE_IMAGE_FROM_WEB_DIALOG extends Dialog
	{
		private String		imageName	= "";
		private FrameLayout	frameL;

		public REMOVE_IMAGE_FROM_WEB_DIALOG(Context context, FrameLayout frameL)
		{
			super(context);
			this.frameL = frameL;
		}

		@Override
		protected void onCreate(Bundle savedInstanceState)
		{
			super.onCreate(savedInstanceState);
			requestWindowFeature(Window.FEATURE_NO_TITLE);
			setContentView(R.layout.dialog_delete_image);
			getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);

			((LinearLayout) findViewById(R.id.ok)).setOnTouchListener(new View.OnTouchListener() {

				@Override
				public boolean onTouch(View v, MotionEvent event)
				{
					if (event.getAction() == MotionEvent.ACTION_UP)
					{
						storeImageToDelete(frameL);
						dismiss();
						v.setPressed(false);
					} else if (event.getAction() == MotionEvent.ACTION_DOWN)
					{
						v.setPressed(true);
					} else if (event.getAction() == MotionEvent.ACTION_CANCEL)
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
					} else if (event.getAction() == MotionEvent.ACTION_DOWN)
					{
						v.setPressed(true);
					} else if (event.getAction() == MotionEvent.ACTION_CANCEL)
					{
						v.setPressed(false);
					}

					return true;
				}
			});
		}
	}

	/* Stores Name of the images to delete from database */
	private ArrayList<String> imagesToDelete = new ArrayList<String>();

	private void storeImageToDelete(final FrameLayout frameL)
	{
		frameL.setVisibility(View.GONE);
		ProductEdit.product.getImages().remove((String) frameL.getTag());

		String imageName = "";
		try
		{
			String filePath = (String) frameL.getTag();
			/**/Log.e(TAG + "_storeImageToDelete()_filePath", filePath + ".");
			imageName = filePath.substring(filePath.lastIndexOf("/") + 1);
		} catch (Exception e)
		{
		}

		imagesToDelete.add(imageName);
	}

	class REMOVE_IMAGE_DIALOG extends Dialog
	{
		private FrameLayout frameL;

		public REMOVE_IMAGE_DIALOG(Context context, FrameLayout frameL)
		{
			super(context);
			this.frameL = frameL;
		}

		@Override
		protected void onCreate(Bundle savedInstanceState)
		{
			super.onCreate(savedInstanceState);
			requestWindowFeature(Window.FEATURE_NO_TITLE);
			setContentView(R.layout.dialog_delete_image);
			getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);

			((LinearLayout) findViewById(R.id.ok)).setOnTouchListener(new View.OnTouchListener() {

				@Override
				public boolean onTouch(View v, MotionEvent event)
				{
					if (event.getAction() == MotionEvent.ACTION_UP)
					{
						ImageUri.images.remove((String) frameL.getTag());
						frameL.setVisibility(View.GONE);
						dismiss();

						v.setPressed(false);
					} else if (event.getAction() == MotionEvent.ACTION_DOWN)
					{
						v.setPressed(true);
					} else if (event.getAction() == MotionEvent.ACTION_CANCEL)
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
					} else if (event.getAction() == MotionEvent.ACTION_DOWN)
					{
						v.setPressed(true);
					} else if (event.getAction() == MotionEvent.ACTION_CANCEL)
					{
						v.setPressed(false);
					}

					return true;
				}
			});
		}

	}

	public static Bitmap decodeFile(String filePath, int WIDTH, int HIGHT)
	{
		try
		{
			File f = new File(filePath);

			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(new FileInputStream(f), null, o);

			final int REQUIRED_WIDTH = WIDTH;
			final int REQUIRED_HIGHT = HIGHT;
			int scale = 1;
			while (o.outWidth / scale / 2 >= REQUIRED_WIDTH && o.outHeight / scale / 2 >= REQUIRED_HIGHT)
				scale *= 2;

			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize = scale;
			return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		return null;
	}

	/* */
	
	private void startProgressDialog()
	{
		dialog = new ProgressDialog(context);
		dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		dialog.setIndeterminate(true);
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();
	}

	private void getCategory()
	{
		startProgressDialog();
		
		String action = "category_get";
		
		com.at.drgrep.web.AsyncWebClient asyncWebClient = new com.at.drgrep.web.AsyncWebClient();
		asyncWebClient.SetUrl(AppConstant.URL);

		RequestParams reqParam = new RequestParams();
		reqParam.add("action", action);
		// if (!lat.equals(""))
		// {
		// reqParam.add("lat", lat);
		// }
		// if (!longi.equals(""))
		// {
		// reqParam.add("long", longi);
		// }

		asyncWebClient.get(reqParam, new JsonHttpResponseHandler() {
			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(int statusCode, org.apache.http.Header[] headers, org.json.JSONObject jsr)
			{
				try
				{
					JSONArray jarray = jsr.getJSONArray("data");
					HashMap<String, ArrayList<String>> hMapObj = new HashMap<String, ArrayList<String>>();

					for (int i = 0; i < jarray.length(); i++)
					{
						JSONObject obj = jarray.getJSONObject(i);

						String catName = obj.getString("cat_name");

						/**/ Log.d(TAG + "_getCategory()_ catName", catName);

						ArrayList<String> subCategoryList = new ArrayList<String>();

						try
						{
							JSONArray jroption = obj.getJSONArray("sub");
							for (int j = 0; j < jroption.length(); j++)
							{
								JSONObject obj2 = jroption.getJSONObject(j);
								String subCat = obj2.getString("cat_name");
								subCategoryList.add(subCat);

								/**/ Log.d(TAG + "_getCategory()_ subCat Name", subCat);
							}
						} catch (Exception e)
						{

						}

						Collections.sort(subCategoryList);

						hMapObj.put(catName, subCategoryList);
					}

					ProdCategory.setCategory(hMapObj);

				} catch (JSONException e)
				{
					Log.e(TAG + "_getCategory()_ JSONException", e.getMessage());
				} finally
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

	private ArrayList<String>	fp			= null;
	private int					imageCount	= 0;
	private String				lastId		= null;

	private void uploadProduct(String Add_Edit)
	{
		if (Network.isConnected(context))
		{
			String[] pDetails = new String[8];
			pDetails[0] = title_et.getText().toString();
			pDetails[1] = description_et.getText().toString().trim();
			pDetails[2] = price_et.getText().toString().substring(1);

			pDetails[3] = category_et.getText().toString();

			if ((Integer) new_used.getTag() == R.drawable.check_on)
			{
				pDetails[4] = "1";
			} else
			{
				pDetails[4] = "0";
			}

			pDetails[5] = ProductLocation.getLatLng().latitude + ""; // latitude
			pDetails[6] = ProductLocation.getLatLng().longitude + ""; // longitude

			if (Add_Edit.equals(AppConstant.EDIT))
			{
				pDetails[7] = ProductEdit.product.getProduct_id(); // _Product_ID
			}

			uploadProduct(pDetails[0], pDetails[1], pDetails[2], pDetails[3], pDetails[4], pDetails[5], pDetails[6],
					pDetails[7], Add_Edit);
		} else
		{
			Toast.showNetworkErrorMsgToast(context);
		}
	}

	private void uploadProduct(String title, String description, String price, String category, String is_new,
			String latitude, String longitude, String product_id, final String Add_Edit)
	{
		String action = "product_add";

		if (Add_Edit.equals(AppConstant.EDIT))
		{
			action = "product_update";
		}
		com.at.drgrep.web.AsyncWebClient asyncWebClient = new com.at.drgrep.web.AsyncWebClient();
		asyncWebClient.SetUrl(AppConstant.URL);

		RequestParams reqParam = new RequestParams();
		reqParam.add("action", action);
		reqParam.add("title", title);
		reqParam.add("description", description);
		reqParam.add("price", price);
		reqParam.add("category", category);
		reqParam.add("is_new", is_new);
		reqParam.add("lat", latitude);
		reqParam.add("long", longitude);

		if (Add_Edit.equals("EDIT"))
		{
			reqParam.add("product_id", product_id);
		}

		/**/Log.e(TAG + "_uploadProduct()_reqParam:", reqParam.toString() + ".");

		asyncWebClient.post(reqParam, new JsonHttpResponseHandler() {

			String response_message = "";

			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(int statusCode, org.apache.http.Header[] headers, org.json.JSONObject jsr)
			{
				try
				{
					String response_code = jsr.getString("response_code");
					response_message = jsr.getString("response_message");

					if (response_code.equals("1"))
					{
						lastId = jsr.getString("last_id");
						uploadProductImage(0, Add_Edit);
					} else
					{
						dialog.dismiss();
						Toast.showSmallToast(context, response_message);
					}

				} catch (JSONException e)
				{
					dialog.dismiss();
					Log.e(TAG + "_uploadProduct()_ JSONException", e.getMessage());
				} finally
				{
					// dialog.dismiss();
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

	/* Resize Image */
	private ResizeImage resizeImage;

	private void UI(String Add_Edit)
	{
		resizeImages(Add_Edit);
	}

	private void resizeImages(final String Add_Edit)
	{
		fp = new ArrayList<String>(ImageUri.images.keySet());

		(new AsyncTask<Void, Void, Void>() {
			
			@Override
			protected void onPreExecute()
			{
				startProgressDialog();
			}

			@Override
			protected Void doInBackground(Void... params)
			{
				Bitmap bitmap = null;
				for (String path : fp)
				{
					try
					{
						bitmap = BITMAP.filePathToBitmap(path);

						int height = bitmap.getHeight();
						int width = bitmap.getWidth();

						File file = null;
						long size = 0;
						try
						{
							file = new File(path);
							size = file.length();
						} catch (Exception e)
						{
							size = 0;
						}

						if (size > 1024 * AppConstant.IMAGE_FILE_SIZE_MIN) // resize
																			// image
																			// if
																			// file
																			// size
																			// is
						// greater than 768 kb.
						{
							// resizeImage = new ResizeImage(file);
						}

					} catch (FileNotFoundException e)
					{
						e.printStackTrace();
					}

				}
				return null;
			}

			@Override
			protected void onPostExecute(Void result)
			{
				super.onPostExecute(result);

				uploadProduct(Add_Edit);

			}
		}).execute();

	}

	private void uploadProductImage(final int pos, final String add_Edit)
	{
		if (pos < fp.size())
		{
			final String action = "product_image_upload";
			AsyncWebClient asyncWebClient = new AsyncWebClient();
			asyncWebClient.SetUrl(AppConstant.URL);

			String filePath = fp.get(pos);

			RequestParams reqParam = new RequestParams();
			reqParam.add("action", action);
			reqParam.add("product_id", lastId);
			
			try
			{
				File myFile = new File(filePath);
				reqParam.put("image_data", new FileInputStream(myFile), myFile.getName());

			} catch (FileNotFoundException e)
			{
				Log.e(TAG + "_uploadProductImage()_ FileNotFoundException", "image_data FileNotFoundException!");
				e.printStackTrace();
			} catch (Exception e)
			{
				Log.e(TAG + "_uploadProductImage()_ Exception", "image_data Exception!");
				e.printStackTrace();
			}

			asyncWebClient.post(reqParam, new JsonHttpResponseHandler() {
				@Override
				public void onSuccess(int statusCode, org.apache.http.Header[] headers, org.json.JSONObject jsr)
				{
					try
					{
						String response_code = jsr.getString("response_code");
						String response_message = jsr.getString("response_message");

						if (response_code.equals("1"))
						{

						} else
						{
							Toast.showSmallToast(context, response_message);
						}

						/**/uploadProductImage(pos + 1, add_Edit);

					} catch (JSONException e)
					{
						dialog.dismiss();
						Log.e(TAG + "_uploadProductImage()_ JSONException", e.getMessage());
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
		} else
		{
			deleteImageFromWeb(0, add_Edit);
		}
	}

	@Override
	protected void onRestart()
	{
		super.onRestart();

		/**/Log.e(TAG + "_onRestart()_", "onRestart()");

		removeLinearLayoutChildViews();
		loadAllImagesfromWEB();
		loadAllLocalImages();
	}

	private void loadAllLocalImages()
	{
		if (!ImageUri.images.isEmpty())
		{
			/**/Log.e(TAG + "_onRestart()_", "!ImageUri.images.isEmpty()");
			for (Map.Entry<String, String> m : ImageUri.images.entrySet())
			{
				addImageToHSV(m.getKey(), m.getValue());
			}
		}
	}

	private void loadAllImagesfromWEB()
	{
		if (ProductEdit.product != null)
		{
			/**/Log.e(TAG + "_onRestart()_", "ProductEdit.product != null");
			for (String url : ProductEdit.product.getImages())
			{
				loadImageFromWEB(url.trim());
			}
		}
	}

	private void removeLinearLayoutChildViews()
	{
		h_linear_layout.removeAllViews();
	}

	public void startLandingPage()
	{
		
		Intent intent = new Intent(ProductAddProductActivity2.this, ProductListActivity.class);
		ProductAddProductActivity2.this.finish();
		startActivity(intent);
		ANIM.onStartActivity(activity);
	}

	public void clearImageSelection()
	{
		/**/Log.e(TAG + "_ProductAddActivity _ clearImageSelection()", " clearImageSelection()");
		try
		{
			fp.clear();
		} catch (Exception e)
		{
		}

		try
		{
			ProductEdit.product.getImages().clear();
		} catch (Exception e)
		{
		}

		try
		{
			ImageUri.images.clear();
		} catch (Exception e)
		{
		}
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		clearImageSelection();
	}

	protected void deleteImageFromWeb(final int pos,final String add_Edit)
	{
		if (pos < imagesToDelete.size())
		{
			String action = "product_image_delete";

			com.at.drgrep.web.AsyncWebClient asyncWebClient = new com.at.drgrep.web.AsyncWebClient();
			asyncWebClient.SetUrl(AppConstant.URL);

			RequestParams reqParam = new RequestParams();
			reqParam.add("action", action);
			reqParam.add("image_name", imagesToDelete.get(pos));

			asyncWebClient.get(reqParam, new JsonHttpResponseHandler() {
				@SuppressWarnings("unchecked")
				@Override
				public void onSuccess(int statusCode, org.apache.http.Header[] headers, org.json.JSONObject jsr)
				{
					try
					{
						if (jsr.getString("response_code").equals("1"))
						{

						} else
						{

						}

					} catch (JSONException e)
					{
						dialog.dismiss();
						Log.e(TAG + "_JSONException", e.getMessage());
					} finally
					{
						deleteImageFromWeb(pos + 1,add_Edit);
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
		} else
		{
			clearImageSelection();
			dialog.dismiss();
			if (add_Edit.equals(AppConstant.EDIT))
			{
				Toast.showSmallToast(context, getResources().getString(R.string.product_edited_successfully_));

				startProductDetailsActivityWithPreviousData();

			} else if (add_Edit.equals(AppConstant.ADD))
			{
				startCongratulationsActivity();
			}
		}
	}
}
