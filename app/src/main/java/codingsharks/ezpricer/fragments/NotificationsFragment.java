package codingsharks.ezpricer.fragments;

import android.app.NotificationManager;
import android.content.Context;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.util.Objects;

import codingsharks.ezpricer.R;
import codingsharks.ezpricer.random.EmailNotification;
import codingsharks.ezpricer.random.NotificationController;

public class NotificationsFragment extends Fragment {

    private Button emailButtonTest;
    private Button notificationButtonTest;
    private Button priceDropButtonTest;

    public NotificationsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().setTitle("Notifications");
        final View view = inflater.inflate(R.layout.fragment_notifications, container, false);

        // code here
        emailButtonTest = (Button)view.findViewById(R.id.email_button_test);
        notificationButtonTest = (Button)view.findViewById(R.id.phone_notification_button_test);
        priceDropButtonTest = (Button)view.findViewById(R.id.price_drop_button_test);

        emailButtonTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TRIGGERS EMAIL FOR TESTING
                EmailNotification email = new EmailNotification(getActivity(), "");
                email.sendEmail();
            }
        });

        notificationButtonTest.setOnClickListener(new View.OnClickListener() {
           @RequiresApi(api = Build.VERSION_CODES.M)
           @Override
           public void onClick(View view) {
               // Tests notification
               NotificationController notificationController = new NotificationController()
                       .setID("Test")
                       .setName("Test")
                       .setTextTitle("Welcome to EZ Pricer!")
                       .setTextContent("This is a test notification for debugging purposes.")
                       .setManagerID(1)
                       .setManager(Objects.requireNonNull(getActivity()).getSystemService(NotificationManager.class))
                       .setContext(getActivity());

               notificationController.createNotification();
           }
        });

        priceDropButtonTest.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                // Tests notification
                NotificationController notificationController = new NotificationController()
                        .setID("PriceDropNotification")
                        .setName("Price Drop")
                        .setTextTitle("")   // Will be set later
                        .setTextContent("") // Will be set later
                        .setManagerID(1)
                        .setManager(Objects.requireNonNull(getActivity()).getSystemService(NotificationManager.class))
                        .setContext(getActivity());


                notificationController.priceDropNotification();

            }
        });

        return view;
    }
}
