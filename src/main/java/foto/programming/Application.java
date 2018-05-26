package foto.programming;


import foto.programming.enums.TypeOfCheckSumCalculation;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static foto.programming.functions.UtilityFunctions.*;

public class Application {

    private static long directoryCounter = 0L;
    private static long fileCounter = 0L;
    private static long notFileOrDirectoryCounter = 0L;
    private static long mediafilesCounter = 0L;
    private static Set<String> mediaFileTypesFound = new HashSet<>();
    private static long notMediafilesCounter = 0L;
    private static Set<String> notMediaFileTypesFound = new HashSet<>();
    private static String rod = "/";
    private static String sti1StorDisk = "/media/asbjorn/Mediedisc/Billeder eksport til linux/";
    private static String sti2StorDisk = "/media/asbjorn/3TerraByte/Billeder eksport til linux/";
    private static String stiLilletest = "/home/asbjorn/Downloads/";


    private void countAllFiles() throws IOException {
        Path path = Paths.get(sti1StorDisk);
        Files.walk(path).forEach(p -> {
            File file = p.toFile();
            if (file.isDirectory()) {
                directoryCounter++;
            } else if (file.isFile()) {
                fileCounter++;
            } else {
                notFileOrDirectoryCounter++;
            }

        });
    }

    public void mapMediaBasedOnLength(HashMap<Long, List<File>> mediaMap, Path path) throws IOException {
        Files.walk(path)
                .forEach(p -> {
                    if (isFile(p)) {
                        String fileExtension = getFileExtensionInlowercase(p);
                        if (isMedia.test(fileExtension)) {
                            File file = p.toFile();
                            addToMap.accept(file.length(), file, mediaMap);
                            mediafilesCounter++;
                            mediaFileTypesFound.add(fileExtension);
                        } else {
                            notMediafilesCounter++;
                            notMediaFileTypesFound.add(fileExtension);
                        }
                    }
                });
    }


    private HashMap<Long, List<File>> mapDuplicatesBasedOnChecksum(Map<Long, List<File>> duplicatesMap,
                                                                   TypeOfCheckSumCalculation typeOfCheckSumCalculation) {
        HashMap<Long, List<File>> duplicatesMapBasedOnChecksum = new HashMap<>();

        duplicatesMap.entrySet().stream().filter(e -> sizeGreaterThanOne.test(e.getValue())).forEach(listEntry ->
                listEntry.getValue().forEach(
                        listItem -> addToMap.accept(
                                calculateChecksum.apply(listItem, typeOfCheckSumCalculation),
                                listItem, duplicatesMapBasedOnChecksum)));

        return duplicatesMapBasedOnChecksum;
    }

    public static void main(String[] args) throws IOException {
        Application application = new Application();
        Path path = Paths.get(sti1StorDisk);
        HashMap<Long, List<File>> mediaMap = new HashMap<>();

        application.mapMediaBasedOnLength(mediaMap, path);

        mediaMap = application.mapDuplicatesBasedOnChecksum(mediaMap, TypeOfCheckSumCalculation.FIRST_1000_BYTES_CHECKSUM);

        mediaMap = application.mapDuplicatesBasedOnChecksum(mediaMap, TypeOfCheckSumCalculation.FULL_CHECKSUM);

        mediaMap.entrySet().forEach(e -> {
            System.out.println("*********************");
            e.getValue().forEach(file -> System.out.println(file.getAbsolutePath()));
        });

        application.countAllFiles();
        System.out.println("Der er " + fileCounter + " filer under: " + sti1StorDisk);
        System.out.println("Der er " + directoryCounter + " biblioteker under: " + sti1StorDisk);
        System.out.println("Der er " + notFileOrDirectoryCounter + " ikke file/bibliotker under: " + sti1StorDisk);
        System.out.println("Der er registreret " + mediafilesCounter + " mediefiler");
        System.out.println("Disse filer regnes for at være mediefiler: " + mediaFileTypesFound.toString());
        System.out.println("Der er registreret " + notMediafilesCounter + " filer som ikke er mediefiler");
        System.out.println("Disse filer regnes ikke for mediefiler: " + notMediaFileTypesFound.toString());
        System.out.println("Forskellen er " + (fileCounter + directoryCounter + notFileOrDirectoryCounter - mediafilesCounter - notMediafilesCounter));

    }
}
