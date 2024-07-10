package com.gojoy.shiyeji;


import static androidx.core.math.MathUtils.clamp;
import static java.lang.Math.PI;
import static java.lang.Math.abs;
import static java.lang.Math.pow;
import static java.lang.Math.random;
import static java.lang.Math.tan;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class PresentationView extends View {
    private Paint paint;

    private final int RADIUS_SIZE = calculateRadiusFromFov(0.43f);
    private final int FIX_EYE_LIGHT_RADIUS_SIZE = calculateRadiusFromFov(0.43f);;

    private final float MAX_EYE_LUMINANCE = 3000.0f * 0.22f; // nits

    private final float[] STIMULUS = {
            1, 3.14f, 10, 12.6f, 15.8f,
            20, 25.1f, 31.6f, 39.8f, 50.1f,
            63.1f, 79.4f, 100, 126, 158,
            200, 251, 316, 398, 501,
            631, 794, 1000};

    private final float BACKGROUND_ASB = 31.4f;
    private int screenWidth = 1920;
    private int screenHeight = 1080;

    private final float FOV_H = 39.1f;
    private final float FOV_V = 23.2f;

    private class Stimulus {
        public int x, y;
        public int r, g, b;
    }

    private Stimulus[] mStimulus;

    private int mMode = 0;

    private final int DURATION = 200; //ms
    private final int WAITTIME = 800; //ms

    private int mDisplayIndex = 0;

    private Timer mTimerDuration;
    private Timer mTimerShow;

    private boolean mStimulusShow = false;

//    显示周期200ms，等待800ms后显
    public PresentationView(Context context) {
        super(context);
        init();
    }

    public PresentationView(Context context, AttributeSet attrs){
        super(context, attrs);
        init();
    }

    public PresentationView(Context context, AttributeSet attrs, int defStyle){
        super(context, attrs, defStyle);
        init();
    }

    class ShowTask extends TimerTask {
        public void run() {
            mStimulusShow = false;
            invalidate();
        }
    }

    class DurationTask extends TimerTask {
        public void run() {
            mTimerShow.schedule(new ShowTask(), DURATION);
            if(++mDisplayIndex >= STIMULUS.length){
                mDisplayIndex = 0;
            }
            mStimulusShow = true;
            invalidate();
        }
    }

    private void init() {
        mStimulus = new Stimulus[23];
        for (int i = 0; i < mStimulus.length; i++) {
            mStimulus[i] = this.new Stimulus();
        }

        mTimerDuration = new Timer();
        mTimerShow = new Timer();
        mTimerDuration.schedule(new DurationTask(), 0, DURATION + WAITTIME);

        paint = new Paint();
        int[] rgb = calculateRGB(BACKGROUND_ASB, 2);
        paint.setStrokeWidth(1f);
        paint.setAntiAlias(false);
        paint.setStyle(Paint.Style.FILL);
        setBackgroundColor(Color.rgb(rgb[0], rgb[1], rgb[2]));

        int rows = 4;
        int cols = 6;
        int pointsPerRow = STIMULUS.length / rows + 1; // 基础点数每行
        int index = 0;

        // 计算水平间距和垂直间距
        int horizontalSpacing = screenWidth / (pointsPerRow + 1);
        int verticalSpacing = screenHeight / (rows + 1);

        // 生成点的坐标
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                if(index < 23) {
                    mStimulus[index].x = (col + 1) * horizontalSpacing;
                    mStimulus[index].y = (row + 1) * verticalSpacing;
                    int[] color = calculateRGB(BACKGROUND_ASB + STIMULUS[index], 2);
                    mStimulus[index].r = color[0];
                    mStimulus[index].g = color[1];
                    mStimulus[index].b = color[2];
                }
                index++;
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        switch (mMode){
            case 0:
                paint.setColor(Color.rgb(114, 114, 0));
                canvas.drawCircle(screenWidth / 2, screenHeight / 2, FIX_EYE_LIGHT_RADIUS_SIZE, paint);
                for(int i = 0 ; i < STIMULUS.length; i++){
                    paint.setColor(Color.rgb(mStimulus[i].r, mStimulus[i].g, mStimulus[i].b));
                    canvas.drawCircle(mStimulus[i].x, mStimulus[i].y, RADIUS_SIZE, paint);
                }
                break;

            case 1:
                paint.setColor(Color.rgb(114, 114, 0));
                canvas.drawCircle(screenWidth / 2, screenHeight / 2, FIX_EYE_LIGHT_RADIUS_SIZE, paint);
                if(mStimulusShow){
                    paint.setColor(Color.rgb(mStimulus[mDisplayIndex].r, mStimulus[mDisplayIndex].g, mStimulus[mDisplayIndex].b));
                    canvas.drawCircle(mStimulus[mDisplayIndex].x, mStimulus[mDisplayIndex].y, RADIUS_SIZE, paint);
                }
                break;

            case 2:
                paint.setColor(Color.rgb(114, 114, 0));
                canvas.drawCircle(screenWidth / 2, screenHeight / 2, FIX_EYE_LIGHT_RADIUS_SIZE, paint);
                if(mStimulusShow){
                    Random random = new Random();
                    int min = 10;
                    int max_x = 1910;
                    int max_y = 1070;
                    int x = min + random.nextInt(max_x - min + 1);
                    int y = min + random.nextInt(max_y - min + 1);
                    paint.setColor(Color.rgb(mStimulus[mDisplayIndex].r, mStimulus[mDisplayIndex].g, mStimulus[mDisplayIndex].b));
                    canvas.drawCircle(x, y, RADIUS_SIZE, paint);
                }
                break;

            case 3:
                setBackgroundColor(Color.rgb(255, 255, 255));
                for(int y = 0; y < 5; y++){
                    for(int x = 0; x < 5; x++){
                        if((y == 1 || y == 3) && x > 0){
                            paint.setColor(Color.rgb(255, 255,255));
                        }
                        else{
                            paint.setColor(Color.rgb(0, 0,0));
                        }
                        canvas.drawPoint(screenWidth / 2 + x - 2, screenHeight / 2 + y - 2,  paint);
                    }
                }

                break;

            default:
                break;
        }
    }

    public void showAsb(int asb) {
        paint.setColor(Color.rgb(asb, asb, asb));
        invalidate();
    }

    int[] calculateRGB(double asb, int range) {
        int red, green, blue;
        double luminance = asb / PI / MAX_EYE_LUMINANCE;
        double gamma = 2.2;
        int initialRGB = (int)(pow(luminance, 1.0 / gamma) * 255);
        red = green = blue = initialRGB;

        for (int i = 0; i <= range; ++i) {
            for (int j = -range; j <= range; ++j) {
                for (int k = -range; k <= range; ++k) {

                    int newRed = clamp(initialRGB + i, 0, 255);
                    int newGreen = clamp(initialRGB + j, 0, 255);
                    int newBlue = clamp(initialRGB + k, 0, 255);

                    double newLuminance = 0.2126 * pow(newRed / 255.0, gamma) +
                            0.7152 * pow(newGreen / 255.0, gamma) +
                            0.0722 * pow(newBlue / 255.0, gamma);

                    if (abs(newLuminance - luminance) < abs(pow(red / 255.0, gamma) * 0.2126 +
                            pow(green / 255.0, gamma) * 0.7152 +
                            pow(blue / 255.0, gamma) * 0.0722 - luminance)) {
                        red = newRed;
                        green = newGreen;
                        blue = newBlue;
                    }
                }
            }
        }

        int[] ret = {red, green, blue};
        return ret;
    }

    public void loopPlay() {
        if(++mMode > 3){
            mMode = 0;
        }

        int[] rgb = calculateRGB(BACKGROUND_ASB, 2);
        if(mMode == 3){
            setBackgroundColor(Color.rgb(255, 255, 255));
        }
        else{
            setBackgroundColor(Color.rgb(rgb[0], rgb[1], rgb[2]));
        }

        invalidate();
    }

    int calculateRadiusFromFov(float fov){
        float halfG3Fov = (float) (fov * PI / 180.0f / 2.0f);
        float halfBBFovH = (float) (FOV_H * PI / 180 / 2);
        float halfBBFovV = (float) (FOV_V * PI / 180 / 2);
        float deltaX = (float) (tan(halfG3Fov) * (1920 / 2) / tan(halfBBFovH));
        float deltaY = (float) (tan(halfG3Fov) * (1080 / 2) / tan(halfBBFovV));

        return (int) ((deltaX + deltaY) / 2.0f);
    }

}