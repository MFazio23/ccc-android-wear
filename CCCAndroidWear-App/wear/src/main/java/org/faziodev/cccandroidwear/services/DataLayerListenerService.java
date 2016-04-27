package org.faziodev.cccandroidwear.services;

import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.WearableListenerService;

/**
 * Created by MFazio on 4/27/2016.
 */
public class DataLayerListenerService extends WearableListenerService {

    private GoogleApiClient googleApiClient;

    @Override
    public void onCreate() {
        super.onCreate();
        /*this.googleApiClient = new GoogleApiClient.Builder(this)
            .addApi(Wearable.API)
            .build();*/

        //this.googleApiClient.connect();
    }

    @Override
    public void onDataChanged(DataEventBuffer dataEventBuffer) {
        Log.wtf("Wear-Service", "Data changed.");
    }
}
