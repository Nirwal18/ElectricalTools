package com.nirwal.electricaltools.ui.admin.uploadAssets;

import androidx.annotation.PluralsRes;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.OpenableColumns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.webkit.WebSettings;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.nirwal.electricaltools.R;
import com.nirwal.electricaltools.databinding.FragmentUploadAssetsBinding;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class UploadAssetsFragment extends Fragment {

    private UploadAssetsViewModel mViewModel;
    private FragmentUploadAssetsBinding _viewBinding;
    StorageReference _storageRef;
    FirebaseFirestore _dbRef;


    private static final int FILE_PICK_REQUEST = 369;
    private Uri _fileUri;

    public static UploadAssetsFragment newInstance() {
        return new UploadAssetsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        _viewBinding = FragmentUploadAssetsBinding.inflate(inflater,container, false);
        _viewBinding.chooseFileBtn.setOnClickListener(v -> {
            openFileChooser();
        });

        _viewBinding.uploadFileBtn.setOnClickListener(v -> {
            _viewBinding.uploadFileBtn.setEnabled(false);
            uploadFile();

        });



        return _viewBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(UploadAssetsViewModel.class);
        _storageRef = FirebaseStorage.getInstance().getReference();
        _dbRef = FirebaseFirestore.getInstance();

        // TODO: Use the ViewModel
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==FILE_PICK_REQUEST && resultCode == RESULT_OK
        && data!=null && data.getData()!=null){
            _fileUri = data.getData();
            loadPreview(_fileUri);
        }
    }

    private void openFileChooser(){
        Intent intent = new Intent();
        intent.setType("*/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, FILE_PICK_REQUEST);
    }

    private String getFileExtension(Uri uri){
        return MimeTypeMap.getSingleton()
                .getExtensionFromMimeType(
                        requireContext()
                                .getContentResolver()
                                .getType(uri)
                );
    }

    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = requireActivity().getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result.split("\\.")[0].trim();
    }


    private void loadPreview(Uri uri){
        _viewBinding.enterFileNameTxt.setText(getFileName(uri));
      String ext = getFileExtension(uri).toLowerCase().trim();
        if(ext.equals("jpeg") || ext.equals("jpg")
        || ext.equals("png") || ext.equals("gif")){
            showImagePreview(uri);
            return;
        }
        else if( ext.equals("html") || ext.equals("htm")
        || ext.equals("txt")){
            showHtmlPreview(uri);
            return;
        }

        showImagePreview(null);
    }

    private void showImagePreview(Uri uri){
        _viewBinding.htmlPlaceHolder.setVisibility(View.GONE);
        _viewBinding.filePlaceHolder.setVisibility(View.VISIBLE);
        if(uri==null){
            _viewBinding.filePlaceHolder.setImageResource(R.drawable.ic_file_24);
            return;
        }
        _viewBinding.filePlaceHolder.setImageURI(uri);
    }


    private void showHtmlPreview(Uri uri){
        _viewBinding.filePlaceHolder.setVisibility(View.GONE);
        _viewBinding.htmlPlaceHolder.setVisibility(View.VISIBLE);
        WebSettings webSetting = _viewBinding.htmlPlaceHolder.getSettings();
        webSetting.setBuiltInZoomControls(true);
        _viewBinding.htmlPlaceHolder.setWebViewClient(new WebViewClient());

        _viewBinding.htmlPlaceHolder.loadUrl(uri.toString());
    }

    private void showUnKnownPreview(){
        _viewBinding.htmlPlaceHolder.setVisibility(View.GONE);
        _viewBinding.filePlaceHolder.setVisibility(View.VISIBLE);
        _viewBinding.filePlaceHolder.setImageResource(R.drawable.ic_file_24);
    }



    private void uploadFile(){
        if(_fileUri==null){
            Snackbar.make(requireView(),"No file selected", Snackbar.LENGTH_LONG).show();
            return;
        }


        StorageReference fileRef= _storageRef.child(
                _viewBinding.spinner.getSelectedItem().toString()+
                        "/"+
                System.currentTimeMillis()+
                        "."+
                getFileExtension(_fileUri));

        fileRef.putFile(_fileUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        _viewBinding.fileUploadProgressBar.setProgress(0);
                        Toast.makeText(requireContext(), "Upload successful", Toast.LENGTH_LONG).show();

                        taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnSuccessListener(uri -> {
                            Upload upload =  new Upload(
                                    _viewBinding.enterFileNameTxt.getText().toString().trim(),
                                    uri.toString()
                            );

                            _dbRef.collection("Uploads").add(upload);
                        });


                        _viewBinding.uploadFileBtn.setEnabled(true);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(requireContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                        _viewBinding.uploadFileBtn.setEnabled(true);
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                        double progress = (100.0 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
                        _viewBinding.fileUploadProgressBar.setProgress((int)progress);
                    }
                });


    }

}