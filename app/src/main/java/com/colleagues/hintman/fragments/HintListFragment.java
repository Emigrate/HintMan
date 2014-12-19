package com.colleagues.hintman.fragments;
import android.content.*;
import android.os.*;
import android.support.v7.widget.*;
import android.view.*;
import android.widget.*;
import com.colleagues.hintman.*;
import com.colleagues.hintman.classes.*;
import com.colleagues.hintman.classes.tasks.*;
import com.colleagues.hintman.objects.*;
import com.colleagues.hintman.view.*;
import java.util.*;
import org.json.*;
import com.colleagues.hintman.classes.jsons.*;
import com.melnykov.fab.FloatingActionButton;
import android.view.View.*;
import android.support.v4.app.*;
import android.graphics.*;

public class HintListFragment extends BaseFragment implements RecyclerViewAdapter.OnClickEvent
{
	FrameLayout inflateListLayout;
	ReaderListView recyclerView;
	RecyclerViewAdapter adapter;
	TextView textEmpty;
	MyJsonTack task;
	ArrayList<Hint>list;
	StaggeredGridLayoutManager mLayoutManager;
	long groupId;
	String title;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View v = inflater.inflate(R.layout.hint_list, container, false);
		initSrl(v);
		textEmpty =(TextView)v.findViewById(R.id.hint_listTextEmpty);
		recyclerView = (ReaderListView)v.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
		recyclerView.setClickable(true);
       	mLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
       	
		recyclerView.setLayoutManager(mLayoutManager);
		recyclerView.setItemAnimator(new DefaultItemAnimator());
       	recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
		
		//listView.setOnScrollListener(onScroll);
		//mLayoutManager.setSpanCount(1);
		//Это новый метод для задания divider
        //listView.addItemDecoration(new Divi);

		FloatingActionButton fab = (FloatingActionButton) v.findViewById(R.id.fab);
		fab.attachToRecyclerView(recyclerView);
		fab.setType(fab.TYPE_MINI);
		fab.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					Fragment fragment = HintAddFragment.getHintAddFragment(groupId);
				
						activity.getSupportFragmentManager().beginTransaction()
							.replace(R.id.main_containerFrameLayout, fragment)
							.addToBackStack(null)
							.commit();
					
				}
			});
		
		return v;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState)
	{
		super.onViewCreated(view, savedInstanceState);
		
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		setupRecyclerAdapter();
		getJson(groupId);
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		groupId = getArguments().getLong("group_id");
		title = getArguments().getString("group_title");
		list = getCurentData();
		
	}

	@Override
	public void onResume()
	{
		super.onResume();
		setupHomeAsUp(false);
	}
	
	
	
	private void setupRecyclerAdapter(){
		adapter = new RecyclerViewAdapter(activity, list, recyclerView);
		adapter.setOnClickEvent(this);
		adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
				@Override
				public void onChanged() {
					super.onChanged();
					checkAdapterIsEmpty();
				}
			});
		recyclerView.setAdapter(adapter);
		
	}
	
	private void checkAdapterIsEmpty () {
        if (adapter.getItemCount() == 0) {
            textEmpty.setVisibility(View.VISIBLE);
        } else {
            textEmpty.setVisibility(View.GONE);
        }
    }
	
	@Override
	public void onClick(View v, int position)
	{
		Hint hint = list.get(position);
		Fragment fragment = HintFragment.getHintFragment(hint.id, hint.content);
		activity.getSupportFragmentManager().beginTransaction()
			.addToBackStack(null)
			.replace(R.id.main_containerFrameLayout, fragment)
			.commit();
	}

	@Override
	public void onRefresh()
	{
		super.onRefresh();
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
	
	public ArrayList<Hint> getCurentData()
	{
		Serializator<Hint> serializatorUser = new Serializator<Hint>(activity, title);
		list = serializatorUser.getSerialization();
		return list;
	}

	public void setCurentData(ArrayList<Hint> data)
	{
		Serializator<Hint> factory = new Serializator<Hint>(activity, title);
		factory.inSerialize(data);
	}
	
	public class MyJsonTack extends BaseTask
	{
		
		public MyJsonTack(Context context){
			super(context, new JsonPostHints(context, groupId));
			this.context = context;
			
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
			JSONParser parser = new JSONParser();
			list = parser.getHintsList(result);
			adapter.changeData(list);
			setCurentData(list);
			checkAdapterIsEmpty();
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
		/*int mLastFirstVisibleItem = 0;
		
		@Override
		public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
			super.onScrolled(recyclerView, dx, dy);
			final int currentFirstVisibleItem = mLayoutManager.findFirstVisibleItemPosition();

			if (currentFirstVisibleItem > this.mLastFirstVisibleItem) {
				activity.getSupportActionBar().hide();
			} else if (currentFirstVisibleItem < this.mLastFirstVisibleItem) {
				activity.getSupportActionBar().show();
			}

			this.mLastFirstVisibleItem = currentFirstVisibleItem;
		}*/
		
		@Override
		public void onScrollStateChanged(RecyclerView recyclerView, int newState)
		{
			super.onScrollStateChanged(recyclerView, newState);
			//updateState(newState);
		}
		};
}
