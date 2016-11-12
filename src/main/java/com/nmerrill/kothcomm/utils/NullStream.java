package com.nmerrill.kothcomm.utils;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

public class NullStream extends PrintStream{
    public NullStream(){
        super(new OutputStream() {
            @Override
            public void write(int b) throws IOException {

            }
        });
    }
}
