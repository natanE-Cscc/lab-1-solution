package com.edu.degrees.data;

import com.edu.degrees.domain.MenuCategory;
import org.springframework.data.repository.CrudRepository;

public interface DegreesBlogRepository extends CrudRepository<MenuCategory, Long>{
}
