package com.example.disen.chefu.Ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.example.disen.chefu.R;

/**
 * Created by disen on 3/22/2018.
 */

public class Circle extends View {
    private Paint mCircleYellow;
    private Paint mCircleGray;
    Float drawUpto;

    private float mRadius;
    private RectF mArcBounds = new RectF();

    public Circle(Context context, Float drawUpto) {
        super(context);
        this.drawUpto = drawUpto;
        // create the Paint and set its color

    }

    public Circle(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initPaints();
    }

    public Circle(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    private void initPaints() {
        mCircleYellow = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCircleYellow.setStyle(Paint.Style.FILL);
        mCircleYellow.setColor(getResources().getColor(R.color.yellow));
        mCircleYellow.setStyle(Paint.Style.STROKE);
        mCircleYellow.setStrokeWidth(14 * getResources().getDisplayMetrics().density);
        //mCircleYellow.setStrokeCap(Paint.Cap.SQUARE);
        // mEyeAndMouthPaint.setColor(getResources().getColor(R.color.colorAccent));
        //mCircleYellow.setColor(Color.parseColor("#F9A61A"));

        mCircleGray = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCircleGray.setStyle(Paint.Style.FILL);
        mCircleGray.setColor(Color.GRAY);
        mCircleGray.setStyle(Paint.Style.STROKE);
        mCircleGray.setStrokeWidth(15 * getResources().getDisplayMetrics().density);
        mCircleGray.setStrokeCap(Paint.Cap.SQUARE);
        mCircleGray.setColor(getResources().getColor(R.color.gray));
        //mCircleGray.setColor(Color.parseColor("#76787a"));

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mRadius = Math.min(w, h) / 2f;

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int w = MeasureSpec.getSize(widthMeasureSpec);
        int h = MeasureSpec.getSize(heightMeasureSpec);

        int size = Math.min(w, h);
        setMeasuredDimension(size, size);
    }
    public void updatedrawUpto(Float f){
        drawUpto = f;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Float drawTo = 0f;
        if(drawUpto== null){
            drawUpto = 0f;
        }
        else{
            drawTo = (drawUpto*360)/100;
        }
        float mouthInset = mRadius / 3f;
        mArcBounds.set(mouthInset, mouthInset, mRadius * 2 - mouthInset, mRadius * 2 - mouthInset);
        canvas.drawArc(mArcBounds, 0f, 360f, false, mCircleGray);

        canvas.drawArc(mArcBounds, 0f, drawTo, false, mCircleYellow);

    }
}
