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
import com.melnykov.fab.FloatingActionButton;
import com.melnykov.fab.ObservableScrollView;

public class HintAddFragment extends BaseFragment
{

	ObservableScrollView scrollVew;
	EditText editContent;
	Button buttonSend;
	long groupId;
	
	
	public static HintAddFragment getHintAddFragment(long id){
		HintAddFragment fragment = new HintAddFragment();
		Bundle args = new Bundle();
		args.putLong("_group_id", id);
		fragment.setArguments(args);
		return fragment;
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		super.onCreateView(inflater, container, savedInstanceState);
		View v = inflater.inflate(R.layout.add_hint, container, false);
		editContent = (EditText)v.findViewById(R.id.add_hintEditText);
		scrollVew =(ObservableScrollView)v.findViewById(R.id.add_hintScrollView);
		FloatingActionButton fab = (FloatingActionButton) v.findViewById(R.id.fab);
		fab.attachToScrollView(scrollVew);
		fab.setType(fab.TYPE_MINI);
		fab.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					String content = editContent.getText().toString();
					if(content.length() >0){
						new AddHintTask(activity, groupId, content).execute("api/v1/hints.json");
					}else
						Toast.makeText(activity, "Пожалуйста введите совет",1000).show();
				}
			});return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		groupId = getArguments().getLong("_group_id", 0);
		
	}

	@Override
	public void onResume()
	{
		super.onResume();
		setupActionBar(true);
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
				activity.onBackPressed();
				
			}
		}
		
		
	}
	
	
}
