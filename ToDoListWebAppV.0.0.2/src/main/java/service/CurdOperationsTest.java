package service;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.Query;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;

import entities.TaskEntity;
import entities.UserEntity;

public class CurdOperationsTest extends CurdOperations {

	@Mock
	private SessionFactory sessionFactory;
	
	@Mock
	private Session session;
	
	@Mock
	private Transaction transaction;
	
	@Mock 
	private Criteria criteria;
	
	@Mock
    private Query<UserEntity> query;

	
	@InjectMocks
	private CurdOperations curdOperations;
	private MockMvc mockMvc;
	
	  @Before
	    public void setup() {
	        MockitoAnnotations.openMocks(this);
	        when(sessionFactory.openSession()).thenReturn(session);
	        when(session.beginTransaction()).thenReturn(transaction);
	        // Mocking the getTransaction method to return the mocked transaction
	        when(session.getTransaction()).thenReturn(transaction);
	    }
	// Test case for registering a user
	    @Test
	    public void testRegisterUser() {
	        UserEntity user = new UserEntity();
	        curdOperations.registerr(user);
	        verify(session).save(user); // Verifies if session.save was called with the user object
	        verify(transaction).commit(); // Verifies if transaction.commit was called
	    }
	    

	    // Test case for validating user credentials
	    @Test
	    public void testValidateUser() {
	        String username = "testUser";
	        String password = "testPassword";
	        UserEntity user = new UserEntity();
	        when(session.createCriteria(UserEntity.class)).thenReturn(criteria);
	        when(criteria.add(Restrictions.eq("username", username))).thenReturn(criteria);
	        when(criteria.add(Restrictions.eq("password", password))).thenReturn(criteria);
	        when(criteria.uniqueResult()).thenReturn(user);

	        UserEntity result = curdOperations.validateUser(username, password);

	        assertEquals("Returned user should be the expected user", user, result);
	    }

	    @Test
	    public void testValidateUser_UserNotFound() {
	        String username = "testUser";
	        String password = "testPassword";

	        when(session.createCriteria(UserEntity.class)).thenReturn(criteria);
	        when(criteria.add(Restrictions.eq("username", username))).thenReturn(criteria);
	        when(criteria.add(Restrictions.eq("password", password))).thenReturn(criteria);
	        when(criteria.uniqueResult()).thenReturn(null);

	        UserEntity result = curdOperations.validateUser(username, password);

	        assertNull(result); // Asserts that no user is returned
	    }
	    
	    
	    

	    @Test
	    public void testAddTask() {
	        // Test data
	        Long userId = 1L;
	        String description = "Test Task";
	        String toTime = "14:00";
	        String status = "Pending";
	        String fromTime = "13:00";

	        // Mocking user and session
	        UserEntity user = new UserEntity();
	        when(session.get(UserEntity.class, userId)).thenReturn(user);

	        // Calling the method
	        curdOperations.addTask(userId, description, fromTime,toTime, status);

	        // Capturing the saved task
	        ArgumentCaptor<TaskEntity> taskCaptor = ArgumentCaptor.forClass(TaskEntity.class);
	        verify(session).save(taskCaptor.capture());
	        TaskEntity savedTask = taskCaptor.getValue();

	        // Verifying saved task properties
	        assertNotNull(savedTask); // Verifies if the saved task is not null
	        assertEquals(description, savedTask.getDescription()); // Verifies if the description matches
	        assertEquals(fromTime, savedTask.getFromTime()); // Verifies if the fromTime matches
	        assertEquals(toTime, savedTask.getToTime()); // Verifies if the toTime matches
	        assertEquals(status, savedTask.getStatus()); // Verifies if the status matches
	        assertEquals(user, savedTask.getUser()); // Verifies if the user matches
	        verify(transaction).commit(); // Verifies if transaction.commit was called
	    }
	    @Test
	    public void testGetTask() {
	        // Arrange
	        Long userId = 1L;
	        int pageNumber = 1;
	        int pageSize = 10;
	        
	        Query<TaskEntity> query = mock(Query.class);
	        List<TaskEntity> expectedTasks = List.of(new TaskEntity(), new TaskEntity()); // Create some dummy tasks
	        
	        when(sessionFactory.openSession()).thenReturn(session);
	        when(session.beginTransaction()).thenReturn(transaction);
	        when(session.createQuery(anyString(), eq(TaskEntity.class))).thenReturn(query);
	        when(query.setParameter("userId", userId)).thenReturn(query);
	        when(query.setFirstResult((pageNumber - 1) * pageSize)).thenReturn(query);
	        when(query.setMaxResults(pageSize)).thenReturn(query);
	        when(query.getResultList()).thenReturn(expectedTasks);

	        // Act
	        List<TaskEntity> actualTasks = curdOperations.getTask(userId, pageNumber, pageSize);

	        // Assert
	        assertEquals(expectedTasks.size(), actualTasks.size());
	        // Additional assertions if needed
	    }
	    @Test
	    public void testUpdateTask() {
	        // Arrange
	        TaskEntity updatedTask = new TaskEntity();
	        updatedTask.setTask_id(1L); // Set the task id for the updated task
	        updatedTask.setDescription("Updated description");
	        updatedTask.setToTime("14:00");
	        updatedTask.setStatus("Completed");
	        
	        TaskEntity existingTask = new TaskEntity();
	        existingTask.setTask_id(1L); // Set the task id for the existing task
	        existingTask.setDescription("Old description");
	        existingTask.setToTime("12:00");
	        existingTask.setStatus("Pending");

	        when(sessionFactory.openSession()).thenReturn(session);
	        when(session.beginTransaction()).thenReturn(transaction);
	        when(session.get(TaskEntity.class, updatedTask.getTask_id())).thenReturn(existingTask);

	        // Act
	        curdOperations.updateTask(updatedTask);

	        // Assert
	        verify(session).update(existingTask);
	        verify(transaction).commit();
	    }

	    @Test
	    public void testDeleteTask() {
	        // Arrange
	        TaskEntity taskToDelete = new TaskEntity();
	        
	        when(sessionFactory.openSession()).thenReturn(session);
	        when(session.beginTransaction()).thenReturn(transaction);

	        // Act
	        TaskEntity deletedTask = curdOperations.deleteTask(taskToDelete);

	        // Assert
	        assertNotNull(deletedTask);
	        verify(session).delete(taskToDelete);
	        verify(transaction).commit();
	    }
	    @Test
	    public void testIsFromTimeAlreadyExists() {
	        // Arrange
	        Long userId = 1L;
	        String fromTime = "10:00";
	        Long expectedCount = 1L; // Assuming a count of 1 for testing purposes
	        
	        Query<Long> query = mock(Query.class);
	        
	        when(sessionFactory.openSession()).thenReturn(session);
	        when(session.createQuery(anyString(), eq(Long.class))).thenReturn(query);
	        when(query.setParameter("userId", userId)).thenReturn(query);
	        when(query.setParameter("fromTime", fromTime)).thenReturn(query);
	        when(query.uniqueResult()).thenReturn(expectedCount);

	        // Act
	        boolean exists = curdOperations.isFromTimeAlreadyExists(userId, fromTime);

	        // Assert
	        assertTrue(exists);
	    }
	    @Test
	    public void testGetTotalTasks() {
	        // Arrange
	        Long userId = 1L;

	        // Act
	        int totalTasks = curdOperations.getTotalTasks(userId);

	        // Assert
	        assertEquals(5, totalTasks); // Assuming the approximate total tasks is 5
	    }

	

}
