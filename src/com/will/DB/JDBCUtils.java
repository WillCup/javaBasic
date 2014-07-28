package com.will.DB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class JDBCUtils {
    private final static Log logger = LogFactory.getLog(JDBCUtils.class);
	/**
	 * Get an connection by default configuration.
	 */
	public static Connection getConnection()
	{
		String driverName = "com.mysql.jdbc.Driver";

		String url = "jdbc:mysql://127.0.0.1:3306/mysql?&useUnicode=true&characterEncoding=UTF-8";
		String user = "root" ;
		String password = "";
		Connection con = null ;
		try {
			
			Class.forName(driverName);
			con = DriverManager.getConnection(url, user, password);
			System.out.println("connect success");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return con;
		
	}
	
	/**
	 * Close the connection and release all the resourse about Mysql.
	 */
	public static void free(ResultSet rs, Statement sta , Connection con)
	{
		try {
			if(null != rs)
			{
				rs.close();
				rs = null ;
			}
			
			if(null != sta)
			{
				sta.close();
				sta = null ;
			}
			
			if(null != con)
			{
				con.close();
				con = null ;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
     * Get connection.
     */
    public static Connection getConnection(String driverName,String url,String user,String password)
    {
        Connection con = null ;
        try {
            Class.forName(driverName);
            logger.info("prepare to connect............"  + driverName);
            con = DriverManager.getConnection(url, user, password);
            logger.info("connect to db " + driverName + "success");
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return con;
        
    }
}
