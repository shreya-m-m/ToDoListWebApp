package controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import entities.TaskEntity;
import entities.UserEntity;
import service.CurdOperations;

@Controller
public class WebPageController {

    @Autowired
    private CurdOperations service;

    @GetMapping("/")
    public String home(Model model) {
        return "home";
    }

    @GetMapping("/registeration")
    public String registration() {
        return "register";
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String register(Model model, UserEntity user) {
        UserEntity localUser = service.registerr(user);
        model.addAttribute("user", localUser);
        return "redirect:/user";
    }

    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public String userLogin() {
        return "userLogin";
    }

    @PostMapping("/login")
    public String userLogin(Model model, HttpSession session, @RequestParam("username") String username, @RequestParam("password") String password) {
        UserEntity localUser = service.validateUser(username, password);
        if (localUser != null) {
            session.setAttribute("user_id", localUser.getUser_id());
            model.addAttribute("validuser", localUser);
            return "redirect:/add";
        } else {
            model.addAttribute("error", "Invalid username or password");
            return "userLogin";
        }
    }

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String addTask() {
        return "addTask";
    }
    @PostMapping("/tasks")
    public String addTask(Model model, HttpSession session, @RequestParam("description") String description,
                          @RequestParam("fromTime") String fromTime, @RequestParam("toTime") String toTime,
                          @RequestParam("status") String status) {
        Long userId = (Long) session.getAttribute("user_id");
        if (service.isFromTimeAlreadyExists(userId, fromTime)) {
            model.addAttribute("error", "Task with the same fromTime already exists.");
            return "addTask";
        }
        service.addTask(userId, description, fromTime, toTime, status);
        return "redirect:/display1";
    }

//
    @GetMapping("/display1")
    public String displayTasks(Model model, HttpSession session, @RequestParam(defaultValue = "1") int pageNumber,
                               @RequestParam(defaultValue = "5") int pageSize) {
        Long userId = (Long) session.getAttribute("user_id");
        if (userId == null) {
            return "redirect:/login"; // Redirect to login if user is not logged in
        }

        List<TaskEntity> tasks = service.getTask(userId, pageNumber, pageSize);
        int totalTasks = service.getTotalTasks(userId);
        
        model.addAttribute("tasks", tasks); 
        model.addAttribute("pageNumber", pageNumber);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("totalTasks", totalTasks);
        
        return "displayPage";
    }

    @PostMapping("/update")
    public String updateTask(@ModelAttribute TaskEntity updatedTask, RedirectAttributes redirectAttributes) {
        service.updateTask(updatedTask);
        redirectAttributes.addFlashAttribute("successMessage", "Task Details updated successfully!");
        return "redirect:/display1";
    }
//
    @GetMapping("/deletetask")
    public String deleteUser(@RequestParam("task_id") long id) {
        TaskEntity userToDelete = new TaskEntity(); 
        userToDelete.setTask_id(id);
        service.deleteTask(userToDelete);
 
        return "redirect:/display1";
    }
    

}
