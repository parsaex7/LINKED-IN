import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;
import java.util.Scanner;

public class sqlConnn {
    public static void main(String[] args) throws Exception{
        String passWord;
        System.out.println("Enter PassWord: ");
        Scanner in = new Scanner(System.in);
        passWord = in.nextLine();
        Connection con = DriverManager.getConnection(
                "jdbc:mysql://127.0.0.1:3306/linked-in"
                ,"root"
                ,passWord
        );
        Statement statement = con.createStatement();
        statement.execute("insert into users (firstname, lastname, email, password) values ('parsa', 'exir', 'pasffdssbh@gmail.com', 'passs')");
        ResultSet resultSet = statement.executeQuery("select * from users");
        while (resultSet.next()) {
            System.out.println(resultSet.getInt(1));
            System.out.println(resultSet.getString(2));
            System.out.println(resultSet.getString("lastname"));
            System.out.println(resultSet.getString("email"));
            System.out.println(resultSet.getString("password"));
        }
        con.close();
    }
}