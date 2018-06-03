package cn.azure.chatbot.preprocessingservice;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.Assert.*;

public class SegmenterTest {
    private Segmenter segmenter;

    @Before
    public void setUp() throws Exception {
        segmenter = new Segmenter();
        // Replace "abc" and "opq" with "def"
        segmenter.buildSynonyms(Stream.of(
                "def=>abc,opq",
                "# This is a comment",
                ""
        ));
        // Remove stop word "xyz"
        segmenter.buildStopwords(Stream.of(
                "xyz"
        ));
    }

    @Test
    public void testSynonyms() {
        List<String> ret = segmenter.cut("AbC def XYZ");
        assertEquals(Arrays.asList("def", "def"), ret);
    }

    @Test
    public void testMixedLanguages() {
        List<String> ret = segmenter.cut("你好世界hello café");
        assertEquals(Arrays.asList("你好", "世界", "hello", "cafe"), ret);
    }

    @Test
    public void testCut() {
        List<String> ret = segmenter.cut("我来到北京清华大学");
        assertEquals(Arrays.asList("我", "来到", "北京", "清华大学"), ret);
    }

    @Test
    public void testCutAll() {
        List<String> ret = segmenter.cutAll("我来到北京清华大学");
        assertEquals(Arrays.asList("我", "来到", "北京", "清华", "华大", "大学", "清华大学"), ret);
    }

    @Test
    public void testGetSynonyms() {
        assertEquals(Arrays.asList("abc", "opq"), segmenter.getSynonyms("def"));
    }
}