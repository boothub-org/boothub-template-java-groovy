{{#def 'docBaseUrl'}}{{#if ghPagesCname}}http://{{ghPagesCname}}{{else}}https://{{ghProjectOwner}}.github.io/{{ghProjectId}}{{/if}}{{/def}}
[![PRs Welcome](https://img.shields.io/badge/PRs-welcome-brightgreen.svg?style=flat-square)](http://makeapullrequest.com)
[![License](https://img.shields.io/badge/license-{{asUrlPath license}}-blue.svg)](https://github.com/{{ghProjectOwner}}/{{ghProjectId}}/blob/master/LICENSE)
[![Build Status](https://img.shields.io/travis/{{ghProjectOwner}}/{{ghProjectId}}/master.svg?label=Build)](https://travis-ci.org/{{ghProjectOwner}}/{{ghProjectId}})
## {{projectName}} ##

Welcome to *{{projectName}}*!

{{#if publishDoc~}}
**Useful links**
 - [Documentation]({{docBaseUrl}})
 - [javadoc]({{docBaseUrl}}/releases/latest/javadoc)
 - [List of all releases](https://{{ghProjectOwner}}/{{docBaseUrl}}/blob/gh-pages/releases.md)
{{~/if}}
