package com.magicauction.batchupdater.exceptions;

public class RestCallException extends BatchException{
    public RestCallException() {
        super("ERROR ON REST CALL");
    }
}
