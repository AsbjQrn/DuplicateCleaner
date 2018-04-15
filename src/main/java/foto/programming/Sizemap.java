package foto.programming;

import java.io.File;
import java.nio.file.Path;
import java.util.*;

public class Sizemap extends HashMap {


    HashMap<Long, List<File>> sizes = new HashMap<>();


    public void put(Path path) {
        File file = path.toFile();
        Long size = file.length();
        List filesOfSameSize = sizes.get(size);
        if (filesOfSameSize == null) {
            filesOfSameSize = new ArrayList<File>();
            filesOfSameSize.add(file);
            sizes.put(size, filesOfSameSize);
        } else
            filesOfSameSize.add(file);
    }
}
