/**
* A Class to keep track of a bunch's orbit
* @author Ian Bailey
* @version 1.0
*/

import java.lang.Math;
import java.util.*;

public class OrbitTracker<T extends Particle>{

	public boolean converge=false; // flag to indicate whether the bunch is approaching or diverging from the origin
	double displacement=0.0; // distance between bunch origin and current position
	PhysicsVector origin;
	
	public OrbitTracker(){
		converge=false;
		displacement=0.0;
		origin = new PhysicsVector();
	}

	public OrbitTracker(PhysicsVector originIn){
		converge=false;
		displacement=0.0;
		origin = new PhysicsVector(originIn);
	}
	
	/** 
	* Tests whether an orbit has been completed by assuming that the bunch
	* will return to its approximate initial position. 
	* @return true if the bunch has completed an orbit
	*/
	public boolean hasOrbited(Bunch<T> theBunch){
	 	double currentDis=(PhysicsVector.subtract(theBunch.getPosition(),origin)).magnitude();
	 	boolean anOrbit=false;
	 	
	 	if (currentDis>=displacement){
	 		if (converge){
	 			// passed through closest approach and is now diverging from origin
	 			anOrbit=true;
	 		}
	 		converge=false;
	 	}
	 	else{
	 		converge=true; // bunch is approaching origin
	 	}
	 	displacement=currentDis;
	 	return anOrbit ;	 
	}

	public boolean hasOrbited(Proton proton){

		double currentDis=(PhysicsVector.subtract(proton.getPosition(),origin)).magnitude();
		boolean anOrbit=false;

		if (currentDis>=displacement){
			if (converge){
				// passed through closest approach and is now diverging from origin
				anOrbit=true;
			}
			converge=false;
		}
		else{
			converge=true; // bunch is approaching origin
		}
		displacement=currentDis;
		return anOrbit ;
	}
	
	
}
