package com.mulesoft.extensions.request.builder.parser;

public interface ParsingFunction <I, O> {
    O parse(I input) throws Exception;
}
