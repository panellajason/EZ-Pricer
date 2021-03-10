package codingsharks.ezpricer.fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import codingsharks.ezpricer.R;

public class NotificationsFragment extends Fragment {

    public NotificationsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().setTitle("Notifications");
        final View view = inflater.inflate(R.layout.fragment_notifications, container, false);

        // code here


        return view;
    }
}
