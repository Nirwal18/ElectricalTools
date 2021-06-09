package com.nirwal.electricaltools.ui.home;

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
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nirwal.electricaltools.R;
import com.nirwal.electricaltools.Utils;
import com.nirwal.electricaltools.ui.Constants;
import com.nirwal.electricaltools.ui.adaptors.CategoryAdaptor;
import com.nirwal.electricaltools.ui.adaptors.SliderAdapter;
import com.nirwal.electricaltools.databinding.FragmentHomeBinding;
import com.nirwal.electricaltools.ui.model.SliderItem;
import com.nirwal.electricaltools.ui.screen.Screen;
import com.nirwal.electricaltools.ui.screenList.ScreenListFragment;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.io.Serializable;
import java.util.List;

public class HomeFragment extends Fragment implements View.OnClickListener, CategoryAdaptor.IOnButtonClickListener {

    private FragmentHomeBinding _binding;
    private HomeViewModel homeViewModel;
    private NavController _navController;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        _binding = FragmentHomeBinding.inflate(inflater);

        initSlider();
        initAds();
        initCategoryRecycler();
        return _binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        _navController = Navigation.findNavController(requireView());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
//            case R.id.btn_inverter_calculator:{
//                InverterCalculatorListDialogFragment dialogFragment = InverterCalculatorListDialogFragment.newInstance(2);
//                dialogFragment.show(getChildFragmentManager(),"bootom sheet");
//                break;
//            }
            default:break;
        }
    }



    /** image slider for moving image on home fragment
     * */
    private void initSlider(){

        SliderView sliderView = _binding.imageSlider;
        SliderAdapter adapter = new SliderAdapter(getContext(),homeViewModel.getSliderList().getValue());

        sliderView.setSliderAdapter(adapter);
        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM); //set indicator animation by using IndicatorAnimationType. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        sliderView.setIndicatorSelectedColor(Color.WHITE);
        sliderView.setIndicatorUnselectedColor(Color.GRAY);
        sliderView.setScrollTimeInSec(4); //set scroll delay in seconds :
        sliderView.startAutoCycle();

        homeViewModel.getSliderList().observe(getViewLifecycleOwner(), new Observer<List<SliderItem>>() {
            @Override
            public void onChanged(List<SliderItem> sliderItems) {
                adapter.renewItems(sliderItems);
            }
        });

    }

    private void initAds(){
//
//        AdRequest adRequest = new AdRequest.Builder().build();
//        ((AdView)mRoot.findViewById(R.id.adView)).loadAd(adRequest);
    }

    private void initCategoryRecycler(){
        RecyclerView recyclerView = _binding.getRoot().findViewById(R.id.recyclerView_catagory_container);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        CategoryAdaptor categoryAdaptor = new CategoryAdaptor(
                requireActivity(),
                homeViewModel.getCategoryList().getValue()
        );
        categoryAdaptor.addOnButtonClickListener(this);
        recyclerView.setAdapter(categoryAdaptor);

        homeViewModel.getCategoryList().observe(getViewLifecycleOwner(), categoryItems -> categoryAdaptor.updateData(categoryItems));

    }

    @Override
    public void onFeatureBtnClick(Screen screen) {
        navigateToURI(screen.getUriType(), screen.getUri(), screen);
    }

    @Override
    public void onSeeAllBtnClicked(String title, List<Screen> screenList) {
        Screen screen =  new Screen(
                title,
                "",
                "",
                "",
                Constants.URI_TYPE_SCREEN_LIST,
                screenList);


        navigateToURI(screen.getUriType(), screen.getUri(), screen);
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
                _navController.navigate(R.id.action_navigation_home_to_webViewFragment,bundle);
                break;
            }
            case Constants.URI_TYPE_SCREEN_LIST:{
                Bundle bundle = new Bundle();

                if(data.getSubScreenList()!=null){
                    bundle.putSerializable(ScreenListFragment.arg1, (Serializable)data.getSubScreenList() );
                    bundle.putString(ScreenListFragment.arg2, data.getTitle());
                }

                _navController.navigate(R.id.action_navigation_home_to_screenListFragment,bundle);
                break;
            }
        }
    }
}