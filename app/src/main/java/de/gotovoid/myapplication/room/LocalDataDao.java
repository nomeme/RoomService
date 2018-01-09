package de.gotovoid.myapplication.room;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

/**
 * {@link Dao} object for the {@link LocalData}.
 * <p>
 * Created by nomeme on 08.01.18.
 */
@Dao
public interface LocalDataDao {
    /**
     * Add a new {@link Data} object.
     *
     * @param data {@link Data} object to add
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertData(final LocalData data);

    /**
     * Returns {@link LiveData} object containing the data.
     *
     * @return {@link LiveData} object containing the data.
     */
    @Query("SELECT * FROM local_data")
    LiveData<List<Data>> observeData();

    /**
     * Deletes the whole content.
     */
    @Query("DELETE FROM local_data")
    void deletaAll();
}
