package nz.gen.mi6.cifster.view;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

public class PathBar extends AdapterView<PathBarAdapter> {

    private static final String LOG_TAG = "PathBar";

    public PathBar(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }

    private PathBarAdapter m_adapter;
    private final DataSetObserver m_dataSetObserver = new DataSetObserver() {

        @Override
        public void onChanged() {
            // TODO Auto-generated method stub
            super.onChanged();
            requestLayout();
        }

        @Override
        public void onInvalidated() {
            // TODO Auto-generated method stub
            super.onInvalidated();
            requestLayout();
        }

    };

    @Override
    public PathBarAdapter getAdapter() {
        return m_adapter;
    }

    @Override
    public View getSelectedView() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setAdapter(final PathBarAdapter adapter) {
        if (m_adapter != null) {
            m_adapter.unregisterDataSetObserver(m_dataSetObserver);
        }
        m_adapter = adapter;
        m_adapter.registerDataSetObserver(m_dataSetObserver);
    }

    @Override
    public void setSelection(final int position) {
        // TODO Auto-generated method stub
    }

    @Override
    protected void onLayout(
            final boolean changed,
            final int left,
            final int top,
            final int right,
            final int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        Log.d(LOG_TAG, "onLayout(" + changed + ", " + left + ", " + top + ", "
                + right + ", " + bottom + ")");
        removeAllViewsInLayout();
        if (m_adapter == null) {
            Log.d(LOG_TAG, "  no adapter - done.");
            return;
        }
        final int viewCount = m_adapter.getCount();
        Log.d(LOG_TAG, "  viewCount = " + viewCount);
        for (int i = 0; i < viewCount; ++i) {
            final View view = m_adapter.getView(i, null, null);
            final LayoutParams params = new LayoutParams(
                    LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT);
            addViewInLayout(view, i, params);
            view.measure(MeasureSpec.makeMeasureSpec(
                    getWidth(),
                    MeasureSpec.AT_MOST), MeasureSpec.makeMeasureSpec(
                    getHeight(),
                    MeasureSpec.AT_MOST));
        }

        int x = 0;
        final int childCount = getChildCount();
        Log.d(LOG_TAG, "  childCount = " + childCount);
        for (int i = 0; i < childCount; ++i) {
            final View child = getChildAt(i);
            final int width = child.getMeasuredWidth();
            final int height = child.getMeasuredHeight();
            Log.d(LOG_TAG, String.format(
                    "  child %d: x=%d, width=%d, height=%d",
                    i,
                    x,
                    width,
                    height));
            child.layout(x, 0, x + width, height);
            x += width;
        }
        requestRectangleOnScreen(new Rect(x, 0, x, 0));
    }

    @Override
    protected void onMeasure(
            final int widthMeasureSpec,
            final int heightMeasureSpec) {
        // TODO Auto-generated method stub
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.d(
                LOG_TAG,
                String.format(
                        "onMeasure(%s, %s)",
                        MeasureSpec.toString(widthMeasureSpec),
                        MeasureSpec.toString(heightMeasureSpec)));
        if (m_adapter == null) {
            Log.d(LOG_TAG, "  no adapter");
        }
        int width = 0;
        int height = 0;
        final int viewCount = (m_adapter == null) ? 0 : m_adapter.getCount();
        Log.d(LOG_TAG, "  viewCount = " + viewCount);
        for (int i = 0; i < viewCount; ++i) {
            Log.d(LOG_TAG, String.format(
                    "  view %d: width=%d, height=%d",
                    i,
                    width,
                    height));
            final View view = m_adapter.getView(i, null, null);
            view.measure(widthMeasureSpec, heightMeasureSpec);
            width += view.getMeasuredWidth();
            height = Math.max(height, view.getMeasuredHeight());
        }
        Log.d(LOG_TAG, String.format("  width=%d, height=%d", width, height));
        setMeasuredDimension(width, height);
    }
}
