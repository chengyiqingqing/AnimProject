package com.fattiger.emotionrain;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.Random;

/**
 * @Author shaowenwen
 * @Date 2020-05-02 08:34
 */
public class SingleEmotionRainView extends View {

    private static final int SCREEN_WIDTH = 720;
    private static final int SCREEN_HEIGHT = 1230;

    private boolean isRaining = false;
    private float mEmotionHeight;
    private float mEmotionWidth;

    private Random mRandom;
    private Matrix matrix;
    private Paint mPaint;
    private long mStartTimeStamp;
    private Context mContext;

    private EmotionBean mEmotionBean = new EmotionBean();

    public SingleEmotionRainView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        init();
    }

    private void init() {
        mRandom = new Random();
        setVisibility(GONE);
        matrix = new Matrix();
        initPaint();
    }

    private void initPaint() {
        // 创建画笔，设置抗锯齿，设置bitmap抗锯齿，设置dither，设置画笔颜色
        mPaint = new Paint();
        mPaint.setAntiAlias(true);//View当中包含有大量的图形，会出现锯齿
        mPaint.setFilterBitmap(true);//对位图进行滤波处理
        mPaint.setDither(true);
        mPaint.setColor(getResources().getColor(android.R.color.holo_red_light));
    }

    public void start(final Bitmap bitmap){
//        bitmap为null不进行操作，停止当前的雨落，设置视图可见，重置bitmap，重置标志位，重绘
        if (bitmap==null) return;
        stop();
        setVisibility(VISIBLE);
        initAndResetBitmap(bitmap);
        isRaining = true;
        invalidate();
    }

    private void initAndResetBitmap(Bitmap bitmap) {
        // 表情宽高设置为50dp，定义，最大市场为2000，
        this.mEmotionHeight = mEmotionWidth = dp2px(mContext,50);
        int maxDuration = 2000;
        // 创建表情对象，表情对象bitmap赋值，表情对象x赋值为【组件宽度】的随机值，y为【表情高度】的向上取整的负值
        EmotionBean emotionBean = new EmotionBean();
        emotionBean.bitmap = bitmap;
//        emotionBean.x = mRandom.nextInt(getWidth());
        emotionBean.x = mRandom.nextInt(SCREEN_WIDTH);
        emotionBean.y = (int) -Math.ceil(mEmotionHeight);
        // 时长获取：500毫秒的随机取值+最大时长
        int duration = mRandom.nextInt(500) + maxDuration;
        // 偏移值y：【组件高度】*16/时长；偏移值x:随机数对象.nextFloat的 round取值
//        emotionBean.velocityY = SCREEN_WIDTH * 16 / duration;
        emotionBean.velocityY = SCREEN_HEIGHT * 16 / duration;
        emotionBean.velocityX = Math.round(mRandom.nextFloat());
        // 为mEmotionBean 赋值
        mEmotionBean = emotionBean;
    }


    public void stop(){
        isRaining = false;
        setVisibility(GONE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!isRaining || mEmotionBean == null) return;
        EmotionBean emotionBean = mEmotionBean;
        Bitmap bitmap = emotionBean.bitmap;
        if (bitmap.isRecycled() || isOutofBottomBound()) {
            return;
        }
        matrix.reset();
        float heightScale = mEmotionHeight/bitmap.getHeight();
        float widthScale = mEmotionWidth/bitmap.getWidth();
        matrix.setScale(widthScale,heightScale);
        emotionBean.x = emotionBean.x+emotionBean.velocityX;
        emotionBean.y = emotionBean.y+emotionBean.velocityY;
        matrix.postTranslate(emotionBean.x,emotionBean.y);
        canvas.drawBitmap(bitmap,matrix,mPaint);
        postInvalidate();
    }

    private boolean isOutofBottomBound() {
        return mEmotionBean.y > getHeight();
    }


    public static int dp2px(Context context,float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int)(dpValue*scale+0.5f);
    }

}
