import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class timeOutFileWriter {

    public static void fileWriter(ChargedParticle particle, String outFileName, double time){

        File log = new File(outFileName + ".data");

        try {
            if (!log.exists()) {
                System.out.println("We had to make a new file.");
                log.createNewFile();
            }
            FileWriter fileWriter = new FileWriter(log, true);

            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(time + " " + (particle.getPosition()).magnitude());
            bufferedWriter.write( "\n");
            bufferedWriter.close();

        } catch (IOException e) {
            System.out.println("COULD NOT LOG!!");
        }

    }
}
