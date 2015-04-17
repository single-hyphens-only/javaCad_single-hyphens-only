// Create a cube
CSG cube = new Cube(20).toCSG()
//create a sphere
CSG sphere = new Sphere(12.5).toCSG()
//perform a difference
cube.difference(sphere)