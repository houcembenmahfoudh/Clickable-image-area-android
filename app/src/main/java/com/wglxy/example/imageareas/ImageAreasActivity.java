package com.wglxy.example.imageareas;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

public class ImageAreasActivity extends Activity
        implements View.OnTouchListener {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        ImageView iv = findViewById(R.id.image);
        if (iv != null) {
            iv.setOnTouchListener(this);
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent ev) {
        boolean handledHere = false;

        final int action = ev.getAction();
        final int evX = (int) ev.getX();
        final int evY = (int) ev.getY();
        int nextImage = -1;
        ImageView imageView = v.findViewById(R.id.image);
        if (imageView == null) return false;

        Integer tagNum = (Integer) imageView.getTag();
        int currentResource = (tagNum == null) ? R.drawable.p2_ship_default : tagNum.intValue();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                if (currentResource == R.drawable.p2_ship_default) {
                    nextImage = R.drawable.p2_ship_pressed;
                    handledHere = true;
                } else handledHere = true;
                break;

            case MotionEvent.ACTION_UP:
                int touchColor = getHotspotColor(R.id.image_areas, evX, evY);
                int tolerance = 100;
                nextImage = R.drawable.p2_ship_default;
                if (closeMatch(Color.RED, touchColor, tolerance)) {
                    Log.d("xxxxxx", "red");
                    nextImage = R.drawable.p2_ship_alien;
                } else if (closeMatch(Color.BLUE, touchColor, tolerance)) {
                    Log.d("xxxxxx", "blue");
                    nextImage = R.drawable.p2_ship_powered;
                } else if (closeMatch(Color.YELLOW, touchColor, tolerance)) {
                    Log.d("xxxxxx", "yellow");
                    nextImage = R.drawable.p2_ship_no_star;
                } else if (closeMatch(Color.WHITE, touchColor, tolerance)) {
                    Log.d("xxxxxx", "white");
                    nextImage = R.drawable.p2_ship_default;
                }
                if (currentResource == nextImage) {
                    nextImage = R.drawable.p2_ship_default;
                }
                handledHere = true;
                break;
        }
        if (handledHere) {
            if (nextImage > 0) {
                imageView.setImageResource(nextImage);
                imageView.setTag(nextImage);
            }
        }
        return handledHere;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public int getHotspotColor(int hotspotId, int x, int y) {
        ImageView img = findViewById(hotspotId);
        if (img == null) {
            Log.d("xxxx", "Hot spot image not found");
            return 0;
        } else {
            img.setDrawingCacheEnabled(true);
            Bitmap hotspots = Bitmap.createBitmap(img.getDrawingCache());
            if (hotspots == null) {
                Log.d("xxxx", "Hot spot bitmap was not created");
                return 0;
            } else {
                img.setDrawingCacheEnabled(false);
                return hotspots.getPixel(x, y);
            }
        }
    }

    public boolean closeMatch(int color1, int color2, int tolerance) {
        if (Math.abs(Color.red(color1) - Color.red(color2)) > tolerance) return false;
        if (Math.abs(Color.green(color1) - Color.green(color2)) > tolerance) return false;
        if (Math.abs(Color.blue(color1) - Color.blue(color2)) > tolerance) return false;
        return true;
    }
}
