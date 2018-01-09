package de.gotovoid.myapplication;

import android.support.annotation.Nullable;

import de.gotovoid.myapplication.room.AppDatabase;
import de.gotovoid.myapplication.room.RemoteData;

/**
 * Service running in a dedicated process, modifying the {@link RemoteData} table in the
 * {@link AppDatabase}.
 * <p>
 * Created by nomeme on 08.01.18.
 */
public class RemoteService extends MyService {
    private static final String TAG = RemoteService.class.getSimpleName();

    @Override
    protected String getTag() {
        return TAG;
    }

    @Override
    protected Task getTask(@Nullable final AppDatabase appDatabase) {
        return new Task(appDatabase, getTag()) {
            @Override
            protected void addValue(final int value, final String serviceName) {
                getAppDatabase().getRemoteDataDao().insertData(new RemoteData(value, serviceName));
            }
        };
    }

}
