package com.at.drgrep.utility;

/**
 * Created by Arup on 7/1/2016.
 */
import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;

import com.at.drgrep.R;


public class ScrollableSwipeRefreshLayout extends SwipeRefreshLayout {

    public ScrollableSwipeRefreshLayout(Context context) {
        super(context);
    }

    public ScrollableSwipeRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean canChildScrollUp() {
        // Condition to check whether scrollview reached at the top while scrolling or not
        return findViewById(R.id.grid_view).canScrollVertically(-1);
    }
}
