package com.nirwal.electricaltools.ui.screenList;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nirwal.electricaltools.MainActivity;
import com.nirwal.electricaltools.R;
import com.nirwal.electricaltools.Utils;
import com.nirwal.electricaltools.databinding.FragmentScreenListBinding;
import com.nirwal.electricaltools.ui.Constants;
import com.nirwal.electricaltools.ui.adaptors.ScreenListAdaptor;
import com.nirwal.electricaltools.ui.screen.Screen;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class ScreenListFragment extends Fragment implements ScreenListAdaptor.IOnScreenListClickListener {

    private List<Screen> _screenList;
    private String _title;
    public static final String arg1 ="SCREEN_LIST";
    public static final String arg2 ="TITLE";

    private FragmentScreenListBinding _viewBinding;
    private NavController _navController;

    public ScreenListFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _screenList =  (ArrayList<Screen>) getArguments().getSerializable(arg1);
        _title = getArguments().getString(arg2);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        _viewBinding =FragmentScreenListBinding.inflate(inflater,container,false);
        _viewBinding.screenListRecycler.setLayoutManager(new LinearLayoutManager(requireContext()));
        ScreenListAdaptor adaptor = new ScreenListAdaptor(requireContext(),_screenList);
        adaptor.addOnScreenListClickListener(this);
        _viewBinding.screenListRecycler.setAdapter(adaptor);
        return _viewBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        _navController = Navigation.findNavController(requireView());
        ((MainActivity)requireContext()).setMyTitle(_title);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        _viewBinding = null;
        //_navController = null;
    }

    @Override
    public void onListItemClicked(Screen screen) {
        navigateToURI(screen.getUriType(),screen.getUri(),screen);
    }

    public void navigateToURI(int uriType, String uri , @Nullable Screen data){


        switch (uriType){
            case Constants.URI_TYPE_FRAGMENTS: {
                _navController.navigate(Utils.getNavigationActionIdFromClassName(uri));
                break;
            }
            case Constants.URI_TYPE_WEBVIEW_FRAG:{
                Bundle bundle = new Bundle();
                bundle.putString("URI", uri);
                _navController.navigate(R.id.action_screenListFragment_to_webViewFragment,bundle);
                break;
            }
            case Constants.URI_TYPE_SCREEN_LIST:{
                Bundle bundle = new Bundle();

                if(data.getSubScreenList()!=null){
                    bundle.putSerializable(ScreenListFragment.arg1, (Serializable)data.getSubScreenList());
                    bundle.putString(ScreenListFragment.arg2,data.getTitle());
                }

                _navController.navigate(R.id.action_screenListFragment_self,bundle);
                break;
            }
        }
    }
}