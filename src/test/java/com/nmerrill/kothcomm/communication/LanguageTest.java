package com.nmerrill.kothcomm.communication;

import com.nmerrill.kothcomm.communication.languages.Language;
import com.nmerrill.kothcomm.game.players.PlayerType;
import org.eclipse.collections.impl.factory.Lists;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.util.Strings;

import java.util.List;

public abstract class LanguageTest {

    public abstract Language<TestPlayer> getLanguage();

    @Test
    public void testLanguage(){
        Language<TestPlayer> language = getLanguage();
        Assert.assertNotNull(language.directoryName());
        List<PlayerType<TestPlayer>> players = language.loadPlayers(Lists.mutable.empty());
        Assert.assertNotNull(players);
        String directoryName = language.directoryName();
        if (language.fileBased()) {
            Assert.assertTrue(players.isEmpty());
            Assert.assertNotNull(directoryName);
            Assert.assertFalse(directoryName.isEmpty());
        } else {
            Assert.assertTrue(Strings.isNullOrEmpty(directoryName));
        }
        String name = language.name();
        Assert.assertNotNull(name);
        Assert.assertFalse(name.isEmpty());
    }
}
