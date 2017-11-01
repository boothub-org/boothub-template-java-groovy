package org.boothub.context

import groovy.transform.SelfType
import org.boothub.context.ConfiguredBy
import org.boothub.context.ProjectContext
import org.boothub.context.TextIOConfigurator
import org.beryx.textio.TextIO

@SelfType(ProjectContext)
@ConfiguredBy(FindbugsSupport.Configurator)
trait FindbugsSupport {
    boolean useFindbugs = true

    static class Configurator extends TextIOConfigurator  {
        @Override
        void configureWithTextIO(ProjectContext context, TextIO textIO) {
            def ctx = context as FindbugsSupport
            ctx.useFindbugs = textIO.newBooleanInputReader()
                    .withDefaultValue(ctx.useFindbugs)
                    .read("Use Findbugs?")
        }
    }
}
