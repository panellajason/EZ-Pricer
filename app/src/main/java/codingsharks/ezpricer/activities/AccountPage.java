package codingsharks.ezpricer.activities;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import codingsharks.ezpricer.R;
import codingsharks.ezpricer.dialogs.ChangeNumberDialog;
import codingsharks.ezpricer.dialogs.ChangePasswordDialog;
import codingsharks.ezpricer.dialogs.DeleteAccountDialog;
import codingsharks.ezpricer.dialogs.ReEnterPasswordDialog;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class AccountPage extends AppCompatActivity
        implements ChangePasswordDialog.ExampleDialogListener,
                    ChangeNumberDialog.PhoneDialogListener,
                    DeleteAccountDialog.DeleteAccountDialogListener,
                    ReEnterPasswordDialog.ConfirmDeleteAccountDialogListener {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser user = mAuth.getCurrentUser();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference userItemRef = db.document("user_settings/" + mAuth.getUid());

    public Context context;
    private String email;

    private Switch textSwitch;
    private Switch appSwitch;
    private Switch emailSwitch;

    private TextView phoneNumberTV;
    private TextView editTV;
    private TextView changePasswordTV;
    private TextView logoutTV;
    private TextView deleteAccount;

    private Button saveBTN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();
        setContentView(R.layout.activity_account_page);
        setTitle("Account Settings");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        textSwitch = findViewById(R.id.textSwitch);
        appSwitch = findViewById(R.id.appSwitch);
        emailSwitch = findViewById(R.id.emailSwitch);
        phoneNumberTV = findViewById(R.id.phoneNumberTV);
        editTV = findViewById(R.id.editNumberTV);
        changePasswordTV = findViewById(R.id.changePasswordTV);
        logoutTV = findViewById(R.id.logoutTV);
        deleteAccount = findViewById(R.id.delete_account);
        saveBTN = findViewById(R.id.saveBTN);
        saveBTN.setVisibility(View.INVISIBLE);
        email = mAuth.getCurrentUser().getEmail();
        displaySettings();

        changePasswordTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                openDialog();
            }
        });

        deleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                deleteAccount();
            }
        });

        logoutTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                logout();
            }
        });

        editTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                openPhoneDialog();
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

    private void openPhoneDialog() {
        ChangeNumberDialog dialog = new ChangeNumberDialog();
        dialog.show(getSupportFragmentManager(), "changeNum dialog");
    }

    @Override
    public void applyTexts(String password, String password2) {
        if (password.equals(password2))
            changePassword(password);
        else
            Toast.makeText(getApplicationContext(), "Passwords do not match", Toast.LENGTH_LONG).show();
    }

    @Override
    public void applyChange(final String number, final String number2) {
        if (number.equals(number2)) {
            if (number.length() == 10) {
                phoneNumberTV.setText(number);
                savePhoneNumber(number);
            } else {
                Toast.makeText(getApplicationContext(), "Not a valid phone number", Toast.LENGTH_LONG).show();
            }
        }
        else
            Toast.makeText(getApplicationContext(), "Phone numbers do not match", Toast.LENGTH_LONG).show();
    }

    @Override
    public void ifDeleteAccount(Boolean ifDelete) {
        if (ifDelete) {
            ReEnterPasswordDialog dialog = new ReEnterPasswordDialog();
            dialog.show(getSupportFragmentManager(), "CONFIRM DELETE ACCOUNT");
        }
    }

    public void confirmDeleteAccount(String password) {
        AuthCredential credential = EmailAuthProvider
                .getCredential(email, password);

        user.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.d("DELETE ACCOUNT",
                                            "Account has successfully been deleted");
                                    Toast.makeText(
                                            context,
                                            "Account has successfully been deleted!",
                                            Toast.LENGTH_SHORT).show();
                                    sendTo(Login.class);
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("DELETE ACCOUNT",
                                        "User account FAILED to delete.");
                                Toast.makeText(
                                        context,
                                        "Something went wrong!",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("USER AUTHENTICATE", "Re-authentication failed!");
                        Toast.makeText(context, "Something went wrong!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void changePassword(String password) {
        user.updatePassword(password)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Password successfully changed", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void savePhoneNumber(String number) {
        userItemRef.update("phone_number", number)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull final Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Phone number Saved", Toast.LENGTH_SHORT).show();
                            displaySettings();
                        } else {
                            Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_LONG).show();
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
                    String number = documentSnapshot.getString("phone_number");
                    String formattedNumber = number.replaceFirst("(\\d{3})(\\d{3})(\\d+)", "($1) $2-$3");

                    phoneNumberTV.setText("Phone Number: " + formattedNumber);

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

    private void logout() {
        mAuth.signOut();
        sendTo(Login.class);
    }

    private void sendTo(Class name) {
        startActivity(new Intent(AccountPage.this, name));
        finish();
    }

    private void deleteAccount() {
        DeleteAccountDialog dialog = new DeleteAccountDialog();
        dialog.show(getSupportFragmentManager(), "DELETE ACCOUNT");
    }
}
