package com.colleagues.hintman;
import android.app.*;
import android.content.*;
import android.graphics.*;
import android.support.v4.app.*;
import com.colleagues.hintman.*;
import android.preference.*;

public class PushNotification
{
	
	SharedPreferences prefs;
	Context context;
	public PushNotification(Context context){
		this.context=context;
		
		prefs = PreferenceManager.getDefaultSharedPreferences(context);
	}
	
	
	public Notification getNotificstion(long id, String content, String group){
		NotificationCompat.Builder nBuilder = new NotificationCompat. Builder(context);
		nBuilder.setSmallIcon(R.drawable.ic_launcher);
		nBuilder.setAutoCancel(true);
		nBuilder.setLargeIcon(BitmapFactory . decodeResource(context.getResources(), R.drawable.ic_launcher));
		
		if(prefs.getBoolean("volume_tag", true))
			nBuilder.setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE | Notification.DEFAULT_LIGHTS);
		else
			nBuilder.setDefaults(Notification.DEFAULT_VIBRATE | Notification.DEFAULT_LIGHTS);

			NotificationCompat.BigTextStyle style = new NotificationCompat. BigTextStyle();
			
			style.bigText(content);
			style.setBigContentTitle("Актуальный совет");
			style.setSummaryText(group);
			/*NotificationCompat.BigPictureStyle style = new NotificationCompat.BigPictureStyle();

			 style.bigPicture(bitmap);
			 style.setBigContentTitle(item.title);
			 style.setSummaryText(item.content);
			 */
			Intent viewIntent = new Intent(context, HintActivity.class);
			viewIntent.putExtra("_id" , id);
			PendingIntent viewPendingIntent = PendingIntent.getActivity(context, 0 , viewIntent , PendingIntent.FLAG_UPDATE_CURRENT);

			NotificationCompat. WearableExtender wearableExtender =new NotificationCompat. WearableExtender()
				.setHintHideIcon(true)
				.setBackground(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher));

			/*nBuilder.setContentTitle(item.title);
			nBuilder.setContentText(item.content);
			nBuilder.addAction(R.drawable.ic_share, "Поделиться", pIntent);
		 */
		 	nBuilder.setContentIntent(viewPendingIntent);
			nBuilder.extend(wearableExtender);
			nBuilder.setStyle(style);
			
		return nBuilder.build();
	}
}
