package CarRaceMVC;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Cylinder;
import JavaFX_3D.Xform;

public class Wheel extends Xform
{
	
	Cylinder c;
	
	public Wheel(double radius, double width)
	{
		c =  new Cylinder(radius, width);
		this.setRotateZ(90);
		PhongMaterial pm = new PhongMaterial();
		pm.setSpecularColor(Color.BLACK);
		pm.setDiffuseMap(new Image("file:wheel.png"));
		
		c.setMaterial(pm);
		this.getChildren().add(c);
	}
}
