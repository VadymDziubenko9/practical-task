# Practical UI and API tasks

### Links: [GSB](https://s.gsb.co.zm/)

___

### Software stack

- [Java 17 LTS](https://www.oracle.com/java/technologies/javase/17-0-5-relnotes.html)
- [Gradle](https://gradle.org/)
- [Rest Assured](https://rest-assured.io/)
- [TestNG](https://testng.org/doc/)
- AssertJ/ Soft Assertions
- [Allure Report](https://docs.qameta.io/allure/)
- [Selenide](https://ru.selenide.org/index.html)

___
Execution API/UI tests:

```shell
./gradlew clean apiTest -d
```

```shell
./gradlew clean uiTest -d
```

___

### Allure Report generation

UI autotests:

```shell
./gradlew allure serve ./build/allure-results
```

Using Gradle task:

```shell
./gradlew allureServe
```

___

