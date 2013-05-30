/*
 * Copyright (C) 2010- Peer internet solutions
 * 
 * This file is part of mixare.
 * 
 * This program is free software: you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License as published by 
 * the Free Software Foundation, either version 3 of the License, or 
 * (at your option) any later version. 
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS 
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License 
 * for more details. 
 * 
 * You should have received a copy of the GNU General Public License along with 
 * this program. If not, see <http://www.gnu.org/licenses/>
 */
package org.mixare;

/**
 * This class is the main application which uses the other classes for different
 * functionalities.
 * It sets up the camera screen and the augmented screen which is in front of the
 * camera screen.
 * It also handles the main sensor events, touch events and location events.
 */

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.hardware.*;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.FloatMath;
import android.util.Log;
import android.view.KeyEvent;
import android.view.*;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;
import com.geocube.R;
import org.mixare.data.DataHandler;
import org.mixare.data.DataSourceList;
import org.mixare.lib.gui.PaintScreen;
import org.mixare.lib.marker.Marker;
import org.mixare.lib.render.Matrix;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static android.hardware.SensorManager.SENSOR_DELAY_GAME;

public class MixView extends Activity implements SensorEventListener, OnTouchListener {
	private CameraSurface camScreen;
	private AugmentedView augScreen;

	private boolean isInited;
	private static PaintScreen dWindow;
	private static DataView dataView;
	private boolean fError;

    private MixViewDataHolder mixViewData;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
			handleIntent(getIntent());

			final PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
			getMixViewData().setmWakeLock(pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "My Tag"));

			killOnError();
			requestWindowFeature(Window.FEATURE_NO_TITLE);

			maintainCamera();
			maintainAugmentR();

			if (!isInited) {
				setdWindow(new PaintScreen());
				setDataView(new DataView(getMixViewData().getMixContext()));

				/* set the radius in data view to the last selected by the user */
				setZoomLevel();
				isInited = true;
			}

		} catch (Exception ex) {
			doError(ex);
		}
	}

	public MixViewDataHolder getMixViewData() {
		if (mixViewData==null){
			mixViewData = new MixViewDataHolder(new MixContext(this));
		}

		return mixViewData;
	}

	@Override
	protected void onPause() {
		super.onPause();

		try {
			this.getMixViewData().getmWakeLock().release();

			try {
				getMixViewData().getSensorMgr().unregisterListener(this,
						getMixViewData().getSensorGrav());
				getMixViewData().getSensorMgr().unregisterListener(this,
						getMixViewData().getSensorMag());
				getMixViewData().setSensorMgr(null);
				
				getMixViewData().getMixContext().getLocationFinder().switchOff();
				getMixViewData().getMixContext().getDownloadManager().switchOff();

				if (getDataView() != null) {
					getDataView().cancelRefreshTimer();
				}
			} catch (Exception ignore) {
			}

			if (fError) {
				finish();
			}
		} catch (Exception ex) {
			doError(ex);
		}
	}

	/**
	 * {@inheritDoc}
	 * Mixare - Receives results from other launched activities
	 * Base on the result returned, it either refreshes screen or not.
	 * Default value for refreshing is false
	 */
	protected void onActivityResult(final int requestCode,
			final int resultCode, Intent data) {
		// check if the returned is request to refresh screen (setting might be
		// changed)
		try {
//			if (data.getBooleanExtra("RefreshScreen", false)) {
			if (data.getBooleanExtra("RefreshScreen", true)) {
				repaint();
			}

		} catch (Exception ex) {
			// do nothing do to mix of return results.
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		try {
			this.getMixViewData().getmWakeLock().acquire();

			killOnError();
			getMixViewData().getMixContext().doResume(this);

			repaint();
			getDataView().doStart();
			getDataView().clearEvents();

			getMixViewData().getMixContext().getDataSourceManager().refreshDataSources();

			float angleX, angleY;
			int marker_orientation = -90;
			int rotation = Compatibility.getRotation(this);
			// display text from left to right and keep it horizontal
			angleX = (float) Math.toRadians(marker_orientation);
			getMixViewData().getM1().set(1f, 0f, 0f, 0f,
					FloatMath.cos(angleX),
					-FloatMath.sin(angleX), 0f,
					FloatMath.sin(angleX),
					FloatMath.cos(angleX));
			angleX = (float) Math.toRadians(marker_orientation);
			angleY = (float) Math.toRadians(marker_orientation);
			if (rotation == 1) {
				getMixViewData().getM2().set(1f, 0f, 0f, 0f,
						FloatMath.cos(angleX),
						-FloatMath.sin(angleX), 0f,
						FloatMath.sin(angleX),
						FloatMath.cos(angleX));
				getMixViewData().getM3().set(FloatMath.cos(angleY), 0f,
						FloatMath.sin(angleY), 0f, 1f, 0f,
						-FloatMath.sin(angleY), 0f,
						FloatMath.cos(angleY));
			} else {
				getMixViewData().getM2().set(FloatMath.cos(angleX), 0f,
						FloatMath.sin(angleX), 0f, 1f, 0f,
						-FloatMath.sin(angleX), 0f,
						FloatMath.cos(angleX));
				getMixViewData().getM3().set(1f, 0f, 0f, 0f,
						FloatMath.cos(angleY),
						-FloatMath.sin(angleY), 0f,
						FloatMath.sin(angleY),
						FloatMath.cos(angleY));

			}

			getMixViewData().getM4().toIdentity();

			for (int i = 0; i < getMixViewData().getHistR().length; i++) {
				getMixViewData().getHistR()[i] = new Matrix();
			}

			getMixViewData()
					.setSensorMgr((SensorManager) getSystemService(SENSOR_SERVICE));

			getMixViewData().setSensors(getMixViewData().getSensorMgr().getSensorList(
					Sensor.TYPE_ACCELEROMETER));
			if (getMixViewData().getSensors().size() > 0) {
				getMixViewData().setSensorGrav(getMixViewData().getSensors().get(0));
			}

			getMixViewData().setSensors(getMixViewData().getSensorMgr().getSensorList(
					Sensor.TYPE_MAGNETIC_FIELD));
			if (getMixViewData().getSensors().size() > 0) {
				getMixViewData().setSensorMag(getMixViewData().getSensors().get(0));
			}

			getMixViewData().getSensorMgr().registerListener(this,
					getMixViewData().getSensorGrav(), SENSOR_DELAY_GAME);
			getMixViewData().getSensorMgr().registerListener(this,
					getMixViewData().getSensorMag(), SENSOR_DELAY_GAME);

			try {
				GeomagneticField gmf = getMixViewData().getMixContext().getLocationFinder().getGeomagneticField(); 
				angleY = (float) Math.toRadians(-gmf.getDeclination());
				getMixViewData().getM4().set(FloatMath.cos(angleY), 0f,
						FloatMath.sin(angleY), 0f, 1f, 0f,
						-FloatMath.sin(angleY), 0f,
						FloatMath.cos(angleY));
			} catch (Exception ex) {
				ex.printStackTrace();
			}

			getMixViewData().getMixContext().getDownloadManager().switchOn();
			getMixViewData().getMixContext().getLocationFinder().switchOn();
		} catch (Exception ex) {
			doError(ex);
			try {
				if (getMixViewData().getSensorMgr() != null) {
					getMixViewData().getSensorMgr().unregisterListener(this,
							getMixViewData().getSensorGrav());
					getMixViewData().getSensorMgr().unregisterListener(this,
							getMixViewData().getSensorMag());
					getMixViewData().setSensorMgr(null);
				}

				if (getMixViewData().getMixContext() != null) {
					getMixViewData().getMixContext().getLocationFinder().switchOff();
					getMixViewData().getMixContext().getDownloadManager().switchOff();
				}
			} catch (Exception ignore) {
			}
		}

		if (getDataView().isFrozen() && getMixViewData().getSearchNotificationTxt() == null) {
			getMixViewData().setSearchNotificationTxt(new TextView(this));
			getMixViewData().getSearchNotificationTxt().setWidth(
					getdWindow().getWidth());
			getMixViewData().getSearchNotificationTxt().setPadding(10, 2, 0, 0);
			getMixViewData().getSearchNotificationTxt().setText(
					getString(R.string.search_active_1) + " "
							+ DataSourceList.getDataSourcesStringList()
							+ getString(R.string.search_active_2));
			getMixViewData().getSearchNotificationTxt().setBackgroundColor(
					Color.DKGRAY);
			getMixViewData().getSearchNotificationTxt().setTextColor(Color.WHITE);

			getMixViewData().getSearchNotificationTxt().setOnTouchListener(this);
			addContentView(getMixViewData().getSearchNotificationTxt(),
					new LayoutParams(LayoutParams.FILL_PARENT,
							LayoutParams.WRAP_CONTENT));
		} else if (!getDataView().isFrozen()
				&& getMixViewData().getSearchNotificationTxt() != null) {
			getMixViewData().getSearchNotificationTxt().setVisibility(View.GONE);
			getMixViewData().setSearchNotificationTxt(null);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * Customize Activity after switching back to it.
	 * Currently it maintain and ensures view creation.
	 */
	protected void onRestart (){
		super.onRestart();
		maintainCamera();
		maintainAugmentR();
	}
	
	/* ********* Operators ***********/ 

	public void repaint() {
		//clear stored data
		getDataView().clearEvents();
		setDataView(null); //It's smelly code, but enforce garbage collector 
							//to release data.
		setDataView(new DataView(mixViewData.getMixContext()));
		setdWindow(new PaintScreen());
	}
	
	/**
	 *  Checks camScreen, if it does not exist, it creates one.
	 */
	private void maintainCamera() {
		if (camScreen == null){
		camScreen = new CameraSurface(this);
		}
		setContentView(camScreen);
	}
	
	/**
	 * Checks augScreen, if it does not exist, it creates one.
	 */
	private void maintainAugmentR() {
		if (augScreen == null ){
		augScreen = new AugmentedView(this);
		}
		addContentView(augScreen, new LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
	}

	public void refresh(){
		dataView.refresh();
	}

	public float calcZoomLevel(){

		int myZoomLevel = 500;
		float myout = 5;

		if (myZoomLevel <= 26) {
			myout = myZoomLevel / 25f;
		} else if (25 < myZoomLevel && myZoomLevel < 50) {
			myout = (1 + (myZoomLevel - 25)) * 0.38f;
		} else if (25 == myZoomLevel) {
			myout = 1;
		} else if (50 == myZoomLevel) {
			myout = 10;
		} else if (50 < myZoomLevel && myZoomLevel < 75) {
			myout = (10 + (myZoomLevel - 50)) * 0.83f;
		} else {
			myout = (30 + (myZoomLevel - 75) * 2f);
		}


		return myout;
	}

	/* ******** Operators - Sensors ****** */

	public void onSensorChanged(SensorEvent evt) {
		try {

			if (evt.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
				getMixViewData().getGrav()[0] = evt.values[0];
				getMixViewData().getGrav()[1] = evt.values[1];
				getMixViewData().getGrav()[2] = evt.values[2];

				augScreen.postInvalidate();
			} else if (evt.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
				getMixViewData().getMag()[0] = evt.values[0];
				getMixViewData().getMag()[1] = evt.values[1];
				getMixViewData().getMag()[2] = evt.values[2];

				augScreen.postInvalidate();
			}

			SensorManager.getRotationMatrix(getMixViewData().getRTmp(),
					getMixViewData().getI(), getMixViewData().getGrav(),
					getMixViewData().getMag());

			int rotation = Compatibility.getRotation(this);

			if (rotation == 1) {
				SensorManager.remapCoordinateSystem(getMixViewData().getRTmp(),
						SensorManager.AXIS_X, SensorManager.AXIS_MINUS_Z,
						getMixViewData().getRot());
			} else {
				SensorManager.remapCoordinateSystem(getMixViewData().getRTmp(),
						SensorManager.AXIS_Y, SensorManager.AXIS_MINUS_Z,
						getMixViewData().getRot());
			}
			getMixViewData().getTempR().set(getMixViewData().getRot()[0],
					getMixViewData().getRot()[1], getMixViewData().getRot()[2],
					getMixViewData().getRot()[3], getMixViewData().getRot()[4],
					getMixViewData().getRot()[5], getMixViewData().getRot()[6],
					getMixViewData().getRot()[7], getMixViewData().getRot()[8]);

			getMixViewData().getFinalR().toIdentity();
			getMixViewData().getFinalR().prod(getMixViewData().getM4());
			getMixViewData().getFinalR().prod(getMixViewData().getM1());
			getMixViewData().getFinalR().prod(getMixViewData().getTempR());
			getMixViewData().getFinalR().prod(getMixViewData().getM3());
			getMixViewData().getFinalR().prod(getMixViewData().getM2());
			getMixViewData().getFinalR().invert();

			getMixViewData().getHistR()[getMixViewData().getrHistIdx()].set(getMixViewData()
					.getFinalR());
			getMixViewData().setrHistIdx(getMixViewData().getrHistIdx() + 1);
			if (getMixViewData().getrHistIdx() >= getMixViewData().getHistR().length)
				getMixViewData().setrHistIdx(0);

			getMixViewData().getSmoothR().set(0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f);
			for (int i = 0; i < getMixViewData().getHistR().length; i++) {
				getMixViewData().getSmoothR().add(getMixViewData().getHistR()[i]);
			}
			getMixViewData().getSmoothR().mult(
					1 / (float) getMixViewData().getHistR().length);

			getMixViewData().getMixContext().updateSmoothRotation(getMixViewData().getSmoothR());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent me) {
		try {
			killOnError();

			float xPress = me.getX();
			float yPress = me.getY();
			if (me.getAction() == MotionEvent.ACTION_UP) {
				getDataView().clickEvent(xPress, yPress);
			}//TODO add gesture events (low)

			return true;
		} catch (Exception ex) {
			// doError(ex);
			ex.printStackTrace();
			return super.onTouchEvent(me);
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		try {
			killOnError();

			if (keyCode == KeyEvent.KEYCODE_BACK) {
				if (getDataView().isDetailsView()) {
					getDataView().keyEvent(keyCode);
					getDataView().setDetailsView(false);
					return true;
				} else {
					//TODO handle keyback to finish app correctly
					return super.onKeyDown(keyCode, event);
				}
			} else if (keyCode == KeyEvent.KEYCODE_MENU) {
				return super.onKeyDown(keyCode, event);
			} else {
				getDataView().keyEvent(keyCode);
				return false;
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			return super.onKeyDown(keyCode, event);
		}
	}

	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		if (sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD
				&& accuracy == SensorManager.SENSOR_STATUS_UNRELIABLE
				&& getMixViewData().getCompassErrorDisplayed() == 0) {
			for (int i = 0; i < 2; i++) {
				Toast.makeText(getMixViewData().getMixContext(),
						"Compass data unreliable. Please recalibrate compass.",
						Toast.LENGTH_LONG).show();
			}
			getMixViewData().setCompassErrorDisplayed(getMixViewData()
					.getCompassErrorDisplayed() + 1);
		}
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		getDataView().setFrozen(false);
		if (getMixViewData().getSearchNotificationTxt() != null) {
			getMixViewData().getSearchNotificationTxt().setVisibility(View.GONE);
			getMixViewData().setSearchNotificationTxt(null);
		}
		return false;
	}


	/* ************ Handlers *************/

	public void doError(Exception ex1) {
		if (!fError) {
			fError = true;
//			setErrorDialog();
			ex1.printStackTrace();
		}

		try {
			augScreen.invalidate();
		} catch (Exception ignore) {
		}
	}

	public void killOnError() throws Exception {
		if (fError)
			throw new Exception();
	}

	private void handleIntent(Intent intent) {
		if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
			String query = intent.getStringExtra(SearchManager.QUERY);
			doMixSearch(query);
		}
	}

	private void doMixSearch(String query) {
		DataHandler jLayer = getDataView().getDataHandler();
		if (!getDataView().isFrozen()) {
			MixListView.originalMarkerList = jLayer.getMarkerList();
			MixMap.originalMarkerList = jLayer.getMarkerList();
		}

		ArrayList<Marker> searchResults = new ArrayList<Marker>();
		Log.d("SEARCH-------------------0", "" + query);
		if (jLayer.getMarkerCount() > 0) {
			for (int i = 0; i < jLayer.getMarkerCount(); i++) {
				Marker ma = jLayer.getMarker(i);
				if (ma.getTitle().toLowerCase().indexOf(query.toLowerCase()) != -1) {
					searchResults.add(ma);
					/* the website for the corresponding title */
				}
			}
		}
		if (searchResults.size() > 0) {
			getDataView().setFrozen(true);
			jLayer.setMarkerList(searchResults);
		}
        else Toast.makeText(this, getString(R.string.search_failed_notification),
                Toast.LENGTH_LONG).show();
    }

	/* ******* Getter and Setters ********** */

	/**
	 * @return the dWindow
	 */
	static PaintScreen getdWindow() {
		return dWindow;
	}


	/**
	 * @param dWindow
	 *            the dWindow to set
	 */
	static void setdWindow(PaintScreen dWindow) {
		MixView.dWindow = dWindow;
	}


	/**
	 * @return the dataView
	 */
	static DataView getDataView() {
		return dataView;
	}

	/**
	 * @param dataView
	 *            the dataView to set
	 */
	static void setDataView(DataView dataView) {
		MixView.dataView = dataView;
	}


	private void setZoomLevel() {
		float myout = calcZoomLevel();
		getDataView().setRadius(myout);
		mixViewData.setZoomLevel(String.valueOf(myout));
		getMixViewData().getMixContext().getDownloadManager().switchOn();
	}


/**
 * @author daniele
 *
 */
class CameraSurface extends SurfaceView implements SurfaceHolder.Callback {
	MixView app;
	SurfaceHolder holder;
	Camera camera;

	CameraSurface(Context context) {
		super(context);

		try {
			app = (MixView) context;

			holder = getHolder();
			holder.addCallback(this);
			holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		} catch (Exception ex) {

		}
	}

	public void surfaceCreated(SurfaceHolder holder) {
		try {
			if (camera != null) {
				try {
					camera.stopPreview();
				} catch (Exception ignore) {
				}
				try {
					camera.release();
				} catch (Exception ignore) {
				}
				camera = null;
			}

			camera = Camera.open();
			camera.setPreviewDisplay(holder);
		} catch (Exception ex) {
			try {
				if (camera != null) {
					try {
						camera.stopPreview();
					} catch (Exception ignore) {
					}
					try {
						camera.release();
					} catch (Exception ignore) {
					}
					camera = null;
				}
			} catch (Exception ignore) {

			}
		}
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		try {
			if (camera != null) {
				try {
					camera.stopPreview();
				} catch (Exception ignore) {
				}
				try {
					camera.release();
				} catch (Exception ignore) {
				}
				camera = null;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}


	public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
		try {
			Camera.Parameters parameters = camera.getParameters();
			try {
				List<Camera.Size> supportedSizes = null;
				// On older devices (<1.6) the following will fail
				// the camera will work nevertheless
				supportedSizes = Compatibility.getSupportedPreviewSizes(parameters);

				// preview form factor
				float ff = (float) w / h;
				Log.d("Mixare", "Screen res: w:" + w + " h:" + h
						+ " aspect ratio:" + ff);

				// holder for the best form factor and size
				float bff = 0;
				int bestw = 0;
				int besth = 0;
				Iterator<Camera.Size> itr = supportedSizes.iterator();

				// we look for the best preview size, it has to be the closest
				// to the
				// screen form factor, and be less wide than the screen itself
				// the latter requirement is because the HTC Hero with update
				// 2.1 will
				// report camera preview sizes larger than the screen, and it
				// will fail
				// to initialize the camera
				// other devices could work with previews larger than the screen
				// though
				while (itr.hasNext()) {
					Camera.Size element = itr.next();
					// current form factor
					float cff = (float) element.width / element.height;
					// check if the current element is a candidate to replace
					// the best match so far
					// current form factor should be closer to the bff
					// preview width should be less than screen width
					// preview width should be more than current bestw
					// this combination will ensure that the highest resolution
					// will win
					Log.d("Mixare", "Candidate camera element: w:"
							+ element.width + " h:" + element.height
							+ " aspect ratio:" + cff);
					if ((ff - cff <= ff - bff) && (element.width <= w)
							&& (element.width >= bestw)) {
						bff = cff;
						bestw = element.width;
						besth = element.height;
					}
				}
				Log.d("Mixare", "Chosen camera element: w:" + bestw + " h:"
						+ besth + " aspect ratio:" + bff);
				// Some Samsung phones will end up with bestw and besth = 0
				// because their minimum preview size is bigger then the screen
				// size.
				// In this case, we use the default values: 480x320
				if ((bestw == 0) || (besth == 0)) {
					Log.d("Mixare", "Using default camera parameters!");
					bestw = 480;
					besth = 320;
				}
				parameters.setPreviewSize(bestw, besth);
			} catch (Exception ex) {
				parameters.setPreviewSize(480, 320);
			}

			camera.setParameters(parameters);
			camera.startPreview();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}

class AugmentedView extends View {
	MixView app;
	public AugmentedView(Context context) {
		super(context);
		try {
			app = (MixView) context;
			app.killOnError();
		} catch (Exception ex) {
			app.doError(ex);
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		try {
			app.killOnError();

			MixView.getdWindow().setWidth(canvas.getWidth());
			MixView.getdWindow().setHeight(canvas.getHeight());

			MixView.getdWindow().setCanvas(canvas);

			if (!MixView.getDataView().isInited()) {
				MixView.getDataView().init(MixView.getdWindow().getWidth(),
						MixView.getdWindow().getHeight());
			}

			MixView.getDataView().draw(MixView.getdWindow());
		} catch (Exception ex) {
			app.doError(ex);
		}
	}
}

/**
 * Internal class that holds Mixview field Data.
 * 
 * @author A B
 */
class MixViewDataHolder {
	private final MixContext mixContext;
	private float[] RTmp;
	private float[] Rot;
	private float[] I;
	private float[] grav;
	private float[] mag;
	private SensorManager sensorMgr;
	private List<Sensor> sensors;
	private Sensor sensorGrav;
	private Sensor sensorMag;
	private int rHistIdx;
	private Matrix tempR;
	private Matrix finalR;
	private Matrix smoothR;
	private Matrix[] histR;
	private Matrix m1;
	private Matrix m2;
	private Matrix m3;
	private Matrix m4;
	private WakeLock mWakeLock;
	private int compassErrorDisplayed;
	private String zoomLevel;
	private int zoomProgress;
	private TextView searchNotificationTxt;

	public MixViewDataHolder(MixContext mixContext) {
		this.mixContext=mixContext;
		this.RTmp = new float[9];
		this.Rot = new float[9];
		this.I = new float[9];
		this.grav = new float[3];
		this.mag = new float[3];
		this.rHistIdx = 0;
		this.tempR = new Matrix();
		this.finalR = new Matrix();
		this.smoothR = new Matrix();
		this.histR = new Matrix[60];
		this.m1 = new Matrix();
		this.m2 = new Matrix();
		this.m3 = new Matrix();
		this.m4 = new Matrix();
		this.compassErrorDisplayed = 0;
	}

	/* ******* Getter and Setters ********** */
	public MixContext getMixContext() {
		return mixContext;
	}

	public float[] getRTmp() {
		return RTmp;
	}

	public void setRTmp(float[] rTmp) {
		RTmp = rTmp;
	}

	public float[] getRot() {
		return Rot;
	}

	public void setRot(float[] rot) {
		Rot = rot;
	}

	public float[] getI() {
		return I;
	}

	public void setI(float[] i) {
		I = i;
	}

	public float[] getGrav() {
		return grav;
	}

	public void setGrav(float[] grav) {
		this.grav = grav;
	}

	public float[] getMag() {
		return mag;
	}

	public void setMag(float[] mag) {
		this.mag = mag;
	}

	public SensorManager getSensorMgr() {
		return sensorMgr;
	}

	public void setSensorMgr(SensorManager sensorMgr) {
		this.sensorMgr = sensorMgr;
	}

	public List<Sensor> getSensors() {
		return sensors;
	}

	public void setSensors(List<Sensor> sensors) {
		this.sensors = sensors;
	}

	public Sensor getSensorGrav() {
		return sensorGrav;
	}

	public void setSensorGrav(Sensor sensorGrav) {
		this.sensorGrav = sensorGrav;
	}

	public Sensor getSensorMag() {
		return sensorMag;
	}

	public void setSensorMag(Sensor sensorMag) {
		this.sensorMag = sensorMag;
	}

	public int getrHistIdx() {
		return rHistIdx;
	}

	public void setrHistIdx(int rHistIdx) {
		this.rHistIdx = rHistIdx;
	}

	public Matrix getTempR() {
		return tempR;
	}

	public void setTempR(Matrix tempR) {
		this.tempR = tempR;
	}

	public Matrix getFinalR() {
		return finalR;
	}

	public void setFinalR(Matrix finalR) {
		this.finalR = finalR;
	}

	public Matrix getSmoothR() {
		return smoothR;
	}

	public void setSmoothR(Matrix smoothR) {
		this.smoothR = smoothR;
	}

	public Matrix[] getHistR() {
		return histR;
	}

	public void setHistR(Matrix[] histR) {
		this.histR = histR;
	}

	public Matrix getM1() {
		return m1;
	}

	public void setM1(Matrix m1) {
		this.m1 = m1;
	}

	public Matrix getM2() {
		return m2;
	}

	public void setM2(Matrix m2) {
		this.m2 = m2;
	}

	public Matrix getM3() {
		return m3;
	}

	public void setM3(Matrix m3) {
		this.m3 = m3;
	}

	public Matrix getM4() {
		return m4;
	}

	public void setM4(Matrix m4) {
		this.m4 = m4;
	}

	public WakeLock getmWakeLock() {
		return mWakeLock;
	}

	public void setmWakeLock(WakeLock mWakeLock) {
		this.mWakeLock = mWakeLock;
	}

	public int getCompassErrorDisplayed() {
		return compassErrorDisplayed;
	}

	public void setCompassErrorDisplayed(int compassErrorDisplayed) {
		this.compassErrorDisplayed = compassErrorDisplayed;
	}

	public String getZoomLevel() {
		return zoomLevel;
	}

	public void setZoomLevel(String zoomLevel) {
		this.zoomLevel = zoomLevel;
	}

	public int getZoomProgress() {
		return zoomProgress;
	}

	public void setZoomProgress(int zoomProgress) {
		this.zoomProgress = zoomProgress;
	}

	public TextView getSearchNotificationTxt() {
		return searchNotificationTxt;
	}

	public void setSearchNotificationTxt(TextView searchNotificationTxt) {
		this.searchNotificationTxt = searchNotificationTxt;
	}
  }
}

