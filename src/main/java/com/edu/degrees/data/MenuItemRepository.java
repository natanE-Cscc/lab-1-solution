package com.edu.degrees.data;

import com.edu.degrees.domain.MenuCategory;
import com.edu.degrees.domain.MenuItem;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
public interface MenuItemRepository extends CrudRepository<MenuItem, Long>, PagingAndSortingRepository<MenuItem, Long> {
    List<MenuItem> findByMenuCategoryOrderBySortOrderAscNameAsc(MenuCategory menuCategory);
}

