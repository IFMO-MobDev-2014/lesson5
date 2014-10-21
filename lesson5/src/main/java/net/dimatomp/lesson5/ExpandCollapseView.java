package net.dimatomp.lesson5;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by dimatomp on 21.10.14.
 */
public class ExpandCollapseView extends ImageView {
    public ExpandCollapseView(Context context) {
        super(context);
        onCreate();
    }

    public ExpandCollapseView(Context context, AttributeSet attrs) {
        super(context, attrs);
        onCreate();
    }

    public ExpandCollapseView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        onCreate();
    }

    private void onCreate() {
        setImageResource(R.drawable.expand_collapse);
        setExpanded(true);
    }

    public void setExpanded(boolean expanded) {
        getDrawable().setLevel(expanded ? 1 : 2);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(heightMeasureSpec, heightMeasureSpec);
    }
}
