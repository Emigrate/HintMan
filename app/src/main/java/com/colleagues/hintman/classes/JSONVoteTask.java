package com.colleagues.hintman.classes;

import android.os.*;
import org.json.*;
import android.content.*;

public class JSONVoteTask extends AsyncTask<String, Void, JSONObject>
{

	public Context context;
	long userId;
	String value;
	public JSONVoteTask(Context context, long userId, String value){
		this.userId = userId;
		this.context = context;
		this.value = value;
	}
	
	@Override
	protected JSONObject doInBackground(String[] p1)
	{
		// TODO: Implement this method
		return new JSONEVote(context, userId,value).getJSONFromUrl(p1[0]);
	}

}
