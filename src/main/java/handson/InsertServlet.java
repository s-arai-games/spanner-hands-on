package handson;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.cloud.spanner.DatabaseClient;
import com.google.cloud.spanner.DatabaseId;
import com.google.cloud.spanner.Spanner;
import com.google.cloud.spanner.SpannerOptions;

import settings.SpannerSetting;

@SuppressWarnings("serial")
public class InsertServlet extends HttpServlet {
  public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

    resp.setContentType("text/plain");

    SpannerOptions.Builder builder = SpannerOptions.newBuilder();
    SpannerOptions options = builder.build();
    Spanner spanner = options.getService();

    DatabaseId db = DatabaseId.of(SpannerSetting.PROJECT_ID, SpannerSetting.INSTANCE_NAME, SpannerSetting.DATABASE_ID);
    DatabaseClient client = spanner.getDatabaseClient(db);

    // TODO:

    resp.getWriter().println("Insert Servlet.");
    spanner.close();
  }

}
