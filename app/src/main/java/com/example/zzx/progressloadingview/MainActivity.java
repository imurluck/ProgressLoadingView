package com.example.zzx.progressloadingview;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;

import com.example.library.ProgressLoadingView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private Button mStartAnimationBtn;
    private Button mSuccessBtn;
    private Button mFailedBtn;
    private Button mAddBtn;

    private ProgressLoadingView mBlueArcView;
    private ProgressLoadingView mBlueWaterView;
    private ProgressLoadingView mGreenArcView;
    private ProgressLoadingView mGreenWaterView;

    private SeekBar mSeekBar;

    private List<ProgressLoadingView> mList;
    LinearLayout rootLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mList = new ArrayList<>();
        initViews();
    }

    private void initViews() {
        rootLayout = (LinearLayout) findViewById(R.id.root_layout);
        mSeekBar = (SeekBar) findViewById(R.id.seek_bar);
        mBlueArcView = (ProgressLoadingView) findViewById(R.id.loading_view_blue_arc);
        mBlueWaterView = (ProgressLoadingView) findViewById(R.id.loading_view_blue_water);
        mGreenArcView = (ProgressLoadingView) findViewById(R.id.loading_view_green_arc);
        mGreenWaterView = (ProgressLoadingView) findViewById(R.id.loading_view_green_water);
        mList.add(mBlueArcView);
        mList.add(mBlueWaterView);
        mList.add(mGreenArcView);
        mList.add(mGreenWaterView);
        mStartAnimationBtn = (Button) findViewById(R.id.start_btn);
        mFailedBtn = (Button) findViewById(R.id.failed_btn);
        mSuccessBtn = (Button) findViewById(R.id.success_btn);
        mAddBtn = (Button) findViewById(R.id.add_btn);
        mStartAnimationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAnimation();
            }
        });
        mSuccessBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                successAnimation();
            }
        });
        mFailedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                failAnimation();
            }
        });

        mAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressLoadingView view = new ProgressLoadingView.Builder().with(MainActivity.this)
                        .setColor(Color.BLUE)
                        .build();
                mList.add(view);
                rootLayout.addView(view, 0, new LinearLayout.LayoutParams(200, 200));
                //请在view.post中调用{@link ProgressLoadingView#startAnimation()},因为有些初始化的计算
                // 会在ProgressLoadingView加载完成之后
                //please invoke {@link ProgressLoadingView#startAnimation()} in view.post, because of
                //some calculation after ProgressLoadingView loaded.
                view.post(new Runnable() {
                    @Override
                    public void run() {
                        view.startAnimation();
                    }
                });
            }
        });
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                setAnimationProgress(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    /**
     *
     * @param progress
     */
    private void setAnimationProgress(int progress) {
        for (ProgressLoadingView view : mList) {
            view.setProgress(progress);
        }
    }

    private void successAnimation() {
        for (ProgressLoadingView view : mList) {
            view.setResult(ProgressLoadingView.RESULT_SUCCESS);
        }
    }

    private void failAnimation() {
        for (ProgressLoadingView view : mList) {
            view.setResult(ProgressLoadingView.RESULT_FAILED);
        }
    }

    private void startAnimation() {
        for (ProgressLoadingView view : mList) {
            view.startAnimation();
        }
    }
}
