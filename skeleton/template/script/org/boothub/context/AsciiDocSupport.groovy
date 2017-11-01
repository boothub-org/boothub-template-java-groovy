package org.boothub.context

import groovy.transform.SelfType
import org.boothub.context.ConfiguredBy
import org.boothub.context.ProjectContext
import org.boothub.context.TextIOConfigurator
import org.beryx.textio.TextIO

@SelfType(ProjectContext)
@ConfiguredBy(AsciiDocSupport.Configurator)
trait AsciiDocSupport {
    boolean useAsciiDoc = true

    static class Configurator extends TextIOConfigurator  {
        @Override
        void configureWithTextIO(ProjectContext context, TextIO textIO) {
            def ctx = context as AsciiDocSupport
            ctx.useAsciiDoc = textIO.newBooleanInputReader()
                    .withDefaultValue(ctx.useAsciiDoc)
                    .read("Use AsciiDoc?")
        }
    }
}
