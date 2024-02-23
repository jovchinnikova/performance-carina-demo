package com.amazon.enums;

public enum SortingOption {

    FEATURED("Featured"),
    PRICE_ACS("Price: Low to High"),
    PRICE_DESC("Price: High to Low"),
    RATING("Avg. Customer Review"),
    NEWEST("Newest Arrivals"),
    BEST_SELLERS("Best Sellers");

    private String title;

    private SortingOption(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
