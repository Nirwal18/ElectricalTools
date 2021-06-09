package com.nirwal.electricaltools.ui.home;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.nirwal.electricaltools.ui.model.CategoryItem;
import com.nirwal.electricaltools.ui.model.SliderItem;
import com.nirwal.electricaltools.ui.screen.Screen;

import java.util.ArrayList;
import java.util.List;

public class HomeViewModel extends ViewModel {
    private static final String TAG = "HomeViewModel";
    public static final String CATEGORY = "CATEGORY";
    public static final String SLIDER = "SLIDER";

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private final MutableLiveData<List<CategoryItem>> _categoryList;
    private final MutableLiveData<List<SliderItem>> _sliderList;

    public HomeViewModel() {
        _categoryList = new MutableLiveData<>(new ArrayList<CategoryItem>());
        _sliderList = new MutableLiveData<>(new ArrayList<SliderItem>());

        fetchCategory();
        fetchSliderData();

    }


    public LiveData<List<CategoryItem>> getCategoryList() {
        return _categoryList;
    }

    public MutableLiveData<List<SliderItem>> getSliderList() {
        return _sliderList;
    }

    private void fetchCategory(){

        ArrayList<CategoryItem> items = new ArrayList<>();

        db.collection(CATEGORY)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                items.add(document.toObject(CategoryItem.class));
                            }
                            _categoryList.setValue(items);
                            Log.d(TAG, "onComplete: "+items.size());

                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    private void fetchSliderData(){
        ArrayList<SliderItem> items = new ArrayList<>();
        db.collection(SLIDER)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                items.add(document.toObject(SliderItem.class));
                            }
                            _sliderList.setValue(items);

                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });

    }





}