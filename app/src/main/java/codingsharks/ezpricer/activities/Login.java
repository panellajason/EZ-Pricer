package codingsharks.ezpricer.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import codingsharks.ezpricer.random.LoginController;
import codingsharks.ezpricer.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {
    private static final String TAG = "Login";

    private EditText emailInput;
    private EditText passwordInput;
    private Button loginButton;
    private TextView sendCreateAccount;

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

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email = emailInput.getText().toString();
                final String password = passwordInput.getText().toString();
                LoginController loginControl = new LoginController(email, password);

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

    }

    private void sendTo(Class name) {
        startActivity(new Intent(Login.this, name));
        finish();
    }
}
