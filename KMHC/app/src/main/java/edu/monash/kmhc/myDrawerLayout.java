package edu.monash.kmhc;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.drawerlayout.widget.DrawerLayout;

public class myDrawerLayout extends DrawerLayout {
    public myDrawerLayout(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        widthMeasureSpec = MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.EXACTLY);
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(heightMeasureSpec), MeasureSpec.EXACTLY);

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

}
