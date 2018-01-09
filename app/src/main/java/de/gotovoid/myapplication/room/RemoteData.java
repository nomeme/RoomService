package de.gotovoid.myapplication.room;

import android.arch.persistence.room.Entity;

/**
 * Data to be modified by the {@link de.gotovoid.myapplication.RemoteService}.
 * <p>
 * Created by nomeme on 09.01.18.
 */
@Entity(tableName = "remote_data")
public class RemoteData extends Data {
    /**
     * Constructor.
     *
     * @param value   value to be set
     * @param service name of the service
     */
    public RemoteData(final long value, final String service) {
        super(value, service);
    }
}
