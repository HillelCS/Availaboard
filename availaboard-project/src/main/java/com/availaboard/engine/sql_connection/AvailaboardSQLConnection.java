package com.availaboard.engine.sql_connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import com.availaboard.engine.resource.Resource;
import com.availaboard.engine.resource.Status;
import com.availaboard.utilitys.ConfigPropReader;

public class AvailaboardSQLConnection {

	ConfigPropReader config = new ConfigPropReader();
	public final String username = config.getPropValues().get(0);
	private final String password = config.getPropValues().get(1);
	private final String url = config.getPropValues().get(2);

	/*
	 * Load all resources from the database
	 */
	public ArrayList<Resource> loadResources() {
		ArrayList<Resource> arr = new ArrayList<Resource>();
		try {
			String query = "select ResourceID from resource";
			Class.forName("com.mysql.cj.jdbc.Driver");
			final Connection con = DriverManager.getConnection(this.url, this.username, this.password);
			PreparedStatement st = con.prepareStatement(query);
			ResultSet rs = st.executeQuery();
			if (rs.next()) {
				Resource res = createResourceWithID(rs.getInt(1));
				arr.add(res);
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return arr;
	}

	public Resource createResourceWithID(int ID) {
		System.out.println(ID);
		try {
			String query = "select name, contact, ResourceID from resource where ResourceID = ?";
			Class.forName("com.mysql.cj.jdbc.Driver");
			final Connection con = DriverManager.getConnection(this.url, this.username, this.password);
			PreparedStatement st = con.prepareStatement(query);
			st.setInt(1, ID);
			ResultSet rs = st.executeQuery();
			if (rs.next()) {
				Resource res = new Resource();
				res.setName(rs.getString(1));
				res.setContact(rs.getString(2));
				res.setId(rs.getInt(3));
				return res;
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
}