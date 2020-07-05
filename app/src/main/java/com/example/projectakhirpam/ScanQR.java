package com.example.projectakhirpam;

import android.app.Activity;
import android.content.Context;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import me.dm7.barcodescanner.zxing.ZXingScannerView;


import com.google.zxing.Result;

import java.util.ArrayList;


public class ScanQR extends Fragment implements ZXingScannerView.ResultHandler{

    private Activity activity;
    private Context content;
    private ZXingScannerView zXingScannerView;
    private boolean flash;
    private boolean autoFokus;
    private ArrayList<Integer> SelectedIndices;
    private int CameraId = -1;
    private ViewGroup contentFrame;
    private static final String FLASH_STATE = "FLASH_STATE";
    private static final String AUTO_FOCUS_STATE = "AUTO_FOCUS_STATE";
    private static final String SELECTED_FORMATS = "SELECTED_FORMATS";
    private static final String CAMERA_ID = "CAMERA_ID";


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
        zXingScannerView = new ZXingScannerView(activity);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scan, container, false);
        contentFrame = view.findViewById(R.id.content_frame);
        activity = getActivity();
        zXingScannerView = new ZXingScannerView(activity);
        if (savedInstanceState != null){
            flash = savedInstanceState.getBoolean(FLASH_STATE, false);
            autoFokus = savedInstanceState.getBoolean(AUTO_FOCUS_STATE, false);
            SelectedIndices = savedInstanceState.getIntegerArrayList(SELECTED_FORMATS);
            CameraId =savedInstanceState.getInt(SELECTED_FORMATS, -1);
        } else{
            flash = false;
            autoFokus = true;
            SelectedIndices = null;
            CameraId = -1;
        }


        contentFrame.addView(zXingScannerView);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        zXingScannerView.setResultHandler(this);
        zXingScannerView.startCamera(CameraId);
        zXingScannerView.setFlash(flash);
        zXingScannerView.setAutoFocus(autoFokus);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(FLASH_STATE, flash);
        outState.putBoolean(AUTO_FOCUS_STATE, autoFokus);
        outState.putIntegerArrayList(SELECTED_FORMATS, SelectedIndices);
        outState.putInt(CAMERA_ID, CameraId);
    }

    @Override
    public void onPause() {
        super.onPause();
        zXingScannerView.stopCamera();
    }

    @Override
    public void handleResult(Result rawResult) {
        try {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(getActivity().getApplicationContext(), notification);
            r.play();
            Log.v("TAG", rawResult.getText()); // Prints scan results
            Log.v("TAG", rawResult.getBarcodeFormat().toString());
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setTitle("Scan Result");
            builder.setMessage(rawResult.getText());
            AlertDialog alert1 = builder.create();
            alert1.show();

            zXingScannerView.resumeCameraPreview(this);
        } catch (Exception e) {
        }
    }


}
