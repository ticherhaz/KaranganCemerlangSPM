package net.ticherhaz.karangancemerlangspm.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.View;

import net.ticherhaz.karangancemerlangspm.R;

public class CustomView extends View {

    private final Paint mPaint;
    private final PointF mCenter = new PointF();
    private int mLineColor = Color.BLACK;
    private int mRadius = 0;

    public CustomView(Context context) {
        this(context, null);
    }

    public CustomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomView);
        final int N = typedArray.getIndexCount();
        for (int i = 0; i < N; i++) {
            int attr = typedArray.getIndex(i);
            if (attr == R.styleable.CustomView_lineColor) {
                mLineColor = typedArray.getColor(attr, Color.BLACK);
            }
        }

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);//设置空心
        mPaint.setStrokeWidth(5);
        mPaint.setColor(mLineColor);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int width = 0;
        int height = 0;
        if (widthMode == MeasureSpec.EXACTLY && heightMode == MeasureSpec.EXACTLY) {
            width = MeasureSpec.getSize(widthMeasureSpec);
            height = MeasureSpec.getSize(heightMeasureSpec);
            mRadius = Math.min(width, height) / 2;
            mCenter.set(width / 2, height / 2);
        }
        setMeasuredDimension(width, height);
    }

    public void setLineColor(int color) {
        mLineColor = color;
        mPaint.setColor(mLineColor);
        invalidate();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(mCenter.x, mCenter.y, mRadius, mPaint);
    }
}
