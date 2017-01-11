package com.nmerrill.kothcomm.communication.languages.java;

import com.nmerrill.kothcomm.communication.FileTest;
import com.nmerrill.kothcomm.communication.LanguageTest;
import com.nmerrill.kothcomm.communication.TestPlayer;
import com.nmerrill.kothcomm.game.players.Submission;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.impl.factory.Lists;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;

public class JavaTest extends LanguageTest implements FileTest {

    @BeforeClass
    public void before(){
        createTestFolder();
    }

    @Override
    public JavaLoader<TestPlayer> getLanguage() {
        return new JavaLoader<>(TestPlayer.class);
    }

    @Test
    public void testPlayerCompilation() throws IOException{
        JavaLoader<TestPlayer> loader = getLanguage();
        String playerName = "Player";
        File player = write("import com.nmerrill.kothcomm.communication.TestPlayer; public class "+playerName+" extends TestPlayer { }", playerName+".java");
        File invalidPlayer = write("public class InvalidPlayer { }", "InvalidPlayer.java");
        MutableList<Submission<TestPlayer>> submissions = loader.loadPlayers(Lists.mutable.of(player, invalidPlayer));
        Assert.assertEquals(1, submissions.size());
        Assert.assertEquals(playerName, submissions.getOnly().create().getClass().getName());
    }

    @Override
    public File testDirectory() {
        return new File("java_test");
    }

    @AfterClass(alwaysRun = true)
    public void after(){
        deleteTestFolder();
    }

}
