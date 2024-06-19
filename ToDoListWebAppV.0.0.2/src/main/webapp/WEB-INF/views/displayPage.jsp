
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Task Display</title>
<style>
    body {
        font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
        background-color: #f4f4f4;
        margin: 0;
        padding: 15px;
        background-color: #363636;
    }
    .container {
        max-width: 1000px;
        margin: 20px auto;
        padding: 20px;
        background-color: #ffffff;
        border-radius: 12px;
        box-shadow: 0 0 20px rgba(0, 0, 0, 0.1);
    }
    h1, h3 {
        text-align: center;
        color: #333;
    }
    table {
        width: 100%;
        border-collapse: collapse;
        margin-top: 20px;
    }
    th, td {
        padding: 12px;
        text-align: left;
    }
    th {
        background-color: #007bff;
        color: #fff;
    }
    tr:nth-child(even) {
        background-color: #f9f9f9;
    }
    .edit-button, .delete-button, .add-button {
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
    .edit-button:hover, .delete-button:hover, .add-button:hover {
        background-color: #ffa000; 
    }
    a {
        color: #007bff;
        text-decoration: none;
    }
    a:hover {
        text-decoration: underline;
    }
    
    td.pending {
        background-color: #FEFEFA 
    }
     
    td.in-progress {
        background-color: #E6A817; 
    }
     
    td.on-hold {
        background-color: #ED2839;
    }
     
    td.completed {
        background-color: #00693E; 
    }

    .edit-form input[type="text"],
    .edit-form input[type="date"] {
        border: none;
        padding: 0;
        outline: none;
        width: 100%; 
        background-color: transparent; 
    }

    .edit-form input[type="text"] {
        color: inherit; 
    }

    .popup {
        display: block ;
        position: fixed;
        top: 50%;
        left: 50%;
        transform: translate(-50%, -50%);
        background-color: #f9f9f9;
        padding: 20px;
        border-radius: 10px;
        box-shadow: 0 0 20px rgba(0, 0, 0, 0.3);
        z-index: 9999;
        max-width: 400px;
    }
    .popup p {
        margin: 0;
        color: red;
        font-weight: bold;
    }
    .close-button {
        position: absolute;
        top: 10px;
        right: 10px;
        cursor: pointer;
        font-size: 20px;
        color: #888;
        transition: color 0.3s;
    }
    .close-button:hover {
        color: red;
    }
    .close-button:focus {
        color: black;
        text-decoration: none;
        cursor: pointer;
    }
    .pagination-link:hover {
    background-color: #ddd;
}

.pagination-link::before {
    content: "";
    display: inline-block;
    width: 0;
    height: 0;
    border-style: solid;
    border-width: 4px 6px 4px 0;
    border-color: transparent #333 transparent transparent;
}

.pagination-link:first-child::before,
.pagination-link:last-child::before {
    margin: 0 4px;
}

.pagination-link:last-child::before {
    transform: rotate(180deg);
}
.success-message {
   
    color: white; 
    margin-bottom: 10px; 
    font-size: 50px; 
    text-align: center;
}
</style>
<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
</head>
<body>
 <c:if test="${not empty successMessage}">
            <div class="success-message">
                ${successMessage}
            </div>
        </c:if>
<div class="container">
    <h1>List Of Tasks</h1>
    <table border="1">
        <tr>
            <th>Description</th>
            <th>Due Time</th>
            <th>Status</th>
            <th></th>
            <th></th>
        </tr>
       <c:forEach items="${tasks}" var="task">
    <tr id="taskRow${task.task_id}">
        <form action="update" method="post" class="edit-form">
            <input type="hidden" name="task_id" value="${task.task_id}" />
            <td>
                <span class="view-mode">${task.description}</span>
                <input type="text" name="description" value="${task.description}" class="edit-mode" style="display: none;" />
            </td>
            <td>
                <span class="view-mode">${task.toTime}</span>
                <input type="time" name="toTime" value="${task.toTime}" class="edit-mode" style="display: none;" />
            </td>
            <td class="${task.status.toLowerCase().replace(" ", "-")}">
                <span class="view-mode">${task.status}</span>
                <select name="status" class="edit-mode" onchange="showPopup(this)" style="display: none;">
                    <option value="Pending" class="pending" ${task.status == 'Pending' ? 'selected' : ''}>Pending</option>
                    <option value="In Progress" class="in-progress" ${task.status == 'In Progress' ? 'selected' : ''}>In Progress</option>
                    <option value="On Hold" class="on-hold" ${task.status == 'On Hold' ? 'selected' : ''}>On Hold</option>
                    <option value="Completed" class="completed" ${task.status == 'Completed' ? 'selected' : ''}>Completed</option>
                </select>
            </td>
            <td>
                <button type="button" class="edit-button" onclick="toggleEditMode(this)">Edit</button>
                        <button type="submit" class="save-button edit-mode" style="display: none;">Save</button>
                    </td>
                    <td><a href="javascript:void(0)" onclick="confirmDelete(${task.task_id})" class="delete-button">Delete</a></td>
                </form>
            </tr>
        </c:forEach>
    </table>
    <button type="button" class="add-button" onclick="loadAddTask()">ADD Task</button>
    <a href="/ToDoListWebAppV.0.0.2/" class="add-button">HOME</a>
     <c:url var="displayUrl" value="/display1">
			<c:param name="pageSize" value="${pageSize}" />
		</c:url>

		<c:set var="totalTasks" value="${totalTasks}" />
		
		<c:set var="prevPage" value="${pageNumber - 1}" />
		<c:set var="nextPage" value="${pageNumber + 1}" />

		
		<a href="${displayUrl}&pageNumber=1" class="pagination-link">&lt;&lt;</a>
	
		<c:if test="${pageNumber > 1}">
			<a href="${displayUrl}&pageNumber=${prevPage}"
				class="pagination-link">&lt;</a>
			
		</c:if>
		<c:if test="${pageNumber < totalTasks}">
			<a href="${displayUrl}&pageNumber=${nextPage}"
				class="pagination-link">&gt;</a>
			
		</c:if>

		<a href="${displayUrl}&pageNumber=${totalTasks}"
			class="pagination-link">&gt;&gt;</a>

    <div id="popup" class="popup" style="display: none;">
        <div class="popup-content">
            <span class="close-button" onclick="closePopup()">&times;</span>
            <p>Your task is "IN PROGRESS"! Please update the due Time.</p>
        </div>
    </div>
</div>

<div id="addTaskContainer">
   
</div>
 
<script>
window.onload = function() {
    var selectElement = document.querySelector('select[name="status"]');
    if (selectElement.value !== "In Progress") {
        document.getElementById("popup").style.display = "none";
    }
};
function showPopup(selectElement) {
    var selectedStatus = selectElement.value;
    if (selectedStatus === "In Progress") {
        document.getElementById("popup").style.display = "block";
    } else {
        document.getElementById("popup").style.display = "none";
    }
}
function closePopup() {
    document.getElementById("popup").style.display = "none";
}
function toggleEditMode(button) {
    var row = button.closest('tr');
    var editModeElements = row.querySelectorAll('.edit-mode');
    var viewModeElements = row.querySelectorAll('.view-mode');

    // Hide view mode elements and display edit mode elements
    editModeElements.forEach(function(element) {
        element.style.display = 'block';
    });

    viewModeElements.forEach(function(element) {
        element.style.display = 'none';
    });

    // Hide the "Edit" button and display the "Save" button
    button.style.display = 'none';
    row.querySelector('.save-button').style.display = 'inline-block';
}

function loadAddTask() {
    fetch('add')
        .then(response => response.text())
        .then(data => {
            document.getElementById('addTaskContainer').innerHTML = data;
        })
        .catch(error => console.error('Error fetching addTask.jsp:', error));
}

function confirmDelete(taskId) {
	  Swal.fire({
	    title: 'Are you sure?',
	    text: "You won't be able to revert this deletion!",
	    icon: 'warning',
	    showCancelButton: true,
	    confirmButtonColor: '#3085d6',
	    cancelButtonColor: '#d33',
	    confirmButtonText: 'Yes, delete it!'
	  }).then((result) => {
	    if (result.isConfirmed) {
	      // Task confirmed deletion, proceed with logic
	      window.location.href = "deletetask?task_id=" + taskId;
	    }
	  })
	}
</script>
</body>
</html>
