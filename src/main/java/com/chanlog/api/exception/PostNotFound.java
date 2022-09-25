package com.chanlog.api.exception;

public class PostNotFound extends ChanException{
    private static final String MESSAGE = "존재하지않은 글입니다";


    public PostNotFound() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 404;
    }

}
