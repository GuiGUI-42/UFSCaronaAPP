package com.example.ufscarona2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
public class FormLogin extends AppCompatActivity {
    private SharedPreferences pref;
    Button googleAuth;
    FirebaseAuth Auth;
    FirebaseDatabase database;
    GoogleSignInClient mGoogleSignInClient;

    int RC_SIGN_IN=20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_form_login);
        pref = getSharedPreferences("perfil", MODE_PRIVATE);
        Button Btn1 = findViewById(R.id.btnLogin);


        googleAuth = findViewById(R.id.btnGoogleAuth);
        Auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this,gso);

        Btn1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                int storedValue = pref.getInt("myInt", -1);
                FirebaseUser user = Auth.getCurrentUser();
                String userEmail = user.getEmail();
                Log.d("User Email", "Email: " + userEmail);
                if (storedValue==10){
                    Intent intent = new Intent(FormLogin.this, MainMotorista.class);
                    startActivity(intent);
                }

            }
        });
        googleAuth.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {



                    googleSignIn();


            }
        });
    }

    private void googleSignIn() {


        Intent intent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(intent,RC_SIGN_IN);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode ==RC_SIGN_IN){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuth(account.getIdToken());
            }
            catch(Exception e){

                Toast.makeText(this,e.getMessage(),Toast.LENGTH_SHORT).show();

            }
        }
    }
    private void firebaseAuth(String idToken){


            AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
            Auth.signInWithCredential(credential)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    FirebaseUser user = Auth.getCurrentUser();
                                    String userEmail = user.getEmail(); // Get the user's email
                                    Log.d("User Email", "Email: " + userEmail);
                                    HashMap<String, Object> map = new HashMap<>();
                                    map.put("id", user.getUid());
                                    map.put("name", user.getDisplayName());
                                    map.put("profile", user.getPhotoUrl().toString());
                                    map.put("email", userEmail);

                                    database.getReference().child("users").child(user.getUid()).setValue(map);
                                    SharedPreferences.Editor editor = pref.edit();
                                    editor.putString("userEmail", userEmail);
                                    editor.apply();
                                    Intent intent = new Intent(FormLogin.this, selecao.class);
                                    startActivity(intent);

                                } else {

                                    Toast.makeText(FormLogin.this, "algo esta errado", Toast.LENGTH_SHORT).show();

                                }



                        }

                    });
        }
    }






