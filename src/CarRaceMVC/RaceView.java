package CarRaceMVC;

import java.util.ArrayList;
import java.util.Timer;
import JavaFX_3D.Xform;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.DepthTest;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.effect.Lighting;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.util.Duration;


public class RaceView extends View
{
	private static final int STADIUM_HEIGHT = 50;
	private static final int RACE_TRACK_WIDTH = 100;
	private static final int STADIUM_WIDTH = 250;
	private static final int STADIUM_LONG = 1000;
	private static final int FINISH_LINE =  - STADIUM_LONG/ 2 + 50; 
	private static final int CARS_INITIAL_X = 400;
	private static final int INITIAL_CAMERA_X_ANGLE = -24;
	private static final int INITIAL_CAMERA_Y_ANGLE = 255;
	//Other Finals
	private static final double DELTA_ROT = 0.1;
	private static final double ZOOM_SENSTIVTY = 0.01f;
	
	//Camera default parameters
	private static final double CAMERA_INITIAL_DISTANCE = -200;
	private static final double CAMERA_NEAR_CLIP		= 0.1;
	private static final double CAMERA_FAR_CLIP 		= 10000.0;
	//End camera params
	
	//MISC CAR_RACE CONSTANTS
	private static final double CAR_SPACE = 20;
	protected static final double DELTA = 0.1;

	
	//Timer
	private Timer rotateTimer;
	
	//Keyboard and mouse params
    private double mousePosX;
    private double mousePosY;
    private  double mouseOldX;
    private  double mouseOldY;
    private  double mouseDeltaX;
    private  double mouseDeltaY;
	

	private Xform ThreeDPane;
	
	private PerspectiveCamera camera;
	private Xform cameraXform;

	private View betView;
	
	private ArrayList<Car> 		cars;
	private ArrayList<CarModel> carModels;
	private int raceNum;
	private int winnerCar;
	private int songId;
	

	public RaceView(ClientController c,String title,int song, ArrayList<Car> carInfo, int raceNum) {
		super(c);
		winnerCar=-1;
		this.songId = song;
		this.cars = carInfo;
		this.raceNum = raceNum;
		
		prepareMe();
		//Close the timer, if it exists
		this.setOnCloseRequest((event) -> {
			if (rotateTimer != null )
				rotateTimer.cancel();
		});

		setTitle(title);
		this.open();
	}

	/**
	 * This method will create the pane for the view
	 */
	private void prepareMe() {
		this.W = 600;
		this.H = 600;

		pane = new Xform();
		ThreeDPane = new Xform();
		pane.setDepthTest(DepthTest.ENABLE);

		buildCamera();
		
		//Set Materials
		PhongMaterial white = new PhongMaterial();
		white.setDiffuseColor(Color.RED);
		white.setDiffuseColor(Color.DARKRED);
		
		buildStadium();
		buildCars();
	}





	/**
	 * 
	 */
	private void buildCars() {
		double currentCarLocation = 0.f;
		carModels = new ArrayList<>();

		for (Car c : cars)
		{
			CarModel cm = new CarModel(c.getType(), c.getCarSize(),c.getModel_id(),c.getColor(),c.getId());
	
			cm.setScaleX(1);
			cm.setScaleY(1);
			cm.setScaleZ(1);
			cm.setRotateX(180);;
			cm.setTranslateY(0);
			cm.setTranslateZ(currentCarLocation - ( ( CAR_SPACE * 5 ) / 2 - 11 ) );

			currentCarLocation += CAR_SPACE;
			
			//Move Cars to Start
			cm.setTranslateX(CARS_INITIAL_X);
			cm.translateXProperty().addListener(new ChangeListener<Number>() {

				@Override
				public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
					if ( Math.abs (newValue.doubleValue() - FINISH_LINE) <= DELTA)
					{
						System.out.println("CAR ## : " + cm.getCarId() + " Has Finished!!");
						if(winnerCar == -1)
							winnerCar = cm.getCarId();
					}
				}
			});
			ThreeDPane.getChildren().add(cm);
			carModels.add(cm);
		}
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
		cameraXform.rx.setAngle(INITIAL_CAMERA_X_ANGLE);
		cameraXform.ry.setAngle(INITIAL_CAMERA_Y_ANGLE);
		
		cameraXform.setTranslateX(400);
		ThreeDPane.getChildren().add(cameraXform);
		
		
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
		((Group)pane).getChildren().add(ThreeDPane);
		scene.setFill(Color.WHITE);
		scene.setCamera(camera);
		
		String [] carNames = new String[5];
		for(int i=0;i<5;i++)
		{
			carNames[i] = Integer.toString(cars.get(i).getId()) + cars.get(i).getModel_id().toString() ;
		}
		
		
	
		betView = new BetView(cont, carNames, this.getTitle(), raceNum);
		betView.open();
		betView.hide();
		scene.setOnKeyTyped((e)->{
			if(e.getCharacter().toLowerCase().equals("b"))
				betView.show();
		});
		handleMouse();
		
		startRace();
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
                System.out.println("X :: " + cameraXform.rx.getAngle() + 
			                		"Y :: " + cameraXform.ry.getAngle() + 
			                		"Z :: " + cameraXform.rz.getAngle() + "\n" 
 
			                		);
           }); // setOnMouseDragged
        
        scene.setOnScroll((e-> {
        // 	 System.out.println("ZOOM :: " + e.getDeltaY());
        	
             cameraXform.setTranslateZ(cameraXform.getTranslateZ() + (e.getDeltaY() ) * ZOOM_SENSTIVTY );
             cameraXform.setTranslateY(cameraXform.getTranslateY() + (e.getDeltaY() ) * ZOOM_SENSTIVTY );
             cameraXform.setTranslateX(cameraXform.getTranslateX() + (e.getDeltaY() ) * ZOOM_SENSTIVTY );
             }));
       
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
        ThreeDPane.getChildren().addAll(axisGroup);
	}
	
	public int getRaceNum()
	{
		return raceNum;
	}
	
	private void buildStadium()
	{
		//Set Shapes
		Box floor = new Box(STADIUM_LONG, 1, STADIUM_WIDTH);
		Box raceTrack = new Box(STADIUM_LONG,1,RACE_TRACK_WIDTH);
		
		Box sky = new Box(STADIUM_LONG,1,STADIUM_LONG);
		
		Box wall0 = new Box(1, STADIUM_HEIGHT, STADIUM_WIDTH);
		Box wall1 = new Box(1, STADIUM_HEIGHT, STADIUM_WIDTH);
		Box wall2 = new Box(STADIUM_LONG, 50, 1);
		Box wall3 = new Box(STADIUM_LONG, 50, 1);
		
		//move walls
		
		//Set Images
		Image floorImg 	= new Image("file:grass.jpg");
		Image skyImg 	= new Image("file:sky.jpg");
		Image wallImg	= new Image("file:wall.jpg");
		Image raceTrackImg = new Image("file:track.jpg");
		
		//Set Materialts
		PhongMaterial floorMat = new PhongMaterial();
		PhongMaterial skyMat   = new PhongMaterial();
		PhongMaterial wallMat  = new PhongMaterial();
		PhongMaterial raceTrackMat  = new PhongMaterial();

		
		floorMat.setDiffuseMap(floorImg);
		raceTrackMat.setDiffuseMap(raceTrackImg);
		skyMat.setDiffuseMap(skyImg);
		wallMat.setDiffuseMap(wallImg);
		
		//Apply Materials to the shapes
		floor.setMaterial(floorMat);
		raceTrack.setMaterial(raceTrackMat);
		
		wall0.setMaterial(wallMat);
		wall1.setMaterial(wallMat);
		wall2.setMaterial(wallMat);
		wall3.setMaterial(wallMat);

		sky.setMaterial(skyMat);
		skyMat.setSpecularPower(64);
		
		//translate what needed 
		raceTrack.setTranslateY(-0.1);
		sky.setTranslateY(-50);
		
		wall0.setTranslateY(-STADIUM_HEIGHT/2);
		wall1.setTranslateY(-STADIUM_HEIGHT/2);
		wall2.setTranslateY(-STADIUM_HEIGHT/2);
		wall3.setTranslateY(-STADIUM_HEIGHT/2);
		
		wall0.setTranslateX(-STADIUM_LONG/2);
		wall1.setTranslateX(STADIUM_LONG/2);
		
		wall2.setTranslateZ(-STADIUM_WIDTH/2);
		wall3.setTranslateZ(STADIUM_WIDTH/2);

        ThreeDPane.getChildren().add(floor);
        ThreeDPane.getChildren().add(raceTrack);
        ThreeDPane.getChildren().add(wall0);
        //ThreeDPane.getChildren().add(wall1);
        ThreeDPane.getChildren().add(wall2);
        ThreeDPane.getChildren().add(wall3);
       // ThreeDPane.getChildren().add(sky);
	}
	
	private void moveCar()
	{
		for (int i=0;i<5;i++)
		{		
			Timeline carPlace = new Timeline(
									new KeyFrame(new Duration( 100000 / (cars.get(i).getSpeed())),
															new KeyValue(carModels.get(i).translateXProperty(), FINISH_LINE)));
			carPlace.setCycleCount(1);
			carPlace.setAutoReverse(false);
			carPlace.setOnFinished((e)->{
				Player.playSound(12);
				System.out.println("Finish in Timeline");
			});
			carPlace.play();
		}
	}
	
	public void startRace()
	{
		Player.playSound(11);
		moveCar();
		System.out.println("::::::RACE ENDED::::");
	}

	public void updateSpeed(int[] newSpeeds) {
		for(int i=0; i<5; i++)
			cars.get(i).setSpeed(newSpeeds[i]);
		
	}

}
