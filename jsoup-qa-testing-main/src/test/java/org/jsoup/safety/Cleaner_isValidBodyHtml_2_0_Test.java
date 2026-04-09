package org.jsoup.safety;

// added import statement for Jsoup class
import org.jsoup.Jsoup;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
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
import org.mockito.*;
import org.junit.jupiter.api.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class Cleaner_isValidBodyHtml_2_0_Test {

    @Test
    public void testIsValidBodyHtml() throws Exception {
    	// Create a Cleaner instance with basic configuration
        Cleaner cleaner = new Cleaner(Safelist.basic());
        Document doc1 = Jsoup.parse("<html><body><p>Hello, World!</p></body></html>");
        assertTrue(cleaner.isValidBodyHtml(doc1.body().html()));
        // treat <h1> as invalid, but <p> as valid
        Document doc2 = Jsoup.parse("<html><body><h1>Invalid</h1><p>HTML</p></body></html>");
        assertFalse(cleaner.isValidBodyHtml(doc2.body().html()));

        Safelist scriptStyleSafelist = new Safelist().addTags("script", "style", "p");
        Cleaner scriptStyleCleaner = new Cleaner(scriptStyleSafelist);
        // Test valid HTML with script and style tags
        Document doc3 = Jsoup.parse("<html><body><script>alert('hi');</script><style>body { background-color: red; }</style><p>Hello, World!</p></body></html>");
        assertTrue(scriptStyleCleaner.isValidBodyHtml(doc3.body().html()));
        // Test invalid HTML with script and style tags, h1 remains invalid
        Document doc4 = Jsoup.parse("<html><body><script>alert('hi');</script><style>body { background-color: red; }</style><h1>Invalid</h1><p>HTML</p></body></html>");
        assertFalse(scriptStyleCleaner.isValidBodyHtml(doc4.body().html()));

        // the last 2 assertions are removed as it does not intelligently test any behaviours of IsValidBodyHtml.
    }
	
}
