import foto.programming.functions.UtilityFunctions
import spock.lang.Shared
import spock.lang.Specification

import java.nio.file.Path
import java.nio.file.Paths

class TestUtilityFunctions extends Specification {


    @Shared String resourcesMedia

    def setup() {
        resourcesMedia = '/home/asbjorn/IntelliJ/DuplicateCleaner/src/test/resources/mediafiles'
    }

    def "Test getfileExtention in lowercase"() {
        when:
        Path path = Paths.get(resourcesMedia + '/hjejejlkj.adf.ft.ert.7.JPEGJPEG')

        then:
        UtilityFunctions.getFileExtensionInlowercase(path) == 'jpegjpeg'

    }

    def "Test mapping of duplicates based on a key"(){
        when:
        HashMap<Long, List<File>> mapOfFiles = new HashMap<>();
        UtilityFunctions.addToMap.accept(1L, new File(resourcesMedia + '/test1.avi'), mapOfFiles)
        UtilityFunctions.addToMap.accept(1L, new File(resourcesMedia + '/test1.avi'), mapOfFiles)
        UtilityFunctions.addToMap.accept(2L, new File(resourcesMedia + '/test2.avi'), mapOfFiles)

        then:
        mapOfFiles.size() == 2
        mapOfFiles.get(1L).size() == 2
        mapOfFiles.get(2L).size() == 1

    }
}
