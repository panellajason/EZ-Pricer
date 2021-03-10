package codingsharks.ezpricer.activities;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import codingsharks.ezpricer.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class CreateAccount extends AppCompatActivity {

    private EditText emailET, passwordET, confirmPasswordET;
    private Button createAccountBTN;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        setTitle("Create an Account");

        emailET = findViewById(R.id.emailET);
        passwordET = findViewById(R.id.passwordET);
        confirmPasswordET = findViewById(R.id.confirmPasswordET);
        createAccountBTN = findViewById(R.id.createBTN);

        createAccountBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final String email = emailET.getText().toString();
                final String password1 = passwordET.getText().toString();
                final String password2 = confirmPasswordET.getText().toString();

                if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password1) && !TextUtils.isEmpty(password2)) {

                    if (password1.equals(password2)) {

                        mAuth.createUserWithEmailAndPassword(email, password1).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull final Task<AuthResult> task) {
                                if (task.isSuccessful()) {

                                    // add user settings here

                                    final Intent i = new Intent(CreateAccount.this, Main.class);
                                    startActivity(i);
                                    finish();

                                } else {

                                    final String error = task.getException().getMessage();
                                    Toast.makeText(CreateAccount.this, "Try uninstalling app and reinstalling: " + error,
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } else {
                        Toast.makeText(CreateAccount.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(CreateAccount.this, "Missing information", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
