package com.colleagues.hintman.classes.jsons;

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

public class JsonPostVote extends BaseDownload
{
	
	long userId;
	String value;
	String auth_token;

	public JsonPostVote(Context context, long groupId, String value) {
		this.value = value;
		this.context = context;
		this.userId=groupId;
		prefs = PreferenceManager.getDefaultSharedPreferences(context);
		auth_token = prefs.getString("auth_token", "");
	}
	
	@Override
	public InputStream getInputStrin()
	{
		InputStream is = null;
		try {
			
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(url);

			List<NameValuePair> nameValues = new ArrayList<NameValuePair>(3);
			nameValues.add(new BasicNameValuePair("user_id",String.valueOf(userId)));
			nameValues.add(new BasicNameValuePair("auth_token",auth_token));
			nameValues.add(new BasicNameValuePair("value",value));
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
