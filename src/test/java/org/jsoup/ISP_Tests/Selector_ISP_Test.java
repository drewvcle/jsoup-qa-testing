package org.jsoup.ISP_Tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.ParseSettings;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Safelist;
import org.jsoup.select.Selector;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


public class Selector_ISP_Test {

    @DisplayName("Selector.select (T, T)")
    @Test
    public void select_validQueryTrue_queryInRootTrue() {
        Document document = Jsoup.parse("<div><p>selct</p><h2>Test</h2></div>");
        assertEquals(1, Selector.select("h2", document).size());
    }

    @DisplayName("Selector.select (F, F)")
    @Test
    public void select_validQueryFalse_queryInRootFalse() {
        Document document = Jsoup.parse("<div><h>select</h><p>Test</p></div>");
        assertThrows(IllegalArgumentException.class, () -> Selector.select("", document));
    }

    @DisplayName("Selector.select (T, F)")
    @Test
    public void select_validQueryTrue_queryInRootFalse() {
        Document document = Jsoup.parse("<div><h>select</h></div>");
        assertEquals(0, Selector.select("h2", document).size());
    }

    @DisplayName("Selector.selectFirst (T, T)")
    @Test
    public void selectFirst_validCssQueryTrue_queryInRootTrue() {
        Document document = Jsoup.parse("<div><h>selectFirst</h><p>Test</p></div>");
        assertEquals("h", Selector.selectFirst("h", document).tagName());
    }

    @DisplayName("Selector.selectFirst (F, F)")
    @Test
    public void selectFirst_validCssQueryFalse_queryInRootFalse() {
        Document document = Jsoup.parse("<div><p>selectFirst</p></div>");
        assertThrows(IllegalArgumentException.class, () -> Selector.selectFirst("", document));
    }

    @DisplayName("Selector.selectFirst (T, F)")
    @Test
    public void selectFirst_validCssQueryTrue_queryInRootFalse() {
        Document document = Jsoup.parse("<div><p>selectFirst</p></div>");
        assertNull(Selector.selectFirst("span", document));
    }
}
