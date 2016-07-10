package com.at.solcoal.model;

public class Product_Concise
{
	private String	product_id;
	private String	title;
	private String	price;
	private String	prod_img_link;
	private String	distance;
	private String	userId;


	public Product_Concise(String product_id, String title, String price, String prod_img_link, String distance)
	{
		this.product_id = product_id;
		this.title = title;
		this.price = price;
		this.prod_img_link = prod_img_link;
		this.distance = distance;
	}

	public Product_Concise(String product_id, String title, String price, String prod_img_link, String distance, String userId)
	{
		this.product_id = product_id;
		this.title = title;
		this.price = price;
		this.prod_img_link = prod_img_link;
		this.distance = distance;
		this.userId = userId;
	}

	public String getProduct_id()
	{
		return product_id;
	}

	public String getTitle()
	{
		return title;
	}

	public String getPrice()
	{
		return price;
	}

	public String getProd_img_link()
	{
		return prod_img_link;
	}

	public String getDistance()
	{
		return distance;
	}

	public String getUserId() {
		return userId;
	}
}
