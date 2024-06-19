package entities;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

// Entity class representing a user in the todolist_users table
@Entity
@Table(name = "todolist_users")
@NamedQuery(name="UserEntity.findAll", query="SELECT u FROM UserEntity u")
public class UserEntity implements Serializable {
    // Primary key representing the user ID
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long user_id;
    
    // Username of the user
    private String username;
    
    // Password of the user
    private String password;

    // Default constructor
    public UserEntity() {
        super();
    }

    // Constructor with username and password parameters
//    public UserEntity(String username, String password) {
//        super();
//        this.username = username;
//        this.password = password;
//    }

    // Getter and setter methods for all fields

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
