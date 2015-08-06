// Create a cube
import eu.mihosoft.vrl.v3d.STL;

double size =40;
CSG cube = new Cube(	size,// X dimention
			size,// Y dimention
			size//  Z dimention
			).toCSG()
//create a sphere
CSG sphere = new Sphere(size/20*12.5).toCSG()
CSG cylinder = new Cylinder(	100, // Radius at the top
				100, // Radius at the bottom
				200, // Height
			         (int)20 //resolution
			         ).toCSG()
CSG polygon = Extrude.points(new Vector3d(0, 0, 5),// This is the  extrusion depth
                new Vector3d(0,0),// All values after this are the points in the polygon
                new Vector3d(10,0),// Bottom right corner
                new Vector3d(7,5),// upper right corner
                new Vector3d(3,5)// upper left corner
        );		         
//perform a difference
// perform union, difference and intersection
CSG cubePlusSphere = cube.union(sphere);
CSG cubeMinusSphere = cube.difference(sphere);
CSG cubeIntersectSphere = cube.intersect(sphere);
// translate geometries to prevent overlapping 
CSG union = cube.
        union(sphere.transformed(Transform.unity().translateY(size*1.5))).
        union(cubePlusSphere.transformed(Transform.unity().translateY(size*3))).
        union(cubeMinusSphere.transformed(Transform.unity().translateY(size*5))).
        union(cubeIntersectSphere.transformed(Transform.unity().translateY(size*7)));
