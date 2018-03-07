package org.boothub.context

class JavaGroovyProjectContext extends StandardProjectContext.Generic
    implements LanguageContext,
            TestFrameworkContext,
            BintraySupport,
            DocPublishing,
            AsciiDocSupport,
            LicenseCheckSupport,
            FindbugsSupport,
            CloverSupport {
}
