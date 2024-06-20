<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Add Task</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f4f4f4;
            margin: 0;
            padding: 20px;
            background-color: #363636;
        }
        h2 {
            text-align: center;
            color: #fff;
        }
        form {
            max-width: 400px;
            margin: 0 auto;
            background-color: #fff;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
        }
        label {
            display: block;
            margin-bottom: 5px;
            color: #333;
        }
        input[type="text"],
        input[type="time"],
        select {
            width: calc(100% - 22px); 
            padding: 10px;
            margin-bottom: 15px;
            border: 1px solid #ccc;
            border-radius: 4px;
            box-sizing: border-box;
        }
        button[type="submit"] {
            background-color: #ff6f61; 
            color: #fff;
            border: none;
            padding: 10px 20px;
            border-radius: 4px;
            cursor: pointer;
            transition: background-color 0.3s ease;
            display: block;
            width: 100%;
        }
        button[type="submit"]:hover {
            background-color: #ff4d3f; 
        }
        .add-button {
            background-color: #ffd54f;
            color: #333;
            border: none;
            padding: 10px 20px;
            border-radius: 6px;
            cursor: pointer;
            transition: background-color 0.3s ease;
            text-decoration: none;
            display: inline-block;
            margin-right: 10px;
        }
       .error {
            color: red;
            text-align: center;
            margin-bottom: 10px;
        }
    </style>
</head>
<body>
    <h2>Add Task</h2>
    <%-- Display error message if exists --%>
     <% if(request.getAttribute("error") != null) { %>
        <p class="error"><%= request.getAttribute("error") %></p>
    <% } %>
    <form action="tasks" method="post">
        <label for="description">Description:</label>
        <input type="text" id="description" name="description" required>
        <label for="fromTime">Start Time:</label>
        <input type="time" id="fromTime" name="fromTime" required>
        <label for="toTime">End Time:</label>
        <input type="time" id="toTime" name="toTime" required><br />
        <label for="status">Status:</label>
        <select id="status" name="status">
            <option value="Pending" style="color: #ff6f61;">Pending</option> 
            <option value="In Progress" style="color: #ffa600;">In Progress</option> 
            <option value="On Hold" style="color: #ffee58;">On Hold</option> 
            <option value="Completed" style="color: #8bc34a;">Completed</option> 
        </select>
        <button type="submit">Add Task</button><br />
        <a href="display1" class="add-button">Task List</a>
    </form>

   
</body>
</html>
