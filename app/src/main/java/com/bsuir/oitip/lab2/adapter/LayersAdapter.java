package com.bsuir.oitip.lab2.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bsuir.oitip.lab2.R;
import com.bsuir.oitip.lab2.holder.LayerVH;
import com.bsuir.oitip.lab2.model.PaintLayer;
import com.bsuir.oitip.lab2.view.Canvas;

import java.util.List;


public class LayersAdapter extends RecyclerView.Adapter<LayerVH> {

    private Canvas canvas;
    public LayersAdapter(Canvas canvas) {
        this.canvas = canvas;
    }

    @NonNull
    @Override
    public LayerVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new LayerVH(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layer, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull LayerVH holder, int position) {
        holder.icon.setColorFilter(canvas.paints.get(position).paint.getColor());
        switch (canvas.paints.get(position).type)
        {
            case BRUSH:holder.icon.setImageResource(R.drawable.ic_brush);break;
            case CIRC:holder.icon.setImageResource(R.drawable.ic_circle);break;
            case RECT:holder.icon.setImageResource(R.drawable.ic_rect);break;
            case LINE:holder.icon.setImageResource(R.drawable.ic_line);break;
            case OPEN:holder.icon.setImageResource(R.drawable.ic_open);break;
        }
    }

    @Override
    public int getItemCount() {
        return canvas.layerPosition;
    }
}
