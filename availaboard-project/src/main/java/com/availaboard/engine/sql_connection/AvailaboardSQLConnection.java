package com.availaboard.engine.sql_connection;

import com.availaboard.engine.resource.Resource;
import com.availaboard.engine.resource.Status;
import com.availaboard.engine.resource.User;
import com.availaboard.utilitys.ConfigPropReader;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Stream;

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
    public void authenticate(final String username, final String password) throws InvalidCredentialsException {
        try {
            final Connection con = DriverManager.getConnection(AvailaboardSQLConnection.url,
                    AvailaboardSQLConnection.username, AvailaboardSQLConnection.password);

            final String query = "SELECT COUNT(1) FROM user WHERE username = ? and password = ?;";
            final PreparedStatement st = con.prepareStatement(query);
            st.setString(1, username);
            st.setString(2, password);
            final ResultSet rs = st.executeQuery();

            if ((rs.next() && ((rs.getInt(1) != 1)))) {
                throw new InvalidCredentialsException();
            }
            st.close();
            rs.close();
            con.close();
        } catch (final SQLException | SecurityException e) {
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
    public <E extends Resource> E createResourceWithID(final int ID, final Class<E> type) {
        try {
            final Connection con = DriverManager.getConnection(AvailaboardSQLConnection.url,
                    AvailaboardSQLConnection.username, AvailaboardSQLConnection.password);

            final Resource res = (Resource) Class.forName(type.getName()).getConstructor().newInstance();
            res.setId(ID);
            final Table table = res.getClass().getAnnotation(Table.class);
            for (final Field field : res.getClass().getDeclaredFields()) {
                if ((field.isAnnotationPresent(Column.class))) {
                    field.setAccessible(true);
                    final Column column = field.getAnnotation(Column.class);
                    final String query = String.format("select %s from %s where ResourceID = ?", column.value(),
                            table.value());
                    final PreparedStatement st = con.prepareStatement(query);
                    st.setInt(1, ID);
                    final ResultSet rs = st.executeQuery();

                    if (rs.next()) {
                        setType(res, rs.getString(1), field);
                    }
                }
            }
            final String query = "select status, name from resource where ResourceID = ?";
            final PreparedStatement st = con.prepareStatement(query);
            st.setInt(1, ID);
            final ResultSet rs = st.executeQuery();
            if (rs.next()) {
                res.setStatus(Status.valueOf(rs.getString(1)));
                res.setName(rs.getString(2));
            }
            st.close();
            rs.close();
            con.close();
            return (E) res;
        } catch (final IllegalArgumentException | SQLException | IllegalAccessException | InstantiationException
                       | InvocationTargetException | NoSuchMethodException | SecurityException |
                       ClassNotFoundException e) {
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
    public User createUserWithUsername(final String username) {
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
    private int getResourceIDFromUsername(final String username) {
        try {
            final Connection con = DriverManager.getConnection(AvailaboardSQLConnection.url,
                    AvailaboardSQLConnection.username, AvailaboardSQLConnection.password);

            final String query = "SELECT ResourceID FROM user WHERE username = ?";
            final PreparedStatement st = con.prepareStatement(query);
            st.setString(1, username);
            final ResultSet rs = st.executeQuery();

            if (rs.next()) {
                return rs.getInt(1);
            }
            st.close();
            rs.close();
            con.close();
        } catch (final SQLException e) {
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
    public <E extends Resource> Collection<E> loadResources(final Class<E> type) {
        final ArrayList<E> arr = new ArrayList<>();
        final Table table = type.getAnnotation(Table.class);
        try {
            final Connection con = DriverManager.getConnection(AvailaboardSQLConnection.url,
                    AvailaboardSQLConnection.username, AvailaboardSQLConnection.password);

            final String query = "select ResourceID from " + table.value();
            final PreparedStatement st = con.prepareStatement(query);
            final ResultSet rs = st.executeQuery();

            while (rs.next()) {
                arr.add(createResourceWithID(rs.getInt(1), type));
            }
            st.close();
            con.close();
            rs.close();
        } catch (final SQLException e) {
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

    public void setType(final Resource res, final String value, final Field field) {
        try {
            if ((field.getType() instanceof Class) && field.getType().isEnum()) {
                field.set(res, Enum.valueOf((Class<Enum>) field.getType(), value));
            } else {
                field.set(res, value);
            }

        } catch (final IllegalArgumentException | IllegalAccessException e) {
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
    public void updateFieldToDatabase(final Resource res, final Field field) {
        final Table table = res.getClass().getAnnotation(Table.class);
        final Column column = field.getAnnotation(Column.class);
        try {
            final Connection con = DriverManager.getConnection(AvailaboardSQLConnection.url,
                    AvailaboardSQLConnection.username, AvailaboardSQLConnection.password);
            final String query = String.format("select %s from %s where ResourceID = ?", column.value(), table.value());

            final PreparedStatement st = con.prepareStatement(query);
            st.setInt(1, res.getId());
            final ResultSet rs = st.executeQuery();

            if (rs.next()) {
                setType(res, rs.getString(1), field);
            }
            st.close();
            rs.close();
            con.close();
        } catch (final IllegalArgumentException | SQLException | SecurityException e) {
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
    public void updateRowInDatabase(final Resource res, final Field field) {
        final Table table = res.getClass().getAnnotation(Table.class);
        final Column column = field.getAnnotation(Column.class);
        try {
            final Connection con = DriverManager.getConnection(AvailaboardSQLConnection.url,
                    AvailaboardSQLConnection.username, AvailaboardSQLConnection.password);
            final String query = String.format("UPDATE %s SET %s = ? WHERE (`ResourceID` = ?);", table.value(),
                    column.value());

            final PreparedStatement st = con.prepareStatement(query);
            st.setString(1, field.get(res).toString());
            st.setInt(2, res.getId());
            st.executeUpdate();
            st.close();
            con.close();
        } catch (final IllegalArgumentException | SQLException | IllegalAccessException | SecurityException e) {
            e.printStackTrace();
        }
    }

    /**
     * Checks if a <code>Username</code> exists in the database.
     *
     * @param username The <code>username</code> being against the database for if
     *                 it exists or not.
     */
    public boolean doesUsernameExist(final String username) {
        try {
            final Connection con = DriverManager.getConnection(AvailaboardSQLConnection.url,
                    AvailaboardSQLConnection.username, AvailaboardSQLConnection.password);

            // Uses alias to store the count as variable
            final String query = "SELECT COUNT(1) AS total FROM user WHERE username = ?;";
            final PreparedStatement st = con.prepareStatement(query);
            st.setString(1, username);
            final ResultSet rs = st.executeQuery();
            if (rs.next() && rs.getInt("total") == 1) {
                return true;
            }

            st.close();
            rs.close();
            con.close();
        } catch (final SQLException | SecurityException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean doesIDExist(final int ResourceID) {
        try {
            final Connection con = DriverManager.getConnection(AvailaboardSQLConnection.url,
                    AvailaboardSQLConnection.username, AvailaboardSQLConnection.password);

            // Uses alias to store the count as variable
            final String query = "SELECT COUNT(1) AS total FROM Resource WHERE ResourceID = ?;";
            final PreparedStatement st = con.prepareStatement(query);
            st.setInt(1, ResourceID);
            final ResultSet rs = st.executeQuery();
            if (rs.next() && rs.getInt("total") == 1) {
                return true;
            }

            st.close();
            rs.close();
            con.close();
        } catch (final SQLException | SecurityException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Inserts a {@link Resource} subclass into the database.
     *
     * @param res The {@link Resource} that is being inserted into the database.
     * @throws NameExistsException
     */
    public void insertResourceIntoDatabase(final Resource res) throws NameExistsException {
        int key = 0;
        try {
            if (doesUsernameExist(res.getName())) {
                throw new NameExistsException();
            } else {
                final Connection con = DriverManager.getConnection(AvailaboardSQLConnection.url,
                        AvailaboardSQLConnection.username, AvailaboardSQLConnection.password);
                final Table table = res.getClass().getAnnotation(Table.class);

                String query = String.format("INSERT INTO `availaboard`.`resource` (`name`, `status`) VALUES (?, ?);");
                PreparedStatement st = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
                st.setString(1, res.getName());
                st.setString(2, res.getStatus().toString());
                st.executeUpdate();
                final ResultSet rs = st.getGeneratedKeys();

                if (rs.next()) {
                    key = rs.getInt(1);
                }

                query = String.format("INSERT INTO %s (ResourceID) VALUES (?)", table.value());

                st = con.prepareStatement(query);
                st.setInt(1, key);
                st.executeUpdate();

                res.setId(key);

                for (final Field field : res.getClass().getDeclaredFields()) {
                    if ((field.isAnnotationPresent(Column.class))) {
                        field.setAccessible(true);
                        updateRowInDatabase(res, field);
                    }
                }
                con.close();
            }
        } catch (final IllegalArgumentException | SQLException | SecurityException e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates the Resource Table columns to the corresponding values in the
     * {@link Resource} Object being passed in.
     *
     * @param res The {@link Resource} object being passed in who's values are being
     *            used to update the values in the database.
     */
    public void updateResourceTable(Resource res) {
        try {
            final Connection con = DriverManager.getConnection(AvailaboardSQLConnection.url,
                    AvailaboardSQLConnection.username, AvailaboardSQLConnection.password);
            final String query = "UPDATE Resource SET Status = ?, Name = ? WHERE (`ResourceID` = ?);";
            final PreparedStatement st = con.prepareStatement(query);
            st.setString(1, String.valueOf(res.getStatus()));
            st.setString(2, res.getName());
            st.setInt(3, res.getId());

            st.executeUpdate();
            st.close();
            con.close();
        } catch (final SQLException | SecurityException e) {
            e.printStackTrace();
        }
    }

    /**
     * Deletes a Resource from the database.
     *
     * @param res The Resource that is being deleted.
     */
    public void deleteResourceFromDatabase(Resource res) throws ResourceDoesNotExistException {
        if (!doesIDExist(res.getId())) {
            throw new ResourceDoesNotExistException();
        } else {
            try {
                final Connection con = DriverManager.getConnection(AvailaboardSQLConnection.url,
                        AvailaboardSQLConnection.username, AvailaboardSQLConnection.password);
                final Table table = res.getClass().getAnnotation(Table.class);
                String query = String.format("DELETE FROM `availaboard`.`%s` WHERE (`ResourceID` = ?);", table.value());
                PreparedStatement st = con.prepareStatement(query);
                st.setString(1, String.valueOf(res.getId()));
                st.executeUpdate();

                query = "DELETE FROM `availaboard`.`Resource` WHERE (`ResourceID` = ?);";
                st = con.prepareStatement(query);
                st.setString(1, String.valueOf(res.getId()));
                st.executeUpdate();

                st.close();
                con.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Updates every column in the database to the corresponding <code>Field</code> in the {@link Resource}.
     *
     * @param res The {@link Resource} used to update the Columns in the database.
     */
    public void updateResourceInDatabase(final Resource res) throws NameExistsException {
        if (!res.getName().equals(createResourceWithID(res.getId(), res.getClass()).getName()) && doesUsernameExist(res.getName())) {
            throw new NameExistsException();
        } else {
            updateResourceTable(res);
            Stream<Field> stream = Stream.of(res.getClass().getDeclaredFields());
            stream.forEach(field -> {
                if (field.isAnnotationPresent(Column.class)) {
                    field.setAccessible(true);
                    updateRowInDatabase(res, field);
                }
            });
        }

    }
}
