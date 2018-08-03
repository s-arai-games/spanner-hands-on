package handson;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.spanner.DatabaseClient;
import com.google.cloud.spanner.DatabaseId;
import com.google.cloud.spanner.Key;
import com.google.cloud.spanner.Mutation;
import com.google.cloud.spanner.Spanner;
import com.google.cloud.spanner.SpannerException;
import com.google.cloud.spanner.SpannerOptions;
import com.google.cloud.spanner.Struct;
import com.google.cloud.spanner.TransactionContext;
import com.google.cloud.spanner.TransactionRunner.TransactionCallable;
import com.google.common.collect.Lists;

import mapper.User;
import mapper.UserFavorite;
import settings.SpannerSetting;

@SuppressWarnings("serial")
public class TransactionServlet extends HttpServlet {
  public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

    resp.setContentType("text/plain");

    SpannerOptions.Builder builder = SpannerOptions.newBuilder();
    builder.setCredentials(GoogleCredentials.fromStream(new FileInputStream(SpannerSetting.CREDENTIAL_PATH)));
    SpannerOptions options = builder.build();
    Spanner spanner = options.getService();

    try{
      DatabaseId db = DatabaseId.of(SpannerSetting.PROJECT_ID, SpannerSetting.INSTANCE_ID, SpannerSetting.DATABASE_ID);
      DatabaseClient client = spanner.getDatabaseClient(db);

      client.readWriteTransaction().run(
          new TransactionCallable<Void>() {
            @Override
            public Void run(TransactionContext tran) throws Exception {
              long userId = 2018080400123456L;
              Struct u = tran.readRow("user", Key.of(userId), Lists.newArrayList("user_id", "name"));
              if(!u.isNull("user_id")){
                // to user
                User user = new User(u.getLong("user_id"), u.getString("name"));
                user.setName("transaction");
                tran.buffer(
                  Mutation.newUpdateBuilder("user")
                    .set("user_id").to(user.getUserId())
                    .set("name").to(user.getName())
                    .build()
                );
                // to user_favorite
                List<UserFavorite> favoList = Lists.newArrayList(
                    new UserFavorite(user.getUserId(), 1, "ringo"),
                    new UserFavorite(user.getUserId(), 2, "mikan"),
                    new UserFavorite(user.getUserId(), 3, "tomato")
                );
                favoList.stream().forEach(f -> {
                  tran.buffer(
                    Mutation.newInsertOrUpdateBuilder("user_favorite")
                      .set("user_id").to(f.getUserId())
                      .set("favorite_id").to(f.getFavoriteId())
                      .set("favorite_thing").to(f.getFavoriteThing())
                      .build()
                  );
                });
              }
              return null;
            }
          }
        );
      spanner.close();
    }catch(SpannerException e){
      resp.getWriter().println("exception error occurred. [detail]:" + e);
    }

    resp.getWriter().println("Transaction Servlet.");
  }

}
