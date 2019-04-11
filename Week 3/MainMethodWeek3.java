/**
 * Main class used to simulate the proton orbiting in a magnetic and electric field
 *
 * @author - Adam Shelbourne
 * @version - 1.4
 */

import java.util.Scanner;

public class MainMethodWeek3 {

    //initialising the constants needed
    public final static double k = 1 / (4 * Math.PI * 8.85418782E-12);// 1/4\pi\epsilon_0
    public final static double Q = -1.161571547494087E-19; // the charge at the centre of the orbit

    /**
     * Main method to simulate the orbit of the proton
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        //Scanner to be used in the interface.
        Scanner scanner = new Scanner(System.in);

        // Simulation controls
        double timeStep = 0.00001; // time step in seconds
        long maxTime = 3000; // maximum simulation time in seconds
        int maxRev = 100; // maximum number of orbital revolutions for proton bunch
        int pStep = 1000; // print output on every pStep time steps
        double orbitRadius = 0.00987775892621835;
        double orbitalPeriod = 0.6559447185377681;
        double orbitalFrequency = 1/orbitalPeriod;
        double L = 0.05*orbitRadius;

        double pSpeed = 0.1;


        // E and B parameters
        double bMag = 1.0e-7; // magnetic flux density in Tesla
        double bErr = 0; // fractional difference in magnetic flux density between positive and negative x regions
        EMField theField = new EMField(); // creation of the EMField object
        double eMag = 1.0e-7;
        double eFieldValue;


        // Set the theoretical radius and frequency to compare at the end
        double frequency = (Proton.pCharge * bMag / (2 * Math.PI * Proton.pMass)); // expected frequency of bunch orbit
        double radius = Proton.pMass * pSpeed / (Proton.pCharge * bMag); // expected radius of bunch orbit


        // original vectors for postion and velocity started
        PhysicsVector pDirn = new PhysicsVector(0, 1, 0); // direction of velocity at start of simulation
        PhysicsVector pOrigin = new PhysicsVector(); // the centre of the simulation
        PhysicsVector pStart = new PhysicsVector(1, 0, 0); // the start position of the proton in a magnetic field

        //Make a proton
        Proton proton = new Proton();

        //set the initial values for the proton
        proton.setPosition(pOrigin); // initial position of the proton
        proton.setVelocity(PhysicsVector.scale(pSpeed, pDirn)); // the initial velocity of the proton

        // Set up the simulation
        double time = 0.0; // initial time
        int nRev = 0; // number of orbits the proton bunch completes
        double lastTime = 0.0; // The time at which the last 'turn' ended

        //set up a new tracker object to trak the proton radius
        ProtonOrbitTracker protonOrbitTracker = new ProtonOrbitTracker();

        /**
         * The bellow block of code i wanted to move into a separate class as an interface, however ran out of time.
         * The idea of the bellow block of code is that the user selects what type of field they want to investigate
         * then which method they would like to use. This allows all the code for all of these to be in this method and
         * applied without changing it.
         */
        //Set up the outfile name and the method to be used
        System.out.println("1= Euler, 2= Euler Cromer, 3= Quit");
        System.out.println("Enter method to use");
        int choice = scanner.nextInt();
        System.out.println("Enter file name");
        String fileName = scanner.next();

        //Switch statement to set the initial values of position for the different method so the electric field doesnt
        // start at the origin.
        while (nRev < maxRev && time < maxTime) {// Loop over time

            time += timeStep;
            eFieldValue = eMag*Math.sin((orbitalFrequency*time));

            theField.setElectric(new PhysicsVector(0,eFieldValue,0)); // Set up the cyclotron fields
            theField.setMagnetic(new PhysicsVector(0,0,bMag));
            proton.update(timeStep, theField.getAcceleration(proton));
            if (((int) (time / timeStep)) % pStep == 1) { // Write out the bunch position at intervals
                //outFile.println((proton.getPosition()).returnSimpleString());
                outFileWriter.writeFile(fileName, proton);
            }

            if (protonOrbitTracker.hasOrbited(proton)) {
                // bunch has completed an orbit
                nRev += 1;
                System.out.printf("Revolution number %3d at time %10.6f s\n", nRev, time);
                System.out.printf("Period of this revolution is  %10.6f s\n",(time-lastTime));
                System.out.println(proton);
                System.out.println();
                lastTime = time;
            }


            //outFile.close();
            //System.exit(0); // Exit (stopping the timer thread that would otherwise keep the process running)

        }
        System.exit(0); // Exit (stopping the timer thread that would otherwise keep the process running)
        scanner.close();


        /**
         * Method for calculating the electric field due to a point charge at some position that the proton is at.
         * @param position position of hte proton
         * @return the electric field as a vector
         */
/*
    public static PhysicsVector cyclotronEField(PhysicsVector position) {

        PhysicsVector eField = new PhysicsVector(position.getUnitVector());
        double distance = position.magnitude();
        double constants = (Q*k)/(1/(distance*distance));;
        eField.scale(constants);



        return eField;
    }*/
    }
}


