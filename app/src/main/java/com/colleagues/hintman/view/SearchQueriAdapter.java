package com.colleagues.hintman.view;
import android.widget.*;
import android.view.*;
import java.util.*;
import android.content.*;
import android.widget.AdapterView.*;
import com.colleagues.hintman.objects.*;
import com.colleagues.hintman.*;

public class SearchQueriAdapter extends ArrayAdapter<Group>
{
	ArrayList<Group> list;
	Context context;
	LayoutInflater inflater;
	MainActivity activity;
	int layoutView;
	public SearchQueriAdapter(Context context, int viewResourceId, ArrayList<Group> list) {
        super(context, viewResourceId, list);
        this.list = list;
		this.context = context;
		this.layoutView = viewResourceId;
		inflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
		activity = (MainActivity)context;
    }

	@Override
	public int getCount()
	{
		// TODO: Implement this method
		return list.size();
	}

	@Override
	public Group getItem(int p1)
	{
		// TODO: Implement this method
		return list.get(p1);
	}

	@Override
	public long getItemId(int p1)
	{
		// TODO: Implement this method
		return p1;
	}

	@Override
	public View getView(int p1, View p2, ViewGroup parent)
	{
		View view = inflater.inflate(layoutView, parent, false);
			
		final TextView textView= (TextView)view.findViewById(R.id.rowTextView);
		final Group item = getItem(p1);
		final String text = item.title;
		textView.setText(text);
		
		return view;
	}

}
