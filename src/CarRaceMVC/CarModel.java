package CarRaceMVC;

import  CarRaceMVC.Defines.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.CullFace;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.DrawMode;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;
import javafx.scene.shape.VertexFormat;
import JavaFX_3D.Xform;

public class CarModel extends Xform
{
	private double speed = 10;
	
	private float sizeFactor;
	
	private double wheelRotation=0;

	// Car Params
	private size carSize;
	private carType type;
	private manufacture carManufacture;
	private Color carColor;
	
	//Textures
	private PhongMaterial windows;
	private PhongMaterial roof;
	private PhongMaterial body;
	private PhongMaterial fender;
	
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
	
	//Timer for rotation
	private Timer rotateTimer;
	
	
	public CarModel(carType ct, size cs, manufacture m, Color c ){
		super();	
		this.carSize = cs;
		this.type = ct;
		this.carManufacture = m;
		this.carColor = c;
		setTextures();
		initCarParam();
		buildCar();

	}

	private void setTextures() {
		//TODO :: Make it look nicer
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
			roofHeight		= 2.5f;
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
				0, 0,  //  t0
				1, 0,  //  t1
				0, 1,  //  t2
		        1, 1   //  t3
				);
		
		roofMesh.getFaces().addAll(
				1,1, 0,0,2,2,
				3,3, 1,2 ,2,1
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
				ArrayList<Wheel> wheels  = new ArrayList<>();
				
				wheels.add (new Wheel(wheelDiameter/2, wheelWidth));
				wheels.add (new Wheel(wheelDiameter/2, wheelWidth));
				wheels.add (new Wheel(wheelDiameter/2, wheelWidth));
				wheels.add (new Wheel(wheelDiameter/2, wheelWidth));
				
				
				
				//Set Wheel rotation
				rotateTimer = new Timer();
				rotateTimer.scheduleAtFixedRate(new TimerTask() {
					
					@Override
					public void run() {
						for (Wheel wheel : wheels )
						{
							if (speed > 0)
								wheel.setRotateY(-(wheelRotation++));
						}
					}
				}, 100, (long) (100/(speed)));
				
				//Move Wheels to place
				wheels.get(0).setTranslate(-bodyWidth/2 , wheelDiameter/2, 0.7f * (bodyLength/2 + hoodLength/2));
				wheels.get(1).setTranslate( bodyWidth/2 , wheelDiameter/2, 0.7f * (bodyLength/2 + hoodLength/2));
				
				wheels.get(2).setTranslate(-bodyWidth/2 , wheelDiameter/2, -0.4f * (bodyLength/2 + hoodLength/2));
				wheels.get(3).setTranslate( bodyWidth/2 , wheelDiameter/2, -0.4f * (bodyLength/2 + hoodLength/2));
				
				//Create Axles
				Cylinder frontCylinder = new Cylinder(0.05, bodyWidth);
				Cylinder rearCylinder = new Cylinder(0.05, bodyWidth);
				PhongMaterial cylinderMat = new PhongMaterial();
				cylinderMat.setDiffuseColor(Color.BLACK);
				
				frontCylinder.setMaterial(cylinderMat);
				rearCylinder.setMaterial(cylinderMat);
				
				frontCylinder.setRotate(90);
				rearCylinder.setRotate(90);
				frontCylinder.setTranslateZ( 0.7f * (bodyLength/2 + hoodLength/2));
				rearCylinder.setTranslateZ( -0.4f * (bodyLength/2 + hoodLength/2));
				
				frontCylinder.setTranslateY(wheelDiameter/2);
				rearCylinder.setTranslateY(wheelDiameter/2);
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
		
		//Get images
		Image frontWindowImg = new Image ("file:frontWindow.jpg");
		
		
		//Set Materials
		PhongMaterial frontWindowMat = new PhongMaterial();
		frontWindowMat.setDiffuseMap(frontWindowImg);
		
		
		//Edit the specific components parameters
		meshViewMap.get("frontWindow").setMaterial(frontWindowMat);
		
		//add the components to the group
		for (MeshView mv : meshViewMap.values())
		{
//			mv.setCullFace(CullFace.NONE);
			mv.setDrawMode(DrawMode.FILL);
			mv.setMouseTransparent(false);
			mv.setOpacity(1);
			
			PhongMaterial m = new PhongMaterial();
			
			m.setDiffuseColor(carColor);
			m.setSpecularColor(carColor);
			
			mv.setMaterial(m);
			
			this.getChildren().add(mv);	
		}
		
		//add the wheels to the group
		for (Wheel w  : wheels)
		{
			this.getChildren().add(w);
		}
		this.getChildren().addAll(frontCylinder, rearCylinder);
	}
}
