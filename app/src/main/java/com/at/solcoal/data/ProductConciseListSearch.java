package com.at.solcoal.data;

import com.at.solcoal.model.Product_Concise;

import java.util.ArrayList;

public class ProductConciseListSearch
{
	public static ArrayList<Product_Concise>	productConciseListSearch	= new ArrayList<Product_Concise>();
	public static ArrayList<Product_Concise>	productConciseListSearchForUser	= new ArrayList<Product_Concise>();
	
	public static void clear()
	{
		try
		{
			productConciseListSearch.clear();
		} catch (Exception e)
		{
		}
	}

	public static void clearForUser()
	{
		try
		{
			productConciseListSearchForUser.clear();
		} catch (Exception e)
		{
		}
	}
}
