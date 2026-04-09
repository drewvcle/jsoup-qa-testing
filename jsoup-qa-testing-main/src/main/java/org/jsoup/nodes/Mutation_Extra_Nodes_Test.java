package org.jsoup.nodes;

import org.jsoup.parser.ParseSettings;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Mutation_Extra_Nodes_Test {

    @Test
    void hasClass_matchesSingleTokenInsideClassAttribute() {
        Element el = new Element("div");
        el.attr("class", "one two three");

        assertTrue(el.hasClass("two"));
        assertFalse(el.hasClass("four"));
    }

    @Test
    void deduplicate_preserveCase_removesOnlyExactDuplicates() {
        Attributes attrs = new Attributes();
        attrs.add("id", "1");
        attrs.add("id", "2");
        attrs.add("ID", "3");

        int dupes = attrs.deduplicate(ParseSettings.preserveCase);

        assertEquals(1, dupes);
        assertEquals(2, attrs.size());
        assertEquals("1", attrs.get("id"));
        assertEquals("3", attrs.get("ID"));
    }

    @Test
    void deduplicate_htmlDefault_removesCaseInsensitiveDuplicates() {
        Attributes attrs = new Attributes();
        attrs.add("Class", "a");
        attrs.add("class", "b");

        int dupes = attrs.deduplicate(ParseSettings.htmlDefault);

        assertEquals(1, dupes);
        assertEquals(1, attrs.size());
    }
}