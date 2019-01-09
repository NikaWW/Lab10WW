package com.example.weronika.czujniklista9;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import java.util.ArrayList;

public class MainActivity extends Activity implements SensorEventListener {

    private static final String TAG = "MainActivity";
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private boolean isRunning = false;
    private PowerManager powerManager;
    private PowerManager.WakeLock myWakeLock;

    private TextView TextAx, TextAz, TextAy, Steps,fileName;
    private LinearLayout Chart;
    private Button btnStop, btnStart, btnSave;
    private String textSave = "";

    private ArrayList<Double> ax = new ArrayList<Double>();
    private ArrayList<Double> ay = new ArrayList<Double>();
    private ArrayList<Double> az = new ArrayList<Double>();
    private int i;

    private XYMultipleSeriesDataset datasetY= new XYMultipleSeriesDataset();
    private XYSeriesRenderer renderer=new XYSeriesRenderer();
    private XYMultipleSeriesRenderer mrenderer= new XYMultipleSeriesRenderer();
    private GraphicalView chartViewY;
    private XYSeries seriesY= new XYSeries("series y");

    private StepCounter st=new StepCounter(TAG);
    private SavetoFile save=new SavetoFile(TAG);

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_FASTEST);

        powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        myWakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "myapp:test");


        btnStop = findViewById(R.id.btnStop);
        btnStart = findViewById(R.id.btnStart);
        btnStop = findViewById(R.id.saveToFile);
        TextAx = findViewById(R.id.textAX);
        TextAy = findViewById(R.id.textAY);
        TextAz = findViewById(R.id.textAZ);
        Steps = findViewById(R.id.textSteps);
        Chart=findViewById(R.id.chart);
        fileName=findViewById(R.id.textImie);

        renderer.setLineWidth(2);
        renderer.setColor(Color.DKGRAY);
        renderer.setPointStyle(PointStyle.CIRCLE);

        mrenderer.addSeriesRenderer(renderer);
        mrenderer.setYAxisMax(10);
        mrenderer.setYAxisMin(0);
        mrenderer.setShowGrid(true);

    }

    public void onClickAction(View view) {
        i=0;
        isRunning = !isRunning;
        Log.d(TAG,"Button start pressed");
        if (isRunning) myWakeLock.acquire();
        else myWakeLock.release();

        seriesY.clear();
    }

    public void onSaveFileClick(View view) {
        Log.d(TAG,"Button save pressed");
        String filename=fileName.getText().toString()+".txt";
        save.saveFile(filename,textSave);

    }

    public void btnStopClick(View view) {

        isRunning=false;
        Log.d(TAG,"Button stop pressed");

        Steps = findViewById(R.id.textSteps);
        int steps=st.countSteps(ay);
        Steps.setText(String.valueOf(steps));

    }


    @Override
    public void onSensorChanged(SensorEvent event) {

        if (isRunning) {
            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {


                double aX = event.values[0];
                double aY = event.values[1];
                double aZ = event.values[2];

                double timeStamp = event.timestamp;
                Log.d(TAG,"time"+timeStamp);

                TextAx.setText(Double.toString(aX));
                TextAy.setText(Double.toString(aY));
                TextAz.setText(Double.toString(aZ));

                textSave = textSave + "aX= " + Double.toString(aX) + "\t" + "aY= " + Double.toString(aY) + "\t" + "aZ= " + Double.toString(aZ) + "\n";

                ax.add(aX);
                ay.add(aY);
                az.add(aZ);

                seriesY.add(i,aY);
                datasetY.clear();
                datasetY.addSeries(seriesY);
                chartViewY = ChartFactory.getLineChartView(this, datasetY, mrenderer);

                Chart.removeAllViews();
                Chart.addView(chartViewY);
                i++;

            }
        }
    }


    @Override
    public void onAccuracyChanged (Sensor sensor,int accuracy){

    }
}