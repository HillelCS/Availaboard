package com.availaboard.engine.sql_connection;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import org.apache.commons.lang3.reflect.FieldUtils;
import com.availaboard.engine.resource.Equipment;
import com.availaboard.engine.resource.Resource;
import com.availaboard.engine.resource.Room;
import com.availaboard.engine.resource.Status;
import com.availaboard.engine.resource.User;
import com.availaboard.utilitys.ConfigPropReader;
import com.google.common.reflect.TypeToken;

import ch.qos.logback.core.recovery.ResilientSyslogOutputStream;

public class AvailaboardSQLConnection {

	ConfigPropReader config = new ConfigPropReader();
	public final String username = config.getPropValues().get(0).replaceAll("^\"|\"$", "");
	private final String password = config.getPropValues().get(1).replaceAll("^\"|\"$", "");
	private final String url = config.getPropValues().get(2).replaceAll("^\"|\"$", "");

	/*
	 * Loads all resources from the database. When calling the method pass in a
	 * class and a type and it will look for that class name in the database and
	 * load the appropriate type of object. It returns a Collection of all the
	 * Objects of the type <E> returned.
	 * 
	 */
	public <E> Collection<E> loadResources(Class<E> res) {
		ArrayList<E> arr = new ArrayList<E>();
		try {
			String query = "select ResourceID from " + res.getSimpleName();
			final Connection con = DriverManager.getConnection(this.url, this.username, this.password);
			PreparedStatement st = con.prepareStatement(query);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				arr.add((E) createResourceWithID(rs.getInt(1), (Class<Resource>) res));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return arr;
	}

	/*
	 * Not a great current solution to the problem but currently all I've got. The
	 * method takes an ID and a class and constructs an object of that class based
	 * off of what type of class it is. It then appropriately loads the information
	 * from the database and adds it to the object. Ideally the method uses metadata
	 * from the database and matches all of the fields in the object to the database
	 * and loads them automatically.
	 */

//	/*
//	 * Refactoring.
//	 */
//	public <E> Resource createResourceWithID(int ID, Class<E> res) {
//		Resource resourceObject = null;
//
//		try {
//			final Connection con = DriverManager.getConnection(this.url, this.username, this.password);
//
//			if (res == Room.class) {
//				// TODO
//			} else if (res == Equipment.class) {
//				// TODO
//			} else {
//				resourceObject = new User();
//				String query = "select FirstName, LastName, Email, Username, Password from " + res.getSimpleName()
//						+ " where ResourceID = ?";
//				PreparedStatement st = con.prepareStatement(query);
//				st.setInt(1, ID);
//				ResultSet rs = st.executeQuery();
//				if (rs.next()) {
//					((User) resourceObject).setFirstName(rs.getString(1));
//					((User) resourceObject).setLastName(rs.getString(2));
//					((User) resourceObject).setEmail(rs.getString(3));
//					((User) resourceObject).setUsername(rs.getString(4));
//					((User) resourceObject).setPassword(rs.getString(5));
//					((User) resourceObject).setId(ID);
//				}
//			}
//
//			String query = "select status, name from resource where ResourceID = ?";
//			PreparedStatement st = con.prepareStatement(query);
//			st.setInt(1, ((Resource) resourceObject).getId());
//			ResultSet rs = st.executeQuery();
//			if (rs.next()) {
//				((Resource) resourceObject).setStatus(Status.valueOf(rs.getString(1)));
//				((Resource) resourceObject).setName(rs.getString(2));
//			}
//			return resourceObject;
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		return null;
//	}

	public <E> Resource createResourceWithID(int ID, Class<? extends Resource> res) {
		try {
			Connection con = DriverManager.getConnection(this.url, this.username, this.password);
			Resource resourceObject = res.getDeclaredConstructor().newInstance();
			((User) resourceObject).setEmail("hey");
			System.out.println(((User) resourceObject).getEmail());
			resourceObject.setId(ID);

			String query = "select status, name from resource where ResourceID = ?";
			PreparedStatement st = con.prepareStatement(query);
			st.setInt(1, ((Resource) resourceObject).getId());
			ResultSet rs = st.executeQuery();
			if (rs.next()) {
				(resourceObject).setStatus(Status.valueOf(rs.getString(1)));
				(resourceObject).setName(rs.getString(2));
			}

			for (Field field : res.getDeclaredFields()) {
				query = "select ? from " + res.getSimpleName() + " where ResourceID = ?";
				PreparedStatement st2 = con.prepareStatement(query);
				st2.setString(1, field.getName());
				st2.setInt(2, ID);
				ResultSet rs2 = st2.executeQuery();
				if (rs2.next()) {
					FieldUtils.writeField(resourceObject, field.getName(), rs.getObject(1), true);
				}
			}
			System.out.println(((User) resourceObject).getEmail());
			return resourceObject;
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}

}
