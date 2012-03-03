package nz.gen.mi6.cifster;

import java.util.List;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import nz.gen.mi6.cifster.model.CifsItem;

public class CifsItemListAdapter extends BaseAdapter {

    private final List<CifsItem> m_items;
    private final LayoutInflater m_layoutInflater;

    public CifsItemListAdapter(
            final LayoutInflater layoutInflater,
            final List<CifsItem> items) {
        m_layoutInflater = layoutInflater;
        m_items = items;
    }

    @Override
    public int getCount() {
        return m_items.size();
    }

    @Override
    public CifsItem getItem(final int position) {
        return m_items.get(position);
    }

    @Override
    public long getItemId(final int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(
            final int position,
            final View convertView,
            final ViewGroup parent) {
        final View view = convertView != null
                ? convertView
                : m_layoutInflater.inflate(R.layout.list_item, null);
        final TextView textView = (TextView) view;
        final CifsItem item = getItem(position);
        if (item != null) {
            textView.setText(
                    item.getName().toCharArray(),
                    0,
                    item.getName().length());
        } else {
            textView.setText(R.string.badItem);
        }
        return textView;
    }

}
