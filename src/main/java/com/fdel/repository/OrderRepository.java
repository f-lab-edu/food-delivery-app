package com.fdel.repository;

import com.fdel.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Menu, Long> {
}
