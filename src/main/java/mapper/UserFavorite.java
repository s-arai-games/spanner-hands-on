package mapper;

import com.google.cloud.spanner.ResultSet;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class UserFavorite {

  public UserFavorite(ResultSet resultSet){
    this.setUserId(resultSet.getLong("user_id"));
    this.setFavoriteId(resultSet.getLong("favorite_id"));
    this.setFavoriteThing(resultSet.isNull("favorite_thing") ? null : resultSet.getString("favorite_thing"));
  }

//  public UserFavorite(long userId, long favoriteId){
//    this.setUserId(userId);
//    this.setFavoriteId(favoriteId);
//  }

  private long userId;
  private long favoriteId;
  private String favoriteThing;

}
