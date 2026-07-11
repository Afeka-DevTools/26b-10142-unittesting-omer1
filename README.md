# Self learning Unit testing With LLM and Copilot - home assignment

**Omer Peled - 209110519**

This is a home assignment project for the Development tools course at Afeka college.

## Running tests

First, clone the repo and enter its root directory:

```sh
git clone https://github.com/Afeka-DevTools/26b-10142-unittesting-omer1
cd 26b-10142-unittesting-omer1
```

Make sure the `gradlew` script is executable:

```sh
chmod +x ./gradlew
```

Then you can run all tests with:

```sh
./gradlew test
```

### Running with a specific Java version

While working on this assignment, I have encountered an issue where the default Java version installed on my machine (25.0.3) was not compatible with the version of Gradle in this project (8.14).

If you encounter a similar issue, run the above command with a `JAVA_HOME` prefix, pointing to your other Java install. For example:

```sh
JAVA_HOME=/usr/lib/jvm/java-17-temurin-jdk ./gradlew test
```
