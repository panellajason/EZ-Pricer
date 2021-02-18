package codingsharks.ezpricer;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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
