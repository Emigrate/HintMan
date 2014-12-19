package com.colleagues.hintman.fragments;
import android.support.v4.app.*;
import android.os.*;
import android.support.v7.app.*;
import com.colleagues.hintman.activity.*;
import android.content.*;
import android.preference.*;
import android.support.v4.widget.*;
import android.view.*;
import com.colleagues.hintman.*;
import android.util.*;

public class BaseFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener
{
	SwipeRefreshLayout srl;
	ActionBarActivity activity;
	SharedPreferences prefs;
	SharedPreferences.Editor e;
	public long userId;
	public ActionBar ab;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		activity = (BaseActivity)getActivity();
		ab = activity.getSupportActionBar();
		ab.setDisplayHomeAsUpEnabled(true);
		ab.setHomeButtonEnabled(true);
		prefs = PreferenceManager.getDefaultSharedPreferences(activity);
	}
	
	public void setupHomeAsUp(boolean show){
		App.getInstance().asUs = show;
		if(!show){
			ab.setHomeAsUpIndicator(R.drawable.ic_navigation_drawer);
		}else{
			ab.setDisplayHomeAsUpEnabled(false);
			ab.setDisplayHomeAsUpEnabled(true);
		}
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		setRetainInstance(true);
	}
	
	public void initSrl(View v){
		srl =(SwipeRefreshLayout)v.findViewById(R.id.refresh);
		srl.setOnRefreshListener(this);
		srl.setProgressBackgroundColor(R.color.primary);
		srl.setColorScheme(R.color.white);
	}
	
	public boolean isRefreshing(){
		return srl.isRefreshing();
	}
	
	public void setProgress(final boolean show){
		srl.post(new Runnable(){

				@Override
				public void run()
				{
					srl.setRefreshing(show);
				}
			});
		
	}
	
	@Override
	public void onRefresh(){}
	
	
}
