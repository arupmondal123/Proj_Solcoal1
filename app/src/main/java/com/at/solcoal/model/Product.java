package com.at.solcoal.model;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;

public class Product  implements Parcelable
{
	private String				product_id;
	private String				product_owner_id;
	private String				product_owner_name;
	private String				title;
	private String				description;
	private String				price;
	private String				category;
	private String				is_new;
	private String				add_time;
	private String				latitude;
	private String				longitude;
	private String				update_time;
	private String				is_active;
	private ArrayList<String>	images	= new ArrayList<String>();
	private String				distance;

	public String getProduct_id()
	{
		return product_id;
	}

	public void setProduct_id(String product_id)
	{
		this.product_id = product_id;
	}

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public String getPrice()
	{
		return price;
	}

	public void setPrice(String price)
	{
		this.price = price;
	}

	public String getCategory()
	{
		return category;
	}

	public void setCategory(String category)
	{
		this.category = category;
	}

	public String getIs_new()
	{
		return is_new;
	}

	public void setIs_new(String is_new)
	{
		this.is_new = is_new;
	}

	public String getAdd_time()
	{
		return add_time;
	}

	public void setAdd_time(String add_time)
	{
		this.add_time = add_time;
	}

	public String getLatitude()
	{
		return latitude;
	}

	public void setLatitude(String latitude)
	{
		this.latitude = latitude;
	}

	public String getLongitude()
	{
		return longitude;
	}

	public void setLongitude(String longitude)
	{
		this.longitude = longitude;
	}

	public String getUpdate_time()
	{
		return update_time;
	}

	public void setUpdate_time(String update_time)
	{
		this.update_time = update_time;
	}

	public String getIs_active()
	{
		return is_active;
	}

	public void setIs_active(String is_active)
	{
		this.is_active = is_active;
	}

	public ArrayList<String> getImages()
	{
		return images;
	}

	public void setImages(ArrayList<String> images)
	{
		this.images = images;
	}

	public String getDistance()
	{
		return distance;
	}

	public void setDistance(String distance)
	{
		this.distance = distance;
	}

	public void clear()
	{
		product_id = "";
		title = "";
		description = "";
		price = "";
		category = "";
		is_new = "";
		add_time = "";
		latitude = "";
		longitude = "";
		update_time = "";
		is_active = "";
		product_owner_id = "";
		product_owner_name = "";

		distance = "";
		try
		{
			images.clear();
		} catch (Exception e)
		{

		}
	}

	public String getProduct_owner_id() {
		return product_owner_id;
	}

	public void setProduct_owner_id(String product_owner_id) {
		this.product_owner_id = product_owner_id;
	}

	public String getProduct_owner_name() {
		return product_owner_name;
	}

	public void setProduct_owner_name(String product_owner_name) {
		this.product_owner_name = product_owner_name;
	}

	public boolean isSameProduct(Product prod)
	{
		if (prod == null)
		{
			return false;
		}

		if (!this.product_id.equals(prod.getProduct_id()))
		{
			return true;
		}

		if (!this.title.equals(prod.getTitle()))
		{

			return true;
		}

		if (!this.description.equals(prod.getDescription()))
		{
			return true;
		}

		if (!this.price.equals(prod.getPrice()))
		{
			return true;
		}

		if (!this.category.equals(prod.getCategory()))
		{
			return true;
		}

		if (!this.is_new.equals(prod.getCategory()))
		{
			return true;
		}

		if (!this.latitude.equals(prod.getLatitude()))
		{
			return true;
		}

		if (!this.longitude.equals(prod.getLongitude()))
		{
			return true;
		}

//		if (this.images.size() != prod.getImages().size())
//		{
//			return true;
//		} else
//		{
//			ArrayList<String> prodImages = prod.getImages();
//			
//			for(int i = 0 ; i < images.size() ; i++)
//			{
//				if(!images.get(i).equals(prodImages.get(i)))
//				{
//					return true;
//				}
//			}
//		}

		return false;
	}

	@Override
	public int describeContents()
	{
		return 0;
	}

	@Override
	public void writeToParcel(Parcel parcel, int flags)
	{
		parcel.writeString(product_id);
		parcel.writeString(product_owner_id);
		parcel.writeString(product_owner_name);
		parcel.writeString(title);
		parcel.writeString(description);
		parcel.writeString(price);
		parcel.writeString(category);
		parcel.writeString(is_new);
		parcel.writeString(latitude);
		parcel.writeString(longitude);
		parcel.writeString(is_active);
		parcel.writeString(distance);
		parcel.writeString(add_time);
		parcel.writeString(update_time);
		parcel.writeList(images);
	}
}
