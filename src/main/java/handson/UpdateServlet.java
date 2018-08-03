package handson;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.spanner.DatabaseClient;
import com.google.cloud.spanner.DatabaseId;
import com.google.cloud.spanner.Mutation;
import com.google.cloud.spanner.Spanner;
import com.google.cloud.spanner.SpannerException;
import com.google.cloud.spanner.SpannerOptions;
import com.google.common.collect.Lists;

import settings.SpannerSetting;

@SuppressWarnings("serial")
public class UpdateServlet extends HttpServlet {
  public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

    resp.setContentType("text/plain");

    SpannerOptions.Builder builder = SpannerOptions.newBuilder();
    builder.setCredentials(GoogleCredentials.fromStream(new FileInputStream(SpannerSetting.CREDENTIAL_PATH)));
    SpannerOptions options = builder.build();
    Spanner spanner = options.getService();

    try{
      DatabaseId db = DatabaseId.of(SpannerSetting.PROJECT_ID, SpannerSetting.INSTANCE_ID, SpannerSetting.DATABASE_ID);
      DatabaseClient client = spanner.getDatabaseClient(db);

      long userId = 2018080400123456L;
      String name = "test_account_updated";

      Mutation mutation = Mutation.newUpdateBuilder("user")
          .set("user_id").to(userId)
          .set("name").to(name)
          .build();

      ArrayList<Mutation> mutations = Lists.newArrayList(mutation);
      client.write(mutations);
      spanner.close();
    }catch(SpannerException e){
      resp.getWriter().println("exception error occurred. [detail]:" + e);
    }

    resp.getWriter().println("Update Servlet.");
  }

}
