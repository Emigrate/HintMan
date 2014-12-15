package com.colleagues.hintman;
import android.content.*;
import android.util.*;
import com.parse.*;
import android.support.v4.app.*;
import org.json.*;
import com.colleagues.hintman.classes.*;
import com.colleagues.hintman.objects.*;

public class PushReceiver extends ParsePushBroadcastReceiver
{

	@Override
	public void onReceive(Context context, Intent intent)
	{
		// TODO: Implement this method
		super.onReceive(context, intent);
		String url = intent.getStringExtra(KEY_PUSH_DATA);
		Log.e("hint", "receive: " + url);
		try
		{
			JSONObject obj = new JSONObject(url);
			JSONParser parser = new JSONParser();
			Hint hint = parser.getPushHint(obj);
			PushNotification notif = new PushNotification(context);
			NotificationManagerCompat.from(context).notify(0, notif.getNotificstion(hint.id, hint.content,hint.grup));
			
		}
		catch (JSONException e)
		{
			Log.e("hint", e.getMessage());
		}
		
	}

	
	
	
	
 @Override
 public void onPushOpen(Context context, Intent intent) {
	 String url = intent.getStringExtra(KEY_PUSH_DATA);
	 Log.e("hint", "pushed: " + url);
	 
 Intent i = new Intent(context, HintActivity.class);
 i.putExtras(intent.getExtras());
 i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
 context.startActivity(i);
 }
 
	
}
