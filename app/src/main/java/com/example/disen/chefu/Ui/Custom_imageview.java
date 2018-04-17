package com.example.disen.chefu.Ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by disen on 3/17/2018.
 */

public class Custom_imageview extends android.support.v7.widget.AppCompatImageView {


    public Custom_imageview(Context context) {
        super(context);
    }

    public Custom_imageview(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public Custom_imageview(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int threeTwoheight = MeasureSpec.getSize(widthMeasureSpec) * 2 / 3;
        int threeTwoheightSpec = MeasureSpec.makeMeasureSpec(threeTwoheight,MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, threeTwoheightSpec);

    }
}
