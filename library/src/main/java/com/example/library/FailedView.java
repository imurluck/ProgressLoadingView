package com.example.library;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

public class FailedView extends ResultView {


    private static final String TAG = "FailedView";

    private Paint mPaint;


    private Animation mFailedAnimation;

    private float mRightStartX;
    private float mRightStartY;


    private int mStyle;

    public FailedView(View view) {
        super(view);
        init();
    }

    public void init() {
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setAntiAlias(true);
    }

    protected void initOptions() {
        Log.e(TAG, "initOptions: ");
        mLeftStartX = mLeftIntersectingX * 0.5f;
        mLeftStartY = calculateLeftY(mLeftStartX);
        mLeftEndX = mLeftStartX;
        mLeftEndY = mLeftStartY;

        mRightStartX = mRightIntersectingX * 0.5f;
        mRightStartY = calculateRightY(mRightStartX);
        mRightEndX = mRightStartX;
        mRightEndY = calculateRightY(mRightEndX);

    }

    @Override
    protected void config(ProgressLoadingView.Builder.Config config) {
        mStrokeColor = config.color;
        mStrokeBackgroundColor = config.backgroundColor;
        mStyle = config.style;
        mOffsetYRatio = 0.0f;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawFailed(canvas);
    }

    private void drawFailed(Canvas canvas) {
        Log.e(TAG, "drawFailed: ");
        if (mStyle == ProgressLoadingView.Builder.STYLE_ARC_ROTATE) {
            mPaint.setStyle(Paint.Style.STROKE);
        } else {
            mPaint.setStyle(Paint.Style.FILL);
        }
        mPaint.setStrokeWidth(mStrokeWidth);
        mPaint.setColor(mStrokeBackgroundColor);
        canvas.drawCircle(mHalfWidth, mHalfHeight, mHalfWidth - mHalfStrokeWidth, mPaint);

        mPaint.setColor(mStrokeColor);
        canvas.drawLine(realX(mLeftStartX), realY(mLeftStartY),
                realX(mLeftEndX), realY(mLeftEndY), mPaint);

        canvas.drawLine(realX(mRightStartX), realY(mRightStartY),
                    realX(mRightEndX), realY(mRightEndY), mPaint);
    }

    @Override
    protected Animation getAnimation() {
        initOptions();
        mFailedAnimation = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                super.applyTransformation(interpolatedTime, t);

                mLeftEndX = mLeftIntersectingX * 0.5f - interpolatedTime * mLeftIntersectingX;
                mLeftEndY = calculateLeftY(mLeftEndX);

                mRightEndX = mRightIntersectingX * 0.5f - interpolatedTime * mRightIntersectingX;
                mRightEndY = calculateRightY(mRightEndX);
                mView.invalidate();
            }
        };
        mFailedAnimation.setDuration(700);
        mFailedAnimation.setStartOffset(100);
        return mFailedAnimation;
    }

    @Override
    protected void cancelAnimation() {
        if (mFailedAnimation != null) {
            mFailedAnimation.cancel();
        }
    }
}
