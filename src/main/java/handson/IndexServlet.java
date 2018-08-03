package handson;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
public class IndexServlet extends HttpServlet {
  public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

    resp.setContentType("text/plain");

    SpannerOptions.Builder builder = SpannerOptions.newBuilder();
    SpannerOptions options = builder.build();
    Spanner spanner = options.getService();

    try{
      DatabaseId db = DatabaseId.of(SpannerSetting.PROJECT_ID, SpannerSetting.INSTANCE_ID, SpannerSetting.DATABASE_ID);
      DatabaseClient client = spanner.getDatabaseClient(db);

      ReadContext readContext = client.singleUse();
      ResultSet resultSet = readContext.readUsingIndex("user_favorite", "idx_favorite_id", KeySet.all(), Lists.newArrayList("user_id", "favorite_id"));
      while(resultSet.next()){
        UserFavorite userFavorite = new UserFavorite(resultSet);
//        UserFavorite userFavorite = new UserFavorite(resultSet.getLong("user_id"), resultSet.getLong("favorite_id"));
        resp.getWriter().println("userFavorite:" + userFavorite);
      }

      spanner.close();
    }catch(SpannerException e){
      resp.getWriter().println("exception error occurred. [detail]:" + e);
    }
    resp.getWriter().println("Select Servlet.");
  }

}
