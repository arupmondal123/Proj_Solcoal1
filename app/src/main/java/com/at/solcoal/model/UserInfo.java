
package com.at.solcoal.model;

import java.io.Serializable;

import android.net.Uri;


/**
 * Created by anjan on 17-03-2016.
 */
public class UserInfo implements Serializable
{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;

	private String				name				= "";

	private String				extId				= "";

	private String				extSource			= "";

	private String				id					= "";

	private String				email				= "";

	private String				gender				= "";

	/* change */

	private String				dob					= "";

	public String getDob()
	{
		return dob;
	}

	public void setDob(String dob)
	{
		this.dob = dob;
	}

	/* /change */

	private Uri imageUrl;

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getExtId()
	{
		return extId;
	}

	public void setExtId(String extId)
	{
		this.extId = extId;
	}

	public String getExtSource()
	{
		return extSource;
	}

	public void setExtSource(String extSource)
	{
		this.extSource = extSource;
	}

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public String getEmail()
	{
		return email;
	}

	public void setEmail(String email)
	{
		this.email = email;
	}

	public Uri getImageUrl()
	{
		return imageUrl;
	}

	public void setImageUrl(Uri imageUrl)
	{
		this.imageUrl = imageUrl;
	}

	public String getGender()
	{
		return gender;
	}

	public void setGender(String gender)
	{
		this.gender = gender;
	}
}
