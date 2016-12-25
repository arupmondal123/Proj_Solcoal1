package com.at.drgrep.utility;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

public class CLTextView extends TextView
{
	private Context context = null;

	public CLTextView(Context context)
	{
		super(context);
		this.context = context;
		st();
	}

	public CLTextView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		this.context = context;
		st();
	}

	public CLTextView(Context context, AttributeSet attrs, int defStyleAttr)
	{
		super(context, attrs, defStyleAttr);
		this.context = context;
		st();
	}

	// public CLTextView(Context context, AttributeSet attrs, int defStyleAttr,
	// int defStyleRes)
	// {
	// super(context, attrs, defStyleAttr, defStyleRes);
	// }

	private void st()
	{
		setTypeface(Typefaces.get(context, "Calibri_Light.ttf"));
	}

}
