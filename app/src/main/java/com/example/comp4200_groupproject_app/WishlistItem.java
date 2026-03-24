package com.example.comp4200_groupproject_app;

public class WishlistItem {
        String name;
        double target;
        double saved;

        WishlistItem(String name, double target) {
            this.name = name;
            this.target = target;
            this.saved = 0;
        }
}
