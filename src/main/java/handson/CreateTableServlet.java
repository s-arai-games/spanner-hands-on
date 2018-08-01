package handson;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.cloud.spanner.Database;
import com.google.cloud.spanner.DatabaseAdminClient;
import com.google.cloud.spanner.DatabaseClient;
import com.google.cloud.spanner.DatabaseId;
import com.google.cloud.spanner.ReadOnlyTransaction;
import com.google.cloud.spanner.ResultSet;
import com.google.cloud.spanner.Spanner;
import com.google.cloud.spanner.SpannerOptions;
import com.google.cloud.spanner.Statement;

import settings.SpannerSetting;

@SuppressWarnings("serial")
@Deprecated
public class CreateTableServlet extends HttpServlet {
  public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

    resp.setContentType("text/plain");

    SpannerOptions.Builder builder = SpannerOptions.newBuilder();
    SpannerOptions options = builder.build();
    Spanner spanner = options.getService();

    DatabaseId db = DatabaseId.of(SpannerSetting.PROJECT_ID, SpannerSetting.INSTANCE_NAME, SpannerSetting.DATABASE_ID);

    DatabaseAdminClient adminClient = spanner.getDatabaseAdminClient();
    DatabaseClient client = spanner.getDatabaseClient(db);

    Database dbAdmin = adminClient.getDatabase(SpannerSetting.INSTANCE_NAME, SpannerSetting.DATABASE_ID);

    ReadOnlyTransaction readContext = client.readOnlyTransaction();


    Statement statement = Statement.newBuilder("SELECT * FROM user WHERE true").build();
    ResultSet resultSet = readContext.executeQuery(statement);

    while(resultSet.next()){
      long userId = resultSet.getLong("user_id");
      String name = resultSet.getString("name");
      String favorite = resultSet.getString("favorite");
      resp.getWriter().println("user_id:" + userId + ", name:" + name + ", favorite:" + favorite);
    }

    resp.getWriter().println("hello world!(Servlet)");

    spanner.close();
  }

}
