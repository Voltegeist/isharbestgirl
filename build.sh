#!/bin/bash

# Download dependencies with maven
mvn dependency:copy-dependencies

# Build with ant
ant exe