package de.gotovoid.myapplication;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.InvalidationTracker.Observer;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;
import java.util.Set;

import de.gotovoid.myapplication.room.AppDatabase;
import de.gotovoid.myapplication.room.Data;

/**
 * This {@link android.app.Activity} starts two {@link android.app.Service} instances. One in the
 * local process and one in a different process.
 * The {@link android.app.Service} instance then modifies a dedicated table in the
 * {@link AppDatabase} to show that modifications from the {@link LocalService} do notify
 * {@link LiveData} objects listening to changes, whereas modifications from the
 * {@link RemoteService} do not notify the {@link LiveData} objects.
 * <p>
 * As you can also see: the {@link android.arch.persistence.room.InvalidationTracker} for the
 * remote database is also not called.
 * <p>
 * Created by nomeme on 08.01.18.
 */
public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    /**
     * Observer for the {@link android.arch.persistence.room.InvalidationTracker}.
     * Prints out the tables in the {@link AppDatabase} that changed.
     */
    private final Observer mInvalidationObserver = new Observer("remote_data") {
        @Override
        public void onInvalidated(@NonNull final Set<String> tables) {
            for (String table : tables) {
                Log.d(TAG, "onInvalidated: table changed:" + table);
            }
        }
    };

    /**
     * Observer for the {@link de.gotovoid.myapplication.room.RemoteData} modified by the
     * {@link RemoteService}.
     */
    private final android.arch.lifecycle.Observer<List<Data>> mRemoteDataObserver =
            new android.arch.lifecycle.Observer<List<Data>>() {

                @Override
                public void onChanged(@Nullable final List<Data> data) {
                    Log.d(TAG, "onChanged: remote");
                    StringBuilder builder = new StringBuilder();
                    for (Data date : data) {
                        builder.append(date.getValue());
                        builder.append(", ");
                        builder.append(date.getService());
                        builder.append('\n');
                    }
                    mRemoteTextView.setText(builder.toString());
                }
            };
    /**
     * Observer for the {@link de.gotovoid.myapplication.room.RemoteData} modified by the
     * {@link LocalService}.
     */
    private final android.arch.lifecycle.Observer<List<Data>> mLocalDataObserver =
            new android.arch.lifecycle.Observer<List<Data>>() {
                @Override
                public void onChanged(@Nullable final List<Data> data) {
                    Log.d(TAG, "onChanged: local");
                    StringBuilder builder = new StringBuilder();
                    for (Data date : data) {
                        builder.append(date.getValue());
                        builder.append(", ");
                        builder.append(date.getService());
                        builder.append('\n');
                    }
                    mLocalTextView.setText(builder.toString());
                }
            };
    /**
     * {@link AppDatabase} instance.
     */
    private AppDatabase mAppDatabase;

    /**
     * Data modified by the {@link RemoteService}.
     */
    private LiveData<List<Data>> mRemoteData;
    /**
     * Data modified by the {@link LocalService}.
     */
    private LiveData<List<Data>> mLocalData;

    /**
     * {@link TextView} showing the content of the
     * {@link de.gotovoid.myapplication.room.RemoteData} table.
     */
    private TextView mRemoteTextView;
    /**
     * {@link TextView} showing the content of the
     * {@link de.gotovoid.myapplication.room.LocalData} table.
     */
    private TextView mLocalTextView;

    /**
     * Button forcing to reload the {@link LiveData} for the {@link RemoteService} from the
     * {@link AppDatabase} which causes the complete data at that point to be displayed.
     */
    private Button mReloadButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAppDatabase = AppDatabase.getInstance(this);

        mRemoteData = mAppDatabase.getRemoteDataDao().observeData();
        mLocalData = mAppDatabase.getLocalDataDao().observeData();

        mRemoteTextView = findViewById(R.id.remote_text_view);
        mLocalTextView = findViewById(R.id.locat_text_view);
        mReloadButton = findViewById(R.id.reload_button);

        mReloadButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(final View view) {
                /*
                 * Reload the {@link LiveData} from {@link AppDatabase}.
                 */
                mRemoteData.removeObserver(mRemoteDataObserver);
                mRemoteData = mAppDatabase.getRemoteDataDao().observeData();
                mRemoteData.observe(MainActivity.this, mRemoteDataObserver);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Start the services
        Intent remoteIntent = new Intent(this, RemoteService.class);
        startService(remoteIntent);
        Intent localIntent = new Intent(this, LocalService.class);
        startService(localIntent);
        // Start observing the {@link LiveData} objects.
        mAppDatabase.getInvalidationTracker().addObserver(mInvalidationObserver);
        mRemoteData.observe(this, mRemoteDataObserver);
        mLocalData.observe(this, mLocalDataObserver);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Stop the services
        final Intent remoteIntent = new Intent(this, RemoteService.class);
        stopService(remoteIntent);
        final Intent localIntent = new Intent(this, LocalService.class);
        stopService(localIntent);

        mAppDatabase.getInvalidationTracker().removeObserver(mInvalidationObserver);
        // Clear the database tables.
        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(final Void... voids) {
                mAppDatabase.getRemoteDataDao().deletaAll();
                mAppDatabase.getLocalDataDao().deletaAll();
                return null;
            }
        };
        task.execute();
    }
}
