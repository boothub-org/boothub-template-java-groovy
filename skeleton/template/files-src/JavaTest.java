\{{javaComment 'license-header.txt'~}}
package {{targetPackage}};

\{{~#if useJUnit4}}
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class {{targetFileClass}} {
    @Test
    public void onePlusOneShouldEqualTwo() {
        assertEquals(2, 1 + 1);
    }
}
\{{~/if}}
\{{~#if useJUnit5}}
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class {{targetFileClass}} {
    @Test
    public void one_plus_one_should_equal_two() {
        Assertions.assertEquals(2, 1 + 1);
    }
}
\{{~/if}}
