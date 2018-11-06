package edu.weber.favmovies;

import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class RegisterActivity extends AppCompatActivity {

    EditText etID, etName, etPassword;
    ImageView imageProfile;
    Button btnSignUp;
    Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etID = findViewById(R.id.id);
        etName = findViewById(R.id.name);
        etPassword = findViewById(R.id.password);
        imageProfile = findViewById(R.id.img_profile);
        btnSignUp = findViewById(R.id.sign_up);


        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signUp();
            }
        });

        imageProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadImg();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void signUp() {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(etID.getText().toString(),
                etPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    final String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    final StorageReference storageReference =
                            FirebaseStorage.getInstance().getReference().child("users").child(uid);

                    storageReference.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                            String imageUrl = task.toString();

                            User user = new User();
                            user.setName(etName.getText().toString());
                            user.setUid(etID.getText().toString());
                            user.setImageUrl(imageUrl);

                            FirebaseDatabase.getInstance().getReference().child("users").child
                                    (uid).setValue(user);

                        }
                    });
                } else {
                    Toast.makeText(RegisterActivity.this, "Error", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void uploadImg() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, 10);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 10 && resultCode == RESULT_OK) {
            imageProfile.setImageURI(data.getData());
            imageUri = data.getData();
        }
    }
}