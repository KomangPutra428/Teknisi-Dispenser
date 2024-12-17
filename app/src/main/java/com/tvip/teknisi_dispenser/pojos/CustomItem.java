package com.tvip.teknisi_dispenser.pojos;

import java.util.Objects;

public class CustomItem {
    private String title;
    private String subtitle;

    public CustomItem(String title, String subtitle) {
        this.title = title;
        this.subtitle = subtitle;
    }

    public String getTitle() {
        return title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    @Override
    public String toString() {
        return subtitle; // or whatever representation you need
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CustomItem that = (CustomItem) o;
        return Objects.equals(title, that.title) && Objects.equals(subtitle, that.subtitle);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, subtitle);
    }
}
