package com.colleagues.hintman;
import com.colleagues.hintman.activity.*;
import android.os.*;
import com.colleagues.hintman.fragments.*;
import android.support.v4.app.*;

public class AddHintActivity extends BaseActivity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO: Implement this method
		super.onCreate(savedInstanceState);
		setContentView(R.layout.container);
		long id = getIntent().getLongExtra("_group_id", 0);
		Fragment fragment = HintAddFragment.getHintAddFragment(id);
		if(savedInstanceState==null){
			getSupportFragmentManager().beginTransaction()
			.replace(R.id.containerFrameLayout, fragment)
			.commit();
		}
		
	}
	
}
