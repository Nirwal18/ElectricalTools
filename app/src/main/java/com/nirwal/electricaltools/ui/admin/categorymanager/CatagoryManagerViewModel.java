package com.nirwal.electricaltools.ui.admin.categorymanager;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.nirwal.electricaltools.ui.home.HomeViewModel;
import com.nirwal.electricaltools.ui.model.CategoryItem;
import com.nirwal.electricaltools.ui.model.SliderItem;

import java.util.ArrayList;
import java.util.List;

public class CatagoryManagerViewModel extends ViewModel {
    private static final String TAG = "CatagoryManagerViewMode";

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private final MutableLiveData<List<CategoryItem>> _categoryList;


    public CatagoryManagerViewModel() {
        _categoryList = new MutableLiveData<>(new ArrayList<CategoryItem>());
        fetchCategory();
    }

    public LiveData<List<CategoryItem>> getCategoryList() {
        return _categoryList;
    }
    public void fetchCategory(){

        ArrayList<CategoryItem> items = new ArrayList<>();

        db.collection(HomeViewModel.CATEGORY)
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

    public void deleteCategoryItem(CategoryItem item,OnSuccessListener<Void> listener){
        db.collection(HomeViewModel.CATEGORY).document(item.title).delete().addOnSuccessListener(listener);
    }



}