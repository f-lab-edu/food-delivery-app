package com.fdel.entity;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class StoreCategory {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "category_id")
  private Long id;

  @ManyToOne
  @JoinColumn(name = "store_id")
  private Store store;

  private String name;

  @ManyToOne
  @JoinColumn(name = "parent_id", referencedColumnName = "category_id")
  private StoreCategory parent;

  @OneToMany(mappedBy = "parent")
  private List<StoreCategory> child = new ArrayList<>();
}
