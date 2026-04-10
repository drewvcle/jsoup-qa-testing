package org.jsoup.ISP_Tests;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.parser.ParseSettings;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Safelist;
import org.jsoup.select.Selector;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


public class Element_ISP_Test {

    // hasClass() tests
    @DisplayName("Element.hasClass (F, T)")
    @Test
    public void hasClass_classNameWRTNullFalse_elementHasAttributeTrue() {
        Element element = new Element("div");
        element.attr("class", "h2");

        assertTrue(element.hasClass("h2"));
    }

    @DisplayName("Element.hasClass (T, F)")
    @Test
    public void hasClass_classNameWRTNullTrue_elementHasAttributeFalse() {
        Element element = new Element("div");

        assertFalse(element.hasClass(null));
    }

    @DisplayName("Element.hasClass (F, F)")
    @Test
    public void hasClass_classNameWRTNullFalse_elementHasAttributeFalse() {
        Element element = new Element("div");

        assertFalse(element.hasClass("h2"));
    }

    // child() tests
    @DisplayName("Element.child (O)")
    @Test
    public void child_outOfBounds() {
        Element element = new Element("div");
        element.appendChild(new Element("h"));
        element.appendChild(new Element("p"));
        element.appendChild(new Element("span"));

        assertThrows(IndexOutOfBoundsException.class, () -> element.child(3));
    }

    @DisplayName("Element.child (B)")
    @Test
    public void child_beginning() {
        Element element = new Element("div");
        element.appendChild(new Element("h"));
        element.appendChild(new Element("p"));
        element.appendChild(new Element("span"));

        assertNotNull(element.child(0));
    }

    @DisplayName("Element.child (M)")
    @Test
    public void child_middle() {
        Element element = new Element("div");
        element.appendChild(new Element("h"));
        element.appendChild(new Element("p"));
        element.appendChild(new Element("span"));

        assertNotNull(element.child(1));
    }

    @DisplayName("Element.child (E)")
    @Test
    public void child_end() {
        Element element = new Element("div");
        element.appendChild(new Element("h"));
        element.appendChild(new Element("p"));
        element.appendChild(new Element("span"));

        assertNotNull(element.child(2));
    }
}
