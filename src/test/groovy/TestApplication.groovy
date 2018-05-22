import foto.programming.Application
import spock.lang.Specification

import java.nio.file.Path
import java.nio.file.Paths

class TestApplication extends Specification {

    def "Test at mediafiler vælges og filer der ikke er mediafiler ikke vælges"(){

        when:
        Path path = Paths.get('/home/asbjorn/IntelliJ/DuplicateCleaner/src/test/resources/mediafiles')
        println(path)
        HashMap<Long, List<File>> mediaMap = new HashMap<Long, List<File>>();
        Application application = new Application();
        application.mapMediaBasedOnLength(mediaMap, path)

        then:

        Application.mediafilesCounter == 2
        Application.notMediafilesCounter == 1
        Application.mediaFileTypesFound.contains('avi')
        Application.mediaFileTypesFound.contains('jpg')
        !Application.mediaFileTypesFound.contains('jkj')
        Application.notMediaFileTypesFound.contains('jkj')
        !Application.notMediaFileTypesFound.contains('avi')
        !Application.notMediaFileTypesFound.contains('jpg')





    }
}
