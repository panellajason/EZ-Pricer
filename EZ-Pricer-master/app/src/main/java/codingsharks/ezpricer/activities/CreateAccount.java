package codingsharks.ezpricer.activities;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import codingsharks.ezpricer.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class CreateAccount extends AppCompatActivity {

    private EditText emailET, passwordET, confirmPasswordET;
    private TextView alreadyHaveAccountTV;
    private Button createAccountBTN;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference itemRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        setTitle("Create an Account");

        emailET = findViewById(R.id.emailET);
        passwordET = findViewById(R.id.passwordET);
        confirmPasswordET = findViewById(R.id.confirmPasswordET);
        createAccountBTN = findViewById(R.id.createBTN);
        alreadyHaveAccountTV = findViewById(R.id.alreadyLoginTV);

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

                                    db = FirebaseFirestore.getInstance();
                                    itemRef = db.collection("user_settings");

                                    final Map<String, String> map = new HashMap<>();
                                    map.put("phone_number", "000-000-0000");
                                    map.put("text_notification", "0");
                                    map.put("app_notification", "0");
                                    map.put("email_notification", "0");

                                    itemRef.document(mAuth.getUid()).set(map);

                                    startActivity(new Intent(CreateAccount.this, AccountPage.class));
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

        alreadyHaveAccountTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                startActivity(new Intent(CreateAccount.this, Login.class));
                finish();
            }
        });
    }
}
