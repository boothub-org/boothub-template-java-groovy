\{{javaComment 'license-header.txt'~}}
package {{targetPackage}};

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class {{targetFileClass}} {
    @Test
    public void onePlusOneShouldEqualTwo() {
        assertEquals(2, 1 + 1);
  }
}
