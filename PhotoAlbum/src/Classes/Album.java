package Classes;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @Author Tin
 * Created by win8.1 on 08-Dec-15.
 */
public class Album {
    private String name;
    private String date;
    private int numberOfPhoto;
    private DbCon dbCon = new DbCon();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getNumberOfPhoto() {
        return numberOfPhoto;
    }

    public void setNumberOfPhoto(int numberOfPhoto) {
        try {
            dbCon.connect();
            String stm = "UPDATE AlbumInfo SET numberOfPhoto=" + numberOfPhoto + " WHERE AlbumName='" + name + "'";
            dbCon.updateSQL(stm);
            dbCon.close();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        this.numberOfPhoto = numberOfPhoto;
    }

    public Album(String albumName) throws SQLException, ClassNotFoundException {
        String stm = "SELECT * FROM AlbumInfo WHERE AlbumName='" + albumName + "'";
        dbCon.connect();
        try {
            ResultSet rs = dbCon.execSQL(stm);
            this.name = rs.getString(2);
            this.numberOfPhoto = rs.getInt(3);
            this.date = rs.getString(4);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        dbCon.close();
    }

    public static void main(String args[]) throws SQLException, ClassNotFoundException {
        Album album = new Album("Family");
        album.setNumberOfPhoto(4);
        System.out.println(album.getName() + " " + album.getDate() + " " + album.getNumberOfPhoto());

    }
}
