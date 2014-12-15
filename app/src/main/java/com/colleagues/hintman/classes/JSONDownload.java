package com.colleagues.hintman.classes;

import android.util.*;
import java.io.*;
import org.apache.http.*;
import org.apache.http.client.*;
import org.apache.http.client.methods.*;
import org.apache.http.impl.client.*;
import org.json.*;

public class JSONDownload extends BaseDownload
{

	@Override
	public InputStream getInputStrin()
	{
		InputStream is = null;
		try {

			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpGet httpPost = new HttpGet(this.url + url);

			HttpResponse httpResponse = httpClient.execute(httpPost);
			HttpEntity httpEntity = httpResponse.getEntity();
			is = httpEntity.getContent();

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return is;
	}

}
