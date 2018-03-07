package org.boothub.context

import groovy.transform.SelfType
import org.beryx.textio.TextIO

@SelfType(ProjectContext)
@ConfiguredBy(TestFrameworkContext.Configurator)
trait TestFrameworkContext {
    static enum TestFramework {
        JUNIT4('JUnit 4'), JUNIT5('JUnit 5'), SPOCK('Spock')

        String name

        TestFramework(String name) { this.name = name }

        @Override
        String toString() { name }
    }

    TestFramework testFramework = TestFramework.SPOCK

    boolean isUseJUnit() { testFramework == TestFramework.JUNIT4 || testFramework == TestFramework.JUNIT5 }
    boolean isUseJUnit4() { testFramework == TestFramework.JUNIT4 }
    boolean isUseJUnit5() { testFramework == TestFramework.JUNIT5 }
    boolean isUseSpock() { testFramework == TestFramework.SPOCK }


    static class Configurator extends TextIOConfigurator  {
        @Override
        void configureWithTextIO(ProjectContext context, TextIO textIO) {
            def ctx = context as TestFrameworkContext
            ctx.testFramework = textIO.newEnumInputReader(TestFramework)
                    .withDefaultValue(ctx.testFramework)
                    .read("Test Framework")
        }
    }
}
