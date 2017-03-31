package CarRaceMVC;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.Light;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.effect.Lighting;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.Box;
import javafx.scene.shape.CullFace;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Sphere;
import javafx.scene.AmbientLight;
import JavaFX_3D.Xform;
import CarRaceMVC.Defines.*;


public class RaceView extends View
{
	//Other Finals
	private static final double DELTA_ROT = 0.1;
	private static final double ZOOM_SENSTIVTY = 100.f;
	
	//Camera default parameters
	private static final double CAMERA_INITIAL_DISTANCE = -2000;
	private static final double CAMERA_NEAR_CLIP		= 0.1;
	private static final double CAMERA_FAR_CLIP 		= 10000.0;
	//End camera params
	
	//Timer
	Timer rotateTimer;
	
	//Keyboard and mouse params
    double mousePosX;
    double mousePosY;
    double mouseOldX;
    double mouseOldY;
    double mouseDeltaX;
    double mouseDeltaY;

	
	
	
	private PerspectiveCamera camera;
	private Xform cameraXform;

	private ArrayList<Car> 		cars;
	private ArrayList<Integer> 	raceID;
	private ArrayList<String> 	songs;
	private ArrayList<String> 	users;
	
	public RaceView(ClientController c) {
		super(c);
		prepareMe();
		//Close the timer, if it exists
		this.setOnCloseRequest((event) -> {
			if (rotateTimer != null )
				rotateTimer.cancel();
		});
		this.open();
	}

	
	/**
	 * This method will create the pane for the view
	 */
	private void prepareMe() {
		this.W = 1000;
		this.H = 1000;

		pane = new Xform();
		
		buildCamera();
		
		//Set Materials
		PhongMaterial white = new PhongMaterial();
		white.setDiffuseColor(Color.RED);
		white.setDiffuseColor(Color.DARKRED);
		
		//Set Lighting
        setLighting();
		//Stadium
		buildStadium();
		//CAR
		buildCars();
		
		buildAxis();
		
	}


	/**
	 * 
	 */
	private void setLighting() {
		Light.Distant light0 = new Light.Distant();
        light0.setAzimuth(-400.f);
        light0.setColor(Color.GREEN);
		Lighting lighting = new Lighting();
        lighting.setSurfaceScale(9.0);

		lighting.setLight(light0);
	}


	/**
	 * 
	 */
	private void buildCars() {
		CarModel cm = new CarModel(carType.SPORT, size.MINI,manufacture.NISSAN,Color.BLUE);

		cm.setScaleX(50);
		cm.setScaleY(50);
		cm.setScaleZ(50);
		cm.setRotateX(180);;
		cm.setTranslateY(0);
		
		((Xform)pane).getChildren().add(cm);
	}


	/**
	 * 
	 */
	private void buildCamera() {
		
		camera = new PerspectiveCamera(true);
		
		camera.setNearClip(CAMERA_NEAR_CLIP);
		camera.setFarClip(CAMERA_FAR_CLIP);
		camera.setTranslateZ(CAMERA_INITIAL_DISTANCE);
		
		
		cameraXform = new Xform();
		cameraXform.getChildren().add(camera);
		
		((Group)pane).getChildren().add(cameraXform);
		
		
		/*
		//Add rotation to the camera
		
		rotateTimer = new Timer();
		rotateTimer.scheduleAtFixedRate(new TimerTask() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				cameraXform.rz.setAngle(cameraXform.rz.getAngle() + DELTA_ROT);
			//	camera.setRotate(cameraXform.rz.getAngle() + DELTA_ROT);
			}
		}, 100, 10);
		*/
	}
	
	public void open()
	{
		super.open();
		scene.setFill(Color.WHITE);
		scene.setCamera(camera);
		
		handleMouse();
	}
	
	private void handleMouse() {
		 
        scene.setOnMousePressed((me) -> {
                mousePosX = me.getSceneX();
                mousePosY = me.getSceneY();
                mouseOldX = me.getSceneX();
                mouseOldY = me.getSceneY();
            });
        
        scene.setOnMouseDragged((me) -> { 
                mouseOldX = mousePosX;
                mouseOldY = mousePosY;
                mousePosX = me.getSceneX();
                mousePosY = me.getSceneY();
                mouseDeltaX = (mousePosX - mouseOldX); 
                mouseDeltaY = (mousePosY - mouseOldY);

               double modifier = 1.0;

               if (me.isControlDown()) {
                    modifier = 0.1; //TODO
                } 
                if (me.isShiftDown()) {
                    modifier = 10.f;
                }     
                if (me.isPrimaryButtonDown()) {
                    cameraXform.ry.setAngle(cameraXform.ry.getAngle() -
                       mouseDeltaX*modifier*DELTA_ROT);  // 
                   cameraXform.rx.setAngle(cameraXform.rx.getAngle() +
                       mouseDeltaY*modifier*DELTA_ROT);  // -
                }
           }); // setOnMouseDragged
        
        scene.setOnZoom(e-> {
        	//System.out.println("ZOOM :: " + e.getZoomFactor());
             cameraXform.setTranslateZ(cameraXform.getTranslateZ() + (e.getZoomFactor() - 1) * ZOOM_SENSTIVTY );
             
             });
       
   } //handleMouse

	private void buildAxis()
	{
        final PhongMaterial redMaterial = new PhongMaterial();
        redMaterial.setDiffuseColor(Color.DARKRED);
        redMaterial.setSpecularColor(Color.RED);
 
        final PhongMaterial greenMaterial = new PhongMaterial();
        greenMaterial.setDiffuseColor(Color.DARKGREEN);
        greenMaterial.setSpecularColor(Color.GREEN);
 
        final PhongMaterial blueMaterial = new PhongMaterial();
        blueMaterial.setDiffuseColor(Color.DARKBLUE);
        blueMaterial.setSpecularColor(Color.BLUE);
 
        final Box xAxis = new Box(240.0, 1, 1);
        final Box yAxis = new Box(1, 240.0, 1);
        final Box zAxis = new Box(1, 1, 240.0);
        
        xAxis.setMaterial(redMaterial);
        yAxis.setMaterial(greenMaterial);
        zAxis.setMaterial(blueMaterial);
        
        Group axisGroup  = new Group();
        axisGroup.getChildren().addAll(xAxis, yAxis, zAxis);
        ((Group)pane).getChildren().addAll(axisGroup);
	}
	
	private void buildStadium()
	{
		//Set Shapes
		Box floor = new Box(10000, 1, 10000);
		
		Sphere sky = new Sphere(10000);
		
		Cylinder walls = new Cylinder(10000, 1000, 8);
		walls.setCullFace(CullFace.NONE);
		
		Box wall0 = new Box(1, 100, 1000);
		Box wall1 = new Box(1, 100, 1000);
		Box wall2 = new Box(1000, 100, 1);
		Box wall3 = new Box(1000, 100, 1);
		
		
		//Set Images
		Image floorImg 	= new Image("file:grass.jpg");
		Image skyImg 	= new Image("file:sky.jpg");
		Image wallImg	= new Image("file:wall.jpg");
		
		//Set Materialts
		PhongMaterial floorMat = new PhongMaterial();
		PhongMaterial skyMat = new PhongMaterial();
		PhongMaterial wallMat = new PhongMaterial();
		
		floorMat.setDiffuseMap(floorImg);
		skyMat.setDiffuseMap(skyImg);
		wallMat.setDiffuseMap(wallImg);
		
		//Apply Materials to the shapes
		
		floor.setMaterial(floorMat);
		walls.setMaterial(wallMat);
		
		
        ((Xform)pane).getChildren().add(floor);
        
	}
}