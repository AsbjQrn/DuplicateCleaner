package foto.programming;

import foto.utils.ChecksumAdler32;

import java.io.File;

public class FileWrapper {



    private Long checkSumFirst1000bytes;
    private Long checkSum;
    private File theFile;
    boolean duplicate;


    public FileWrapper(File theFile) {
        this.theFile = theFile;
    }

    public Long calculateCheckSumFirst1000bytes(ChecksumAdler32 adler32) {
        this.checkSumFirst1000bytes = adler32.calculate(theFile, true);
        return checkSumFirst1000bytes;
    }

    public Long calculateFullCheckSum(ChecksumAdler32 adler32) {
        this.checkSum = adler32.calculate(theFile, false);
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

    public boolean isDuplicate() {
        return duplicate;
    }

    public void setDuplicate(boolean duplicate) {
        this.duplicate = duplicate;
    }
}
