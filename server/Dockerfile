FROM amazoncorretto:22 AS runtime
COPY server-all.jar server-all.jar
EXPOSE 8080
CMD ["java", "-jar", "server-all.jar"]