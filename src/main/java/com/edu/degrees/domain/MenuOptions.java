package com.edu.degrees.domain;

import java.util.List;

public class MenuOptions {
    private long id;
    private MenuCategory menuCategory;
    private List<MenuItem> menuItemList;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public MenuOptions() {
    }

    public MenuOptions(MenuCategory menuCategory, List<MenuItem> menuItemList) {
        this.menuCategory = menuCategory;
        this.menuItemList = menuItemList;
    }

    public MenuCategory getMenuCategory() {
        return menuCategory;
    }

    public void setMenuCategory(MenuCategory menuCategory) {
        this.menuCategory = menuCategory;
    }

    public List<MenuItem> getMenuItemList() {
        return menuItemList;
    }

    public void setMenuItemList(List<MenuItem> menuItemList) {
        this.menuItemList = menuItemList;
    }

    @Override
    public String toString() {
        return "MenuOptions{" +
                "menuCategory=" + menuCategory +
                ", menuItemList=" + menuItemList +
                '}';
    }
}
