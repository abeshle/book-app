package com.project.bookapp.repository.category;

import com.project.bookapp.model.Category;
import lombok.experimental.Accessors;
import org.springframework.data.jpa.repository.JpaRepository;

@Accessors(chain = true)
public interface CategoryRepository extends JpaRepository<Category, Long> {
}
