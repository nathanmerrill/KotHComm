package com.ppcg.kothcomm.game.exceptions;

import java.io.File;

/**
 * Thrown when the path given doesn't match the folder/files
 */
public class InvalidFilePathException extends RuntimeException {
    public InvalidFilePathException(String message, File file){
        super(message+": "+file.getAbsolutePath());
    }
}
