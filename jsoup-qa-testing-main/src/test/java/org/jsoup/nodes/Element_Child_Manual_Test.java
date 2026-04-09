package org.jsoup.nodes;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import org.jsoup.nodes.Element;

class Element_Child_Manual_Test {

    @Test
    void child_zeroIndexReturnsFirstChild() {
        Element element = new Element("div");
        element.appendChild(new Element("p"));
        element.appendChild(new Element("span"));
        element.appendChild(new Element("a"));

        assertEquals("p", element.child(0).tagName());
    }

    @Test
    void child_middleIndexReturnsMiddleChild() {
        Element element = new Element("div");
        element.appendChild(new Element("p"));
        element.appendChild(new Element("span"));
        element.appendChild(new Element("a"));

        assertEquals("span", element.child(1).tagName());
    }

    @Test
    void child_lastIndexReturnsLastChild() {
        Element element = new Element("div");
        element.appendChild(new Element("p"));
        element.appendChild(new Element("span"));
        element.appendChild(new Element("a"));

        assertEquals("a", element.child(2).tagName());
    }

    @Test
    void child_negativeIndexThrowsException() {
        Element element = new Element("div");
        element.appendChild(new Element("p"));

        assertThrows(org.jsoup.helper.ValidationException.class, () -> element.child(-1));
    }

    @Test
    void child_indexEqualToSizeThrowsException() {
        Element element = new Element("div");
        element.appendChild(new Element("p"));
        element.appendChild(new Element("span"));

        assertThrows(IndexOutOfBoundsException.class, () -> element.child(2));
    }

    @Test
    void child_indexGreaterThanSizeThrowsException() {
        Element element = new Element("div");
        element.appendChild(new Element("p"));

        assertThrows(IndexOutOfBoundsException.class, () -> element.child(5));
    }

    @Test
    void child_onElementWithNoChildrenThrowsException() {
        Element element = new Element("div");

        assertThrows(IndexOutOfBoundsException.class, () -> element.child(0));
    }

    @Test
    void child_singleChild_zeroIndex_returnsOnlyChild() {
        Element element = new Element("div");
        element.appendChild(new Element("p"));

        assertEquals("p", element.child(0).tagName());
    }

    @Test
    void child_ignoresTextNodesWhenSelectingElementChildren() {
        Element element = new Element("div");
        element.appendText("hello");
        element.appendChild(new Element("p"));
        element.appendText("world");
        element.appendChild(new Element("span"));

        assertEquals("p", element.child(0).tagName());
        assertEquals("span", element.child(1).tagName());
    }

    @Test
    void child_usesCachedChildrenWhenCacheAlreadyExists() {
        Element element = new Element("div");
        element.appendChild(new Element("p"));
        element.appendChild(new Element("span"));
        element.appendChild(new Element("a"));

        element.children();

        assertEquals("span", element.child(1).tagName());
    }
    
}