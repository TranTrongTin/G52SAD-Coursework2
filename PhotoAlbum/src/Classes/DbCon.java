package Classes;

import java.sql.*;

/**
 * @Author Tran Trong Tin
 * Created by win8.1 on 04-Dec-15.
 */
// this is the comment haha
public class DbCon {
    String connectionURL = "jdbc:sqlite:dbCon.db";
    private Connection conn = null;

    public DbCon() {
        super();
    }

    public void connect() throws ClassNotFoundException, SQLException {
        Class.forName("org.sqlite.JDBC");
        try {

            conn = DriverManager.getConnection(connectionURL);
            conn.setAutoCommit(false);
            Statement stm = conn.createStatement();
//            stm.executeUpdate("SET CHARACTER SET UTF8");
            stm.close();
            System.out.println("Connect successfully");
        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }

    public void close() throws SQLException {
        conn.close();
        System.out.println("Disconnected!");
    }


    public ResultSet execSQL(String sql) throws SQLException {
        Statement st = conn.createStatement();
        return (st.executeQuery(sql));
    }

    public void updateSQL(String sql) throws SQLException {
        Statement st = conn.createStatement();
        st.executeUpdate(sql);
    }

    public static void main(String args[]) {
        System.out.println("hello world");
        DbCon dbCon = new DbCon();
        try {
            dbCon.connect();
            String stm = "SELECT Username FROM UserInfo";
            ResultSet rs = dbCon.execSQL(stm);
            while (rs.next()) {
                System.out.println(rs.getString(1));
            }
            dbCon.close();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
