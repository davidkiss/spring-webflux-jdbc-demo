package com.kaviddiss.webflux.model;

public final class World {
  public final int id;
  public int randomNumber;

  public World(int id, int randomNumber) {
    this.id = id;
    this.randomNumber = randomNumber;
  }

  @Override
  public String toString() {
    return "World{" +
            "id=" + id +
            ", randomNumber=" + randomNumber +
            '}';
  }
}
