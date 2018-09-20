package mapper;

import com.google.cloud.spanner.ResultSet;

public class User {

  public User(ResultSet resultSet){
    this.setUserId(resultSet.getLong("user_id"));
    this.setName(resultSet.isNull("name") ? null : resultSet.getString("name"));
  }

  public User(long userId, String name) {
    this.setUserId(userId);
    this.setName(name);
  }

  private long userId;
  private String name;

  public long getUserId() {
    return this.userId;
  }
  public void setUserId(long userId) {
    this.userId = userId;
  }

  public String getName() {
    return this.name;
  }
  public void setName(String name) {
    this.name = name;
  }

}
