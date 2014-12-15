package com.colleagues.hintman;

import android.content.*;
import android.os.*;
import android.preference.*;
import android.support.v4.app.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import com.colleagues.hintman.activity.*;
import com.colleagues.hintman.classes.*;
import com.colleagues.hintman.classes.tasks.*;
import com.colleagues.hintman.fragments.*;
import com.colleagues.hintman.objects.*;
import com.colleagues.hintman.view.*;
import java.net.*;
import java.util.*;
import org.json.*;
import com.colleagues.hintman.classes.jsons.*;
import android.support.v7.app.*;
import android.widget.LinearLayout.*;

public class MainActivity extends BaseActivity implements AdapterView.OnItemClickListener
{
	public GridView myListView;
	ImageView close;
	long groupId;
	String title;
	private ReadSearchTask readSearchTask;
    public SearchQueriAdapter myAdapter;
    public CustomAutoCompleteView myAutoComplete;
	public ArrayList<Group> items;
	SharedPreferences prefs;
	ActionBar ab;
	
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_container);
		ab = getSupportActionBar();
		ab.setDefaultDisplayHomeAsUpEnabled(false);
		ab.setDisplayShowTitleEnabled(false);
		ab.setDisplayShowCustomEnabled(true);
		
		LayoutInflater inflster = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View bar = inflster.inflate(R.layout.action_bar, null);
		
		close =(ImageView)bar.findViewById(R.id.main_containerImageView);
		
		//getActionBar().setLogo(R.drawable.logo);
		myAutoComplete = (CustomAutoCompleteView)bar.findViewById(R.id.myautocomplete);
		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		groupId = prefs.getLong("_group_id", 0);
		title = prefs.getString("_group_title", "Deweloper");
		items = new ArrayList<Group>();
		try{
			myListView = (GridView) findViewById(R.id.mainGridView);
            myListView.setNumColumns(1);
			myAutoComplete.addTextChangedListener(new CustomAutoCompleteTextChangedListener(this));
            myAdapter = new SearchQueriAdapter(this, R.layout.row, items);
            myListView.setAdapter(myAdapter);
			myListView.setOnItemClickListener(this);

        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
		
		close.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					myAutoComplete.setText("");
					saveGroup(0, "");
					}
			});
		ActionBar.LayoutParams params = new ActionBar.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		ab.setCustomView(bar, params);
		myAutoComplete.setText(title);
		myListView.setVisibility(View.GONE);
		initView(groupId);
    }
	
	private void initView(long groupId){
		Fragment fragment = new HintListFragment();
		Bundle args = new Bundle();
		args.putLong("group_id", groupId);
		fragment.setArguments(args);
		
		getSupportFragmentManager().beginTransaction()
		.replace(R.id.main_containerFrameLayout, fragment)
		.commit();
		
	}
	
	@Override
	public void onItemClick(AdapterView<?> p1, View p2, int p3, long p4)
	{
		
			Group item = myAdapter.getItem(p3);
			saveGroup(item.id, item.title);
			myAutoComplete.setText(item.title);
			myListView.setVisibility(View.GONE);
			initView(item.id);
			
	}
	
	void saveGroup(long id, String title){
		SharedPreferences.Editor e = prefs.edit();
		e.putLong("_group_id", id);
		e.putString("_group_title", title);
		e.commit();
		this.groupId = id;
		this.title = title;
		invalidateOptionsMenu();
	}

    
    public void getItemsFromDb(String searchTerm){
		if(searchTerm.length() == 0){
			hideListView();
			close.setVisibility(View.GONE);
			
			return;
		}
		saveGroup(groupId, searchTerm);
		readSearchTask = new ReadSearchTask(this);
		readSearchTask.execute("api/v1/groups.json?limit=10&q=" + URLEncoder.encode(searchTerm));
    }
	
	public void hideListView(){
		myListView.setVisibility(View.GONE);
	}
	public void showListView(){
		close.setVisibility(View.VISIBLE);
		myListView.setVisibility(View.VISIBLE);
	}

	@Override
	protected void onResume()
	{
		// TODO: Implement this method
		super.onResume();
		
		
	}
	
	
	
	public class ReadSearchTask extends BaseTask
	{
		public ReadSearchTask(Context context){
			super(context, new JsonGet());
		}
		@Override
		protected void onPostExecute(JSONObject result)
		{
		
			super.onPostExecute(result);
			if(result != null){
			JSONParser parser = new JSONParser();
			items = parser.getGroupList(result);
			
			if(items.size() == 0)
				myListView.setVisibility(View.GONE);
			
			myAdapter.notifyDataSetChanged();
			myAdapter = new SearchQueriAdapter(MainActivity.this, R.layout.row, items);
			myListView.setAdapter(myAdapter);
			}
		}
		
	}

	/*@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		 super.onCreateOptionsMenu(menu);
		 menu.add(0, 2, 0, "Добавить совет");
		 return true;
	}*/

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		// TODO: Implement this method
		 super.onOptionsItemSelected(item);
		 switch(item.getItemId()){
			 case 2:
				 Intent i = new Intent(this, AddHintActivity.class);
				 i.putExtra("_group_id", groupId);
				 startActivity(i);
		 }
		 
		 return true;
	}
	

	
		
		
	
}
