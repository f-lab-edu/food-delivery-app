package com.fdel.repository;

import com.fdel.entity.Menu;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuRepository extends JpaRepository<Menu, Long> {

  Optional<Menu> findById(String id);
}
