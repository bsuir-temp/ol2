package com.bsuir.oitip.lab2.model;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.MotionEvent;
import android.view.View;

public class PaintLayer {
    private static final int UNDEFINED = 0;
    public coord coord = new coord();
    public Paint paint;
    public Bitmap bitmap;
    public Path path = new Path();
    public drawingtype type = drawingtype.BRUSH;

    public enum drawingtype {
        BRUSH,
        LINE,
        RECT,
        CIRC,
        FILL,
        OPEN
    }

    public void onTouch(View v, MotionEvent event) {
        int action = event.getAction();
        if (action == MotionEvent.ACTION_DOWN) {
            this.coord.startX = event.getX();
            this.coord.startY = event.getY();
            this.path.reset();
            this.path.moveTo(this.coord.startX,this.coord.startY);
        } else if (action == MotionEvent.ACTION_UP)
        {
            this.coord.endX = event.getX();
            this.coord.endY = event.getY();
            this.path.lineTo(this.coord.endX,this.coord.endY);
        } else if(action == MotionEvent.ACTION_MOVE) {
            this.coord.endX = event.getX();
            this.coord.endY = event.getY();
            this.path.lineTo(this.coord.endX,this.coord.endY);
        }
    }

    public double calculateDistanceBetweenPoints(
            float x1,
            float y1,
            float x2,
            float y2) {
        float xDelta = x2 - x1;
        float yDelta = y2 - y1;
        return Math.sqrt(xDelta * xDelta + yDelta * yDelta);
    }

    public void draw(Canvas canvas) {
        if(type!=drawingtype.OPEN)
        if(this.coord.startX == UNDEFINED || this.coord.endX == UNDEFINED)return;
        switch (type)
        {
            case RECT:
                canvas.drawRect(this.coord.startX, this.coord.startY, this.coord.endX, this.coord.endY, this.paint);break;
            case CIRC:
                final float radius = Float.parseFloat(String.valueOf(calculateDistanceBetweenPoints(this.coord.startX,this.coord.startY,this.coord.endX,this.coord.endY)));
                canvas.drawCircle(this.coord.startX, this.coord.startY, radius, this.paint);break;
            case BRUSH:
                canvas.drawPath(this.path, this.paint);break;
            case LINE:
                canvas.drawLine(this.coord.startX, this.coord.startY, this.coord.endX, this.coord.endY, this.paint);break;
            case OPEN:
                canvas.drawBitmap(this.bitmap,0,0, paint);
        }
    }

    static class coord {
        public float endX = UNDEFINED;
        public float endY = UNDEFINED;
        public float startX = UNDEFINED;
        public float startY = UNDEFINED;
    }
}
