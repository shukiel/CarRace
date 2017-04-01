package CarRaceMVC;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Cylinder;
import JavaFX_3D.Xform;
public class Wheel extends Xform
{
	
	Cylinder c;
	Cylinder rim;
	
	public Wheel(double radius, double width, boolean isLeft)
	{
		c =  new Cylinder(radius, 0.1);		
		rim = new Cylinder (radius, width);
		
		c.setTranslateY( (0.01+  width/2) * (isLeft ? 1 : -1));
		
		PhongMaterial pm = new PhongMaterial();
		pm.setSpecularColor(Color.BLACK);
		pm.setDiffuseMap(new Image("file:wheel.png"));

		PhongMaterial bm = new PhongMaterial();
		bm.setSpecularColor(Color.BLACK);
		bm.setDiffuseColor(Color.BLACK);
		
		c.setMaterial(pm);
		rim.setMaterial(bm);

		
		this.setRotateZ(90);
		this.getChildren().add(rim);
		this.getChildren().add(c);
	}


}
