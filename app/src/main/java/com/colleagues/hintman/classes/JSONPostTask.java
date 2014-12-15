package com.colleagues.hintman.classes;

import android.os.*;
import org.json.*;
import android.content.*;

public class JSONPostTask extends AsyncTask<String, Void, JSONObject>
{

	public Context context;
	long groupId;
	public JSONPostTask(Context context, long groupId){
		this.groupId = groupId;
		this.context = context;
	}
	
	@Override
	protected JSONObject doInBackground(String[] p1)
	{
		// TODO: Implement this method
		return new JSONPost(context, groupId).getJSONFromUrl(p1[0]);
	}

}
