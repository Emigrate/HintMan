package com.colleagues.hintman.classes.tasks;

import android.os.*;
import org.json.*;
import com.colleagues.hintman.classes.jsons.*;
import android.widget.*;
import android.content.*;
import android.util.*;

public class BaseTask extends AsyncTask<String, Void, JSONObject>
{
	private BaseDownload jsonDownload;
	public Context context;
	int cycle = 0;
	int maxCycle = 10;
	public BaseTask(Context context,BaseDownload jsonDownload){
		this.jsonDownload = jsonDownload;
		this.context = context;
	}

	@Override
	protected JSONObject doInBackground(String[] urls)
	{
		JSONObject jsonObject = null;
		
		//while(jsonObject == null){
		jsonObject = jsonDownload.getJSONFromUrl(urls[0]);
		
		//}
		return jsonObject;
	}

	@Override
	protected void onPostExecute(JSONObject result)
	{

		super.onPostExecute(result);
		Log.e("hint", "result " + result);
		if(result == null){
			Toast.makeText(context, "Произошла ошибка", 1000).show();
		}
	}

	
}
