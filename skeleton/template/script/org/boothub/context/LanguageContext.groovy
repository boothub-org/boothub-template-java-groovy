package org.boothub.context

import groovy.transform.SelfType
import org.boothub.context.ConfiguredBy
import org.boothub.context.ProjectContext
import org.boothub.context.TextIOConfigurator
import org.beryx.textio.TextIO

@SelfType(ProjectContext)
@ConfiguredBy(LanguageContext.Configurator)
trait LanguageContext {
    static enum Language {
        JAVA('only Java'), GROOVY('only Groovy'), JAVA_GROOVY('Java and Groovy')

        String name

        Language(String name) { this.name = name }

        @Override
        String toString() { name }
    }

    Language language = Language.JAVA

    boolean isUseJava() { language == Language.JAVA || language == Language.JAVA_GROOVY }

    boolean isUseGroovy() { language == Language.GROOVY || language == Language.JAVA_GROOVY }


    static class Configurator extends TextIOConfigurator  {
        @Override
        void configureWithTextIO(ProjectContext context, TextIO textIO) {
            def ctx = context as LanguageContext
            ctx.language = textIO.newEnumInputReader(Language)
                    .withDefaultValue(ctx.language)
                    .read("Language")
        }
    }
}
