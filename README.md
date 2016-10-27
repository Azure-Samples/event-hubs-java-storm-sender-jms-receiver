---
services: eventhub
platforms: java
author: msonecode
---

# How to write data to Event Hub via JMS and read via Storm on Azure

## Introduction

An example of how to write data to Event Hub via JMS and read using Apache Storm topology (written in Java) on Azure.
<br/>
<br/>
<br/>

## Prerequisites

*__Azure EventHubs__*

Azure subscription with one event hub created under service bus namespace.

http://wacn-ppe.chinacloudsites.cn/documentation/articles/event-hubs-overview/  
<br/>
<br/>

*__JDK 8__*

Java development environment, such as Eclipse and Maven, with JDK 8.

•	JDK 8: http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html  

•	Eclipse: https://www.eclipse.org/

•	Maven: http://maven.apache.org/
<br/>
<br/>
<br/>

## Required information

An Azure Event Hub with two shared access policies; one that has listen permissions, and one that has write permissions. I will refer to these as "reader" and "writer", which is what I named mine

•	The policy keys for the "reader" and "writer" policies

•	The name of your Event Hub

•	The Service Bus namespace that your Event Hub was created in

•	The number of partitions available with your Event Hub configuration

For information on creating and using EventHubs, see the Create an Event Hub section of Get Started with EventHubs.
<br/>
<br/>
<br/>


## Building the Sample

*__In Eclipse, install Azure Toolkit for Eclipse plugin__*

http://wacn-ppe.chinacloudsites.cn/documentation/articles/azure-toolkit-for-eclipse-installation/
<br/>
<br/>

*__Import the two projects into Eclipse__*

SenderViaJMS is a Java project. ReciverViaStorm is a Maven project.
<br/>
<br/>

*__Configure SenderViaJMS__*

Add library “Apache Qpid Client Libraries for JMS” into this project’s Java Build Path. The library will be automatically installed when installing Azure Toolkit for Eclipse.

<img src="https://github.com/Azure-Samples/event-hubs-java-storm-sender-jms-receiver/blob/master/Images/1.png">

open servicebus.properties file and change below content to your actual value.

•	SASPolicyName: previous created “writer” policy name

•	SASPolicyKey: previous created “writer” policy key with URL encoded. URL encode tool: http://www.w3schools.com/tags/ref_urlencode.asp

•	Eventhub name: previous created event hub name
<br/>
<br/>

*__Configure ReceiverViaStorm__*

Open Config.properties file and change below content to your actual value:

•	eventhubspout.username={eventhub sas policy name}

•	eventhubspout.password={eventhub sas policy key}

•	eventhubspout.namespace={service bus namespace that eventhub was created in}

•	eventhubspout.entitypath={eventhub name}

•	eventhubspout.partitions.count={eventhub partitions count}
<br/>
<br/>
<br/>


## Running the Sample

In Eclipse, run LogTopology class of ReceiverViaStorm to get it start for all partitions.

Run SenderViaJMS to send messages, then you will see events appear on receiver window.

<img src="https://github.com/Azure-Samples/event-hubs-java-storm-sender-jms-receiver/blob/master/Images/2.png">
