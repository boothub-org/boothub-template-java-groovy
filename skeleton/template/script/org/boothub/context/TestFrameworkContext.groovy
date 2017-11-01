package org.boothub.context

import groovy.transform.SelfType
import org.boothub.context.ConfiguredBy
import org.boothub.context.ProjectContext
import org.boothub.context.TextIOConfigurator
import org.beryx.textio.TextIO

@SelfType(ProjectContext)
@ConfiguredBy(TestFrameworkContext.Configurator)
trait TestFrameworkContext {
    static enum TestFramework {
        JUNIT('only JUnit'), SPOCK('only Spock'), JUNIT_SPOCK('JUnit and Spock')

        String name

        TestFramework(String name) { this.name = name }

        @Override
        String toString() { name }
    }

    TestFramework testFramework = TestFramework.SPOCK

    boolean isUseJUnit() { testFramework == TestFramework.JUNIT || testFramework == TestFramework.JUNIT_SPOCK }

    boolean isUseSpock() { testFramework == TestFramework.SPOCK || testFramework == TestFramework.JUNIT_SPOCK }


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
