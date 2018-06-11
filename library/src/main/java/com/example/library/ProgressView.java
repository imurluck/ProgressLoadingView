package com.example.library;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;


public class ProgressView extends PartView {

    private static final String TAG = "ProgressView";

    private int mHalfWidth;
    private int mHalfHeight;

    private static final int DEFAULT_STROKE_BACKGROUND_COLOR = 0x1100FF00;
    private static final int DEFAULT_STROKE_COLOR = 0xFF00FF00;

    private int mStrokeBackgroundColor = DEFAULT_STROKE_BACKGROUND_COLOR;
    private int mStrokeColor = DEFAULT_STROKE_COLOR;
    private float mStrokeWidth;
    private float mHalfStrokeWidth;

    private float mRadius;

    private View mView;

    private Paint mPaint;

    private RectF mRectF;

    private Animation mProgressAnimation;
    private ProgressListener mListener;

    private float mDefaultStartAngle = 0;
    private float mStartAngle;
    private float mMaxLength;
    private float mLength;
    private int mCycleCount;

    private boolean mIsCancel;

    private int mProgress;

    private int mStyle;

    public ProgressView(View view) {
        this.mView = view;
        init();
    }

    public void init() {
        mPaint = new Paint();
        mMaxLength = 90;
        mLength = 0;
        mStartAngle = 0;
        mCycleCount = 1;
        mIsCancel = false;
    }

    @Override
    protected void config(ProgressLoadingView.Builder.Config config) {
        mStrokeColor = config.color;
        mStrokeBackgroundColor = config.backgroundColor;
        mStyle = config.style;
    }

    @Override
    public void onSizeChanged(int w, int h, int oldW, int oldH) {
        mHalfWidth = w / 2;
        mHalfHeight = h / 2;
        mStrokeWidth = w / 20;
        mHalfStrokeWidth = mStrokeWidth / 2;
        mRadius = mHalfWidth - mHalfStrokeWidth;
        mRectF = new RectF(0.0f + mHalfStrokeWidth, 0.0f + mHalfStrokeWidth,
                w - mHalfStrokeWidth, h - mHalfStrokeWidth);
    }

    @Override
    public void onDraw(Canvas canvas) {
        drawProgress(canvas);
        drawProgressText(canvas, mProgress);
    }

    public void setProgress(int progress) {
        mProgress = progress;
    }

    private void drawProgress(Canvas canvas) {
        if (mStyle == ProgressLoadingView.Builder.STYLE_ARC_ROTATE) {
            mPaint.setStyle(Paint.Style.STROKE);
        } else if (mStyle == ProgressLoadingView.Builder.STYLE_WATER_ROTATE) {
            mPaint.setStyle(Paint.Style.FILL);
        }
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(mStrokeWidth);
        mPaint.setColor(mStrokeBackgroundColor);
        canvas.drawCircle(mHalfWidth, mHalfHeight, mRadius, mPaint);

        mPaint.setColor(mStrokeColor);
        canvas.drawArc(mRectF, mStartAngle, mLength, false, mPaint);
    }

    public void setDefaultStartAngle(float angle) {
       this.mDefaultStartAngle = angle;
       mStartAngle = mDefaultStartAngle;
    }

    @Override
    public Animation getAnimation() {
        mStartAngle = mDefaultStartAngle;
        mIsCancel = false;
        if (mProgressAnimation != null) {
            mProgressAnimation.cancel();
        }
        mProgressAnimation = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                super.applyTransformation(interpolatedTime, t);
                //Log.e(TAG, "applyTransformation: isCancel = " + mIsCancel);
                if (mIsCancel) {
                    if (mStartAngle == mDefaultStartAngle) {
                        //cancel();
                        if (mListener != null) {
                            mListener.finished();
                        }
                    }
                }
                mStartAngle = (-1 * 360f * interpolatedTime + mDefaultStartAngle) % 360;
                mLength = (float) Math.abs(Math.sin(Math.PI * interpolatedTime * mCycleCount))
                        * mMaxLength;
                //Log.e(TAG, "applyTransformation: mLength = " + mLength + " interlodatedTime = " + interpolatedTime);
                mView.invalidate();
            }
        };
        mProgressAnimation.setDuration(1000);
        //mProgressAnimation.setStartOffset(100);
        mProgressAnimation.setRepeatCount(Animation.INFINITE);
        return mProgressAnimation;
    }

    @Override
    public void cancelAnimation() {
        mIsCancel = true;
    }

    public void setProgressListener(ProgressListener listener) {
        if (listener != null) {
            this.mListener = listener;
        } else {
            Log.w(TAG, "progressListener is null, you maybe get a incorrect display");
        }
    }

    private void drawProgressText(Canvas canvas, int progress) {
        mPaint.setTextSize(3 * mStrokeWidth);
        String content = progress + "%";
        Rect bounds = new Rect();
        mPaint.getTextBounds(content, 0, content.length(), bounds);
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setStrokeWidth(3);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawText(content, mHalfWidth, mHalfHeight + bounds.height() / 2, mPaint);
    }

}
