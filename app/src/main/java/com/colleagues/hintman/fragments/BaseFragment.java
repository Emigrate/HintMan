package com.colleagues.hintman.fragments;
import android.support.v4.app.*;
import android.os.*;
import android.support.v7.app.*;
import com.colleagues.hintman.activity.*;
import android.content.*;
import android.preference.*;

public class BaseFragment extends Fragment
{

	ActionBarActivity activity;
	SharedPreferences prefs;
	SharedPreferences.Editor e;
	public long userId;
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		// TODO: Implement this method
		super.onActivityCreated(savedInstanceState);
		activity = (BaseActivity)getActivity();
		prefs = PreferenceManager.getDefaultSharedPreferences(activity);
	}
	
}
