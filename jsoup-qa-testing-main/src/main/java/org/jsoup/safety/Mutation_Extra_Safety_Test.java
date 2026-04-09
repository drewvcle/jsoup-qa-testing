package org.jsoup.safety;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Mutation_Extra_Safety_Test {

    @Test
    void isValid_returnsTrueForAllowedBodyAndEmptyHead() {
        Cleaner cleaner = new Cleaner(Safelist.basic());
        Document doc = Jsoup.parse("<p>Hello</p><b>Bold</b>");

        assertTrue(cleaner.isValid(doc));
    }

    @Test
    void isValid_returnsFalseWhenHeadContainsContent() {
        Cleaner cleaner = new Cleaner(Safelist.basic());
        Document doc = Jsoup.parse("<html><head><title>X</title></head><body><p>Hello</p></body></html>");

        assertFalse(cleaner.isValid(doc));
    }

    @Test
    void isSafeAttribute_allowsValidHref_butRejectsJavascriptAndUnknownAttribute() {
        Safelist safelist = Safelist.basic();
        Element el = Jsoup.parse("<a href='http://example.com'>x</a>").selectFirst("a");

        assertTrue(safelist.isSafeAttribute("a", el, new Attribute("href", "http://example.com")));
        assertFalse(safelist.isSafeAttribute("a", el, new Attribute("onclick", "alert(1)")));
        assertFalse(safelist.isSafeAttribute("a", el, new Attribute("href", "javascript:alert(1)")));
    }

    @Test
    void isSafeAttribute_fallsBackToAllTag() {
        Safelist safelist = new Safelist()
                .addTags("p")
                .addAttributes(":all", "data-x");

        Element el = new Element("p");

        assertTrue(safelist.isSafeAttribute("p", el, new Attribute("data-x", "1")));
    }

    @Test
    void removeProtocols_removesRestrictionsAndReturnsSameObject() {
        Safelist safelist = new Safelist()
                .addTags("a")
                .addAttributes("a", "href")
                .addProtocols("a", "href", "http", "https");

        Element el = Jsoup.parse("<a href='javascript:alert(1)'>x</a>").selectFirst("a");

        assertFalse(safelist.isSafeAttribute("a", el, new Attribute("href", "javascript:alert(1)")));

        Safelist returned = safelist.removeProtocols("a", "href", "http", "https");

        assertSame(safelist, returned);
        assertTrue(safelist.isSafeAttribute("a", el, new Attribute("href", "javascript:alert(1)")));
    }
}