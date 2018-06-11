package com.example.zzx.progressloadingview;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.library.ProgressLoadingView;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    Button startAnimationBtn;
    Button successBtn;
    Button failedBtn;
    Button addBtn;
    ProgressLoadingView progressLoadingView;
    LinearLayout rootLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /**successTickView = (SuccessTickView) findViewById(R.id.success_tick);
         startAnimationBtn = (Button) findViewById(R.id.animate_btn);
         startAnimationBtn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
        successTickView.startTickAnim();
        }
        });*/
        rootLayout = (LinearLayout) findViewById(R.id.root_layout);
        progressLoadingView = (ProgressLoadingView) findViewById(R.id.loading_view);
        startAnimationBtn = (Button) findViewById(R.id.start_btn);
        failedBtn = (Button) findViewById(R.id.failed_btn);
        successBtn = (Button) findViewById(R.id.success_btn);
        addBtn = (Button) findViewById(R.id.add_btn);
        startAnimationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "onClick: " + Thread.currentThread());
                progressLoadingView.startAnimation();
            }
        });
        successBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ProgressLoadingView.startAnimation(2);
                progressLoadingView.setResult(ProgressLoadingView.RESULT_SUCCESS);
            }
        });
        failedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressLoadingView.setResult(ProgressLoadingView.RESULT_FAILED);
            }
        });


        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressLoadingView view = new ProgressLoadingView.Builder().with(MainActivity.this)
                        .setColor(Color.BLUE)
                        .build();
                rootLayout.addView(view, 0, new LinearLayout.LayoutParams(200, 200));
                view.post(new Runnable() {
                    @Override
                    public void run() {
                        view.startAnimation();;
                    }
                });
            }
        });

    }
}
