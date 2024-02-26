package com.edu.degrees.data;

import com.edu.degrees.domain.MenuCategory;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
public interface DegreesBlogRepository extends CrudRepository<MenuCategory, Long>, PagingAndSortingRepository<MenuCategory, Long> {
    List<MenuCategory> findAllByOrderBySortOrderAscCategoryTitleAsc();
}

