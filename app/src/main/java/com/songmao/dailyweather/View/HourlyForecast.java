package com.songmao.dailyweather.View;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Typeface;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.songmao.dailyweather.gson.Hourly;
import com.songmao.dailyweather.gson.Weather;
import com.songmao.dailyweather.util.MyApplication;
import com.songmao.dailyweather.util.Utility;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Acer on 2017/5/16.
 */

public class HourlyForecast extends View {

    private static final String TAG = "HourlyForecast";
    private List<Integer> tmp ;
    private List<String> date ;
    private List<String> info ;
    private List<String> pop ;



    private Paint paint;
    private Path tmpPath ;
    private Path gonePath ;
    private Paint textPaint ;
    private float textOffset;
    private float w;

    public HourlyForecast(Context context) {
        super(context);
        init();
        setWillNotDraw(false);
    }

    public HourlyForecast(Context context,List<Hourly> hourlyList){
       this(context);
        for (Hourly hourly : hourlyList){
            tmp.add(Integer.parseInt(hourly.tmp));
            date.add(hourly.date.split(" ")[1]);
            info.add(hourly.cond.info);
            pop.add(hourly.pop);
        }
    }

    public HourlyForecast(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
        setWillNotDraw(false);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.d(TAG, "onMeasure: 1");
        int height  = (int) (textOffset * 10);
        final int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        if (heightMode == MeasureSpec.AT_MOST){
            setMeasuredDimension(getDefaultSize(getSuggestedMinimumWidth(),widthMeasureSpec), height);
        } else if (heightMode == MeasureSpec.EXACTLY){
            setMeasuredDimension(getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec),
                    getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec));
        }else {
            setMeasuredDimension(getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec),
                    height);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
        float xSpace = w/9;
        canvas.drawText("降水率", textPaint.measureText("降水率") / 2 +10, textOffset * 9, textPaint);
        canvas.drawText("天气", textPaint.measureText("降水率") / 2 +10, textOffset * 8, textPaint);
        canvas.drawText("时间", textPaint.measureText("降水率") / 2 +10, textOffset * 7, textPaint);
        if (tmp.size() != 0) {
            int offsetTmp = maxTmp(tmp) - minTmp(tmp);
            int count = tmp.size();
            int minTmp = minTmp(tmp);
            if (offsetTmp>= 1) {
                float ySpace =  textOffset * 3 / offsetTmp;
                tmpPath.moveTo(w, textOffset * 5 - ySpace * ( tmp.get(count - 1) - minTmp) );  //移动到最后一个数据对应的坐标；
                float i= 0;
                for (int j = count - 1; j >= 0 ; j--,i++) {
                    float x = (float) (w - xSpace * (i + 0.5));
                    int offset = tmp.get(j) - minTmp;
                    float y = textOffset * 5 - ySpace * offset;
                    tmpPath.lineTo(x , y);
                    canvas.drawText(tmp.get(j)+"°C", x,y - textOffset /2 ,textPaint);  //y - textOffset /2 在曲线的上方绘制温度；
                    canvas.drawText(date.get(j), x , textOffset * 7,textPaint);
                    canvas.drawText(info.get(j), x , textOffset * 8,textPaint);
                    canvas.drawText(pop.get(j)+"%",x , textOffset * 9,textPaint);

                }

                tmpPath.lineTo((float) (w - xSpace * (i - 1 + 0.5) - xSpace*0.3),textOffset * 5 - ySpace * (tmp.get(0) - minTmp) );
                paint.setPathEffect(new CornerPathEffect(300));
                canvas.drawPath(tmpPath,paint);
                gonePath.moveTo((float) (w - xSpace * (i - 1 + 0.5) - xSpace*0.3),textOffset * 5 - ySpace * (tmp.get(0) - minTmp) );
                gonePath.lineTo(0, textOffset * 5 - ySpace *( tmp.get(0)  - minTmp) );
                paint.setPathEffect(new DashPathEffect(new float[] {MyApplication.sp2px(5),MyApplication.sp2px(2)},100));
                canvas.drawPath(gonePath,paint);
            }else {
                int i = 0;
                tmpPath.moveTo(w , textOffset * 4);
                for (int j = count- 1; j >= 0 ;j--,i++){
                    float x = (float) (w - xSpace * (i + 0.5));
                    tmpPath.lineTo(x,textOffset * 4);
                    canvas.drawText(tmp.get(j)+"°", x,textOffset * 4 - textOffset /2 ,textPaint);
                    canvas.drawText(date.get(j), x , textOffset * 7,textPaint);
                    canvas.drawText(info.get(j), x , textOffset * 8,textPaint);
                    canvas.drawText(pop.get(j)+"%",x , textOffset * 9,textPaint);
                }
                tmpPath.lineTo((float) (w - xSpace * (i -  0.5) - xSpace*0.4),textOffset * 4);
                paint.setPathEffect(new CornerPathEffect(300));
                canvas.drawPath(tmpPath,paint);
                gonePath.moveTo((float) (w - xSpace * (i -  0.5) - xSpace*0.4), textOffset * 4);
                gonePath.lineTo(0,textOffset * 4);
                paint.setPathEffect(new DashPathEffect(new float[] {MyApplication.sp2px(5),MyApplication.sp2px(2)},0 ));
                canvas.drawPath(gonePath,paint);
            }
        } else {
            gonePath.moveTo(0,textOffset * 4);
            gonePath.lineTo(w,textOffset * 4);
            paint.setPathEffect(new DashPathEffect(new float[] {MyApplication.sp2px(5),MyApplication.sp2px(2)},100));
            canvas.drawPath(gonePath,paint);
        }
    }

    private void init(){
        tmp = new ArrayList<>();
        date = new ArrayList<>();
        info = new ArrayList<>();
        pop = new ArrayList<>();  //降水率

        paint = new Paint();
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setDither(true);
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(MyApplication.sp2px(2));

        textPaint = new Paint();
        textPaint.setSubpixelText(true);
        textPaint.setDither(true);
        textPaint.setTextSize(MyApplication.sp2px(14));
        textPaint.setColor(Color.WHITE);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setTextAlign(Paint.Align.CENTER);

        textOffset = getTextPaintOffset(textPaint);
        tmpPath = new Path();
        gonePath = new Path();
    }
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        this.w = w ;
        super.onSizeChanged(w, h, oldw, oldh);
    }

    private float getTextPaintOffset(Paint paint){
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        return (fontMetrics.bottom - fontMetrics.top);
    }

    private int maxTmp(List<Integer> data){
        int max = data.get(0);
            for (int i = 0; i < data.size(); i++) {
                if (data.get(i) > max) {
                    max = data.get(i);
                }
            }
        return max;
    }
    private int minTmp(List<Integer> data){
            int min = data.get(0);
            for (int i = 0; i < data.size(); i++) {
                if (data.get(i) < min ){
                    min = data.get(i);
                }
            }
        return min;
    }
    public void setHourlyData(List<Hourly> hourlyList){
        tmp.clear();
        date.clear();
        info.clear();
        pop.clear();
        for (Hourly hourly : hourlyList){
            tmp.add(Integer.parseInt(hourly.tmp));
            date.add(hourly.date.split(" ")[1]);
            info.add(hourly.cond.info);
            pop.add(hourly.pop);
        }
        invalidate();
    }
}
