package com.test.sensorregistrations;


import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class MainActivity extends Activity {

	private Button startTest;
	private Button stopTest;
	private SensorManager sensorManager;
	private SensorEventThread sensorThread;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.rect_layout);
		startTest = (Button) findViewById(R.id.start_button);
		stopTest = (Button) findViewById(R.id.stop_button);

		sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		sensorThread = new SensorEventThread("SensorThread");

	}

	private void startCapturing() {
		Log.v("SensorTest", "Registering listeners for sensors");
		sensorManager.registerListener(sensorThread,
				sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE), 0,
				sensorThread.getHandler());
		sensorManager
				.registerListener(sensorThread, sensorManager
						.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION), 0,
						sensorThread.getHandler());
		sensorManager.registerListener(sensorThread,
				sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY), 10000,
				sensorThread.getHandler());
		startTest.setVisibility(View.GONE);
		stopTest.setVisibility(View.VISIBLE);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void startTest(View v) {
		startCapturing();
	
	}
	
	public void stopTest(View v) {

		Log.v("SensorTest", "Un-Register Listner");

		sensorManager.unregisterListener(sensorThread);

		sensorThread.quitLooper();

		startTest.setVisibility(View.VISIBLE);
		stopTest.setVisibility(View.GONE);

	}
	class SensorEventThread extends HandlerThread implements
			SensorEventListener {

		Handler handler;

		public SensorEventThread(String name) {
			super(name);
		}

		@Override
		public void onSensorChanged(SensorEvent event) {
			Log.v("SensorTest", "onSensorChanged");
			if (event.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION) {
			} else if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
			} else if (event.sensor.getType() == Sensor.TYPE_GRAVITY) {
			}

		}

		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {

		}

		@Override
		protected void onLooperPrepared() {
			super.onLooperPrepared();
			handler = new Handler(sensorThread.getLooper());
		}

		public Handler getHandler() {
			return handler;
		}

		public void quitLooper() {
			if (sensorThread.isAlive()) {
				sensorThread.getLooper().quit();
			}
		}

	}

}
