/**
 * Class used to calculate the charge needed at the centre
 *
 * @author Adam Shelbourne
 * @version 1.0
 */
public class calculateNeededElectricCharge {

    public final static double k2= (4*Math.PI*8.85418782E-12);// 1/4\pi\epsilon_0
    public static final double pCharge=1.60217657e-19; //the electric charge of the proton in Coulombs
    public static final double pMass=1.67262178E-27; // the mass of the proton in kilograms

    /**
     * Method used to calculate the charge needed at the centre to produce and electric field that maintains the orbit at
     * the same distance as that of the magnetic field
     * @param args the command line arguments
     */
    public static void main(String[] args){
        double charge = k2*pMass*0.1/pCharge;
        System.out.println("The Charge you need is: " + charge);
    }
}
