package de.gotovoid.myapplication.room;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

/**
 * Database holding a table for {@link LocalData} and {@link RemoteData}.
 * {@link LocalData} is modified by the {@link de.gotovoid.myapplication.LocalService}.
 * {@link RemoteData} is modified by the {@link de.gotovoid.myapplication.RemoteService}.
 * <p>
 * Created by nomeme on 08.01.18.
 */
@Database(entities = {LocalData.class, RemoteData.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase sInstance;

    /**
     * Return the {@link AppDatabase} instance.
     */
    public static AppDatabase getInstance(final Context context) {
        if (sInstance == null) {
            sInstance = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class,
                    "data-database").build();
        }
        return sInstance;
    }

    /**
     * Returns the {@link LocalDataDao}.
     *
     * @return the {@link LocalDataDao}
     */
    public abstract LocalDataDao getLocalDataDao();

    /**
     * Returns the {@link RemoteDataDao}.
     *
     * @return the {@link RemoteDataDao}
     */
    public abstract RemoteDataDao getRemoteDataDao();
}
