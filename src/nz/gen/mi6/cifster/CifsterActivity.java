package nz.gen.mi6.cifster;

import nz.gen.mi6.cifster.model.CifsDir;
import nz.gen.mi6.cifster.model.Model;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class CifsterActivity extends Activity {
	private Model m_model;
	private ViewGroup m_pathBar;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		m_model = new Model();
		// TODO: restore model state from bundle

		m_pathBar = (ViewGroup) findViewById(R.id.pathBar);
		assert m_pathBar != null;
		for (CifsDir dir : m_model.getCurrentDir().getChildren()) {
			Button parentDirView = (Button) getLayoutInflater().inflate(
					R.layout.path_button, null);
			String name = dir.getName();
			parentDirView.setText(name.toCharArray(), 0, name.length());
			parentDirView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub

				}
			});
		}
	}
}