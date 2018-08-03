package handson;

import java.io.FileInputStream;
import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.spanner.DatabaseClient;
import com.google.cloud.spanner.DatabaseId;
import com.google.cloud.spanner.KeySet;
import com.google.cloud.spanner.ReadContext;
import com.google.cloud.spanner.ResultSet;
import com.google.cloud.spanner.Spanner;
import com.google.cloud.spanner.SpannerException;
import com.google.cloud.spanner.SpannerOptions;
import com.google.common.collect.Lists;

import mapper.UserFavorite;
import settings.SpannerSetting;

@SuppressWarnings("serial")
public class StoringServlet extends HttpServlet {
  public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

    resp.setContentType("text/plain");

    SpannerOptions.Builder builder = SpannerOptions.newBuilder();
    builder.setCredentials(GoogleCredentials.fromStream(new FileInputStream(SpannerSetting.CREDENTIAL_PATH)));
    SpannerOptions options = builder.build();
    Spanner spanner = options.getService();

    try{
      DatabaseId db = DatabaseId.of(SpannerSetting.PROJECT_ID, SpannerSetting.INSTANCE_ID, SpannerSetting.DATABASE_ID);
      DatabaseClient client = spanner.getDatabaseClient(db);

      ReadContext readContext = client.singleUse();
      ResultSet resultSet = readContext.readUsingIndex("user_favorite", "idx_favorite_id", KeySet.all(), Lists.newArrayList("user_id", "favorite_id"));
      while(resultSet.next()){
        UserFavorite userFavorite = new UserFavorite(resultSet);
        resp.getWriter().println("userFavorite:" + userFavorite);
      }

      spanner.close();
    }catch(SpannerException e){
      resp.getWriter().println("exception error occurred. [detail]:" + e);
    }
    resp.getWriter().println("Select Servlet.");
  }

}
