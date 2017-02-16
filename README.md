---
services: eventhub
platforms: java
author: msonecode
---

# How to write data to Event Hub via JMS and read via Storm on Azure

## Introduction

This example shows how to write data to Event Hub via JMS and read using Apache Storm topology (written in Java) on Azure.

## Prerequisites

### Azure EventHubs

Azure subscription with one event hub created under service bus namespace.

http://wacn-ppe.chinacloudsites.cn/documentation/articles/event-hubs-overview/  

### JDK 8

Java development environment, such as Eclipse and Maven, with JDK 8.

- JDK 8: http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html  

- Eclipse: https://www.eclipse.org/

- Maven: http://maven.apache.org/

## Required information

An Azure Event Hub with two shared access policies: one has listening permissions, the other has writing permissions. I would prefer these as "reader" and "writer", which are what I named as mine.

- The policy keys for the "reader" and "writer" policies

- The name of your Event Hub

- The Service Bus namespace that your Event Hub was created in

- The number of partitions available with your Event Hub configuration

For information about creating and using EventHubs, see the Create an Event Hub section of Get Started with EventHubs.

## Building the Sample

### In Eclipse, install Azure Toolkit for Eclipse plugin

http://wacn-ppe.chinacloudsites.cn/documentation/articles/azure-toolkit-for-eclipse-installation/

### Import the two projects into Eclipse

SenderViaJMS is a Java project. ReciverViaStorm is a Maven project.

### Configure SenderViaJMS

Add library “Apache Qpid Client Libraries for JMS” into this project’s Java Build Path. The library will be automatically installed when installing Azure Toolkit for Eclipse.

<img src="https://github.com/Azure-Samples/event-hubs-java-storm-sender-jms-receiver/blob/master/Images/1.png">

open servicebus.properties file and change below content with your actual value:

- SASPolicyName: previously created “writer” policy name

- SASPolicyKey: previously created “writer” policy key with URL encoded. URL encode tool: http://www.w3schools.com/tags/ref_urlencode.asp

- Eventhub name: previouly created event hub name

### Configure ReceiverViaStorm

Open Config.properties file and change below content with your actual value:

- eventhubspout.username={eventhub sas policy name}

- eventhubspout.password={eventhub sas policy key}

- eventhubspout.namespace={service bus namespace that eventhub was created in}

- eventhubspout.entitypath={eventhub name}

- eventhubspout.partitions.count={eventhub partitions count}

## Running the Sample

In Eclipse, run LogTopology class of ReceiverViaStorm to get it ready for all partitions.

Run SenderViaJMS to send messages, then you will see events appear on receiver window.

<img src="https://github.com/Azure-Samples/event-hubs-java-storm-sender-jms-receiver/blob/master/Images/2.png">
