package com.nirwal.electricaltools.ui.admin.categorymanager;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.nirwal.electricaltools.R;
import com.nirwal.electricaltools.databinding.FragmentAddCatagoryBinding;
import com.nirwal.electricaltools.ui.adaptors.CategoryAdaptor;
import com.nirwal.electricaltools.ui.adaptors.ScreenListAdaptor;
import com.nirwal.electricaltools.ui.adaptors.ScreenListAdaptor2;
import com.nirwal.electricaltools.ui.dialogs.AddScreenDialog;
import com.nirwal.electricaltools.ui.home.HomeViewModel;
import com.nirwal.electricaltools.ui.model.CategoryItem;
import com.nirwal.electricaltools.ui.screen.Screen;

import java.util.ArrayList;
import java.util.List;

public class AddCatagoryFragment extends Fragment implements
        AddScreenDialog.IOnUpdateListener,
        AddScreenDialog.IOnClickListener,
        ScreenListAdaptor2.IOnClickListener{

    private static final String TAG = "AddCatagoryFragment";
    public static final String arg1 = "CATEGORY_ITEM";
    private AddCatagoryViewModel _viewModel;
    private FragmentAddCatagoryBinding _viewBinding;
    private List<Screen> _screenList;
    private ScreenListAdaptor2 _adaptor;
    private CategoryItem _categoryItem;


    public static AddCatagoryFragment newInstance() {
        return new AddCatagoryFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        _categoryItem =  getArguments().getParcelable(arg1);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {


        _viewBinding = FragmentAddCatagoryBinding.inflate(inflater, container, false);


        _viewBinding.addScreenFab.setOnClickListener(v -> {
            openAddScreenDialog();
        });
        _viewBinding.addCatagorySaveBtn.setOnClickListener(v -> {
            saveCategory();
        });

        return _viewBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        _viewModel = new ViewModelProvider(this).get(AddCatagoryViewModel.class);
        _screenList = new ArrayList<>();

        _adaptor = new ScreenListAdaptor2(requireContext(), new ArrayList<Screen>());
        _viewBinding.addCataRecycler.setLayoutManager(new LinearLayoutManager(requireContext()));
        _viewBinding.addCataRecycler.setAdapter(_adaptor);
        _adaptor.addIonClickListener(this);


        //change observer
        _viewModel.getScreenList().observe(getViewLifecycleOwner(), screenList -> {
            if(screenList==null)return;
            _screenList = screenList;
            _adaptor.updateList(_screenList);


        });
        if(_categoryItem!=null){
            _viewBinding.addCatagoryTitle.setText(_categoryItem.title);
            _viewBinding.addCatagoryDesc.setText(_categoryItem.description);
            _screenList = _categoryItem.getScreens();
            _viewModel.getScreenList().setValue(_categoryItem.getScreens());
        }
    }


    private void openAddScreenDialog(){
        AddScreenDialog dialog = new AddScreenDialog();
        dialog.show(getChildFragmentManager(),"SCREEN INPUT DIALOG");
        dialog.addOnSaveClickListener(this);
    }

    private void saveCategory(){
        String title = _viewBinding.addCatagoryTitle.getText().toString();
        String desc = _viewBinding.addCatagoryDesc.getText().toString();

        if(_screenList.size()<7){
            Snackbar.make(requireView(), "Screen list item count should be more than 7", Snackbar.LENGTH_LONG)
                    .setBackgroundTint(Color.RED)
                    .show();
            return;
        }

        CategoryItem item = new CategoryItem(title,desc,_screenList);

        saveToFireStore(item);

    }

    void saveToFireStore(CategoryItem item){
        FirebaseFirestore.getInstance()
                .collection(HomeViewModel.CATEGORY)
                .document(item.title)
                .set(item)
        .addOnSuccessListener(documentReference -> {
            Snackbar.make(requireView(),"Successfully saved to firebase", Snackbar.LENGTH_LONG)
                .setBackgroundTint(Color.GREEN)
                .show();
        })
        .addOnFailureListener(e -> {
            Snackbar.make(requireView(),"Error :"+e.getMessage(), Snackbar.LENGTH_LONG)
                    .setBackgroundTint(Color.RED)
                    .show();
        }
        );
    }

    private void addScreen(Screen screen){
        _viewModel.addScreen(screen);
    }

    @Override
    public void onEdit(int position) {
        AddScreenDialog dialog = new AddScreenDialog(AddScreenDialog.MODE_UPDATE,_screenList.get(position),position);
        dialog.show(getChildFragmentManager(),"SCREEN INPUT DIALOG");
        dialog.addOnUpdateClickListener(this);
    }

    @Override
    public void onAddScreensList(int position) {
        //_screenList.get(position,screen)
        Screen screen = _screenList.get(position);
        AddScreenDialog dialog = new AddScreenDialog(AddScreenDialog.MODE_NEW_SUBLIST,null, position);
        dialog.show(getChildFragmentManager(),"SCREEN INPUT DIALOG");
        dialog.addOnSaveClickListener(this);

    }

    @Override
    public void onSave(Screen screen) {
        addScreen(screen);
    }


    @Override
    public void onUpdate(Screen screen, int position) {
        _screenList.set(position,screen);
        _adaptor.notifyDataSetChanged();
        //Log.d(TAG, "onEdit: update :"+ screen.getTitle());
        //Log.d(TAG, "onEdit: update :"+ _screenList.get(position).getTitle());
    }

    @Override
    public void onSaveSubList(Screen screen, int position) {
        List<Screen> screens = _screenList.get(position).getSubScreenList();
        screens.add(screen);

        Log.d(TAG, "onSaveSubList: screen:"+ screen.getTitle());
        Log.d(TAG, "onSaveSubList: size:"+screens.size());
        _screenList.get(position).setSubScreenList(screens);
        Log.d(TAG, "onSaveSubList: size c:"+_screenList.get(position).getSubScreenList().size());

    }


}