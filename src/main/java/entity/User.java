package entity;

import annotations.Column;
import annotations.Table;

@Table("User")
public class User {
    public User(){

    }
    public User(int id, String name){
        this.id = id;
        this.name = name;
    }
    @Column(value = "id", type = "int", length = 11,
            primaryKey = true, allowNull = false, unique = true)
    private int id;

    @Column(value = "name", type = "varchar", length = 20)
    private String name;

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
