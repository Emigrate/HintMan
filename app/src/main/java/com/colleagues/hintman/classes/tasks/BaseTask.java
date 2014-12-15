package com.colleagues.hintman.classes.tasks;

import android.os.*;
import org.json.*;
import com.colleagues.hintman.classes.jsons.*;
import android.widget.*;
import android.content.*;

public class BaseTask extends AsyncTask<String, Void, JSONObject>
{
	private BaseDownload jsonDownload;
	public Context context;
	public BaseTask(Context context,BaseDownload jsonDownload){
		this.jsonDownload = jsonDownload;
		this.context = context;
	}

	@Override
	protected JSONObject doInBackground(String[] urls)
	{
		return jsonDownload.getJSONFromUrl(urls[0]);
	}

	@Override
	protected void onPostExecute(JSONObject result)
	{
		// TODO: Implement this method
		super.onPostExecute(result);
		if(result == null){
			Toast.makeText(context, "Произошла ошибка", 1000).show();
		}
	}

	
}
