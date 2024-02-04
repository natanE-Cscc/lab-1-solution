package com.edu.degrees.data;

import com.edu.degrees.domain.MenuItem;
import org.springframework.data.repository.CrudRepository;

public interface MenuItemCrudRepository extends CrudRepository<MenuItem, Long> {
}
