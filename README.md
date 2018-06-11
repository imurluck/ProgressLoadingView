## ProgressLoadingView

一款Android自定义加载动画控件，你可以使用提供的接口设置进度，设置加载完成或者加载失败。

## 效果截图

<img src="./screenshots/display.gif" width = "300" alt = "效果图" />

### 属性设置

|     属性名      |                              值                              |
| :-------------: | :----------------------------------------------------------: |
|      color      |                     勾，叉，旋转条的颜色                     |
| backgroundColor | 圆中背景色(如未设置，会自动计算设置的color值，设定其alpha为0x11) |
|    styleName    |   arcRotate(截图中左侧形式) \| waterRotate(截图中右侧形式)   |



## 下载

- compile

等待jcenter审核中...

- 安装包

  [示例程序](./sampleApk/release/app-release.apk)

### 创建

- 在xml中

  ```xml
  <com.example.library.ProgressLoadingView
              android:id="@+id/loading_view_green_water"
              android:layout_width="100dp"
              android:layout_height="100dp"
              android:background="#88ffffff"
              app:color="#ff00ff00"
              app:styleName="waterRotate"/>
  ```

  

- 在Java代码中

  ~~~java
  final ProgressLoadingView view = new ProgressLoadingView.Builder()
                          .with(MainActivity.this)
                          .setColor(Color.BLUE)
                          .build();
  linearLayout.addView(view, 0, new LinearLayout.LayoutParams(200, 200));
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
  ~~~

## 使用

- 设置进度progress

  `progressLoadingView.setProgress(progress)`

- 设置加载结果

  有两种结果

  `public static final int RESULT_SUCCESS = 0;`

  `public static final int RESULT_FAILED = 1;`

  `progressLoadingView.setResult(int result)`

## 作者

白山黑水小码农

email:1289042324@qq.com

### License

本人同意以任何形式使用此控件,但不允许擅自修改.
