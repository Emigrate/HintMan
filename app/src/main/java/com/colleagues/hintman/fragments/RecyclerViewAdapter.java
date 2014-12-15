package com.colleagues.hintman.fragments;

import android.content.*;
import android.graphics.*;
import android.graphics.drawable.*;
import android.support.v7.graphics.*;
import android.view.*;
import android.widget.*;
import java.util.*;
import android.support.v7.widget.*;
import android.util.*;
import com.colleagues.hintman.*;
import com.colleagues.hintman.objects.*;
import android.view.View.*;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> 
{
	
	private Context context;
	private Typeface face;
	int background;

	private ArrayList<Hint> parser;
	public LayoutInflater inflater;
	
    public RecyclerViewAdapter(Context context, ArrayList<Hint> parser)
	{

		this.parser = parser;
		this.context = context;
		//face = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Regular.ttf");
		inflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getItemCount()
	{
		return parser.size();
	}

	public void changeData()
	{

		notifyDataSetChanged();
	}

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
	{
		final View v = inflater.inflate(R.layout.hint_item, null);
		return new ViewHolder(v);
    }

	public class ViewHolder extends RecyclerView.ViewHolder implements OnClickListener
	{

       // private final TextView itemTitle;		
		private final TextView itemHint;	
		private final TextView itemDate;	
		private final LinearLayout linear;
        public ViewHolder(View view)
		{
            super(view);
			linear = (LinearLayout)view.findViewById(R.id.hint_itemLinearLayout);
			//itemTitle = (TextView)view.findViewById(R.id.hint_itemTitleView);
			itemHint = (TextView)view.findViewById(R.id.hint_itemHintView);
			itemDate = (TextView)view.findViewById(R.id.hint_itemDateView);
			linear.setOnClickListener(this);
	    }
		@Override
		public void onClick(View p1)
		{
			long id = parser.get(getPosition()).id;
			Intent intent = new Intent(context, HintActivity.class);
			intent.putExtra("_id", id);
			context.startActivity(intent);
		}
		
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position)
	{
		//Log.e("itc","position: " + position);
		final Hint item = parser.get(position);
		
		
		
		if (item.content != null)
		{
			holder.itemHint.setText(item.content);
		}
		if(item.date != null){
			holder.itemDate.setText(item.date);
		}
	}	
	
}
