package nz.gen.mi6.cifster;

import nz.gen.mi6.cifster.model.CifsDir;
import nz.gen.mi6.cifster.model.Model;
import nz.gen.mi6.cifster.view.OnPathClickListener;
import nz.gen.mi6.cifster.view.PathBar;
import nz.gen.mi6.cifster.view.PathBarAdapter;
import android.app.Activity;
import android.os.Bundle;

public class CifsterActivity extends Activity {
    private Model m_model;
    private PathBar m_pathBar;
    private PathBarAdapter m_pathBarAdapter;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        m_model = new Model();
        // TODO: restore model state from bundle

        final OnPathClickListener listener = new OnPathClickListener() {

            @Override
            public void onPathClick(final CifsDir path) {
                // TODO Auto-generated method stub

            }
        };
        m_pathBar = (PathBar) findViewById(R.id.pathBar);
        assert m_pathBar != null;
        m_pathBarAdapter = new PathBarAdapter(
                m_model,
                getLayoutInflater(),
                listener);
        m_pathBar.setAdapter(m_pathBarAdapter);
    }
}