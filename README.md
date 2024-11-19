# Practical UI and API Tasks

### Links

- [GSB](https://s.gsb.co.zm/)

---

### Software Stack

- **[Java 17 LTS](https://www.oracle.com/java/technologies/javase/17-0-5-relnotes.html)**: Reliable and modern version
  of Java.
- **[Gradle](https://gradle.org/)**: Build automation tool.
- **[Rest Assured](https://rest-assured.io/)**: API testing library.
- **[TestNG](https://testng.org/doc/)**: Testing framework.
- **AssertJ/Soft Assertions**: For enhanced assertion capabilities.
- **[Allure Report](https://docs.qameta.io/allure/)**: Comprehensive test reporting.
- **[Selenide](https://ru.selenide.org/index.html)**: Simplified Selenium WebDriver wrapper for UI testing.

---

### Test Execution Commands

Efficiently execute your tests with the following commands:

- **Run API Tests**:
```shell
./gradlew clean apiTest -d
```

- **Run UI Tests**:
```shell
./gradlew clean uiTest -d
```

___

### Generate Allure Reports

#### Easily generate and view test reports:



UI autotests:

```shell
./gradlew allure serve ./build/allure-results
```

Alternative (if using raw results):

```shell
./gradlew allureServe
```

___


