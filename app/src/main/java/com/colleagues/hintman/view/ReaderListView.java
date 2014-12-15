package com.colleagues.hintman.view;
import android.support.v7.widget.*;
import android.content.*;
import android.util.*;
import android.widget.*;
import android.widget.AdapterView.*;

public class ReaderListView extends RecyclerView {
    public ReaderListView(Context context) {
        super(context);
    }

    public ReaderListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ReaderListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean canScrollVertically(int direction) {
        // check if scrolling up
        if (direction < 1) {
            boolean original = super.canScrollVertically(direction);
            return !original && getChildAt(0) != null && getChildAt(0).getTop() < 0 || original;
        }
	
		
        return super.canScrollVertically(direction);

		
    }
	
}
