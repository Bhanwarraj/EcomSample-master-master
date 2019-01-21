package com.allandroidprojects.ecomsample;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

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

//import com.google.firebase.quickstart.auth.R;

public class Login extends AppCompatActivity implements View.OnClickListener {


   // SignInButton button;
    FirebaseAuth mAuth;
    private static final int RC_SIGN_IN = 9001;
    private GoogleSignInClient mGoogleSignInClient;
/*
    private String name, email;

    private String photo;

    private Uri photoUri;
    private String idToken;

   // public  ;

*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
       // button = (SignInButton) findViewById(R.id.Glogin);
        findViewById(R.id.Glogin).setOnClickListener(this);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("1099185445947-pv8tcuqvd0msof2kn0hj8i2fb0b6081m.apps.googleusercontent.com")
                .requestEmail()
                .build();

        /*GoogleApiClient mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Toast.makeText(Login.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();*/

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        mAuth = FirebaseAuth.getInstance();

    }




    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
           /* if (requestCode == RC_SIGN_IN) {

                GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);

                if (result.isSuccess()) {

                    // Google Sign In was successful, save Token and a state then authenticate with Firebase

                    GoogleSignInAccount account = result.getSignInAccount();



                    idToken = account.getIdToken();



                    name = account.getDisplayName();

                    email = account.getEmail();

                    photoUri = account.getPhotoUrl();

                    photo = photoUri.toString();



                    // Save Data to SharedPreference

                    sharedPrefManager = new SharedPrefManager(mContext);

                    sharedPrefManager.saveIsLoggedIn(mContext, true);



                    sharedPrefManager.saveEmail(mContext, email);

                    sharedPrefManager.saveName(mContext, name);

                    sharedPrefManager.savePhoto(mContext, photo);



                    sharedPrefManager.saveToken(mContext, idToken);

                    //sharedPrefManager.saveIsLoggedIn(mContext, true);



                    AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);

                    firebaseAuthWithGoogle();

                } else {

                    // Google Sign In failed, update UI appropriately

                   // Log.e(TAG, "Login Unsuccessful. ");

                    Toast.makeText(this, "Login Unsuccessful", Toast.LENGTH_SHORT)

                            .show();

                }

            }*/
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {

              //  Toast.makeText(Login.this, "Toast1", Toast.LENGTH_SHORT).show();

                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
            //    Toast.makeText(Login.this, "Toast2", Toast.LENGTH_SHORT).show();
               firebaseAuthWithGoogle(account);
              //  Toast.makeText(Login.this, "Sign", Toast.LENGTH_SHORT).show();
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Toast.makeText(Login.this, "Sign failed", Toast.LENGTH_SHORT).show();

            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        //Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        //Toast.makeText(Login.this, "firebaseAuthWithGoogle", Toast.LENGTH_SHORT).show();
        // [START_EXCLUDE silent]
        //showProgressDialog();
        // [END_EXCLUDE]

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            //Log.d(TAG, "signInWithCredential:success");
                            Toast.makeText(Login.this, "Signin successful", Toast.LENGTH_SHORT).show();
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            //Log.w(TAG, "signInWithCredential:failure", task.getException());
                            //Snackbar.make(findViewById(R.id.main_layout), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                            Toast.makeText(Login.this, "Auth failed.", Toast.LENGTH_SHORT).show();

                            updateUI(null);
                        }

                        // [START_EXCLUDE]
                        //hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
    }

    private void updateUI(FirebaseUser user) {
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.Glogin) {
            signIn();
        }
    }
}



