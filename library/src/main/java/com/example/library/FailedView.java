package com.example.library;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

public class FailedView extends ResultView {


    private static final String TAG = "FailedView";

    private Paint mPaint;

    private boolean mIsLeftFinished;

    private Animation mFailedAnimation;

    private float mRightStartX;
    private float mRightStartY;

    private int mErrorTextAlpha;
    private String mErrorContent;

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

    private void initOption() {
        mIsLeftFinished = false;
        mLeftStartX = mLeftIntersectingX;
        mLeftStartY = calculateLeftY(mLeftStartX);
        mLeftEndX = mLeftStartX;
        mLeftEndY = mLeftStartY;

        mRightRatio = 1.0f;
        mRightStartX = mRightIntersectingX * mRightRatio - 1.0f / 4.0f * mRightIntersectingX;
        mRightStartY = calculateRightY(mRightStartX);
        mRightEndX = mRightStartX;
        mRightEndY = calculateRightY(mRightEndY);

        mErrorTextAlpha = 0x00;
    }

    @Override
    protected void config(ProgressLoadingView.Builder.Config config) {
        mStrokeColor = config.color;
        mStrokeBackgroundColor = config.backgroundColor;
        mErrorContent = config.errorContent;
        mStyle = config.style;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawFailed(canvas);
    }

    private void drawFailed(Canvas canvas) {
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
        if (mIsLeftFinished) {
            canvas.drawLine(realX(mRightStartX), realY(mRightStartY),
                    realX(mRightEndX), realY(mRightEndY), mPaint);
            drawErrorText(canvas);
        }
    }

    private void drawErrorText(Canvas canvas) {
            mPaint.setTextSize(3 * mStrokeWidth);
            String content = mErrorContent;
            Rect bounds = new Rect();
            mPaint.getTextBounds(content, 0, content.length(), bounds);
            mPaint.setTextAlign(Paint.Align.CENTER);
            mPaint.setStrokeWidth(3);
            mPaint.setAlpha(mErrorTextAlpha);
            mPaint.setStyle(Paint.Style.FILL);
            canvas.drawText(content, mHalfWidth + mOffsetYRatio,
                    mHalfHeight - mOffsetYRatio * mHalfHeight, mPaint);
    }

    @Override
    protected Animation getAnimation() {
        initOption();
        mFailedAnimation = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                super.applyTransformation(interpolatedTime, t);
                if (0.0f < interpolatedTime && interpolatedTime <= 0.5f) {
                    mLeftEndX = (-1.0f * interpolatedTime * 3.0f + 1.0f)
                            * mLeftIntersectingX;//最后加上误差
                    mLeftEndY = calculateLeftY(mLeftEndX);
                    //Log.e(TAG, "applyTransformation: interpolatedTime = " + interpolatedTime);
                    //Log.e(TAG, "applyTransformation: mLeftStartX = " + mLeftStartX + " mRightStartX = " + mRightStartX);
                    //Log.e(TAG, "applyTransformation: mLeftEndX = " + mLeftEndX + " mRightEndX = " + mRightEndX);
                    mView.invalidate();
                } else if (0.5f < interpolatedTime && interpolatedTime <= 1.0f) {
                    mIsLeftFinished = true;

                    mLeftStartX = (1.0f - (interpolatedTime - 0.5f) / 2.0f)
                            * mLeftIntersectingX;
                    mLeftStartY = calculateLeftY(mLeftStartX);

                    mRightEndX = (-1.0f * (interpolatedTime - 0.5f) * 5.0f / 2.0f + 3.0f / 4.0f)
                            * mRightIntersectingX;//最后加上误差
                    mRightEndY = calculateRightY(mRightEndX);
                    //Log.e(TAG, "applyTransformation: interpolatedTime = " + interpolatedTime);
                    //Log.e(TAG, "applyTransformation: mLeftStartX = " + mLeftStartX + " mRightStartX = " + mRightStartX);
                    //Log.e(TAG, "applyTransformation: mLeftEndX = " + mLeftEndX + " mRightEndX = " + mRightEndX);
                    mErrorTextAlpha = ((int) ((interpolatedTime - 0.5f) * 2.0f)) * 0xFF;
                    mView.invalidate();
                }
            }
        };
        mFailedAnimation.setDuration(1000);
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
