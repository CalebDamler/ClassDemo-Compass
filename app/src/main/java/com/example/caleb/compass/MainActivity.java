package com.example.caleb.compass;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private ImageView compass;
    private float currentDegree = 0.0f;
    private SensorManager sensorManager;
    private Sensor accelerometer, magnetometer;
    private float[] accelerometerArray;
    private float[] magnoteterArray;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        compass = findViewById(R.id.imageView);
        sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
    }

    @Override
    protected void onPause(){
        super.onPause();
        sensorManager.unregisterListener(this);
    }
    @Override
    protected void onResume(){
        super.onResume();
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME);
        sensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        final int DELAY = 1000;
        if(sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
            accelerometerArray = sensorEvent.values;
        }
        if(sensorEvent.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD){
            magnoteterArray = sensorEvent.values;
        }
        if(accelerometerArray != null && magnoteterArray !=null){
            float r[] = new float[9];
            float i[] = new float[9];
            boolean foundRotation = SensorManager.getRotationMatrix(r,i,accelerometerArray,magnoteterArray);
            if(foundRotation){
                float orientation [] = new float[3];
                SensorManager.getOrientation(r,orientation);
                float degree = (float)Math.toDegrees(orientation[0]);
                RotateAnimation animation = new RotateAnimation(currentDegree, -degree, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                animation.setDuration(DELAY);
                animation.setFillAfter(true);
                compass.startAnimation(animation);
                currentDegree = -degree;
            }
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
