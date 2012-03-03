package nz.gen.mi6.cifster.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;

public class PathBar extends AdapterView<PathBarAdapter> {

    public PathBar(
            final Context context,
            final AttributeSet attrs,
            final int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
    }

    private PathBarAdapter m_adapter;

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
        m_adapter = adapter;
    }

    @Override
    public void setSelection(final int position) {
        // TODO Auto-generated method stub
    }

}
