package nz.gen.mi6.cifster;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;

import nz.gen.mi6.cifster.model.CifsItem;
import nz.gen.mi6.cifster.model.Model;
import nz.gen.mi6.cifster.operation.Operation;
import nz.gen.mi6.cifster.operation.DownloadOperation;
import nz.gen.mi6.cifster.view.OnPathClickListener;
import nz.gen.mi6.cifster.view.PathBar;
import nz.gen.mi6.cifster.view.PathBarAdapter;

public class CifsterActivity extends Activity {
    private Model m_model;
    private PathBar m_pathBar;
    private PathBarAdapter m_pathBarAdapter;
    private ListView m_listView;
    private ProgressBar m_progressBar;
    private CifsItem m_operatedUponItem;

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
        registerForContextMenu(m_listView);

        m_progressBar = (ProgressBar) findViewById(R.id.empty);

        startUpdatingList();
    }

    @Override
    protected void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("model", m_model);
    }

    @Override
    public void onCreateContextMenu(
            final ContextMenu menu,
            final View v,
            final ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        final MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.item_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(final MenuItem item) {
        final AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
        m_operatedUponItem = (CifsItem) m_listView.getAdapter().getItem(
                info.position);
        switch (item.getItemId()) {
        case R.id.copyItem:
            startActivityForResult(new Intent(
                    this,
                    DestinationPickerActivity.class), 1);
            // copyItem(info.id);
            return true;
        case R.id.deleteItem:
            // deleteItem(info.id);
            return true;
        default:
            return super.onContextItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(
            final int requestCode,
            final int resultCode,
            final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
        case 1:
            if (resultCode == RESULT_OK) {
                final String destination = data.getStringExtra(DestinationPickerActivity.RESULT_EXTRA);
                if (destination != null) {
                    final Intent copyCommand = new Intent(
                            this,
                            BackgroundOperationService.class);
                    final Operation operation = new DownloadOperation(
                            m_operatedUponItem,
                            destination);
                    copyCommand.putExtra(
                            BackgroundOperationService.OPERATION_EXTRA,
                            operation);
                    startService(copyCommand);
                }
            }
        }
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