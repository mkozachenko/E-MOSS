package SQL;

/**
 * Created by symph on 31.05.2017.
 */

import java.sql.*;

public class updateSQL {
    public static Object rs;
    public static boolean query;


    private void onlineUpdate (){

        return;
    }
    public static void localUpdate(String name, String radius, String distance, String orbspeed, String rotspeed, String tilt, String texture, String origin) throws SQLException {
        Connection con = null;
        switch (origin){
            case "planet":
                try {
                    Class.forName("org.h2.Driver");
                    //create database on memory
                    con = DriverManager.getConnection("jdbc:h2:./data/DB/sol", "application", "abc");
                    System.out.println("dbConnect//Connected to H2 - database name: sol");
                    Statement st = con.createStatement(); // Готовим запрос
                    st.execute("INSERT INTO planets (`name`,`radius`, `distance`, `orb_speed`, `rot_speed`, `tilt`, `texture`) VALUES ('"
                            +name+"','"+radius+"','"+distance+"','"+orbspeed+"','"+rotspeed+"','"+tilt+"','"+texture+"')");
                    con.close(); // closing the connection
                    System.out.println("Disconnected from H2 local DB");
                } catch (Exception e) {
                    System.out.println("Unable to connect");
                    e.printStackTrace();
                } finally {
                    // connection should be closed to release the resource
                    if (con != null){con.close();System.out.println("Connection closed");}
                }
                break;
				/*
			case "moon":
				try {
					Class.forName("org.h2.Driver");
					//create database on memory
					con = DriverManager.getConnection("jdbc:h2:./data/DB/sol", "sa", "");
					System.out.println("dbConnect//Connected to H2 - database name: sol");
					Statement st = con.createStatement(); // Готовим запрос
					query = st.execute("INSERT INTO builder_db (`name`,`radius`, `distance`, `orbspeed`, `rotspeed`, `tilt`,`parent`, `texture`) "
							+ "VALUES ('"+name+"','"+radius+"','"+distance+"','"+orbspeed+"','"+rotspeed+"','"+tilt+"','"+texture+"')");
					 con.close(); // closing the connection
					System.out.println("Disconnected from H2 local DB");
				} catch (Exception e) {
					System.out.println("Unable to connect");
					e.printStackTrace();
				} finally {
					// connection should be closed to release the resource
					if (con != null){con.close();System.out.println("Connection closed");}
				}
				break;*/
            default:
                System.out.println("what is the origin?");
                break;

        }
    }
}
