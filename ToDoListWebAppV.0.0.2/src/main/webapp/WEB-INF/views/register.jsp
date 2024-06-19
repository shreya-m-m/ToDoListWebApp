<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>User Registration</title>
    <style>
          body {
            font-family: Arial, sans-serif;
            background-color: #f4f4f4;
            margin: 0;
            padding: 0px;
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
	.open-popup{
		visibility: visible;
		top: 50%;
		transform: translate(-50%, -50%) scale(1);
	
	}
        .popup h2{
        font-size: 38px;
        font-weight: 500;
        margin: 30px 0 10px;
        }
        .popup button{
        	width:100px;
        	margin-top: 50px;
        	padding: 10px 0;
        	background: #6fd649;
        	color: #fff;
        	border; 0;
        	outline: none;
        	font-size: 18px;
        	border-radius: 4px;
        	cursor: pointer;
        	box-shadow: 0 5px 5px rgba(0,0,0,0.2);
        
        }
    </style>
</head>
<body>
    <h2 style="color: burlywood;">USER REGISTRATION</h2>
    <form action="register" method="post">
        <label for="username">Username:</label>
        <input type="text" id="username" name="username" required><br><br>
        <label for="password">Password:</label>
        <input type="password" id="password" name="password" required>
        <span id="passwordError" style="color: red;"></span><br><br>
        <button type="submit" class="btn" onclick="openPoupup()" id="submitButton" disabled>Register</button> 
    </form>
    
    <script>
        let popup = document.getElementById("popup");

        function openPoupup() {
            popup.classList.add("open-popup");
        }

        function closePoupup() {
            popup.classList.remove("open-popup");
        }

        document.getElementById("password").addEventListener("input", function() {
            var passwordInput = document.getElementById("password");
            var passwordError = document.getElementById("passwordError");
            var password = passwordInput.value;
            var passwordPattern = /^(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{8,}$/;

            if (!passwordPattern.test(password)) {
                passwordError.textContent = "Password must contain at least one digit, one lowercase letter, one uppercase letter, and be at least 8 characters long.";
                submitButton.disabled = true;
            } else {
                passwordError.textContent = "";
                submitButton.disabled = false;
            }
        });
    </script>
</body>
</html>
