package com.will.tooljars.mysql;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.xml.sax.SAXException;

import com.will.Consts;

/**
 * 测试场景：
 * 	同事问我一个SQL语句是不是能够跨库查询。想起JDBC的连接串里需要制定数据库名称的，但是在mysqlworkbench等工具内又可以使用连接查询，这里跑个例子验证一下；
 * 结果：
 * 	可以进行跨库查询。但是用户必须有访问这些库的权限。
 * 
 * @author root
 *
 */
public class MysqlTest {
	private final static String QUERY_STATUS = "show global status";
	private final static String QUERY_VARS = "show global variables";
	private final static String QUERY_SLOW_QUERYS_PREFIX = "SELECT *, sum(info) as count FROM information_schema.PROCESSLIST group by info having time > ";
	private final static String QUERY_SHOW_PROCESS_LIST = "show processlist";
	private final static String QUERY_SLOW_QUERYS_SUFFIX = " order by time desc ";

	private final String DRIVER_NAME = "com.mysql.jdbc.Driver";

	private Connection conn;

	Statement statement;

	private final String CACHE_KEY = "mysql";

	ResultSet rs;
	double query_cache_hits_rate = 0;

	protected Object getObj() {
		Map<String, Object> sqlResult = new HashMap<String, Object>();
		try {
			String connStr = Consts.MYSQL_CONN_PREFIX + Consts.LOCAL
					+ Consts.COLON + 3306 + Consts.MYSQL_CONN_SUFFIX;
			conn = DriverManager.getConnection(connStr, "root",
					null);
			if (conn == null) {
				return null;
			}
			getStatement();
			rs = statement.executeQuery(QUERY_STATUS);
			while (rs.next()) {
//				System.out.println(rs.getString(1) + "\t" + rs.getString(2));
			}
			rs = statement.executeQuery("select * from performance_schema.setup_timers");
			while (rs.next()) {
				System.out.println(">>>>>>>>>" + rs.getString(1) + "\t" + rs.getString(2));
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
					rs = null;
				}
				if (statement != null) {
					statement.close();
					statement = null;
				}
				if (conn != null) {
					conn.close();
					conn = null;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return sqlResult;
	}

	private void getStatement() throws SQLException {
		if (statement == null) {
			statement = conn.createStatement();
		}
	}

	public static void main(String[] args) throws Exception {
		MysqlTest target = new MysqlTest();
		Map<String, String> cfg = new HashMap<String, String>();
		cfg.put("uri",
				"jdbc:mysql://127.0.0.1:3306/mysql?&useUnicode=true&characterEncoding=UTF-8");
		cfg.put("user", "root");
		cfg.put("password", "");
		target.init();
		target.getObj();
	}

	private static Double getVal() {
		return 0.2151451;
	}

	public void init() throws SAXException, IOException,
			ParserConfigurationException, XPathExpressionException,
			ClassNotFoundException, SQLException {
		Class.forName(DRIVER_NAME);
	}
}
