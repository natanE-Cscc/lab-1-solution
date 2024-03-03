package com.edu.degrees.api;

import com.edu.degrees.domain.MenuCategory;
import com.edu.degrees.data.DegreesBlogRepository;
import com.edu.degrees.data.MenuItemRepository;
import com.edu.degrees.domain.MenuItem;
import com.edu.degrees.domain.MenuOptions;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
//@RequestMapping("“/public/api/menus")
public class MenuController {
    private final DegreesBlogRepository degreesBlogRepository;
    private final MenuItemRepository menuItemRepository;

    public MenuController(DegreesBlogRepository degreesBlogRepository, MenuItemRepository menuItemRepository) {
        this.degreesBlogRepository = degreesBlogRepository;
        this.menuItemRepository = menuItemRepository;
    }
    @GetMapping("/public/api/menus")
    public Iterable<MenuOptions> getAllMenuCategories() {
        List<MenuOptions> menus = new ArrayList<>();
        for (MenuCategory menuCategory : degreesBlogRepository.findAllByOrderBySortOrderAscCategoryTitleAsc()) {
            List<MenuItem> menuItemList = new ArrayList<>();
            menuItemList = menuItemRepository.findByMenuCategoryOrderBySortOrderAscNameAsc(menuCategory);
            MenuOptions menuOptions = new MenuOptions(menuCategory, menuItemList);
            menus.add(menuOptions);
        }

        return menus;
    }

}
