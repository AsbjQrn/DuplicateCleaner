package foto.programming;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class Application {


    Sizemap sizeMap = new Sizemap();
    ChecksumAdler32 checksumAdler32 = new ChecksumAdler32();



    private void findDuplicates(){

        

        File file = new File("/home/asbjorn/Downloads/himmel.jpg");

        checksumAdler32.calculate(file);


        sizeMap.put(file);


    }

    public static void main(String[] args) {

        Application application = new Application();
        application.findDuplicates();


    }

}
