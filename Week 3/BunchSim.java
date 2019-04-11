/**
* A Class to simulate a bunch of protons in a (mostly) uniform magnetic field.
* @author Ian Bailey
* @version 1.2
*/

import java.lang.Math;
import java.util.*;
import java.io.*;

public class BunchSim {
	
	public final static double k= 1/(4*Math.PI*8.85418782E-12);// 1/4\pi\epsilon_0
	
	/**
	* Main method to simulate the motion of a single bunch of protons in a uniform B field (with errors)
	* and with optional accelerating  E field.
	*
	*/
	public static void main (String[] args) 
	{
   	 	
		
   	 	// Simulation controls
   	 	double timeStep=0.00001; // time step in seconds
   	 	long maxTime=3000; // maximum simulation time in seconds
   	 	int maxRev=1000; // maximum number of orbital revolutions for proton bunch
   	 	String outFileName="week2.data"; //output file name for bunch trajectory data
   	 	char rDist='U'; // the random distribution to use (U for uniform, G for Gaussian)
   	 	int pStep=1000; // print output on every pStep time steps
   	 	boolean checkSpread=true; // whether to check for spreadX == spreadY
   	 	double spreadTol=0.1; // the tolerance to use in checking for the spreads to be equal
   	 	
   	 	// E and B parameters
   	 	double bMag=1.0e-7; // magnetic flux density in Tesla
   	 	double bErr=1.0e-1; // fractional difference in magnetic flux density between positive and negative x regions
		EMField theField = new EMField(); 
   	 	
		// Bunch parameters 
   	 	int nProtons = 100;
   	 	double pSpeed=0.1; // initial average speed in ms^-1
   	 	double pEnergy = 0.5*Proton.pMass*(pSpeed)*(pSpeed); // initial average energy of particles in bunch (non-relativistic)
   	 	double pESpread= 0.00*pEnergy; // absolute spread in energy of particles in the bunch
   	 	double frequency=(Proton.pCharge*bMag/(2*Math.PI*Proton.pMass)); // expected frequency of bunch orbit
		double radius=Proton.pMass*pSpeed/(Proton.pCharge*bMag); // expected radius of bunch orbit
   	 	
   	 	PhysicsVector pDirn = new PhysicsVector(0,1,0); // direction of bunch at start of simulation
   	 	PhysicsVector pDirnSpread=new PhysicsVector(0,0,0); // relative spread in the direction of the bunch at the start of the simulation
   	 	PhysicsVector pOrigin= new PhysicsVector(); // nominal average position of bunch at start of simulation
   	 	PhysicsVector pSpread = new PhysicsVector(0.1, 0*radius/100, 0); // absolute spread in position of particles in bunch
   	 	
   	 	// Open a file to save the bunch positions in
   	 	final PrintWriter outFile;
   	 	PrintWriter tryFile=null;
   	 	try{
   	 		tryFile = new PrintWriter(outFileName);
   	 	}
   	 	catch (IOException e){
   	 		System.out.println("Exception opening file: " + e.getMessage());
   	 		System.exit(4);	
   	 	}
   	 	finally{
   	 		outFile=tryFile;
   	 	}
   	 	
   	 	//Make a bunch of protons 
   	 	Proton aProton = new Proton();
   	 	Bunch<Proton> pBunch = new Bunch<Proton>();
   	 	
   	 	for (int i=1; i<=nProtons; i++){
   	 		pBunch.addParticle(new Proton(aProton));
   	 	}
   	 		
   	 	
   	 	pBunch.setDist(rDist);
   	 	pBunch.setPosition(pOrigin,pSpread);
   	 	pBunch.setVelocity(pDirn,pDirnSpread,pEnergy,pESpread);
   	 	System.out.println(pBunch);
   	 	
   	 	// Set up the simulation
		double time=0.0; 
		int nRev=0; // number of orbits the proton bunch completes
		double lastTime=0.0; // The time at which the last 'turn' ended
		double spreadX=0.0;
		double spreadY=0.0;
		OrbitTracker<Proton> bunchOrbit = new OrbitTracker<Proton>(pOrigin); // Start tracking the orbit of the bunch
	
		
		while(nRev<maxRev && time < maxTime){// Loop over time
			time+=timeStep;
			
			 //Move all particles in the bunch 
			 Iterator<Proton> bunchIt = pBunch.iterator();
			 while(bunchIt.hasNext()){
			 	Proton proton=bunchIt.next();
				theField.setElectric(new PhysicsVector()); // Set up the cyclotron fields
				theField.setMagnetic(cyclotronBField(proton.getPosition(),time,bMag,bMag*(1.0-bErr)));
				
				proton.update(timeStep, theField.getAcceleration(proton));
			 }
				
				
			
			
			if (((int)(time/timeStep))%pStep==1){ // Write out the bunch position at intervals
				outFile.println((pBunch.getPosition()).returnSimpleString());
			}
			
			if (bunchOrbit.hasOrbited(pBunch)){
				// bunch has completed an orbit
				nRev+=1;
				System.out.printf("Revolution number %3d at time %10.6f s\n", nRev, time);
				System.out.printf("Period of this revolution is  %10.6f s\n",(time-lastTime)); 
				System.out.println(pBunch);
				System.out.println();
				lastTime=time;
				PhysicsVector spread= pBunch.getSpreadMax();
				spreadX=spread.getX();
				spreadY=spread.getY();
				if (checkSpread && spreadTest(spreadX, spreadY,spreadTol)){ // Check the spreads and quit if they are equal within tolerance.
					break;
				}
			}
		}                          
		
		// After simulation, calculate the average periodic orbit 
		double calcPeriod=1/frequency;
		double simPeriod=time/nRev;
		double radius2=Proton.pMass*(pBunch.getVelocity()).magnitude()/(Proton.pCharge*bMag);
		System.out.println("\n\n");
		System.out.println("Magnetic flux density " + bMag + " Tesla ");
		System.out.println("Calculated period of orbit " + calcPeriod + " s");
		System.out.println("Simulated period of orbit " + simPeriod + " s");  
		System.out.println("Fractional difference " + (calcPeriod-simPeriod)/calcPeriod + "\n");
		System.out.println("Initial radius of orbit (calculated) " + radius + " m");
		System.out.println("Final radius of orbit (calculated) " + radius2 + " m\n\n");
		
		outFile.close(); 
		System.exit(0); // Exit (stopping the timer thread that would otherwise keep the process running)
	}
	
	/**
	* Method to test whether spreads in X and Y are equal within tolerance
	*
	* @param spreadX 	Bunch spread in X
	* @param spreadY	Bunch spread in Y
	* @param spreadTol	relative difference in spreadX and spreadY
	* @return true if spreads are equal within tolerance. False otherwise.
	*/
	public static boolean spreadTest(double spreadX, double spreadY, double spreadTol)
	{
		return ((Math.abs(spreadX-spreadY)<=(spreadTol*spreadX))&&
			(Math.abs(spreadX-spreadY)<=(spreadTol*spreadY)));
	}
	
	
	
	/**
	* Method for calculating the B field that particles experience in a cylotron with an error in the magnet so that 
	* it is not entirely uniform. The B field is in the +ve z direction
	* We assume the field has value b1 in region x>0 and value b2 in region x=<0
	* @param position the position of the particle 
	* @param time the time at which we're evaluating the field (not used)
	* @param b1 the amplitude of the B field in the region x>0
	* @param b2 the amplitude of the B field in the region x=<0
	*
	*/
	public static PhysicsVector cyclotronBField(PhysicsVector position,  double time, double b1, double b2){
	 	PhysicsVector bField = new PhysicsVector(0,0,1);
	 	
	 	if (position.getX()>0){
	 	 	bField.scale(b1);
		}
		else{
			bField.scale(b2);
		}
	 	return bField; 
	}	
	
}

