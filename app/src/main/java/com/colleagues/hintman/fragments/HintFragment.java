package com.colleagues.hintman.fragments;
import android.app.*;
import android.content.*;
import android.os.*;
import android.util.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import com.colleagues.hintman.*;
import com.colleagues.hintman.classes.*;
import com.colleagues.hintman.classes.tasks.*;
import com.colleagues.hintman.objects.*;
import org.json.*;
import com.colleagues.hintman.classes.jsons.*;

public class HintFragment extends BaseFragment
{
	TextView textHint;
	TextView textDate;
	TextView textGroup;
	RelativeLayout relativeVote;
	ImageView buttonMinus;
	ImageView buttonPlus;
	HintTask hintTask;
	long id;
	long userId;
	String auth;

	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		super.onCreateView(inflater, container, savedInstanceState);
		View v = inflater.inflate(R.layout.hint_fragment, container, false);
		textGroup =(TextView)v.findViewById(R.id.hint_fragmentGroupView);
		textHint =(TextView)v.findViewById(R.id.hint_fragmentHintView);
		textDate =(TextView)v.findViewById(R.id.hint_fragmentDataView);
		buttonMinus = (ImageView)v.findViewById(R.id.hint_buttonMinus);
		buttonPlus = (ImageView)v.findViewById(R.id.hint_buttonPlus);
		relativeVote = (RelativeLayout)v.findViewById(R.id.hint_fragmentRelativeLayout);
		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		id = activity.getIntent().getLongExtra("_id", 0);
		if(id == 0){
			String ext = activity.getIntent().getStringExtra("com.parse.Data");
			if(ext != null){
				Log.e("hint", "ext: " + ext);
				try
				{
					JSONObject obj = new JSONObject(ext);
					id = obj.getLong("_id");
				}
				catch (JSONException e)
				{}
			}
		}
		userId = prefs.getLong("_user_id", 0);
		auth = prefs.getString("auth_token", "");
		
		
		
		buttonMinus.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					setVote("down");
				}
			});
		buttonPlus.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					setVote("up");
				}
			});
		
		getHint();
	}
	
	private void getHint(){
		String url = "api/v1/hints/" + id +".json?auth_token=" + auth + "&"+"user_id=" +userId;
		hintTask = new HintTask(activity);
		hintTask.execute(url);
		Log.e("hint", "url: " + url);
	}
	
	public class HintTask extends BaseTask
	{

		public HintTask(Context context){
			super(context, new JsonGet());
		}
		
		@Override
		protected void onPreExecute()
		{
			super.onPreExecute();
			
		}

		@Override
		protected void onPostExecute(JSONObject result)
		{
			super.onPostExecute(result);
			if(result != null)
			setUiData(result);
			
		}
		
		
	}
	
	
	public void setVote(String vote){
		new VoteTask(activity, userId, vote).execute("api/v1/hints/" + id+ "/vote.json");
	}
	
	public class VoteTask extends BaseTask{
		
		ProgressDialog progress;
		
		public VoteTask(Context context, long userId, String value){
			super(context, new JsonPostVote(context, userId,value));
			progress = new ProgressDialog(context);
			progress.setMessage("Подождите...");
			progress.setCancelable(false);
		}

		@Override
		protected void onPreExecute()
		{
			super.onPreExecute();
			progress.show();
		}

		@Override
		protected void onPostExecute(JSONObject result)
		{
			super.onPostExecute(result);
			if(result != null)
			setUiData(result);
			progress.dismiss();
		}
		
		
	}
	
	private void setUiData(JSONObject jsonObject){
		JSONParser parser = new JSONParser();
		Hint hint = parser.getHint(jsonObject);
		if(hint.content != null && hint.content.length() > 0) {
		textHint.setText(hint.content);
		textGroup.setText(hint.grup);
		textDate.setText(hint.date);

		if(hint.voted){
			buttonMinus.setEnabled(false);
			buttonPlus.setEnabled(false);
			if(hint.voteValue.equals("up")){
				buttonPlus.setColorFilter(activity.getResources().getColor(R.color.green));
			}else if(hint.voteValue.equals("down")){
				buttonMinus.setColorFilter(activity.getResources().getColor(R.color.red));
			}
		}else{
			if(hint.expired){
				relativeVote.setVisibility(View.GONE);
			}else
				relativeVote.setVisibility(View.VISIBLE);
		}
	}}
	
}
