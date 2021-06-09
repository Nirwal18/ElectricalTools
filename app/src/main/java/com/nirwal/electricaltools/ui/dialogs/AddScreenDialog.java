package com.nirwal.electricaltools.ui.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.appcompat.widget.AppCompatAutoCompleteTextView;

import com.nirwal.electricaltools.R;
import com.nirwal.electricaltools.ui.Constants;
import com.nirwal.electricaltools.ui.screen.Screen;

import java.util.ArrayList;


public class AddScreenDialog extends AppCompatDialogFragment {

    public static final int MODE_NEW = 0;
    public static final int MODE_UPDATE= 1;
    public static final int MODE_NEW_SUBLIST = 2;
    public static final int MODE_UPDATE_SUBLIST= 3;

    private int _currentMode=0;
    private Screen _screen;
    private int _position;

    private IOnClickListener _listener;
    private IOnUpdateListener _upListener;

    public AddScreenDialog() {
    }

    public AddScreenDialog(int currentMode,Screen screen, int position) {
        this._currentMode = currentMode;
        this._screen = screen;
        this._position = position;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        View v = getLayoutInflater().inflate(R.layout.dialog_add_screen,null);

        EditText screenTitle = v.findViewById(R.id.screen_tittle);
        EditText screenDesc =v.findViewById(R.id.screen_desc);
        EditText screenLogoUri = v.findViewById(R.id.screen_icon_url);
        AutoCompleteTextView types = v.findViewById(R.id.screen_types);
        AutoCompleteTextView uriInput = v.findViewById(R.id.screen_uri);


        types.setAdapter(new ArrayAdapter(
                requireContext(),
                R.layout.auto_complete_text_row,
                new String[]{"SCREEN", "HTML", "SCREEN LIST"})
        );

        if(_currentMode == MODE_UPDATE ||_currentMode==MODE_UPDATE_SUBLIST && _screen!=null){
            screenTitle.setText(_screen.getTitle());
            screenDesc.setText(_screen.getDescription());
            screenLogoUri.setText(_screen.getLogoUrl());
            types.setText(getUriTypeFromID(_screen.getUriType()));
            uriInput.setText(_screen.getUri());
        }

        types.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String screen_type = ((TextView)view).getText().toString();
                if (getUriType(screen_type) == Constants.URI_TYPE_SCREEN_LIST) {
                uriInput.setEnabled(false);
                uriInput.setText("");
                }
                else {
                    uriInput.setEnabled(false);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        AlertDialog.Builder  alertDialog =  new AlertDialog.Builder(requireActivity());

        alertDialog.setTitle("Add Screen");
        alertDialog.setView(v);
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton("Save", (dialog, which) -> {
            String title = screenTitle.getText().toString();
            String desc = screenDesc.getText().toString();
            String logoUrl = screenLogoUri.getText().toString();
            String uri = uriInput.getText().toString();

                switch (_currentMode){
                    case MODE_NEW:{
                        if(_listener==null){break;}

                            _listener.onSave(new Screen(
                                title,
                                desc,
                                logoUrl,
                                uri,
                                getUriType(types.getText().toString())));
                        break;
                    }

                    case MODE_UPDATE:{
                        if(_upListener==null)break;
                        _screen = new Screen(
                                title,
                                desc,
                                logoUrl,
                                uri,
                                getUriType(types.getText().toString()));
                            _upListener.onUpdate(_screen, _position);
                        break;
                    }

                    case MODE_NEW_SUBLIST:{
                        if(_listener==null){break;}

                        _listener.onSaveSubList(new Screen(
                                title,
                                desc,
                                logoUrl,
                                uri,
                                getUriType(types.getText().toString())),_position);
                        break;
                    }



                }


        });
        alertDialog.setNegativeButton("Cancel", (dialog, which) -> {
       // do nothing
        });


        return alertDialog.create();
    }


    private int getUriType(String screen_type){
        switch (screen_type) {
            case "SCREEN":
                return Constants.URI_TYPE_FRAGMENTS;
            case "HTML":
                return Constants.URI_TYPE_WEBVIEW_FRAG;
            case "SCREEN LIST":
                return Constants.URI_TYPE_SCREEN_LIST;

            default:return Constants.URI_TYPE_FRAGMENTS;
        }

    }

    private String getUriTypeFromID(int id){
        switch (id) {
            case Constants.URI_TYPE_FRAGMENTS:{
                return "SCREEN";
            }

            case Constants.URI_TYPE_WEBVIEW_FRAG:{
                return "HTML";
            }
            case Constants.URI_TYPE_SCREEN_LIST:
                return "SCREEN LIST";

            default:return "SCREEN";
        }

    }

    public void addOnSaveClickListener(IOnClickListener listener){
        _listener = listener;
    }

    public void addOnUpdateClickListener(IOnUpdateListener listener){
        _upListener = listener;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    public interface IOnClickListener{
        void onSave(Screen screen);
        void onSaveSubList(Screen screen, int position);

    }

    public interface IOnUpdateListener{
        void onUpdate(Screen screen, int position);
    }
}
