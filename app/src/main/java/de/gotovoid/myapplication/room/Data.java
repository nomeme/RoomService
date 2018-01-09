package de.gotovoid.myapplication.room;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Room database entity defining a simple table.
 * <p>
 * Created by nomeme on 08.01.18.
 */
@Entity(tableName = "data")
public class Data {
    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    private int mId;
    @ColumnInfo(name = "service")
    private String mService;
    @ColumnInfo(name = "value")
    private long mValue;

    /**
     * Constructor.
     *
     * @param value   value to be set
     * @param service name of the service
     */
    public Data(final long value, final String service) {
        mValue = value;
        mService = service;
    }

    /**
     * Returns the value.
     *
     * @return the value
     */
    public long getValue() {
        return mValue;
    }

    /**
     * Returns the id.
     *
     * @return the id
     */
    public int getId() {
        return mId;
    }

    /**
     * Set the id of the object.
     *
     * @param id id of the object
     */
    void setId(final int id) {
        mId = id;
    }

    /**
     * Returns the name of the service.
     *
     * @return name of the service
     */
    public String getService() {
        return mService;
    }
}
