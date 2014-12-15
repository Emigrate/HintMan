package com.colleagues.hintman.classes.jsons;

import android.content.*;
import android.util.*;
import java.io.*;
import org.apache.http.*;
import org.apache.http.client.*;
import org.apache.http.client.methods.*;
import org.apache.http.impl.client.*;
import org.json.*;

public abstract class BaseDownload
{
	public String url = "http://hintman.herokuapp.com/";
	
	SharedPreferences prefs;
	SharedPreferences.Editor e;
	Context context;
	
	private InputStream is = null;
	private JSONObject jObj = null;
	private String json = "";
	
	
	
	public abstract InputStream getInputStrin();


	public JSONObject getJSONFromUrl(String url) {

		this.url += url;
		Log.e("hint","url: " + this.url);
		is = getInputStrin();
		
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();
			json = sb.toString();
			
		} catch (Exception e) {
			Log.e("hint", "Error converting result " + e.toString());
		}

		try {
			jObj = new JSONObject(json);
		} catch (JSONException e) {
			Log.e("hint", "Error parsing data " + e.toString());
		}

		return jObj;

	}
}
