# Room Service example

This is an Android application. It serves as use case example to show that working with  [Room](https://developer.android.com/topic/libraries/architecture/room.html) and [LiveData](https://developer.android.com/topic/libraries/architecture/livedata.html) using a **Service** can be problematic in some cases.

## Problem description

When modifying the data of a  [Room](https://developer.android.com/topic/libraries/architecture/room.html) database from a **Service** running in the same process as the **Activity**, the [LiveData](https://developer.android.com/topic/libraries/architecture/livedata.html) will be updated as soon as the data in the table was changed.

When the **Service** modifying the data runs in another process the [LiveData](https://developer.android.com/topic/libraries/architecture/livedata.html) is no longer updated. The data is stored in the database but there are no updates to the [LiveData](https://developer.android.com/topic/libraries/architecture/livedata.html) object instances in the **Activity**.
Also the **InvalidationTracker** of the database in the **Activity** is not updated.

When a new [LiveData](https://developer.android.com/topic/libraries/architecture/livedata.html) instance is created it contains the newly created data but again no updates are received in the **Activity**.

### Implementation

This repository contains an Android application monitoring two tables of a **RoomDatabase** using **LiveData**. It starts two **Services** each modifying the content of one of the tables. One **Service** runs in the **Activity** process and one **Service** runs in it's own process.
The content of each table is displayed in the **MainActivity**.

Also a *Reload* button is provided. This button creates a new **LiveData** object monitoring the content of the table modified by the **Service** running in it's own process.
