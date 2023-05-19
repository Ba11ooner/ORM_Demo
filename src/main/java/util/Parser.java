package util;

import annotations.Column;
import annotations.Table;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Parser 用于生成 SQL 语句
 */
public class Parser {
    public static String createTable(Object obj) {
        //创建数据库表需要提供
        //1.表名
        /**
         * 从 Object 中获取 Class (反射机制)
         * 再从 Class 中获取 自定义注解对象 Table (注解机制)
         *  Table 注解的作用是提供表名
         */
        Table table = obj.getClass().getAnnotation(Table.class);
        String tableName = table.value();
        //2.属性名称
        //3.属性约束

        // 语句约束
        String constraints = "";

        /**
         * 从 Object 中获取 Class (反射机制)
         * 再从 Class 中获取 Field 数据
         * Column 注解的作用是提供属性相关信息（属性名称、属性约束）
         */

        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {
            Column column = field.getAnnotation(Column.class);
            constraints += "`" + column.value() + "` " + column.type() + "(" + column.length() + ") ";
            if (column.primaryKey() == true)
                constraints += "PRIMARY KEY ";
            if (column.unique() == true)
                constraints += "UNIQUE ";
            if (column.allowNull() == false)
                constraints += "NOT NULL ";
            constraints += ",";
        }

        //TODO 作用域是 FIELD 的注解，直接取取不到 → 空指针异常
        //Column column  =obj.getClass().getAnnotation(Column.class);
        //System.out.println(column.value());

        // SQL 语句
        StringBuffer SQL = new StringBuffer();
        //TODO JDBC 一次只能执行一条语句
        //SQL.append("DROP TABLE IF EXISTS `" + tableName + "`;\n");
        SQL.append("CREATE TABLE `" + tableName + "`(");
        SQL.append(constraints.substring(0, constraints.length() - 2));//最后会多一个 , 去除多余的 ,
        SQL.append(");");

        System.out.println(SQL.toString());
        return SQL.toString();
    }

    public static String update(Object oldObj, Object newObj) throws Exception {
        //System.out.println(oldObj.toString());
        //System.out.println(newObj.toString());
        // UPDATE 表名称 SET 列名称 = 新值 WHERE 列名称 = 某值
        //更新数据需要
        //1.表名称
        Table table = oldObj.getClass().getAnnotation(Table.class);
        String tableName = table.value();
        //2.列名称
        Field[] fields = oldObj.getClass().getDeclaredFields();
        List<String> attributes = new ArrayList<>();
        for (Field field : fields) {
            field.setAccessible(true);
            Column column = field.getAnnotation(Column.class);
            attributes.add(column.value());
        }
        //3.新值
        //System.out.println("get new values");
        List<String> newValues = new ArrayList<>();
        Field[] fieldsOfNewObj = newObj.getClass().getDeclaredFields();
        for (Field field : fieldsOfNewObj) {
            field.setAccessible(true);
            //System.out.println(field.get(newObj).toString());
            if (field.getType() == String.class) {
                newValues.add("'" + field.get(newObj).toString() + "'");
            } else {
                newValues.add(field.get(newObj).toString());
            }

        }
        //4.某值
        //System.out.println("get old value");
        List<String> oldValues = new ArrayList<>();
        for (Field field : fields) {
            field.setAccessible(true);
            //System.out.println(field.get(oldObj).toString());
            if (field.getType() == String.class) {
                oldValues.add("'" + field.get(oldObj).toString() + "'");
            } else {
                oldValues.add(field.get(oldObj).toString());
            }
        }

        // SQL 构建
        // UPDATE 表名称 SET 列名称 = 新值 WHERE 列名称 = 某值
        StringBuffer sql = new StringBuffer();
        sql.append("UPDATE ").append(tableName).append(" SET ");
        for (int i = 0; i < attributes.size(); i++) {
            sql.append(attributes.get(i)).append(" = ").append(newValues.get(i)).append(", ");
        }
        sql.deleteCharAt(sql.length() - 2);
        sql.append(" WHERE ");
        sql.append(attributes.get(0)).append(" = ").append(oldValues.get(0));
        if (attributes.size() > 1) {
            for (int i = 1; i < attributes.size(); i++) {
                sql.append(" AND ").append(attributes.get(i)).append(" = ").append(oldValues.get(i)).append(", ");
            }
        }
        sql.deleteCharAt(sql.length() - 2);
        System.out.println(sql);
        return sql.toString();
    }

    public static String delete(Object obj) throws Exception {
        //DELETE FROM 表名称 WHERE 列名称 = 值
        //删除数据需要
        //1.表名称
        Table table = obj.getClass().getAnnotation(Table.class);
        String tableName = table.value();
        //2.列名称
        //3.值
        Field[] fields = obj.getClass().getDeclaredFields();
        List<String> attributes = new ArrayList<>();
        List<String> values = new ArrayList<>();
        for (Field field : fields) {
            field.setAccessible(true);
            Column column = field.getAnnotation(Column.class);
            attributes.add(column.value());
            if (field.getType() == String.class) {
                values.add("'" + field.get(obj).toString() + "'");
            } else {
                values.add(field.get(obj).toString());
            }

        }
        //SQL
        //DELETE FROM 表名称 WHERE 列名称 = 值
        StringBuffer sql = new StringBuffer();
        sql.append("DELETE ").append("FROM ").append(tableName).append(" WHERE ");
        sql.append(attributes.get(0)).append(" = ").append(values.get(0));
        if (attributes.size() > 1) {
            for (int i = 1; i < attributes.size(); i++) {
                sql.append(" AND ").append(attributes.get(i)).append(" = ").append(values.get(i));
            }
        }
        System.out.println(sql);
        return sql.toString();
    }

    public static String insert(Object obj) throws Exception {
        //INSERT INTO 表名称 VALUES (值1, 值2,....)
        //插入数据需要
        //1.表名称
        Table table = obj.getClass().getAnnotation(Table.class);
        String tableName = table.value();
        //2.属性值
        Field[] fields = obj.getClass().getDeclaredFields();
        List<String> values = new ArrayList<>();
        for (Field field : fields) {
            field.setAccessible(true);
            if (field.getType() == String.class) {
                values.add("'" + field.get(obj) + "'");
            } else {
                values.add(field.get(obj).toString());
            }
        }
        //SQL
        //INSERT INTO 表名称 VALUES (值1, 值2,....)
        StringBuffer sql = new StringBuffer();
        sql.append("INSERT INTO ").append(tableName).append(" VALUES (");
        for (int i = 0; i < values.size(); i++) {
            sql.append(values.get(i)).append(",");
        }
        sql.deleteCharAt(sql.length() - 1);
        sql.append(")");
        System.out.println(sql);
        return sql.toString();
    }

    public static String select(Object obj){
        //SELECT * FROM 表名称
        //查找只需要表名
        Table table = obj.getClass().getAnnotation(Table.class);
        String tableName = table.value();
        //SQL
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT * FROM ").append(tableName);
        System.out.println(sql);
        return sql.toString();
    }
}
