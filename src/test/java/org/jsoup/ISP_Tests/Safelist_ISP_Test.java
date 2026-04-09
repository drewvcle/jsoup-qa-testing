package org.jsoup.ISP_Tests;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import main.java.org.jsoup.safety.Safelist;


public class Safelist_ISP_Test {

    // removeProtocols() tests
    @DisplayName("Safelist.removeProtocols (T, T, T, Once)")
    @Test
    public void removeProtocols_validTagTrue_validAttributeTrue_validProtocolTrue_protocolInRuleOnce() {
        Safelist safelist = new Safelist().addTags("a").addAttributes("a", "href").addProtocols("a", "href", "https");
        Element element = new Element("a").attr("href", "javascript:alert(1)");
        safelist.removeProtocols("a", "href", "https");

        assertTrue(safelist.isSafeAttribute("a", element, new Attribute("href", "javascript:alert(1)")));
    }

    @DisplayName("Safelist.removeProtocols (F, T, T, Once)")
    @Test
    public void removeProtocols_validTagFalse_validAttributeTrue_validProtocolTrue_protocolInRuleOnce() {
        Safelist safelist = new Safelist().addTags("a").addAttributes("a", "href").addProtocols("a", "href", "https");
        assertThrows(IllegalArgumentException.class, () -> safelist.removeProtocols("img", "href", "https"));
    }

    @DisplayName("Safelist.removeProtocols (T, F, T, Once)")
    @Test
    public void removeProtocols_validTagTrue_validAttributeFalse_validProtocolTrue_protocolInRuleOnce() {
        Safelist safelist = new Safelist().addTags("a").addAttributes("a", "href").addProtocols("a", "href", "https");
        assertThrows(IllegalArgumentException.class, () -> safelist.removeProtocols("a", "src", "https"));
    }

    @DisplayName("Safelist.removeProtocols (T, T, F, Once)")
    @Test
    public void removeProtocols_validTagTrue_validAttributeTrue_validProtocolFalse_protocolInRuleOnce() {
        Safelist safelist = new Safelist().addTags("a").addAttributes("a", "href").addProtocols("a", "href", "https");
        assertThrows(IllegalArgumentException.class, () -> safelist.removeProtocols("a", "href", ""));
    }

    @DisplayName("Safelist.removeProtocols (T, T, T, None)")
    @Test
    public void removeProtocols_validTagTrue_validAttributeTrue_validProtocolTrue_protocolInRuleNone() {
        Safelist safelist = new Safelist().addTags("a").addAttributes("a", "href").addProtocols("a", "href", "http");
        Element element = new Element("a").attr("href", "https://example.com");
        safelist.removeProtocols("a", "href", "ftp");

        assertFalse(safelist.isSafeAttribute("a", element, new Attribute("href", "https://example.com")));
    }

    @DisplayName("Safelist.removeProtocols (T, T, T, Multiple)")
    @Test
    public void removeProtocols_validTagTrue_validAttributeTrue_validProtocolTrue_protocolInRuleMultiple() {
        Safelist safelist = new Safelist().addTags("a").addAttributes("a", "href").addProtocols("a", "href", "http", "https");
        Element element = new Element("a").attr("href", "https://example.com");
        safelist.removeProtocols("a", "href", "https");

        assertFalse(safelist.isSafeAttribute("a", element, new Attribute("href", "https://example.com")));
    }

    // isSafeAttribute() tests
    @DisplayName("Safelist.isSafeAttribute (T, F, T)")
    @Test
    public void isSafeAttribute_attributeEnabledForTagTrue_enforcementUsedFalse_elementInProtocolTrue() {
        Safelist safelist = new Safelist().addTags("a").addAttributes("a", "href").addProtocols("a", "href", "https");
        Element element = new Element("a").attr("href", "https://example.com");

        assertTrue(safelist.isSafeAttribute("a", element, new Attribute("href", "https://example.com")));
    }

    @DisplayName("Safelist.isSafeAttribute (F, F, T)")
    @Test
    public void isSafeAttribute_attributeEnabledForTagFalse_enforcementUsedFalse_elementInProtocolTrue() {
        Safelist safelist = new Safelist().addTags("a").addAttributes("a", "href").addProtocols("a", "href", "https");
        Element element = new Element("a").attr("src", "https://example.com");

        assertFalse(safelist.isSafeAttribute("a", element, new Attribute("src", "https://example.com")));
    }

    @DisplayName("Safelist.isSafeAttribute (T, T, T)")
    @Test
    public void isSafeAttribute_attributeEnabledForTagTrue_enforcementUsedTrue_elementInProtocolTrue() {
        Safelist safelist = new Safelist().addTags("a").addAttributes("a", "rel").addEnforcedAttribute("a", "rel", "nofollow");
        Element element = new Element("a").attr("rel", "nofollow");

        assertTrue(safelist.isSafeAttribute("a", element, new Attribute("rel", "nofollow")));
    }

    @DisplayName("Safelist.isSafeAttribute (T, F, F)")
    @Test
    public void isSafeAttribute_attributeEnabledForTagTrue_enforcementUsedFalse_elementInProtocolFalse() {
        Safelist safelist = new Safelist().addTags("a").addAttributes("a", "href").addProtocols("a", "href", "https");
        Element element = new Element("a").attr("href", "/docs/page.html");

        assertFalse(safelist.isSafeAttribute("a", element, new Attribute("href", "/docs/page.html")));
    }
}
