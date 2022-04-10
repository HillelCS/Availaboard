package com.availaboard.engine.sql_connection;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import com.availaboard.engine.resource.FieldExcludedFromDatabase;
import com.availaboard.engine.resource.Resource;
import com.availaboard.engine.resource.Status;
import com.availaboard.engine.resource.User;
import com.availaboard.utilitys.ConfigPropReader;
import com.vaadin.flow.component.notification.Notification;

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
		ArrayList<E> arr = new ArrayList<>();
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
	 * Third attempt at this method.
	 *
	 * @param ID is the ResourceID
	 *
	 * @param type is the object being passed in
	 *
	 * This method creates a new instance of the class being passed in by using a
	 * plain Resource object and constructing a new instance of the object being
	 * passed in with class.forName. The E variable requires the object to be an
	 * instance of the Resource so it is guaranteed to be fine. Then, the method
	 * iterates through each field of the class being passed in. It selects the
	 * column value in the database that has the same name as the field in the
	 * class. It then set's the field to the value in the database of that
	 * ResourceID. Finally, it sets the status and name values. The Resource is
	 * casted to (E) and returned.
	 *
	 */
	@SuppressWarnings("unchecked")
	public <E extends Resource> E createResourceWithID(int ID, Class<E> type) {
		try {
			Resource res = (Resource) Class.forName(type.getName()).getConstructor().newInstance();
			res.setId(ID);

			final Connection con = DriverManager.getConnection(this.url, this.username, this.password);
			String query = "select status, name from resource where ResourceID = ?";
			PreparedStatement st = con.prepareStatement(query);
			st.setInt(1, ID);
			ResultSet rs = st.executeQuery();

			if (rs.next()) {
				res.setStatus(Status.valueOf(rs.getString(1)));
				res.setName(rs.getString(2));
			}

			for (Field field : res.getClass().getDeclaredFields()) {
				if (!(field.isAnnotationPresent(FieldExcludedFromDatabase.class))) {
					field.setAccessible(true);
					query = String.format("select %s from %s where ResourceID = ?", field.getName(),
							type.getSimpleName());

					st = con.prepareStatement(query);
					st.setInt(1, ID);
					rs = st.executeQuery();

					if (rs.next()) {
						setType(res, rs.getString(1), field);
					}

				}
			}
			con.close();
			return (E) res;
		} catch (IllegalArgumentException | SQLException | IllegalAccessException | InstantiationException
				| InvocationTargetException | NoSuchMethodException | SecurityException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void setType(Resource res, String value, Field field) {
		try {
			if (field.isEnumConstant()) {
				field.set(res, Enum.valueOf((Class<? extends Enum>) Class.forName(field.getType().getSimpleName()), value));
			} else {
				field.set(res, value);
			}

		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void authenticate(String username, String password) throws InvalidCredentialsException {
		try {
			String query = "SELECT COUNT(1) FROM user WHERE username = ? and password = ?;";
			Connection con = DriverManager.getConnection(this.url, this.username, this.password);
			PreparedStatement st = con.prepareStatement(query);
			st.setString(1, username);
			st.setString(2, password);
			ResultSet rs = st.executeQuery();

			if (rs.next()) {
				if (rs.getInt(1) == 1) {

				} else {

					throw new InvalidCredentialsException();
				}
			}
			con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
