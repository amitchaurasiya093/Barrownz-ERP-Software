//Helper method to get auth header
function getAuthHeaders(isJson = true) {
  const token = localStorage.getItem('token');
  const headers = {
    "Authorization": `Bearer ${token}`
  };
  if (isJson) {
    headers["Content-Type"] = "application/json";
  }
  return headers;
}

//helper method to get empId from JWT
function getEmpIdFromToken() {
  const token = localStorage.getItem('token');
  if (!token) return null;

  try {
    const payload = token.split('.')[1];
    const decodedPayload = JSON.parse(atob(payload));
    return decodedPayload.empId;
  } catch (error) {
    console.error("Error decoding token:", error);
    return null;
  }
}

const API_TASK = 'http://localhost:8080/api';


document.addEventListener('DOMContentLoaded', async function () {
  await loadDropdowns();
  await fetchAndDisplayTasks();
});

//load dropdowns
async function loadDropdowns() {
  try {
    const res = await fetch(`${API_TASK}/employee-dashboard/dropdowns`, {
      headers: getAuthHeaders()
    });
    if (!res.ok) throw new Error("Failed to fetch dropdown data");

    const data = await res.json();

    populateDropdownData('task-type', data.types, 'typeId', 'typeName');
    populateDropdownData('task-priority', data.priorities, 'priorityId', 'priorityName');
    populateDropdownData('task-status', data.statuses, 'statusId', 'statusName');
    populateDropdownData('task-subject', data.subjects, 'subjectId', 'subjectName');
    populateDropdownData('edit-task-type', data.types, 'typeId', 'typeName');
    populateDropdownData('edit-task-priority', data.priorities, 'priorityId', 'priorityName');
    populateDropdownData('edit-task-status', data.statuses, 'statusId', 'statusName');
    populateDropdownData('edit-task-subject', data.subjects, 'subjectId', 'subjectName');

    if (data.projects && data.projects.length > 0) {
      populateDropdownData('task-project', data.projects, 'projectId', 'projectName');
      populateDropdownData('edit-task-project', data.projects, 'projectId', 'projectName');
    } else {
      document.getElementById('task-project').innerHTML = `<option value="">No Projects Assigned</option>`;
      document.getElementById('edit-task-project').innerHTML = `<option value="">No Projects Assigned</option>`;
    }

  } catch (err) {
    console.error("❌ Failed to load dropdowns:", err);
  }
}

//populate prefilled dropdown
function populateDropdownData(selectId, dataList, valueKey, textKey) {
  const select = document.getElementById(selectId);
  if (!select) return;

  select.innerHTML = '<option value="">-Select-</option>';
  dataList.forEach(item => {
    const option = document.createElement('option');
    option.value = item[valueKey];
    option.textContent = item[textKey];
    select.appendChild(option);
  });
}

// Add Task method
const form = document.getElementById("taskForm");
if (form) {
  form.addEventListener("submit", handleTaskSubmit);
}

async function handleTaskSubmit(e) {
  e.preventDefault();

  const empId = getEmpIdFromToken();
  if (!empId) return alert("⚠️ Please log in first");

  const taskData = {
    typeId: +document.getElementById("task-type").value,
    priorityId: +document.getElementById("task-priority").value,
    statusId: +document.getElementById("task-status").value,
    subjectId: +document.getElementById("task-subject").value,
    projectId: +document.getElementById("task-project").value,
    employeeId: empId,
    taskQuantity: +document.getElementById("task-quantity").value,
    taskDescription: document.getElementById("task-description").value,
    startDate: document.getElementById("task-start-date").value,
    endDate: document.getElementById("task-end-date").value
  };

  try {
    const res = await fetch(`${API_TASK}/addTask`, {
      method: "POST",
      headers: getAuthHeaders(),
      body: JSON.stringify(taskData)
    });

    if (!res.ok) throw new Error("Failed to create task");

    alert("✅ Task added successfully!");
    form.reset();
    await fetchAndDisplayTasks();
  } catch (err) {
    console.error("❌ Task creation error:", err);
    alert("❌ Error creating task. Try again.");
  }
}

// fetch and display task list by loggedin employee
async function fetchAndDisplayTasks() {

  try {
    const res = await fetch(`${API_TASK}/addTask/by-employee-today`, {
      headers: getAuthHeaders()
    });
    const tasks = await res.json();
  
    const tbody = document.getElementById("taskTableBody");
    tbody.innerHTML = "";
  
    if(tasks.length === 0) {
      tbody.innerHTML = `<tr><td colspan="11" class="text-center text-danger">No tasks found for today.</td></tr>`;
      return;
    }
  
    tasks.forEach(task => {
      const row = document.createElement("tr");
  
      row.innerHTML = `
        <td>${task.projectName}</td>
        <td>${task.subjectName}</td>
        <td>${task.typeName}</td>
        <td>${task.priorityName}</td>
        <td>${task.startDate.replace("T", " ")}</td>
        <td>${task.endDate.replace("T", " ")}</td>
        <td>${task.taskQuantity}</td>
        <td>-</td>
        <td>${task.taskDescription}</td>
        <td>${task.statusName}</td>
        <td>
          <button class="btn btn-sm btn-warning" onclick="openEditModal(${task.taskId})">Edit</button>
        </td>
      `;
      tbody.appendChild(row);
    });
  }
  catch (err) {
    console.error("❌ Error fetching today's tasks:", err);
    alert("❌ Unable to load today's tasks.");
  }

  

}

// open edit modal
async function openEditModal(taskId) {
  try {
    const res = await fetch(`${API_TASK}/addTask/${taskId}`, {
      headers: getAuthHeaders()
    });

    if (!res.ok) throw new Error("Failed to fetch task");

    const task = await res.json();

    document.getElementById("edit-task-id").value = task.taskId;
    document.getElementById("edit-task-type").value = task.taskType.typeId;
    document.getElementById("edit-task-priority").value = task.taskPriority.priorityId;
    document.getElementById("edit-task-status").value = task.taskStatus.statusId;
    document.getElementById("edit-task-subject").value = task.taskSubject.subjectId;
    document.getElementById("edit-task-project").value = task.taskProject.projectId;
    document.getElementById("edit-task-quantity").value = task.taskQuantity;
    document.getElementById("edit-task-description").value = task.taskDescription;
    document.getElementById("edit-task-start-date").value = task.startDate;
    document.getElementById("edit-task-end-date").value = task.endDate;

    const editModal = new bootstrap.Modal(document.getElementById("editTaskModal"));
    editModal.show();

  } catch (err) {
    console.error("❌ Failed to open edit modal:", err);
    alert("❌ Failed to load task for editing.");
  }
}

// update task
document.getElementById("editTaskForm").addEventListener("submit", async function (e) {
  e.preventDefault();

  const taskId = document.getElementById("edit-task-id").value;
  const taskData = {
    typeId: +document.getElementById("edit-task-type").value,
    priorityId: +document.getElementById("edit-task-priority").value,
    statusId: +document.getElementById("edit-task-status").value,
    subjectId: +document.getElementById("edit-task-subject").value,
    projectId: +document.getElementById("edit-task-project").value,
    employeeId: getEmpIdFromToken(),
    taskQuantity: +document.getElementById("edit-task-quantity").value,
    taskDescription: document.getElementById("edit-task-description").value,
    startDate: document.getElementById("edit-task-start-date").value,
    endDate: document.getElementById("edit-task-end-date").value
  };

  try {
    const res = await fetch(`${API_TASK}/addTask/${taskId}`, {
      method: "PUT",
      headers: getAuthHeaders(),
      body: JSON.stringify(taskData)
    });

    if (!res.ok) throw new Error("Failed to update task");

    alert("✅ Task updated successfully!");
    document.getElementById("editTaskForm").reset();
    bootstrap.Modal.getInstance(document.getElementById("editTaskModal")).hide();
    await fetchAndDisplayTasks();
  } catch (err) {
    console.error("❌ Task update error:", err);
    alert("❌ Failed to update task.");
  }
});







