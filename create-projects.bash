#! /bin/bash
mkdir microservices
cd microservices

 spring init --boot-version=3.2.4 --java-version=17 --packaging=jar --name=product-service --package-name=com.yubit.microservices.core.product --groupId=com.yubit.microservices.core.product --dependencies=actuator,webflux --version=1.0.0-SNAPSHOT product-service

 spring init --boot-version=3.2.4 --java-version=17 --packaging=jar --name=review-service --package-name=com.yubit.microservices.core.review --groupId=com.yubit.microservices.core.review --dependencies=actuator,webflux --version=1.0.0-SNAPSHOT review-service

 spring init --boot-version=3.2.4 --java-version=17 --packaging=jar --name=recommendation-service --package-name=com.yubit.microservices.core.recommendation --groupId=com.yubit.microservices.core.recommendation --dependencies=actuator,webflux --version=1.0.0-SNAPSHOT recommendation-service

 spring init --boot-version=3.2.4 --java-version=17 --packaging=jar --name=product-composite-service --package-name=com.yubit.microservices.composite.product --groupId=com.yubit.microservices.composite.product --dependencies=actuator,webflux --version=1.0.0-SNAPSHOT product-composite-service
 
 spring init --boot-version=3.2.4 --java-version=17 --packaging=jar --name=author-service --package-name=com.yubit.microservices.core.author --groupId=com.yubit.microservices.core.author --dependencies=actuator,webflux --version=1.0.0-SNAPSHOT author-service


cd ..