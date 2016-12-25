package com.at.drgrep.utility;

public class StringSizeLimit
{
	public String getLimitedString(String str, int limit)
	{
		if(limit >= str.length())
		{
			return str;
		}
		else
		{
			return str.substring(0,limit)+"...";
		}
	}
}
