package com.github.stigmata;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.github.stigmata.birthmarks.cvfv.ConstantValueOfFieldVariableBirthmarkService;
import com.github.stigmata.birthmarks.fmc.FrequencyMethodCallBirthmarkService;
import com.github.stigmata.birthmarks.fuc.FrequencyUsedClassesBirthmarkService;
import com.github.stigmata.birthmarks.is.InheritanceStructureBirthmarkService;
import com.github.stigmata.birthmarks.kgram.KGramBasedBirthmarkService;
import com.github.stigmata.birthmarks.smc.SequentialMethodCallBirthmarkService;
import com.github.stigmata.birthmarks.uc.UsedClassesBirthmarkService;
import com.github.stigmata.birthmarks.wsp.StackPatternBasedBirthmarkService;

public class StigmataEnvironmentTest {
    private BirthmarkEnvironment environment;

    @Before
    public void setUp(){
        environment = BirthmarkEnvironment.getDefaultEnvironment();
    }

    @Test
    public void testBirthmarkService(){
        Assert.assertThat(environment.getService("cvfv"),  is(instanceOf(ConstantValueOfFieldVariableBirthmarkService.class)));
        Assert.assertThat(environment.getService("fmc"),   is(instanceOf(FrequencyMethodCallBirthmarkService.class)));
        Assert.assertThat(environment.getService("fuc"),   is(instanceOf(FrequencyUsedClassesBirthmarkService.class)));
        Assert.assertThat(environment.getService("is"),    is(instanceOf(InheritanceStructureBirthmarkService.class)));
        Assert.assertThat(environment.getService("kgram"), is(instanceOf(KGramBasedBirthmarkService.class)));
        Assert.assertThat(environment.getService("smc"),   is(instanceOf(SequentialMethodCallBirthmarkService.class)));
        Assert.assertThat(environment.getService("uc"),    is(instanceOf(UsedClassesBirthmarkService.class)));
        Assert.assertThat(environment.getService("wsp"),   is(instanceOf(StackPatternBasedBirthmarkService.class)));
    }

    @Test
    public void testKGramBirthmarkService(){
        Assert.assertThat(environment.getService("ngram"), is(instanceOf(KGramBasedBirthmarkService.class)));
        Assert.assertThat(environment.getService("3gram"), is(instanceOf(KGramBasedBirthmarkService.class)));
        Assert.assertThat(environment.getService("2gram"), is(instanceOf(KGramBasedBirthmarkService.class)));
        Assert.assertThat(environment.getService("1gram"), is(instanceOf(KGramBasedBirthmarkService.class)));

        Assert.assertThat(environment.getService("-1gram"), is(nullValue()));
        Assert.assertThat(environment.getService("Ugram"), is(nullValue()));
    }
}
