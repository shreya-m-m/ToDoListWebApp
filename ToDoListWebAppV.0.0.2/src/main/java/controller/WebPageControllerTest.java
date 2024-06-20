package controller;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.junit.Before;
import org.junit.Test;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import entities.TaskEntity;
import entities.UserEntity;
import service.CurdOperations;

public class WebPageControllerTest {

	  // Mocked CurdOperations service
    @Mock
    private CurdOperations service;

    // Injecting mocks into WebPageController
    @InjectMocks
    private WebPageController controller;

    // MockMvc instance for testing MVC endpoints
    private MockMvc mockMvc;
    
    @Mock
    private HttpSession session;
    
    @Mock
    private Model model;


    // Setup method to initialize mocks and mockMvc
    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }
 // Test for home page request mapping
    @Test
    public void testHome() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("home"));
    }
    // Test for registration  request mapping
    @Test
    public void testRegistration() throws Exception {
        mockMvc.perform(get("/registeration"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"));
    }

    // Test for user registration
    @Test
    public void testRegister() throws Exception {
        UserEntity user = new UserEntity();
        when(service.registerr(any(UserEntity.class))).thenReturn(user);

        mockMvc.perform(post("/register")
                .param("username", "testuser")
                .param("password", "password"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user"));

        verify(service, times(1)).registerr(any(UserEntity.class));
    }

    // Test for user login page request mapping
    @Test
    public void testUserLogin() throws Exception {
        mockMvc.perform(get("/user"))
                .andExpect(status().isOk())
                .andExpect(view().name("userLogin"));
    }

    // Test for user login handling
    @Test
    public void testUserLoginPost() throws Exception {
        UserEntity user = new UserEntity();
        user.setUser_id(1L);
        when(service.validateUser("username", "password")).thenReturn(user);

        mockMvc.perform(post("/login")
                .param("username", "username")
                .param("password", "password"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/add"));

        verify(service, times(1)).validateUser("username", "password");
    }

    // Test for adding task GET request mapping
    @Test
    public void testAddTaskGet() throws Exception {
        mockMvc.perform(get("/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("addTask"));
    }
 // Test for adding task POST request mapping
    @Test
    public void testAddTask_Success() {
        // Mocking session attribute
        when(session.getAttribute("user_id")).thenReturn(1L);

        // Mocking service behavior
        when(service.isFromTimeAlreadyExists(1L, "09:00")).thenReturn(false);

        // Call the method
        String result = controller.addTask(model, session, "Task description", "09:00", "10:00", "Pending");

        // Verify behavior
        verify(service).addTask(1L, "Task description", "09:00", "10:00", "Pending");
        assertEquals("redirect:/display1", result);
    }

    //Test for duplicate fromTime
    @Test
    public void testAddTask_DuplicateFromTime() {
        // Mocking session attribute
        when(session.getAttribute("user_id")).thenReturn(1L);

        // Mocking service behavior
        when(service.isFromTimeAlreadyExists(1L, "09:00")).thenReturn(true);

        // Call the method
        String result = controller.addTask(model, session, "Task description", "09:00", "10:00", "Pending");

        // Verify behavior
        verify(service, never()).addTask(anyLong(), anyString(), anyString(), anyString(), anyString());
        verify(model).addAttribute(eq("error"), eq("Task with the same fromTime already exists."));
        assertEquals("addTask", result);
    }
    
    //Test to requesting displayPage 
    @Test
    public void testDisplayTasks_UserLoggedIn() {
        // Mocking session attribute
        when(session.getAttribute("user_id")).thenReturn(1L);

        // Mocking service behavior
        List<TaskEntity> tasks = Collections.emptyList();
        when(service.getTask(1L, 1, 5)).thenReturn(tasks);
        when(service.getTotalTasks(1L)).thenReturn(0);

        // Call the method
        String result = controller.displayTasks(model, session, 1, 5);

        // Verify behavior
        verify(model).addAttribute("tasks", tasks);
        verify(model).addAttribute("pageNumber", 1);
        verify(model).addAttribute("pageSize", 5);
        verify(model).addAttribute("totalTasks", 0);
        assertEquals("displayPage", result);
    }

    @Test
    public void testDisplayTasks_UserNotLoggedIn() {
        // Mocking session attribute
        when(session.getAttribute("user_id")).thenReturn(null);

        // Call the method
        String result = controller.displayTasks(model, session, 1, 5);

        // Verify behavior
        verify(session).getAttribute("user_id");
        assertEquals("redirect:/login", result);
    }

    // Test for requesting update
    @Test
    public void testUpdateTask() {
        // Mocking updated task
        TaskEntity updatedTask = new TaskEntity();
        updatedTask.setTask_id(1L);
        updatedTask.setDescription("Updated description");

        // Mocking RedirectAttributes
        RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);

        // Call the method
        String result = controller.updateTask(updatedTask, redirectAttributes);

        // Verify behavior
        verify(service).updateTask(updatedTask);
        verify(redirectAttributes).addFlashAttribute("successMessage", "Task Details updated successfully!");
        assertEquals("redirect:/display1", result);
    }


    //Test for requesting Deletetask
    @Test
    public void testDeleteTask() throws Exception {
    	 // Perform GET request to delete a task with ID 1
        mockMvc.perform(get("/deletetask")
                .param("task_id", "1"))
                .andExpect(status().is3xxRedirection()) 
                .andExpect(redirectedUrl("/display1")); 
        verify(service, times(1)).deleteTask(any(TaskEntity.class));
    }

}




