package edu.weber.favmovies;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class RegisterActivity extends AppCompatActivity {

    EditText etID, etName, etPassword;
    ImageView imageProfile;
    Button btnSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etID = findViewById(R.id.id);
        etName = findViewById(R.id.name);
        etPassword = findViewById(R.id.password);
        imageProfile = findViewById(R.id.img_profile);
        btnSignUp = findViewById(R.id.sign_up);
    }

    @Override
    protected void onResume() {
        super.onResume();

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signUp();
            }
        });

        imageProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                
            }
        });
    }

    private void signUp() {

    }
}
