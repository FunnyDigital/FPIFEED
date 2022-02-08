package com.bitvilltecnologies.fpifeed;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    Button Login_btn;

    private FirebaseAuth mAuth;

    EditText email ,password;

    ProgressBar progressBar;


    private FirebaseAuth.AuthStateListener mAuthlistener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Login_btn= (Button)findViewById(R.id.loginbtn);

        mAuth = FirebaseAuth.getInstance();

        email=(EditText)findViewById(R.id.emaileditext) ;
        password=(EditText)findViewById(R.id.passwordeditext) ;
        progressBar=(ProgressBar)findViewById(R.id.progressBar);

        final  String TAG ="ViewDatabase";

        mAuthlistener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null){
                    Log.d(TAG,"onAuthStateChanged:signed_in:"+user.getUid());
                    toastmessage("LOADING FEED.....");
                }else {

                }
            }
        };











        Login_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                userLogin();
            }
        });
    }

    private void userLogin() {


        String semail = email.getText().toString().trim();
        String spassword = password.getText().toString().trim();


        if (semail.isEmpty()) {

            email.setHint("EMAIL IS EMPTY");
            email.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(semail).matches()) {
            email.setText("type a vail email,NA computer science");
            email.requestFocus();
            return;
        }

        if (spassword.isEmpty()) {
            password.setHint("password is empty");
            password.requestFocus();
            return;
        }

        if (spassword.length() < 6) {
            password.setHint("MAX PASSWORD LENGTH IS 6");
            password.requestFocus();
            return;
        }

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Signing in.....");
        progressDialog.show();

        mAuth.signInWithEmailAndPassword(semail, spassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {
                    finish();
                    progressDialog.dismiss();
                    Intent intent = new Intent(LoginActivity.this, NewsFeed.class);
                    Toast.makeText(getApplicationContext(), "WELCOME", Toast.LENGTH_LONG).show();

                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);


                    startActivity(intent);


                } else {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });


    }


        @Override
        protected void onStart() {
            super.onStart();
            mAuth.addAuthStateListener(mAuthlistener);
            if (mAuth.getCurrentUser()!=null){
                finish();
                startActivity(new Intent(this,NewsFeed.class));
            }
        }




    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(mAuthlistener);
    }
    public void signup(View view) {
        Intent intent = new Intent(LoginActivity.this,SignupActivity.class );
        startActivity(intent);
    }
    private void toastmessage(String message){
        Toast.makeText(LoginActivity.this,message, Toast.LENGTH_LONG).show();
    }

}
