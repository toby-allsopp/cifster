package nz.gen.mi6.cifster.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import nz.gen.mi6.cifster.R;
import nz.gen.mi6.cifster.model.CifsItem;
import nz.gen.mi6.cifster.model.Model;

public class PathBarAdapter extends BaseAdapter {

    private final Model m_model;
    private final LayoutInflater m_layoutInflater;
    private final OnPathClickListener m_onPathClickListener;

    public PathBarAdapter(
            final Model model,
            final LayoutInflater layoutInflater,
            final OnPathClickListener listener) {
        m_model = model;
        m_layoutInflater = layoutInflater;
        m_onPathClickListener = listener;
    }

    @Override
    public int getCount() {
        return m_model.getParentDirs().size() + 1;
    }

    @Override
    public CifsItem getItem(final int position) {
        if (position < getCount() - 1) {
            return m_model.getParentDirs().get(position);
        } else {
            return m_model.getCurrentDir();
        }
    }

    @Override
    public long getItemId(final int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(
            final int position,
            View convertView,
            final ViewGroup parent) {
        if (convertView == null || !(convertView instanceof Button)) {
            convertView = m_layoutInflater.inflate(R.layout.path_button, null);
        }
        final Button button = (Button) convertView;
        final CifsItem item = getItem(position);
        button.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(final View v) {
                m_onPathClickListener.onPathClick(item);
            }
        });
        button.setText(item.getName().toCharArray(), 0, item.getName().length());
        button.setEnabled(position < getCount() - 1);
        return button;
    }
}
