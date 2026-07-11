> [!IMPORTANT]
> Tool used: Google Antigravity \
> Model: Gemini 3.1 Pro

👨🏻‍🦱 **Prompt**

Explain unit testing in software development, and why they're important, and specifically how to write them in Java

🤖 **Gemini**

## What is Unit Testing?

Unit testing is a software development practice where the smallest testable parts of an application—usually individual methods or functions—are tested in isolation. The goal is to validate that each discrete "unit" of code performs exactly as intended. These tests are written by developers as they build the application and are executed automatically via build tools or continuous integration (CI) pipelines.

---

## Why Unit Tests are Important

- **Early Bug Detection:** By testing isolated units, developers catch logical errors immediately after writing the code, preventing bugs from cascading into complex, integrated systems where they are harder and more expensive to trace.
- **Safe Refactoring:** Unit tests act as a safety net. If you need to optimize or rewrite a piece of code, a passing test suite guarantees that you haven't altered the intended external behavior of that unit.
- **Living Documentation:** Well-written tests demonstrate exactly how a class or method is supposed to be used and what outputs it produces for specific inputs. This is often more reliable than written documentation, which can quickly become outdated.
- **Enforces Better Design:** Code that is difficult to unit test usually suffers from poor design (e.g., tight coupling, hidden dependencies, violating the Single Responsibility Principle). The friction of writing tests forces developers to build more modular, decoupled architectures.

---

## How to Write Unit Tests in Java

In Java, **JUnit** (specifically JUnit 5) is the industry-standard framework for writing and running unit tests.

### 1. The AAA Pattern

A standard unit test follows the **Arrange, Act, Assert (AAA)** structure:

1. **Arrange:** Set up the test environment. Initialize objects, set variables, and mock necessary dependencies.
2. **Act:** Invoke the specific method being tested.
3. **Assert:** Verify that the output or the resulting state matches your expectations.

### 2. Code Example

Let's assume you have a simple class that handles business logic, like a `Calculator`.

**The Target Class (`Calculator.java`)**

```java
public class Calculator {
    public int add(int a, int b) {
        return a + b;
    }

    public int divide(int a, int b) {
        if (b == 0) {
            throw new IllegalArgumentException("Cannot divide by zero");
        }
        return a / b;
    }
}

```

**The Test Class (`CalculatorTest.java`)**
To test this, you create a corresponding test class.

```java
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CalculatorTest {

    @Test
    void add_TwoPositiveNumbers_ReturnsSum() {
        // Arrange
        Calculator calculator = new Calculator();

        // Act
        int result = calculator.add(5, 3);

        // Assert
        assertEquals(8, result, "5 + 3 should equal 8");
    }

    @Test
    void divide_ValidNumbers_ReturnsQuotient() {
        // Arrange
        Calculator calculator = new Calculator();

        // Act
        int result = calculator.divide(10, 2);

        // Assert
        assertEquals(5, result);
    }

    @Test
    void divide_ByZero_ThrowsIllegalArgumentException() {
        // Arrange
        Calculator calculator = new Calculator();

        // Act & Assert (combined when testing for exceptions)
        assertThrows(IllegalArgumentException.class, () -> {
            calculator.divide(10, 0);
        }, "Dividing by zero should throw an IllegalArgumentException");
    }
}

```

### 3. Key JUnit 5 Annotations and Assertions

- `@Test`: Tells the JUnit runner that the method is a test case.
- `@BeforeEach`: Executes before _every_ test method. Useful for resetting data or re-initializing the class under test so every test starts with a clean slate.
- `@AfterEach`: Executes after _every_ test method (useful for teardown or cleanup).
- `assertEquals(expected, actual)`: The most common assertion. It fails the test if the two values do not match.
- `assertThrows(Exception.class, executable)`: Verifies that the enclosed code block (often a lambda) throws a specific exception. If the exception is not thrown, the test fails.

---

👨🏻‍🦱 **Prompt**

You mentioned `@BeforeEach` and `@AfterEach` - explain them in more detail, and where they would be useful. Give an example for each.

🤖 **Gemini**

In JUnit 5, `@BeforeEach` and `@AfterEach` are lifecycle annotations. They are used to manage the environment—or "state"—in which your tests run. Because unit tests must be entirely independent of one another, you often need a way to set up a clean slate before a test executes and clean up any lingering artifacts after it finishes.

Here is a detailed breakdown of each.

---

## `@BeforeEach`

The `@BeforeEach` annotation flags a method to be executed **before every single `@Test` method** in the current class.

### Where it is useful

- **Preventing Test Pollution:** If one test modifies an object, you do not want those modifications leaking into the next test and causing a false positive or negative.
- **Reducing Code Duplication:** Instead of instantiating the target class and its dependencies at the beginning of every test method, you centralize that setup in one place.
- **Initializing Mocks/Resources:** Setting up mock data, establishing a connection to an in-memory database, or initializing variables to a known baseline state.

### Example

If you are testing a `ShoppingCart`, you want to ensure every test starts with an empty cart.

```java
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ShoppingCartTest {

    private ShoppingCart cart;

    @BeforeEach
    void setUp() {
        // This runs immediately before testAddProduct()
        // AND immediately before testCalculateTotal()
        cart = new ShoppingCart();
    }

    @Test
    void testAddProduct() {
        cart.add("Apple", 1);
        assertEquals(1, cart.getItemCount());
    }

    @Test
    void testCalculateTotal() {
        cart.add("Apple", 1); // We know the cart was empty before this line
        cart.add("Banana", 2);
        assertEquals(3, cart.getItemCount());
    }
}

```

---

## `@AfterEach`

The `@AfterEach` annotation flags a method to be executed **after every single `@Test` method** in the class completes, regardless of whether the test passed, failed, or threw an exception.

### Where it is useful

- **Resource Cleanup:** Closing open file streams, network connections, or database connections so you don't exhaust system resources during a large test suite run.
- **State Reset:** Reverting static variables, clearing out caches, or deleting temporary files created during the test.
- **System integrity:** Ensuring that the environment is returned to its original state so subsequent test classes are not impacted.

### Example

If you are testing a class that writes to a database or a file system, you must clean up those side effects. Java's garbage collector handles objects in memory (like the `ShoppingCart` above), but it won't drop database tables or delete files on your hard drive.

```java
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UserDatabaseTest {

    private DatabaseConnection db;

    @BeforeEach
    void connectToDatabase() {
        db = new DatabaseConnection("jdbc:test-db");
        db.connect();
    }

    @AfterEach
    void tearDown() {
        // This executes even if testInsertUser() fails.
        // It guarantees the test database is wiped and the connection is closed.
        db.clearAllTables();
        db.disconnect();
    }

    @Test
    void testInsertUser() {
        db.insert("Alice");
        assertTrue(db.containsUser("Alice"));
    }

    @Test
    void testDeleteUser() {
        db.insert("Bob");
        db.delete("Bob");
        assertFalse(db.containsUser("Bob"));
    }
}

```

---

👨🏻‍🦱 **Prompt**

Describe some other useful assertion functions, like `assertEquals` and `assertThrows` you mentioned earlier. Explain where they would be useful and give a usage example for each.

🤖 **Gemini**

Here are several other core assertion functions provided by JUnit 5, along with their specific use cases and examples.

### 1. `assertTrue` and `assertFalse`

These assertions verify that a supplied condition evaluates to `true` or `false`.

- **Where they are useful:** Validating boolean return methods (like `isEmpty()`, `isValid()`, `hasPermissions()`) or checking complex expressions that resolve to a boolean value.

**Example:**

```java
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ValidationTest {

    @Test
    void testPasswordValidation() {
        PasswordValidator validator = new PasswordValidator();

        // Asserting a valid password returns true
        assertTrue(validator.isValid("SecureP@ssw0rd!"), "Password should be valid");

        // Asserting an invalid password returns false
        assertFalse(validator.isValid("123"), "Short password should be invalid");
    }
}

```

### 2. `assertNull` and `assertNotNull`

These assertions check whether an object reference points to a specific object in memory or if it is `null`.

- **Where they are useful:** Testing repository or service layers where searching for a non-existent entity should return `null`, or verifying that an object factory successfully instantiated an object.

**Example:**

```java
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UserRepositoryTest {

    @Test
    void findUser_ExistingId_ReturnsUser() {
        UserRepository repo = new UserRepository();
        User user = repo.findById(1);

        assertNotNull(user, "User object should not be null for existing ID");
    }

    @Test
    void findUser_NonExistingId_ReturnsNull() {
        UserRepository repo = new UserRepository();
        User user = repo.findById(999);

        assertNull(user, "User object should be null for non-existent ID");
    }
}

```

### 3. `assertAll`

This assertion allows you to group multiple assertions together. In a standard test, if the first assertion fails, the test aborts immediately, and subsequent assertions are never executed. `assertAll` executes every assertion in the block and reports all failures at once.

- **Where it is useful:** Verifying multiple attributes of a single object simultaneously. This prevents you from having to run a test multiple times to fix a series of sequential failures.

**Example:**

```java
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void createUser_ValidInputs_SetsAllProperties() {
        User user = new User("jdoe", "John", "Doe");

        assertAll("User properties",
            () -> assertEquals("jdoe", user.getUsername()),
            () -> assertEquals("John", user.getFirstName()),
            () -> assertEquals("Doe", user.getLastName())
        );
    }
}

```

### 4. `assertTimeout`

This assertion verifies that a block of code executes within a specified time limit. If the code takes longer than the allotted time, the test fails.

- **Where it is useful:** Establishing performance baselines or ensuring that a specific method (e.g., a complex database query or a sorting algorithm) adheres to strict Service Level Agreements (SLAs) for response times.

**Example:**

```java
import org.junit.jupiter.api.Test;
import java.time.Duration;
import static org.junit.jupiter.api.Assertions.*;

class PerformanceTest {

    @Test
    void sortLargeArray_CompletesWithinTimeLimit() {
        DataSorter sorter = new DataSorter();
        int[] largeArray = generateLargeArray(); // assume this generates 1,000,000 items

        assertTimeout(Duration.ofMillis(100), () -> {
            sorter.sort(largeArray);
        }, "Sorting the array should take less than 100 milliseconds");
    }
}

```

### 5. `assertIterableEquals`

This assertion verifies that two iterables (like `List` or `Set`) are deeply equal. This means they must contain the same elements, in the exact same order.

- **Where it is useful:** Testing methods that return collections, ensuring that sorting algorithms work correctly, or verifying that a database query returns records in the expected sequence.

**Example:**

```java
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class ListTest {

    @Test
    void getTopThreeScores_ReturnsScoresInDescendingOrder() {
        ScoreBoard board = new ScoreBoard();
        List<Integer> expectedScores = Arrays.asList(99, 85, 72);

        List<Integer> actualScores = board.getTopThreeScores();

        assertIterableEquals(expectedScores, actualScores, "The lists should contain the exact same elements in the exact same order");
    }
}

```

---

👨🏻‍🦱 **Prompt**

How can I write good tests that properly handle all edge cases?

🤖 **Gemini**

Writing tests that catch edge cases requires shifting your mindset from verifying that the code _works_ (the "happy path") to actively trying to _break_ it.

To systematically handle edge cases without writing hundreds of redundant tests, you can combine formal testing theories with advanced JUnit 5 features.

---

## 1. Systematic Techniques for Finding Edge Cases

Instead of guessing what might go wrong, use these two foundational software testing techniques to mathematically narrow down your test cases:

- **Equivalence Partitioning:** Divide all possible inputs into "valid" and "invalid" buckets (partitions). If one input in a bucket behaves a certain way, assume all others in that bucket will too. You only need to write one test per bucket.
- **Boundary Value Analysis (BVA):** Bugs almost always cluster at the boundaries of these partitions. If your method accepts ages `18` to `65`, the boundaries are the exact numbers, just below them, and just above them. You must test: `17`, `18`, `19`, and `64`, `65`, `66`.

### Common Edge Case Triggers to Always Check:

- **Collections/Strings:** `null`, empty (`""` or size 0), size 1, extremely large sizes.
- **Numbers:** `0`, `-1`, `1`, negative numbers when positive are expected, `Integer.MAX_VALUE`, and `Integer.MIN_VALUE` (to check for overflow).
- **Dates/Time:** Leap years, different time zones, UNIX epoch (Jan 1, 1970).

---

## 2. The Solution in Code: Parameterized Tests

When you start testing all these boundaries, writing a separate `@Test` method for `17`, `18`, and `19` creates massive code bloat.

JUnit 5 solves this with **Parameterized Tests**. This allows you to run the exact same test logic multiple times with different inputs.

**Example: Testing a Discount Calculator** \
Let's say a method grants a 10% discount if you buy 5 or more items, up to a maximum of 10 items. Buying more than 10 or less than 0 throws an exception.

```java
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import static org.junit.jupiter.api.Assertions.*;

class DiscountCalculatorTest {

    private final DiscountCalculator calc = new DiscountCalculator();

    // 1. Testing the Boundaries with CsvSource (Input, ExpectedOutput)
    @ParameterizedTest(name = "Buying {0} items should yield a {1}% discount")
    @CsvSource({
        "4, 0",   // Just below boundary
        "5, 10",  // Exact boundary
        "6, 10",  // Just above boundary
        "9, 10",  // Just below upper boundary
        "10, 10"  // Exact upper boundary
    })
    void calculateDiscount_ValidQuantities_ReturnsCorrectDiscount(int items, int expectedDiscount) {
        assertEquals(expectedDiscount, calc.calculateDiscount(items));
    }

    // 2. Testing Invalid Edge Cases with ValueSource
    @ParameterizedTest(name = "Quantity {0} should throw exception")
    @ValueSource(ints = { -1, 11, Integer.MAX_VALUE }) // The negative edge cases
    void calculateDiscount_InvalidQuantities_ThrowsException(int invalidItems) {
        assertThrows(IllegalArgumentException.class, () -> {
            calc.calculateDiscount(invalidItems);
        });
    }
}

```

_Notice how just two methods comprehensively test 8 different boundary and edge conditions._

---

## 3. Best Practices for Bulletproof Tests

- **Don't rely solely on Code Coverage:** Tools like JaCoCo will tell you if a line of code was _executed_ during a test, but it does not check if you actually _asserted_ the correct behavior. 100% line coverage means nothing if you have weak assertions.
- **Follow the F.I.R.S.T. Principles:**
  - **F**ast: Tests should run in milliseconds so you can run them constantly.
  - **I**solated: Tests should not depend on the order in which they run, nor share state.
  - **R**epeatable: They should yield the same result locally, on a coworker's machine, and in the CI pipeline.
  - **S**elf-validating: They must output a boolean pass/fail. You shouldn't have to check a log file to see if it worked.
  - **T**imely: Write them just before or exactly alongside the production code.

What kind of specific logic or application are you trying to write tests for right now?
