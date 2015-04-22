package com.github.stigmata.birthmarks.uc;

import java.io.FileInputStream;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.github.stigmata.Birthmark;
import com.github.stigmata.BirthmarkElement;
import com.github.stigmata.BirthmarkEnvironment;
import com.github.stigmata.BirthmarkExtractor;
import com.github.stigmata.birthmarks.uc.UsedClassesBirthmarkService;
import com.github.stigmata.utils.WellknownClassJudgeRule;
import com.github.stigmata.utils.WellknownClassManager;
import com.github.stigmata.utils.WellknownClassJudgeRule.MatchPartType;
import com.github.stigmata.utils.WellknownClassJudgeRule.MatchType;

/**
 *
 * @author Haruaki TAMADA
 */
public class UsedClassesBirthmarkExtractorTest{
    private BirthmarkExtractor extractor; 

    @Before
    public void setup(){
        extractor = new UsedClassesBirthmarkService().getExtractor();
        BirthmarkEnvironment env = BirthmarkEnvironment.getDefaultEnvironment();
        WellknownClassManager manager = env.getWellknownClassManager();
        manager.add(new WellknownClassJudgeRule("java.", MatchType.PREFIX, MatchPartType.FULLY_NAME));
        manager.add(new WellknownClassJudgeRule("javax.", MatchType.PREFIX, MatchPartType.FULLY_NAME));
    }

    @Test
    public void checkBirthmark() throws Exception{
        Birthmark birthmark = extractor.extract(new FileInputStream("target/test-classes/resources/HelloWorldFrame.class"));

        Assert.assertEquals("uc", birthmark.getType());
        Assert.assertEquals(7, birthmark.getElementCount());

        BirthmarkElement[] elements = birthmark.getElements();
        Assert.assertEquals("java.awt.Component", elements[0].getValue());
        Assert.assertEquals("java.awt.Container", elements[1].getValue());
        Assert.assertEquals("java.awt.Font", elements[2].getValue());
        Assert.assertEquals("java.lang.Object", elements[3].getValue());
        Assert.assertEquals("java.lang.String", elements[4].getValue());
        Assert.assertEquals("javax.swing.JFrame", elements[5].getValue());
        Assert.assertEquals("javax.swing.JLabel", elements[6].getValue());
    }
}
