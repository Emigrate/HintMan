package com.colleagues.hintman.fragments;
import android.content.*;
import android.graphics.*;
import android.os.*;
import android.support.v7.widget.*;
import android.text.*;
import android.util.*;
import android.view.*;
import android.view.animation.*;
import android.widget.*;
import com.colleagues.hintman.*;
import com.colleagues.hintman.classes.*;
import com.colleagues.hintman.objects.*;
import com.colleagues.hintman.view.*;
import java.util.*;
import org.json.*;

public class MainListFragment extends BaseFragment
{
	LinearLayout inflateListLayout;
	ReaderListView listView;
	RecyclerViewAdapter adapter;
	MyJsonTack task;
	StaggeredGridLayoutManager mLayoutManager;
	long groupId;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View v = inflater.inflate(R.layout.main, container, false);
		inflateListLayout = (LinearLayout) v.findViewById(R.id.mainLinearLayout);
		
		listView = new ReaderListView(getActivity());
        listView.setHasFixedSize(true);
		listView.setClickable(true);
       	mLayoutManager = new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL);
       	listView.setLayoutManager(mLayoutManager);
		listView.setItemAnimator(new DefaultItemAnimator());
        listView.setOnScrollListener(onScroll);
		mLayoutManager.setSpanCount(1);
		//Это новый метод для задания divider
        //listView.addItemDecoration(new DividerItemDecoration((BaseActivity)getActivity()));

        inflateListLayout.addView(listView);
		
		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		// TODO: Implement this method
		super.onActivityCreated(savedInstanceState);
		groupId = getArguments().getLong("group_id");
		getJson(groupId);
	}
	
	private void getJson(long id){
		
		task = new MyJsonTack(activity);
		task.execute("api/v1/users.json");
	}

	@Override
	public void onDestroy()
	{
		super.onDestroy();
		if(task != null){
			task.cancel(true);
		}
		
	}


	
	
	
	
	public class MyJsonTack extends JSONPostTask
	{
	
		
		public MyJsonTack(Context context){
			super(context, groupId);
			
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
			if(result != null){
			JSONParser parser = new JSONParser();
			ArrayList<Hint>list = parser.getHintsList(result);
			adapter = new RecyclerViewAdapter(context, list);
			listView.setAdapter(adapter);
			
				try
				{
					e = prefs.edit();
					e.putString("auth_token", result.getString("auth_token"));
					e.putLong("_user_id", result.getLong("id"));
					e.commit();
				}
				catch (JSONException e)
				{}
			}
		}
		
		
	}
	
	private RecyclerView.OnScrollListener onScroll = new RecyclerView.OnScrollListener() {

		@Override
		public void onScrollStateChanged(RecyclerView recyclerView, int newState)
		{
			super.onScrollStateChanged(recyclerView, newState);
			//updateState(newState);
		}
		};
}
