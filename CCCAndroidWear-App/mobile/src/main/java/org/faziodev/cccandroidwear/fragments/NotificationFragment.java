package org.faziodev.cccandroidwear.fragments;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import org.faziodev.cccandroidwear.R;

public class NotificationFragment extends Fragment {

    private EditText subjectEditText;
    private EditText contentEditText;

    public NotificationFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_notification, container, false);

        this.subjectEditText = (EditText) view.findViewById(R.id.subject_edit_text);
        this.contentEditText = (EditText) view.findViewById(R.id.content_edit_text);

        final Button sendNotificationButton = (Button) view.findViewById(R.id.send_notification_button);
        sendNotificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayNotification(subjectEditText.getText().toString(), contentEditText.getText().toString());
            }
        });

        return view;
    }

    private void displayNotification(final String subject, final String content) {
        int notificationId = 023;

        final Intent intent = new Intent();
        intent.putExtra("Extra-NotificationId", notificationId);
        PendingIntent pendingIntent = PendingIntent.getActivity(this.getContext(), 0, intent, 0);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this.getContext())
            .setDefaults(Notification.DEFAULT_ALL)
            .setSmallIcon(R.mipmap.wear_gb_launcher)
            .setContentTitle(subject)
            .setContentText(content)
            .setContentIntent(pendingIntent);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this.getContext());

        notificationManager.notify(notificationId, notificationBuilder.build());
    }
}
