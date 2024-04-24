# Introduction

This repository contains a demo code sample intended to use the following concepts and technologies:

* API-first Development Approach
* Open API / Swagger
* Hexagonal (Ports & Adapters) Architecture
* Infrastructure as code (IaC)
* Docker
* Google Cloud Platform (GCP)
* Google Kubernetes Engine (GKE)
* Cloud Build
* Cloud Deploy
* Terraform
* Helm
* Skaffold

# Modules structure

<img src="img/modules.svg"/>

# Hexagonal (Ports & Adapters) Architecture

Hexagonal architecture, also known as Ports and Adapters architecture, is a design pattern that emphasizes decoupling
the core business logic of an application from its external dependencies. In this architecture, the core logic is
surrounded by ports, that handle communication with external systems such as databases, user
interfaces, or third-party services. The purpose of Hexagonal Architecture is to improve maintainability and testability
by promoting separation of concerns and reducing the impact of changes in external dependencies on the core application
logic.

<img src="https://www.arhohuttunen.com/media/post/hexagonal-architecture/hexagonal-architecture-external-dependencies.svg" width=600/>
<sub>Image Source: Hexagonal Architecture Explained - https://www.arhohuttunen.com/hexagonal-architecture/</sub>
<p/>

By separating concerns and dependencies, Hexagonal Architecture enables meaningful business use case testing through the
implementation of "ports" or interfaces. The ports define the contract between the core application logic and its
external dependencies, allowing for easy substitution of implementations during testing. For example, in unit tests,
mock implementations of these ports can be used to simulate interactions with external systems, enabling meaningful
testing of business logic in isolation.

<img src="https://www.arhohuttunen.com/media/post/hexagonal-architecture/hexagonal-architecture-unit-test.svg" width="600"/>
<sub>Image Source: Hexagonal Architecture Explained - https://www.arhohuttunen.com/hexagonal-architecture/</sub>
<p/>

For more information on Hexagonal (Ports & Adapters) Architecture please see [Hexagonal Architecture Explained
](https://www.arhohuttunen.com/hexagonal-architecture/).

# API-first Development Approach

API-first development approach is a methodology that prioritizes designing and building the application programming
interface (API) before implementing other aspects of the software system. This strategy ensures that the API is
well-defined, robust, and supports intended use cases. Open API, formerly known as
Swagger, plays a crucial role in API-first development by providing a standardized format for documenting APIs. With
Open API, developers can describe the functionality, data models, and endpoints of their APIs in a standardized
format, allowing integration and collaboration across teams. API-first approach leverages tools like Open
API and Swagger.

# Build

TBD

# Run

## Using Java

TBD

## Using Docker

TBD

# Deployment

TBD

# References

Hexagonal Architecture Explained. Accessed April 24, 2024. https://www.arhohuttunen.com/hexagonal-architecture/

# Author

Dominik Cebula

* https://dominikcebula.com/
* https://blog.dominikcebula.com/
* https://www.udemy.com/user/dominik-cebula/
