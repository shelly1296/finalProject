/**
 * Class to write the output values of the position
 *
 * @author - Adam Shelbourne
 * @version - 1.0
 */

import java.io.*;

public class outFileWriter {

    public static void writeFile(String outFileName, Proton proton){

        File log = new File(outFileName + ".data");

        try {
            if (!log.exists()) {
                System.out.println("We had to make a new file.");
                log.createNewFile();
            }

            FileWriter fileWriter = new FileWriter(log, true);

            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write((proton.getPosition()).returnSimpleString());
            bufferedWriter.write( "\n");
            bufferedWriter.close();

        } catch (IOException e) {
            System.out.println("COULD NOT LOG!!");
        }


    }
}
