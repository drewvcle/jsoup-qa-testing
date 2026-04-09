package org.jsoup.ISP_Tests;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import main.java.org.jsoup.Jsoup;
import main.java.org.jsoup.safety.Safelist;


public class Cleaner_ISP_Test {

    // isValid() tests
    @DisplayName("Cleaner.isValid (Not Null, True)")
    @Test
    public void isValid_dirtyDocumentWRTNullNotNull_isDocumentDirtyTrue() {
        Cleaner cleaner = new Cleaner(Safelist.basic());
        Document dirtyDocument = Jsoup.parse("<p>Hello</p><script>zip bomb extractor 9000</script>");

        assertFalse(cleaner.isValid(dirtyDocument));
    }

    @DisplayName("Cleaner.isValid (Null, False)")
    @Test
    public void isValid_dirtyDocumentWRTNullNull_isDocumentDirtyFalse() {
        Cleaner cleaner = new Cleaner(Safelist.basic());

        assertThrows(IllegalArgumentException.class, () -> cleaner.isValid(null));
    }

    @DisplayName("Cleaner.isValid (Not Null, False)")
    @Test
    public void isValid_dirtyDocumentWRTNullNotNull_isDocumentDirtyFalse() {
        Cleaner cleaner = new Cleaner(Safelist.basic());
        Document cleanDocument = Jsoup.parse("<p>Hello</p>");

        assertTrue(cleaner.isValid(cleanDocument));
    }

    // isValidBodyHtml() tests
    @DisplayName("Cleaner.isValidBodyHtml (T, T)")
    @Test
    public void isValidBodyHtml_htmlBodyIsParseableTrue_htmlBodyIsCleanTrue() {
        Cleaner cleaner = new Cleaner(Safelist.basic());

        assertTrue(cleaner.isValidBodyHtml("<p>Hello</p>"));
    }

    @DisplayName("Cleaner.isValidBodyHtml (F, T)")
    @Test
    public void isValidBodyHtml_htmlBodyIsParseableFalse_htmlBodyIsCleanTrue() {
        Cleaner cleaner = new Cleaner(Safelist.basic());

        assertFalse(cleaner.isValidBodyHtml("<a href='https://example.com'>test"));
    }

    @DisplayName("Cleaner.isValidBodyHtml (T, F)")
    @Test
    public void isValidBodyHtml_htmlBodyIsParseableTrue_htmlBodyIsCleanFalse() {
        Cleaner cleaner = new Cleaner(Safelist.basic());

        assertFalse(cleaner.isValidBodyHtml("<script>alert(1)</script>"));
    }
}
