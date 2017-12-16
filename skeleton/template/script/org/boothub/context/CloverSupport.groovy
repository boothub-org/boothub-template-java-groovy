package org.boothub.context

import groovy.transform.SelfType
import org.beryx.textio.TextIO

@SelfType(ProjectContext)
@ConfiguredBy(CloverSupport.Configurator)
trait CloverSupport {
    boolean useClover = true

    static class Configurator extends TextIOConfigurator  {
        @Override
        void configureWithTextIO(ProjectContext context, TextIO textIO) {
            def ctx = context as CloverSupport
            ctx.useClover = textIO.newBooleanInputReader()
                    .withDefaultValue(ctx.useClover)
                    .read("Use Clover?")
        }
    }
}
