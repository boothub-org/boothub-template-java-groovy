package org.boothub.context

import org.boothub.context.StandardProjectContext

class JavaGroovyProjectContext extends StandardProjectContext.Generic
    implements LanguageContext, TestFrameworkContext, DocPublishing, AsciiDocSupport, LicenseCheckSupport, FindbugsSupport{
}
