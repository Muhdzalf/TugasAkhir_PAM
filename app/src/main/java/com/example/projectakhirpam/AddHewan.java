package com.example.projectakhirpam;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.hardware.camera2.params.BlackLevelPattern;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.Writer;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.WriteAbortedException;
import java.lang.ref.Reference;
import java.lang.reflect.Array;
import java.util.Arrays;

import static android.text.TextUtils.isEmpty;

public class AddHewan extends Fragment {

    private EditText inputid, inputnama, inputras, inputno ;
    private ImageView imgView;
    private Button btnsave;
    private Button saveData;
    private Activity activity;
    private Bitmap generateBitmap;
    private String filename;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add, container, false);
        activity = getActivity();

        imgView = view.findViewById(R.id.outputBarcode);
        inputid = view.findViewById(R.id.inputText);
        btnsave = view.findViewById(R.id.save);
        saveData = view.findViewById(R.id.saveData);



        //inisiasi ID
        inputnama = view.findViewById(R.id.inputText2);
        inputras = view.findViewById(R.id.inputText3);
        inputno = view.findViewById(R.id.inputText4);

        inputid.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0){
                    imgView.setImageResource(R.drawable.ic_baseline_image_24);
                }else {
                    try {
                        generateBarcode(s.toString());
                    } catch (WriterException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        saveData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference reference = database.getReference();

                // save input user
                String getKode = inputid.getText().toString();
                String getNama  = inputnama.getText().toString();
                String  getRas = inputras.getText().toString();
                String getTlp = inputno.getText().toString();

                if(isEmpty(getKode) || isEmpty(getNama) || isEmpty(getRas) || isEmpty(getTlp)){

                    Toast.makeText(getActivity(),"Data Tidak Boleh Kosong", Toast.LENGTH_SHORT).show();
                }else{
                    reference.child("Hewan").push().setValue(new DataHewan(getKode,getNama,getRas, getTlp))
                            .addOnSuccessListener(activity, new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    inputid.setText("");
                                    inputnama.setText("");
                                    inputras.setText("");
                                    inputno.setText("");
                                }
                            });
                }
            }
        });


        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveImage(generateBitmap);
            }
        });

    return view;
    }





    public  void saveImage (Bitmap generatedBitmap){
        FileOutputStream out = null;
        File file = new File(Environment.getExternalStorageDirectory().getPath(), "QRCode");
        if (!file.exists()){
            file.mkdir();
        }if (filename.contains("/")){
            filename = filename.replace("/", "\\");
        }
        String filepath = (file.getAbsolutePath() + "/" + filename + ".png");
        try {
            out = new FileOutputStream(filepath);
            generatedBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        Toast.makeText(activity, "File Saved at\n" + filepath, Toast.LENGTH_SHORT).show();
    }

    private void generateBarcode (String s) throws WriterException {
        filename = s;
        BitMatrix result;
        result = new MultiFormatWriter().encode(s, BarcodeFormat.QR_CODE, 1080, 1080, null);

        int w = result.getWidth();
        int h =  result.getHeight();
        int [] pixels = new  int[w * h];

        for (int y = 0; y<h; y++){
            int offset = y * w;
            for (int x = 0; x<w; x++){
                pixels[offset + x] = result.get(x, y) ? Color.BLACK : Color.WHITE;
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, 1080, 0,0, w, h);
        generateBitmap = bitmap;
        imgView.setImageBitmap(bitmap);

    }
}
