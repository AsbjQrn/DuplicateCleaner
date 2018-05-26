package foto.programming.functions;

import foto.programming.enums.TypeOfCheckSumCalculation;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.spi.FileTypeDetector;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.zip.Adler32;
import java.util.zip.CheckedInputStream;

import static foto.programming.constants.Types.imageTypes;
import static foto.programming.constants.Types.videoTypes;

public class UtilityFunctions {


    private final static FileTypeDetector fileTypeDetector = new FileTypeDetector() {
        @Override
        public String probeContentType(Path path) throws IOException {
            if (path.getFileName() != null) {
                String filnavn = path.getFileName().toString();
                return filnavn.substring(filnavn.lastIndexOf('.') + 1).toLowerCase();
            }
            return null;
        }
    };

    public static boolean isFile(Path path){
        return path.toFile().isFile();
    }

    public static String getFileExtensionInlowercase(Path p) {
        try {
            return fileTypeDetector.probeContentType(p);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static final TriConsumer<Long, File, HashMap<Long, List<File>>> addToMap = (key, file, map) -> {
        List filesOfSameSize = map.get(key);
        if (filesOfSameSize == null) {
            filesOfSameSize = new ArrayList<File>();
            filesOfSameSize.add(file);
            map.put(key, filesOfSameSize);
        } else
            filesOfSameSize.add(file);

    };
    public static final BiFunction<File, TypeOfCheckSumCalculation, Long> calculateChecksum = (f, t) -> calculate(f, t);
    public static final Predicate<String> isImage = (s) -> imageTypes.contains(s);
    public static final Predicate<String> isVideo = (s) -> videoTypes.contains(s);
    public static final Predicate<String> isMedia = (s) -> isImage.test(s) || isVideo.test(s) ;
    public static final Predicate<List> sizeGreaterThanOne = (list) -> list.size() > 1;

    private static long calculate(File file, TypeOfCheckSumCalculation typeOfCheckSumCalculation) {
        long checksum = 0;

        try {
            CheckedInputStream cis = null;

            try {
                cis = new CheckedInputStream(new FileInputStream(file), new Adler32());

            } catch (Exception e) { //TODO indsaet logging her
                System.out.println("File Not found ");
                throw e;
            }
            byte[] buffer = new byte[1024];
            //can change the size according to needs
            while (cis.read(buffer) >= 0) {
                checksum = cis.getChecksum().getValue();
                if (typeOfCheckSumCalculation == TypeOfCheckSumCalculation.FIRST_1000_BYTES_CHECKSUM) return checksum;
            }
            checksum = cis.getChecksum().getValue();


        } catch (IOException e) {
            System.out.println("The exception has been thrown:" + e);
            System.exit(1);
        }
        return checksum;

    }
}