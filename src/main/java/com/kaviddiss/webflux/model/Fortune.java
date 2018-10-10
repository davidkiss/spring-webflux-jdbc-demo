package com.kaviddiss.webflux.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
@AllArgsConstructor
public class Fortune {
  @Id
  private int id;
  private String message;
}
