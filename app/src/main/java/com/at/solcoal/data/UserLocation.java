package com.at.solcoal.data;

import com.google.android.gms.maps.model.LatLng;

public class UserLocation
{
	private static LatLng latLng = null;
	private static LatLng userORGLatLong = null;
	private static String userAddress = "";
	
	public static LatLng getUserPreferredLatLng()
	{
		return latLng;
	}
	public static void setUserPreferredLatLng(LatLng latLng)
	{
		UserLocation.latLng = latLng;
	}
	
	public static String getUserAddress()
	{
		return userAddress;
	}
	public static void setUserAddress(String userAddress)
	{
		UserLocation.userAddress = userAddress;
	}
	
	public static LatLng getUserORGLatLong()
	{
		return userORGLatLong;
	}
	public static void setUserORGLatLong(LatLng userORGLatLong)
	{
		UserLocation.userORGLatLong = userORGLatLong;
	}
}
