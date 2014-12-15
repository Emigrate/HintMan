package com.colleagues.hintman.fragments;
import android.view.*;
import android.os.*;
import com.colleagues.hintman.*;
import android.widget.*;
import com.colleagues.hintman.classes.*;
import org.json.*;
import com.colleagues.hintman.objects.*;
import android.util.*;
import android.view.View.*;
import android.content.*;
import android.app.*;

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
		hintTask = new HintTask();
		hintTask.execute(url);
		Log.e("hint", "url: " + url);
	}
	
	public class HintTask extends JSONTask
	{

		@Override
		protected void onPreExecute()
		{
			super.onPreExecute();
			Log.e("hint", "start");
		}

		@Override
		protected void onPostExecute(JSONObject result)
		{
			super.onPostExecute(result);
			Log.e("hint","res: " + result);
			JSONParser parser = new JSONParser();
			setUiData(parser.getHint(result));
			Log.e("hint","stop");
		}
		
		
	}
	
	
	public void setVote(String vote){
		new VoteTask(activity, userId, vote).execute("api/v1/hints/" + id+ "/vote.json");
	}
	
	public class VoteTask extends JSONVoteTask{
		
		ProgressDialog progress;
		
		public VoteTask(Context context, long userId, String value){
			super(context, userId, value);
			progress = new ProgressDialog(context);
			progress.setMessage("Подождите...");
			progress.setCancelable(false);
		}

		@Override
		protected void onPreExecute()
		{
			// TODO: Implement this method
			super.onPreExecute();
			progress.show();
		}

		
		
		@Override
		protected void onPostExecute(JSONObject result)
		{
			// TODO: Implement this method
			super.onPostExecute(result);
			JSONParser parser = new JSONParser();
			setUiData(parser.getHint(result));
			progress.dismiss();
		}
		
		
	}
	
	private void setUiData(Hint hint){
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
			}
		}
	}}
	
}
