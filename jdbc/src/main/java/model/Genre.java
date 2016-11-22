package model;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Genre {
  private Long genre_id;
  private String genre_name;

  public Long getGenre_id() {
    return genre_id;
  }

  public void setGenre_id(Long genre_id) {
    this.genre_id = genre_id;
  }

  public String getGenre_name() {
    return genre_name;
  }

  public void setGenre_name(String genre_name) {
    this.genre_name = genre_name;
  }
}
