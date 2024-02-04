package com.edu.degrees.domain;

import jakarta.persistence.*;

@Entity
public class MenuItem {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        @ManyToOne(cascade = CascadeType.ALL)
        private MenuCategory menuCategory;
        private String name;
        private String description;
        private String price;
        private Integer sortOrder;

        public MenuItem() {
        }

        public MenuItem(Long id, MenuCategory menuCategory, String name, String description, String price, Integer sortOrder) {
            this.id = id;
            this.menuCategory = menuCategory;
            this.name = name;
            this.description = description;
            this.price = price;
            this.sortOrder = sortOrder;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public MenuCategory getMenuCategory() {
            return menuCategory;
        }

        public void setMenuCategory(MenuCategory menuCategory) {
            this.menuCategory = menuCategory;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public Integer getSortOrder() {
            return sortOrder;
        }

        public void setSortOrder(Integer sortOrder) {
            this.sortOrder = sortOrder;
        }
    }

