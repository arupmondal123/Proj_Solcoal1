package com.at.drgrep.model;

public class Product_Concise_Shop
{
	private String	product_id;
	private String	title;
	private String	price;
	private String	prod_img_link;
	private String	distance;
	private String	userId;

	public void setIsprodinstore(String isprodinstore) {
		this.isprodinstore = isprodinstore;
	}

	private String	isprodinstore;


	public void setShop_id(String shop_id) {
		this.shop_id = shop_id;
	}

	private String	shop_id;


	public Product_Concise_Shop(String product_id, String title, String price, String prod_img_link, String distance,String isprodinstore,String shop_id)
	{
		this.product_id = product_id;
		this.title = title;
		this.price = price;
		this.prod_img_link = prod_img_link;
		this.distance = distance;
		this.isprodinstore = isprodinstore;
		this.shop_id = shop_id;
	}

	public Product_Concise_Shop(String product_id, String title, String price, String prod_img_link, String distance, String userId,String isprodinstore,String shop_id)
	{
		this.product_id = product_id;
		this.title = title;
		this.price = price;
		this.prod_img_link = prod_img_link;
		this.distance = distance;
		this.userId = userId;
		this.isprodinstore = isprodinstore;
		this.shop_id = shop_id;
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

	public String getIsprodinstore() {
		return isprodinstore;
	}


	public String getShop_id() {
		return shop_id;
	}
}
