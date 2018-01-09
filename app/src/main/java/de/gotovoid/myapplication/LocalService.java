package de.gotovoid.myapplication;

import android.support.annotation.NonNull;

import de.gotovoid.myapplication.room.AppDatabase;
import de.gotovoid.myapplication.room.LocalData;

/**
 * Service running in local process modifying the {@link LocalData} table of the
 * {@link AppDatabase}.
 * <p>
 * Created by nomeme on 09.01.18.
 */
public class LocalService extends MyService {
    private static final String TAG = LocalService.class.getSimpleName();

    @Override
    protected String getTag() {
        return TAG;
    }

    @Override
    protected Task getTask(@NonNull final AppDatabase appDatabase) {
        return new Task(appDatabase, getTag()) {
            @Override
            protected void addValue(final int value, final String serviceName) {
                getAppDatabase().getLocalDataDao().insertData(new LocalData(value, serviceName));
            }
        };
    }
}
