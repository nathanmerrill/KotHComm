package com.nmerrill.kothcomm.communication.languages.javascript;

import com.nmerrill.kothcomm.communication.Communicator;
import com.nmerrill.kothcomm.exceptions.CommunicationException;
import jdk.nashorn.api.scripting.NashornScriptEngine;
import jdk.nashorn.api.scripting.NashornScriptEngineFactory;

import javax.script.ScriptException;
import java.io.File;

public final class JSCommuncator<T, U> implements Communicator<T, U> {

    private final NashornScriptEngine engine;
    public JSCommuncator(File file){
        engine = (NashornScriptEngine) new NashornScriptEngineFactory().getScriptEngine();
        try {
            engine.eval(file.toPath().toString());
        } catch (ScriptException e) {
            throw new CommunicationException(e);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public U sendMessage(T message, String method, int timeout) {
        try {
            return (U) engine.invokeFunction(method, message);
        } catch (ScriptException | NoSuchMethodException e){
            throw new CommunicationException(e);
        }
    }
}
