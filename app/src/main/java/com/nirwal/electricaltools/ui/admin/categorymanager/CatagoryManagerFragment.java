package com.nirwal.electricaltools.ui.admin.categorymanager;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.nirwal.electricaltools.R;
import com.nirwal.electricaltools.databinding.FragmentCatagoryManagerBinding;
import com.nirwal.electricaltools.ui.adaptors.CategoryAdaptor;
import com.nirwal.electricaltools.ui.model.CategoryItem;

import java.util.ArrayList;
import java.util.List;

public class CatagoryManagerFragment extends Fragment implements CategoryAdaptor.IOnEditPnlListener {

    private CatagoryManagerViewModel _viewModel;
    private FragmentCatagoryManagerBinding _viewBinding;
    private List<CategoryItem> _categoryItemList;
    private CategoryAdaptor _categoryAdaptor;

    public static CatagoryManagerFragment newInstance() {
        return new CatagoryManagerFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        _viewBinding = FragmentCatagoryManagerBinding.inflate(inflater, container, false);
        _viewBinding.addCatagoryFab.setOnClickListener(v -> {
            Navigation.findNavController(requireView())
                    .navigate(R.id.action_catagoryManagerFragment_to_addCatagoryFragment);
        });


        _viewBinding.catagoryListRecycler.setLayoutManager(new LinearLayoutManager(requireContext()));

        return _viewBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        _viewModel = new ViewModelProvider(this).get(CatagoryManagerViewModel.class);

        if(_categoryItemList==null) _categoryItemList = new ArrayList<>();

        _categoryAdaptor = new CategoryAdaptor(requireContext(),_categoryItemList, true);
        _categoryAdaptor.addOnEditPNLClickListener(this);

        _viewModel.getCategoryList().observe(getViewLifecycleOwner(), new Observer<List<CategoryItem>>() {
            @Override
            public void onChanged(List<CategoryItem> categoryItems) {
                _categoryItemList=categoryItems;
                if(_categoryAdaptor!=null) {
                    _viewBinding.catagoryListRecycler.setAdapter(_categoryAdaptor);
                    _categoryAdaptor.updateData(_categoryItemList);
                }
            }
        });
        // TODO: Use the ViewModel
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        _viewBinding = null;
        _viewModel = null;
    }

    @Override
    public void onEditClick(CategoryItem categoryItem) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(AddCatagoryFragment.arg1, categoryItem);
        Navigation.findNavController(requireView())
                .navigate(R.id.action_catagoryManagerFragment_to_addCatagoryFragment,bundle);
    }

    @Override
    public void onDeleteClick(CategoryItem categoryItem) {
        _viewModel.deleteCategoryItem(categoryItem, new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Snackbar.make(requireView(),categoryItem.title + " deleted",Snackbar.LENGTH_LONG)
                        .setBackgroundTint(Color.GREEN)
                        .show();
                _viewModel.fetchCategory();
            }
        });
    }
}