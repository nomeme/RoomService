package de.gotovoid.myapplication;

import android.app.Service;
import android.arch.persistence.room.InvalidationTracker.Observer;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.Set;

import de.gotovoid.myapplication.room.AppDatabase;

/**
 * Service to update data in an {@link AppDatabase} instance every second.
 * <p>
 * Created by nomeme on 09.01.18.
 */
public abstract class MyService extends Service {
    private final Observer mObserver = new Observer("remote_data", "local_data") {
        @Override
        public void onInvalidated(@NonNull final Set<String> tables) {
            for (String table : tables) {
                Log.d(getTag(), "onInvalidated: " + table);
            }
        }
    };

    private AsyncTask<Integer, Void, Void> mTask;
    private AppDatabase mAppDatabase;

    /**
     * Returns the unique name for the {@link MyService} instance.
     *
     * @return unique name for the {@link MyService}
     */
    protected abstract String getTag();

    @Override
    public void onCreate() {
        Log.d(getTag(), "onCreate() called");
        super.onCreate();
        mAppDatabase = AppDatabase.getInstance(this);
        mAppDatabase.getInvalidationTracker().addObserver(mObserver);
        mTask = getTask(mAppDatabase);

    }

    @Override
    public int onStartCommand(final Intent intent, final int flags, final int startId) {
        Log.d(getTag(), "onStartCommand() called with: intent = [" + intent + "], flags = [" + flags
                + "], startId = [" + startId + "]");
        mTask.execute(0);
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(final Intent intent) {
        Log.d(getTag(), "onBind: ");
        return null;
    }

    @Override
    public void onDestroy() {
        Log.d(getTag(), "onDestroy: ");
        mTask.cancel(true);
        mAppDatabase.getInvalidationTracker().removeObserver(mObserver);
        super.onDestroy();
    }

    /**
     * Returns the {@link Task} to use for modifying the {@link AppDatabase}.
     *
     * @param appDatabase the {@link AppDatabase} to modify
     * @return the {@link Task}
     */
    protected abstract Task getTask(@Nullable final AppDatabase appDatabase);

    /**
     * {@link AsyncTask} used to modify {@link AppDatabase} table.
     */
    protected static abstract class Task extends AsyncTask<Integer, Void, Void> {
        private final AppDatabase mAppDatabase;
        private final String mServiceName;
        private int mValue;

        public Task(final AppDatabase database, final String name) {
            mAppDatabase = database;
            mServiceName = name;
        }

        @Override
        protected Void doInBackground(final Integer... integers) {
            mValue = integers[0];
            while (!isCancelled()) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                addValue(mValue, mServiceName);
                mValue++;
            }
            return null;
        }

        /**
         * Returns the {@link AppDatabase} instance to modify.
         *
         * @return the {@link AppDatabase}
         */
        protected AppDatabase getAppDatabase() {
            return mAppDatabase;
        }

        /**
         * Add a value to the appropriate table.
         *
         * @param value       value to be added
         * @param serviceName name of the service
         */
        protected abstract void addValue(final int value, final String serviceName);
    }
}
