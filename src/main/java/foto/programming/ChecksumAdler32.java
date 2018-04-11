package foto.programming;


import java.io.*;
import java.util.zip.CheckedInputStream;
import java.util.zip.Adler32;

public class ChecksumAdler32 {

    public void calculate(File file) {
        try {
            CheckedInputStream cis = null;

            try {
                cis = new CheckedInputStream(new FileInputStream(file), new Adler32());

            } catch (Exception e) { //TODO indsaet logging her
                System.out.println("File Not found ");
            }
            byte[] buffer = new byte[1024];
            //can change the size according to needs
            while (cis.read(buffer) >= 0) {
                long checksum = cis.getChecksum().getValue();
                System.out.println(checksum + " " + file.length() + " " + file);
            }
            long checksum = cis.getChecksum().getValue();
            System.out.println(checksum);

        } catch (IOException e) {
            System.out.println("The exception has been thrown:" + e);
            System.exit(1);
        }
    }
}
