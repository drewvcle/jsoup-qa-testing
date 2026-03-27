package org.jsoup.nodes;

import java.lang.reflect.Field;
import org.mockito.*;
import org.junit.jupiter.api.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.jsoup.helper.Validate;
import org.jsoup.internal.QuietAppendable;
import org.jsoup.internal.SharedConstants;
import org.jsoup.internal.StringUtil;
import org.jsoup.nodes.Attributes;
import org.jsoup.parser.ParseSettings;
import org.jspecify.annotations.Nullable;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;
import static org.jsoup.internal.Normalizer.lowerCase;
import static org.jsoup.internal.SharedConstants.AttrRangeKey;
import static org.jsoup.nodes.Range.AttributeRange.UntrackedAttr;

class Attributes_put_9_0_Test {

    private Attributes attributes;

    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        attributes = new Attributes();
    }

    @Test
    void testPut() throws Exception {
        Field sizeField = Attributes.class.getDeclaredField("size");
        sizeField.setAccessible(true);
        attributes.put("key1", "value1");
        assertEquals(1, (int) sizeField.get(attributes));
        assertEquals("value1", attributes.get("key1"));
        attributes.put("key2", "value2");
        assertEquals(2, (int) sizeField.get(attributes));
        assertEquals("value2", attributes.get("key2"));
        // Test replacing existing value
        attributes.put("key1", "new_value1");
        assertEquals(2, (int) sizeField.get(attributes));
        assertEquals("new_value1", attributes.get("key1"));
    }
}
