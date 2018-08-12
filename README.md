# Morfeu

Morfeu is an application to manage multiple APIs represented as [YAML](http://yaml.org) or XML documents. It features a browser-based visual editor that can group heterogeneous APIs together. APIs are defined formally and enriched with metadata, which enables Morfeu to present a common UI.

YAML-based APIs are commonly used in many domains to represent complex API configurations, and Morfeu makes it easier for users to engage and experiment with those APIs. [Eurinome](https://github.com/danigiri/eurinome) is a sister project that applies the Morfeu concept to the Kubernetes ecosystem using Helm.

Morfeu is flexible and can work with any YAML or XML API once a schema is for that API is created and added to the system.

It is licensed under the Apache 2 open-source license and is under heavy development.

## Getting Started

```
# clone the project
git clone https://github.com/danigiri/morfeu.git
cd morfeu
# Start the backend (notice that we are setting an java env var to point to our location)
mvn clean compile jetty:run -D__RESOURCES_PREFIX=file://$(PWD)/ &
# launch the frontend
cd src/main/typescript && npm install && npm start
# This will open a browser with the application on http://localhost:3000
```


### Demo

[![Introduction to Morfeu fundamentals](http://img.youtube.com/vi/yjCPwHbjsVA/0.jpg)](http://www.youtube.com/watch?v=yjCPwHbjsVA "Morfeu introduction")

In this short video we showcase a Morfeu editing session with a simple YAML-based API.

### Installation

Morfeu requires [Java 8](https://java.com/en/download/), [Maven](http://maven.apache.org), [Angular 6](https://angular.io) and [npm](https://www.npmjs.com). [Selenium](https://www.seleniumhq.org) is used for browser integration tests

### Tests

Morfeu includes a fair number of unit and integration tests. Launch the following command to run them:

    mvn test failsafe:integration-test -Dit.test='*IntTest' -Dapp-url='http://localhost:3000/'

This will run unit tests, integration tests and also browser UI tests using Selenium. As there is a fairly large number of them this will take some time.


## Rationale

APIs are complex to design, develop and use. Many organisations have an ever-growing list of APIs that gets bigger and bigger over time, having poorly-documented APIs, with contracts that are hard to use for novices and challenging for experts. This problem is exacerbated when APIs are acting in combination, or they are produced by different teams with different styles, granularity levels, etc.

Often, organisations that expose a rich API that is showing naturally end up developing a custom UI (often browser-based) that makes it easier to interact with that API. This is a valid strategy, but rich APIs are complex to develop and maintain, also requiring specific sets of skills that typical backend API developers may not have. In practice, building a complex UI for every single API is too resource and time-consuming, restricting the feasibility of custom UIs to the most popular APIs. 

This problem is compounded when the underlying systems interact between themselves. It is even more complex to create all-encompassing or modular UIs that handle APIs created by different teams, crossing domain and organisational boundaries.

Morfeu proposes a generic browser-based UI to handle multiple APIs at the same time, maintaining boundaries and making it easier for engineers to test and interact with YAML and XML-based APIs (JSON or other formats can be added).


## Concepts


### Catalogue

Logical organisational unit, it is expected all documents contained share the same model
It also contains templates

## Templates

### Model

Model represeting an API, a DSL or an abstraction layer

## CellModel

Elements within the model
Can have cardinality, high level rules

## Type

Low level types, basic rules

## Metadata

Metadata related to the above items to increase usability, add some semantics and enrich models

### Document

Instances of an usage of an API, DSL or abstraction layer

### Cell

Elements within a document, tied to cell models

## Architecture

Architecture diagrams go here

## Deployment

## Contributing

PRs or issues are welcome. Morfeu is still under heavy development.

## Authors

Daniel Giribet - Twitter: [@danielgiri](https://twitter.com/danielgiri)

## License

> Copyright 2018 Daniel Giribet
>
> Licensed under the Apache License, Version 2.0 (the "License");
> you may not use this file except in compliance with the License.
> You may obtain a copy of the License at

>   http://www.apache.org/licenses/LICENSE-2.0

> Unless required by applicable law or agreed to in writing, software
> distributed under the License is distributed on an "AS IS" BASIS,
> WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
> See the License for the specific language governing permissions and
> limitations under the License.

## Acknowledgements

