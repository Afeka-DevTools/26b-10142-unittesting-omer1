package org.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class AppTest {

    // -------------------------------------------------------------------------
    // add(int a, int b)
    // -------------------------------------------------------------------------

    @ParameterizedTest(name = "add({0}, {1}) == {2}")
    @CsvSource({
        " 3,  4,  7",   // two positives
        " 3, -4, -1",   // positive + negative
        "-4, -5, -9",   // two negatives
        " 5,  0,  5",   // identity: adding zero
    })
    void add_returnsCorrectSum(int a, int b, int expected) {
        assertEquals(expected, App.add(a, b),
                () -> "add(" + a + ", " + b + ") should return " + expected);
    }

    // -------------------------------------------------------------------------
    // isPrime(int n)
    // -------------------------------------------------------------------------

    @ParameterizedTest(name = "{0} is prime")
    @ValueSource(ints = {2, 3, 5, 7, 97})
    void isPrime_primeNumbers_returnTrue(int n) {
        assertTrue(App.isPrime(n),
                n + " is a prime number and should return true");
    }

    @ParameterizedTest(name = "{0} is not prime")
    @ValueSource(ints = {-5, 0, 1, 4, 9, 25})
    void isPrime_nonPrimeNumbers_returnFalse(int n) {
        assertFalse(App.isPrime(n),
                n + " is not prime and should return false");
    }

    // -------------------------------------------------------------------------
    // reverse(String s)
    // -------------------------------------------------------------------------

    @ParameterizedTest(name = "reverse(\"{0}\") == \"{1}\"")
    @CsvSource({
        "hello,   olleh",    // normal string
        "'',      ''",       // empty string  (single-quoted so CSV parser keeps them)
        "a,       a",        // single character
        "racecar, racecar",  // palindrome stays the same
    })
    void reverse_returnsReversedString(String input, String expected) {
        assertEquals(expected, App.reverse(input),
                () -> "reverse(\"" + input + "\") should return \"" + expected + "\"");
    }

    // -------------------------------------------------------------------------
    // factorial(int n)
    // -------------------------------------------------------------------------

    @ParameterizedTest(name = "{0}! == {1}")
    @CsvSource({
        "0,  1",    // 0! = 1 by definition
        "1,  1",    // 1! = 1
        "5,  120",  // 5! = 120
        "6,  720",  // 6! = 720
    })
    void factorial_nonNegativeInput_returnsCorrectResult(int n, int expected) {
        assertEquals(expected, App.factorial(n),
                n + "! should equal " + expected);
    }

    @Test
    void factorial_negativeInput_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> App.factorial(-1),
                "factorial of a negative number should throw IllegalArgumentException");
    }

    // -------------------------------------------------------------------------
    // isPalindrome(String s)
    // -------------------------------------------------------------------------

    @ParameterizedTest(name = "\"{0}\" is a palindrome")
    @ValueSource(strings = {
        "racecar",                        // simple palindrome
        "RaceCar",                        // mixed case
        "A man, a plan, a canal: Panama", // spaces and punctuation
        "",                               // empty string is trivially a palindrome
    })
    void isPalindrome_palindromeInputs_returnTrue(String s) {
        assertTrue(App.isPalindrome(s),
                "\"" + s + "\" should be recognised as a palindrome");
    }

    @ParameterizedTest(name = "\"{0}\" is not a palindrome")
    @ValueSource(strings = {"hello", "world", "java"})
    void isPalindrome_nonPalindromeInputs_returnFalse(String s) {
        assertFalse(App.isPalindrome(s),
                "\"" + s + "\" is not a palindrome and should return false");
    }

    // -------------------------------------------------------------------------
    // fibonacciUpTo(int n)
    // -------------------------------------------------------------------------

    static Stream<org.junit.jupiter.params.provider.Arguments> fibonacciUpToProvider() {
        return Stream.of(
            org.junit.jupiter.params.provider.Arguments.of(0,  Arrays.asList(0)),
            org.junit.jupiter.params.provider.Arguments.of(1,  Arrays.asList(0, 1, 1)),
            org.junit.jupiter.params.provider.Arguments.of(10, Arrays.asList(0, 1, 1, 2, 3, 5, 8))
        );
    }

    @ParameterizedTest(name = "fibonacciUpTo({0}) == {1}")
    @MethodSource("fibonacciUpToProvider")
    void fibonacciUpTo_validLimit_returnsCorrectSequence(int limit, List<Integer> expected) {
        assertEquals(expected, App.fibonacciUpTo(limit),
                "fibonacciUpTo(" + limit + ") returned an unexpected sequence");
    }

    @Test
    void fibonacciUpTo_negativeLimit_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> App.fibonacciUpTo(-1),
                "a negative limit should throw IllegalArgumentException");
    }

    // -------------------------------------------------------------------------
    // charFrequency(String input)
    // charFrequency asserts multiple map entries per case, so individual @Test
    // methods remain clearer than a parameterized approach here.
    // -------------------------------------------------------------------------

    @Test
    void charFrequency_normalString_returnsCorrectFrequencies() {
        Map<Character, Integer> freq = App.charFrequency("aabbc");
        assertEquals(2, freq.get('a'), "character 'a' appears twice in \"aabbc\"");
        assertEquals(2, freq.get('b'), "character 'b' appears twice in \"aabbc\"");
        assertEquals(1, freq.get('c'), "character 'c' appears once in \"aabbc\"");
    }

    @Test
    void charFrequency_emptyString_returnsEmptyMap() {
        assertTrue(App.charFrequency("").isEmpty(),
                "an empty string should produce an empty frequency map");
    }

    @Test
    void charFrequency_singleChar_returnsCountOfOne() {
        Map<Character, Integer> freq = App.charFrequency("z");
        assertEquals(1, freq.get('z'),
                "a single-character string should have a frequency of 1 for that character");
    }

    // -------------------------------------------------------------------------
    // isAnagram(String s1, String s2)
    // -------------------------------------------------------------------------

    @ParameterizedTest(name = "\"{0}\" and \"{1}\" are anagrams")
    @CsvSource({
        "listen,        silent",           // classic anagram
        "Listen,        Silent",           // case-insensitive
        "conversation,  'voices rant on'", // spaces ignored
    })
    void isAnagram_anagramPairs_returnTrue(String s1, String s2) {
        assertTrue(App.isAnagram(s1, s2),
                "\"" + s1 + "\" and \"" + s2 + "\" should be recognised as anagrams");
    }

    @ParameterizedTest(name = "\"{0}\" and \"{1}\" are NOT anagrams")
    @CsvSource({
        "hello, world",  // completely different letters
        "abc,   ab",     // different lengths
    })
    void isAnagram_nonAnagramPairs_returnFalse(String s1, String s2) {
        assertFalse(App.isAnagram(s1, s2),
                "\"" + s1 + "\" and \"" + s2 + "\" should not be recognised as anagrams");
    }

    // -------------------------------------------------------------------------
    // average(int[] arr)
    // Arrays can't be inlined into @CsvSource, so a @MethodSource provider is used.
    // -------------------------------------------------------------------------

    static Stream<org.junit.jupiter.params.provider.Arguments> averageProvider() {
        return Stream.of(
            org.junit.jupiter.params.provider.Arguments.of(new int[]{1, 2, 3, 4, 5},  3.0,  "average of [1,2,3,4,5]"),
            org.junit.jupiter.params.provider.Arguments.of(new int[]{7},               7.0,  "average of a single-element array"),
            org.junit.jupiter.params.provider.Arguments.of(new int[]{-1, -2, -3},     -2.0,  "average of [-1,-2,-3]")
        );
    }

    @ParameterizedTest(name = "{2} == {1}")
    @MethodSource("averageProvider")
    void average_validArray_returnsCorrectAverage(int[] arr, double expected, String description) {
        assertEquals(expected, App.average(arr), 0.001, description + " should be " + expected);
    }

    @Test
    void average_emptyArray_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> App.average(new int[]{}),
                "computing the average of an empty array should throw IllegalArgumentException");
    }

    // -------------------------------------------------------------------------
    // filterEvens(List<Integer> list)
    // Input and expected output are both lists; @MethodSource keeps this readable.
    // -------------------------------------------------------------------------

    static Stream<org.junit.jupiter.params.provider.Arguments> filterEvensProvider() {
        return Stream.of(
            org.junit.jupiter.params.provider.Arguments.of(
                Arrays.asList(1, 2, 3, 4, 5, 6), Arrays.asList(2, 4, 6),
                "mixed list — only evens kept"),
            org.junit.jupiter.params.provider.Arguments.of(
                Arrays.asList(2, 4, 6),           Arrays.asList(2, 4, 6),
                "all-even list — all elements kept"),
            org.junit.jupiter.params.provider.Arguments.of(
                Arrays.asList(-4, -3, -2, -1),    Arrays.asList(-4, -2),
                "negative even numbers must be included"),
            org.junit.jupiter.params.provider.Arguments.of(
                Arrays.asList(-1, 0, 1),           Arrays.asList(0),
                "zero is even and must be included")
        );
    }

    @ParameterizedTest(name = "{2}")
    @MethodSource("filterEvensProvider")
    void filterEvens_variousInputs_returnsOnlyEvens(List<Integer> input,
                                                     List<Integer> expected,
                                                     String description) {
        assertEquals(expected, App.filterEvens(input), description);
    }

    static Stream<org.junit.jupiter.params.provider.Arguments> filterEvensNoEvensProvider() {
        return Stream.of(
            org.junit.jupiter.params.provider.Arguments.of(
                Arrays.asList(1, 3, 5), "list of only odd numbers"),
            org.junit.jupiter.params.provider.Arguments.of(
                Arrays.asList(),        "empty list")
        );
    }

    @ParameterizedTest(name = "filterEvens({0}) returns empty list")
    @MethodSource("filterEvensNoEvensProvider")
    void filterEvens_listsWithNoEvens_returnEmptyList(List<Integer> input, String description) {
        assertTrue(App.filterEvens(input).isEmpty(), description);
    }

    // -------------------------------------------------------------------------
    // mostCommonWord(String text)
    // -------------------------------------------------------------------------

    @ParameterizedTest(name = "most common word in \"{0}\" is \"{1}\"")
    @CsvSource({
        "'the cat sat on the mat and the cat', the",  // 'the' appears 3 times
        "hello,                                hello", // single word
        "'Word word WORD',                     word",  // case-insensitive normalisation
    })
    void mostCommonWord_validInput_returnsMostFrequentWord(String text, String expected) {
        assertEquals(expected, App.mostCommonWord(text),
                "most common word in \"" + text + "\" should be \"" + expected + "\"");
    }

    @Test
    void mostCommonWord_emptyString_returnsEmptyToken() {
        // split("\\W+") on "" produces [""], so Collections.max() runs on a
        // non-empty map — the result is the empty string "".
        // This test documents the actual (surprising) behaviour so any future
        // change to the method is a conscious, tested decision.
        assertEquals("", App.mostCommonWord(""),
                "empty input produces a single empty-string token; the method returns \"\" rather than throwing");
    }

    @Test
    void mostCommonWord_punctuationOnly_throwsNoSuchElementException() {
        // split("\\W+") on punctuation-only input returns an *empty* array (unlike
        // split on "", which returns [""]).  Collections.max() then receives an empty
        // map and throws NoSuchElementException — an unhandled crash in the implementation.
        assertThrows(java.util.NoSuchElementException.class,
                () -> App.mostCommonWord("!!! ???"),
                "punctuation-only input produces an empty word list, causing Collections.max() to throw");
    }
}
