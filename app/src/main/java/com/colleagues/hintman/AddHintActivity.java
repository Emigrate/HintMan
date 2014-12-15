package com.colleagues.hintman;
import com.colleagues.hintman.activity.*;
import android.os.*;
import com.colleagues.hintman.fragments.*;

public class AddHintActivity extends BaseActivity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO: Implement this method
		super.onCreate(savedInstanceState);
		setContentView(R.layout.container);
		
		if(savedInstanceState==null){
			getSupportFragmentManager().beginTransaction()
			.replace(R.id.containerFrameLayout, new AddHintFragment())
			.commit();
		}
		
	}
	
}
