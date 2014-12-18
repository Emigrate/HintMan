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
	private final float SCROLL_MULTIPLIER = 0.5f;
	
	private CustomRelativeWrapper mHeaderView;
	private ReaderListView mRecyclerView;
	private ActionBarActivity context;
	private Typeface face;
	int background;
	private OnClickEvent mOnClickEvent;
    private OnParallaxScroll mParallaxScroll;
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
		if (i == VIEW_TYPES.HEADER && mHeaderView != null)
            return new ViewHolder(mHeaderView);
        if (i == VIEW_TYPES.FIRST_VIEW && mHeaderView != null && mRecyclerView != null) {
            RecyclerView.ViewHolder holder = mRecyclerView.findViewHolderForPosition(0);
            if (holder != null)
                translateHeader(-holder.itemView.getTop());
        }
		
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
		/*@Override
		public void onClick(View p1)
		{
			long id = parser.get(getPosition()).id;
			Fragment fragment = HintFragment.getHintFragment(id);
			context.getSupportFragmentManager().beginTransaction()
				.addToBackStack(null)
				.replace(R.id.main_containerFrameLayout, fragment)
				.commit();
		}*/
		
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
						mOnClickEvent.onClick(v, position - (mHeaderView == null ? 0 : 1));
					}
				});
	}	
	
	public void setOnClickEvent(OnClickEvent onClickEvent) {
        mOnClickEvent = onClickEvent;
    }

    public void setOnParallaxScroll(OnParallaxScroll parallaxScroll) {
        mParallaxScroll = parallaxScroll;
        mParallaxScroll.onParallaxScroll(0, 0,mHeaderView );
    }
	
	public void translateHeader(float of) {
        float ofCalculated = of * SCROLL_MULTIPLIER;
		
        mHeaderView.setTranslationY(ofCalculated);
        mHeaderView.setClipY(Math.round(ofCalculated));
		
		if (mParallaxScroll != null) {
            float left = Math.min(1, ((ofCalculated) / (mHeaderView.getHeight() * SCROLL_MULTIPLIER)));
            mParallaxScroll.onParallaxScroll(left, of, mHeaderView);
        }
    }

    public void setParallaxHeader(View header, final RecyclerView view) {
        mHeaderView = new CustomRelativeWrapper(header.getContext());
        mHeaderView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        mHeaderView.addView(header, new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        
		view.setOnScrollListener(new RecyclerView.OnScrollListener() {
				@Override
				public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
					super.onScrolled(recyclerView, dx, dy);
					if (mHeaderView!= null) {
						RecyclerView.ViewHolder holder = view.findViewHolderForPosition(0);
						if (holder != null)
							translateHeader(-holder.itemView.getTop());
					}
				}
			});
    }
	
	public interface OnClickEvent {
        /**
         * Event triggered when you click on a item of the adapter
         *
         * @param v        view
         * @param position position on the array
         */
        void onClick(View v, int position);
    }

    public interface OnParallaxScroll {
        /**
         * Event triggered when the parallax is being scrolled.
         *
         * @param percentage
         * @param offset
         * @param parallax
         */
        void onParallaxScroll(float percentage, float offset, View parallax);
    }
	
	static class CustomRelativeWrapper extends RelativeLayout {

        private int mOffset;

        public CustomRelativeWrapper(Context context) {
            super(context);
        }

        @Override
        protected void dispatchDraw(Canvas canvas) {
            canvas.clipRect(new Rect(getLeft(), getTop(), getRight(), getBottom() + mOffset));
            super.dispatchDraw(canvas);
        }

        public void setClipY(int offset) {
            mOffset = offset;
            invalidate();
        }
    }
}
