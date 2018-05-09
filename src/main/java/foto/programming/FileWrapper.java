package foto.programming;

import foto.utils.ChecksumAdler32;

import java.io.File;

public class FileWrapper {



    private Long checkSumFirst1000bytes;
    private Long checkSum;
    private File theFile;
    private static ChecksumAdler32 checksumAdler32 = new ChecksumAdler32();


    public FileWrapper(File theFile) {
        this.theFile = theFile;
    }

    public Long calculateCheckSumFirst1000bytes() {
        this.checkSumFirst1000bytes = checksumAdler32.calculate(theFile, true);
        return checkSumFirst1000bytes;
    }

    public Long calculateFullCheckSum() {
        this.checkSum = checksumAdler32.calculate(theFile, false);
        return checkSum;
    }


    public Long getCheckSum() {
        return checkSum;
    }

    public void setCheckSum(Long checkSum) {
        this.checkSum = checkSum;
    }

    public File getTheFile() {
        return theFile;
    }

    public void setTheFile(File theFile) {
        this.theFile = theFile;
    }

    public Long getCheckSumFirst1000bytes() {
        return checkSumFirst1000bytes;
    }


}
