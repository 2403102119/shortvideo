/**
 * Copyright 2014  XCL-Charts
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * @Project XCL-Charts
 * @Description Android图表基类库
 * @author XiongChuanLiang<br />(xcl_168@aliyun.com)
 * @Copyright Copyright (c) 2014 XCL-Charts (www.xclcharts.com)
 * @license http://www.apache.org/licenses/  Apache v2 License
 * @version 1.0
 */
package com.lxkj.shortvideo.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import org.xclcharts.chart.LineChart;
import org.xclcharts.chart.LineData;
import org.xclcharts.common.IFormatterDoubleCallBack;
import org.xclcharts.common.IFormatterTextCallBack;
import org.xclcharts.renderer.XEnum;
import org.xclcharts.renderer.info.AnchorDataPoint;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @ClassName LineChart01View
 * @Description 折线图的例子 <br/>
 *  * 	~_~
 * @author XiongChuanLiang<br />(xcl_168@aliyun.com)
 */
public class LineChart02View extends BaseChartView implements Runnable {

    private String TAG = "LineChart02View";
    private LineChart chart = new LineChart();

    //标签集合
    private LinkedList<String> labels = new LinkedList<String>();
    private LinkedList<LineData> chartData = new LinkedList<LineData>();
    double max = 10000, step = 500;
    //批注
    List<AnchorDataPoint> mAnchorSet = new ArrayList<AnchorDataPoint>();

    public LineChart02View(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        initView();
    }

    public LineChart02View(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public LineChart02View(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }

    private void initView() {
//		 	chartLabels();
//			chartDesireLines();
//			chartRender();
//			new Thread(this).start();
//
//			//綁定手势滑动事件
//			this.bindTouch(this,chart);
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //图所占范围大小
        chart.setChartRange(w, h);
    }

    private void chartRender() {
        try {
            //设置绘图区默认缩进px值,留置空间显示Axis,Axistitle....
            int[] ltrb = getBarLnDefaultSpadding();
            chart.setPadding(ltrb[0], ltrb[1], ltrb[2], ltrb[3]);

            //设定数据源
            chart.setCategories(labels);
            //	chart.setDataSource(chartData);
            //chart.setCustomLines(mCustomLineDataset);

            //数据轴最大值
            chart.getDataAxis().setAxisMax(max);
            //数据轴刻度间隔
            chart.getDataAxis().setAxisSteps(step);
            //指隔多少个轴刻度(即细刻度)后为主刻度
            chart.getDataAxis().setDetailModeSteps(1);

            //背景网格
            chart.getPlotGrid().hideHorizontalLines();

            //标题

            //隐藏顶轴和右边轴
            //chart.hideTopAxis();
            //chart.hideRightAxis();

            //设置轴风格

            //chart.getDataAxis().setTickMarksVisible(false);
            chart.getDataAxis().getAxisPaint().setStrokeWidth(1);
            chart.getDataAxis().getTickMarksPaint().setStrokeWidth(1);
            chart.getDataAxis().showAxisLabels();
            chart.getDataAxis().getAxisPaint().setColor(Color.rgb(251, 201, 25));
            chart.getDataAxis().getTickMarksPaint().setColor(Color.rgb(251, 201, 25));
            chart.getDataAxis().getTickLabelPaint().setColor(Color.rgb(139, 140, 145));
            chart.getDataAxis().getTickMarksPaint().setColor(Color.rgb(139, 140, 145));


            chart.getCategoryAxis().getAxisPaint().setColor(Color.rgb(251, 201, 25));
            chart.getCategoryAxis().getTickMarksPaint().setColor(Color.rgb(251, 201, 25));
            chart.getCategoryAxis().getTickLabelPaint().setColor(Color.rgb(139, 140, 145));
            chart.getCategoryAxis().getTickMarksPaint().setColor(Color.rgb(139, 140, 145));

            chart.getCategoryAxis().getAxisPaint().setStrokeWidth(1);
            chart.getCategoryAxis().hideTickMarks();

            //定义数据轴标签显示格式
            chart.getDataAxis().setLabelFormatter(new IFormatterTextCallBack() {

                @Override
                public String textFormatter(String value) {
                    // TODO Auto-generated method stub
                    Double tmp = Double.parseDouble(value);
                    DecimalFormat df = new DecimalFormat("#0");
                    String label = df.format(tmp).toString();
                    return (label);
                }

            });


            //定义线上交叉点标签显示格式
            chart.setItemLabelFormatter(new IFormatterDoubleCallBack() {
                @Override
                public String doubleFormatter(Double value) {
                    // TODO Auto-generated method stub
                    DecimalFormat df = new DecimalFormat("#0");
                    String label = df.format(value).toString();
                    return label;
                }
            });

            //chart.setItemLabelFormatter(callBack)

            //允许线与轴交叉时，线会断开
            chart.setLineAxisIntersectVisible(false);

            //动态线
            chart.showDyLine();

            //不封闭
            chart.setAxesClosed(false);
            //扩展绘图区右边分割的范围，让定制线的说明文字能显示出来
            chart.getClipExt().setExtRight(150.f);
            //设置标签交错换行显示
            chart.getCategoryAxis().setLabelLineFeed(XEnum.LabelLineFeed.ODD_EVEN);
            //仅能横向移动
            chart.setPlotPanMode(XEnum.PanMode.HORIZONTAL);

            //chart.getDataAxis().hide();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            Log.e(TAG, e.toString());
        }
    }


    public void chartDataSet(LinkedList<LineData> chartData, double max, double steps) {
        // 因为Java中Float和double的计算误差问题，所以建议
        //用图库中的MathHelper.getInstance()来做运算,以保证总值为100%
        chartLabels();
        //设置图表数据源
        this.chartData = chartData;
        this.max = max;
        this.step = steps;
        chartRender();
        this.bindTouch(this, chart);
        new Thread(this).start();
    }

    private void chartLabels() {
        labels.add("1月");
        labels.add("2月");
        labels.add("3月");
        labels.add("4月");
        labels.add("5月");
        labels.add("6月");
        labels.add("7月");
        labels.add("8月");
        labels.add("9月");
        labels.add("10月");
        labels.add("11月");
        labels.add("12月");
    }


    @Override
    public void render(Canvas canvas) {
        try {
            chart.render(canvas);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }


    @Override
    public void run() {
        // TODO Auto-generated method stub
        try {
            chartAnimation();
        } catch (Exception e) {
            Thread.currentThread().interrupt();
        }
    }

    private void chartAnimation() {
        try {
            int count = chartData.size();
            for (int i = 0; i < count; i++) {
                Thread.sleep(150);
                LinkedList<LineData> animationData = new LinkedList<LineData>();
                for (int j = 0; j <= i; j++) {
                    animationData.add(chartData.get(j));
                }

                //Log.e(TAG,"size = "+animationData.size());
                chart.setDataSource(animationData);
                if (i == count - 1) {
                    chart.getDataAxis().show();
                    chart.getDataAxis().showAxisLabels();

//          			chart.setCustomLines(mCustomLineDataset);

                }
                postInvalidate();
            }
        } catch (Exception e) {
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub

        super.onTouchEvent(event);

        if (event.getAction() == MotionEvent.ACTION_UP) {
            //交叉线
            if (chart.getDyLineVisible()) {
                chart.getDyLine().setCurrentXY(event.getX(), event.getY());
                if (chart.getDyLine().isInvalidate()) this.invalidate();
            }
        }
        return true;
    }

}
