package codingsharks.ezpricer.fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import codingsharks.ezpricer.R;
import codingsharks.ezpricer.activities.Login;
import codingsharks.ezpricer.random.EmailNotification;
import codingsharks.ezpricer.random.NotificationController;

public class NotificationsFragment extends Fragment {

    private Button emailButtonTest;

    public NotificationsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().setTitle("Notifications");
        final View view = inflater.inflate(R.layout.fragment_notifications, container, false);

        // code here
        emailButtonTest = (Button)view.findViewById(R.id.email_button_test);

        emailButtonTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TRIGGERS EMAIL FOR TESTING
                EmailNotification email = new EmailNotification(NotificationsFragment.this, "");
                email.sendEmail();
            }
        });

        return view;
    }
}
