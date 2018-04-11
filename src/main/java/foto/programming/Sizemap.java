package foto.programming;

import java.io.File;
import java.util.*;

public class Sizemap extends HashMap {


    HashMap<Long, List<File>> sizes = new HashMap<>();


    public void put(File file) {

        Long size = file.length();

        List filesOfSameSize = sizes.get(size);

        if (filesOfSameSize == null)
            sizes.put(size, Arrays.asList(file));
        else
            filesOfSameSize.add(file);

    }


}
