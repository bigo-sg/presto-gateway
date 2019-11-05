package sg.bigo.test.utils;

import com.facebook.presto.jdbc.PrestoConnection;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 * @author tangyun@bigo.sg
 * @date 11/4/19 10:02 AM
 */
public class ConnectionUtil {
  private static final String URL = "jdbc:presto://test.presto.bigo.sg:8286/hive/default";
  private static final String JDBC_DRIVER = "com.facebook.presto.jdbc.PrestoDriver";
  private static PrestoConnection connection = null;
  private static Properties initProperties = new Properties();

  public ConnectionUtil(String user) throws ClassNotFoundException, SQLException {
    Class.forName(JDBC_DRIVER);
    connection = (PrestoConnection) DriverManager.getConnection(URL, "presto-test", null);
    initProperties.setProperty("user", user);
    initProperties.setProperty("applicationNamePrefix", "bigo-presto-cli");
  }

  public String execute(String sql, boolean enable_hive_syntax, Properties properties) throws SQLException {
    PrestoConnection connection = (PrestoConnection) DriverManager.getConnection(URL, initProperties);
    properties.forEach((s, s2) -> connection.setSessionProperty((String) s, (String) s2));
    if (enable_hive_syntax) {
      connection.setSessionProperty("enable_hive_syntax", "true");
    } else {
      connection.setSessionProperty("enable_hive_syntax", "false");
    }
    Statement stmt = connection.createStatement();
    ResultSet rs = null;
    try {
      rs = stmt.executeQuery(sql);
      while (rs.next()) {
        System.out.println(rs.getString(1));
      }
      return "success";
    } catch (Exception e) {
      return e.getMessage();
    } finally {
      if (rs != null) {
        rs.close();
      }
      stmt.close();
      connection.close();
    }
  }
}
