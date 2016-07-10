
package com.at.solcoal.utility;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.util.Base64;
import android.util.Log;


public class KeyHash
{
	public static String printKeyHash(Activity context)
	{
		PackageInfo packageInfo;
		String key = null;
		try
		{
			String packageName = context.getApplicationContext().getPackageName();
			packageInfo = context.getPackageManager().getPackageInfo(packageName, PackageManager.GET_SIGNATURES);

			Log.e("Package Name=", context.getApplicationContext().getPackageName());

			for (Signature signature : packageInfo.signatures)
			{
				MessageDigest md = MessageDigest.getInstance("SHA");
				md.update(signature.toByteArray());
				key = new String(Base64.encode(md.digest(), 0));

				// String key = new String(Base64.encodeBytes(md.digest()));
				Log.e("Key Hash=", key);
			}
		}
		catch (NameNotFoundException e1)
		{
			Log.e("Name not found", e1.toString());
		}
		catch (NoSuchAlgorithmException e)
		{
			Log.e("No such an algorithm", e.toString());
		}
		catch (Exception e)
		{
			Log.e("Exception", e.toString());
		}

		return key;
	}
}
