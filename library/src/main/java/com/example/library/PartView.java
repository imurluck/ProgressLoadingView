package com.example.library;

import android.graphics.Canvas;
import android.view.animation.Animation;

public abstract class PartView {

   protected abstract void onSizeChanged(int w, int h, int oldW, int oldH);
   protected abstract void onDraw(Canvas canvas);
   protected abstract Animation getAnimation();
   protected abstract void cancelAnimation();
   protected abstract void setProgressListener(ProgressListener listener);
   protected abstract void config(ProgressLoadingView.Builder.Config config);
}
