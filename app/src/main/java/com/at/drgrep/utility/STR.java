package com.at.drgrep.utility;

public class STR
{
	public static String getSliceStr(String str, int length)
	{
		try
		{
			int strL = str.length();

			if (strL <= 0)
			{
				return "";
			}
			else if (length >= strL)
			{
				return str;
			}
			else
			{
				return str.substring(0, length).trim() + "...";
			}

		} catch (Exception e)
		{
			return str;
		}
	}
}
