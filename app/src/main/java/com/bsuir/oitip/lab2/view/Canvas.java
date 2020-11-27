package com.bsuir.oitip.lab2.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bsuir.oitip.lab2.adapter.LayersAdapter;
import com.bsuir.oitip.lab2.model.PaintLayer;

import java.util.ArrayList;
import java.util.Vector;

public class Canvas extends View {
    public int layerPosition = 0;
    public ArrayList<PaintLayer> paints = new ArrayList<>();
    public PaintLayer.drawingtype type = PaintLayer.drawingtype.BRUSH;
    public LayersAdapter adapter = new LayersAdapter(this);
    public int color = Color.BLACK;

    public Canvas(Context context) {
        super(context);
        init();
    }

    public Canvas(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public Canvas(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public Canvas(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        setOnTouchListener(getTouchListener());
    }

    public void undo() {
        int i = this.layerPosition;
        if (i > 0) {
            this.layerPosition = i - 1;
            adapter.notifyDataSetChanged();
        }
        invalidate();
    }

    public void redo() {
        int size = this.paints.size();
        int i = this.layerPosition;
        if (size > i) {
            this.layerPosition = i + 1;
            adapter.notifyDataSetChanged();
        }
        invalidate();
    }

    public void onDraw(android.graphics.Canvas canvas) {
        int i = 0;
        while (i < this.paints.size() && i < this.layerPosition) {
            this.paints.get(i).draw(canvas);
            i++;
        }
    }

    public void setAdapter(RecyclerView recyclerView)
    {
        recyclerView.setAdapter(adapter);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
    }

    private View.OnTouchListener getTouchListener() {
        return new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    PaintLayer layer = new PaintLayer();
                    layer.type = type;
                    layer.paint = new Paint();
                    layer.paint.setColor(color);
                    layer.paint.setStyle(Paint.Style.STROKE);
                    layer.paint.setStrokeWidth(9.0f);
                    if(paints.size()!=layerPosition) {
                        paints.add( layerPosition,layer);
                        paints.subList(layerPosition+1,paints.size()).clear();
                    }
                    else
                        paints.add(layer);
                    layerPosition++;
                    adapter.notifyDataSetChanged();
                }
                paints.get(paints.size() - 1).onTouch(v, event);
                invalidate();
                return true;
            }
        };
    }
}
