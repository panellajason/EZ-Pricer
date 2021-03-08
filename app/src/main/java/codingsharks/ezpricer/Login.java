package codingsharks.ezpricer;

import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
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
    private NotificationController notificationController;

    private FirebaseAuth auth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sendCreateAccount = findViewById(R.id.createAccountPageButton);
        loginButton = findViewById(R.id.loginButton);
        emailInput = findViewById(R.id.emailInputField);
        passwordInput = findViewById(R.id.passwordInputField);

        //NOTIFICATIONTEST
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            notificationController = new NotificationController()
                    .setID("Login")
                    .setName("Login")
                    .setTextTitle("Test Title")
                    .setTextContent("This this message")
                    .setManagerID(1)
                    .setManager(getSystemService(NotificationManager.class))
                    .setContext(Login.this);
        }

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email = emailInput.getText().toString();
                final String password = passwordInput.getText().toString();
                LoginController loginControl = new LoginController(email, password);

                //NOTIFICATIONTEST
                notificationController.createNotification();


                if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
                    auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "Sign in Successful");
                                final Intent intent = new Intent(Login.this, Main.class);
                                startActivity(intent);
                                finish();
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
                final Intent intent = new Intent(Login.this, CreateAccount.class);
                startActivity(intent);
                finish();
            }
        });

    }
}
