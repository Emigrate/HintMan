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
	protected void onPushReceive(Context context, Intent intent)
	{
		super.onPushReceive(context, intent);
		String url = intent.getStringExtra(KEY_PUSH_DATA);
		Log.e("hint", "pushedResive: " + url);
		Log.e("hint", "extras: " + intent.getExtras().toString());
		Log.e("hint", "action: " + intent.getAction());
		Log.e("hint", "receive: " + url);
		
		
	}

	@Override
	protected void onPushDismiss(Context context, Intent intent)
	{
		super.onPushDismiss(context, intent);
		String url = intent.getStringExtra(KEY_PUSH_DATA);
		Log.e("hint", "pushedDismis: " + url);
		
	}

	@Override
	public void onPushOpen(Context context, Intent intent)
	{
		
		Log.e("hint", "pushedOpen: ");
			Intent i = new Intent(context, HintActivity.class);
			i.putExtras(intent.getExtras());
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(i);
			//PushNotification notif = new PushNotification(context);
			//NotificationManagerCompat.from(context).notify(0, notif.getNotificstion(hint.id, hint.content, hint.grup));

		
	}


}
