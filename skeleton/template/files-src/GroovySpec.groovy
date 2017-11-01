\{{javaComment 'license-header.txt'~}}
package {{targetPackage}}

import spock.lang.Specification

class {{targetFileClass}} extends Specification {
    def "one plus one should equal two"() {
        expect:
        1 + 1 == 2
    }
}
