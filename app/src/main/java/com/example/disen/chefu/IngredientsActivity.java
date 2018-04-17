package com.example.disen.chefu;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.example.disen.chefu.AutomatedEmails.SendMail;
import com.example.disen.chefu.Ui.IngredientAdapter;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Arrays;

public class IngredientsActivity extends AppCompatActivity {
    final int Request_code = 11;
    private FirebaseAuth mAuth;
    String userEmail;
    Snackbar snackbar;
    View view;
    private static final int RC_SIGN_IN = 123;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredients);
        view = findViewById(R.id.ingredients_root);
        if(getIntent()!= null){
            ArrayList<String>ingredients = getIntent().getStringArrayListExtra("ingredients");
            final String link = getIntent().getStringExtra("shareAs");
            final String url = getIntent().getStringExtra("url");
            IngredientAdapter ingredientAdapter = new IngredientAdapter(this,ingredients);
            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.ingredient_recyclv);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setAdapter(ingredientAdapter);

            mAuth = FirebaseAuth.getInstance();
            mAuthListener = new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    if(user!= null){
                        userEmail = user.getEmail();
                    }
                    else{
                        startActivityForResult(
                                AuthUI.getInstance()
                                        .createSignInIntentBuilder()
                                        .setIsSmartLockEnabled(false)
                                        .setAvailableProviders(Arrays.asList(
                                                new AuthUI.IdpConfig.GoogleBuilder().build()))
                                        .build(),
                                RC_SIGN_IN);
                    }
                }
            };

            final FloatingActionButton share = (FloatingActionButton)findViewById(R.id.share_action);
            share.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                @Override
                public void onClick(View view) {
                    //sign in
                    shareRecipe(userEmail,link);
                }
            });
            FloatingActionButton browser = findViewById(R.id.browser);
            browser.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                }
            });
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Request_code) {
            if (resultCode == RESULT_OK) {
                //get the email so it's send it to the user email
                sendEmail();
            }
        }
        if(requestCode == RC_SIGN_IN) {

            if (resultCode == RESULT_OK) {
                snackbar = Snackbar.make(view, getString(R.string.signedIn), Snackbar.LENGTH_LONG);
                snackbar.show();
            } else {
                snackbar = Snackbar.make(view, getString(R.string.again), Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        }
    }
    public void shareRecipe(String userEmail,String link){
        if(userEmail!= null){
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.shared)+link);
        sendIntent.setType("text/plain");
        startActivityForResult(sendIntent,Request_code);}
    }
    public void sendEmail(){

        SendMail sm = new SendMail(this, userEmail, getString(R.string.from), getString(R.string.automated_email));
        //Executing sendmail to send email
        sm.execute();
    }
}
