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

class Attributes_put_Manual_Test {

    private Attributes attributes;

    @BeforeEach
    void setUp() {
        attributes = new Attributes();
    }

    @Test
    void put_newKey_addsAttribute() {
        attributes.put("aa", "bb");
        assertEquals("bb", attributes.get("aa"));
        assertEquals(1, attributes.size());
    }

    @Test
    void put_secondNewKey_increasesCollection() {
        attributes.put("cc", "dd");
        attributes.put("ee", "ff");

        assertEquals("dd", attributes.get("cc"));
        assertEquals("ff", attributes.get("ee"));
        assertEquals(2, attributes.size());
    }

    @Test
    void put_existingKey_replacesValue() {
        attributes.put("gg", "hh");
        attributes.put("gg", "ii");

        assertEquals("ii", attributes.get("gg"));
        assertEquals(1, attributes.size());
    }

    @Test
    void put_existingKey_sameValue_keepsSingleAttribute() {
        attributes.put("jj", "kk");
        attributes.put("jj", "kk");

        assertEquals("kk", attributes.get("jj"));
        assertEquals(1, attributes.size());
    }

    @Test
    void put_emptyKey_storesValue() {
        attributes.put("", "value");

        assertEquals("value", attributes.get(""));
        assertEquals(1, attributes.size());
    }

    @Test
    void put_emptyValue_storesEmptyString() {
        attributes.put("key", "");

        assertEquals("", attributes.get("key"));
        assertEquals(1, attributes.size());
    }

    @Test
    void put_nullValue_behaviour() {
        attributes.put("key", null);

        assertEquals("", attributes.get("key"));
        assertEquals(1, attributes.size());
    }

    @Test
    void put_nullKey_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> attributes.put(null, "value"));
    }

    @Test
    void put_caseSensitiveKeys_treatedAsDifferentKeys() {
        attributes.put("href", "a");
        attributes.put("HREF", "b");

        assertEquals("a", attributes.get("href"));
        assertEquals("b", attributes.get("HREF"));
    }

    @Test
    void put_specialCharacterKey_storesValue() {
        attributes.put("data-id", "123");

        assertEquals("123", attributes.get("data-id"));
        assertEquals(1, attributes.size());
    }
}
