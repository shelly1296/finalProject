/**
 * Class used to track the orbit of a proton extending the class particle
 *
 * @author Adam Shelbourne
 * @version 1.0
 */
public class ProtonOrbitTracker extends Particle {

    public boolean converge=false; // flag to indicate whether the bunch is approaching or diverging from the origin
    double displacement=0.0; // distance between bunch origin and current position
    PhysicsVector origin;

    /**
     * Default constructor of the class
     */
    public ProtonOrbitTracker(){
        converge=false;
        displacement=0.0;
        origin = new PhysicsVector();
    }


    /**
     * Class used to check if the proton has completed an orbit
     * @param proton a proton object
     * @return anOrbit - boolean if the proton has completed an orbit or not
     */
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
