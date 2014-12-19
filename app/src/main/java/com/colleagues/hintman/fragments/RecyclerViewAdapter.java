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
import android.support.v4.app.*;
import android.os.*;
import android.support.v7.app.*;
import com.colleagues.hintman.view.*;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> 
{
	private ReaderListView mRecyclerView;
	private ActionBarActivity context;
	private Typeface face;
	int background;
	private OnClickEvent mOnClickEvent;
    private ArrayList<Hint> parser;
	public LayoutInflater inflater;
	
	private class VIEW_TYPES {
        public static final int NORMAL = 1;
        public static final int HEADER = 2;
        public static final int FIRST_VIEW = 3;
    }
	
    public RecyclerViewAdapter(ActionBarActivity context, ArrayList<Hint> list, ReaderListView recyclerView)
	{
		this.context = context;
		this.mRecyclerView = recyclerView;
		face = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Regular.ttf");
		inflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
		this.parser = list;
	}

	@Override
	public int getItemCount()
	{
		return parser.size();
	}

	public void changeData(ArrayList<Hint> list)
	{
		parser.clear();
		notifyDataSetChanged();
		for(int i =0 ; i< list.size();i++){
			Hint hint = list.get(i);
			parser.add(hint);
			notifyItemInserted(i);
		}
		
	}

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
	{
		final View v = inflater.inflate(R.layout.hint_item, null);
		
		return new ViewHolder(v);
    }

	public class ViewHolder extends RecyclerView.ViewHolder
	{

		private final TextView itemHint;	
		private final TextView itemDate;	
		private final LinearLayout linear;
        public ViewHolder(View view)
		{
            super(view);
			linear = (LinearLayout)view.findViewById(R.id.hint_itemLinearLayout);
			itemHint = (TextView)view.findViewById(R.id.hint_itemHintView);
			itemDate = (TextView)view.findViewById(R.id.hint_itemDateView);
	    }
		
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position)
	{
		final Hint item = parser.get(position);
		
		if (item.content != null)
		{
			holder.itemHint.setText(item.content);
			holder.itemHint.setTypeface(face);
		}
		if(item.date != null){
			holder.itemDate.setText(item.date);
		}
		
		if (mOnClickEvent != null)
            holder.itemView.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						mOnClickEvent.onClick(v, position);
					}
				});
	}	
	
	public void setOnClickEvent(OnClickEvent onClickEvent) {
        mOnClickEvent = onClickEvent;
    }

    
	public interface OnClickEvent {
        
        void onClick(View v, int position);
    }

    
}
