Summary
=======
This Integration Module (IM) provides a simple example of a using builder pattern in flow.

Build Requirements
==================
Java JDK 1.8.x
Maven 3.3.x

Runtime Requirements
====================

Archetype Creation
==================
This archetype can be used to create a builder-pattern IM via the following Maven archeytpe command.

mvn archetype:generate \
-DarchetypeGroupId=org.ikasan \
-DarchetypeArtifactId=ikasan-im-builder-pattern-maven-plugin \
-DarchetypeVersion=<Ikasan Version> \
-DgroupId=<Your project groupId> \
-DbuildParentGroupId=<Your project parent groupId> \
-Dversion=<your project version> \
-DartifactId=<IM Name> \
-DflowName=<IM Flow Name> \

For example,

mvn archetype:generate \
-DarchetypeGroupId=org.ikasan \
-DarchetypeArtifactId=ikasan-im-builder-pattern-maven-plugin \
-DarchetypeVersion=1.0.3-SNAPSHOT \
-DgroupId=com.company.esb.project \
-DbuildParentGroupId=com.company.esb.project \
-Dversion=1.0.0-SNAPSHOT \
-DartifactId=MyIntegrationModule \
-DflowName=jmsFlow \

Build
=====
Once the archetype is created,

cd MyIntegrationModule
mvn clean package

Start
======
java -jar jar/target/MyIntegrationModule-1.0.0-SNAPSHOT.jar


