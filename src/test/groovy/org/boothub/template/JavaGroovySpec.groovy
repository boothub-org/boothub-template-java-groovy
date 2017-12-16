/*
 * Copyright 2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.boothub.template

import org.boothub.context.ProjectContext
import org.boothub.gradle.GradleTemplateBuilder
import org.boothub.gradle.ProjectContextStreamBuilder
import org.boothub.Initializr
import org.boothub.gradle.BuildChecker
import org.boothub.gradle.OutputChecker
import spock.lang.Specification
import spock.lang.Unroll

import java.nio.file.Paths
import java.util.stream.Collectors

@Unroll
class JavaGroovySpec extends Specification {
    private static final String BASE_PATH = 'org/bizarre_soft'
    private static final String MODULE_STRANGE_LIB = 'strange_lib'
    private static final String MODULE_STRANGE_NAME = MODULE_WEIRD_APP.replaceAll('_', '-')
    private static final String MODULE_SPOOKY_UI = 'spooky_ui'
    private static final String MODULE_SPOOKY_NAME = MODULE_WEIRD_APP.replaceAll('_', '-')
    private static final String MODULE_WEIRD_APP = 'weird_app'
    private static final String MODULE_WEIRD_NAME = MODULE_WEIRD_APP.replaceAll('_', '-')

    private static final String APP_MAIN_CLASS = 'WeirdAppMain'

    private static final String TEMPLATE_DIR = getPath("/template")
    private static final String SAMPLE_CONTEXT = getPath("/sample-context.yml")

    private static boolean ignoreJavadoc = (System.getenv("TRAVIS") == "true")

    private static String getPath(String resourcePath) {
        def resource = JavaGroovySpec.class.getResource(resourcePath)
        assert resource : "Resource not available: $resourcePath"
        Paths.get(resource.toURI()).toAbsolutePath().toString()
    }

    private static boolean checkBuildArtifacts(GradleTemplateBuilder builder, String module,
                                               List<String> fileNames, List<String> forbiddenFileNames,
                                               List<String> testFileNames, List<String> forbiddenTestFileNames) {
        def checker = new BuildChecker(builder, module, "$BASE_PATH/$module")

        checker.checkClassesAndJars('sources', ['java', 'groovy'], fileNames, forbiddenFileNames, testFileNames, forbiddenTestFileNames)
        if(!ignoreJavadoc) {
            checker.checkClassesAndJars('javadoc', ['html'], fileNames, forbiddenFileNames, testFileNames, forbiddenTestFileNames)
        }
        checker.checkClassesAndJars('jar', ['class'], fileNames, forbiddenFileNames, testFileNames, forbiddenTestFileNames)
        true
    }

    def "should build valid artifacts for module #module using sample-context.yml"() {
        given:
        def builder = new GradleTemplateBuilder(TEMPLATE_DIR)
                .withContextFile(SAMPLE_CONTEXT)

        expect:
        checkBuildArtifacts(builder, module, fileNames, forbiddenFileNames, testFileNames, forbiddenTestFileNames)

        where:
        module             | fileNames                                      | forbiddenFileNames                             | testFileNames                        | forbiddenTestFileNames
        MODULE_SPOOKY_UI   | ["SpookyUiGroovyUtil", "SpookyUiJavaUtil"]     | ["SpookyUiGroovyMain", "SpookyUiJavaMain"]     | ["SpookyUiTest", "SpookyUiSpec"]     | []
        MODULE_STRANGE_LIB | ["StrangeLibGroovyUtil", "StrangeLibJavaUtil"] | ["StrangeLibGroovyMain", "StrangeLibJavaMain"] | ["StrangeLibTest", "StrangeLibSpec"] | []
        MODULE_WEIRD_APP   | ["WeirdAppMain", "WeirdAppJavaUtil"]           | ["WeirdAppGroovyUtil"]                         | ["WeirdAppTest", "WeirdAppSpec"]     | []
    }

    def "should create a valid application using sample-context.yml"() {
        when:
        def context = new Initializr(TEMPLATE_DIR).createContext(SAMPLE_CONTEXT)

        then:
        new OutputChecker(TEMPLATE_DIR, context, MODULE_WEIRD_NAME)
                .checkOutput("Hello from $APP_MAIN_CLASS!")
    }


    private static Collection<ProjectContext> getContexts(String contextFile) {
        def builder = new ProjectContextStreamBuilder({new Initializr(TEMPLATE_DIR).createContext(getPath("/$contextFile"))})
                .withFlagNames('language', 'testFramework')
        if(Boolean.getBoolean('test.verbose')) {
            builder.withFlagNames('checkLicenseHeader', 'useFindbugs', 'useClover', 'useAsciiDoc', 'publishDoc')
        }
        builder.stream().collect(Collectors.toList())
    }

    private static boolean checkBuildArtifactsSingle(ProjectContext context) {
        def builder = new GradleTemplateBuilder(TEMPLATE_DIR).withContext(context)
        def buildResult = builder.runGradleBuild()
        assert buildResult.artifacts['jar'].size() == 1
        assert buildResult.artifacts['sources'].size() == 1
        if(!ignoreJavadoc) {
            assert buildResult.artifacts['javadoc'].size() == 1
        }
        assert buildResult.artifacts['groovydoc'].size() == 0
        true
    }

    def "should build valid single-module artifacts with appMainClass using {#flags}"() {
        expect:
        checkBuildArtifactsSingle(context)

        where:
        context << getContexts('base-context-single.yml')
        flags = context.dumpFlags()
    }

    def "should build valid single-module artifacts without appMainClass using {#flags}"() {
        expect:
        checkBuildArtifactsSingle(context)

        where:
        context << getContexts('base-context-single.yml').each {ctx -> ctx.appMainClass = null}
        flags = context.dumpFlags()
    }

    private static boolean checkBuildArtifactsMulti(ProjectContext context) {
        def builder = new GradleTemplateBuilder(TEMPLATE_DIR).withContext(context)
        [MODULE_STRANGE_NAME, MODULE_SPOOKY_NAME, MODULE_WEIRD_NAME].each {moduleName ->
            def buildResult = builder.runGradleBuild(moduleName)
            assert buildResult.artifacts['jar'].size() == 1
            assert buildResult.artifacts['sources'].size() == 1
            if(!ignoreJavadoc) {
                assert buildResult.artifacts['javadoc'].size() == 1
            }
            assert buildResult.artifacts['groovydoc'].size() == 0
        }
        true
    }

    def "should build valid multi-module artifacts with appMainClass using {#flags}"() {
        expect:
        checkBuildArtifactsMulti(context)

        where:
        context << getContexts('base-context-multi.yml')
        flags = context.dumpFlags()
    }

    def "should build valid multi-module artifacts without appMainClass using {#flags}"() {
        expect:
        checkBuildArtifactsMulti(context)

        where:
        context << getContexts('base-context-multi.yml').each {ctx -> ctx.appMainClass = null}
        flags = context.dumpFlags()
    }

    def "should create valid single-module application using {#flags}"() {
        when:
        def checker = new OutputChecker(TEMPLATE_DIR, context)

        then:
        checker.checkOutput("Hello from $context.appMainClass!")

        where:
        context << getContexts('base-context-single.yml')
        flags = context.dumpFlags()
    }

    def "should create valid multi-module application using {#flags}"() {
        when:
        def checker = new OutputChecker(TEMPLATE_DIR, context, MODULE_WEIRD_NAME)

        then:
        checker.checkOutput("Hello from $context.appMainClass!")

        where:
        context << getContexts('base-context-multi.yml')
        flags = context.dumpFlags()
    }

    private static Collection<ProjectContext> getAsciiDocContexts(String contextFile) {
        def builder = new ProjectContextStreamBuilder({new Initializr(TEMPLATE_DIR).createContext(getPath("/$contextFile"))})
                .withFlagNames('language', 'testFramework', 'publishDoc')
        if(Boolean.getBoolean('test.verbose')) {
            builder.withFlagNames('checkLicenseHeader', 'useFindbugs', 'useClover')
        }
        builder.stream().map{ctx -> ctx.useAsciiDoc = true; ctx}.collect(Collectors.toList())
    }

    private static boolean checkAsciiDoc(ProjectContext context) {
        def builder = new GradleTemplateBuilder(TEMPLATE_DIR).withContext(context).withInProcessBuild(true)
        def taskName = builder.getQualifiedTaskName(null, 'asciidoctor')
        def gradleResult = builder.runGradle(taskName)
        assert builder.checkTask(taskName, gradleResult)
        def docDir = gradleResult.projectPath.resolve('build/asciidoc/html5').toFile()
        assert docDir.directory
        assert docDir.list() as Set == ['asciidoctor.css', 'coderay-asciidoctor.css', 'index.html'] as Set
        true
    }

    def "should create valid AsciiDoc for single-module project using {#flags}"() {
        expect:
        checkAsciiDoc(context)

        where:
        context << getAsciiDocContexts('base-context-single.yml')
        flags = context.dumpFlags()
    }

    def "should create valid AsciiDoc for multi-module project using {#flags}"() {
        expect:
        checkAsciiDoc(context)

        where:
        context << getAsciiDocContexts('base-context-multi.yml')
        flags = context.dumpFlags()
    }

}
