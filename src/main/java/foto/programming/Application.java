package foto.programming;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Predicate;

public class Application {
    enum ChecksumCalculation {FIRST_1000_BYTES_CHECKSUM, FULL_CHECKSUM}

    private String rod = "/";
    private String sti1StorDisk = "/media/asbjorn/Mediedisc/Billeder eksport til linux/";
    private String sti2StorDisk = "/media/asbjorn/3TerraByte/Billeder eksport til linux/";
    private String stiLilletest = "/home/asbjorn/Downloads/";

    List imagetypes = Arrays.asList("jpg", "raw");


    private String getFileTypeinLowercase(Path path) {

        if (path.getFileName() != null) {
            String filnavn = path.getFileName().toString();
            return filnavn.substring(filnavn.lastIndexOf('.') + 1).toLowerCase();
        }
        return ".not_A_file";

    }


    private void addToDuplicateMap(long key, FileWrapper fileWrapper, HashMap<Long, List<FileWrapper>> duplicatesMap) {
        List filesOfSameSize = duplicatesMap.get(key);
        if (filesOfSameSize == null) {
            filesOfSameSize = new ArrayList<FileWrapper>();
            filesOfSameSize.add(fileWrapper);
            duplicatesMap.put(key, filesOfSameSize);
        } else
            filesOfSameSize.add(fileWrapper);
    }


    private void mapDuplicatesBasedOnLength(HashMap<Long, List<FileWrapper>> duplicatesMapBasedonLength) throws IOException {
        Predicate<Path> isImage = (p) -> imagetypes.contains(getFileTypeinLowercase(p));
        Path path = Paths.get(stiLilletest);
        System.out.println(path.toString());
        Files.walk(path)
                .forEach(p -> {
                    if (isImage.test(p)) {
                        File file = p.toFile();
                        addToDuplicateMap(file.length(), new FileWrapper(file), duplicatesMapBasedonLength);
                    }
                });
    }


    private HashMap<Long, List<FileWrapper>> mapDuplicatesBasedOnChecksum(Map<Long, List<FileWrapper>> duplicatesMap, ChecksumCalculation checksumCalculation) {
        HashMap<Long, List<FileWrapper>> duplicatesMapBasedOnChecksum = new HashMap<>();
        Set<Map.Entry<Long, List<FileWrapper>>> entryset = duplicatesMap.entrySet();
        for (Map.Entry<Long, List<FileWrapper>> entry : entryset) {
            List<FileWrapper> possibleDuplicates = entry.getValue();
            if (possibleDuplicates.size() > 1) {
                possibleDuplicates.forEach(fileWrapper -> {
                    long checksum = calculateChecksum(checksumCalculation, fileWrapper);
                    addToDuplicateMap(checksum, fileWrapper, duplicatesMapBasedOnChecksum);
                });
            }
        }
        return duplicatesMapBasedOnChecksum;
    }

    private long calculateChecksum(ChecksumCalculation checksumCalculation, FileWrapper fileWrapper) {
        switch (checksumCalculation) {
            case FIRST_1000_BYTES_CHECKSUM:
                return fileWrapper.calculateCheckSumFirst1000bytes();
            case FULL_CHECKSUM:
                return fileWrapper.calculateFullCheckSum();
            default:
                return 0L;
        }
    }


    public static void main(String[] args) throws IOException {
        Application application = new Application();
        HashMap<Long, List<FileWrapper>> duplicatesMap = new HashMap<>();
        application.mapDuplicatesBasedOnLength(duplicatesMap);
        System.out.println("Duplikater baseret på fil-længde map-størrelse: " + duplicatesMap.size());
        duplicatesMap = application.mapDuplicatesBasedOnChecksum(duplicatesMap, ChecksumCalculation.FIRST_1000_BYTES_CHECKSUM);
        System.out.println("Duplikater baseret på FIRST_THOUSAND_BYTES_CHECKSUM map-størrelse: " + duplicatesMap.size());
        duplicatesMap = application.mapDuplicatesBasedOnChecksum(duplicatesMap, ChecksumCalculation.FULL_CHECKSUM);
        System.out.println("Duplikater baseret på FULL_CHECKSUM map-størrelse: " + duplicatesMap.size());
        duplicatesMap.entrySet().forEach(e -> {
            System.out.println("*********************");
            e.getValue().forEach(fileWrapper -> System.out.println(fileWrapper.getTheFile().getAbsolutePath()));
        });
    }
}
