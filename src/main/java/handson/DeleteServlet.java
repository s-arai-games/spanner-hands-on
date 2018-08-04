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
import com.google.cloud.spanner.Key;
import com.google.cloud.spanner.Mutation;
import com.google.cloud.spanner.Spanner;
import com.google.cloud.spanner.SpannerException;
import com.google.cloud.spanner.SpannerOptions;
import com.google.common.collect.Lists;

import settings.SpannerSetting;

@SuppressWarnings("serial")
public class DeleteServlet extends HttpServlet {
  public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

    resp.setContentType("text/plain");

    SpannerOptions.Builder builder = SpannerOptions.newBuilder();
    builder.setCredentials(GoogleCredentials.fromStream(new FileInputStream(SpannerSetting.CREDENTIAL_PATH)));
    SpannerOptions options = builder.build();
    Spanner spanner = options.getService();

    try {
      DatabaseId db = DatabaseId.of(SpannerSetting.PROJECT_ID, SpannerSetting.INSTANCE_ID, SpannerSetting.DATABASE_ID);
      DatabaseClient client = spanner.getDatabaseClient(db);

      long userId = 2018080400123456L;

      Mutation mutation = Mutation.delete("user", Key.of(userId));

      ArrayList<Mutation> mutations = Lists.newArrayList(mutation);
      client.write(mutations);
    } catch(SpannerException e) {
      resp.getWriter().println("exception error occurred. [detail]:" + e);
    } finally {
      spanner.close();
    }

    resp.getWriter().println("Delete Servlet.");
  }
}
