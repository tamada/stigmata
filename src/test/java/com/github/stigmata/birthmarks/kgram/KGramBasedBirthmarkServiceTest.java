package com.github.stigmata.birthmarks.kgram;

import static org.hamcrest.CoreMatchers.*;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class KGramBasedBirthmarkServiceTest {
    private KGramBasedBirthmarkService service;

    @Before
    public void setUp(){
        service = new KGramBasedBirthmarkService();
    }

    @Test
    public void testKValue(){
        KGramBasedBirthmarkExtractor extractor1 = (KGramBasedBirthmarkExtractor)service.getExtractor();
        Assert.assertThat(extractor1.getKValue(), is(4));

        Assert.assertThat(service.isType("6gram"), is(true));
        KGramBasedBirthmarkExtractor extractor2 = (KGramBasedBirthmarkExtractor)service.getExtractor();
        Assert.assertThat(extractor2.getKValue(), is(6));
    }
}
