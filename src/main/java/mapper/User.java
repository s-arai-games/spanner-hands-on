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
public class User {

  public User(ResultSet resultSet){
    this.setUserId(resultSet.getLong("user_id"));
    this.setName(resultSet.isNull("name") ? null : resultSet.getString("name"));
  }

  private long userId;
  private String name;

}
