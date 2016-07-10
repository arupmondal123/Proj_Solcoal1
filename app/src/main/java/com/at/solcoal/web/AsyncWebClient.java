package com.at.solcoal.web;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.ResponseHandlerInterface;

import android.content.Context;

public class AsyncWebClient extends AsyncHttpClient
{
	String ApiUrl="";

	public void SetUrl(String u)
	{
		ApiUrl = u;
	}

	public RequestHandle get(Context context, RequestParams params, ResponseHandlerInterface responseHandler)
	{
		// TODO Auto-generated method stub
		super.addHeader("content-type", "application/json");
		return super.get(context, ApiUrl, params, responseHandler);
	}

	public RequestHandle get(RequestParams params, ResponseHandlerInterface responseHandler)
	{
		// TODO Auto-generated method stub
		super.addHeader("content-type", "application/json");
		return super.get(ApiUrl, params, responseHandler);
	}

	public RequestHandle post(RequestParams params, ResponseHandlerInterface responseHandler)
	{
		// TODO Auto-generated method stub
		/// super.addHeader("content-type", "application/json");
		return super.post(ApiUrl, params, responseHandler);
	}
}
