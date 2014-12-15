package com.colleagues.hintman.classes;

import android.os.*;
import org.json.*;
import android.content.*;

public class JSONAddHintTask extends AsyncTask<String, Void, JSONObject>
{

	public Context context;
	long groupId;
	String content;
	public JSONAddHintTask(Context context, long groupId, String content){
		this.groupId = groupId;
		this.context = context;
		this.content = content;
	}

	@Override
	protected JSONObject doInBackground(String[] p1)
	{
		// TODO: Implement this method
		return new JSONPostHint(context, groupId, content).getJSONFromUrl(p1[0]);
	}

}
