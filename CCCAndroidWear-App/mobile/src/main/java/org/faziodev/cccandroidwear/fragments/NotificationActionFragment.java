package org.faziodev.cccandroidwear.fragments;

import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
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

public class NotificationActionFragment extends Fragment {

    private EditText subjectEditText;
    private EditText contentEditText;
    private EditText searchTermEditText;

    public NotificationActionFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_notification_action, container, false);

        this.subjectEditText = (EditText) view.findViewById(R.id.subject_edit_text);
        this.contentEditText = (EditText) view.findViewById(R.id.content_edit_text);
        this.searchTermEditText = (EditText) view.findViewById(R.id.search_term_edit_text);

        final Button sendNotificationButton = (Button) view.findViewById(R.id.send_notification_button);
        sendNotificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Uri uri = Uri.parse("http://www.google.com/#q=" + searchTermEditText.getText().toString());
                final Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                final PendingIntent pendingIntent = PendingIntent.getActivity(getContext(), 0, intent, 0);

                final NotificationCompat.Action action = new NotificationCompat.Action(android.R.drawable.ic_menu_search, "Google Search", pendingIntent);
                MainActivity.triggerNotification(getContext(), subjectEditText.getText().toString(), contentEditText.getText().toString(), action);
            }
        });

        return view;
    }
}
