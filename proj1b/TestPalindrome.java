import org.junit.Test;
import static org.junit.Assert.*;

public class TestPalindrome {
    // You must use this palindrome, and not instantiate
    // new Palindromes, or the autograder might be upset.
    static Palindrome palindrome = new Palindrome();

    @Test
    public void testWordToDeque() {
        Deque d = palindrome.wordToDeque("persiflage");
        String actual = "";
        for (int i = 0; i < "persiflage".length(); i++) {
            actual += d.removeFirst();
        }
        assertEquals("persiflage", actual);
    }

    @Test
    public void testIsPalindrome() {
        boolean test1 = palindrome.isPalindrome("aba");
        assertTrue(test1);
        boolean test2 = palindrome.isPalindrome("asdfggfdsa");
        assertTrue(test2);
        boolean test3 = palindrome.isPalindrome("");
        assertTrue(test3);
        boolean test4 = palindrome.isPalindrome("qrqfadsf");
        assertFalse(test4);
        boolean test5 = palindrome.isPalindrome("horse");
        assertFalse(test5);
    }
    @Test
    public void testIsPalindromeOffByOne() {
        CharacterComparator cc=new OffByOne();
        boolean test1 = palindrome.isPalindrome("flake",cc);
        assertTrue(test1);
        boolean test2 = palindrome.isPalindrome("asdfggfdsa",cc);
        assertFalse(test2);
        boolean test3 = palindrome.isPalindrome("");
        assertTrue(test3);
        boolean test4 = palindrome.isPalindrome("qrqfadsf",cc);
        assertFalse(test4);
        boolean test5 = palindrome.isPalindrome("horse",cc);
        assertFalse(test5);
    }
    @Test
    public void testIsPalindromeOffByN() {
        CharacterComparator cc=new OffByN(3);
        boolean test1 = palindrome.isPalindrome("f",cc);
        assertTrue(test1);
        boolean test2 = palindrome.isPalindrome("asdfggfdsa",cc);
        assertFalse(test2);
        boolean test3 = palindrome.isPalindrome("");
        assertTrue(test3);
        boolean test4 = palindrome.isPalindrome("ad",cc);
        assertTrue(test4);
        boolean test5 = palindrome.isPalindrome("horse",cc);
        assertFalse(test5);
    }

    public static void main(String... args) {
        jh61b.junit.TestRunner.runTests("all", TestPalindrome.class);
    }
}
