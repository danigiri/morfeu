# Morfeu

Morfeu is an application to visually manage APIs represented as [YAML](http://yaml.org), [JSON](https://www.json.org/json-en.html), [JSX](https://facebook.github.io/jsx/) or [XML](https://www.w3.org/XML/) documents. APIs are defined formally and enriched with metadata, which enables Morfeu to present a practical generic UI. Morfeu treats the original document as source of truth, is fully compatible with manual editing, git workflows and requires no dependencies in the API implementation.

YAML and JSON-based APIs are commonly used in many domains to represent complex API configurations, and Morfeu makes it
easier 
for users to engage and experiment with those APIs. Using a microservices approach, other services can be developed
and run a a sidecars to support different environments, systems and APIs. The sidecars in question are lightweight 
layers that are ran alongside existing complex systems.

- [snow-package](https://github.com/danigiri/snow-package) is a demo project that lets you manage pages written in JSX, 
  for instance your typical React web application. An example site can be found in the
  [snowpackage-site](https://github.com/danigiri/snow-package) repo, which implements a trivial test site.
- [eurinome](https://github.com/danigiri/eurinome) is another project that applies the Morfeu concept to the Kubernetes
  ecosystem using Helm

Morfeu is flexible and can work with any YAML, JSON or XML API once a schema is for that API is created and added to the 
system.It can even work with JSX markup code given it is an structured language that can be parsed.

It is licensed under the Apache 2 open-source license and is under heavy development.

## Getting Started

### Using Docker
```
git clone https://github.com/danigiri/morfeu.git
cd morfeu
docker build -t morfeu .
docker run --rm -p 8980:8980 morfeu
```

### Manually
```
# clone the project
git clone https://github.com/danigiri/morfeu.git
# install maven, npm and angular and then...
cd morfeu
# Start the backend (notice that we are setting an java env var to point to our location)
mvn clean compile jetty:run -D__RESOURCES_PREFIX=file://$(PWD)/ &
# launch the frontend
cd src/main/typescript && npm install && npm start
# This will open a browser with the application on http://localhost:3000
```

### Deploying on Kubernetes
Follow the same steps for Docker and use the generated image (example below is for a local microk8s cluster).

```
docker tag morfeu 127.0.0.1:32000/morfeu
docker push 127.0.0.1:32000/morfeu
# examine dashboard
http://<ip>:8080/api/v1/namespaces/kubernetes-dashboard/services/https:kubernetes-dashboard:/proxy/#/login
```


## Demo

[![Introduction to Morfeu fundamentals](http://img.youtube.com/vi/yjCPwHbjsVA/0.jpg)](https://youtu.be/jiqDp8Wzsjs "Morfeu introduction")

In this short video we showcase a Morfeu editing session with a simple YAML-based API.


## How it works

### Main idea

![Diagram of Morfeu basic architecture](./src/site/img/how-it-works.png)

As shown in the diagram, YAML, JSON, XML, or JSX files or  _documents_  are parsed into an [Abstract Syntax Tree](https://en.wikipedia.org/wiki/Abstract_syntax_tree) and presented to the user as a generic drag and drop UI, the UI can have a realtime interactive feedback loop when manipulating the different elements of the API. When the user is satisfied the document content is OK the AST is converted back to its original format.

### How it looks

![How the UI looks](./src/site/img/ui.png)


### Conceptual model

![How the different elements are defined](./src/site/img/concepts.png)


#### Catalogue

Convenience grouping of documents of a given API, it is expected all documents contained share the same API, model,
preview functionality, etc. It will also contain a list of snippets for convenience.

#### Document

Instances of an usage of an API, DSL or abstraction layer, it is composed of a number of cells

### Snippet

Convenience fragment of a document, with pre-filled values. Useful as a starting point for complex API usage and for begginner 
users. Necessarily tied to the model though it may not always validate (given it may be a fragment that needs to sit within a given
structure.

#### Model

Model represeting an API, a DSL or an abstraction layer

#### CellModel

Elements within the model
Can have cardinality, high level rules

#### Type

Low level types, basic rules

#### Metadata

Metadata related to the above items to increase usability, add some semantics and enrich models

#### Cell

Elements within a document, tied to cell models


## Rationale

APIs are complex to design, develop and use. Many organisations have an ever-growing list of APIs that gets bigger and bigger over time, having poorly-documented APIs, with contracts that are hard to use for novices and challenging for experts. This problem is exacerbated when APIs are acting in combination, or they are produced by different teams with different styles, granularity levels, etc.

Often, organisations that expose a rich API that is showing naturally end up developing a custom UI (often browser-based) that makes it easier to interact with that API. This is a valid strategy, but rich APIs are complex to develop and maintain, also requiring specific sets of skills that typical backend API developers may not have. In practice, building a complex UI for every single API is too resource and time-consuming, restricting the feasibility of custom UIs to the most popular APIs. 

This problem is compounded when the underlying systems interact between themselves. It is even more complex to create all-encompassing or modular UIs that handle APIs created by different teams, crossing domain and organisational boundaries.

Morfeu proposes a generic browser-based UI to handle multiple APIs at the same time, maintaining boundaries and making it easier for engineers to test and interact with YAML, JSON and XML-based APIs (other formats can be added as long as there is a way to transform to and from a DOM-like tree structure). JSX support was added with minimal effort.

## Development

### Dependencies

Morfeu requires [Java 11](https://java.com/en/download/), [Maven](http://maven.apache.org), [Angular 10](https://angular.io) and [npm](https://www.npmjs.com). [Selenium](https://www.seleniumhq.org) is used for browser integration tests. Docker images are
available.

### Tests

Morfeu includes a fair number of unit and integration tests. Launch the following command to run them:

    mvn test failsafe:integration-test -Dit.test='*IntTest' -Dapp-url='http://localhost:3000/'

This will run unit tests and general integration tests (see next command for UI ones). As there is a fairly large number of them this will take some time.

To launch UI Selenium integration tests you can do:

    mvn test failsafe:integration-test -Dit.test='*IntTest' -Dapp-url='http://localhost:3000/'

## Contributing

PRs or issues are welcome. Morfeu is under heavy development.

## Authors

Daniel Giribet - Twitter: [@danielgiri](https://twitter.com/danielgiri)

## License

> Copyright 2020 Daniel Giribet
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

