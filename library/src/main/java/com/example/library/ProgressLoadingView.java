package com.example.library;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class ProgressLoadingView extends View implements ProgressListener {

    private static final String TAG = "ProgressSuccessView";

    public static final int RESULT_SUCCESS = 0;
    public static final int RESULT_FAILED = 1;

    private SuccessView mSuccessView;
    private ProgressView mProgressView;
    private FailedView mFaileddView;

    private State mCurrentState;
    private State mResult;

    private Builder.Config mConfig;

    enum State {
        PROGRESS,
        SUCCESS,
        FAILED
    }

    public ProgressLoadingView(Context context) {
        super(context);
    }

    public ProgressLoadingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProgressLoadingView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.LoadingView);
        Builder.Config config = new Builder.Config();
        config.context = context;
        config.color = array.getColor(R.styleable.LoadingView_color, Builder.DEFAULT_COLOR);
        config.backgroundColor = array.getColor(R.styleable.LoadingView_backgroundColor,
                Builder.getBackgroundColor(config.color));
        config.style = array.getInt(R.styleable.LoadingView_styleName, Builder.DEFAULT_STYLE);
        config(config);
    }

    private ProgressLoadingView(Context context, Builder.Config config) {
        super(context);
        config(config);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Log.e(TAG, "onSizeChanged: width = " + w + " height = " + h);
        mSuccessView.onSizeChanged(w, h, oldw, oldh);
        mProgressView.onSizeChanged(w, h, oldw, oldw);
        mFaileddView.onSizeChanged(w, h, oldw, oldh);
    }


    private void init() {
        mCurrentState = State.PROGRESS;
        mResult = State.SUCCESS;
        mSuccessView = new SuccessView(this);
        mFaileddView = new FailedView(this);
        mProgressView = new ProgressView(this);
        mProgressView.setProgressListener(this);
    }

    private void config(Builder.Config config) {
        init();
        this.mConfig = config;
        mProgressView.config(config);
        mSuccessView.config(config);
        mFaileddView.config(config);
    }

    public void setProgress(int progress) {
        mProgressView.setProgress(progress);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //Log.e(TAG, "onDraw: " + Thread.currentThread());
        if (mCurrentState == State.PROGRESS) {
            mProgressView.onDraw(canvas);
        } else if (mCurrentState == State.SUCCESS) {
            mSuccessView.onDraw(canvas);
        } else if (mCurrentState == State.FAILED) {
            mFaileddView.onDraw(canvas);
        }
    }

    public void setResult(int result) {
        if (result == RESULT_SUCCESS) {
            mResult = State.SUCCESS;
            mProgressView.cancelAnimation();
        } else if (result == RESULT_FAILED) {
            mResult = State.FAILED;
            mProgressView.cancelAnimation();
        } else {
            Log.w(TAG, "the result should be success or failed, have any other results? " +
                    "let me have a think...");
        }
    }

    public void startAnimation() {
        Log.e(TAG, "startAnimation: " + Thread.currentThread());
        mProgressView.setDefaultStartAngle(mSuccessView.getStartAngle());
        mCurrentState = State.PROGRESS;
        startAnimation(mProgressView.getAnimation());
    }

    @Override
    public void finished() {
        Log.e(TAG, "finished: " + Thread.currentThread());
        if (mResult == State.SUCCESS) {
            mCurrentState = State.SUCCESS;
            this.startAnimation(mSuccessView.getAnimation());
        } else if (mResult == State.FAILED) {
            mCurrentState = State.FAILED;
            this.startAnimation(mFaileddView.getAnimation());
        }
    }

    public static class Builder {

        public static final int STYLE_ARC_ROTATE = 0;
        public static final int STYLE_WATER_ROTATE = 1;

        private static final int DEFAULT_COLOR = 0xFF00FF00;
        private static final int DEFAULT_BACKGROUND_COLOR = 0x1100FF00;
        private static final int DEFAULT_STYLE = STYLE_WATER_ROTATE;

        private Config mConfig = new Config();

        public Builder setColor(int color) {
            mConfig.color = color;
            return this;
        }

        public Builder with(Context context) {
            mConfig.context = context;
            return this;
        }

        public Builder setBackgroundColor(int color) {
            mConfig.backgroundColor = color;
            return this;
        }

        public Builder setStyle(int style) {
            mConfig.style = style;
            return this;
        }

        public ProgressLoadingView build() {
            if (mConfig.context == null) {
                throw new NullPointerException("there is no context, please create the LoadingView " +
                        "in a context by invoking the method(with(Context context))");
            }
            if (mConfig.color != DEFAULT_COLOR
                    && mConfig.backgroundColor == DEFAULT_BACKGROUND_COLOR) {
                mConfig.backgroundColor = getBackgroundColor(mConfig.color);
            }
            return new ProgressLoadingView(mConfig.context, mConfig);
        }

        public static int getBackgroundColor(int color) {
            int r = (color >> 16) & 0xff;
            int g = (color >> 8) & 0xff;
            int b = color & 0xff;
            return (0x11 << 24) | r << 16 | g << 8 | b;
        }

        static class Config {
            Context context;
            int color = DEFAULT_COLOR;
            int backgroundColor = DEFAULT_BACKGROUND_COLOR;
            int style = DEFAULT_STYLE;
        }
    }

}
