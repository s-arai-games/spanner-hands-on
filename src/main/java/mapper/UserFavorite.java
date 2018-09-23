package mapper;

import com.google.cloud.spanner.ResultSet;


public class UserFavorite {

  @Override
  public String toString() {
    return "UserFavorite [userId=" + userId + ", favoriteId=" + favoriteId + ", favoriteThing=" + favoriteThing + "]";
  }

  public UserFavorite(ResultSet resultSet){
    this.setUserId(resultSet.getLong("user_id"));
    this.setFavoriteId(resultSet.getLong("favorite_id"));
    this.setFavoriteThing(resultSet.isNull("favorite_thing") ? null : resultSet.getString("favorite_thing"));
  }

  public UserFavorite(long userId, long favoriteId, String favoriteThing){
    this.setUserId(userId);
    this.setFavoriteId(favoriteId);
    this.setFavoriteThing(favoriteThing);
  }

//  public UserFavorite(long userId, long favoriteId){
//    this.setUserId(userId);
//    this.setFavoriteId(favoriteId);
//  }

  private long userId;
  private long favoriteId;
  private String favoriteThing;

  public long getUserId() {
    return this.userId;
  }
  public void setUserId(long userId) {
    this.userId = userId;
  }

  public long getFavoriteId() {
    return this.favoriteId;
  }
  public void setFavoriteId(long favoriteId) {
    this.favoriteId = favoriteId;
  }

  public String getFavoriteThing() {
    return this.favoriteThing;
  }
  public void setFavoriteThing(String favoriteThing) {
    this.favoriteThing = favoriteThing;
  }

}
