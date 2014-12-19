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
import com.melnykov.fab.FloatingActionButton;
import com.melnykov.fab.FloatingRelativeLayout;
import com.melnykov.fab.ObservableScrollView;
import android.support.v7.widget.*;

public class HintFragment extends BaseFragment
{
	FloatingActionButton buttonMinus;
	FloatingActionButton buttonPlus;
	ObservableScrollView sctollView;
	TextView textHint;
	TextView textDate;
	TextView textGroup;
	TextView voteView;
	CardView cardView;
	FloatingRelativeLayout relativeVote;
	
	HintTask hintTask;
	long id;
	long userId;
	String content;
	String auth;

	public static HintFragment getHintFragment(long id, String content){
		HintFragment fragment = new HintFragment();
		Bundle args = new Bundle();
		args.putLong("_id", id);
		args.putString("_content", content);
		fragment.setArguments(args);
		return fragment;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		id = getArguments().getLong("_id", 0);
		content = getArguments().getString("_content");
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
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		super.onCreateView(inflater, container, savedInstanceState);
		View v = inflater.inflate(R.layout.hint_fragment, container, false);
		initSrl(v);
		sctollView =(ObservableScrollView)v.findViewById(R.id.hint_hintScrollView);
		cardView =(CardView)v.findViewById(R.id.cardView);
		voteView =(TextView)v.findViewById(R.id.hint_fragmentVoteView);
		textGroup =(TextView)v.findViewById(R.id.hint_fragmentGroupView);
		textHint =(TextView)v.findViewById(R.id.hint_fragmentHintView);
		textDate =(TextView)v.findViewById(R.id.hint_fragmentDataView);
		buttonMinus = (FloatingActionButton) v.findViewById(R.id.hint_buttonMinus);
		buttonMinus.setType(buttonMinus.TYPE_MINI);
		buttonMinus.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					setVote("down");
				}
			});
		buttonPlus = (FloatingActionButton) v.findViewById(R.id.hint_buttonPlus);
		buttonPlus.setType(buttonPlus.TYPE_MINI);
		buttonPlus.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					setVote("up");
				}
			});
			
		relativeVote = (FloatingRelativeLayout)v.findViewById(R.id.hint_fragmentRelativeLayout);
		relativeVote.hide();
		return v;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState)
	{
		super.onViewCreated(view, savedInstanceState);
		if(content!= null){
		cardView.setVisibility(View.VISIBLE);
		textHint.setText(content);
		}
		getHint();
	}

	@Override
	public void onResume()
	{
		super.onResume();
		setupHomeAsUp(true);
	}

	
	

	@Override
	public void onRefresh()
	{
		super.onRefresh();
		getHint();
	}
	
	private void getHint(){
		String url = "api/v1/hints/" + id +".json?auth_token=" + auth + "&"+"user_id=" +userId;
		hintTask = new HintTask(activity);
		hintTask.execute(url);
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
			setProgress(true);
		}

		@Override
		protected void onPostExecute(JSONObject result)
		{
			super.onPostExecute(result);
			setProgress(false);
			if(result != null){
				relativeVote.attachToScrollView(sctollView);
				setUiData(result);
			}
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
		relativeVote.show(true);
		textHint.setText(hint.content);
		textGroup.setText(hint.grup);
		textDate.setText(hint.date);
		cardView.setVisibility(View.VISIBLE);
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
				relativeVote.hide(true);
				voteView.setVisibility(View.VISIBLE);
			}else{
				relativeVote.show(true);
				voteView.setVisibility(View.GONE);
			}
		}
	}}
	
}
