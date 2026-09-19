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


const API = "http://localhost:8080/api";
let allEmployees = []; 
let deleteSalaryId = null;

document.addEventListener("DOMContentLoaded", () => {
  loadEmployeeDropdowns();
  loadSalaryList();

  document.getElementById("clientForm").addEventListener("submit", addSalary);
  document.getElementById("editSalaryForm").addEventListener("submit", updateSalary);
  // -----------------------
  // Bind delete button once
  // -----------------------
  document.getElementById("confirmDeleteBtn").addEventListener("click", confirmDelete);
});
// ---------------------------------------------------
// Load employee data once and populate both dropdowns
// ---------------------------------------------------
async function loadEmployeeDropdowns() {
  try {
    const res = await fetch(`${API}/employees`, {
        headers: getAuthHeaders()
    });
    allEmployees = await res.json();

    populateEmployeeDropdown("employee");
    populateEmployeeDropdown("editemployee");
  } catch (err) {
    console.error("Failed to load employees:", err);
  }
}

function populateEmployeeDropdown(selectId, selectedEmpId = null) {
  const select = document.getElementById(selectId);
  if (!select) return;

  select.innerHTML = `<option value="" disabled selected>- Select Employee -</option>`;
  allEmployees.forEach(emp => {
    const option = document.createElement("option");
    option.value = emp.empId;
    option.textContent = `${emp.first_name} ${emp.last_name}`;
    if (selectedEmpId && selectedEmpId == emp.emp_id) {
      option.selected = true;
    }
    select.appendChild(option);
  });
}
// ------------------------
// Add salary JS Start Here
// ------------------------
async function addSalary(e) {
  e.preventDefault();

  const empId = document.getElementById("employee").value;
  const salary = document.getElementById("salary").value;

  if (!empId || !salary) {
    showAlert("Please fill all fields", "danger");
    return;
  }

  const payload = {
    empId: parseInt(empId),
    monthlySalary: parseFloat(salary),
    // employee: { empId: parseInt(empId) } 
    
  };

  try {
    const res = await fetch(`${API}/salary`, {
      method: "POST",
      headers: getAuthHeaders(),
      body: JSON.stringify(payload)
    });

    if (!res.ok) throw new Error("Insert failed");

    document.getElementById("clientForm").reset();
    showAlert("Employee salary added successfully!", "success");
    loadSalaryList();
  } catch (err) {
    console.error(err);
    showAlert("Failed to add salary", "danger");
  }
}
// ------------------------
// Add salary JS End Here
// ------------------------

// ------------------------------
// Load Salary List JS Start Here
// ------------------------------
async function loadSalaryList() {
  try {
    const res = await fetch(`${API}/salary`, {
         headers: getAuthHeaders()
    });
    const data = await res.json();

    const tbody = document.getElementById("salaryTableBody");

     // Destroy existing DataTable instance before replacing content
     if ($.fn.DataTable.isDataTable('#salary-list-table')) {
      $('#salary-list-table').DataTable().destroy();
    }

    tbody.innerHTML = "";

    data.forEach(salary => {
      const row = document.createElement("tr");
      row.innerHTML = `
        <td class="text-center">${salary.salaryId}</td>
        <td class="text-center">${salary.employeeName}
        <td class="text-center">${salary.monthlySalary}</td>
        <td class="text-center">
          <i class="fa-solid fa-pen-to-square text-primary me-2"
             onclick="openEditModal(${salary.salaryId})"
             style="cursor: pointer; font-size: 18px;"></i>
          <i class="fa-solid fa-trash text-danger"
             onclick="openDeleteModal(${salary.salaryId})"
             style="cursor: pointer; font-size: 18px;"></i>
        </td>
      `;
      tbody.appendChild(row);
    });

     // Reinitialize DataTable after DOM update
     $('#salary-list-table').DataTable();
  } catch (err) {
    console.error("Failed to load salaries:", err);
  }
}
// ------------------------------
// Load Salary List JS End Here
// ------------------------------

// -----------------------------
// Open Edit Model JS Start Here
// -----------------------------
async function openEditModal(id) {
  try {
    const res = await fetch(`${API}/salary/${id}`, {
      headers: getAuthHeaders()
    });
    const salary = await res.json();
 
    document.getElementById("edit_salary_id").value = salary.salaryId;
    document.getElementById("editsalary").value = salary.monthlySalary;
 
    console.log("Salary Response:", salary);
    console.log("salary.employee:", salary.employee);
    console.log("salary.employee.emp_id:", salary.employee?.emp_id);
    console.log("salary.employee.empId:", salary.employee?.empId);
 
    const empId = salary.employee?.empId || salary.employee?.emp_id;
    await populateEmployeeDropdown("editemployee", empId);
 
    const modal = new bootstrap.Modal(document.getElementById("editSalaryModal"));
    modal.show();
  } catch (err) {
    console.error("Failed to load salary:", err);
  }
}
// -----------------------------
// Open Edit Model JS End Here
// -----------------------------

// ---------------------------
// Update Salary JS Start Here
// ---------------------------
async function updateSalary(e) {
  e.preventDefault();

  const id = document.getElementById("edit_salary_id").value;
  const salary = document.getElementById("editsalary").value;
  const empId = document.getElementById("editemployee").value;

  if (!id || !empId || !salary) {
    showAlert("Please fill all fields", "danger");
    return;
  }

  const payload = {
    empId: parseInt(empId),
    monthlySalary: parseFloat(salary),
    // employee: { empId: parseInt(empId) }
  };

  try {
    const res = await fetch(`${API}/salary/${id}`, {
      method: "PUT",
      headers: getAuthHeaders(),
      body: JSON.stringify(payload)
    });

    if (!res.ok) throw new Error("Update failed");

    bootstrap.Modal.getInstance(document.getElementById("editSalaryModal")).hide();
    showAlert("Salary updated successfully!", "success");
    loadSalaryList();
  } catch (err) {
    console.error(err);
    showAlert("Failed to update salary", "danger");
  }
}
// --------------------------
// Update Salary JS End Here
// --------------------------

// --------------------------------------------
// Open Delete Confirmation Modal JS Start Here
// --------------------------------------------
function openDeleteModal(id) {
  deleteSalaryId = id;
  const modal = new bootstrap.Modal(document.getElementById("deleteConfirmModal"));
  modal.show();
}
// --------------------------------------------
// Open Delete Confirmation Modal JS End Here
// --------------------------------------------

// ------------------------------
// Confirm Deletion JS Start Here
// ------------------------------
async function confirmDelete() {
  if (!deleteSalaryId) return;

  try {
    const res = await fetch(`${API}/salary/${deleteSalaryId}`, {
      method: "DELETE",
      headers: getAuthHeaders()  
    });

    showAlert("Salary deleted successfully!", "danger");
    loadSalaryList();
    const modalEl = document.getElementById("deleteConfirmModal");
    bootstrap.Modal.getInstance(modalEl).hide();
    if (!res.ok) throw new Error();

    

    
  } catch (err) {
    console.error(err);
    
  } finally {
    deleteSalaryId = null;
  }
}
// ------------------------------
// Confirm Deletion JS End Here
// ------------------------------

// ---------------------------
// Alert Display JS Start Here
// ---------------------------
function showAlert(message, type = "success") {
  const alertBox = document.getElementById("alertBox");
  const alertMessage = document.getElementById("alertMessage");

  // Update alert content and class
  alertMessage.textContent = message;
  alertBox.className = `alert alert-${type} alert-dismissible fade show`;
  alertBox.classList.remove("d-none");

  // Auto-hide after 4 seconds
  setTimeout(() => {
    alertBox.classList.add("d-none");
  }, 4000);
}

// -------------------------
// Alert Display JS End Here
// -------------------------


// Salary Datatable Responsive JS Start

// $(document).ready(function () {
//         $('#user-list-table').DataTable({
//             responsive: true,
//             scrollX: false,
//             autoWidth: false,
//             columnDefs: [
//                 { targets: '_all', className: 'dt-center' }
//             ]
//         });
//     });

// Salary Datatable Responsive JS End