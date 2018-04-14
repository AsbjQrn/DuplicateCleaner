package foto.programming;

import foto.utils.ChecksumAdler32;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Predicate;

public class Application {

    List imagetypes = Arrays.asList("jpg", "raw");
    SortedMap<Long, List<FileWrapper>> sizeMap = new TreeMap<>();
    ChecksumAdler32 checksumAdler32 = new ChecksumAdler32();

    private String getFileTypeinLowercase(Path path) {
        String filnavn = path.getFileName().toString();
        return filnavn.substring(filnavn.lastIndexOf('.') + 1).toLowerCase();
    }


    public void addToSizeMap(Path path) {

        FileWrapper fileWrapper = new FileWrapper(path.toFile());

        Long size = fileWrapper.getTheFile().length();

        List filesOfSameSize = sizeMap.get(size);

        if (filesOfSameSize == null) {
            filesOfSameSize = new ArrayList<FileWrapper>();
            filesOfSameSize.add(fileWrapper);
            sizeMap.put(size, filesOfSameSize);
        } else
            filesOfSameSize.add(fileWrapper);
    }


    private void registerImagefilesToSizeMap() throws IOException {

        Predicate<Path> isImage = (p) -> imagetypes.contains(getFileTypeinLowercase(p));

        Files.walk(Paths.get("/home/asbjorn/Downloads/"))
                .forEach(p -> {
                    if (isImage.test(p)) {
                        addToSizeMap(p);
                    }
                });
    }

    private void calculateFirst1000Bytes(List<FileWrapper> files) {
        files.forEach(f -> f.calculateCheckSumFirst1000bytes(checksumAdler32));
    }

    private void calculateFullCheckSum(List<FileWrapper> files) {
        files.forEach(f -> f.calculateFullCheckSum(checksumAdler32));
    }

    private void sortByFirst1000Bytes(List<FileWrapper> files) {
        Collections.sort(files, Comparator.comparing(FileWrapper::getCheckSumFirst1000bytes));
//        (fileWrapper1, fileWrapper2) -> fileWrapper1.getCheckSumFirst1000bytes().compareTo(fileWrapper2.getCheckSumFirst1000bytes()) );
    }

    private void markAndCountDuplicates(List<FileWrapper> files) {
        FileWrapper fileWrapperOld = null;
        FileWrapper fileWrapper;
        for (int i = 0; i < files.size(); i++) {
            fileWrapper = files.get(i);
            if (fileWrapperOld != null) {
                if (fileWrapper.getCheckSumFirst1000bytes() == fileWrapperOld.getCheckSumFirst1000bytes()) {
                    fileWrapper.setDuplicate(true);
                    fileWrapperOld.setDuplicate(true);
                }
            }
            fileWrapperOld = fileWrapper;
        }
    }

    private void checkForDuplicates(List<FileWrapper> files) {

        calculateFirst1000Bytes(files);
        sortByFirst1000Bytes(files);
        markAndCountDuplicates(files);

    }


    public static void main(String[] args) throws IOException {

        Application application = new Application();
        application.registerImagefilesToSizeMap();
        application.filterNonDuplicatesFromSizemap();

    }

    private void filterNonDuplicatesFromSizemap() {

        sizeMap.entrySet().forEach(e -> {
                    if (e.getValue().size() > 1)
                        checkForDuplicates(e.getValue());
                    else
                        sizeMap.remove(e.getKey());
                }
        );
    }
}
