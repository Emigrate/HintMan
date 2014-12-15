package com.colleagues.hintman.classes;

import android.util.*;
import java.io.*;
import org.apache.http.*;
import org.apache.http.client.*;
import org.apache.http.client.methods.*;
import org.apache.http.impl.client.*;
import org.json.*;
import android.content.*;
import android.provider.*;
import android.preference.*;
import java.util.*;
import org.apache.http.message.*;
import org.apache.http.client.entity.*;

public class JSONPost extends BaseDownload
{
	long groupId;
	String deviceId;
	String auth_token;
	
	public JSONPost(Context context, long groupId) {
		this.context = context;
		this.groupId=groupId;
		deviceId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
		prefs = PreferenceManager.getDefaultSharedPreferences(context);
		auth_token = prefs.getString("auth_token", "");
	}

	@Override
	public InputStream getInputStrin()
	{
		InputStream is = null;
		try {
			
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(this.url + url);

			List<NameValuePair> nameValues = new ArrayList<NameValuePair>(3);
			nameValues.add(new BasicNameValuePair("group_id",String.valueOf(groupId)));
			nameValues.add(new BasicNameValuePair("auth_token",auth_token));
			nameValues.add(new BasicNameValuePair("device_id",deviceId));
			httpPost.setEntity(new UrlEncodedFormEntity(nameValues));

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
