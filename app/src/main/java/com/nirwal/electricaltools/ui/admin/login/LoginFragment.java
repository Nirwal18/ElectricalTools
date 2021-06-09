package com.nirwal.electricaltools.ui.admin.login;

import androidx.annotation.ColorInt;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.nirwal.electricaltools.R;
import com.nirwal.electricaltools.databinding.FragmentLoginBinding;
import com.nirwal.electricaltools.ui.dialogs.LoginDialog;

import java.util.concurrent.Executor;

public class LoginFragment extends Fragment {


    private FragmentLoginBinding _viewBinding;
    private LoginViewModel mViewModel;

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    private FirebaseAuth _auth;
    GoogleSignInClient _googleSignInClient;
    private static final int RC_G_SIGN_IN =3;

    private static final String TAG = "LoginFragment";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        _viewBinding = FragmentLoginBinding.inflate(inflater,container,false);

        _auth = FirebaseAuth.getInstance();


        // Configure Google Sign In
        initGSO();
        _viewBinding.gSignBtn.setOnClickListener(v -> {
           signUsingGoogle();
        });


        _viewBinding.loginUsingOtp.setOnClickListener(v1 -> {
            Log.d(TAG, "onCreateView: otp btn clicked");
            Navigation.findNavController(requireView()).navigate(R.id.action_loginFragment_to_loginOtpFragment);
        });



        return _viewBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FirebaseUser currentUser = _auth.getCurrentUser();
        updateUI(currentUser);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(LoginViewModel.class);


        // TODO: Use the ViewModel
    }

    private void updateUI(FirebaseUser user){
        if(user==null){
            return;
        }
        showSnackBar("Login Success", Color.GREEN);
        Navigation.findNavController(_viewBinding.getRoot()).navigate(R.id.adminHomeFragment);
    }
    

    private void signUsingGoogle(){
        // Configure Google Sign In
        Intent signInIntent = _googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_G_SIGN_IN);
    }

    /* Google sign object init*/
    private void initGSO(){
        Log.d(TAG, "initGSO: ");
        GoogleSignInOptions gso = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        _googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso);
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        _auth.signInWithCredential(credential)
                .addOnCompleteListener((Executor) this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = _auth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());

                            showSnackBar("Authentication Failed.", Color.RED);
                            updateUI(null);
                        }


                    }
                });
    }

    private void showSnackBar(String msg,@ColorInt int color ){
        Snackbar.make(requireView(),msg, Snackbar.LENGTH_LONG)
                .setBackgroundTint(color)
                .show();
    }

    private void signOut(){
        Log.d(TAG, "signOut: ");
        FirebaseAuth.getInstance().signOut();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case RC_G_SIGN_IN:{
                // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                try {
                    // Google Sign In was successful, authenticate with Firebase

                    GoogleSignInAccount account = task.getResult(ApiException.class);
                    //Log.d(TAG,"Login success"+account.getId());
                    showSnackBar("Login success"+account.getId(), Color.GREEN);

                    firebaseAuthWithGoogle(account.getIdToken());

                } catch (ApiException e) {
                    // Google Sign In failed, update UI appropriately
                    //Log.d(TAG, "Google sign in failed", e);

                    showSnackBar("Error: "+e.getMessage(), Color.RED);
                }
            }
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        _viewBinding = null;
        //_googleSignInClient = null;
        //_auth = null;
    }
}