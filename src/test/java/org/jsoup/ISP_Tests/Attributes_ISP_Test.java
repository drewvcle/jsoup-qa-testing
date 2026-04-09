package org.jsoup.ISP_Tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import main.java.org.jsoup.parser.ParseSettings;


public class Attributes_ISP_Test {

    // Attributes.deduplicate() tests
    @DisplayName("Attributes.deduplicate (One, CC, SC)")
    @Test
    public void deduplicate_oneDuplicate_considerCase_sameCase() {
        Attributes attributes = new Attributes();
        attributes.add("h2", "a");
        attributes.add("h2", "b");

        int removed = attributes.deduplicate(ParseSettings.preserveCase);
        assertEquals(1, removed);
    }

    @DisplayName("Attributes.deduplicate (No, CC, SC)")
    @Test
    public void deduplicate_noDuplicates_considerCase_sameCase() {
        Attributes attributes = new Attributes();
        attributes.add("h2", "a");
        attributes.add("p", "b");

        int removed = attributes.deduplicate(ParseSettings.preserveCase);
        assertEquals(0, removed);
    }

    @DisplayName("Attributes.deduplicate (Many, CC, SC)")
    @Test
    public void deduplicate_manyDuplicates_considerCase_sameCase() {
        Attributes attributes = new Attributes();
        attributes.add("h2", "a");
        attributes.add("h2", "b");
        attributes.add("h2", "c");

        int removed = attributes.deduplicate(ParseSettings.preserveCase);
        assertEquals(2, removed);
    }

    @DisplayName("Attributes.deduplicate (One, IC, SC)")
    @Test
    public void deduplicate_oneDuplicate_ignoreCase_sameCase() {
        Attributes attributes = new Attributes();
        attributes.add("h2", "a");
        attributes.add("h2", "b");

        int removed = attributes.deduplicate(ParseSettings.htmlDefault);
        assertEquals(1, removed);
    }

    @DisplayName("Attributes.deduplicate (One, CC, DC)")
    @Test
    public void deduplicate_oneDuplicate_considerCase_differentCase() {
        Attributes attributes = new Attributes();
        attributes.add("h2", "a");
        attributes.add("H2", "b");

        int removed = attributes.deduplicate(ParseSettings.preserveCase);
        assertEquals(0, removed);
    }

    // Attributes.put() tests
    @DisplayName("Attributes.put (T, F, F)")
    @Test
    public void put_validKey_nonNullValue_noDuplicateKeys() {
        Attributes attributes = new Attributes();
        attributes.put("h2", "v");

        assertEquals("v", attributes.get("h2"));
    }

    @DisplayName("Attributes.put (F, F, F)")
    @Test
    public void put_invalidKey_nonNullValue_noDuplicateKeys() {
        Attributes attributes = new Attributes();

        assertThrows(IllegalArgumentException.class, () -> attributes.put(null, "v"));
    }

    @DisplayName("Attributes.put (T, T, F)")
    @Test
    public void put_validKey_nullValue_noDuplicateKeys() {
        Attributes attributes = new Attributes();

        attributes.put("h2", null);

        assertEquals("", attributes.get("h2"));
    }

    @DisplayName("Attributes.put (T, F, T)")
    @Test
    public void put_validKey_nonNullValue_duplicateKeysPresent() {
        Attributes attributes = new Attributes();
        attributes.put("href", "a");

        attributes.put("href", "b");

        assertEquals("b", attributes.get("href"));
    }
}
