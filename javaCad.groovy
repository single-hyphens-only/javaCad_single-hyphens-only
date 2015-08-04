// Create a cube
import eu.mihosoft.vrl.v3d.STL;

double size =40;
CSG cube = new Cube(	size,// X dimention
			size,// Y dimention
			size//  Z dimention
			).toCSG()
//create a sphere
CSG sphere = new Sphere(size/20*12.5).toCSG()
CSG cylinder = new Cylinder(	100, // Radius
				200, // Height
			         (int)20 //resolution
			         ).toCSG()
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
