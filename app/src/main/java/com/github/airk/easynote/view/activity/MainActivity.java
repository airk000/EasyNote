package com.github.airk.easynote.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Button;

import com.github.airk.easynote.R;
import com.github.airk.easynote.adapter.NoteAdapter;
import com.github.airk.easynote.bean.Note;
import com.github.airk.easynote.util.FileOperation;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class MainActivity extends ActionBarActivity implements NoteAdapter.ClickListener, LoaderManager.LoaderCallbacks<List<Note>> {
    private final String TAG = MainActivity.class.getSimpleName();

    @InjectView(R.id.new_note)
    Button mNewNote;
    @InjectView(R.id.recycler_view)
    RecyclerView mRecycler;

    private final int REQUEST_NEW_NOTE = 0x01;
    NoteAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.inject(this);

        mRecycler.setLayoutManager(new GridLayoutManager(this, 2));

        mAdapter = new NoteAdapter(this, this);
        mAdapter.setData(null);
        mRecycler.setAdapter(mAdapter);
        getSupportLoaderManager().initLoader(0, null, this);
    }

    @OnClick(R.id.new_note)
    void newNote() {
        startActivityForResult(new Intent(this, NewNoteActivity.class), REQUEST_NEW_NOTE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_NEW_NOTE && resultCode == RESULT_OK) {
            getSupportLoaderManager().restartLoader(0, null, this);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onItemClicked(long timestamp) {
        Intent intent = new Intent(this, NewNoteActivity.class);
        intent.putExtra(NewNoteActivity.KEY_TIMESTAMP, timestamp);
        startActivityForResult(intent, REQUEST_NEW_NOTE);
    }

    @Override
    public android.support.v4.content.Loader<List<Note>> onCreateLoader(int id, Bundle bundle) {
        mAdapter.setData(null);
        return new NoteLoader(this);
    }

    @Override
    public void onLoadFinished(android.support.v4.content.Loader<List<Note>> listLoader, List<Note> notes) {
        mAdapter.setData(notes);
    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<List<Note>> listLoader) {
        mAdapter.setData(null);
    }

    static class NoteLoader extends AsyncTaskLoader<List<Note>> {
        private final String TAG = NoteLoader.class.getSimpleName();

        public NoteLoader(Context context) {
            super(context);
        }

        @Override
        protected void onStartLoading() {
            super.onStartLoading();
            forceLoad();
        }

        @Override
        protected void onStopLoading() {
            super.onStopLoading();
        }

        @Override
        public List<Note> loadInBackground() {
            List<Note> result = new ArrayList<Note>();
            List<File> files = FileOperation.getAllFiles(getContext());
            for (File f : files) {
                Log.d(TAG, f.getName());
                Note note = FileOperation.readFromFile(getContext(), f.getName());
                result.add(note);
            }
            return result;
        }
    }
}
