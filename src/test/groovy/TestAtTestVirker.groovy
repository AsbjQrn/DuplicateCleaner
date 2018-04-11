import foto.programming.Application
import spock.lang.Specification

class TestAtTestVirker extends Specification {


    def "testattestvireksomdeskal"(){

        when:
        Application.main('Hejjegerentest')

        then:
        true




    }
}
