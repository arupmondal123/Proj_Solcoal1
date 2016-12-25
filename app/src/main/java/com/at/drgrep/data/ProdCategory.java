package com.at.drgrep.data;

import java.util.ArrayList;
import java.util.HashMap;

public class ProdCategory
{
	private static HashMap<String, ArrayList<String>> category = new HashMap<String, ArrayList<String>>();

	public static HashMap<String, ArrayList<String>> getCategory()
	{
		return category;
	}

	public static void setCategory(HashMap<String, ArrayList<String>> category)
	{
		ProdCategory.category = category;
	}
}
