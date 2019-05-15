
class SampleMaker implements IParameterChanged{//collection of parts
	ArrayList<CSG> parts = null;
	boolean loading=false;
	ArrayList<CSG> makeSamples(){
		if(parts !=null){
			return parts
		}
		loading=true;
		double myStartSize = 40;
		LengthParameter size 		= new LengthParameter("size",myStartSize,[120.0,1.0])
		LengthParameter smallerSize 		= new LengthParameter("smaller size",myStartSize/20*12.5,[120.0,1.0])
		CSGDatabase.addParameterListener(size.getName(),this);
		CSGDatabase.addParameterListener(smallerSize.getName(),this);
		// force a value to override the database loaded value
		smallerSize.setMM(size.getMM()/20*12.5);
		// Create a cube
		CSG cube = new Cube(	size,// X dimention
					size,// Y dimention
					size//  Z dimention
					).toCSG()
					
		//BowlerStudioController.addCsg(cube)//displays just this item
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
		CSG cubeIntersectSphereBigger = cubeIntersectSphere
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
		
		
		parts = new ArrayList<CSG>();
		int numVits = 0;
		for(String type: Vitamins.listVitaminTypes()){
			String script = Vitamins.getMeta(type).get("scriptGit")
			//println "Type = "+type+" Loading script from "+ script
			for(String s:Vitamins.listVitaminSizes(type) ){
	
				HashMap<String, Object>  vitaminData = Vitamins.getConfiguration( type,s)
				//println "\tSize = "+s+" "+vitaminData
			}
			
			if(script!=null){
			// 	Grab the first vitamin from the list and load that
				println "Loading "+type+" "+Vitamins
				.listVitaminSizes(type)
				.get(0)
				ArrayList<String> options = Vitamins.listVitaminSizes(type);
				CSG lastPart;
				if(parts.size()>0)
					lastPart= parts.get(parts.size()-1)
				else
					lastPart = cube
				StringParameter typParam = new StringParameter(	type+" Default",
														options.get(0),
														options)
				try{
					CSG vitaminFromScript = Vitamins.get( type,typParam.getStrValue())
					if(vitaminFromScript!=null){
						vitaminFromScript=vitaminFromScript
											.toXMax()
											.movex(-size.getMM()*2)
											.toYMin()
											.movey(lastPart.getMaxY()+5)
						CSGDatabase.addParameterListener(typParam.getName(),this);
						numVits++;		
						if(vitaminFromScript!=null){
							parts.add(vitaminFromScript)
							//BowlerStudioController.addCsg(vitaminFromScript)//displays just this item
						}
					}else{
						println type+" "+typParam.getStrValue()+" Failed "
					}
				}catch (Exception ex){
					println type+" "+typParam.getStrValue()+" exception "
				}
			}else
				println "ERROR no script for "+type
		}		
		
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
		for(int i=0;i<parts.size();i++){
			CSG part=parts.get(i)
			int myIndex=i;
			part.setRegenerate({ 
				makeSamples().get(myIndex)
			})
			.setParameter(size)
		}
		loading=false;
		return parts
	}
	/**
	 * This is a listener for a parameter changing
	 * @param name
	 * @param p
	 */
	 HashMap<String,String> lastValue = new HashMap<>()
	public void parameterChanged(String name, Parameter p){
		//if(p.getStrValue()==null&& p.getValue()==null)
		//	return
		if(loading)
			return
		//if(lastValue.get(name)!=null )
		//	if(p.getStrValue().contains(lastValue.get(name)))
		//		return
		
		lastValue.put(name,p.getStrValue())
		println "CHANGED: "+name
		parts=null
	}
}
CSGDatabase.clear()
return new SampleMaker().makeSamples()