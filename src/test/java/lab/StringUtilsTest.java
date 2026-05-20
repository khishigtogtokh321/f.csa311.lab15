package lab;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StringUtilsTest {

    @Test
    @DisplayName("Хоосон string урвуулах")
    void reverse_emptyString() {
        assertEquals("", StringUtils.reverse(""));
    }

    @Test
    @DisplayName("Нэг тэмдэгт өөрчлөгдөхгүй")
    void reverse_singleCharacter() {
        assertEquals("a", StringUtils.reverse("a"));
    }

    @Test
    @DisplayName("ASCII string урвуулах")
    void reverse_asciiString() {
        assertEquals("dlrow olleh", StringUtils.reverse("hello world"));
    }

    @Test
    @DisplayName("Юникод тэмдэгттэй string урвуулах")
    void reverse_unicodeString() {
        assertEquals("абйаС", StringUtils.reverse("Сайба"));
    }

    @Test
    @DisplayName("Null утга null хэвээр буцна")
    void reverse_nullString() {
        assertNull(StringUtils.reverse(null));
    }

    @Test
    void isBlank_handlesNullEmptyAndWhitespace() {
        assertTrue(StringUtils.isBlank(null));
        assertTrue(StringUtils.isBlank(""));
        assertTrue(StringUtils.isBlank("   "));
        assertFalse(StringUtils.isBlank("lab"));
    }

    @Test
    void capitalize_handlesBlankAndWords() {
        assertNull(StringUtils.capitalize(null));
        assertEquals("", StringUtils.capitalize(""));
        assertEquals("Lab", StringUtils.capitalize("lab"));
        assertEquals("Lab", StringUtils.capitalize("Lab"));
    }
}
