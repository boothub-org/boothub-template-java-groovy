package org.boothub.context

import groovy.transform.SelfType
import org.boothub.context.ConfiguredBy
import org.boothub.context.ProjectContext
import org.boothub.context.TextIOConfigurator
import org.beryx.textio.TextIO

@SelfType(ProjectContext)
@ConfiguredBy(DocPublishing.Configurator)
trait DocPublishing {
    boolean publishDoc = true
    String ghPagesCname

    static class Configurator extends TextIOConfigurator {
        @Override
        void configureWithTextIO(ProjectContext context, TextIO textIO) {
            def ctx = context as DocPublishing
            ctx.publishDoc = textIO.newBooleanInputReader()
                    .withDefaultValue(ctx.publishDoc)
                    .read("Publish documentation to GitHub Pages?")

            if(ctx.publishDoc) {
                ctx.ghPagesCname = textIO.newStringInputReader()
                        .withMinLength(0)
                        .read("Custom domain name for GitHub Pages site (optional)", "\t\t\t(Example: ${ctx.ghProjectId}.mydomain.org)")
            }
        }
    }
}
