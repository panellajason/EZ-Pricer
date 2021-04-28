package codingsharks.ezpricer.activities;

import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import codingsharks.ezpricer.R;
import codingsharks.ezpricer.dialogs.ChangePasswordDialog;
import codingsharks.ezpricer.dialogs.ShareDialog;
import codingsharks.ezpricer.fragments.CompareFragment;
import codingsharks.ezpricer.fragments.HomeFragment;
import codingsharks.ezpricer.fragments.BarcodeFragment;
import codingsharks.ezpricer.fragments.NotificationsFragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

public class Main extends AppCompatActivity implements ShareDialog.ShareDialogListener {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser user = mAuth.getCurrentUser();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference itemRef = db.collection("items");

    private BottomNavigationView bottomNav;
    private HomeFragment homeFragment;
    private CompareFragment compareFragment;
    private BarcodeFragment barcodeFragment;
    private NotificationsFragment notificationsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Your Watchlist");
        bottomNav = findViewById(R.id.bottomNav);

        if (mAuth.getCurrentUser() != null) {
            homeFragment = new HomeFragment();
            compareFragment = new CompareFragment();
            barcodeFragment = new BarcodeFragment();
            notificationsFragment = new NotificationsFragment();

            bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull final MenuItem menuItem) {

                    switch (menuItem.getItemId()) {

                        case R.id.navHome:
                            replaceFragment(homeFragment);
                            return true;
                        case R.id.navCompare:
                            replaceFragment(compareFragment);
                            return true;
                        case R.id.nvaBarcode:
                            replaceFragment(new BarcodeFragment());
                            return true;
                        case R.id.navNotifications:
                            replaceFragment(new NotificationsFragment());
                        default:
                            return false;
                    }
                }
            });

            if (savedInstanceState == null) {
                bottomNav.setSelectedItemId(R.id.navHome);
            }

        } else {
            sendTo(Login.class);
        }
    }

    public void replaceFragment(final Fragment fragment) {
        final FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.mainContainer, fragment);
        fragmentTransaction.commit();
    }


    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        final MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {

        switch (item.getItemId()) {
            case R.id.actionLogout:
                logout();
                return true;
            case R.id.actionShare:

                final Query query1 = itemRef.whereEqualTo("userId", mAuth.getUid());
                query1.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull final Task<QuerySnapshot> task) {

                        if (task.isSuccessful()) {
                            if(task.getResult().size() == 0) {
                                Toast.makeText(getApplicationContext(), "No items to share", Toast.LENGTH_LONG).show();
                                return;
                            }
                            else {
                                openDialog();
                            }
                        }
                    }
                });
                return true;
            case R.id.actionSettings:
                startActivity(new Intent(Main.this, AccountPage.class));
                return true;
            default:
                return false;
        }
    }

    @Override
    public void shareWatchlist(final String to, final String subject, final String message) {
        String recipientList = to;
        String[] recipients = recipientList.split(",");
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_EMAIL, recipients);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, message);
        intent.setType("message/rfc822");
        startActivity(Intent.createChooser(intent, "Choose an email client"));
    }


    private void openDialog() {
        ShareDialog dialog = new ShareDialog();
        dialog.show(getSupportFragmentManager(), "share dialog");
    }

    private void logout() {
        mAuth.signOut();
        sendTo(Login.class);
    }

    private void sendTo(Class name) {
        startActivity(new Intent(Main.this, name));
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        bottomNav.setSelectedItemId(R.id.navHome);
        if (mAuth.getCurrentUser() == null) {
            sendTo(Login.class);
        }

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String upc = extras.getString("upc");

            Bundle bundle = new Bundle();
            bundle.putString("upc2", upc);
            compareFragment.setArguments(bundle);
            getIntent().removeExtra("upc");

            replaceFragment(compareFragment);
            bottomNav.setSelectedItemId(R.id.navCompare);
        }
    }
}
