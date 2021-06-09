package com.nirwal.electricaltools.ui.admin.home;

import android.graphics.Color;
import android.util.Log;
import android.view.View;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.FirebaseFirestore;
import com.nirwal.electricaltools.ui.model.CategoryItem;
import com.nirwal.electricaltools.ui.model.SliderItem;

import java.util.List;

public class AdminHomeViewModel extends ViewModel {

    private static final String TAG = "AdminHomeViewModel";

    public static final String CATEGORY = "CATEGORY";
    public static final String SLIDER = "SLIDER";
    private FirebaseFirestore _db;


    public AdminHomeViewModel() {
        _db = FirebaseFirestore.getInstance();
    }

    public void addCategoryToCloud(CategoryItem item, View view){
        Log.d(TAG, "addCategory: data adding start");

            _db.collection(CATEGORY).document(item.title).set(item)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "onSuccess: data written in firebase");
                            Snackbar.make(view,"Successful added: "+item.title, Snackbar.LENGTH_LONG)
                                    .setBackgroundTint(Color.GREEN)
                                    .show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(TAG, "onFailure: "+e.getMessage());
                            Snackbar.make(view,
                                    "Failed to add ("+item.title+") due to\nError: "+e.getMessage(),
                                    Snackbar.LENGTH_LONG)
                                    .setBackgroundTint(Color.RED)
                                    .show();

                        }
                    });

    }

    private void addSliderToCloud(SliderItem item){
        Log.d(TAG, "addSliderToCloud: Started");

            _db.collection(SLIDER).document().set(item)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "onSuccess: data wiritten in firebase");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(TAG, "onFailure: "+e.getMessage());

                        }
                    });
    }


}