package com.at.drgrep.model;

public class M1 implements Comparable<M1>
{
	public String imagePath;
	public Long dateTaken; 
	public Long imageSize;
	public int orientation;

	public M1(String imagePath, Long dateTaken,Long imageSize,int orientation)
	{
		this.imagePath = imagePath;
		this.dateTaken = dateTaken;
		this.imageSize= imageSize;
		this.orientation = orientation;
	}

	@Override
	public int compareTo(M1 another)
	{
		if (this.dateTaken < another.dateTaken)
		{
			return 1;
		}
		else if (this.dateTaken > another.dateTaken)
		{
			return -1;
		}
		else
		{
			return 0;
		}
	}
	
	public Long getImageSize()
	{
		return this.imageSize;
	}

	@Override
	public String toString()
	{
		return this.imagePath;
	}
}
