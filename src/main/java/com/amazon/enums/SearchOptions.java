package com.amazon.enums;

import java.util.Random;

public enum SearchOptions {

    PENCIL_POUCH("pencil pouch"),
    HEADPHONES("headphones"),
    PENCIL_CASE("pencil case"),
    FOOTWEAR("men footwear");

    private String search;

    SearchOptions(String search) {
        this.search = search;
    }

    public String getSearch() {
        return search;
    }

    public static String getRandomSearch() {
        return SearchOptions.values()[new Random().nextInt(SearchOptions.values().length)].getSearch();
    }
}
