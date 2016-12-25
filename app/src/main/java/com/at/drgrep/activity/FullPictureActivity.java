package com.at.drgrep.activity;

import com.at.drgrep.R;
import com.at.drgrep.animation.ANIM;
import com.at.drgrep.data.ProductEdit;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.Indicators.PagerIndicator.IndicatorVisibility;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx.OnPageChangeListener;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

public class FullPictureActivity extends Activity
{
	private SliderLayout		slider		= null;
	private TextView			textView			= null;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.act_full_image);

		slider = (SliderLayout) findViewById(R.id.slider);
		textView = (TextView) findViewById(R.id.t);


		for (String uri : ProductEdit.product.getImages())
		{
			/**/ Log.e("uri", uri);
			
			TextSliderView textSliderView = new TextSliderView(this);
			textSliderView.image(uri).setScaleType(BaseSliderView.ScaleType.CenterInside);
			slider.addSlider(textSliderView);
		}

		final int total = ProductEdit.product.getImages().size();
		textView.setText("1 of " + total);

		slider.setPresetTransformer(SliderLayout.Transformer.Default);
		// slider.setPresetIndicator(SliderLayout.PresetIndicators.Left_Top);
		// PagerIndicator pagerIndicator = (PagerIndicator)
		// findViewById(R.id.custom_indicator);
		// pagerIndicator.setDefaultIndicatorColor(Color.WHITE, Color.GRAY);
		// slider.setCustomIndicator(pagerIndicator);
		slider.setIndicatorVisibility(IndicatorVisibility.Invisible);
		slider.setCustomAnimation(new DescriptionAnimation());
		slider.setDuration(5000);
		slider.setPresetTransformer(2);

		slider.addOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position)
			{
				String c = (position + 1) + " of " + total;
				textView.setText(c);
			}

			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
			{
			}

			@Override
			public void onPageScrollStateChanged(int state)
			{
			}
		});

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
		FullPictureActivity.this.finish();
		ANIM.onBack(FullPictureActivity.this);
	}
}
