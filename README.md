# Topics-classifier

## The study project for creating programmed method of data classification

The app crawls website nytimes.com for headers of given topic and returns in console 
AND creates separate .csv documents with:
- Dictionary of found relevant words in topics and their usage
- Denormalized probability of each certificated word
- Normalized probability of each certificated word
- Verification data to check sample analysis

## Prerequisites
* **JDK >= 1.8.0_131**;
* **Apache Maven >= 3.3.9**

## Use:
- to create executable .jar file of app
```
mvn clean package
```
- to execute .jar file with the app
```
java -jar LR2_topic_classifier-0.0.1-SNAPSHOT.jar
```
- to execute .jar file for optional topics specify comma separated topics in double quotes
```
java -jar LR2_topic_classifier-0.0.1-SNAPSHOT.jar --topics="some,foo,bar"
```
