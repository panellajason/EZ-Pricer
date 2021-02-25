package codingsharks.ezpricer.activities;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import codingsharks.ezpricer.R;
import codingsharks.ezpricer.fragments.CompareFragment;
import codingsharks.ezpricer.fragments.HomeFragment;
import codingsharks.ezpricer.fragments.NotificationsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class Main extends AppCompatActivity {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private BottomNavigationView bottomNav;
    private Fragment homeFragment;
    private Fragment compareFragment;
    private Fragment notificationsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Home");

        if (mAuth.getCurrentUser() != null) {

            homeFragment = new HomeFragment();
            compareFragment = new CompareFragment();
            notificationsFragment = new NotificationsFragment();

            bottomNav = findViewById(R.id.bottomNav);

            replaceFragment(homeFragment);

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
                        case R.id.navNotifications:
                            replaceFragment(new NotificationsFragment());
                            return true;

                        default:
                            return false;
                    }
                }
            });

            if (savedInstanceState == null) {
                bottomNav.setSelectedItemId(R.id.navHome);
            }

        } else {
            sendToLogin();
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
            case R.id.actionSearch:
                return true;
            case R.id.actionSettings:
                return true;
            default:
                return false;
        }
    }

    private void logout() {
        mAuth.signOut();
        sendToLogin();
    }

    private void sendToLogin() {
        startActivity(new Intent(Main.this, Login.class));
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mAuth.getCurrentUser() == null) {
            startActivity(new Intent(Main.this, Login.class));
            finish();
        }

        bottomNav.setSelectedItemId(R.id.navHome);

    }
}
