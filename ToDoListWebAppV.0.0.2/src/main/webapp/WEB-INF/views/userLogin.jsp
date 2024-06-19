<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>User Login</title>
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
            color: #333;
        }
        form {
            max-width: 400px;
            margin: 20px auto;
            background-color: #fff;
            padding: 30px;
            border-radius: 8px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
        }
        label {
            display: block;
            margin-bottom: 10px;
            color: #666;
        }
        input[type="text"],
        input[type="password"] {
            width: 100%;
            padding: 10px;
            margin-bottom: 20px;
            border: 1px solid #ccc;
            border-radius: 4px;
        }
        button[type="submit"] {
            background-color: #007bff;
            color: #fff;
            border: none;
            padding: 10px 20px;
            border-radius: 4px;
            cursor: pointer;
            transition: background-color 0.3s ease;
        }
        button[type="submit"]:hover {
            background-color: #0056b3;
        }
        .error {
            color: red;
            text-align: center;
            margin-bottom: 10px;
        }
        .popup {
            width: 400px;
            background: #fff;
            border-radius: 6px;
            position: fixed;
            top: 0%;
            left: 50%;
            transform: translate(-50%, -50%) scale(0.1);
            text-align: center;
            padding: 0 30px 30px; 
            color: #333;
            visibility: hidden;
            transition: transform 0.8s, top 0.8s;
        }
        .open-popup {
            visibility: visible;
            top: 50%;
            transform: translate(-50%, -50%) scale(1);
        }
        .popup h2 {
            font-size: 38px;
            font-weight: 500;
            margin: 30px 0 10px;
        }
        .popup button {
            width: 100px;
            margin-top: 50px;
            padding: 10px 0;
            background: #6fd649;
            color: #fff;
            border: 0;
            outline: none;
            font-size: 18px;
            border-radius: 4px;
            cursor: pointer;
            box-shadow: 0 5px 5px rgba(0,0,0,0.2);
        }
        #display {
            visibility: hidden;
        }
    </style>
</head>
<body>
<h2 style="color: burlywood;">USER LOGIN</h2>
   <h2>User Login</h2>
    <% if(request.getAttribute("error") != null) { %>
        <p class="error"><%= request.getAttribute("error") %></p>
    <% } %>
    <form action="login" method="post">
        <label for="username">Username:</label>
        <input type="text" id="username" name="username" required><br><br>
        <label for="password">Password:</label>
        <input type="password" id="password" name="password" required><br><br>
        <button type="submit" class="btn">Login</button> 
    </form>

   
    <div class="popup" id="popup">
       <h2>Thank You!</h2>
        <h2>Login Successfully</h2>
        <button type="button" class="btn" onclick="redirectToLogin()">Ok</button>
    </div>

    <script>
        let popup = document.getElementById("popup");
   
        function openPopup() {
            popup.classList.add("open-popup");
        }
        
        function redirectToLogin() {
            closePopup(); 
            window.location.href = "/add"; 
        }
        
        function closePopup() {
            popup.classList.remove("open-popup");
        }

        function validateForm() {
            var username = document.getElementById("username").value;
            var password = document.getElementById("password").value;

            // Check if username and password are not empty
            if (username.trim() === "" || password.trim() === "") {
                alert("Please enter both username and password.");
                return false;
            }

        }
    </script>
</body>
</html>
