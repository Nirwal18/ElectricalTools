package com.nirwal.electricaltools;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toolbar;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.nirwal.electricaltools.databinding.ActivityMainBinding;
import com.nirwal.electricaltools.ui.Constants;
import com.nirwal.electricaltools.Interface.IOnBackPress;
import com.nirwal.electricaltools.ui.about.AboutFragment;
import com.nirwal.electricaltools.ui.screen.Screen;
import com.nirwal.electricaltools.ui.screenList.ScreenListFragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    public static final int NOTIFICATION_URI=1;
    public static String arg1 = "URI";
    private ActivityMainBinding _viewBinding;
    private List<IOnBackPress> _onBackPressListenerList;
    private NavController _navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _viewBinding = ActivityMainBinding.inflate(getLayoutInflater());

        setContentView(_viewBinding.getRoot());
        BottomNavigationView navView = _viewBinding.navView;

        MaterialToolbar toolbar = _viewBinding.topAppBar;
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);


        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        _navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        _navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {

                setMyTitle(destination.getLabel());

            }
        });

        NavigationUI.setupActionBarWithNavController(this, _navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, _navController);
        //getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.WHITE));

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {

            }
        });


        Log.d(TAG, "onCreate: "+getIntent().getIntExtra(arg1,0));
        if(getIntent().getIntExtra(arg1,0)==NOTIFICATION_URI){
            _navController.navigate(R.id.navigation_notifications);
        }

        FirebaseMessaging.getInstance().subscribeToTopic("ALL");
    }






    public void setMyTitle(CharSequence title) {
        if(getSupportActionBar()!=null) {
             getSupportActionBar().setTitle(title);
        }
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
                }

                _navController.navigate(R.id.action_navigation_home_to_screenListFragment,bundle);
                break;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_app_bar,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        Log.d(TAG, "onOptionsItemSelected: menu_top");
        switch (item.getItemId()) {
            case R.id.menu_top_about:{

                if(_navController.getCurrentDestination().getId()==R.id.aboutFragment) break;

                _navController.navigate(R.id.aboutFragment);
                Log.d(TAG, "onOptionsItemSelected: menu_top_about");
                break;
            }
            default:

        }
        return true;
    }

    @Override
    public void onBackPressed() {
        Log.d(TAG, "onBackPressed: triggerd");

        if(_onBackPressListenerList!=null){
            for (int i = _onBackPressListenerList.size(); i>=0 ; i--){
                _onBackPressListenerList.get(i).onBackPress();
                Log.d(TAG, "onBackPressed: " + i);
            }
        }

        super.onBackPressed();
    }

    public void addOnBackPressListener(IOnBackPress listener){
        if(_onBackPressListenerList == null){
            _onBackPressListenerList = new ArrayList<>();
        }
        _onBackPressListenerList.add(listener);

    }


}