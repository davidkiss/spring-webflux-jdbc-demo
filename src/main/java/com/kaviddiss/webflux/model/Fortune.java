package com.kaviddiss.webflux.model;

public final class Fortune {
  public final int id;
  public final String message;

  public Fortune(int id, String message) {
    this.id = id;
    this.message = message;
  }

  @Override
  public String toString() {
    return "Fortune{" +
            "id=" + id +
            ", message='" + message + '\'' +
            '}';
  }
}
