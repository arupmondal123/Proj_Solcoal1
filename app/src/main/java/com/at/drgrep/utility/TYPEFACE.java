package com.at.drgrep.utility;

import com.at.drgrep.constants.AppConstant;

import android.content.Context;
import android.graphics.Typeface;

public class TYPEFACE
{
	public static Typeface getTF(Context context)
	{
		return Typefaces.get(context, AppConstant.app_font);
	}
}
