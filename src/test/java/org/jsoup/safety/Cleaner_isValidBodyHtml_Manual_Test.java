package org.jsoup.safety;

import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.Parser;
import org.jsoup.select.NodeVisitor;
import java.util.List;
import static org.jsoup.internal.SharedConstants.DummyUri;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class Cleaner_isValidBodyHtml_Manual_Test  {

	
    @Test
    void isValidBodyHtml_noneSafelist_plainTextIsValid() {
        Cleaner cleaner = new Cleaner(Safelist.none());

        assertTrue(cleaner.isValidBodyHtml("Hello, World!"));
    }

    @Test
    void isValidBodyHtml_noneSafelist_htmlTagIsInvalid() {
        Cleaner cleaner = new Cleaner(Safelist.none());

        assertFalse(cleaner.isValidBodyHtml("<p>Hello, World!</p>"));
    }

    @Test
    void isValidBodyHtml_basicSafelist_allowsBasicTags() {
        Cleaner cleaner = new Cleaner(Safelist.basic());

        assertTrue(cleaner.isValidBodyHtml("<p>Hello</p><b>Bold</b>"));
    }

    @Test
    void isValidBodyHtml_basicSafelist_rejectsUnsafeTag() {
        Cleaner cleaner = new Cleaner(Safelist.basic());

        assertFalse(cleaner.isValidBodyHtml("<script>alert('hi');</script>"));
    }

    @Test
    void isValidBodyHtml_preserveRelativeLinksTrue_coversDummyUriBranch() {
        Safelist safelist = Safelist.basicWithImages().preserveRelativeLinks(true);
        Cleaner cleaner = new Cleaner(safelist);

        assertTrue(cleaner.isValidBodyHtml("<a href=\"/docs/page.html\">Link</a>"));
    }

    @Test
    void isValidBodyHtml_preserveRelativeLinksFalse_coversEmptyBaseUriBranch() {
        Safelist safelist = Safelist.basicWithImages().preserveRelativeLinks(false);
        Cleaner cleaner = new Cleaner(safelist);

        assertTrue(cleaner.isValidBodyHtml("<p>Hello</p><b>Bold</b>"));
    }

    @Test
    void isValidBodyHtml_malformedButAllowedHtml_canFailBecauseOfParseErrors() {
        Cleaner cleaner = new Cleaner(Safelist.basic());

        assertFalse(cleaner.isValidBodyHtml("<a href=\"https://example.com\">test"));
    }

}
