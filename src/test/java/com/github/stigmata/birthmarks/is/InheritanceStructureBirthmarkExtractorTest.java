package com.github.stigmata.birthmarks.is;

import java.io.FileInputStream;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.github.stigmata.Birthmark;
import com.github.stigmata.BirthmarkElement;
import com.github.stigmata.BirthmarkEnvironment;
import com.github.stigmata.BirthmarkExtractor;
import com.github.stigmata.birthmarks.NullBirthmarkElement;
import com.github.stigmata.birthmarks.is.InheritanceStructureBirthmarkService;
import com.github.stigmata.utils.WellknownClassJudgeRule;
import com.github.stigmata.utils.WellknownClassManager;
import com.github.stigmata.utils.WellknownClassJudgeRule.MatchPartType;
import com.github.stigmata.utils.WellknownClassJudgeRule.MatchType;

/**
 *
 * @author Haruaki TAMADA
 */
public class InheritanceStructureBirthmarkExtractorTest{
    private BirthmarkExtractor extractor; 

    @Before
    public void setup(){
        extractor = new InheritanceStructureBirthmarkService().getExtractor();
        BirthmarkEnvironment env = BirthmarkEnvironment.getDefaultEnvironment();
        WellknownClassManager manager = env.getWellknownClassManager();
        manager.add(new WellknownClassJudgeRule("java.", MatchType.PREFIX, MatchPartType.FULLY_NAME));
        manager.add(new WellknownClassJudgeRule("javax.", MatchType.PREFIX, MatchPartType.FULLY_NAME));
    }

    @Test
    public void checkBirthmark() throws Exception{
        Birthmark birthmark = extractor.extract(new FileInputStream("target/test-classes/resources/HelloWorldFrame.class"));
        Assert.assertEquals("is", birthmark.getType());

        BirthmarkElement[] elements = birthmark.getElements();
        Assert.assertEquals(2, elements.length);

        Assert.assertTrue(elements[0] instanceof NullBirthmarkElement);
        Assert.assertEquals("java.lang.Object", elements[1].getValue());
    }
}
