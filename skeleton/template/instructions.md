### Getting started

{{~def 'prjId' (asJavaId ghProjectId)~}}

{{#if ghApiUsed}}
```
git clone https://github.com/{{ghProjectOwner}}/{{ghProjectId}}.git
cd {{ghProjectId}}
```
{{else}}
Download the generated zip file and unpack it. In the {{ghProjectId}} directory execute:
{{/if}}

{{#if appMainClass}}
&#8226; *On Linux or Mac OS:*
```
./gradlew installDist
{{#if multiModule}}
cd {{appModule.artifact}}/build/install/{{prjId}}/bin
{{else}}
cd build/install/{{prjId}}/bin
{{/if}}
./{{prjId}}
```

&#8226; *On Windows:*
```
gradlew installDist
{{#if multiModule}}
cd {{appModule.artifact}}\build\install\ {{~prjId}}/bin
{{else}}
cd build\install\ {{~prjId}}\bin
{{/if}}
{{prjId}}
```

The following text should appear on your screen:

```
Hello from {{appMainClass}}!
```
{{else}}
&#8226; *On Linux or Mac OS:*
```
./gradlew build
```

&#8226; *On Windows:*
```
gradlew build
```
{{/if}}

See the [template documentation](http://java-groovy.boothub.org) for more info.
