package handson;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.cloud.spanner.DatabaseClient;
import com.google.cloud.spanner.DatabaseId;
import com.google.cloud.spanner.ReadContext;
import com.google.cloud.spanner.Spanner;
import com.google.cloud.spanner.SpannerOptions;

import settings.SpannerSetting;

@SuppressWarnings("serial")
public class AccessTestServlet extends HttpServlet {
  public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

    resp.setContentType("text/plain");

    SpannerOptions.Builder builder = SpannerOptions.newBuilder();
    SpannerOptions options = builder.build();
    Spanner spanner = options.getService();

    DatabaseId db = DatabaseId.of(SpannerSetting.PROJECT_ID, SpannerSetting.INSTANCE_ID, SpannerSetting.DATABASE_ID);
    DatabaseClient client = spanner.getDatabaseClient(db);

    resp.getWriter().println("db:" + db);
    resp.getWriter().println("client:" + client);

    // この時点ではまだCloudSpannerへのアクセスは行っていない

    ReadContext readContext = client.singleUse();
    resp.getWriter().println("readContext:" + readContext);

    resp.getWriter().println("AccessTest Servlet.");
    spanner.close();
  }

}
