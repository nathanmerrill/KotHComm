package com.nmerrill.kothcomm.communication;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Random;

public class ArgumentsTest {
    
    private final String[] argsOf(String...args){
        return args;
    }

    private final String[] emptyArgs = argsOf();

    @Test
    public void randomIsNotNull(){
        Assert.assertNotNull(Arguments.parse(emptyArgs).getRandom());
        Assert.assertNotNull(Arguments.parse(argsOf("-r","-1")).getRandom());
        Assert.assertNotNull(Arguments.parse(argsOf("-r","1000")).getRandom());
        Arguments args = new Arguments();
        args.randomSeed = 1000;
        Assert.assertNotNull(Arguments.parse(emptyArgs).getRandom());
    }
    
    @Test
    public void randomSeedIsSet(){
        int seed = new Random().nextInt();
        Random deterministic1 = new Random(seed);
        Random deterministic2 = Arguments.parse(argsOf("-r", seed+"")).getRandom();
        Arguments args = new Arguments();
        args.randomSeed = seed;
        Random deterministic3 = Arguments.parse(emptyArgs, args).getRandom();
        Random nonDeterministic = Arguments.parse(emptyArgs).getRandom();
        for (int i = 0; i < 10; i++){
            double expected = deterministic1.nextDouble();
            Assert.assertEquals(deterministic2.nextDouble(), expected);
            Assert.assertEquals(deterministic3.nextDouble(), expected);
            Assert.assertNotEquals(nonDeterministic.nextDouble(), expected);
        }
    }

    @Test
    public void testValidQuestionId(){
        Assert.assertFalse(Arguments.parse(emptyArgs).validQuestionID());
        Assert.assertTrue(Arguments.parse(argsOf("-q","15")).validQuestionID());
        Assert.assertFalse(Arguments.parse(argsOf("-q","-20")).validQuestionID());
        Arguments arguments = new Arguments();
        arguments.questionID = 20;
        Assert.assertTrue(Arguments.parse(emptyArgs, arguments).validQuestionID());
    }
}
