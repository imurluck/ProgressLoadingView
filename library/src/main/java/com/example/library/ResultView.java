package com.example.library;

import android.graphics.Paint;
import android.util.Log;
import android.view.View;

public abstract class ResultView extends PartView {

    private static final String TAG = "ResultView";

    protected static final float DEFAULT_LEFT_RATIO = 1.0f;
    protected static final float DEFAULT_RIGHT_RATIO = 0.75f;

    protected static final int DEFAULT_STROKE_BACKGROUND_COLOR = 0x1100FF00;
    protected static final int DEFAULT_STROKE_COLOR = 0xFF00FF00;

    protected int mStrokeBackgroundColor = DEFAULT_STROKE_BACKGROUND_COLOR;
    protected int mStrokeColor = DEFAULT_STROKE_COLOR;

    protected float mOffsetYRatio = 1.0f / 3.0f;

    protected int mStrokeWidth;
    protected int mHalfStrokeWidth = mStrokeWidth;

    protected float mRadius;

    protected Paint mPaint;

    protected int mHalfWidth;
    protected int mHalfHeight;

    protected float mLeftIntersectingX;
    protected float mRightIntersectingX;
    protected float mLeftStartX;
    protected float mLeftStartY;
    protected float mLeftEndX;
    protected float mLeftEndY;
    protected float mRightEndX;
    protected float mRightEndY;
    protected float mCenterX;
    protected float mCenterY;

    protected float mLeftRatio;
    protected float mRightRatio;

    protected float mStartAngle;

    protected View mView;


    public ResultView(View view) {
        this.mView = view;
    }


    public void onSizeChanged(int w, int h, int oldw, int oldh) {
        mHalfHeight = w / 2;
        mHalfWidth = h / 2;
        Log.e(TAG, "onSizeChanged: mHalfWidth = " + mHalfWidth + " mHalfHeight = " + mHalfHeight);
        Log.e(TAG, "onSizeChanged: " + Thread.currentThread());
        //经过多次测试，这个比例比较美观
        mStrokeWidth = w / 20;
        mHalfStrokeWidth = mStrokeWidth / 2;
        mRadius = mHalfWidth - mHalfStrokeWidth;

        calculateLeftIntersectingPoint();
        calculateRightIntersectingPoint();
        calculateCenterIntersectingPoint();
        calculateStartAngle();
        /**mLeftX = mLeftIntersectingX * mLeftRatio;
        mLeftY = calculateLeftY(mLeftX);
        mRightX = mRightIntersectingX * mRightRatio;
        mRightY = calculateRightY(mRightX);*/
    }

    protected void calculateStartAngle() {
        float leftIntersectingY = calculateLeftY(mLeftIntersectingX);
        mStartAngle = (float) (-1 * Math.atan(mLeftIntersectingX / leftIntersectingY)
                * 180f / Math.PI - 90);
    }


    public float getStartAngle() {
        return mStartAngle;
    }


    protected float realX(float x) {
        return x + mHalfWidth;
    }

    protected float realY(float y) {
        return y + mHalfHeight;
    }

    protected void calculateLeftIntersectingPoint() {
        mLeftIntersectingX = (float) ((-1 * mOffsetYRatio * mRadius
                - Math.sqrt((2 - mOffsetYRatio * mOffsetYRatio) * mRadius * mRadius)) / 2);
    }

    protected float calculateLeftY(float leftX) {
        return leftX + mOffsetYRatio * mRadius;
    }

    protected void calculateRightIntersectingPoint() {
        mRightIntersectingX = (float) ((mOffsetYRatio * mRadius
                + Math.sqrt((2 - mOffsetYRatio * mOffsetYRatio) * mRadius * mRadius)) / 2);
    }

    protected float calculateRightY(float rightX) {
        return -1 * rightX + mOffsetYRatio * mRadius;
    }

    protected void calculateCenterIntersectingPoint() {
        mCenterX = 0;
        mCenterY = mOffsetYRatio * mRadius;
    }

    @Override
    public void setProgressListener(ProgressListener listener) {

    }
}
