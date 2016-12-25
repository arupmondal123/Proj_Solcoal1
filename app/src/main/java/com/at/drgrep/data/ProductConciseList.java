package com.at.drgrep.data;

import java.util.ArrayList;

import com.at.drgrep.model.Product_Concise;
import com.at.drgrep.model.Product_Concise_Shop;

public class ProductConciseList
{
	public static ArrayList<Product_Concise>	productConciseList	= new ArrayList<Product_Concise>();
	public static ArrayList<Product_Concise>	productConciseListForUser	= new ArrayList<Product_Concise>();
	public static ArrayList<Product_Concise_Shop>	productConciseListForUserShop	= new ArrayList<Product_Concise_Shop>();
	public static ArrayList<Product_Concise_Shop>	productConciseListForUserShopAddProducts	= new ArrayList<Product_Concise_Shop>();

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

	public static void clearForUserShop()
	{
		try
		{
			productConciseListForUserShop.clear();
		} catch (Exception e)
		{
		}
	}

	public static void clearForUserShopAddProducts()
	{
		try
		{
			productConciseListForUserShopAddProducts.clear();
		} catch (Exception e)
		{
		}
	}
}
