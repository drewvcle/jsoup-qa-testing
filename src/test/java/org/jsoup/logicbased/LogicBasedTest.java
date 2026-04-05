package org.jsoup.logicbased;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.ParseSettings;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Safelist;
import org.jsoup.select.Elements;
import org.jsoup.select.Selector;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Logic-based Testing — Section 2.3
 * CACC / PC coverage for 10 methods across 5 Jsoup classes.
 * Author: Pavit Buttar
 */
public class LogicBasedTest {

    // METHOD 1: Element.hasClass — P1: len==0||len<wantLen, P2: tokenLen==wantLen&&regionMatches, P3: inClass&&lastLen==wantLen
    @Nested @DisplayName("Method 1: Element.hasClass")
    class HasClassTest {
        @Test void p1_true_emptyClassAttr() {
            Element el = new Element("div"); el.attr("class", "");
            assertFalse(el.hasClass("test"));
        }
        @Test void p1_true_shorterClassAttr() {
            Element el = new Element("div"); el.attr("class", "ab");
            assertFalse(el.hasClass("abcdef"));
        }
        @Test void p1_false_proceedToScan() {
            Element el = new Element("div"); el.attr("class", "alpha beta");
            assertTrue(el.hasClass("alpha"));
        }
        @Test void p2_true_matchInMiddle() {
            Element el = new Element("div"); el.attr("class", "one two three");
            assertTrue(el.hasClass("two"));
        }
        @Test void p2_false_sameLenDifferentContent() {
            Element el = new Element("div"); el.attr("class", "abc def");
            assertFalse(el.hasClass("xyz"));
        }
        @Test void p3_true_lastTokenMatches() {
            Element el = new Element("div"); el.attr("class", "first last");
            assertTrue(el.hasClass("last"));
        }
        @Test void p3_false_lastTokenWrongLen() {
            Element el = new Element("div"); el.attr("class", "first toolong");
            assertFalse(el.hasClass("tool"));
        }
        @Test void noAttributes_returnsFalse() {
            assertFalse(new Element("div").hasClass("any"));
        }
        @Test void exactLenMatch_caseInsensitive() {
            Element el = new Element("div"); el.attr("class", "Hello");
            assertTrue(el.hasClass("hello"));
        }
    }

    // METHOD 2: Element.child — P1: cached!=null, P2: node instanceof Element, P3: e++==index
    @Nested @DisplayName("Method 2: Element.child")
    class ChildTest {
        @Test void validIndex0() {
            Document doc = Jsoup.parse("<div><p>one</p><p>two</p></div>");
            assertEquals("p", doc.selectFirst("div").child(0).tagName());
        }
        @Test void validIndex1() {
            Document doc = Jsoup.parse("<div><p>one</p><p>two</p></div>");
            assertEquals("two", doc.selectFirst("div").child(1).text());
        }
        @Test void textNodesSkipped() {
            Document doc = Jsoup.parse("<div>text<span>child</span></div>");
            assertEquals("span", doc.selectFirst("div").child(0).tagName());
        }
        @Test void indexOutOfBounds() {
            Document doc = Jsoup.parse("<div><p>only</p></div>");
            assertThrows(IndexOutOfBoundsException.class, () -> doc.selectFirst("div").child(5));
        }
        @Test void negativeIndex() {
            Document doc = Jsoup.parse("<div><p>c</p></div>");
            assertThrows(IllegalArgumentException.class, () -> doc.selectFirst("div").child(-1));
        }
    }

    // METHOD 3: Attributes.put — P1: i!=NotFound (T→update, F→add)
    @Nested @DisplayName("Method 3: Attributes.put")
    class PutTest {
        @Test void p1_false_newKeyAdded() {
            Attributes a = new Attributes(); a.put("id", "main");
            assertEquals("main", a.get("id"));
        }
        @Test void p1_true_existingKeyUpdated() {
            Attributes a = new Attributes(); a.put("id", "old"); a.put("id", "new");
            assertEquals("new", a.get("id"));
        }
        @Test void nullValue_booleanAttr() {
            Attributes a = new Attributes(); a.put("disabled", null);
            assertTrue(a.hasKey("disabled"));
        }
        @Test void nullKey_throws() {
            assertThrows(IllegalArgumentException.class, () -> new Attributes().put(null, "v"));
        }
    }

    // METHOD 4: Attributes.deduplicate — P1: (preserve&&equals)||(!preserve&&equalsIgnoreCase)
    @Nested @DisplayName("Method 4: Attributes.deduplicate")
    class DeduplicateTest {
        @Test void cacc_a_true_b_true() {
            Attributes a = new Attributes(); a.add("id","1"); a.add("id","2");
            assertEquals(1, a.deduplicate(ParseSettings.preserveCase));
        }
        @Test void cacc_a_true_b_false() {
            Attributes a = new Attributes(); a.add("ID","1"); a.add("id","2");
            assertEquals(0, a.deduplicate(ParseSettings.preserveCase));
        }
        @Test void cacc_a_false_d_true() {
            Attributes a = new Attributes(); a.add("Class","a"); a.add("class","b");
            assertEquals(1, a.deduplicate(ParseSettings.htmlDefault));
        }
        @Test void cacc_a_false_d_false() {
            Attributes a = new Attributes(); a.add("id","1"); a.add("class","2");
            assertEquals(0, a.deduplicate(ParseSettings.htmlDefault));
        }
        @Test void emptyAttributes() {
            assertEquals(0, new Attributes().deduplicate(ParseSettings.htmlDefault));
        }
    }

    // METHOD 5: Cleaner.isValid — P1: numDiscarded==0 && head.isEmpty (a&&b)
    @Nested @DisplayName("Method 5: Cleaner.isValid")
    class IsValidTest {
        @Test void cacc_TT_valid() {
            assertTrue(new Cleaner(Safelist.basic()).isValid(Jsoup.parse("<p>Hello</p>")));
        }
        @Test void cacc_FT_unsafeBody() {
            assertFalse(new Cleaner(Safelist.basic()).isValid(Jsoup.parse("<script>x</script>")));
        }
        @Test void cacc_TF_contentInHead() {
            assertFalse(new Cleaner(Safelist.basic()).isValid(
                Jsoup.parse("<html><head><style>x</style></head><body><p>Hi</p></body></html>")));
        }
    }

    // METHOD 6: Cleaner.isValidBodyHtml — P1: numDiscarded==0 && errorList.isEmpty (a&&b)
    @Nested @DisplayName("Method 6: Cleaner.isValidBodyHtml")
    class IsValidBodyHtmlTest {
        @Test void cacc_TT_validHtml() {
            assertTrue(new Cleaner(Safelist.basic()).isValidBodyHtml("<p>Hello</p>"));
        }
        @Test void cacc_FT_unsafeTag() {
            assertFalse(new Cleaner(Safelist.basic()).isValidBodyHtml("<script>x</script>"));
        }
        @Test void cacc_TF_malformed() {
            assertFalse(new Cleaner(Safelist.relaxed()).isValidBodyHtml("<p>text</invalidtag>"));
        }
    }

    // METHOD 7: Safelist.isSafeAttribute — P1: okSet!=null&&contains, P2: !containsKey||isSafeProtocol, P3: enforced, P4: fallback to :all
    @Nested @DisplayName("Method 7: Safelist.isSafeAttribute")
    class IsSafeAttributeTest {
        private Element el(String tag, String key, String val) {
            return Jsoup.parse("<"+tag+" "+key+"=\""+val+"\"></"+tag+">").selectFirst(tag);
        }
        @Test void p1TT_p2FT_validProtocol() {
            Element e = el("a","href","http://x.com");
            assertTrue(Safelist.basic().isSafeAttribute("a", e, e.attribute("href")));
        }
        @Test void p1TT_noProtocol() {
            Element e = el("a","title","t");
            assertTrue(Safelist.relaxed().isSafeAttribute("a", e, e.attribute("title")));
        }
        @Test void p1TF_attrNotAllowed() {
            Element e = el("a","onclick","x");
            assertFalse(Safelist.basic().isSafeAttribute("a", e, e.attribute("onclick")));
        }
        @Test void p2FF_invalidProtocol() {
            Element e = el("a","href","javascript:alert()");
            assertFalse(Safelist.basic().isSafeAttribute("a", e, e.attribute("href")));
        }
        @Test void p4TT_fallbackToAll() {
            Element e = el("p","data-x","v");
            assertTrue(Safelist.basic().addAttributes(":all","data-x").isSafeAttribute("p", e, e.attribute("data-x")));
        }
        @Test void p4TF_allAlsoDisallows() {
            Element e = el("p","onclick","x");
            assertFalse(Safelist.basic().isSafeAttribute("p", e, e.attribute("onclick")));
        }
    }

    // METHOD 8: Safelist.removeProtocols — P1: attrProtocols.isEmpty, P2: tagProtocols.isEmpty
    @Nested @DisplayName("Method 8: Safelist.removeProtocols")
    class RemoveProtocolsTest {
        @Test void p1F_p2F_removeOne() {
            Safelist sl = Safelist.basic(); sl.removeProtocols("a","href","ftp");
            Element e = Jsoup.parse("<a href=\"http://x\">x</a>").selectFirst("a");
            assertTrue(sl.isSafeAttribute("a", e, e.attribute("href")));
        }
        @Test void p1T_p2T_removeAll() {
            Safelist sl = new Safelist().addTags("a").addAttributes("a","href").addProtocols("a","href","http");
            sl.removeProtocols("a","href","http");
            Element e = Jsoup.parse("<a href=\"http://x\">x</a>").selectFirst("a");
            assertTrue(sl.isSafeAttribute("a", e, e.attribute("href")));
        }
        @Test void validation_throwsIfNone() {
            assertThrows(IllegalArgumentException.class, () ->
                Safelist.none().addTags("div").addAttributes("div","id").removeProtocols("div","id","http"));
        }
    }

    // METHOD 9: Selector.select — P1: valid query, P2: dedup in Iterable overload
    @Nested @DisplayName("Method 9: Selector.select")
    class SelectorSelectTest {
        @Test void multipleMatches() {
            assertEquals(3, Selector.select("p", Jsoup.parse("<div><p>1</p><p>2</p><p>3</p></div>")).size());
        }
        @Test void noMatches() {
            assertTrue(Selector.select("span", Jsoup.parse("<div><p>t</p></div>")).isEmpty());
        }
        @Test void invalidQuery_throws() {
            assertThrows(Selector.SelectorParseException.class, () -> Selector.select("[", Jsoup.parse("<div></div>")));
        }
        @Test void iterableDedup() {
            Document doc = Jsoup.parse("<div><p>t</p></div>");
            assertEquals(1, Selector.select("p", Arrays.asList(doc.body(), doc.body())).size());
        }
    }

    // METHOD 10: Selector.selectFirst — P1 (Iterable): first!=null
    @Nested @DisplayName("Method 10: Selector.selectFirst")
    class SelectorSelectFirstTest {
        @Test void matchFound() {
            Element f = Selector.selectFirst("p", Jsoup.parse("<div><p>1</p><p>2</p></div>"));
            assertNotNull(f); assertEquals("1", f.text());
        }
        @Test void noMatch_null() {
            assertNull(Selector.selectFirst("span", Jsoup.parse("<div><p>t</p></div>")));
        }
        @Test void iterableFirstRootMatch() {
            Document doc = Jsoup.parse("<div><p>found</p></div><section><span>x</span></section>");
            Element f = Selector.selectFirst("p", Arrays.asList(doc.selectFirst("div"), doc.selectFirst("section")));
            assertNotNull(f); assertEquals("found", f.text());
        }
        @Test void iterableNoMatch_null() {
            Document doc = Jsoup.parse("<div><span>a</span></div><section><span>b</span></section>");
            assertNull(Selector.selectFirst("p", Arrays.asList(doc.selectFirst("div"), doc.selectFirst("section"))));
        }
    }
}
