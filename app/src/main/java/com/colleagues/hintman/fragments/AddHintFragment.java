package com.colleagues.hintman.fragments;
import android.app.*;
import android.content.*;
import android.os.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import com.colleagues.hintman.*;
import com.colleagues.hintman.classes.tasks.*;
import org.json.*;
import com.colleagues.hintman.classes.jsons.*;

public class AddHintFragment extends BaseFragment
{

	EditText editContent;
	Button buttonSend;
	long groupId;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		// TODO: Implement this method
		super.onCreateView(inflater, container, savedInstanceState);
		View v = inflater.inflate(R.layout.add_hint, container, false);
		editContent = (EditText)v.findViewById(R.id.add_hintEditText);
		buttonSend = (Button)v.findViewById(R.id.add_hintButton);
		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		groupId = activity.getIntent().getLongExtra("_group_id", 0);
		buttonSend.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					String content = editContent.getText().toString();
					if(content.length() >0){
						new AddHintTask(activity, groupId, content).execute("api/v1/hints.json");
					}else
						Toast.makeText(activity, "Пожалуйста введите совет",1000).show();
					
				}
			});
	}
	
	public class AddHintTask extends BaseTask{
		ProgressDialog dialog;
		
		public AddHintTask(Context context, long groupId, String content){
			super(context, new JsonPostHint(context, groupId, content));
			dialog = new ProgressDialog(activity);
			dialog.setMessage("Подождите...");
			dialog.setCancelable(false);
		}

		@Override
		protected void onPreExecute()
		{
			super.onPreExecute();
			dialog.show();
		}

		@Override
		protected void onPostExecute(JSONObject result)
		{
			super.onPostExecute(result);
			dialog.dismiss();
			if(result != null){
				activity.finish();
				
			}
		}
		
		
	}
	
	
}
