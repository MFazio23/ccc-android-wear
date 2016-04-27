package org.faziodev.cccandroidwear.fragments;


import android.app.Notification;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import org.faziodev.cccandroidwear.R;
import org.faziodev.cccandroidwear.activities.MainActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class NotificationPagesFragment extends Fragment {

    private EditText subjectEditText;
    private EditText contentEditTextPage1;
    private EditText contentEditTextPage2;

    public NotificationPagesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_notification_pages, container, false);

        this.subjectEditText = (EditText) view.findViewById(R.id.subject_edit_text);
        this.contentEditTextPage1 = (EditText) view.findViewById(R.id.page_1_text);
        this.contentEditTextPage2 = (EditText) view.findViewById(R.id.page_2_text);

        final Button sendNotificationButton = (Button) view.findViewById(R.id.send_notification_button);
        sendNotificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int notificationId = (int) (Math.random() * 10000);
                //MainActivity.triggerNotification(getContext(), subjectEditText.getText().toString(), contentEditText.getText().toString());

                final NotificationCompat.Builder mainNotificationBuilder =
                    MainActivity.createNotificationBuilder(getContext(), notificationId, subjectEditText.getText().toString() + " - Page 1", contentEditTextPage1.getText().toString());

                final NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();
                bigTextStyle
                    .setBigContentTitle(subjectEditText.getText().toString() + " - Page 2")
                    .bigText(contentEditTextPage2.getText().toString());

                final Notification page2Notification = new NotificationCompat.Builder(getContext())
                    .setStyle(bigTextStyle)
                    .build();

                final NotificationCompat.Builder notificationBuilder
                    = mainNotificationBuilder.extend(
                        new NotificationCompat.WearableExtender().addPage(page2Notification)
                    );

                MainActivity.triggerNotification(notificationId, getContext(), notificationBuilder);
            }
        });

        return view;
    }

}
