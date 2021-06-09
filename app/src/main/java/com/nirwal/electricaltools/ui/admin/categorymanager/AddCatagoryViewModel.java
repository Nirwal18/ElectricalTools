package com.nirwal.electricaltools.ui.admin.categorymanager;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.nirwal.electricaltools.ui.model.CategoryItem;
import com.nirwal.electricaltools.ui.screen.Screen;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class AddCatagoryViewModel extends ViewModel {
    // TODO: Implement the ViewModel


    private MutableLiveData<List<Screen>> _screenList;

    public AddCatagoryViewModel() {
    }


    public MutableLiveData<List<Screen>> getScreenList(){
        if(_screenList==null)
        {
            _screenList = new MutableLiveData<>();
        }

        return _screenList;
    }

    public void addScreen(Screen item){
        if(getScreenList().getValue()==null)
        {
            getScreenList().setValue(new ArrayList<>());
        }

        List<Screen> list = getScreenList().getValue();
        list.add(item);

        getScreenList().setValue(list);

    }
}