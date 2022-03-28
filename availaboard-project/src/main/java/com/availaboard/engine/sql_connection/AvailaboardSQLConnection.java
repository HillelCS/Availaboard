package com.availaboard.engine.sql_connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.availaboard.engine.resource.Equipment;
import com.availaboard.engine.resource.Resource;
import com.availaboard.engine.resource.Room;
import com.availaboard.engine.resource.Status;
import com.availaboard.engine.resource.User;
import com.availaboard.utilitys.ConfigPropReader;

import ch.qos.logback.core.subst.Token.Type;

public class AvailaboardSQLConnection {

	ConfigPropReader config = new ConfigPropReader();
	public final String username = config.getPropValues().get(0).replaceAll("^\"|\"$", "");
	private final String password = config.getPropValues().get(1).replaceAll("^\"|\"$", "");
	private final String url = config.getPropValues().get(2).replaceAll("^\"|\"$", "");

	/*
	 * Load all resources from the database
	 */
	public ArrayList<?> loadResources(Class<?> res) {
		System.out.println(res.getClass().toString());
		ArrayList<?> arr = new ArrayList();
		try {
			String query = "select ResourceID from ?";
			final Connection con = DriverManager.getConnection(this.url, this.username, this.password);
			PreparedStatement st = con.prepareStatement(query);
			System.out.println(res.toString());
			st.setString(1, res.toString());
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return arr;
	}

	public <T> Object createResourceWithID(int ID, T res2) {
		Object res = null;
//
//		try {
//			final Connection con = DriverManager.getConnection(this.url, this.username, this.password);
//
//			if (res2 == Room.class) {
//				// TODO
//			} else if (res2 == Equipment.class) {
//				// TODO
//			} else {
//				res = new User();
//				String query = "select FirstName, LastName, Email, Username, Password from ? where ResourceID = ?";
//				PreparedStatement st = con.prepareStatement(query);
//				st.setString(1, res2.toString());
//				st.setInt(2, ID);
//				ResultSet rs = st.executeQuery();
//				if (rs.next()) {
//					((User) res).setFirstName(rs.getString(1));
//					((User) res).setLastName(rs.getString(2));
//					((User) res).setEmail(rs.getString(3));
//					((User) res).setUsername(rs.getString(4));
//					((User) res).setPassword(rs.getString(5));
//				}
//			}
//
//			String query = "select status from resource where ResourceID = ?";
//			PreparedStatement st = con.prepareStatement(query);
//			st.setInt(1, res.getId());
//			ResultSet rs = st.executeQuery();
//			if (rs.next()) {
//				res.setStatus(Status.valueOf(rs.getString(1)));
//			}
//			return res;
//
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
		return null;
	}
}