package codingsharks.ezpricer;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Main extends AppCompatActivity {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Main Page");

        if (mAuth.getCurrentUser() != null) {


        }

    }


    @Override
    protected void onStart() {
        super.onStart();

        final FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) {

            final Intent i = new Intent(Main.this, CreateAccount.class);
            startActivity(i);
            finish();
        }

        //bottomNav.setSelectedItemId(R.id.navList);

    }
}
