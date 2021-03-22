package codingsharks.ezpricer.activities;

import android.content.Intent;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import codingsharks.ezpricer.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class AccountPage extends AppCompatActivity implements ChangePasswordDialog.ExampleDialogListener{

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private String email = mAuth.getCurrentUser().getEmail();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference userItemRef = db.document("user_settings/" + mAuth.getUid());

    private Switch textSwitch;
    private Switch appSwitch;
    private Switch emailSwitch;

    private TextView phoneNumberTV;
    private TextView emailTV;
    private TextView editTV;
    private TextView changePasswordTV;

    private Button saveBTN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_page);
        setTitle("Account Settings");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        textSwitch = findViewById(R.id.textSwitch);
        appSwitch = findViewById(R.id.appSwitch);
        emailSwitch = findViewById(R.id.emailSwitch);
        phoneNumberTV = findViewById(R.id.phoneNumberTV);
        emailTV = findViewById(R.id.emailTV);
        editTV = findViewById(R.id.editNumberTV);
        changePasswordTV = findViewById(R.id.changePasswordTV);
        saveBTN = findViewById(R.id.saveBTN);
        saveBTN.setVisibility(View.INVISIBLE);

        displaySettings();

        changePasswordTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                openDialog();
            }
        });

        saveBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                saveSettings();
            }
        });

        textSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                saveBTN.setVisibility(View.VISIBLE);
            }
        });
        appSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                saveBTN.setVisibility(View.VISIBLE);
            }
        });
        emailSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                saveBTN.setVisibility(View.VISIBLE);
            }
        });
    }

    private void openDialog() {
        ChangePasswordDialog dialog = new ChangePasswordDialog();
        dialog.show(getSupportFragmentManager(), "changePass dialog");
    }

    @Override
    public void applyTexts(String password, String password2) {
        if (password == password2)
            changePassword(password);
        else
            Toast.makeText(getApplicationContext(), "Passwords do not match", Toast.LENGTH_LONG).show();
    }

    private void changePassword(String password) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        user.updatePassword(password)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            //close dialog
                            Toast.makeText(getApplicationContext(), "Password successfully changed", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void saveSettings() {

        userItemRef.update("text_notification", textSwitch.isChecked() ? 1 + "" : 0 + "",
                "app_notification", appSwitch.isChecked() ? 1 + "" : 0 + "",
                "email_notification", emailSwitch.isChecked() ? 1 + "" : 0 + "")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull final Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Account Settings Saved", Toast.LENGTH_SHORT).show();
                            saveBTN.setVisibility(View.INVISIBLE);
                            displaySettings();
                        } else {
                            Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void displaySettings() {
        userItemRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(final DocumentSnapshot documentSnapshot) {

                if (documentSnapshot.exists()) {

                    phoneNumberTV.setText("Phone Number: " + documentSnapshot.getString("phone_number"));
                    emailTV.setText("Email: " + email);

                    textSwitch.setChecked(Integer.parseInt(documentSnapshot.getString("text_notification")) == 1);
                    appSwitch.setChecked(Integer.parseInt(documentSnapshot.getString("app_notification")) == 1);
                    emailSwitch.setChecked(Integer.parseInt(documentSnapshot.getString("email_notification")) == 1);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull final Exception e) {
                startActivity(new Intent(AccountPage.this, Main.class));
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            startActivity(new Intent(AccountPage.this, Main.class));
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
