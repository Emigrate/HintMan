package com.colleagues.hintman;

import android.content.*;
import android.os.*;
import android.preference.*;
import android.support.v4.app.*;
import android.support.v4.widget.*;
import android.support.v7.app.*;
import android.support.v7.widget.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import com.colleagues.hintman.activity.*;
import com.colleagues.hintman.classes.*;
import com.colleagues.hintman.classes.jsons.*;
import com.colleagues.hintman.classes.tasks.*;
import com.colleagues.hintman.fragments.*;
import com.colleagues.hintman.objects.*;
import com.colleagues.hintman.view.*;
import java.net.*;
import java.util.*;
import org.json.*;

import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.content.res.*;
import android.util.*;

public class MainActivity extends BaseActivity implements AdapterView.OnItemClickListener
{
	private FrameLayout frameDrawer;
	private DrawerLayout mDrawerLayout ;
	private ListView mDrawerList ;
	private Toolbar mToolbar;
	
	ImageView close;
	long groupId;
	String title;
	private ReadSearchTask readSearchTask;
    public SearchQueriAdapter myAdapter;
    public AutoCompleteTextView myAutoComplete;
	public ArrayList<Group> items;
	SharedPreferences prefs;
	
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_container);
		mToolbar = (Toolbar) findViewById(R.id.toolbar);
		mDrawerLayout = (DrawerLayout) findViewById(R .id. drawer_layout);
		mDrawerList = (ListView) findViewById(R .id. list_drawer);
		frameDrawer = (FrameLayout)findViewById(R.id.drawerFrameLayout);
		close = (ImageView)findViewById(R.id.main_containerImageView);
		myAutoComplete = (AutoCompleteTextView)findViewById(R.id.myautocomplete);
		
		if (mToolbar != null) {
            setSupportActionBar(mToolbar);
		}
		
		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		groupId = prefs.getLong("_group_id", 0);
		title = prefs.getString("_group_title", "Deweloper");
		items = new ArrayList<Group>();

		myAutoComplete.addTextChangedListener(new CustomAutoCompleteTextChangedListener(this));

		myAdapter = new SearchQueriAdapter(this, R.layout.row, items);

		myAutoComplete.setAdapter(myAdapter);

		myAutoComplete.setOnItemClickListener(this);

		close.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					myAutoComplete.setText("");
					saveGroup(0, "");
				}
			});
	
		setTitle(title);
		setTitleColor(R.color.white);
		
		initView(groupId, title);
    }
	

	private void initView(long groupId, String title)
	{
		if (groupId == 0)
			return;
		Fragment fragment = new HintListFragment();
		Bundle args = new Bundle();
		args.putLong("group_id", groupId);
		args.putString("group_title", title);
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
		initView(item.id, item.title);
		items.clear();
	}

	void saveGroup(long id, String title)
	{
		SharedPreferences.Editor e = prefs.edit();
		e.putLong("_group_id", id);
		e.putString("_group_title", title);
		e.commit();
		this.groupId = id;
		this.title = title;
		invalidateOptionsMenu();
	}


    public void getItemsFromDb(String searchTerm)
	{
		if (searchTerm.length() == 0)
		{
			close.setVisibility(View.GONE);
			return;
		}
		saveGroup(groupId, searchTerm);
		readSearchTask = new ReadSearchTask(this);
		readSearchTask.execute("api/v1/groups.json?limit=10&q=" + URLEncoder.encode(searchTerm));
    }


	public void showClearButton()
	{
		close.setVisibility(View.VISIBLE);
	}


	public class ReadSearchTask extends BaseTask
	{
		public ReadSearchTask(Context context)
		{
			super(context, new JsonGet());
		}
		@Override
		protected void onPostExecute(JSONObject result)
		{

			super.onPostExecute(result);
			if (result != null)
			{
				JSONParser parser = new JSONParser();
				items = parser.getGroupList(result);


				myAdapter.notifyDataSetChanged();
				myAdapter = new SearchQueriAdapter(MainActivity.this, R.layout.row, items);
				myAutoComplete.setAdapter(myAdapter);
			}
		}

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		super.onOptionsItemSelected(item);
		if(!App.getInstance().asUs){
			mDrawerLayout.openDrawer(frameDrawer);
			return true;
		}
		
		switch (item.getItemId())
		{
			case android.R.id.home:
				onBackPressed();
			break;
		}

		return true;
	}

	
}
