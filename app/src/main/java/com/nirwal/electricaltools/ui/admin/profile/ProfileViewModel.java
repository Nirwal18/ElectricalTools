package com.nirwal.electricaltools.ui.admin.profile;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileViewModel extends ViewModel {
    // TODO: Implement the ViewModel


    MutableLiveData<FirebaseUser> _userMutableLiveData;
    public ProfileViewModel() {
        _userMutableLiveData = new MutableLiveData<>();
        userStateListener();
    }

    private void userStateListener(){
        FirebaseAuth.getInstance().addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                //if(firebaseAuth.getCurrentUser()!=null){
                    _userMutableLiveData.postValue(firebaseAuth.getCurrentUser());
                //}

            }
        });
    }

    public LiveData<FirebaseUser> getCurrentUser(){
        return _userMutableLiveData;
    }





}