package nz.gen.mi6.cifster.view;

import android.content.Context;
import android.database.DataSetObserver;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;

public class PathBar extends AdapterView<PathBarAdapter> {

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
        }

        @Override
        public void onInvalidated() {
            // TODO Auto-generated method stub
            super.onInvalidated();
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

        removeAllViewsInLayout();
        if (m_adapter == null) {
            return;
        }
        for (int i = 0; i < m_adapter.getCount(); ++i) {
            addViewInLayout(m_adapter.getView(i, null, null), i, null);
        }

        int x = 0;
        for (int i = 0; i < getChildCount(); ++i) {
            final View child = getChildAt(i);
            child.layout(
                    x,
                    0,
                    x + child.getMeasuredWidth(),
                    child.getMeasuredHeight());
            x += child.getMeasuredWidth();
        }
    }

    @Override
    protected void onMeasure(
            final int widthMeasureSpec,
            final int heightMeasureSpec) {
        // TODO Auto-generated method stub
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = 0;
        int height = 0;
        for (int i = 0; i < getChildCount(); ++i) {
            final View child = getChildAt(i);
            child.measure(widthMeasureSpec, heightMeasureSpec);
            width += child.getMeasuredWidth();
            height = Math.max(height, child.getMeasuredHeight());
        }
        setMeasuredDimension(width, height);
    }

}
