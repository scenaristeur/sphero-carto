package orbotix.uisample;



import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

  
import java.util.Random;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;
import orbotix.robot.app.ColorPickerActivity;
import orbotix.robot.base.CollisionDetectedAsyncData;
import orbotix.robot.base.Robot;
import orbotix.robot.base.CollisionDetectedAsyncData.CollisionPower;
import orbotix.robot.sensor.Acceleration;
import orbotix.robot.sensor.LocatorData;
import orbotix.robot.widgets.CalibrationImageButtonView;
import orbotix.robot.widgets.NoSpheroConnectedView;
import orbotix.robot.widgets.NoSpheroConnectedView.OnConnectButtonClickListener;
import orbotix.robot.widgets.SlideToSleepView;
import orbotix.robot.widgets.joystick.JoystickView;
import orbotix.sphero.CollisionListener;
import orbotix.sphero.ConnectionListener;
import orbotix.sphero.LocatorListener;
import orbotix.sphero.Sphero;
import orbotix.view.calibration.CalibrationView;
import orbotix.view.calibration.ControllerActivity;
import orbotix.view.connection.SpheroConnectionView;


public class UiSampleActivity extends ControllerActivity {
    /** ID to start the StartupActivity for result to connect the Robot */
    private final static int STARTUP_ACTIVITY = 0;
    private static final int BLUETOOTH_ENABLE_REQUEST = 11;
    private static final int BLUETOOTH_SETTINGS_REQUEST = 12;

    /** ID to start the ColorPickerActivity for result to select a color */
    private final static int COLOR_PICKER_ACTIVITY = 1;
    private boolean mColorPickerShowing = false;

    /** The Robot to control */
    private Sphero mRobot;

    /** One-Touch Calibration Button */
    private CalibrationImageButtonView mCalibrationImageButtonView;

    /** Calibration View widget */
    private CalibrationView mCalibrationView;

    /** Slide to sleep view */
    private SlideToSleepView mSlideToSleepView;

    /** No Sphero Connected Pop-Up View */
    private NoSpheroConnectedView mNoSpheroConnectedView;

    /** Sphero Connection View */
    private SpheroConnectionView mSpheroConnectionView;

    //Colors
    private int mRed = 0xff;
    private int mGreen = 0xff;
    private int mBlue = 0xff;
    private float positionX;
    private float positionY;
    private float velocityX;
    private float velocityY;
    private String couleur="vert";
    private float angle=0f;
    private float vitesse=0.6f;
    private boolean stop;
    private boolean auto;
    private int countCollision=0;
    private int maxCollision=10;
    private float accelX;
    private float accelY;
    private float powerX;
    private float powerY;
    private static final String TAG = "OBX-LocatorSmag";
    private Point point=new Point();
    private ArrayList<Point> points=new ArrayList<Point>();
    File repertoire= new File (Environment.getExternalStorageDirectory()+"/smag-sphero");

    private LocatorListener mLocatorListener = new LocatorListener() {
        @Override
        public void onLocatorChanged(LocatorData locatorData) {
            Log.d(TAG, locatorData.toString());
            if (locatorData != null) {
            	positionX=locatorData.getPositionX();
            	positionY=locatorData.getPositionY();
            	velocityX=locatorData.getVelocityX();
            	velocityY=locatorData.getVelocityY();
            	point=new Point(positionX,positionY,velocityX,velocityY,couleur);
            	points.add(point);
            	
            	if ((auto)&&(Math.abs((velocityX))<.01)&&(Math.abs(velocityY)<.01)){
            	//	 Toast.makeText(UiSampleActivity.this,velocityX+" "+velocityY, Toast.LENGTH_SHORT).show();
            		chercheDirection();
            	}
            	//Toast.makeText(UiSampleActivity.this,"X:"+positionX, Toast.LENGTH_SHORT).show();
               // ((TextView) findViewById(R.id.txt_locator_x)).setText(locatorData.getPositionX() + " cm");
               // ((TextView) findViewById(R.id.txt_locator_y)).setText(locatorData.getPositionY() + " cm");
               // ((TextView) findViewById(R.id.txt_locator_vx)).setText(locatorData.getVelocityX() + " cm/s");
               // ((TextView) findViewById(R.id.txt_locator_vy)).setText(locatorData.getVelocityY() + " cm/s");
            }
        }
    };
     
    private BroadcastReceiver mColorChangeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // update colors
            int red = intent.getIntExtra(ColorPickerActivity.EXTRA_COLOR_RED, 0);
            int green = intent.getIntExtra(ColorPickerActivity.EXTRA_COLOR_GREEN, 0);
            int blue = intent.getIntExtra(ColorPickerActivity.EXTRA_COLOR_BLUE, 0);

            // change the color on the ball
            mRobot.setColor(red, green, blue);
        }
    };

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        if (!repertoire.exists()) {
        	  repertoire.mkdir();
        	}

        // Set up the Sphero Connection View
        mSpheroConnectionView = (SpheroConnectionView) findViewById(R.id.sphero_connection_view);
        
        mSpheroConnectionView.addConnectionListener(new ConnectionListener() {

            @Override
            public void onConnected(Robot robot) {
                // Set Robot
                mRobot = (Sphero) robot; // safe to cast for now
                //Set connected Robot to the Controllers
                setRobot(mRobot);
                //active LocatorListener;
                mRobot.getSensorControl().addLocatorListener(mLocatorListener);
                mRobot.getSensorControl().setRate(5);
                mRobot.getCollisionControl().addCollisionListener(mCollisionListener);
                mRobot.getCollisionControl().startDetection(45, 45, 45, 45, 100);
                // Make sure you let the calibration view knows the robot it should control
                mCalibrationView.setRobot(mRobot);

                // Make connect sphero pop-up invisible if it was previously up
                mNoSpheroConnectedView.setVisibility(View.GONE);
                mNoSpheroConnectedView.switchToConnectButton();
                Toast.makeText(UiSampleActivity.this,"Déplacez Sphéro, cliquez ensuite sur Sleep pour enregistrer le parcours dans /smag-sphero", Toast.LENGTH_SHORT).show();
               
                if (repertoire.isDirectory()){
                    Toast.makeText(UiSampleActivity.this,"positions enregistrées dans /smag-sphero", Toast.LENGTH_SHORT).show();

                }else{
                    Toast.makeText(UiSampleActivity.this,"création du repertoire /smag-sphero", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onConnectionFailed(Robot sphero) {
                // let the SpheroConnectionView handle or hide it and do something here...
            }

            @Override
            public void onDisconnected(Robot sphero) {
            	if (points!=null){ecrireFichier();}
                mSpheroConnectionView.startDiscovery();
                
            }
        });

        //Add the JoystickView as a Controller
        addController((JoystickView) findViewById(R.id.joystick));

        // Add the calibration view
        mCalibrationView = (CalibrationView) findViewById(R.id.calibration_view);

        // Set up sleep view
        mSlideToSleepView = (SlideToSleepView) findViewById(R.id.slide_to_sleep_view);
        mSlideToSleepView.hide();
        // Send ball to sleep after completed widget movement
        mSlideToSleepView.setOnSleepListener(new SlideToSleepView.OnSleepListener() {
            @Override
            public void onSleep() {
                mRobot.sleep(0);
            }
        });

        // Initialize calibrate button view where the calibration circle shows above button
        // This is the default behavior
        mCalibrationImageButtonView = (CalibrationImageButtonView) findViewById(R.id.calibration_image_button);
        mCalibrationImageButtonView.setCalibrationView(mCalibrationView);
        // You can also change the size and location of the calibration views (or you can set it in XML)
        mCalibrationImageButtonView.setRadius(100);
        mCalibrationImageButtonView.setOrientation(CalibrationView.CalibrationCircleLocation.ABOVE);

        // Grab the No Sphero Connected View
        mNoSpheroConnectedView = (NoSpheroConnectedView) findViewById(R.id.no_sphero_connected_view);
        mNoSpheroConnectedView.setOnConnectButtonClickListener(new OnConnectButtonClickListener() {

            @Override
            public void onConnectClick() {
                mSpheroConnectionView.setVisibility(View.VISIBLE);
                mSpheroConnectionView.startDiscovery();
            }

            @Override
            public void onSettingsClick() {
                // Open the Bluetooth Settings Intent
                Intent settingsIntent = new Intent(Settings.ACTION_BLUETOOTH_SETTINGS);
                UiSampleActivity.this.startActivityForResult(settingsIntent, BLUETOOTH_SETTINGS_REQUEST);
            }
        });


    }


    /** Called when the user comes back to this app */
    @Override
    protected void onResume() {
        super.onResume();
        if (mColorPickerShowing) {
            mColorPickerShowing = false;
            return;
        }

        Log.d("", "registering Color Change Listener");
        IntentFilter filter = new IntentFilter(ColorPickerActivity.ACTION_COLOR_CHANGE);
        registerReceiver(mColorChangeReceiver, filter);
    }

    /** Called when the user presses the back or home button */
    @Override
    protected void onPause() {
        super.onPause();
        if (mColorPickerShowing) return;
ecrireFichier();
        // Disconnect Robot properly
        if (mRobot != null) {
            mRobot.disconnect();
        }
        try {
            unregisterReceiver(mColorChangeReceiver); // many times throws exception on leak
        } catch (Exception e) {
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == COLOR_PICKER_ACTIVITY) {
                //Get the colors
                mRed = data.getIntExtra(ColorPickerActivity.EXTRA_COLOR_RED, 0xff);
                mGreen = data.getIntExtra(ColorPickerActivity.EXTRA_COLOR_GREEN, 0xff);
                mBlue = data.getIntExtra(ColorPickerActivity.EXTRA_COLOR_BLUE, 0xff);

                //Set the color
                mRobot.setColor(mRed, mGreen, mBlue);
            } else if (requestCode == BLUETOOTH_ENABLE_REQUEST) {
                // User enabled bluetooth, so refresh Sphero list
                mSpheroConnectionView.setVisibility(View.VISIBLE);
                mSpheroConnectionView.startDiscovery();
            }
        } else {
            if (requestCode == STARTUP_ACTIVITY) {
                // Failed to return any robot, so we bring up the no robot connected view
                mNoSpheroConnectedView.setVisibility(View.VISIBLE);
            } else if (requestCode == BLUETOOTH_ENABLE_REQUEST) {

                // User clicked "NO" on bluetooth enable settings screen
                Toast.makeText(UiSampleActivity.this,
                        "Enable Bluetooth to Connect to Sphero", Toast.LENGTH_LONG).show();
            } else if (requestCode == BLUETOOTH_SETTINGS_REQUEST) {
                // User enabled bluetooth, so refresh Sphero list
                mSpheroConnectionView.setVisibility(View.VISIBLE);
                mSpheroConnectionView.startDiscovery();
            }
        }
    }

    /**
     * When the user clicks the "Color" button, show the ColorPickerActivity
     *
     * @param v The Button clicked
     */
    public void onColorClick(View v) {

        mColorPickerShowing = true;
        Intent i = new Intent(this, ColorPickerActivity.class);

        //Tell the ColorPickerActivity which color to have the cursor on.
        i.putExtra(ColorPickerActivity.EXTRA_COLOR_RED, mRed);
        i.putExtra(ColorPickerActivity.EXTRA_COLOR_GREEN, mGreen);
        i.putExtra(ColorPickerActivity.EXTRA_COLOR_BLUE, mBlue);

        startActivityForResult(i, COLOR_PICKER_ACTIVITY);
    }

    public void onAutoClick(View v) {
    	mRobot.setColor(150, 150, 0);
    	 Toast.makeText(UiSampleActivity.this,"Auto", Toast.LENGTH_SHORT).show();
    	// for (int i=0;i<10;i++){
    	 auto=true;
    	 mRobot.drive(angle, vitesse);
    	 
    }
    
    /**
     * When the user clicks the "Sleep" button, show the SlideToSleepView shows
     *
     * @param v The Button clicked
     */
    public void onSleepClick(View v) {
    	ecrireFichier();
        mSlideToSleepView.show();
    }
    
    public void onWriteClick(View v){
    	String message= "X :"+positionX+" Y : "+positionY;
    	 Toast.makeText(UiSampleActivity.this,"Ecriture fichier"+message, Toast.LENGTH_LONG).show();
    	 
    	/* String NOTES ="info.txt";
    	 File file = new File(Environment.getExternalStorageDirectory(), NOTES);
    	  
    	 String Info = "Test Text";
    	 try {
			file.createNewFile();
	    	 FileWriter filewriter = new FileWriter(file,false);
	    	 filewriter.write(Info);
	    	 filewriter.close();
	    	 Toast.makeText(UiSampleActivity.this,"OK", Toast.LENGTH_LONG).show();
	     	
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
	    	 Toast.makeText(UiSampleActivity.this,"Ecriture plantée"+message, Toast.LENGTH_LONG).show();
	     	
		}*/

    }
    public void onStopClick(View v) {
        if (mRobot != null) {
            // Stop robot
        	stop=true;
        	auto=false;
            mRobot.stop();
        }
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        mCalibrationView.interpretMotionEvent(event);
        mSlideToSleepView.interpretMotionEvent(event);
        return super.dispatchTouchEvent(event);
    }
    private final CollisionListener mCollisionListener = new CollisionListener() {
        public void collisionDetected(CollisionDetectedAsyncData collisionData) {
        	mRobot.setColor(200, 0, 0);
        	Acceleration acceleration = collisionData.getImpactAcceleration();
        	CollisionPower power = collisionData.getImpactPower();
        	stop=true;
        	accelX=(float) acceleration.x;
        	accelY=(float) acceleration.y;
        	powerX=power.x;
        	powerY=power.y;
        	couleur="rouge";
        	point=new Point(positionX,positionY,velocityX,velocityY,couleur);
        	points.add(point);
        	couleur="vert";
        	countCollision++;

        	 Toast.makeText(UiSampleActivity.this,countCollision+" Collision "+angle, Toast.LENGTH_SHORT).show();
        	 if (auto){
        		 mRobot.setColor(20,0,0);
chercheDirection();

//mRobot.drive(angle,vitesse);
        	 }
        /*    // Update the UI with the collision data
            Acceleration acceleration = collisionData.getImpactAcceleration();
            mAccelXValueLabel = (TextView) findViewById(R.id.accel_x_value);
            mAccelXValueLabel.setText("" + acceleration.x);

            mAccelYValueLabel = (TextView) findViewById(R.id.accel_y_value);
            mAccelYValueLabel.setText("" + acceleration.y);

            mAccelZValueLabel = (TextView) findViewById(R.id.accel_z_value);
            mAccelZValueLabel.setText("" + acceleration.z);

            mXAxisCheckBox = (CheckBox) findViewById(R.id.axis_x_checkbox);
            mXAxisCheckBox.setChecked(collisionData.hasImpactXAxis());

            mYAxisCheckBox = (CheckBox) findViewById(R.id.axis_y_checkbox);
            mYAxisCheckBox.setChecked(collisionData.hasImpactYAxis());

            CollisionPower power = collisionData.getImpactPower();
            mPowerXValueLabel = (TextView) findViewById(R.id.power_x_value);
            mPowerXValueLabel.setText("" + power.x);

            mPowerYValueLabel = (TextView) findViewById(R.id.power_y_value);
            mPowerYValueLabel.setText("" + power.y);

            mSpeedValueLabel = (TextView) findViewById(R.id.speed_value);
            mSpeedValueLabel.setText("" + collisionData.getImpactSpeed());

            mTimestampLabel = (TextView) findViewById(R.id.time_stamp_value);
            mTimestampLabel.setText(collisionData.getImpactTimeStamp() + " ms");*/
        }
    };
     
    
    public void chercheDirection(){
    	//mRobot.drive(angle, vitesse);
    	
    	if((velocityX==0)&&(velocityY==0)){
    		
    		mRobot.setColor(0, 0, 20);

    	Random rand = new Random();
        		 float variation = rand.nextFloat();
    		/* float sens=rand.nextFloat();
    		 if (sens>.5){
    		angle=angle+(180f*variation);
    		 }else{
    			 angle=angle-(180f*variation); 
    		 }*/
    		angle=angle+180f*variation;
    		if(angle>=360f){
    			angle-=360;
    		}
    		mRobot.setColor(20,0,0);
    		//vitesse=0.2f;
    		// Toast.makeText(UiSampleActivity.this,"demi tour "+angle+" "+vitesse, Toast.LENGTH_SHORT).show();
         	
    		mRobot.drive(angle,vitesse);
    		//vitesse=0.6f;
    		try {
				Thread.sleep(250);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		mRobot.setColor(20,20,0);
    		mRobot.drive(angle,vitesse);
    	}
    }
    
    public void ecrireFichier(){
    	String message= ""; 
      	 
       	//ecriture
       	 String cheminTime ="smag-sphero/Smag-Sphero"+System.currentTimeMillis()+".txt";
       	String cheminLast ="smag-sphero/Smag-Sphero-last.txt";
    	 File file = new File(Environment.getExternalStorageDirectory(), cheminTime);
    	 File fileLast = new File(Environment.getExternalStorageDirectory(), cheminLast);
    	 Point dernier=null;
    	  for (int i=0;i<points.size();i++){
    		  Point p=points.get(i);
    		  dernier=p;
    		  message=message+p.getX()+","+p.getY()+","+p.getVelX()+","+p.getVelY()+","+p.getCouleur()+","+p.getAccelX()+","+p.getAccelY()+","+p.getPowerX()+","+p.getPowerY()+"\n";
    	  }
    	  message=message+dernier.getX()+","+dernier.getY()+","+dernier.getVelX()+","+dernier.getVelY()+",bleu,"+dernier.getAccelX()+","+dernier.getAccelY()+","+dernier.getPowerX()+","+dernier.getPowerY()+"\n";
    	 String Info = message;
    	 try {
    		file.createNewFile();
       	 FileWriter filewriter = new FileWriter(file,true);
       	 filewriter.write(Info);
       	 filewriter.close();
       	
        	//pour last
       	 if (fileLast.exists()){
       		 fileLast.delete();
       		 }
    		fileLast.createNewFile();
    	   	 FileWriter filewriterLast = new FileWriter(fileLast,true);
    	   	 filewriterLast.write(Info);
    	   	 filewriterLast.close();
    	   	 Toast.makeText(UiSampleActivity.this,"OK", Toast.LENGTH_LONG).show();
    	} catch (IOException e) {
    		// TODO Auto-generated catch block
    		e.printStackTrace();
       	 Toast.makeText(UiSampleActivity.this,"Ecriture plantée"+message, Toast.LENGTH_LONG).show();
        	
    	}
    }
}
