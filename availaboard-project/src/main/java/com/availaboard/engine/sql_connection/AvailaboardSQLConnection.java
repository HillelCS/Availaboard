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
	 * Third attempt at this method. 
	 * @param ID is the ResourceID
	 * @param type is the object being passed in
	 * 
	 * This method creates a new instance of the class being passed in by using a plain Resource
	 * object and constructing a new instance of the object being passed in with class.forName. The E
	 * variable requires the object to be an instance of the Resource so it is guaranteed to be fine.
	 * Then, the method iterates through each field of the class being passed in. It selects the column
	 * value in the database that has the same name as the field in the class. It then set's the field to 
	 * the value in the database of that ResourceID. Finally, it sets the status and name values. 
	 * The Resource is casted to (E) and returned. 
	 * 
	 */
	private <E extends Resource> E createResourceWithID(int ID, Class<E> type) {
		try {
			Resource res = (Resource) Class.forName(type.getName()).getConstructor().newInstance();
			for (Field field : res.getClass().getDeclaredFields()) {
				field.setAccessible(true);
				String query = "select ? from " + type.getSimpleName();
				final Connection con = DriverManager.getConnection(this.url, this.username, this.password);
				PreparedStatement st = con.prepareStatement(query);
				st.setString(1, type.getName());
				ResultSet rs = st.executeQuery();

				if (rs.next()) {
					field.set(res, rs.getObject(1));
				}

				query = "select status, name from resource where ResourceID = ?";
				st = con.prepareStatement(query);
				st.setInt(1, ID);
				rs = st.executeQuery();
				if (rs.next()) {
					res.setStatus(Status.valueOf(rs.getString(1)));
					res.setName(rs.getString(2));
				}

			}
			return (E) res;
		} catch (IllegalArgumentException | SQLException | IllegalAccessException | InstantiationException
				| InvocationTargetException | NoSuchMethodException | SecurityException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}
}
