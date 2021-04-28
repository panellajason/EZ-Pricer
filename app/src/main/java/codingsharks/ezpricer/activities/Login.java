package codingsharks.ezpricer.activities;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import codingsharks.ezpricer.R;
import codingsharks.ezpricer.random.NotificationController;

public class Login extends AppCompatActivity {
    private static final String TAG = "Login";

    private EditText emailInput;
    private EditText passwordInput;
    private Button loginButton;
    private TextView sendCreateAccount;
    private NotificationController notificationController;

    private TextView forgotPassword;
    private EditText emailText;
    private FirebaseAuth auth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle("Login");

        sendCreateAccount = findViewById(R.id.createAccountPageButton);
        loginButton = findViewById(R.id.loginButton);
        emailInput = findViewById(R.id.emailInputField);
        passwordInput = findViewById(R.id.passwordInputField);
        forgotPassword = findViewById(R.id.forgotPasswordButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email = emailInput.getText().toString();
                final String password = passwordInput.getText().toString();

                if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
                    auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "Sign in Successful");
                                sendTo(Main.class);
                            } else {
                                Toast.makeText(Login.this, "Credentials do not exist", Toast.LENGTH_SHORT).show();
                                final String error = task.getException().getMessage();
                                Log.d(TAG, "Error Message: " + error);
                            }
                        }
                    });
                } else {
                    Toast.makeText(Login.this, "Missing Information", Toast.LENGTH_SHORT).show();
                }
            }
        });

        sendCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this, CreateAccount.class));
            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
                LayoutInflater inflater = Login.this.getLayoutInflater();
                View view2 = inflater.inflate(R.layout.dialog_forgotpassword, null);
                builder.setView(view2).setTitle("Forgot Password").setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String email = String.valueOf(emailText.getText());
                        Log.i("email is", email);
                        try{
                            auth.sendPasswordResetEmail(email);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        AlertDialog alertDialog = new AlertDialog.Builder(Login.this).create();
                        alertDialog.setTitle("Confirmation");
                        alertDialog.setMessage("Please check your e-mail to reset your password");
                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        alertDialog.show();
                    }
                });
                builder.create();
                emailText = view2.findViewById(R.id.emailText_passwordReset);
                builder.show();

            }
        });



    }

    private void sendTo(Class name) {
        startActivity(new Intent(Login.this, name));
        finish();
    }
}
