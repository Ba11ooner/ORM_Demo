import entity.User;
import util.JDBC;
import util.Parser;

public class Example {
    public static void main(String[] args) throws Exception {
        User user = new User(1, "user01");
        User user1 = new User(2, "user02");
        String createTable = Parser.createTable(user);
        String update = Parser.update(user, user1);
        String delete = Parser.delete(user);
        String insert = Parser.insert(user);
        String select =  Parser.select(user);

        //通过 JDBC 可以实现对数据库的操作
        // String sql;
        // JDBC.request(sql);
    }
}
