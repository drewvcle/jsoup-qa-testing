package org.jsoup.nodes;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.mockito.*;
import org.junit.jupiter.api.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.jsoup.helper.Validate;
import org.jsoup.internal.Normalizer;
import org.jsoup.internal.QuietAppendable;
import org.jsoup.helper.Regex;
import org.jsoup.internal.StringUtil;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.Parser;
import org.jsoup.parser.Tag;
import org.jsoup.parser.TokenQueue;
import org.jsoup.select.Collector;
import org.jsoup.select.Elements;
import org.jsoup.select.Evaluator;
import org.jsoup.select.NodeFilter;
import org.jsoup.select.NodeVisitor;
import org.jsoup.select.Nodes;
import org.jsoup.select.Selector;
import org.jspecify.annotations.Nullable;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import static org.jsoup.internal.Normalizer.normalize;
import static org.jsoup.nodes.Document.OutputSettings.Syntax.xml;
import static org.jsoup.nodes.TextNode.lastCharIsWhitespace;
import static org.jsoup.parser.Parser.NamespaceHtml;
import static org.jsoup.parser.TokenQueue.escapeCssIdentifier;
import static org.jsoup.select.Selector.evaluatorOf;

public class Element_id_18_0_Test {

    @Test
    void testId() throws NoSuchFieldException, IllegalAccessException {
        // Create an instance of the Element class with the id method
        Element element = new Element("div");
        // Set the attributes field using reflection to avoid null pointer exception
        Attributes attributes = new Attributes();
        Field attributesField = Element.class.getDeclaredField("attributes");
        attributesField.setAccessible(true);
        attributesField.set(element, attributes);
        // Call the id method and check if the attribute is set correctly
        String testId = "test";
        Element result = element.id(testId);
        assertEquals(testId, result.attributes().get("id"));
    }
}
