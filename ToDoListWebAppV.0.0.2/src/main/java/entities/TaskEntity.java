package entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

// Entity class representing a task in the todolist_task table
@Entity
@Table(name = "todolist_task")
@NamedQuery(name="TaskEntity.findAll", query="SELECT t FROM TaskEntity t")
public class TaskEntity {
    // Primary key representing the task ID
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long task_id;
    
    // Description of the task
    private String description;
    
    // Start time of the task
    private String fromTime; 
    
    // End time of the task
    private String toTime; 
    
    // Status of the task
    private String status;
    
    // Many-to-one mapping with UserEntity, representing the user associated with this task
    @ManyToOne
    @JoinColumn(name = "userId")
    private UserEntity user;

    // Default constructor
    public TaskEntity() {
        super();
    }

    // Constructor with description, fromTime, toTime, and status parameters
    public TaskEntity(String description, String fromTime, String toTime, String status) {
        super();
        this.description = description;
        this.fromTime = fromTime;
        this.toTime = toTime;
        this.status = status;
    }

    // Getter and setter methods for all fields

    public long getTask_id() {
        return task_id;
    }

    public void setTask_id(long task_id) {
        this.task_id = task_id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFromTime() {
        return fromTime;
    }

    public void setFromTime(String fromTime) {
        this.fromTime = fromTime;
    }

    public String getToTime() {
        return toTime;
    }

    public void setToTime(String toTime) {
        this.toTime = toTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }
}
