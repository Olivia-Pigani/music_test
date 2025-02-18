package com.example.music_unittest.domain.enumeration;

public enum Genre {
    ROCK("ROCK"),
    POP("POP"),
    METAL("METAL"),
    ELECTRO("ELECTRO"),
    CLASSIC("CLASSIC");

    final String code;

    Genre(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return code;
    }
}
