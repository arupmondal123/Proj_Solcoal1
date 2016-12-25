package com.at.drgrep.data;

import com.google.android.gms.maps.model.LatLng;

public class ProductLocation
{
	private static LatLng latLng = null;
	
	public static LatLng getLatLng()
	{
		return latLng;
	}
	public static void setLatLng(LatLng latLng)
	{
		ProductLocation.latLng = latLng;
	}
}
