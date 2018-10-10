package com.kaviddiss.webflux.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
@AllArgsConstructor
public final class World {
  @Id
  private int id;
  private int randomnumber;
}
