package org.apache.streams.urls;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.apache.streams.core.StreamsDatum;
import org.apache.streams.jackson.StreamsJacksonModule;
import org.apache.streams.pojo.json.Activity;
import org.apache.streams.pojo.json.Link;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.List;

/**
 * Created by rebanks on 2/27/14.
 */
public class TestLinkResolverProcessor {

    @Test
    public void testActivityLinkUnwinderProcessorBitly() throws Exception {
        testActivityUnwinderHelper(
                prepareOriginalUrlLinks(Lists.newArrayList("http://bit.ly/1cX5Rh4")),
                prepareFinalUrlLinks(Lists.newArrayList("http://www.wcgworld.com/")));
    }

    @Test
    public void testActivityLinkUnwinderProcessorGoogle() throws Exception {
        testActivityUnwinderHelper(
                prepareOriginalUrlLinks(Lists.newArrayList("http://goo.gl/wSrHDA")),
                prepareFinalUrlLinks(Lists.newArrayList("http://www.wcgworld.com/")));
    }

    @Test
    public void testActivityLinkUnwinderProcessorOwly() throws Exception {
        testActivityUnwinderHelper(
                prepareOriginalUrlLinks(Lists.newArrayList("http://ow.ly/u4Kte")),
                prepareFinalUrlLinks(Lists.newArrayList("http://www.wcgworld.com/")));
    }

    @Test
    public void testActivityLinkUnwinderProcessorGoDaddy() throws Exception {
        testActivityUnwinderHelper(
                prepareOriginalUrlLinks(Lists.newArrayList("http://x.co/3yapt")),
                prepareFinalUrlLinks(Lists.newArrayList("http://www.wcgworld.com/")));
    }

    @Test
    public void testActivityLinkUnwinderProcessorMulti() throws Exception {
        testActivityUnwinderHelper(
                prepareOriginalUrlLinks(Lists.newArrayList("http://x.co/3yapt", "http://ow.ly/u4Kte", "http://goo.gl/wSrHDA")),
                prepareFinalUrlLinks(Lists.newArrayList("http://www.wcgworld.com/", "http://www.wcgworld.com/", "http://www.wcgworld.com/")));
    }

    @Test
    public void testActivityLinkUnwinderProcessorUnwindable() throws Exception {
        testActivityUnwinderHelper(
                prepareOriginalUrlLinks(Lists.newArrayList("http://bit.ly/1cX5Rh4", "http://nope@#$%")),
                prepareFinalUrlLinks(Lists.newArrayList("http://www.wcgworld.com/")));
    }

    public void testActivityUnwinderHelper(List<Link> input, List<Link> expected) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.registerModule(new StreamsJacksonModule());
        Activity activity = new Activity();
        activity.setLinks(input);
        StreamsDatum datum = new StreamsDatum(activity);
        LinkResolverProcessor processor = new LinkResolverProcessor();
        processor.prepare(null);
        List<StreamsDatum> result = processor.process(datum);
        assertNotNull(result);
        assertEquals(1, result.size());
        StreamsDatum resultDatum = result.get(0);
        assertNotNull(resultDatum);
        assertTrue(resultDatum.getDocument() instanceof Activity);
        Activity resultActivity = (Activity) resultDatum.getDocument();
        assertNotNull(resultActivity.getLinks());
        List<Link> resultLinks = resultActivity.getLinks();
        assertEquals(expected.size(), resultLinks.size());
        for( Link link : resultLinks )
            assertNotNull(link.getAdditionalProperties().get("finalURL"));
    }

    public List<Link> prepareOriginalUrlLinks(List<String> originalUrls) {
        List<Link> links = Lists.newArrayList();
        for( String originalUrl : originalUrls ) {
            Link in = (new Link());
            in.setAdditionalProperty("originalURL", originalUrl);
            links.add(in);
        }
        return links;
    }

    public List<Link> prepareFinalUrlLinks(List<String> finalUrls) {
        List<Link> links = Lists.newArrayList();
        for( String finalUrl : finalUrls ) {
            Link out = (new Link());
            out.setAdditionalProperty("finalURL", finalUrl);
            links.add(out);
        }
        return links;
    }

}
