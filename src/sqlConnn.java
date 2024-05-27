import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

public class sqlConnn {
    public static void main(String[] args) throws Exception{
        String passWord;
        System.out.println("Enter PassWord: ");
        Scanner in = new Scanner(System.in);
        System.out.println("Enter userName: ");
        String userName = in.nextLine();
        passWord = in.nextLine();
        Connection con = DriverManager.getConnection(
                "jdbc:mysql://127.0.0.1:3306/linked-in"
                ,userName
                ,passWord
        );
        Statement statement = con.createStatement();
        statement.execute("insert into users values (9, 'lavid')");
        ResultSet resultSet = statement.executeQuery("select * from users");
        while (resultSet.next()) {
            System.out.println(resultSet.getInt(1));
            System.out.println(resultSet.getString(2));
        }
        con.close();
    }
}
