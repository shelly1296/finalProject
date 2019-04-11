public class EMField{
/**
 * Represents an arbitrary electromagnetic field.  SI units are used throughout.
 *
 * @author Ian Bailey
 * @version 1.0
 */
 
 	protected PhysicsVector electric; // electric field strength
 	protected PhysicsVector magnetic; // magnetic flux density
 	
 	/**
    	*  Default constructor. Set data members to zero.
    	*
    	*/
    	public EMField(){
    		electric = new PhysicsVector();
    		magnetic = new PhysicsVector();
    	}
    	
 	/**
    	*  Constructor with two inputs - the electric field strength and magnetic flux density
    	*
    	* @param electricIn The electric field strength
    	* @param magneticIn The magnetic flux density
    	*/
    	public EMField(PhysicsVector electricIn, PhysicsVector magneticIn){
    		electric = new PhysicsVector(electricIn);
    		magnetic = new PhysicsVector(magneticIn);
    	}
 	
    	
    	/**
    	*  Set the electric field strength
    	*
    	* @param electricIn The electric field strength
    	*/
    	public void setElectric(PhysicsVector electricIn){
    		electric = new PhysicsVector(electricIn);
    	}
 	
    	/**
    	*  Set the magnetic flux density
    	*
    	* @param magneticIn The magnetic flux density
    	*/
    	public void setMagnetic(PhysicsVector magneticIn){
    		magnetic = new PhysicsVector(magneticIn);
    	}
    	
    	/**
    	*  Return the electric field strength
    	*
    	* @return The current value of the electric field strength
    	*/
    	public PhysicsVector getElectric(){
    		return new PhysicsVector(electric);
    	}
 	
    	/**
    	*  Get the magnetic flux density
    	*
    	* @return The current value of the magnetic flux density
    	*/
    	public PhysicsVector getMagnetic(){
    		return new PhysicsVector(magnetic);
    	}
    	
    	
    	/**
    	* Returns the acceleration experienced by a charged particle according to the Lorentz force law (non-relativistic).
    	* @param theParticle - the charged particle moving in the field
    	* @return the acceleration calculated from (qE + vXB / m)
    	*/
    	public PhysicsVector getAcceleration(ChargedParticle theParticle)
    	{
    		PhysicsVector lorentz=new PhysicsVector(electric); // E
    		lorentz.increaseBy(PhysicsVector.cross(theParticle.getVelocity(),magnetic)); //+ v cross B
    		lorentz.scale(theParticle.getCharge()/theParticle.getMass()); // multiply by charge divided by mass
    		return lorentz;

    		
    	}


    	
 
}
