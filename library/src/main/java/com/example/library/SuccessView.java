package com.example.library;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

public class SuccessView extends ResultView {

    private static final String TAG = "SuccessView";

    protected Animation mSuccessAnimation;

    protected boolean mIsArriveCenter;

    private int mStyle;

    public SuccessView(View view) {
        super(view);
        init();
    }


    public void init() {
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setAntiAlias(true);
    }

    @Override
    protected void config(ProgressLoadingView.Builder.Config config) {
        mStrokeColor = config.color;
        mStrokeBackgroundColor = config.backgroundColor;
        mStyle = config.style;
    }

    protected void initOptions() {
        mIsArriveCenter = false;
        mLeftRatio = 0.0f;
        mRightRatio = 0.0f;
        mLeftStartX = mLeftIntersectingX;
        mLeftStartY = calculateLeftY(mLeftStartX);
        mLeftEndX = mLeftStartX;
        mLeftEndY = mLeftStartY;
    }

    public void onDraw(Canvas canvas) {
        drawSuccess(canvas);
    }

    protected void drawSuccess(Canvas canvas) {
        if (mStyle == ProgressLoadingView.Builder.STYLE_ARC_ROTATE) {
            mPaint.setStyle(Paint.Style.STROKE);
        } else if (mStyle == ProgressLoadingView.Builder.STYLE_WATER_ROTATE) {
            mPaint.setStyle(Paint.Style.FILL);
        }
        mPaint.setStrokeWidth(mStrokeWidth);
        mPaint.setColor(mStrokeBackgroundColor);
        canvas.drawCircle(mHalfWidth, mHalfHeight, mHalfWidth - mHalfStrokeWidth, mPaint);

        mPaint.setColor(mStrokeColor);
        canvas.drawLine(realX(mLeftStartX), realY(mLeftStartY),
                realX(mLeftEndX), realY(mLeftEndY), mPaint);
        if (mIsArriveCenter) {
            canvas.drawLine(realX(mCenterX), realY(mCenterY),
                    realX(mRightEndX), realY(mRightEndY), mPaint);
            mPaint.setStyle(Paint.Style.FILL);
            canvas.drawCircle(realX(mCenterX), realY(mCenterY), mHalfStrokeWidth, mPaint);
        }
    }

    public Animation getAnimation() {
        initOptions();
        if (mSuccessAnimation != null)
            mSuccessAnimation.cancel();
        mSuccessAnimation = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                super.applyTransformation(interpolatedTime, t);
                //Log.e(TAG, "applyTransformation: ");
                if (0.0 <= interpolatedTime && 0.2 >= interpolatedTime) {
                    mLeftRatio = 1.0f - interpolatedTime * 5;
                    mLeftStartX = mLeftIntersectingX;
                    mLeftStartY = calculateLeftY(mLeftStartX);
                    mLeftEndX = mLeftIntersectingX * mLeftRatio;
                    mLeftEndY = calculateLeftY(mLeftEndX);
                    mView.invalidate();
                } else if (0.2 < interpolatedTime && 0.8 >= interpolatedTime) {
                    /**mLeftRatio = 1.0f - (0.5f + (interpolatedTime - 0.2f) * 5 / 12);
                     mLeftX = mLeftIntersectingX * mLeftRatio;
                     mLeftY = calculateLeftY(mLeftX);*/
                    mLeftRatio = 1.0f - (interpolatedTime - 0.2f) * 5 / 3;
                    mLeftStartX = mLeftIntersectingX * mLeftRatio;
                    mLeftStartY = calculateLeftY(mLeftStartX);
                    mLeftEndX = mCenterX;
                    mLeftEndY = mCenterY;

                    mIsArriveCenter = true;
                    mRightRatio = (interpolatedTime - 0.2f) * 5 / 3;
                    mRightEndX = mRightIntersectingX * mRightRatio;
                    mRightEndY = calculateRightY(mRightEndX);
                    mView.invalidate();
                } else {
                    mLeftRatio = (interpolatedTime - 0.8f) * 5 / 2;
                    mLeftStartX = mLeftIntersectingX * mLeftRatio;
                    mLeftStartY = calculateLeftY(mLeftStartX);
                    mLeftEndX = mCenterX;
                    mLeftEndY = mCenterY;

                    mRightRatio = 1 - (interpolatedTime - 0.8f) * 5 / 4;
                    mRightEndX = mRightIntersectingX * mRightRatio;
                    mRightEndY = calculateRightY(mRightEndX);
                    mView.invalidate();
                }
            }
        };
        mSuccessAnimation.setDuration(700);
        mSuccessAnimation.setStartOffset(100);
        return mSuccessAnimation;
    }

    @Override
    public void cancelAnimation() {
        if (mSuccessAnimation != null) {
            mSuccessAnimation.cancel();
        }
    }
}
