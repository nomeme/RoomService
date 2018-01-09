package de.gotovoid.myapplication.room;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

/**
 * {@link Dao} object for the {@link RemoteData}.
 * <p>
 * Created by nomeme on 08.01.18.
 */
@Dao
public interface RemoteDataDao {
    /**
     * Add a new {@link Data} object.
     *
     * @param data {@link Data} object to add
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertData(final RemoteData data);

    /**
     * Returns {@link LiveData} object containing the data.
     *
     * @return {@link LiveData} object containing the data.
     */
    @Query("SELECT * FROM remote_data")
    LiveData<List<Data>> observeData();

    /**
     * Deletes the whole content
     */
    @Query("DELETE FROM remote_data")
    void deletaAll();
}
