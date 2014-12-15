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
import java.net.*;

public class JSONPostHint extends BaseDownload
{
	long groupId;
	long userId;
	String deviceId;
	String auth_token;
	String content;

	public JSONPostHint(Context context, long groupId, String content) {
		this.context = context;
		this.groupId=groupId;
		this.content = content;
		prefs = PreferenceManager.getDefaultSharedPreferences(context);
		auth_token = prefs.getString("auth_token", "");
		userId = prefs.getLong("_user_id", 0);
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
			nameValues.add(new BasicNameValuePair("user_id",String.valueOf(userId)));
			nameValues.add(new BasicNameValuePair("content", content));
			httpPost.setEntity(new UrlEncodedFormEntity(nameValues, "UTF-8"));

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
