## ProgressLoadingView | [中文](./README.md)

A custom loading view with progress in Android,you can using the interface provided to set loading progress, and the result success or failure.

## Screenshots

<img src="./screenshots/display.gif" width = "300" alt = "效果图" />

## Attribute Setting

| Attribute Name  |                            Value                             |
| :-------------: | :----------------------------------------------------------: |
|      color      |                          the color                           |
| backgroundColor | the background in screenshots(if not set, it will be set the alpha 0x11 automatically based on the color's value ) |
|    styleName    | arcRotate(the left style on screenshots) \| waterRotate(the right style on screenshots) |

## Download

- [ ![Download](https://api.bintray.com/packages/neuzzx/ProgressLoadingView/ProgressLoadingView/images/download.svg) ](https://bintray.com/neuzzx/ProgressLoadingView/ProgressLoadingView/_latestVersion)

-  compile

  `implementation 'com.zzx:ProgressLoadingView:1.0.0'`

- Apk

  [示例程序](./sampleApk/release/app-release.apk)

## Creation

- in xml

  ~~~xml
  <com.example.library.ProgressLoadingView
              android:id="@+id/loading_view_green_water"
              android:layout_width="100dp"
              android:layout_height="100dp"
              android:background="#88ffffff"
              app:color="#ff00ff00"
              app:styleName="waterRotate"/>
  ~~~

- in Java code

  ~~~java
  final ProgressLoadingView view = new ProgressLoadingView.Builder()
                          .with(MainActivity.this)
                          .setColor(Color.BLUE)
                          .build();
  linearLayout.addView(view, 0, new LinearLayout.LayoutParams(200, 200));
  //please invoke {@link ProgressLoadingView#startAnimation()} in view.post, because //of some calculation after ProgressLoadingView loaded.
  view.post(new Runnable() {
      @Override
      public void run() {
          view.startAnimation();
      }
  });
  ~~~

## Usage

- set loading progress

  `progressLoadingView.setProgress(progress)`

- set loading result 

  it has two kind of results

  `public static final int RESULT_SUCCESS = 0;`

  `public static final int RESULT_FAILED = 1;`

  `progressLoadingView.setResult(int result)`

## Author

a code farmer in NEU university

email:1289042324@qq.com

## License

you can use it in any way, but can not allowed to amend it without permission