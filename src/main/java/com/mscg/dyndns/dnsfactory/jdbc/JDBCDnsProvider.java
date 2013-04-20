/**
 *
 */
package com.mscg.dyndns.dnsfactory.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import javax.sql.DataSource;

import org.apache.log4j.Logger;

import com.mscg.config.ConfigLoader;
import com.mscg.dyndns.dnsfactory.DnsProvider;
import com.mscg.util.Util;

/**
 * @author Giuseppe Miscione
 *
 */
public class JDBCDnsProvider implements DnsProvider {

	private static final long serialVersionUID = 8012711636804920421L;

	private static Logger log = Logger.getLogger(JDBCDnsProvider.class);

	protected DataSource ds;
	protected Connection conn;

	private String jndi;
	private String tableName;
	private String idCol;
	private String serviceCol;
	private String ipCol;
	protected String createStatement;

	public JDBCDnsProvider() throws Exception {
		Map<String, Object> configs = ConfigLoader.getInstance();

		jndi = (String) configs.get("JDBCProvider.jndi-name");
		if(jndi == null){
			log.error("Cannot find datasource name in configuration file.");
			throw new Exception("Cannot find datasource name in configuration file.");
		}
		else{
			ds = (DataSource) Util.lookup(jndi);
			if(ds == null){
				log.error("Cannot lookup datasource.");
				throw new Exception("Cannot lookup datasource.");
			}

			conn = null;

			tableName = (String) configs.get("JDBCProvider.table-name");
			idCol = (String) configs.get("JDBCProvider.id-col");
			serviceCol = (String) configs.get("JDBCProvider.service-col");
			ipCol = (String) configs.get("JDBCProvider.ip-col");
			createStatement = Util.getConfigString("JDBCProvider.create-statement").trim();

			initTable();
		}
	}

	protected void initTable() throws SQLException {
		boolean closeConn = false;
		if(conn == null){
			conn = ds.getConnection();
			closeConn = true;
		}

		try{
			String query = createStatement.
				replace("${tableName}", tableName).
				replace("${idCol}", idCol).
				replace("${serviceCol}", serviceCol).
				replace("${ipCol}", ipCol);

			PreparedStatement ps = conn.prepareStatement(query);
			log.debug("Executing query: " + ps);

			ps.executeUpdate();
		} finally{
			if(closeConn && conn != null){
				try{
					conn.close();
				} catch(SQLException e){}
				conn = null;
			}
		}
	}

	public void addIP(String service, String IP) {
		boolean closeConn = false;

		try{
			if(conn == null){
				conn = ds.getConnection();
				closeConn = true;
			}

			String query =
				"INSERT INTO " + tableName +
				"(" + serviceCol + ", " + ipCol + ") " +
				"VALUES(?, ?)";

			PreparedStatement ps = conn.prepareStatement(query);
			ps.setString(1, service);
			ps.setString(2, IP);

			log.debug("Executing query: " + ps);

			ps.executeUpdate();

		} catch (SQLException e) {
			log.error("An error occurred while inserting IP.", e);
		} finally{
			if(closeConn && conn != null){
				try{
					conn.close();
				} catch(SQLException e){}
				conn = null;
			}
		}
	}

	public void clearService(String service) {
		boolean closeConn = false;

		try{
			if(conn == null){
				conn = ds.getConnection();
				closeConn = true;
			}

			String query =
				"DELETE FROM " + tableName + " WHERE " + serviceCol + " = ?";

			PreparedStatement ps = conn.prepareStatement(query);
			ps.setString(1, service);

			log.debug("Executing query: " + ps);

			ps.executeUpdate();

		} catch (SQLException e) {
			log.error("An error occurred while clearing service.", e);
		} finally{
			if(closeConn && conn != null){
				try{
					conn.close();
				} catch(SQLException e){}
				conn = null;
			}
		}
	}

	public Collection<String> getIPs(String service) {
		boolean closeConn = false;
		ResultSet rs = null;

		Collection<String> ret = null;

		try{
			if(conn == null){
				conn = ds.getConnection();
				closeConn = true;
			}

			String query =
				"SELECT * " +
				"FROM " + tableName + " " +
				"WHERE " + serviceCol + " = ?";

			PreparedStatement ps = conn.prepareStatement(query);
			ps.setString(1, service);

			log.debug("Executing query: " + ps);

			rs = ps.executeQuery();

			while(rs.next()){
				if(ret == null){
					ret = new LinkedList<String>();
				}
				ret.add(rs.getString(ipCol));
			}

		} catch (SQLException e) {
			log.error("An error occurred while retrieving IPs.", e);
		} finally{
			if(closeConn && conn != null){
				try{
					conn.close();
				} catch(SQLException e){}
				conn = null;
			}

			if(rs != null){
				try{
					rs.close();
				} catch(SQLException e){}
			}
		}
		return ret;
	}

	public Set<String> getServicesList() {
		boolean closeConn = false;
		ResultSet rs = null;

		Set<String> ret = null;

		try{
			if(conn == null){
				conn = ds.getConnection();
				closeConn = true;
			}

			String query =
				"SELECT DISTINCT " + serviceCol + " " +
				"FROM " + tableName;

			PreparedStatement ps = conn.prepareStatement(query);

			log.debug("Executing query: " + ps);

			rs = ps.executeQuery();

			while(rs.next()){
				if(ret == null){
					ret = new LinkedHashSet<String>();
				}
				ret.add(rs.getString(serviceCol));
			}

		} catch (SQLException e) {
			log.error("An error occurred while retrieving IPs.", e);
		} finally{
			if(closeConn && conn != null){
				try{
					conn.close();
				} catch(SQLException e){}
			}

			if(rs != null){
				try{
					rs.close();
				} catch(SQLException e){}
				conn = null;
			}
		}
		return ret;
	}

}
