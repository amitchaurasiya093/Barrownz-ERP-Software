//helper function for generating header
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
////


const API = "http://localhost:8080/api/employee-projects";
const EMPLOYEE_API = "http://localhost:8080/api/employees";
const PROJECT_API = "http://localhost:8080/api/projects";

let projectOptionsHTML = "";
let employeeMap = {};
let currentAssignmentsMap = {};

document.addEventListener("DOMContentLoaded", () => {
  loadAssignments();
  fetchEmployees();
  fetchProjects();

  const form = document.getElementById("assignProjectForm");
  const projectContainer = document.getElementById("projectDropdownContainer");
  const addProjectBtn = document.getElementById("addProjectDropdown");

  // Add More Projects
  addProjectBtn.addEventListener("click", function (e) {
    e.preventDefault();
    const wrapper = document.createElement("div");
    wrapper.classList.add("project-select-wrapper", "mb-2");
    wrapper.innerHTML = `
      <select class="form-control" name="projectName[]" required>
        ${projectOptionsHTML}
      </select>
    `;
    projectContainer.appendChild(wrapper);
  });

  //  Assign Form Submit
  form.addEventListener("submit", function (e) {
    e.preventDefault();

    const employeeId = parseInt(document.getElementById("employeeName").value);
    if (!employeeId || isNaN(employeeId)) {
      showAlert("Please select a valid employee.", "error");
      return;
    }

    const projectDropdowns = form.querySelectorAll('select[name="projectName[]"]');
    const assignments = [];
    const alreadyAssignedProjects = currentAssignmentsMap[employeeId] || [];
    let duplicateFound = false;

    projectDropdowns.forEach(dropdown => {
      const value = dropdown.value;
      if (value) {
        if (alreadyAssignedProjects.includes(value)) {
          duplicateFound = true;
        } else {
          assignments.push({
            employee: { emp_id: employeeId },
            empProjectName: value
          });
        }
      }
    });

    if (duplicateFound) {
      showAlert("This project is already assigned to this employee.", "error");
      return;
    }

    if (assignments.length > 0) {
      fetch(API, {
        method: "POST",
        headers: getAuthHeaders(),
        body: JSON.stringify(assignments)
      })
        .then(res => res.json())
        .then(() => {
          form.reset();
          const extraDropdowns = projectContainer.querySelectorAll(".project-select-wrapper");
          extraDropdowns.forEach((el, idx) => {
            if (idx !== 0) el.remove();
          });

          const firstDropdown = document.querySelector("select[name='projectName[]']");
          if (firstDropdown) {
            firstDropdown.innerHTML = projectOptionsHTML;
          }

          loadAssignments();
          showAlert("Project(s) successfully assigned!", "success");
        });
    }
  });

  //  Edit Form Submit
  document.getElementById("editAssignForm").addEventListener("submit", function (e) {
    e.preventDefault();

    const id = document.getElementById("editAssignId").value;
    const empId = document.getElementById("editEmployee").value;
    const updatedProject = document.getElementById("editProjectName").value;

    const updatedData = {
      empProjectId: id,
      employee: { emp_id: parseInt(empId) },
      empProjectName: updatedProject
    };

    fetch(`${API}/${id}`, {
      method: "PUT",
      headers: getAuthHeaders(),
      body: JSON.stringify(updatedData)
    })
      .then(res => {
        if (!res.ok) throw new Error("Failed to update");
        return res.json();
      })
      .then(() => {
        const modal = bootstrap.Modal.getInstance(document.getElementById("editAssignModal"));
        modal.hide();
        showAlert("Assignment updated successfully!", "success");
        loadAssignments();
      })
      .catch(err => {
        console.error(err);
        showAlert("Failed to update assignment", "error");
      });
  });
});

//  Load Assignments and Group by Employee
function loadAssignments() {
  fetch(API, {
    headers: getAuthHeaders(false)
  })
    .then(res => res.json())
    .then(data => {
      const grouped = groupByEmployee(data);
      const tbody = document.getElementById("assignmentTableBody");
      tbody.innerHTML = "";
      currentAssignmentsMap = {};

      Object.keys(grouped).forEach((empId, index) => {
        const recordList = grouped[empId];
        const emp = recordList[0]?.employee;
        if (!emp) return;

        const empName = `${emp.first_name} ${emp.last_name}`;
        const projectNames = recordList.map(r => r.empProjectName).filter(Boolean);

        currentAssignmentsMap[empId] = projectNames;

        const projectsHtml = projectNames.length === 1
          ? projectNames[0]
          : `<marquee behavior="scroll" direction="left">${projectNames.join(", ")}</marquee>`;

        tbody.innerHTML += `
          <tr>
            <td class="text-center">${index + 1}</td>
            <td class="text-center">${empName}</td>
            <td class="text-center">${projectsHtml}</td>
            <td class="text-center">
              <i class="fa-solid fa-trash text-danger" style="cursor:pointer" onclick='confirmDelete(${recordList[0].empProjectId})'></i>
            </td>
          </tr>
        `;
      });
    })
    .catch(err => {
      console.error("❌ Error loading assignments:", err);
    });
}

function groupByEmployee(data) {
  return data.reduce((acc, curr) => {
    const id = curr.employee?.emp_id;
    if (!id) return acc;
    if (!acc[id]) acc[id] = [];
    acc[id].push(curr);
    return acc;
  }, {});
}

//  Load Employees
function fetchEmployees() {
  fetch(EMPLOYEE_API, {
    headers: getAuthHeaders()
  })
    .then(res => res.json())
    .then(data => {
      const select = document.getElementById("employeeName");
      let html = `<option value="" disabled selected>- Select Employee -</option>`;
      data.forEach(emp => {
        const fullName = `${emp.first_name} ${emp.last_name}`;
        employeeMap[emp.emp_id] = fullName;
        html += `<option value="${emp.emp_id}">${fullName}</option>`;
      });
      select.innerHTML = html;
    });
}

// Load Projects
function fetchProjects() {
  fetch(PROJECT_API, {
    headers: getAuthHeaders()
  })
    .then(res => res.json())
    .then(data => {
      projectOptionsHTML = `<option value="" disabled selected>- Select Project -</option>`;
      data.forEach(p => {
        projectOptionsHTML += `<option value="${p.projectName}">${p.projectName}</option>`;
      });

      const firstDropdown = document.querySelector("select[name='projectName[]']");
      if (firstDropdown) {
        firstDropdown.innerHTML = projectOptionsHTML;
      }
    });
}

//  Confirm Delete
function confirmDelete(id) {
  const modal = new bootstrap.Modal(document.getElementById("deleteConfirmModal"));
  modal.show();

  document.getElementById("confirmDeleteBtn").onclick = () => {
    fetch(`${API}/${id}`, { 
      method: "DELETE",
      headers: getAuthHeaders()
     })
      .then(() => {
        modal.hide();
        showAlert("Project assignment successfully deleted!", "success");
        loadAssignments();
      });
  };
}

//  Alert Handler
function showAlert(message, type = "success") {
  const box = document.getElementById("alertBox");
  const msg = document.getElementById("alertMessage");

  box.classList.remove("d-none", "alert-success", "alert-danger");
  box.classList.add(type === "error" ? "alert-danger" : "alert-success");

  msg.textContent = message;
  setTimeout(() => box.classList.add("d-none"), 3000);
}
