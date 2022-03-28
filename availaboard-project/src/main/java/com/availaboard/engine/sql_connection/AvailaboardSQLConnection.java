package com.availaboard.engine.sql_connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import com.availaboard.engine.resource.Equipment;
import com.availaboard.engine.resource.Resource;
import com.availaboard.engine.resource.Room;
import com.availaboard.engine.resource.Status;
import com.availaboard.engine.resource.User;
import com.availaboard.utilitys.ConfigPropReader;


public class AvailaboardSQLConnection {

	ConfigPropReader config = new ConfigPropReader();
	public final String username = config.getPropValues().get(0).replaceAll("^\"|\"$", "");
	private final String password = config.getPropValues().get(1).replaceAll("^\"|\"$", "");
	private final String url = config.getPropValues().get(2).replaceAll("^\"|\"$", "");

	/*
	 * Load all resources from the database
	 */
	public <E> Collection<E> loadResources(Class<E> res) {
		ArrayList<E> arr = new ArrayList<E>();
		try {
			String query = "select ResourceID from " + res.getSimpleName();
			final Connection con = DriverManager.getConnection(this.url, this.username, this.password);
			PreparedStatement st = con.prepareStatement(query);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				arr.add((E) createResourceWithID(rs.getInt(1), res));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return arr;
	}

	public <E> Resource createResourceWithID(int ID, Class<E> res) {
		Resource resourceObject = null;

		try {
			final Connection con = DriverManager.getConnection(this.url, this.username, this.password);

			if (res == Room.class) {
				// TODO
			} else if (res == Equipment.class) {
				// TODO
			} else {
				resourceObject = new User();
				String query = "select FirstName, LastName, Email, Username, Password from " + res.getSimpleName()
						+ " where ResourceID = ?";
				PreparedStatement st = con.prepareStatement(query);
				st.setInt(1, ID);
				ResultSet rs = st.executeQuery();
				if (rs.next()) {
					((User) resourceObject).setFirstName(rs.getString(1));
					((User) resourceObject).setLastName(rs.getString(2));
					((User) resourceObject).setEmail(rs.getString(3));
					((User) resourceObject).setUsername(rs.getString(4));
					((User) resourceObject).setPassword(rs.getString(5));
					((User) resourceObject).setId(ID);
				}
			}

			String query = "select status, name from resource where ResourceID = ?";
			PreparedStatement st = con.prepareStatement(query);
			st.setInt(1, ((Resource) resourceObject).getId());
			ResultSet rs = st.executeQuery();
			if (rs.next()) {
				((Resource) resourceObject).setStatus(Status.valueOf(rs.getString(1)));
				((Resource) resourceObject).setName(rs.getString(2));
			}
			return resourceObject;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
}