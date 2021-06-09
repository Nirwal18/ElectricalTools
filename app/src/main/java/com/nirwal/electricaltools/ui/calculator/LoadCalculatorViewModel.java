package com.nirwal.electricaltools.ui.calculator;

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
import com.nirwal.electricaltools.R;
import com.nirwal.electricaltools.ui.adaptors.ImageTextItem;
import com.nirwal.electricaltools.ui.model.CategoryItem;
import com.nirwal.electricaltools.ui.model.Load;

import java.util.ArrayList;
import java.util.List;

public class LoadCalculatorViewModel extends ViewModel {
    private static final String TAG = "LoadCalculatorViewModel";


    public MutableLiveData<List<Load>>  loads = new MutableLiveData<>(new ArrayList<>());
    public MutableLiveData<List<ImageTextItem>> _loadTypes = new MutableLiveData<>(new ArrayList<>());
    public List<ImageTextItem> liveData;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private static final String LOAD_TYPES="LOAD_TYPES";

   public LoadCalculatorViewModel(){

        fetchLoadTypes();

    }


    private void fetchLoadTypes(){
        ArrayList<ImageTextItem> loadTypes = new ArrayList<>();

        db.collection(LOAD_TYPES)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                loadTypes.add(document.toObject(ImageTextItem.class));
                            }
                            _loadTypes.setValue(loadTypes);
                            liveData = new ArrayList<>(loadTypes);

                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    private void setLoadTypes(){
        List<ImageTextItem> list = new ArrayList<>();
        list.add(new ImageTextItem("Bulb", "https://firebasestorage.googleapis.com/v0/b/electricaltools-2021.appspot.com/o/icons%2Fcolor_bulb.png?alt=media&token=058d3565-3021-4446-943c-308e46280f26"));
        list.add(new ImageTextItem("Fan", "https://firebasestorage.googleapis.com/v0/b/electricaltools-2021.appspot.com/o/icons%2Ffan_icon.png?alt=media&token=2d84ab06-9e26-4a7c-837b-1072d7a22401"));
        list.add(new ImageTextItem("TV", "https://firebasestorage.googleapis.com/v0/b/electricaltools-2021.appspot.com/o/icons%2Fcolor_tv.jpg?alt=media&token=0cc7076f-3ab3-430b-8469-fc80a89babaf"));

        Log.d(TAG, "sendDummyCatagoryData: data adding start");

        for(ImageTextItem item : list){

            db.collection(LOAD_TYPES).document().set(item);

        }
    }


}
