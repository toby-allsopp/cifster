package nz.gen.mi6.cifster;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.EditText;
import android.widget.TextView.BufferType;

public class DestinationPickerActivity extends Activity {
    public static final String RESULT_EXTRA = DestinationPickerActivity.class.getPackage().getName()
            + "." + DestinationPickerActivity.class.getName() + ".Result";

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.destination_picker);

        final EditText textBox = (EditText) findViewById(R.id.text);
        textBox.setText(
                Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_DOWNLOADS).getPath(),
                BufferType.NORMAL);
        textBox.setOnKeyListener(new OnKeyListener() {

            @Override
            public boolean onKey(
                    final View v,
                    final int keyCode,
                    final KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN
                        && keyCode == KeyEvent.KEYCODE_ENTER) {
                    final Intent result = new Intent();
                    result.putExtra(RESULT_EXTRA, textBox.getText().toString());
                    setResult(RESULT_OK, result);
                    finish();
                    return true;
                }
                return false;
            }
        });
    }
}
