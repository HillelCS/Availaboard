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
import java.util.Set;
import java.util.function.Supplier;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AssignableTypeFilter;

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
	public <E extends Resource> Collection<E> loadResources(Class<E> type) {
		ArrayList<E> arr = new ArrayList<E>();
		try {
			String query = "select ResourceID from " + type.getSimpleName();
			final Connection con = DriverManager.getConnection(this.url, this.username, this.password);
			PreparedStatement st = con.prepareStatement(query);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				arr.add(createResourceWithID(rs.getInt(1), type));
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

	// Try #2
	/*
	 * private void setAllFields(User user, Connection con) {
	 * 
	 * try { for (Field field : user.getClass().getDeclaredFields()) { String query
	 * = "select ? from " + user.getClass().getSimpleName() +
	 * " where ResourceID = ?"; PreparedStatement st = con.prepareStatement(query);
	 * st.setString(1, field.getName()); st.setInt(2, user.getId()); ResultSet rs =
	 * st.executeQuery(); if (rs.next()) { FieldUtils.writeField(user,
	 * field.getName(), rs.getObject(1), true); } } } catch (IllegalAccessException
	 * | SQLException e) {
	 * 
	 * } }
	 * 
	 * private void setAllFields(Room room, Connection con) { try { for (Field field
	 * : room.getClass().getDeclaredFields()) { String query = "select ? from " +
	 * room.getClass().getSimpleName() + " where ResourceID = ?"; PreparedStatement
	 * st = con.prepareStatement(query); st.setString(1, field.getName());
	 * st.setInt(2, room.getId()); ResultSet rs = st.executeQuery(); if (rs.next())
	 * { FieldUtils.writeField(room, field.getName(), rs.getObject(1), true); } } }
	 * catch (IllegalAccessException | SQLException e) {
	 * 
	 * } }
	 * 
	 * private void setAllFields(Equipment equipment, Connection con) { try { for
	 * (Field field : equipment.getClass().getDeclaredFields()) { String query =
	 * "select ? from " + equipment.getClass().getSimpleName() +
	 * " where ResourceID = ?"; PreparedStatement st = con.prepareStatement(query);
	 * st.setString(1, field.getName()); st.setInt(2, equipment.getId()); ResultSet
	 * rs = st.executeQuery(); if (rs.next()) { FieldUtils.writeField(equipment,
	 * field.getName(), rs.getObject(1), true); } } } catch (IllegalAccessException
	 * | SQLException e) {
	 * 
	 * } }
	 * 
	 * public <E extends Resource> E createResourceWithID(int ID, Class<E> type) {
	 * String name = null; Status status = null; Resource res = new Resource();
	 * ClassPathScanningCandidateComponentProvider e = new
	 * ClassPathScanningCandidateComponentProvider(false); try { Connection con =
	 * DriverManager.getConnection(this.url, this.username, this.password); String
	 * query = "select status, name from resource where ResourceID = ?";
	 * PreparedStatement st = con.prepareStatement(query); st.setInt(1, ID);
	 * ResultSet rs = st.executeQuery(); if (rs.next()) { status =
	 * Status.valueOf(rs.getString(1)); name = rs.getString(2); }
	 * 
	 * if (type == Room.class) { Room room = new Room(); room.setName(name);
	 * room.setStatus(status); setAllFields(room, con); return (E) room; } else if
	 * (type == Equipment.class) { Equipment equipment = new Equipment(); return (E)
	 * equipment; } else { User user = new User(); return (E) user; } } catch
	 * (SQLException e) { e.printStackTrace(); } catch (IllegalArgumentException e)
	 * { // TODO Auto-generated catch block e.printStackTrace(); } catch
	 * (SecurityException e) { e.printStackTrace(); } return null;
	 * 
	 * }
	 */

	public <E extends Resource> E createResourceWithID(int ID, Class<E> type) {
		try {
			Resource res = (Resource) Class.forName(type.getName()).getConstructor().newInstance();
			for (Field field : res.getClass().getDeclaredFields()) {
				field.setAccessible(true);
				System.out.println("Field: " + field);
				System.out.println("Field Type: " + field.getType());
	
				field.set(res, "test");
				System.out.println("Field Value: " + field.get(res));
			}
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
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
