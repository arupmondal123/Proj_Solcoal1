package com.at.solcoal.data;

import java.util.ArrayList;

import com.at.solcoal.model.Product;

public class ProductList
{
	public static ArrayList<Product> pList = new ArrayList<Product>();
	
	public static void clear()
	{
		try
		{
			pList.clear();
		} catch (Exception e)
		{
		}
	}
}
