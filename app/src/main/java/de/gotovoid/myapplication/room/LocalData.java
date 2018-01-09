package de.gotovoid.myapplication.room;

import android.arch.persistence.room.Entity;

/**
 * Data to be modified by the {@link de.gotovoid.myapplication.LocalService}.
 * <p>
 * Created by daja3703 on 09.01.18.
 */
@Entity(tableName = "local_data")
public class LocalData extends Data {
    /**
     * Constructor.
     *
     * @param value   value to be set
     * @param service name of the service
     */
    public LocalData(final long value, final String service) {
        super(value, service);
    }
}
