package com.availaboard.engine.sql_connection;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;

import com.availaboard.engine.resource.Resource;
import com.availaboard.engine.resource.Status;
import com.availaboard.engine.resource.User;
import com.availaboard.engine.security.AccessControl;
import com.availaboard.engine.security.AccessControlFactory;
import com.availaboard.utilitys.ConfigPropReader;

/**
 * Connects to the Availaboard database. Uses the {@link ConfigPropReader} to
 * get the information needed to connect.
 */
public class AvailaboardSQLConnection {

    private static final String username = ConfigPropReader.getPropValues().get(0).replaceAll("^\"|\"$", "");
    private static final String password = ConfigPropReader.getPropValues().get(1).replaceAll("^\"|\"$", "");
    private static final String url = ConfigPropReader.getPropValues().get(2).replaceAll("^\"|\"$", "");

    /**
     * Check's if a <code>username</code> and <code> password </code> are valid. If
     * they are not the method throws a {@link InvalidCredentialsException}.
     *
     * @param username Passed into database to check if exists.
     * @param password Passed into database to check if exists.
     * @throws InvalidCredentialsException Thrown if both a <code>username</code>
     *                                     and <code> password </code> don't exist
     *                                     in the same row.
     */
    public void authenticate(String username, String password) throws InvalidCredentialsException {
        try {
            Connection con = DriverManager.getConnection(AvailaboardSQLConnection.url,
                    AvailaboardSQLConnection.username, AvailaboardSQLConnection.password);

            String query = "SELECT COUNT(1) FROM user WHERE username = ? and password = ?;";
            PreparedStatement st = con.prepareStatement(query);
            st.setString(1, username);
            st.setString(2, password);
            ResultSet rs = st.executeQuery();

            if ((rs.next() && ((rs.getInt(1) != 1)))) {
                throw new InvalidCredentialsException();
            }
            st.close();
            rs.close();
            con.close();
        } catch (SQLException | SecurityException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates and returns a {@link Resource} Object based off a <code>Class</code>
     * and a <code>ResourceID</code>. The Method iterates through every field of the
     * class reflectively and sets each of them to the value of a column with an
     * identical name. It then manually sets the {@link Status} and
     * <code>Name</code> of the {@link Resource} .
     *
     * @param <E>  The type of Object that the method is grabbing and returning from
     *             the database.
     * @param ID   The ID used to identify which <code>E</code> should be grabbed
     *             from the database.
     * @param type The type of Object that the method is grabbing and returning from
     *             the database.
     * @return A <code>E</code> Object with every field set from the database.
     */
    public <E extends Resource> E createResourceWithID(int ID, Class<E> type) {
        try {
            Connection con = DriverManager.getConnection(AvailaboardSQLConnection.url,
                    AvailaboardSQLConnection.username, AvailaboardSQLConnection.password);

            Resource res = (Resource) Class.forName(type.getName()).getConstructor().newInstance();
            res.setId(ID);
            final Table table = res.getClass().getAnnotation(Table.class);
            for (Field field : res.getClass().getDeclaredFields()) {
                if ((field.isAnnotationPresent(Column.class))) {
                    field.setAccessible(true);
                    final Column column = field.getAnnotation(Column.class);
                    String query = String.format("select %s from %s where ResourceID = ?", column.value(),
                            table.value());
                    PreparedStatement st = con.prepareStatement(query);
                    st.setInt(1, ID);
                    ResultSet rs = st.executeQuery();

                    if (rs.next()) {
                        setType(res, rs.getString(1), field);
                    }
                }
            }
            String query = "select status, name from resource where ResourceID = ?";
            PreparedStatement st = con.prepareStatement(query);
            st.setInt(1, ID);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                res.setStatus(Status.valueOf(rs.getString(1)));
                res.setName(rs.getString(2));
            }
            st.close();
            rs.close();
            con.close();
            return (E) res;
        } catch (IllegalArgumentException | SQLException | IllegalAccessException | InstantiationException
                 | InvocationTargetException | NoSuchMethodException | SecurityException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Creates a {@link User} from a <code>Username</code>. Uses
     * {@link #createResourceWithID} and {@link #getResourceIDFromUsername} to
     * create it.
     *
     * @param username used to create the {@link User} Object.
     * @return A new {@link User}.
     */
    public User createUserWithUsername(String username) {
        return createResourceWithID(getResourceIDFromUsername(username), User.class);
    }

    /**
     * Gets a <code>ResourceID</code> from the database with a
     * <code>Username</code>.
     *
     * @param username The <code>Username</code> used to find the
     *                 <code>ResourceID</code>.
     * @return The <code>ResourceID</code> of a <code>Resource</code>.
     */
    private int getResourceIDFromUsername(String username) {
        try {
            Connection con = DriverManager.getConnection(AvailaboardSQLConnection.url,
                    AvailaboardSQLConnection.username, AvailaboardSQLConnection.password);

            String query = "SELECT ResourceID FROM user WHERE username = ?";
            PreparedStatement st = con.prepareStatement(query);
            st.setString(1, username);
            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                return rs.getInt(1);
            }
            st.close();
            rs.close();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Loads all {@link Resource} in the database of a given type (<code>E</code>).
     * Iterates through every <code>ResourceID</code> in the <code>E</code> table
     * and creates an <code>E</code> object from that ID. It then adds all of them
     * to an {@link ArrayList} and returns.
     *
     * @param <E>  The type of Object that the method is grabbing and returning from
     *             the database.
     * @param type The type of Object that the method is grabbing and returning from
     *             the database.
     * @return An {@link ArrayList} of the type <code>E</code> loaded from the
     * database.
     */
    public <E extends Resource> Collection<E> loadResources(Class<E> type) {
        ArrayList<E> arr = new ArrayList<>();
        final Table table = type.getAnnotation(Table.class);
        try {
            Connection con = DriverManager.getConnection(AvailaboardSQLConnection.url,
                    AvailaboardSQLConnection.username, AvailaboardSQLConnection.password);

            String query = "select ResourceID from " + table.value();
            PreparedStatement st = con.prepareStatement(query);
            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                arr.add(createResourceWithID(rs.getInt(1), type));
            }
            st.close();
            con.close();
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return arr;
    }

    /**
     * Sets the value of a <code>Field</code>. The reason there is a Method for this
     * action is because since the values are being set reflectively if the value is
     * an <code>Enum</code> then the program breaks down because an
     * <code>Enum</code> value can not be set by a <code>String</code> without
     * <code>Enum.valueOf</code>
     *
     * @param res   The {@link Resource} Object that contains the <code>Field</code>
     *              that will be set.
     * @param value The value that the <code>Field</code> will be set to.
     * @param field The <code>Field</code> that the Method will set.
     */

    public void setType(Resource res, String value, Field field) {
        try {
            if ((field.getType() instanceof Class) && ((Class<?>) field.getType()).isEnum()) {
                field.set(res, Enum.valueOf((Class<Enum>) field.getType(), value));
            } else {
                field.set(res, value);
            }

        } catch (IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates a <code>Field</code> in a {@link Resource} object to the
     * corresponding column in the database.
     *
     * @param res   The {@link Resource} that will be updated.
     * @param field The <code>Field</code> that will be updated.
     */
    public void updateFieldToDatabase(Resource res, Field field) {
        final Table table = res.getClass().getAnnotation(Table.class);
        final Column column = field.getAnnotation(Column.class);
        try {
            Connection con = DriverManager.getConnection(AvailaboardSQLConnection.url,
                    AvailaboardSQLConnection.username, AvailaboardSQLConnection.password);
            String query = String.format("select %s from %s where ResourceID = ?", column.value(), table.value());

            PreparedStatement st = con.prepareStatement(query);
            st.setInt(1, res.getId());
            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                setType(res, rs.getString(1), field);
            }
            st.close();
            rs.close();
            con.close();
        } catch (IllegalArgumentException | SQLException | SecurityException e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates a column in the database to the corresponding <code>Field</code> in
     * the {@link Resource}.
     *
     * @param res   The {@link Resource} that has a value in a <code>Field</code>
     *              that will be used to update the database.
     * @param field A <code>Field</code> that has a value that will be used to
     *              update the database.
     */
    public void updateRowInDatabase(Resource res, Field field) {
        final Table table = res.getClass().getAnnotation(Table.class);
        final Column column = field.getAnnotation(Column.class);
        try {
            Connection con = DriverManager.getConnection(AvailaboardSQLConnection.url,
                    AvailaboardSQLConnection.username, AvailaboardSQLConnection.password);
            String query = String.format("UPDATE %s SET %s = ? WHERE (`ResourceID` = ?);", table.value(),
                    column.value());

            PreparedStatement st = con.prepareStatement(query);
            st.setString(1, field.get(res).toString());
            st.setInt(2, res.getId());
            st.executeUpdate();
            st.close();
            con.close();
        } catch (SQLIntegrityConstraintViolationException | IllegalArgumentException | SQLException | IllegalAccessException | SecurityException e) {
            e.printStackTrace();
        }
    }

    /**
     * Checks if a <code>Username</code> exists in the database.
     *
     * @param username The <code>username</code> being against the database for if
     *                 it exists or not.
     */
    public boolean doesUsernameExist(String username) {
        try {
            Connection con = DriverManager.getConnection(AvailaboardSQLConnection.url,
                    AvailaboardSQLConnection.username, AvailaboardSQLConnection.password);

            String query = "SELECT COUNT(1) FROM user WHERE username = ?;";
            PreparedStatement st = con.prepareStatement(query);
            st.setString(1, username);
            ResultSet rs = st.executeQuery();

            if (rs.next() && (rs.getInt(1) == 1)) {
                return true;
            }
            st.close();
            rs.close();
            con.close();
        } catch (SQLException | SecurityException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Inserts a {@link Resource} subclass into the database.
     *
     * @param res The {@link Resource} that is being inserted into the database.
     */
    public void insertResourceIntoDatabase(Resource res) throws UsernameExistsException {
        int key = 0;
        try {
            if (doesUsernameExist(username)) {
                throw new UsernameExistsException();
            }
            Connection con = DriverManager.getConnection(AvailaboardSQLConnection.url,
                    AvailaboardSQLConnection.username, AvailaboardSQLConnection.password);
            final Table table = res.getClass().getAnnotation(Table.class);

            String query = String.format("INSERT INTO `availaboard`.`resource` (`name`, `status`) VALUES (?, ?);");
            PreparedStatement st = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            st.setString(1, res.getName());
            st.setString(2, res.getStatus().toString());
            st.executeUpdate();
            ResultSet rs = st.getGeneratedKeys();

            if (rs.next()) {
                key = rs.getInt(1);
            }

            query = String.format("INSERT INTO %s (ResourceID) VALUES (?)", table.value());

            st = con.prepareStatement(query);
            st.setInt(1, key);
            st.executeUpdate();

            res.setId(key);

            for (Field field : res.getClass().getDeclaredFields()) {
                if ((field.isAnnotationPresent(Column.class))) {
                    field.setAccessible(true);
                    updateRowInDatabase(res, field);
                }
            }

            con.close();
        } catch (IllegalArgumentException | SQLException | SecurityException e) {
            e.printStackTrace();
        }
    }
}
