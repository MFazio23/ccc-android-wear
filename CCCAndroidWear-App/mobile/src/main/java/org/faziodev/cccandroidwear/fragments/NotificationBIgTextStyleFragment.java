package org.faziodev.cccandroidwear.fragments;

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

public class NotificationBigTextStyleFragment extends Fragment {

    private EditText subjectEditText;
    private EditText contentEditText;

    public NotificationBigTextStyleFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_notification_big_text_style, container, false);

        this.subjectEditText = (EditText) view.findViewById(R.id.subject_edit_text);
        this.contentEditText = (EditText) view.findViewById(R.id.content_edit_text);

        final Button sendNotificationButton = (Button) view.findViewById(R.id.send_notification_button);
        sendNotificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();
                bigTextStyle.bigText(contentEditText.getText().toString());

                MainActivity.triggerNotification(getContext(), subjectEditText.getText().toString(), "We're not using this value.", bigTextStyle);
            }
        });

        return view;
    }
}
