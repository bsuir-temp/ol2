package com.bsuir.oitip.lab2;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bsuir.oitip.lab2.model.PaintLayer;
import com.bsuir.oitip.lab2.view.Canvas;
import com.jaredrummler.android.colorpicker.ColorPickerDialog;
import com.jaredrummler.android.colorpicker.ColorPickerDialogListener;
import com.jaredrummler.android.colorpicker.ColorShape;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity implements ColorPickerDialogListener {
    private static final int READ_REQUEST_CODE = 52;
    private static final int WRITE_REQUEST_CODE = 53;
    private Canvas canvas = null;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.canvas = findViewById(R.id.canvas);
        this.canvas.setAdapter((RecyclerView)findViewById(R.id.layers));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        super.onActivityResult(requestCode, resultCode, resultData);
        try {
            if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
                if (resultData != null) {
                    PaintLayer layer = new PaintLayer();
                    layer.bitmap = getBitmapFromUri(resultData.getData());
                    layer.type = PaintLayer.drawingtype.OPEN;
                    layer.paint = new Paint();
                    if (this.canvas.paints.size() != this.canvas.layerPosition) {
                        this.canvas.paints.add(this.canvas.layerPosition, layer);
                        this.canvas.paints.subList(this.canvas.layerPosition + 1, this.canvas.paints.size()).clear();
                    } else
                        this.canvas.paints.add(layer);
                    this.canvas.layerPosition++;
                    this.canvas.adapter.notifyDataSetChanged();
                }
            }else
            if (requestCode == WRITE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
                if (resultData != null) {
                    setBitmapUri(resultData.getData());
                }
            }
        }catch (Throwable th){
            th.printStackTrace();
            //error(th.getLocalizedMessage());
        }
    }

    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor = getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }

    public static Bitmap getBitmapFromView(View view) {
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(),Bitmap.Config.ARGB_8888);
        android.graphics.Canvas canvas = new android.graphics.Canvas(returnedBitmap);
        Drawable bgDrawable =view.getBackground();
        if (bgDrawable!=null)
            bgDrawable.draw(canvas);
        else
            canvas.drawColor(Color.WHITE);
        view.draw(canvas);
        return returnedBitmap;
    }

    private void setBitmapUri(Uri uri) {
        try {
            ParcelFileDescriptor pfd = this.getContentResolver().openFileDescriptor(uri, "w");
            FileOutputStream fileOutputStream = new FileOutputStream(pfd.getFileDescriptor());
            Bitmap temp = getBitmapFromView(this.canvas);
            temp.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
            pfd.close();
        } catch (Exception e) {
            e.printStackTrace();
            //error(e.getLocalizedMessage());
        }
    }

    public void undo(View view) { this.canvas.undo(); }
    public void redo(View view) { this.canvas.redo(); }
    public void torect(View view) { this.canvas.type = PaintLayer.drawingtype.RECT; }
    public void tocirc(View view) { this.canvas.type = PaintLayer.drawingtype.CIRC; }
    public void toline(View view) { this.canvas.type = PaintLayer.drawingtype.LINE; }
    public void tobrus(View view) { this.canvas.type = PaintLayer.drawingtype.BRUSH; }

    public void save(View view) {
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/jpeg");
        intent.putExtra(Intent.EXTRA_TITLE, "undefined.jpg");
        startActivityForResult(intent, WRITE_REQUEST_CODE);
    }

    public void open(View view) {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startActivityForResult(intent, READ_REQUEST_CODE);
    }

    public void picker(View view) {
        ColorPickerDialog.newBuilder()
                .setColor(this.canvas.color)
                .setDialogType(ColorPickerDialog.TYPE_PRESETS)
                .setAllowCustom(true)
                .setAllowPresets(true)
                .setColorShape(ColorShape.CIRCLE)
                .setDialogId(1)
                .show(this);
    }

    public void error(String text)
    {
        AlertDialog  alert = new AlertDialog.Builder(getBaseContext()).create();
        alert.setTitle("Error "+text);
        alert.show();
    }

    @Override
    public void onColorSelected(int dialogId, int color) { this.canvas.color = color; }

    @Override
    public void onDialogDismissed(int dialogId) { }
}
