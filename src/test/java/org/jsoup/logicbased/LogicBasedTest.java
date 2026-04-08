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
 * Author: Pavit Buttar
 */
public class LogicBasedTest {

 
    @Nested @DisplayName("Method 1: Element.hasClass")
    class HasClassTest {
        @Test void returnsFalseForEmptyClassString() {
            Element el = new Element("div"); el.attr("class", "");
            assertFalse(el.hasClass("test"));
        }
        @Test void returnsFalseWhenAttributeIsTooShort() {
            Element el = new Element("div"); el.attr("class", "ab");
            assertFalse(el.hasClass("abcdef"));
        }
        @Test void verifyBasicScanningLogic() {
            Element el = new Element("div"); el.attr("class", "alpha beta");
            assertTrue(el.hasClass("alpha"));
        }
        @Test void matchFoundInMiddleOfList() {
            Element el = new Element("div"); el.attr("class", "one two three");
            assertTrue(el.hasClass("two"));
        }
        @Test void contentMismatchWithSameLength() {
            Element el = new Element("div"); el.attr("class", "abc def");
            assertFalse(el.hasClass("xyz"));
        }
        @Test void matchFoundInLastPosition() {
            Element el = new Element("div"); el.attr("class", "first last");
            assertTrue(el.hasClass("last"));
        }
        @Test void lastTokenLengthIsIncorrect() {
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
        @Test void skipOverNonElementNodes() {
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


    @Nested @DisplayName("Method 3: Attributes.put")
    class PutTest {
        @Test void insertNewAttributeKey() {
            Attributes a = new Attributes(); a.put("id", "main");
            assertEquals("main", a.get("id"));
        }
        @Test void updateExistingAttributeKey() {
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


    @Nested @DisplayName("Method 4: Attributes.deduplicate")
    class DeduplicateTest {
        @Test void deduplicateWhenCaseSensitive() {
            Attributes a = new Attributes(); a.add("id","1"); a.add("id","2");
            assertEquals(1, a.deduplicate(ParseSettings.preserveCase));
        }
        @Test void noDeduplicationWhenCaseDiffersInSensitiveMode() {
            Attributes a = new Attributes(); a.add("ID","1"); a.add("id","2");
            assertEquals(0, a.deduplicate(ParseSettings.preserveCase));
        }
        @Test void deduplicateWhenCaseInsensitive() {
            Attributes a = new Attributes(); a.add("Class","a"); a.add("class","b");
            assertEquals(1, a.deduplicate(ParseSettings.htmlDefault));
        }
        @Test void noDeduplicationWhenKeysAreUnique() {
            Attributes a = new Attributes(); a.add("id","1"); a.add("class","2");
            assertEquals(0, a.deduplicate(ParseSettings.htmlDefault));
        }
        @Test void emptyAttributes() {
            assertEquals(0, new Attributes().deduplicate(ParseSettings.htmlDefault));
        }
    }


    @Nested @DisplayName("Method 5: Cleaner.isValid")
    class IsValidTest {
        @Test void validCleanBodyReturnsTrue() {
            assertTrue(new Cleaner(Safelist.basic()).isValid(Jsoup.parse("<p>Hello</p>")));
        }
        @Test void bodyWithUnsafeTagsReturnsFalse() {
            assertFalse(new Cleaner(Safelist.basic()).isValid(Jsoup.parse("<script>x</script>")));
        }
        @Test void contentFoundInHeadReturnsFalse() {
            assertFalse(new Cleaner(Safelist.basic()).isValid(
                Jsoup.parse("<html><head><style>x</style></head><body><p>Hi</p></body></html>")));
        }
    }

   
    @Nested @DisplayName("Method 6: Cleaner.isValidBodyHtml")
    class IsValidBodyHtmlTest {
        @Test void validSnippetReturnsTrue() {
            assertTrue(new Cleaner(Safelist.basic()).isValidBodyHtml("<p>Hello</p>"));
        }
        @Test void unsafeSnippetReturnsFalse() {
            assertFalse(new Cleaner(Safelist.basic()).isValidBodyHtml("<script>x</script>"));
        }
        @Test void malformedSnippetReturnsFalse() {
            assertFalse(new Cleaner(Safelist.relaxed()).isValidBodyHtml("<p>text</invalidtag>"));
        }
    }


    @Nested @DisplayName("Method 7: Safelist.isSafeAttribute")
    class IsSafeAttributeTest {
        private Element el(String tag, String key, String val) {
            return Jsoup.parse("<"+tag+" "+key+"=\""+val+"\"></"+tag+">").selectFirst(tag);
        }
        @Test void allowsSecureHttpProtocol() {
            Element e = el("a","href","http://x.com");
            assertTrue(Safelist.basic().isSafeAttribute("a", e, e.attribute("href")));
        }
        @Test void allowsAttributeWithoutProtocolConstraints() {
            Element e = el("a","title","t");
            assertTrue(Safelist.relaxed().isSafeAttribute("a", e, e.attribute("title")));
        }
        @Test void rejectsDisallowedAttributeKey() {
            Element e = el("a","onclick","x");
            assertFalse(Safelist.basic().isSafeAttribute("a", e, e.attribute("onclick")));
        }
        @Test void rejectsInsecureJavascriptProtocol() {
            Element e = el("a","href","javascript:alert()");
            assertFalse(Safelist.basic().isSafeAttribute("a", e, e.attribute("href")));
        }
        @Test void verifyFallbackToGlobalAllRules() {
            Element e = el("p","data-x","v");
            assertTrue(Safelist.basic().addAttributes(":all","data-x").isSafeAttribute("p", e, e.attribute("data-x")));
        }
        @Test void globalRulesStillBlockUnsafeAttributes() {
            Element e = el("p","onclick","x");
            assertFalse(Safelist.basic().isSafeAttribute("p", e, e.attribute("onclick")));
        }
    }

 
    @Nested @DisplayName("Method 8: Safelist.removeProtocols")
    class RemoveProtocolsTest {
        @Test void testRemovingOneOfManyProtocols() {
            Safelist sl = Safelist.basic(); sl.removeProtocols("a","href","ftp");
            Element e = Jsoup.parse("<a href=\"http://x\">x</a>").selectFirst("a");
            assertTrue(sl.isSafeAttribute("a", e, e.attribute("href")));
        }
        @Test void testRemovingAllProtocolsForAttribute() {
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
        @Test void testDeduplicationForMultipleRoots() {
            Document doc = Jsoup.parse("<div><p>t</p></div>");
            assertEquals(1, Selector.select("p", Arrays.asList(doc.body(), doc.body())).size());
        }
    }


    @Nested @DisplayName("Method 10: Selector.selectFirst")
    class SelectorSelectFirstTest {
        @Test void matchFound() {
            Element f = Selector.selectFirst("p", Jsoup.parse("<div><p>1</p><p>2</p></div>"));
            assertNotNull(f); assertEquals("1", f.text());
        }
        @Test void noMatch_null() {
            assertNull(Selector.selectFirst("span", Jsoup.parse("<div><p>t</p></div>")));
        }
        @Test void findFirstMatchAcrossRootList(){
            Document doc = Jsoup.parse("<div><p>found</p></div><section><span>x</span></section>");
            Element f = Selector.selectFirst("p", Arrays.asList(doc.selectFirst("div"), doc.selectFirst("section")));
            assertNotNull(f); assertEquals("found", f.text());
        }
        @Test void returnNullIfNoRootsContainMatch() {
            Document doc = Jsoup.parse("<div><span>a</span></div><section><span>b</span></section>");
            assertNull(Selector.selectFirst("p", Arrays.asList(doc.selectFirst("div"), doc.selectFirst("section"))));
        }
    }
}
