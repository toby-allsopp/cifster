package nz.gen.mi6.cifster;

import java.util.List;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;

import nz.gen.mi6.cifster.model.CifsItem;
import nz.gen.mi6.cifster.model.Model;
import nz.gen.mi6.cifster.view.OnPathClickListener;
import nz.gen.mi6.cifster.view.PathBar;
import nz.gen.mi6.cifster.view.PathBarAdapter;

public class CifsterActivity extends Activity {
    private Model m_model;
    private PathBar m_pathBar;
    private PathBarAdapter m_pathBarAdapter;
    private ListView m_listView;
    private ProgressBar m_progressBar;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        if (savedInstanceState != null) {
            m_model = savedInstanceState.getParcelable("model");
        } else {
            m_model = new Model();
        }

        final OnPathClickListener listener = new OnPathClickListener() {

            @Override
            public void onPathClick(final CifsItem path) {
                m_model.rewindToParent(path);
                m_pathBarAdapter.notifyDataSetChanged();
                startUpdatingList();
            }
        };
        m_pathBar = (PathBar) findViewById(R.id.pathBar);
        assert m_pathBar != null;
        m_pathBarAdapter = new PathBarAdapter(
                m_model,
                getLayoutInflater(),
                listener);
        m_pathBar.setAdapter(m_pathBarAdapter);

        m_listView = (ListView) findViewById(R.id.list);
        m_listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(
                    final AdapterView<?> listView,
                    final View view,
                    final int position,
                    final long id) {
                final CifsItem item = (CifsItem) m_listView.getAdapter().getItem(
                        position);
                m_model.enterChild(item);
                m_pathBarAdapter.notifyDataSetChanged();
                startUpdatingList();
            }
        });

        m_progressBar = (ProgressBar) findViewById(R.id.empty);

        startUpdatingList();
    }

    @Override
    protected void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("model", m_model);
    }

    protected void startUpdatingList() {

        m_listView.setAdapter(null);
        m_progressBar.setVisibility(View.VISIBLE);
        (new AsyncTask<Void, Void, List<CifsItem>>() {

            @Override
            protected List<CifsItem> doInBackground(final Void... arg0) {
                return m_model.getCurrentDir().getChildren();
            }

            @Override
            protected void onPostExecute(final List<CifsItem> result) {
                m_progressBar.setVisibility(View.GONE);
                m_listView.setAdapter(new CifsItemListAdapter(
                        getLayoutInflater(),
                        result));
            }

        }).execute();
    }
}