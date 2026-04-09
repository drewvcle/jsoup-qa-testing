package org.jsoup.select;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class Mutation_Extra_Selector_Test {

    @Test
    void select_iterableDeduplicatesOverlappingRoots() {
        Document doc = Jsoup.parse("<div><p id='x'>A</p></div>");
        Element div = doc.selectFirst("div");

        Elements result = Selector.select("p", Arrays.asList(doc, div));

        assertEquals(1, result.size());
        assertEquals("x", result.get(0).id());
    }

    @Test
    void selectFirst_iterableReturnsFirstMatchAcrossRoots() {
        Element firstRoot = Jsoup.parse("<section></section>").selectFirst("section");
        Document secondDoc = Jsoup.parse("<div><p id='target'>A</p></div>");

        Element result = Selector.selectFirst("p", Arrays.asList(firstRoot, secondDoc));

        assertNotNull(result);
        assertEquals("target", result.id());
    }

    @Test
    void selectFirst_iterableReturnsNullWhenNoMatchExists() {
        Element root = Jsoup.parse("<div><span>A</span></div>").selectFirst("div");

        assertNull(Selector.selectFirst("p", Arrays.asList(root)));
    }
}