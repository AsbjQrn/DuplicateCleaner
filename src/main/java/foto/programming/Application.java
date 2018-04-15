package foto.programming;

import foto.utils.ChecksumAdler32;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Predicate;

public class Application {

    enum ChecksumCalculation {FIRST_THOUSAND_BYTES, FULL}

    List imagetypes = Arrays.asList("jpg", "raw");

    private String getFileTypeinLowercase(Path path) {
        String filnavn = path.getFileName().toString();
        return filnavn.substring(filnavn.lastIndexOf('.') + 1).toLowerCase();
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


    private void findDuplicatesBasedOnLength(HashMap<Long, List<FileWrapper>> duplicatesMapBasedonLength) throws IOException {

        Predicate<Path> isImage = (p) -> imagetypes.contains(getFileTypeinLowercase(p));

        Files.walk(Paths.get("/home/asbjorn/Downloads/"))
                .forEach(p -> {
                    if (isImage.test(p)) {
                        File file = p.toFile();
                        addToDuplicateMap(file.length(), new FileWrapper(file), duplicatesMapBasedonLength);
                    }
                });
    }


    private HashMap<Long, List<FileWrapper>> findDuplicatesBasedOnChecksum(Map<Long, List<FileWrapper>> duplicatesMap, ChecksumCalculation checksumCalculation) {

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
            case FIRST_THOUSAND_BYTES:
                return fileWrapper.calculateCheckSumFirst1000bytes();
            case FULL:
                return fileWrapper.calculateFullCheckSum();
            default:
                return 0L;
        }
    }


    public static void main(String[] args) throws IOException {

        Application application = new Application();
        HashMap<Long, List<FileWrapper>> duplicatesMap = new HashMap<>();
        application.findDuplicatesBasedOnLength(duplicatesMap);
        duplicatesMap = application.findDuplicatesBasedOnChecksum(duplicatesMap, ChecksumCalculation.FIRST_THOUSAND_BYTES);
        duplicatesMap = application.findDuplicatesBasedOnChecksum(duplicatesMap, ChecksumCalculation.FULL);


    }


}
