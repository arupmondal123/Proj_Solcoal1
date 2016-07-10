package com.at.solcoal.utility;

import com.at.solcoal.constants.AppConstant;

import android.content.Context;
import android.graphics.Typeface;

public class TYPEFACE
{
	public static Typeface getTF(Context context)
	{
		return Typefaces.get(context, AppConstant.app_font);
	}
}
