package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

/**
 * 用于执行 SQL 语句，一次只能执行一句
 * 用到 JDBC，为方便进行包管理，引入 Maven
 */
public class JDBC {
    public static void request(String sql) throws Exception {
        //1、导入驱动jar包
        //2、注册驱动
        Class.forName("com.mysql.cj.jdbc.Driver");

        //3、获取数据库的连接对象
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/orm-demo", "root", "123456");

        //4、定义sql语句，SQL 语句源于 Parser

        //5、获取执行sql语句的对象
        Statement stat = con.createStatement();

        //6、执行sql并接收返回结果
        int count = stat.executeUpdate(sql);

        //7、处理结果
        System.out.println(count);

        //8、释放资源
        stat.close();
        con.close();
    }
}
