package handson;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.cloud.spanner.DatabaseClient;
import com.google.cloud.spanner.DatabaseId;
import com.google.cloud.spanner.ReadContext;
import com.google.cloud.spanner.ResultSet;
import com.google.cloud.spanner.Spanner;
import com.google.cloud.spanner.SpannerOptions;
import com.google.cloud.spanner.Statement;

import mapper.User;
import settings.SpannerSetting;

@SuppressWarnings("serial")
public class SelectBySqlServlet extends HttpServlet {
  public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

    resp.setContentType("text/plain");

    SpannerOptions.Builder builder = SpannerOptions.newBuilder();
    SpannerOptions options = builder.build();
    Spanner spanner = options.getService();

    DatabaseId db = DatabaseId.of(SpannerSetting.PROJECT_ID, SpannerSetting.INSTANCE_ID, SpannerSetting.DATABASE_ID);
    DatabaseClient client = spanner.getDatabaseClient(db);
    ReadContext readContext = client.singleUse();

    Statement statement = Statement.newBuilder("SELECT * FROM user WHERE true").build();
    ResultSet resultSet = readContext.executeQuery(statement);

    while(resultSet.next()){
      User user = new User(resultSet);
      resp.getWriter().println("user:" + user);
    }

    resp.getWriter().println("Select Servlet.");
    spanner.close();
  }

}
