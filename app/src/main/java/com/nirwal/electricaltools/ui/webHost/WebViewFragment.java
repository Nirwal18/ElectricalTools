package com.nirwal.electricaltools.ui.webHost;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

import androidx.fragment.app.Fragment;

import com.nirwal.electricaltools.MainActivity;
import com.nirwal.electricaltools.databinding.FragmentWebViewBinding;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WebViewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WebViewFragment extends Fragment {

    FragmentWebViewBinding _binding;
    private static final String TAG = "WebViewFragment";

    private static final String URI = "URI";

    private String _uri;


    public WebViewFragment() {
        // Required empty public constructor
    }


    public static WebViewFragment newInstance(String uri) {
        WebViewFragment fragment = new WebViewFragment();
        Bundle args = new Bundle();
        args.putString(URI, uri);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        if(getActivity() instanceof MainActivity){
//            ((MainActivity)getActivity()).addOnBackPressListener(this);
//        }

        if (getArguments() != null) {
            _uri = getArguments().getString(URI);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        _binding = FragmentWebViewBinding.inflate(inflater,container,false);
        initWebView();
        if(_uri!=null && !_uri.isEmpty()){
            _binding.webViewHost.loadUrl(_uri);
        }

        return _binding.getRoot();
    }


    private void initWebView() {
        WebSettings ws = _binding.webViewHost.getSettings();
        ws.setJavaScriptEnabled(true);
        ws.setAllowFileAccess(true);


        _binding.webViewHost.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);

                if(_binding.webProgressBar.getVisibility()== View.VISIBLE & newProgress==100){
                    _binding.webProgressBar.setVisibility(View.GONE);
                }

                if(_binding.webProgressBar.getVisibility()==View.GONE & newProgress<100){
                    _binding.webProgressBar.setVisibility(View.VISIBLE);
                }

                Log.d(TAG, "onProgressChanged: "+newProgress);
                _binding.webProgressBar.setProgress(newProgress);



            }


            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                if(requireActivity() instanceof  MainActivity) {

                    ((MainActivity)requireContext()).setMyTitle(title);
                }
            }
        });

        try {
            Log.d(TAG, "Enabling HTML5-Features");
            Method m1 = WebSettings.class.getMethod("setDomStorageEnabled", Boolean.TYPE);
            m1.invoke(ws, Boolean.TRUE);

            Method m2 = WebSettings.class.getMethod("setDatabaseEnabled", Boolean.TYPE);
            m2.invoke(ws, Boolean.TRUE);

            Method m3 = WebSettings.class.getMethod("setDatabasePath", String.class);
            m3.invoke(ws, "/data/data/" + requireContext().getPackageName() + "/databases/");

            Method m4 = WebSettings.class.getMethod("setAppCacheMaxSize", Long.TYPE);
            m4.invoke(ws, 1024*1024*8);

            Method m5 = WebSettings.class.getMethod("setAppCachePath", String.class);
            m5.invoke(ws, "/data/data/" + getContext().getPackageName() + "/cache/");

            Method m6 = WebSettings.class.getMethod("setAppCacheEnabled", Boolean.TYPE);
            m6.invoke(ws, Boolean.TRUE);

            Log.d(TAG, "Enabled HTML5-Features");
        }
        catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            Log.e(TAG, "Reflection fail", e);
        }

    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        _binding.webViewHost .destroy();
        _binding = null;

    }

//    @Override
//    public void onBackPress() {
//        if(_binding.webViewHost.canGoBack()) _binding.webViewHost.goBack();
//        Log.d(TAG, "onBackPress: ");
//    }
}