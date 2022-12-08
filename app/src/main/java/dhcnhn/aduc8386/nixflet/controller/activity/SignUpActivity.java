package dhcnhn.aduc8386.nixflet.controller.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuthEmailException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;
import dhcnhn.aduc8386.nixflet.R;
import dhcnhn.aduc8386.nixflet.helper.FirebaseAuthHelper;
import dhcnhn.aduc8386.nixflet.helper.FirebaseDatabaseHelper;
import dhcnhn.aduc8386.nixflet.helper.FirebaseStorageHelper;
import dhcnhn.aduc8386.nixflet.helper.SharedPreferencesHelper;
import dhcnhn.aduc8386.nixflet.model.User;

public class SignUpActivity extends AppCompatActivity {

    private Uri profileUri;

    private EditText editTextFullName;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private EditText editTextConfirmPassword;
    private Button buttonRegister;
    private CircleImageView circleImageviewProfile;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        bindView();
    }

    private void bindView() {
        editTextFullName = findViewById(R.id.edittext_sign_up_user_name);
        editTextEmail = findViewById(R.id.edittext_sign_up_email);
        editTextPassword = findViewById(R.id.edittext_sign_up_password);
        editTextConfirmPassword = findViewById(R.id.edittext_sign_up_confirm_password);
        buttonRegister = findViewById(R.id.button_sign_up_register_button);
        circleImageviewProfile = findViewById(R.id.circle_imageview_sign_up_user_profile);

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fullName = editTextFullName.getText().toString().trim();
                String email = editTextEmail.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();
                String confirmPassword = editTextConfirmPassword.getText().toString().trim();

                if (fullName.isEmpty()) {
                    editTextFullName.setError("Full Name is required");
                    editTextFullName.requestFocus();
                    return;
                }
                if (email.isEmpty()) {
                    editTextEmail.setError("Email is required");
                    editTextEmail.requestFocus();
                    return;
                }
                if (password.isEmpty()) {
                    editTextPassword.setError("Password is required");
                    editTextPassword.requestFocus();
                    return;
                }
                if (confirmPassword.isEmpty()) {
                    editTextConfirmPassword.setError("Password is required");
                    editTextConfirmPassword.requestFocus();
                    return;
                }
                if (!confirmPassword.equals(password)) {
                    editTextConfirmPassword.setError("Confirm password is not match");
                    editTextConfirmPassword.requestFocus();
                    return;
                }

                registerNewUser(fullName, email, password);
            }
        });

        circleImageviewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

    }

    private void registerNewUser(String fullName, String email, String password) {
        FirebaseAuthHelper.getInstance().createUserWithEmailAndPassword(email, password).addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {


                if (task.isSuccessful()) {
                    Toast.makeText(SignUpActivity.this, "Sign up successful", Toast.LENGTH_SHORT).show();

                    if (profileUri == null) {
                        profileUri = Uri.parse("android.resource://dhcnhn.aduc8386.nixflet/drawable/user_avatar_holder");
                    }

                    User user = new User(FirebaseAuthHelper.getCurrentUser().getUid(), fullName, email, profileUri.toString());

                    setUserFirebase(user);

                } else {
                    if (task.getException() != null) {
                        try {
                            throw task.getException();
                        } catch (FirebaseAuthWeakPasswordException e) {
                            editTextPassword.setError(e.getMessage());
                            editTextPassword.requestFocus();
                        } catch (FirebaseAuthUserCollisionException | FirebaseAuthInvalidCredentialsException e) {
                            editTextEmail.setError(e.getMessage());
                            editTextEmail.requestFocus();
                        } catch (Exception e) {
                            Log.d("TAG", "onComplete: " + e);
                        }
                    } else {
                        Log.d("TAG", "onComplete: something went wrong");
                    }
                }
            }
        });
    }

    private void setUserFirebase(User user) {
        FirebaseDatabaseHelper.getUserReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                FirebaseDatabaseHelper.getUserReference().child(user.getId()).setValue(user, (error, ref) -> {
                    FirebaseStorageHelper.setProfilePicture(Uri.parse(user.getProfilePicture()));
                    Log.d("TAG", "onComplete: user data upload successful");
                    Toast.makeText(SignUpActivity.this, "Login to continue", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("TAG", "onCancelled: user data upload failed");
                Toast.makeText(SignUpActivity.this, "User data upload failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void selectImage() {
        activityResultLauncher.launch("image/*");
    }

    ActivityResultLauncher<String> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri result) {
                    if (result != null) {
                        profileUri = result;
                        circleImageviewProfile.setImageURI(result);
                    }
                }
            });
}
