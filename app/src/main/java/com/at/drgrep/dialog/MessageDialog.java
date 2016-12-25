package com.at.drgrep.dialog;

import com.at.drgrep.R;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MessageDialog extends Dialog
{
	private Context ctx = null;
	private String message = null;

	public MessageDialog(Context context, String message)
	{
		super(context);
		this.ctx = context;
		this.message = message;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialog_warning);
		getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);
		
		View.OnTouchListener onTouch = new View.OnTouchListener() {

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
		};

		((TextView) findViewById(R.id.message)).setText(message);
		((RelativeLayout) findViewById(R.id.ok)).setOnTouchListener(onTouch);
		((TextView) findViewById(R.id.tv4)).setOnTouchListener(onTouch);
	}
}