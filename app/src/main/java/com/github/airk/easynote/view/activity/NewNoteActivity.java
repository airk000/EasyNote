package com.github.airk.easynote.view.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.github.airk.easynote.R;
import com.github.airk.easynote.bean.Note;
import com.github.airk.easynote.util.FileOperation;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by kevin on 2014/10/18.
 */
public class NewNoteActivity extends ActionBarActivity {
    @InjectView(R.id.title)
    EditText mTitle;
    @InjectView(R.id.timestamp)
    EditText mTimestamp;
    @InjectView(R.id.content)
    EditText mContent;

    long timestamp;
    public static final String KEY_TIMESTAMP = "timestamp_key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);

        ButterKnife.inject(this);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm E");
        if (getIntent() != null && getIntent().getLongExtra(KEY_TIMESTAMP, 0) != 0) {
            timestamp = getIntent().getLongExtra(KEY_TIMESTAMP, 0);
            Note note = FileOperation.readFromFile(this, "" + timestamp);
            if (null != note) {
                mTitle.setText(note.title);
                mContent.setText(note.content);
            }
        } else {
            timestamp = System.currentTimeMillis();
        }
        mTimestamp.setText(simpleDateFormat.format(new Date(timestamp)));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        menu.clear();
        inflater.inflate(R.menu.note, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_save:
                if (TextUtils.isEmpty(mContent.getText())) {
                    Toast.makeText(this, getResources().getString(R.string.note_no_content), Toast.LENGTH_SHORT).show();
                    break;
                }
                Toast.makeText(this, getResources().getString(R.string.note_action_save), Toast.LENGTH_SHORT).show();
                Note note = new Note();
                note.title = mTitle.getText().toString();
                note.timestamp = timestamp;
                note.content = mContent.getText().toString();
                FileOperation.writeOrUpdateNote(this, note, timestamp + "");
                finish();
                setResult(RESULT_OK);
                break;
            case R.id.action_remove:
                Toast.makeText(this, getResources().getString(R.string.note_action_remove), Toast.LENGTH_SHORT).show();
                FileOperation.deleteFile(this, timestamp + "");
                finish();
                setResult(RESULT_OK);
        }
        return super.onOptionsItemSelected(item);
    }
}
