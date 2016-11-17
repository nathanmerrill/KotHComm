package com.nmerrill.kothcomm.communication.languages.java;

import com.nmerrill.kothcomm.communication.LanguageTest;
import com.nmerrill.kothcomm.communication.languages.Language;

public class JavaTest extends LanguageTest{

    @Override
    public Language<TestPlayer> getLanguage() {
        return new JavaLoader<>(TestPlayer.class);
    }
}
