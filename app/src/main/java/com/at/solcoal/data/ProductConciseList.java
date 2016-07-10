package com.at.solcoal.data;

import java.util.ArrayList;

import com.at.solcoal.model.Product;
import com.at.solcoal.model.Product_Concise;

public class ProductConciseList
{
	public static ArrayList<Product_Concise>	productConciseList	= new ArrayList<Product_Concise>();
	public static ArrayList<Product_Concise>	productConciseListForUser	= new ArrayList<Product_Concise>();
	
	public static void clear()
	{
		try
		{
			productConciseList.clear();
		} catch (Exception e)
		{
		}
	}

	public static void clearForUser()
	{
		try
		{
			productConciseListForUser.clear();
		} catch (Exception e)
		{
		}
	}
}
