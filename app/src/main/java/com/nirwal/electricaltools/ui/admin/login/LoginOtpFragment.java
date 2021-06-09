package com.nirwal.electricaltools.ui.admin.login;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.nirwal.electricaltools.R;
import com.nirwal.electricaltools.databinding.FragmentLoginUsingOtpBinding;

import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginOtpFragment#//newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginOtpFragment extends Fragment implements FirebaseAuth.AuthStateListener {
    private static final String TAG = "LoginOtpFragment";

    private FragmentLoginUsingOtpBinding _viewBinding;

    FirebaseAuth _auth;
    String _verificationId;
    PhoneAuthProvider.ForceResendingToken _resendToken;
    private int OTP_TIME_OUT = 3000; //ms
    public LoginOtpFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        _auth = FirebaseAuth.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        _viewBinding = FragmentLoginUsingOtpBinding.inflate(inflater, container,false);
        _viewBinding.loginPhoneSendOtpBtn.setOnClickListener(v -> sendOtp());
        _viewBinding.loginPhoneSubmitOtp
                .setOnClickListener(v ->
                        submitOtpAndSignIn( _viewBinding.loginPhoneOtpInput.getText().toString())
                );
        return _viewBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        _auth.addAuthStateListener(this);
    }

    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        if(firebaseAuth.getCurrentUser()!=null){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Navigation.findNavController(requireView()).popBackStack();
                }
            },2000);

        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        _viewBinding = null;
        _callbacks = null;

    }

    PhoneAuthProvider.OnVerificationStateChangedCallbacks _callbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verification without
                //     user action.
                Log.d(TAG, "onVerificationCompleted:" + credential);
                submitOtpAndSignIn(credential.getSmsCode());
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Log.w(TAG, "onVerificationFailed", e);

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                }

                // Show a message and update the UI
            }

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                Log.d(TAG, "onCodeSent:" + verificationId);

                // Save verification ID and resending token so we can use them later
                _verificationId = verificationId;
                _resendToken = token;
                enableOtpUI();
            }
        };



    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        _auth.signInWithCredential(credential)
                .addOnCompleteListener( requireActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");

                            FirebaseUser user = task.getResult().getUser();
                            // Update UI


                            showSnackBar("Login success", Color.GREEN);

                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                showSnackBar("OTP not valid",Color.RED);
                                _viewBinding.loginPhoneSubmitOtp.setEnabled(true);
                            }
                        }
                    }
                });
    }



    private void sendOtp(){
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(_auth)
                        .setPhoneNumber(_viewBinding.loginPhoneInput.getText().toString())       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(requireActivity())                 // Activity (for callback binding)
                        .setCallbacks(_callbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);

            _viewBinding.textInputLayout.setEnabled(false);
            _viewBinding.loginPhoneSendOtpBtn.setEnabled(false);

    }

    private void showSnackBar(String msg,@ColorInt int color ){
        Snackbar.make(requireView(),msg, Snackbar.LENGTH_LONG)
                .setBackgroundTint(color)
                .show();
    }

    private void enableOtpUI(){
        _viewBinding.textInputLayout2.setVisibility(View.VISIBLE);
        _viewBinding.loginPhoneSubmitOtp.setVisibility(View.VISIBLE);
        _viewBinding.loginPhoneSendOtpBtn.setEnabled(true);
        _viewBinding.loginPhoneSendOtpBtn.setText("Resend OTP");

        showSnackBar("OTP sent to "+_viewBinding.loginPhoneInput.getText(),Color.DKGRAY);
    }


    private void submitOtpAndSignIn(String otp){
        _viewBinding.loginPhoneSubmitOtp.setEnabled(false);
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(_verificationId,otp);
        signInWithPhoneAuthCredential(credential);
    }



}