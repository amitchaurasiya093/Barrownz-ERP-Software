//helper function for jwt header
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
///////

const API = "http://localhost:8080/api/projects";
let selectedDeleteProjectId = null;

document.addEventListener("DOMContentLoaded", () => {
  loadProjects();

  document.getElementById("projectForm").addEventListener("submit", createProject);
  document.getElementById("editProjectForm").addEventListener("submit", updateProject);
  document.getElementById("confirmDeleteProject").addEventListener("click", deleteProject);
});

// --------------------------
// Load All Projects JS Start
// --------------------------

async function loadProjects() {
  try {
    const res = await fetch(API, {
      headers: getAuthHeaders() //  Added auth headers
    });
    const data = await res.json();
    const tbody = document.querySelector("#projectTable tbody");

    // Destroy existing DataTable instance before replacing content
    if ($.fn.DataTable.isDataTable('#projectTable')) {
      $('#projectTable').DataTable().destroy();
    }

    tbody.innerHTML = data.map(p => `
      <tr>
        <td class="text-center">${p.projectId}</td>
        <td class="text-center">${p.projectName}</td>
        <td class="text-center">
            <i class="fa-solid fa-pen-to-square text-primary me-2"
               onclick="openEditModal(${p.projectId})"
               style="cursor: pointer; font-size: 18px;"></i>
            <i class="fa-solid fa-trash text-danger"
               onclick="openDeleteModal(${p.projectId})"
               style="cursor: pointer; font-size: 18px;"></i>
        </td>
      </tr>
    `).join("");

     // Reinitialize DataTable after DOM update
     $('#projectTable').DataTable();

  } catch (error) {
    console.error("Failed to load projects", error);
  }
}

// --------------------------
// Load All Projects JS End
// --------------------------

// -----------------------
// Create Project JS Start
// -----------------------

async function createProject(e) {
  e.preventDefault();
  const name = document.getElementById("project_name").value.trim();
  if (!name) return;

  const project = { projectName: name };

  const res = await fetch(API, {
    method: "POST",
    headers: getAuthHeaders(),   //added jwt auth
    body: JSON.stringify(project)
  });

  if (res.ok) {
    e.target.reset();
    showSuccess("Project created successfully");
    loadProjects();
  } else {
    showError("Failed to create project");
  }
}

// -----------------------
// Create Project JS End
// -----------------------

// ------------------------
// Open Edit Modal JS Start
// ------------------------

async function openEditModal(id) {
  const res = await fetch(`${API}/${id}`, {
       headers: getAuthHeaders()  //added jwt auth
  });
  const data = await res.json();

  document.getElementById("edit_project_id").value = data.projectId;
  document.getElementById("edit_project_name").value = data.projectName;

  const modal = new bootstrap.Modal(document.getElementById("editProjectModal"));
  modal.show();
}

// ------------------------
// Open Edit Modal JS End
// ------------------------

// -----------------------
// Update project JS Start
// -----------------------

async function updateProject(e) {
  e.preventDefault();
  const id = document.getElementById("edit_project_id").value;
  const name = document.getElementById("edit_project_name").value;

  const res = await fetch(`${API}/${id}`, {
    method: "PUT",
    headers: getAuthHeaders(),   //added jwt auth
    body: JSON.stringify({ projectName: name })
  });

  if (res.ok) {
    bootstrap.Modal.getInstance(document.getElementById("editProjectModal")).hide();
    showSuccess("Project updated successfully");
    loadProjects();
  } else {
    showError("Update failed");
  }
}

// -----------------------
// Update project JS End
// -----------------------

// ----------------------------------
// Open Delete Confirm Modal JS Start
// ----------------------------------

function openDeleteModal(id) {
  selectedDeleteProjectId = id;
  const modal = new bootstrap.Modal(document.getElementById("deleteProjectModal"));
  modal.show();
}

// ----------------------------------
// Open Delete Confirm Modal JS End
// ----------------------------------

// -----------------------
// Delete Project JS Start
// -----------------------

async function deleteProject() {
  if (!selectedDeleteProjectId) return;

  const res = await fetch(`${API}/${selectedDeleteProjectId}`, {
    method: "DELETE",
    headers: getAuthHeaders()  //added jwt auth
  });

  if (res.ok) {

  } else {
        bootstrap.Modal.getInstance(document.getElementById("deleteProjectModal")).hide();
    showDanger("Project deleted successfully");
    loadProjects();
  }
}

// -----------------------
// Delete Project JS End
// -----------------------

// -----------------------
// Alerts Message JS Start
// -----------------------

function showSuccess(msg) {
  showAlert(msg, "success");
}
function showDanger(msg) {
  showAlert(msg, "danger");
}
function showError(msg) {
  showAlert(msg, "danger");
}
function showAlert(msg, type) {
  const alert = document.createElement("div");
  alert.className = `alert alert-${type} alert-dismissible fade show mx-auto`;
  alert.role = "alert";
  alert.style.maxWidth = "600px";
  alert.style.minWidth = "300px";
  alert.style.width = "auto";
  alert.innerHTML = `
    ${msg}
    <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
  `;

  const placeholder = document.getElementById("alertPlaceholder");
  placeholder.innerHTML = ''; 
  placeholder.appendChild(alert);

  setTimeout(() => alert.remove(), 2000);
}

// -----------------------
// Alerts Message JS End
// -----------------------
