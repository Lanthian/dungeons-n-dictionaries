################################################################################
# <p> Developed in https://github.com/SWEN90007-2025-sem2/High-Integrity in    #
#  * collaboration with:                                                       #
#  * <ul>                                                                      #
#  *   <li> Yuk Hang Cheng : {@link https://github.com/SKYYKS-9998}            #
#  *   <li> Adam Mantello : {@link https://github.com/adammantello-melbuni}    #
#  *   <li> Andrew Walton : {@link https://github.com/AndrewWalton002}         #
#  * </ul>                                                                     #
################################################################################

# 1st we build react files
FROM node:22 AS react-build

WORKDIR /frontend

COPY ./src/frontend .
# (env file baked into frontend - must exist pre-build)
COPY .env .

RUN npm install
RUN npm run build

# 2nd we build the backend and the build files with tomcat
FROM maven:3.9-amazoncorretto-17 AS build

WORKDIR /backend

COPY ./src/backend .

COPY --from=react-build /frontend/dist ./src/main/webapp

RUN mvn clean install

FROM tomcat:10.0.27-jre17

COPY --from=build /backend/target/backend-1.0-SNAPSHOT.war $CATALINA_HOME/webapps/ROOT.war
