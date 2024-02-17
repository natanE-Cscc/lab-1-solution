package com.edu.degrees.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity

public class MenuCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    @Size(min = 1, max = 80, message = "Please enter a category title up to 80 characters in length")
    private String categoryTitle;
    private String categoryNotes;
    @NotNull(message = "sortOrder is required")
    private Integer sortOrder;

    public MenuCategory(Long id, String categoryTitle, String categoryNotes, Integer sortOrder) {
        this.id = id;
        this.categoryTitle = categoryTitle;
        this.categoryNotes = categoryNotes;
        this.sortOrder = sortOrder;
    }

    public MenuCategory() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCategoryTitle() {
        return categoryTitle;
    }

    public void setCategoryTitle(String categoryTitle) {
        this.categoryTitle = categoryTitle;
    }

    public String getCategoryNotes() {
        return categoryNotes;
    }

    public void setCategoryNotes(String categoryNotes) {
        this.categoryNotes = categoryNotes;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    @Override
    public String toString() {
        return "MenuCategory{" +
                "id=" + id +
                ", categoryTitle='" + categoryTitle + '\'' +
                ", categoryNotes='" + categoryNotes + '\'' +
                ", sortOrder=" + sortOrder +
                '}';
    }
}
