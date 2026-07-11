# Test Coverage Analysis

## Are the current tests sufficient?

**Short answer:** They're a solid first pass, but there are meaningful gaps.

The tests cover the main "happy path" and the most obvious edge cases for each method.
However, several **untested execution paths** and **boundary conditions** remain.

---

## Gap Analysis by Method

### ✅ `add(int a, int b)` — Good coverage
All branches are trivially linear (no `if`/loops), so the four existing tests are sufficient for branch coverage.

> **Minor gap**: Integer overflow — `add(Integer.MAX_VALUE, 1)` silently wraps to a negative number. Worth a test if overflow behavior matters.

---

### ✅ `isPrime(int n)` — Good coverage
Both branches of the guard (`n < 2`) and the loop (`divisor found` / `no divisor found`) are exercised.

> **Minor gap**: Even composites like `4` aren't tested (though `9` covers the composite case well enough).

---

### ✅ `reverse(String s)` — Good coverage
No branches in the implementation, and the four cases (normal, empty, single char, palindrome) cover all meaningful inputs.

---

### ✅ `factorial(int n)` — Good coverage
Both the exception branch and the loop body are covered. The `0` and `1` tests cover the loop-skipped case.

> **Noteworthy gap**: `factorial(13)` silently overflows `int` (13! = 6,227,020,800 which exceeds `Integer.MAX_VALUE`). No test documents or catches this.

---

### ✅ `isPalindrome(String s)` — Good coverage
The `replaceAll` / `toLowerCase` branch is exercised by the punctuation and mixed-case tests. Well covered.

---

### ✅ `fibonacciUpTo(int n)` — Good coverage
Both the exception path and the `while` loop body are covered.

> **Minor gap**: The loop guard `a <= n` is tested at `0` and `1` but not at exactly a Fibonacci boundary (e.g., `fibonacciUpTo(8)` should include 8 but `fibonacciUpTo(9)` should not). Worth one extra test.

---

### ✅ `charFrequency(String input)` — Good coverage
The loop body and the `getOrDefault` path (first occurrence vs. repeat) are both exercised by the `"aabbc"` test.

---

### ✅ `isAnagram(String s1, String s2)` — Good coverage
Both the `true` and `false` return paths are covered, along with case/space normalization.

---

### ⚠️ `filterEvens(List<Integer> list)` — Missing cases

The `n % 2 == 0` check in Java **also evaluates to `true` for negative even numbers and zero**, but neither is tested:

| Input | Expected | Currently tested? |
|---|---|---|
| `[-2, -4, -6]` | `[-2, -4, -6]` | ❌ No |
| `[0, 1, 2]` | `[0, 2]` | ❌ No |

This is a real gap — if the implementation were ever changed to `n > 0 && n % 2 == 0`, the existing tests would still pass but the method would be broken.

---

### ⚠️ `mostCommonWord(String text)` — Missing cases

| Scenario | What happens | Currently tested? |
|---|---|---|
| Empty string `""` | Throws `NoSuchElementException` (no entries to max) | ❌ No |
| All punctuation `"!!! ???"` | Likely returns `""` as the "word" | ❌ No |
| Two words tied in frequency | Returns one arbitrarily (HashMap is unordered) | ❌ No |

The empty-string case is an **unhandled crash** — a genuine bug worth documenting with a test.

---

## Summary Table

| Method | Branch Coverage | Notable Gaps |
|---|---|---|
| `add` | ✅ Full | Integer overflow (minor) |
| `isPrime` | ✅ Full | None significant |
| `reverse` | ✅ Full | None |
| `factorial` | ✅ Full | Silent int overflow on large `n` |
| `isPalindrome` | ✅ Full | None |
| `fibonacciUpTo` | ✅ Full | Exact boundary value (minor) |
| `charFrequency` | ✅ Full | None |
| `isAnagram` | ✅ Full | None |
| `average` | ✅ Full | None |
| `filterEvens` | ⚠️ Partial | Negative evens, zero |
| `mostCommonWord` | ⚠️ Partial | Empty input (crashes), ties |

---

## How to Measure Coverage Objectively: JaCoCo

The industry-standard way to measure coverage in a Java/Gradle project is **JaCoCo** (Java Code Coverage).
It has been added to `build.gradle.kts` in this project. Run it with:

```bash
JAVA_HOME=/usr/lib/jvm/java-17-temurin-jdk ./gradlew test jacocoTestReport
```

Then open the HTML report at:
```
app/build/reports/jacoco/test/html/index.html
```

JaCoCo measures three levels:

| Metric | What it checks |
|---|---|
| **Line coverage** | Was each line executed at least once? |
| **Branch coverage** | Was each `if`/`else`/loop path taken? |
| **Method coverage** | Was each method called at least once? |

> **Branch coverage is the most meaningful metric.** 100% line coverage is easy to achieve; 100% branch coverage means every `true`/`false` decision in the code was exercised.

---

## Beyond Coverage: Mutation Testing (Advanced)

Code coverage tells you *which lines were run*, but not *whether your assertions actually catch bugs*.
**Mutation testing** answers that — it deliberately introduces small bugs ("mutants") into your source code and checks if any test fails as a result. If a mutant **survives** (no test catches it), your tests have a blind spot.

The tool for this in Java is **PIT** (`pitest`). It can be added to Gradle similarly to JaCoCo.

```
# A surviving mutant example:
# Original:  if (n < 2) return false;
# Mutant:    if (n <= 2) return false;   ← would isPrime_two_returnsTrue catch this? Yes.
# Mutant:    if (n < 3) return false;    ← same. Good.
```

---

> **Rule of thumb for "sufficient" tests:**
> - Aim for **100% branch coverage** as a baseline.
> - Every method that throws an exception should have a test for the throwing path.
> - Every boundary value (0, -1, empty string, single element) should have a test.
> - Use JaCoCo to find what you missed — then write tests to fill those gaps.
