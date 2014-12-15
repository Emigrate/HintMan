package com.colleagues.hintman.classes;

import android.os.*;
import org.json.*;

public class JSONTask extends AsyncTask<String, Void, JSONObject>
{
	@Override
	protected JSONObject doInBackground(String[] p1)
	{
		return new JSONDownload().getJSONFromUrl(p1[0]);
	}
}
