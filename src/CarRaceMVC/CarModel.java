package CarRaceMVC;

import  CarRaceMVC.Defines.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.Point3D;
import javafx.scene.effect.Light;
import javafx.scene.effect.Light.Spot;
import javafx.scene.effect.Lighting;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.CullFace;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.DrawMode;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.Shape;
import javafx.scene.shape.Shape3D;
import javafx.scene.shape.TriangleMesh;
import javafx.scene.shape.VertexFormat;
import javafx.util.Duration;
import JavaFX_3D.Xform;

public class CarModel extends Xform
{
	private double speed = 10;
	private float sizeFactor;
	private double wheelRotation=0;

	public int getCarId() {
		return carId;
	}
	// Car Params
	private size carSize;
	private carType type;
	private manufacture carManufacture;
	private Color carColor;
	private int carId;
	
	
	
	//Textures
	private Image logo;
	
	//Lighting
	Lighting lighting;
	
	//3D Parameters
	private float roofHeight;
	private float roofWidth;
	private float roofLength;
	private float bodyWidth;
	private float bodyLength;
	private float doorHeight;
	private float fenderHeight;
	private float hoodLength;
	private float hoodWidth;
	private float wheelDiameter;
	private float wheelWidth;
	private float wheelGap;
	private ArrayList<Wheel> wheels ;
	
	//TimeLine for wheel rotation
	private ArrayList<Timeline> tl;

	
	public CarModel(carType ct, size cs, manufacture m, Color c ,int carId){
		super();	
		tl = new ArrayList<Timeline>();
		this.carSize = cs;
		this.type = ct;
		this.carManufacture = m;
		this.carColor = c;
		this.carId = carId;

		setLighting();
		setTextures();
		initCarParam();
		buildCar();
		this.setRotateY(90);
	}

	private void setTextures() {
		switch(carManufacture)
		{
		case JAGUAR:
			logo = new Image("file:toyota.jpg");
			break;
		case MERC:
			logo = new Image("file:merc.png");
			break;
		case NISSAN:
			logo = new Image("file:nissan.jpg");
			break;
		case SUSITA:
			logo = new Image("file:johnDeer.png");
			break;
		}
	}

	private void initCarParam() {
		
		switch(type)
		{
		case SALOON:
			roofHeight		= 1.5f;
			roofWidth 		= 2f;
			roofLength 		= 6f;
			bodyWidth 		= 3f;
			bodyLength 		= 7f;
			doorHeight 		= 2f;
			fenderHeight	= 1f;	
			hoodLength 		= 3f;
			hoodWidth		= 3f;
			wheelDiameter 	= 2f;
			wheelWidth		= 0.5f;
			wheelGap 		=  -0.5f;
			break;
			
		case SPORT:
			roofHeight		= 1f;
			roofWidth 		= 2.5f;
			roofLength 		= 4f;
			bodyWidth 		= 3f;
			bodyLength 		= 7f;
			doorHeight 		= 2f;
			fenderHeight	= 0.5f;	
			hoodLength 		= 4f;
			hoodWidth		= 1f;
			wheelDiameter 	= 2f;
			wheelWidth		= 1f;
			wheelGap 		=  -0.5f;
			break;		
			
		case JEEP:
			roofHeight		= 2.f;
			roofWidth 		= 3f;
			roofLength 		= 6f;
			bodyWidth 		= 4f;
			bodyLength 		= 9f;
			doorHeight 		= 3f;
			fenderHeight	= 2f;	
			hoodLength 		= 4f;
			hoodWidth		= 4f;
			wheelDiameter 	= 4f;
			wheelWidth		= 1f;
			wheelGap 		= 1.5f;
			break;
		}
	}
	
	public void setSpeed(double spd)
	{
		this.speed = spd;
	}
	
	public void buildCar()
	{
		
		// =============================  ROOF ============================= // 
		
		TriangleMesh roofMesh = new TriangleMesh(VertexFormat.POINT_TEXCOORD);
		
		roofMesh.getPoints().addAll(
				/* X */ -roofWidth/2.f,	 /* Y */	roofHeight + wheelDiameter / 2  + wheelGap + doorHeight, /* Z */ - roofLength/2,	//PT0
				/* X */ roofWidth/2.f, 	 /* Y */	roofHeight + wheelDiameter / 2  + wheelGap + doorHeight, /* Z */ - roofLength/2, 	//PT1
				/* X */ -roofWidth/2.f,  /* Y */	roofHeight + wheelDiameter / 2  + wheelGap + doorHeight, /* Z */  roofLength/2, 	//PT2
				/* X */ roofWidth/2.f, 	 /* Y */	roofHeight + wheelDiameter / 2  + wheelGap + doorHeight, /* Z */  roofLength/2  	//PT3
				);
		
		roofMesh.getTexCoords().addAll(
				1, 0,   //  t3
				1, 1, 	 //  t1
				0, 0, 	 //  t0
				0, 1  	//  t2
				);
		
		roofMesh.getFaces().addAll(
				1,1, 0,0,2,2,
				3,3, 1,1 ,2,2
				);
		
		// =============================  Side Windows ============================= //	
		
		TriangleMesh sideWindowL = new TriangleMesh(VertexFormat.POINT_TEXCOORD);
		
		sideWindowL.getPoints().addAll(
				/* X */ -roofWidth/2.f,	 /* Y */	roofHeight + wheelDiameter / 2  + wheelGap + doorHeight, /* Z */ - roofLength/2,	//PT0
				/* X */ -bodyWidth/2.f,	 /* Y */	wheelDiameter / 2  + wheelGap + doorHeight, 			 /* Z */ - bodyLength/2,	//PT1
				/* X */ -roofWidth/2.f,  /* Y */	roofHeight + wheelDiameter / 2  + wheelGap + doorHeight, /* Z */  roofLength/2, 	//PT2
				/* X */ -bodyWidth/2.f,  /* Y */	wheelDiameter / 2  + wheelGap + doorHeight,				 /* Z */  bodyLength/2 	//PT3

				);
		
		sideWindowL.getTexCoords().addAll(
				0, 0,  //  t0
				1, 0,  //  t1
				0, 1,  //  t2
		        1, 1   //  t3
				);
		
		sideWindowL.getFaces().addAll(
				0,1, 1,3,2,3,
				3,3, 2,2, 1,1
				);
		
		TriangleMesh sideWindowR = new TriangleMesh(VertexFormat.POINT_TEXCOORD);
		
		sideWindowR.getPoints().addAll(
				/* X */ roofWidth/2.f,	 /* Y */	roofHeight + wheelDiameter / 2  + wheelGap + doorHeight, /* Z */ - roofLength/2,	//PT0
				/* X */ bodyWidth/2.f,	 /* Y */	wheelDiameter / 2  + wheelGap + doorHeight, 			 /* Z */ - bodyLength/2,	//PT1
				/* X */ roofWidth/2.f,  /* Y */		roofHeight + wheelDiameter / 2  + wheelGap + doorHeight, /* Z */  roofLength/2, 	//PT2
				/* X */ bodyWidth/2.f,  /* Y */		wheelDiameter / 2  + wheelGap + doorHeight,				 /* Z */  bodyLength/2 	//PT3

				);
		
		sideWindowR.getTexCoords().addAll(
				0, 0,  //  t0
				1, 0,  //  t1
				0, 1,  //  t2
		        1, 1   //  t3
				);
		
		sideWindowR.getFaces().addAll(
				 1,1,0,0,2,2,
                 2,2,3,3,1,1
				);
		
		// =============================  Rear Window ============================= //	
		TriangleMesh rearWindow = new TriangleMesh(VertexFormat.POINT_TEXCOORD);
		
		rearWindow.getPoints().addAll(
				/* X */ -roofWidth/2.f,	 /* Y */	roofHeight + wheelDiameter / 2  + wheelGap + doorHeight, /* Z */ - roofLength/2,	//PT0
				/* X */ -bodyWidth/2.f,	 /* Y */	wheelDiameter / 2  + wheelGap + doorHeight, 			 /* Z */ - bodyLength/2,	//PT1
				/* X */ roofWidth/2.f,  /* Y */		roofHeight + wheelDiameter / 2  + wheelGap + doorHeight, /* Z */ - roofLength/2, 	//PT2
				/* X */ bodyWidth/2.f,  /* Y */		wheelDiameter / 2  + wheelGap + doorHeight,				 /* Z */ - bodyLength/2 	//PT3

				);
		
		rearWindow.getTexCoords().addAll(
				0, 0,  //  t0
				1, 0,  //  t1
				0, 1,  //  t2
		        1, 1   //  t3
				);
		
		rearWindow.getFaces().addAll(
				 1,1,0,0,2,2,
                 2,2,3,3,1,1
				);
		// =============================  Rear body ============================= //	
		TriangleMesh rearBody = new TriangleMesh(VertexFormat.POINT_TEXCOORD);
		
		rearBody.getPoints().addAll(
				/* X */ -bodyWidth/2.f,	 /* Y */	wheelDiameter / 2  + wheelGap + doorHeight, 			 /* Z */ - bodyLength/2,	//PT1
				/* X */ -bodyWidth/2.f,	 /* Y */	wheelDiameter / 2  + wheelGap, 							 /* Z */ - bodyLength/2,	//PT1
				/* X */ bodyWidth/2.f,	 /* Y */	wheelDiameter / 2  + wheelGap + doorHeight, 			 /* Z */ - bodyLength/2,	//PT1
				/* X */ bodyWidth/2.f,  /* Y */		wheelDiameter / 2  + wheelGap,							 /* Z */ - bodyLength/2 	//PT3

				);
		
		rearBody.getTexCoords().addAll(
				0, 0,  //  t0
				1, 0,  //  t1
				0, 1,  //  t2
		        1, 1   //  t3
				);
		
		rearBody.getFaces().addAll(
				 1,1,0,0,2,2,
                 2,2,3,3,1,1
				);
		
		
		// =============================  Front Window ============================= //	
		TriangleMesh frontWindow = new TriangleMesh(VertexFormat.POINT_TEXCOORD);
		
		frontWindow.getPoints().addAll(
				/* X */ -roofWidth/2.f,	 /* Y */	roofHeight + wheelDiameter / 2  + wheelGap + doorHeight, /* Z */  roofLength/2,	//PT0
				/* X */ -bodyWidth/2.f,	 /* Y */	wheelDiameter / 2  + wheelGap + doorHeight, 			 /* Z */  bodyLength/2,	//PT1
				/* X */ roofWidth/2.f,  /* Y */		roofHeight + wheelDiameter / 2  + wheelGap + doorHeight, /* Z */  roofLength/2, //PT2
				/* X */ bodyWidth/2.f,  /* Y */		wheelDiameter / 2  + wheelGap + doorHeight,				 /* Z */  bodyLength/2 	//PT3

				);
		
		frontWindow.getTexCoords().addAll(
				1, 1,   //  t3
				1, 0,  //  t1
				0, 1,  //  t2
				0, 0  //  t0
		        
				);
		
		frontWindow.getFaces().addAll(
				3,3,2,2,1,1,
				0,0,1,1,2,2
				);
		
		// =============================  Hood  ============================= //	
				TriangleMesh hood = new TriangleMesh(VertexFormat.POINT_TEXCOORD);
				
				hood.getPoints().addAll(
						
						/* X */ -bodyWidth/2.f,	 /* Y */	wheelDiameter / 2  + wheelGap + doorHeight, 			 /* Z */  bodyLength/2,	//PT0
						/* X */ -hoodWidth/2.f,	 /* Y */	wheelDiameter / 2  + wheelGap + fenderHeight, 			 /* Z */  bodyLength/2 + hoodLength,	//PT1
						/* X */  bodyWidth/2.f,	 /* Y */	wheelDiameter / 2  + wheelGap + doorHeight, 			 /* Z */  bodyLength/2,	//PT2
						/* X */  hoodWidth/2.f,	 /* Y */	wheelDiameter / 2  + wheelGap + fenderHeight, 			 /* Z */  bodyLength/2 + hoodLength	//PT3

						);
				
				hood.getTexCoords().addAll(
						0, 0,  //  t0
						1, 0,  //  t1
						0, 1,  //  t2
				        1, 1   //  t3
						);
				
				hood.getFaces().addAll(
						3,3,2,2,1,1,
						0,0,1,1,2,2
						);
		
				
				// =============================  Fender  ============================= //
				TriangleMesh fender = new TriangleMesh(VertexFormat.POINT_TEXCOORD);
				
				fender.getPoints().addAll(
						/* X */ -hoodWidth/2.f,	 /* Y */	wheelDiameter / 2  + wheelGap + fenderHeight, 			 /* Z */  bodyLength/2 + hoodLength,	//PT1
						/* X */ -hoodWidth/2.f,	 /* Y */	wheelDiameter / 2  + wheelGap,				 			 /* Z */  bodyLength/2 + hoodLength,	//PT1
						/* X */  hoodWidth/2.f,	 /* Y */	wheelDiameter / 2  + wheelGap + fenderHeight, 			 /* Z */  bodyLength/2 + hoodLength,	//PT3
						/* X */  hoodWidth/2.f,	 /* Y */	wheelDiameter / 2  + wheelGap , 			 			 /* Z */  bodyLength/2 + hoodLength	//PT3
						);
				
				fender.getTexCoords().addAll(
						0, 0,  //  t0
						1, 0,  //  t1
						0, 1,  //  t2
				        1, 1   //  t3
						);
				
				fender.getFaces().addAll(
						3,3,2,2,1,1,
						0,0,1,1,2,2
						);
					
				
				

				// =============================  Wing L  ============================= //
				
				TriangleMesh wingL = new TriangleMesh(VertexFormat.POINT_TEXCOORD);
				
				wingL.getPoints().addAll(
						/* X */ -hoodWidth/2.f,	 /* Y */	wheelDiameter / 2  + wheelGap + fenderHeight, 			 /* Z */  bodyLength/2 + hoodLength,	//PT0
						/* X */ -bodyWidth/2.f,	 /* Y */	wheelDiameter / 2  + wheelGap + doorHeight, 			 /* Z */  bodyLength/2,					//PT2
						/* X */ -hoodWidth/2.f,	 /* Y */	wheelDiameter / 2  + wheelGap,				 			 /* Z */  bodyLength/2 + hoodLength,	//PT1
						/* X */ -bodyWidth/2.f,	 /* Y */	wheelDiameter / 2  + wheelGap , 			 			 /* Z */  bodyLength/2					//PT3
						);
				
				wingL.getTexCoords().addAll(
						0, 0,  //  t0
						1, 0,  //  t1
						0, 1,  //  t2
				        1, 1   //  t3
						);
				
				wingL.getFaces().addAll(
						3,3,2,2,1,1,
						0,0,1,1,2,2
						);

				
// =============================  DOORL  ============================= //
				
				TriangleMesh doorL = new TriangleMesh(VertexFormat.POINT_TEXCOORD);
				
				doorL.getPoints().addAll(
						/* X */ -bodyWidth/2.f,	 /* Y */	wheelDiameter / 2  + wheelGap , 			 			 /* Z */  bodyLength/2,					//PT0
						/* X */ -bodyWidth/2.f,	 /* Y */	wheelDiameter / 2  + wheelGap + doorHeight, 			 /* Z */  bodyLength/2,					//PT2
						/* X */ -bodyWidth/2.f,	 /* Y */	wheelDiameter / 2  + wheelGap,				 			 /* Z */  -bodyLength/2,					//PT1
						/* X */ -bodyWidth/2.f,	 /* Y */	wheelDiameter / 2  + wheelGap + doorHeight, 			 /* Z */  -bodyLength/2					//PT3
						);
				
				doorL.getTexCoords().addAll(
						0, 0,  //  t0
						1, 0,  //  t1
						0, 1,  //  t2
				        1, 1   //  t3
						);
				
				doorL.getFaces().addAll(
						3,3,2,2,1,1,
						0,0,1,1,2,2
						);

				
				// =============================  Wing L  ============================= //
				
				TriangleMesh wingR = new TriangleMesh(VertexFormat.POINT_TEXCOORD);
				
				wingR.getPoints().addAll(
						/* X */ hoodWidth/2.f,	 /* Y */	wheelDiameter / 2  + wheelGap + fenderHeight, 			 /* Z */  bodyLength/2 + hoodLength,	//PT0
						/* X */ bodyWidth/2.f,	 /* Y */	wheelDiameter / 2  + wheelGap + doorHeight, 			 /* Z */  bodyLength/2,					//PT2
						/* X */ hoodWidth/2.f,	 /* Y */	wheelDiameter / 2  + wheelGap,				 			 /* Z */  bodyLength/2 + hoodLength,	//PT1
						/* X */ bodyWidth/2.f,	 /* Y */	wheelDiameter / 2  + wheelGap , 			 			 /* Z */  bodyLength/2					//PT3
						);
				
				wingR.getTexCoords().addAll(
						0, 0,  //  t0
						1, 0,  //  t1
						0, 1,  //  t2
				        1, 1   //  t3
						);
				
				wingR.getFaces().addAll(
						 1,0,2,1,3,3,
		                 2,3,1,2,0,0
						);

				
				// 	=============================  DOORL  ============================= //
				
				TriangleMesh doorR = new TriangleMesh(VertexFormat.POINT_TEXCOORD);
				
				doorR.getPoints().addAll(
						/* X */ bodyWidth/2.f,	 /* Y */	wheelDiameter / 2  + wheelGap , 			 			 /* Z */  bodyLength/2,					//PT0
						/* X */ bodyWidth/2.f,	 /* Y */	wheelDiameter / 2  + wheelGap + doorHeight, 			 /* Z */  bodyLength/2,					//PT2
						/* X */ bodyWidth/2.f,	 /* Y */	wheelDiameter / 2  + wheelGap,				 			 /* Z */  -bodyLength/2,					//PT1
						/* X */ bodyWidth/2.f,	 /* Y */	wheelDiameter / 2  + wheelGap + doorHeight, 			 /* Z */  -bodyLength/2					//PT3
						);
				
				doorR.getTexCoords().addAll(
						0, 0,  //  t0
						1, 0,  //  t1
						0, 1,  //  t2
				        1, 1   //  t3
						);
				
				doorR.getFaces().addAll(
						 1,0,2,1,3,3,
		                 2,3,1,2,0,0
						);

		// =============================  Wheels  ============================= //
				wheels = new ArrayList<>();
				
				wheels.add (new Wheel(wheelDiameter/2, wheelWidth,true));
				wheels.add (new Wheel(wheelDiameter/2, wheelWidth,false));
				wheels.add (new Wheel(wheelDiameter/2, wheelWidth,true));
				wheels.add (new Wheel(wheelDiameter/2, wheelWidth,false));
				
				
			
			
				
				
				
				//Move Wheels to place
				wheels.get(0).setTranslate(-bodyWidth/2 , wheelDiameter/2, 0.7f * (bodyLength/2 + hoodLength/2));
				wheels.get(1).setTranslate( bodyWidth/2 , wheelDiameter/2, 0.7f * (bodyLength/2 + hoodLength/2));
				
				wheels.get(2).setTranslate(-bodyWidth/2 , wheelDiameter/2, -0.4f * (bodyLength/2 + hoodLength/2));
				wheels.get(3).setTranslate( bodyWidth/2 , wheelDiameter/2, -0.4f * (bodyLength/2 + hoodLength/2));
				
				//Create Axles
				Cylinder frontCylinder = new Cylinder(0.1, bodyWidth);
				Cylinder rearCylinder = new Cylinder(0.1, bodyWidth);
				PhongMaterial cylinderMat = new PhongMaterial();
				cylinderMat.setDiffuseColor(Color.BLACK);
				cylinderMat.setSpecularColor(Color.BLACK);

				frontCylinder.setMaterial(cylinderMat);
				rearCylinder.setMaterial(cylinderMat);
				
				frontCylinder.setRotate(90);
				rearCylinder.setRotate(90);
				frontCylinder.setTranslateZ( 0.7f * (bodyLength/2 + hoodLength/2));
				rearCylinder.setTranslateZ( -0.4f * (bodyLength/2 + hoodLength/2));
				
				frontCylinder.setTranslateY(wheelDiameter/2);
				rearCylinder.setTranslateY(wheelDiameter/2);
				this.getChildren().add(frontCylinder);
				this.getChildren().add(rearCylinder);
				
		//Create the MeshViews
	
		HashMap<String, MeshView> meshViewMap = new HashMap<>();
		
		meshViewMap.put("roof",			 new MeshView(roofMesh));
		meshViewMap.put("sideWindowL",	 new MeshView(sideWindowL));
		meshViewMap.put("sideWindowR",	 new MeshView(sideWindowR));
		meshViewMap.put("rearWindow",	 new MeshView(rearWindow));
		meshViewMap.put("frontWindow",	 new MeshView(frontWindow));
		meshViewMap.put("hood",	 		 new MeshView(hood));
		meshViewMap.put("fender",		 new MeshView(fender));
		meshViewMap.put("wingL",		 new MeshView(wingL));
		meshViewMap.put("doorL",		 new MeshView(doorL));
		meshViewMap.put("wingR",		 new MeshView(wingR));
		meshViewMap.put("doorR",		 new MeshView(doorR));
		meshViewMap.put("rearBody",		 new MeshView(rearBody));
		

		
		//add the components to the group
		for (MeshView mv : meshViewMap.values())
		{
			mv.setDrawMode(DrawMode.FILL);
			mv.setEffect(lighting);
			setTexColor(mv, carColor, "");
			this.getChildren().add(mv);	
		}
		
		setTexColor(meshViewMap.get("frontWindow"), Color.GRAY, "");
		setTexColor(meshViewMap.get("rearWindow"), Color.GRAY, "");
		
		setTexColor(meshViewMap.get("sideWindowL"), carColor.desaturate()
.desaturate()
, "");
		setTexColor(meshViewMap.get("sideWindowR"), carColor.desaturate()
.desaturate()
, "");
		
		setTexColor(meshViewMap.get("roof"), carColor, "logo");

		//add the wheels to the group
		for (Wheel w  : wheels)
		{
			this.getChildren().add(w);
		}
		
	}
	
	private void setTexColor(Shape3D shape, Color c, String imagePath )
	{
		PhongMaterial pm = new PhongMaterial();
		if (imagePath.equals("logo"))
		{
			pm.setDiffuseMap(logo);
			//pm.setDiffuseColor(Color.BLACK);
			pm.setSpecularColor(Color.BLACK);
			shape.setMaterial(pm);
			return;
		}
		pm.setDiffuseColor(c);
		pm.setSpecularColor(c.darker());
		shape.setMaterial(pm);
		
	}
	private void setLighting() {
		Spot light0 = new Light.Spot();
        light0.setX(500);
        light0.setY(0);
        light0.setZ(500);
        
        light0.setColor(Color.GREEN);
		lighting = new Lighting();
        lighting.setSurfaceScale(5.0);
		lighting.setLight(light0);
	}

	public void rotateWheels(double speed)
	{
		System.out.println("StartRotate");
		for (int i = 0 ; i< 4 ; ++i)
	 	{
			KeyValue kv0 = new KeyValue(wheels.get(i).ry.angleProperty(), 360);
			KeyValue kv1 = new KeyValue(wheels.get(i).ry.angleProperty(), 0);
			KeyFrame kf0 = new KeyFrame(Duration.ZERO, kv0);
			KeyFrame kf1 = new KeyFrame(Duration.seconds(100/speed), kv1);
			
			Timeline tmp = new Timeline(kf0, kf1);
			tmp.setCycleCount(Timeline.INDEFINITE);
			tmp.setAutoReverse(false);
			tmp.play();
			tl.add(tmp);
			
			
		}
	}
	
	public void stopWheels()
	{
		for ( Timeline tmp : tl)
			tmp.stop();
	}
	
}
