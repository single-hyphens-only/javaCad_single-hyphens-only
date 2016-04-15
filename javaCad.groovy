import eu.mihosoft.vrl.v3d.STL;
import eu.mihosoft.vrl.v3d.RoundedCube;
import com.neuronrobotics.bowlerstudio.vitamins.*;
import eu.mihosoft.vrl.v3d.parametrics.*;

ArrayList<CSG> makeSamples(){
	double myStartSize = 40;
	LengthParameter size 		= new LengthParameter("size",myStartSize,[120.0,1.0])
	LengthParameter smallerSize 		= new LengthParameter("smaller size",myStartSize/20*12.5,[120.0,1.0])
	// Create a cube
	CSG cube = new Cube(	size,// X dimention
				size,// Y dimention
				size//  Z dimention
				).toCSG()
	//create a rounded cube
	CSG roundedCube = new RoundedCube(	size,// X dimention
					size,// Y dimention
					size//  Z dimention
					)
					.cornerRadius(size.getMM()/10)
					.toCSG()
						
	//create a sphere
	CSG sphere = new Sphere(smallerSize).toCSG()
	//create a Cylinder
	CSG cylinder = new Cylinder(	size, // Radius at the top
					smallerSize, // Radius at the bottom
					size, // Height
				         (int)80 //resolution
				         ).toCSG()
	//create an extruded polygon
	CSG polygon = Extrude.points(new Vector3d(0, 0, size.getMM()),// This is the  extrusion depth
	                new Vector3d(0,0),// All values after this are the points in the polygon
	                new Vector3d(size.getMM()*2,0),// Bottom right corner
	                new Vector3d(size.getMM()*1.5,size.getMM()),// upper right corner
	                new Vector3d(size.getMM()/2,size.getMM())// upper left corner
	        );		         
	//perform a difference
	// perform union, difference and intersection
	CSG cubePlusSphere = cube.union(sphere);
	CSG cubeMinusSphere = cube.difference(sphere);
	CSG cubeIntersectSphere = cube.intersect(sphere);
	
	//Scale lets you increas or decrease the sise by a scale factor
	CSG cubeMinusSphereSmall = cubeMinusSphere
					.scalex(0.5)
					.scaley(0.5)
					.scalez(0.5)
					.movez(size.getMM()*1.5)
	
	//Move and rotate opperations
	//cubeIntersectSphere = cubeIntersectSphere.move(1,2,3);// vector notation
	cubeIntersectSphere = cubeIntersectSphere
				.movex(1)
				.movey(2)
				.movez(3)
	//rotate
	//cubeIntersectSphere = cubeIntersectSphere.rot(15,20,30);// vector notation
	cubeIntersectSphere = cubeIntersectSphere
				.rotx(15)
				.roty(20)
				.rotz(30)
	//set colors
	cube.setColor(javafx.scene.paint.Color.CYAN);
	
	//make a keepaway shape 
	// this can be a shell or printer keepaway
	// this increases the size by a spacific measurment in mm
	cubeIntersectSphereBigger = cubeIntersectSphere
					.makeKeepaway((double)10.0)
					.movez(size.getMM()*1.5)
	// Load an STL file from a git repo
	// Loading a local file also works here
	File servoFile = ScriptingEngine.fileFromGit(
		"https://github.com/NeuronRobotics/BowlerStudioVitamins.git",
		"BowlerStudioVitamins/stl/servo/smallservo.stl");
	// Load the .CSG from the disk and cache it in memory
	CSG servo  = Vitamins.get(servoFile);
	// Alternantly you can load the file without caching it
	//CSG servo  = STL.file(servoFile.toPath());
	servo=servo
		.movez(size.getMM()*1.5);
	
	//collection of parts
	ArrayList<CSG> parts = new ArrayList<CSG>();
	
	parts.add(cube)
	parts.add(servo)
	parts.add(sphere.movey(size.getMM()*1.5))
	parts.add(cubePlusSphere.movey(size.getMM()*3))
	parts.add(cubeMinusSphere.movey(size.getMM()*5))
	parts.add(cubeMinusSphereSmall.movey(size.getMM()*5))
	parts.add(cubeIntersectSphere.movey(size.getMM()*7))
	parts.add(cubeIntersectSphereBigger.movey(size.getMM()*7))
	parts.add(cylinder.movex(size.getMM()*3))
	parts.add(polygon.movex(size.getMM()*5))
	parts.add(roundedCube.movex(size.getMM()*8))
	for(CSG part:parts){
		part.setRegenerate({ makeSamples()})
	}
	return parts
}