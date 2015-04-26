// Create a cube
double size =90;
CSG cube = new Cube(size).toCSG()
//create a sphere
CSG sphere = new Sphere(size/20*12.5).toCSG()
//perform a difference
// perform union, difference and intersection
CSG cubePlusSphere = cube.union(sphere);
CSG cubeMinusSphere = cube.difference(sphere);
CSG cubeIntersectSphere = cube.intersect(sphere);

// translate geometries to prevent overlapping 
CSG union = cube.
        union(sphere.transformed(Transform.unity().translateX(size*1.5))).
        union(cubePlusSphere.transformed(Transform.unity().translateX(size*3))).
        union(cubeMinusSphere.transformed(Transform.unity().translateX(size*5))).
        union(cubeIntersectSphere.transformed(Transform.unity().translateX(size*7)));