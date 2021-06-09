package com.nirwal.electricaltools.ui.calculator;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.nirwal.electricaltools.R;
import com.nirwal.electricaltools.databinding.FragmentLoadCalculatorBinding;
import com.nirwal.electricaltools.ui.adaptors.AutoCompleteIconTextAdaptor;
import com.nirwal.electricaltools.ui.adaptors.ImageTextItem;
import com.nirwal.electricaltools.ui.dialogs.CustomAlertDialogBuilder;
import com.nirwal.electricaltools.ui.model.Load;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

public class LoadCalculatorFragment extends Fragment {
    private static final String TAG = "LoadCalculatorFragment";
    private FragmentLoadCalculatorBinding _binding;
    private LoadCalculatorViewModel _viewModel;
    private LoadListAdaptor _adaptor;
    private List<Load> _data;
    private MaterialAlertDialogBuilder _addLoadDialog;


    public LoadCalculatorFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static LoadCalculatorFragment newInstance() {
        LoadCalculatorFragment fragment = new LoadCalculatorFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        _viewModel = new ViewModelProvider(this).get(LoadCalculatorViewModel.class);
        _binding = FragmentLoadCalculatorBinding.inflate(inflater,container,false);



        _binding.fabAddNew.setOnClickListener((v -> {
         //   _viewModel.loads.setValue(_data);
            showDialog();

        }));


        _viewModel.loads.observe(getViewLifecycleOwner(), new Observer<List<Load>>() {
            @Override
            public void onChanged(List<Load> loads) {
                _data = loads;

                Log.d(TAG, "onChanged: Live list");
                if(_adaptor==null){
                    _adaptor = new LoadListAdaptor(loads);
                }else {
                    //_adaptor.updateData(loads);
                }

                _binding.recyclerLoadList.setAdapter(_adaptor);
                Log.d(TAG, "onChanged: Live list count "+loads.size());
                _binding.hintPlaceholder.setVisibility(loads.size()>0 ? View.GONE: View.VISIBLE);


                //_binding.recyclerLoadList.invalidate(); //not working
            }
        });

        _binding.fabLoadCalculate.setOnClickListener(v->{
            calculate();

        });

        initList();
        //getParentFragmentManager().popBackStack();
        return _binding.getRoot();
    }

    private void showDialog(){
        this._addLoadDialog = new CustomAlertDialogBuilder(getContext())
                .AddLoadInputDialog(getLoadTypes(), new CustomAlertDialogBuilder.OnClick() {
                    @Override
                    public void onPositiveBtnClick(Load load) {
                        addDataToList(load);
                        //_addLoadDialog = null;
                    }

                    @Override
                    public void onNegativeBtnClick() {

                    }
                });
        this._addLoadDialog.show();
    }


    public void calculate(){
        float result=0;
        List<Load> data = _viewModel.loads.getValue();
        for(int i=0; i<data.size();i++){
            switch (data.get(i).unit){
                case "mW":{
                    result = result + ((data.get(i).powerConsumption/1000) * data.get(i).quantity);
                    break;
                }

                case "W":{
                    result = result + (data.get(i).powerConsumption * data.get(i).quantity);
                    break;
                }

                case "KW":{
                    result = result + ((data.get(i).powerConsumption*1000) * data.get(i).quantity);
                    break;
                }

                default:{
                    result = 0;
                    break;
                }
            }

        }
        navigateToResultFragment(result);
    }

    private List<ImageTextItem> getLoadTypes(){
        //return _viewModel._loadTypes.getValue();
        return new ArrayList<>(_viewModel._loadTypes.getValue());
    }

    private void addDataToList(Load e){
        List<Load> l = _viewModel.loads.getValue();
        l.add(e);
        _viewModel.loads.setValue(l);
    }

    public void initList(){
        _binding.recyclerLoadList.setLayoutManager(new LinearLayoutManager(this.getContext()));
        _binding.recyclerLoadList.setAdapter(_adaptor== null? new LoadListAdaptor(_data):_adaptor);
    }

    private void navigateToResultFragment(Float result){
        Bundle bundle = new Bundle();
        bundle.putFloat("TOTAL_LOAD", result);
        NavHostFragment.findNavController(this)
                .navigate(R.id.action_loadCalculatorFragment_to_loadResultFragment,bundle);
    }


}

