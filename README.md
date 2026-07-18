# Intuit Tax Refund AI Platform

## Version
v0.1.0 - Platform Foundation

## Overview
A Kubernetes-native platform for tracking tax refund status.

Components:
- React Customer UI
- Spring Boot Refund Status Service
- PostgreSQL
- Keycloak
- Kubernetes (Kind + Kustomize)

## Project Structure

applications/
services/
infrastructure/
docs/

## Prerequisites

- Java 21+
- Maven
- Docker Desktop
- kubectl
- Kind
- IntelliJ IDEA

## First Time Setup

1. Start Docker Desktop
2. Create Kind cluster
3. Build images
4. Load images
5. kubectl apply -k infrastructure/kubernetes/overlays/local

## Daily Development

Java:
- Debug locally using application-local.yml

React:
- Rebuild customer-ui image
- kind load docker-image
- rollout restart

Kubernetes:
- kubectl apply -k infrastructure/kubernetes/overlays/local

## Spring Profiles

Local:
-Dspring.profiles.active=local

Kubernetes:
SPRING_PROFILES_ACTIVE=kubernetes

## Current Features

- JWT Authentication
- Refund Status
- Refund Timeline
- PostgreSQL
- Database Bootstrap
- Health Checks

## Roadmap

v0.2
- AI Gateway
- MCP Servers
- Spring AI
- RAG
- Redis
- Kafka
