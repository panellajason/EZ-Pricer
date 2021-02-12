package codingsharks.ezpricer;

import android.content.Intent;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class StartUp extends AppCompatActivity {

    private static int SPLASH_TIMEOUT = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup);



        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                final Intent splash = new Intent(StartUp.this, Main.class);
                startActivity(splash);
                finish();
            }
        }, SPLASH_TIMEOUT);
    }
}
