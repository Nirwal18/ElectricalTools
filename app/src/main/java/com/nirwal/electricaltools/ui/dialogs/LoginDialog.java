package com.nirwal.electricaltools.ui.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
import com.nirwal.electricaltools.Utils;

import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

public class LoginDialog extends AppCompatDialogFragment {
    private static final String TAG = "LoginDialog";
    public static final int TYPE_EMAIL_PASS=0;
    public static final int TYPE_PHONE=1;
    public static final int TYPE_EMAIL_LINK=2;


    LoginDialogListener _listener;
    EditText _email, _pass, _phone, _otp;
    Button _otpBtn;
    FirebaseAuth _auth = FirebaseAuth.getInstance();


    int _dialog_type=TYPE_EMAIL_PASS;

    public LoginDialog(int dialogType) {
        this._dialog_type = dialogType;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        View v;
        LayoutInflater inflater =  requireActivity().getLayoutInflater();
        switch (_dialog_type){
            case TYPE_EMAIL_PASS:{
                v = inflater.inflate(R.layout.dialog_login_input,null);
                _email = v.findViewById(R.id.login_phone_input);
                _pass = v.findViewById(R.id.login_pass_input);
                builder.setView(v);
            break;
            }
            case TYPE_PHONE:{
                v = inflater.inflate(R.layout.dialog_login_phone,null);
                _phone =v.findViewById(R.id.login_phone_input);
                _otp = v.findViewById(R.id.login_phone_otp_input);
                _otpBtn = v.findViewById(R.id.login_otp_btn);
                _otpBtn.setOnClickListener((v1 -> {
                    sendOTP(_phone.getText().toString());
                }));
                builder.setView(v);
                break;
            }
            case TYPE_EMAIL_LINK:{

                break;
            }


        }




        builder.setTitle("Login")
                .setNegativeButton("Cancel",null)
                .setPositiveButton("Login",null);

        return builder.create();
    }

    PhoneAuthProvider.OnVerificationStateChangedCallbacks _callbacks;
    String mVerificationId;
    PhoneAuthProvider.ForceResendingToken mResendToken;

    void sendOTP(String phone){

        _callbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verification without
                //     user action.
                Log.d(TAG, "onVerificationCompleted:" + credential);

                signInWithPhoneAuthCredential(credential);
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
                mVerificationId = verificationId;
                mResendToken = token;
            }
        };


        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(_auth)
                        .setPhoneNumber(phone)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this.requireActivity())                 // Activity (for callback binding)
                        .setCallbacks(_callbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        _auth.signInWithCredential(credential)
                .addOnCompleteListener((Executor) this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");

                            FirebaseUser user = task.getResult().getUser();
                            // Update UI
                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                            }
                        }
                    }
                });
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try{
            _listener = (LoginDialogListener) context;
        }
        catch (ClassCastException e){
            throw new ClassCastException(context.toString() +
                    "must implement LoginDialogListener");
        }
    }

    private void login(){
        if(Utils.isEmailValid(_email.getText().toString())){
            _email.setError("please input correct email address");
        }

        _listener.onSuccess(_email.toString(), _pass.toString());

    }

    public interface LoginDialogListener{
        void onSuccess(String email, String pass);
        void onCancel();

    }
}
