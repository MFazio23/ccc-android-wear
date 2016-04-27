package org.faziodev.cccandroidwear.activities;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;

import org.faziodev.cccandroidwear.R;
import org.faziodev.cccandroidwear.fragments.NotificationActionFragment;
import org.faziodev.cccandroidwear.fragments.NotificationBigTextStyleFragment;
import org.faziodev.cccandroidwear.fragments.NotificationFragment;
import org.faziodev.cccandroidwear.fragments.NotificationPagesFragment;
import org.faziodev.cccandroidwear.fragments.NotificationWearableActionFragment;
import org.faziodev.cccandroidwear.types.DataApiResult;
import org.faziodev.cccandroidwear.types.NotificationContent;

import java.util.Date;

public class MainActivity extends AppCompatActivity
        implements
            NavigationView.OnNavigationItemSelectedListener,
            DataApi.DataListener,
            GoogleApiClient.ConnectionCallbacks,
            GoogleApiClient.OnConnectionFailedListener {

    private static String currentGroup;
    private Firebase firebaseDB;
    private GoogleApiClient googleApiClient;

    private boolean newNotifiationItems = false;
    private boolean newDataApiItems = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        drawer.openDrawer(Gravity.LEFT);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Firebase.setAndroidContext(this);
        this.firebaseDB = new Firebase("https://ccc-androidwear.firebaseio.com/");

        final Firebase notificationsDB = this.firebaseDB.child("notifications");

        notificationsDB.limitToLast(10).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //newItems check is to avoid getting all existing values
                if(newNotifiationItems) {
                    final NotificationContent notificationContent
                        = dataSnapshot.getValue(NotificationContent.class);

                    PutDataMapRequest putDataMapReq = PutDataMapRequest.create("/firebase");
                    putDataMapReq.getDataMap().putInt("source", DataApiResult.DataSource.Mobile.ordinal());
                    putDataMapReq.getDataMap().putString("message", notificationContent.toString());
                    putDataMapReq.getDataMap().putString("timestamp", new Date().toString());
                    PutDataRequest putDataReq = putDataMapReq.asPutDataRequest();
                    PendingResult<DataApi.DataItemResult> pendingResult =
                        Wearable.DataApi.putDataItem(googleApiClient, putDataReq);

                } else {
                    newNotifiationItems = true;
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) { }
        });

        final Firebase dataApiDB = this.firebaseDB.child("data-api");
        dataApiDB.limitToLast(1).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //newItems check is to avoid getting all existing values
                if(newDataApiItems) {
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        final DataApiResult result = snapshot.getValue(DataApiResult.class);
                        Log.wtf("DataAPI Data", "Data API Result => [" + result.toString() + "]");
                        MainActivity.triggerNotification(getApplicationContext(), result.getSource().name(), result.getNotes());
                    }
                    //Log.wtf("DataApiDB", "Data => [" + dataSnapshot.getValue().toString() + "]");
                } else {
                    newDataApiItems = true;
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) { }
        });

        this.googleApiClient = new GoogleApiClient.Builder(this)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .addApi(Wearable.API)
            .build();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        Fragment selectedFragment = null;
        int id = item.getItemId();

        if (id == R.id.nav_notifications) {
            selectedFragment = new NotificationFragment();
            MainActivity.currentGroup = "Notifications";
        } else if (id == R.id.nav_notification_actions) {
            selectedFragment = new NotificationActionFragment();
            MainActivity.currentGroup = "NotificationAction";
        } else if (id == R.id.nav_notification_wearable_actions) {
            selectedFragment = new NotificationWearableActionFragment();
            MainActivity.currentGroup = "NotificationWearableAction";
        } else if (id == R.id.nav_notification_large_text) {
            selectedFragment = new NotificationBigTextStyleFragment();
            MainActivity.currentGroup = "NotificationBigText";
        } else if (id == R.id.nav_notification_pages) {
            selectedFragment = new NotificationPagesFragment();
            MainActivity.currentGroup = "NotificationPages";
        }

        if(selectedFragment != null) {
            final FragmentTransaction transaction = this.getSupportFragmentManager().beginTransaction();

            transaction.replace(R.id.fragment_container, selectedFragment);
            transaction.addToBackStack(null);

            transaction.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public static void triggerNotification(final int notificationId, final Context context, final NotificationCompat.Builder builder) {
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        notificationManager.notify(notificationId, builder.build());
    }
    public static void triggerNotification(final Context context, final String subject, final String content, final NotificationCompat.Action... actions) {
        MainActivity.triggerNotification(context, subject, content, false, null, actions);
    }
    public static void triggerNotification(final Context context, final String subject, final String content, final boolean wearableOnly, final NotificationCompat.Action... actions) {
        MainActivity.triggerNotification(context, subject, content, wearableOnly, null, actions);
    }
    public static void triggerNotification(final Context context, final String subject, final String content, final NotificationCompat.BigTextStyle bigTextStyle, final NotificationCompat.Action... actions) {
        MainActivity.triggerNotification(context, subject, content, false, bigTextStyle, actions);
    }
    public static void triggerNotification(final Context context, final String subject, final String content, final boolean wearableOnly, final NotificationCompat.BigTextStyle bigTextStyle, final NotificationCompat.Action... actions) {
        int notificationId = (int) (Math.random() * 10000);

        final NotificationCompat.Builder builder = MainActivity.createNotificationBuilder(context, notificationId, subject, content, wearableOnly, bigTextStyle, actions);

        MainActivity.triggerNotification(notificationId, context, builder);
    }

    public static NotificationCompat.Builder createNotificationBuilder(final Context context, final int notificationId, final String subject, final String content) {
       return MainActivity.createNotificationBuilder(context, notificationId, subject, content, false, null);
    }

    public static NotificationCompat.Builder createNotificationBuilder(final Context context, final int notificationId, final String subject, final String content, final boolean wearableOnly, final NotificationCompat.BigTextStyle bigTextStyle, final NotificationCompat.Action... actions) {
        final Intent intent = new Intent();
        intent.putExtra("Extra-NotificationId", notificationId);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
            .setDefaults(Notification.DEFAULT_ALL)
            .setSmallIcon(R.mipmap.wear_gb_launcher)
            .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.wear_gb_launcher_white_bg))
            .setContentTitle(subject)
            .setContentText(content)
            .setGroup(currentGroup)
            .setContentIntent(pendingIntent);

        for(NotificationCompat.Action action : actions) {
            if(wearableOnly) {
                notificationBuilder.extend(new NotificationCompat.WearableExtender().addAction(action));
            } else {
                notificationBuilder.addAction(action);
            }
        }

        if(bigTextStyle != null) {
            notificationBuilder.setStyle(bigTextStyle);
        }

        return notificationBuilder;
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.googleApiClient.connect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Wearable.DataApi.addListener(this.googleApiClient, this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Wearable.DataApi.removeListener(this.googleApiClient, this);
        this.googleApiClient.disconnect();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onDataChanged(DataEventBuffer dataEvents) {
        for (DataEvent event : dataEvents) {
            if (event.getType() == DataEvent.TYPE_CHANGED) {
                // DataItem changed
                DataItem item = event.getDataItem();
                if (item.getUri().getPath().compareTo("/firebase") == 0) {
                    DataMap dataMap = DataMapItem.fromDataItem(item).getDataMap();
                    this.firebaseDB.child("data-api").push().setValue(new DataApiResult(DataApiResult.DataSource.values()[dataMap.getInt("source")],dataMap.getString("message")));
                }
            } else if (event.getType() == DataEvent.TYPE_DELETED) {
                // DataItem deleted
            }
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
