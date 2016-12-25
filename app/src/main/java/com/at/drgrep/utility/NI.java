package com.at.drgrep.utility;

public class NI
{
	public static String getInitial(String userName)
	{
		char initial = 0;

		try
		{
			initial = userName.charAt(0);
		}
		catch (Exception e)
		{

		}

		return initial + "";
	}

}
