package com.colleagues.hintman;
import com.colleagues.hintman.activity.*;
import android.os.*;
import com.colleagues.hintman.fragments.*;
import android.view.*;
import android.content.*;

public class HintActivity extends BaseActivity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.container);
		
		if(savedInstanceState == null){
			long id = getIntent().getLongExtra("hint_id", 0);
			String content = getIntent().getStringExtra("alert");
			HintFragment fragment = HintFragment.getHintFragment(id, content);
			getSupportFragmentManager().beginTransaction()
			.replace(R.id.containerFrameLayout, fragment)
			.commit();
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		super.onOptionsItemSelected(item);
		switch(item.getItemId()){
			case android.R.id.home:
				Intent i = new Intent(this, MainActivity.class);
				i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(i);
				this.finish();
				break;
		}
		return true;
	}
	
	
	
}
