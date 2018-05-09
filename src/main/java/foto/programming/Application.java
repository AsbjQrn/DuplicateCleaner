package foto.programming;


import foto.programming.enums.TypeOfCheckSumCalculation;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

import static foto.programming.functions.UtilityFunctions.*;

public class Application {

    private String rod = "/";
    private String sti1StorDisk = "/media/asbjorn/Mediedisc/Billeder eksport til linux/";
    private String sti2StorDisk = "/media/asbjorn/3TerraByte/Billeder eksport til linux/";
    private String stiLilletest = "/home/asbjorn/Downloads/";


    private void mapMediaBasedOnLength(HashMap<Long, List<File>> mediaMap) throws IOException {
        Path path = Paths.get(stiLilletest);
        Files.walk(path)
                .forEach(p -> {
                    if (isImage.test(p) || isVideo.test(p)) {
                        File file = p.toFile();
                        addToMap.accept(file.length(), file, mediaMap);
                    }
                });
    }


    private HashMap<Long, List<File>> mapDuplicatesBasedOnChecksum(Map<Long, List<File>> duplicatesMap, TypeOfCheckSumCalculation typeOfCheckSumCalculation) {
        HashMap<Long, List<File>> duplicatesMapBasedOnChecksum = new HashMap<>();

        duplicatesMap.entrySet().stream().filter(e -> sizeGreaterThanOne.test(e.getValue())).forEach(entry ->
                entry.getValue()
                        .forEach(listItem ->
                                addToMap.accept(
                                        calculateChecksum.apply(listItem, typeOfCheckSumCalculation), listItem, duplicatesMapBasedOnChecksum)));

//
//        Set<Map.Entry<Long, List<File>>> entryset = duplicatesMap.entrySet();
////        for (Map.Entry<Long, List<File>> entry : entryset) {
////            List<File> possibleDuplicates = entry.getValue();
////            if (possibleDuplicates.size() > 1) {
////                possibleDuplicates.forEach(file -> {
////                    addToMap.accept(calculateChecksum.apply(file, typeOfCheckSumCalculation), file, duplicatesMapBasedOnChecksum);
////                });
//            }
//        }
        return duplicatesMapBasedOnChecksum;
    }

    public static void main(String[] args) throws IOException {
        Application application = new Application();
        HashMap<Long, List<File>> mediaMap = new HashMap<>();

        application.mapMediaBasedOnLength(mediaMap);

        mediaMap = application.mapDuplicatesBasedOnChecksum(mediaMap, TypeOfCheckSumCalculation.FIRST_1000_BYTES_CHECKSUM);

        mediaMap = application.mapDuplicatesBasedOnChecksum(mediaMap, TypeOfCheckSumCalculation.FULL_CHECKSUM);

        mediaMap.entrySet().forEach(e -> {
            System.out.println("*********************");
            e.getValue().forEach(file -> System.out.println(file.getAbsolutePath()));
        });
    }
}
