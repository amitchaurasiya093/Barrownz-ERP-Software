
//helper function for jwt headers
function getAuthHeaders(isJson = true) {
  const token = localStorage.getItem("token");
  const headers = { "Authorization": `Bearer ${token}` };
  if (isJson) headers["Content-Type"] = "application/json";
  return headers;
}

const BASE_API = 'http://localhost:8080/api';
let deleteRMId = null;

// Load departments in dropdown
async function loadDepartments() {
  try {
    const res = await fetch(`${BASE_API}/departments`, {
          headers: getAuthHeaders(false)    //added jwt token 
    });
    const data = await res.json();
    const select = document.getElementById('dep_name');
    select.innerHTML = `<option value="" disabled selected>-Select Department-</option>` +
      data.map(dep => `<option value="${dep.deptId}">${dep.dept_name}</option>`).join('');

    select.addEventListener('change', function () {
      const deptId = this.value;
      if (deptId) {
        loadEmployeesByDept(deptId);
      }
    });
  } catch (err) {
    console.error("Failed to load departments:", err);
  }
}

// Load employees by department ID
// async function loadEmployeesByDept(deptId) {
//   try {
//     const res = await fetch(`${BASE_API}/employees/by-departments/${deptId}`);
//     const data = await res.json();

//     const rm1 = document.getElementById('rep_name1');
//     const rm2 = document.getElementById('rep_name2');

//     if (data.length === 0) {
//       const noDataOption = `<option value="" disabled selected>No employees found</option>`;
//       rm1.innerHTML = noDataOption;
//       rm2.innerHTML = noDataOption;
//       return;
//     }

//     const options = data.map(emp =>
//       `<option value="${emp.first_name} ${emp.last_name}">${emp.first_name} ${emp.last_name}</option>`
//     ).join('');

//     rm1.innerHTML = `<option value="" disabled selected>-Select Manager-</option>` + options;
//     rm2.innerHTML = `<option value="" disabled selected>-Select Manager-</option>` + options;

//   } catch (err) {
//     console.error("Failed to load employees:", err);
//   }
// }

// Save reporting manager
async function saveReportingManager(e) {
  e.preventDefault();

  const deptId = document.getElementById('dep_name').value;
  const rm1 = document.getElementById('rm_1').value;
  const rm2 = document.getElementById('rm_2').value;

  const body = {
    rm_name1: rm1,
    rm_name2: rm2,
    department: { deptId: parseInt(deptId) }
  };

  try {
    const res = await fetch(`${BASE_API}/managers`, {
      method: 'POST',
      headers: getAuthHeaders(),   //added jwt token
      body: JSON.stringify(body)
    });

    if (res.ok) {
      showAlert("✅ Reporting Manager Added Successfully.", "success");
      document.getElementById('rmForm').reset();
      loadReportingManagers();
    } else {
      const msg = await res.text();
      console.error("Error saving:", msg);
      showAlert("❌ Failed to save.", "danger");
    }
  } catch (err) {
    console.error("Save error:", err);
    showAlert("❌ Save error occurred.", "danger");
  }
}

// Load all reporting managers
async function loadReportingManagers() {
  try {
    const res = await fetch(`${BASE_API}/managers`, {
         headers: getAuthHeaders(false)    //added jwt token
    });
    const data = await res.json();
    const tbody = document.querySelector('#rmTable tbody');

         // Destroy existing DataTable instance before replacing content
         if ($.fn.DataTable.isDataTable('#rmTable')) {
          $('#rmTable').DataTable().destroy();
        }

    if (!tbody) {
      console.error("Table body not found for #rmTable");
      return;
    }

    tbody.innerHTML = data.map(rm => `
      <tr>
        <td>${rm.rm_id}</td>
        <td>${rm.department?.dept_name || ''}</td>
        <td>${rm.rm_name1}</td>
        <td>${rm.rm_name2}</td>
        <td>
          <i class="fa-solid fa-pen-to-square text-primary me-2" onclick="editRM(${rm.rm_id})" style="cursor: pointer; font-size: 18px;"></i>
          <i class="fa-solid fa-trash text-danger" onclick="openDeleteModal(${rm.rm_id})" style="cursor: pointer; font-size: 18px;"></i>
        </td>
      </tr>
    `).join('');

       // Reinitialize DataTable after DOM update
       $('#rmTable').DataTable();

  } catch (err) {
    console.error("Failed to load reporting managers:", err);
  }
}

// Delete confirmation modal
function openDeleteModal(id) {
  deleteRMId = id;
  const modal = new bootstrap.Modal(document.getElementById('deleteConfirmModal'));
  modal.show();
}

// Confirm delete
async function confirmDelete() {
  if (!deleteRMId) return;

  try {
    const res = await fetch(`${BASE_API}/managers/${deleteRMId}`, {
      method: 'DELETE',
      headers: getAuthHeaders(false)   //added jwt token
    });

    const modal = bootstrap.Modal.getInstance(document.getElementById('deleteConfirmModal'));
    modal.hide();

    if (res.ok) {
      loadReportingManagers();
      showAlert("❌ Record deleted successfully.", "danger");
    } else {
      showAlert("✔ Record deleted successfully.", "danger");
      loadReportingManagers();
    }
  } catch (err) {
    console.error("Delete error:", err);
    showAlert("❌ Error occurred while deleting.", "danger");
  } finally {
    deleteRMId = null;
  }
}

// Edit reporting manager
async function editRM(id) {
  try {
    const res = await fetch(`${BASE_API}/managers/${id}`, {
         headers: getAuthHeaders(false)   //added jwt token
    });
    const data = await res.json();

    document.getElementById('edit_rm_id').value = data.rm_id;

    const depRes = await fetch(`${BASE_API}/departments`, {
      headers: getAuthHeaders(false)    //added jwt token
    });
    const departments = await depRes.json();
    const depSelect = document.getElementById('edit_dep_name');
    depSelect.innerHTML = `<option value="" disabled selected>-Select Department-</option>` +
      departments.map(dep => `<option value="${dep.deptId}">${dep.dept_name}</option>`).join('');
    depSelect.value = data.department.deptId;

    await loadEditEmployees(data.department.deptId);

    document.getElementById('edit_rm1').value = data.rm_name1;
    document.getElementById('edit_rm2').value = data.rm_name2;

    const modal = new bootstrap.Modal(document.getElementById('editRMModal'));
    modal.show();
  } catch (err) {
    console.error("Error loading RM for edit:", err);
  }
}

// Load employees for edit modal
async function loadEditEmployees(deptId) {
  try {
    const res = await fetch(`${BASE_API}/employees/by-departments/${deptId}`, {
        headers: getAuthHeaders(false)   //added jwt token
    });
    const data = await res.json();

    // const rm1 = document.getElementById('edit_rep_name1');
    // const rm2 = document.getElementById('edit_rep_name2');

    if (data.length === 0) {
      const noDataOption = `<option value="" disabled selected>No employees found</option>`;
      rm1.innerHTML = noDataOption;
      rm2.innerHTML = noDataOption;
      return;
    }

    const options = data.map(emp =>
      `<option value="${emp.first_name} ${emp.last_name}">${emp.first_name} ${emp.last_name}</option>`
    ).join('');

    // rm1.innerHTML = `<option value="" disabled selected>-Select Manager 1-</option>` + options;
    // rm2.innerHTML = `<option value="" disabled selected>-Select Manager 2-</option>` + options;

  } catch (err) {
    console.error("Error loading employees:", err);
  }
}

// Update reporting manager
async function updateReportingManager(e) {
  e.preventDefault();

  const id = document.getElementById('edit_rm_id').value;
  const deptId = document.getElementById('edit_dep_name').value;
  const rm1 = document.getElementById('edit_rm1').value;
  const rm2 = document.getElementById('edit_rm2').value;

  const body = {
    rm_id: parseInt(id),
    rm_name1: rm1,
    rm_name2: rm2,
    department: { deptId: parseInt(deptId) }
  };

  try {
    const res = await fetch(`${BASE_API}/managers/${id}`, {
      method: 'PUT',
      headers: getAuthHeaders(),   //added jwt token
      body: JSON.stringify(body)
    });

    if (res.ok) {
      showAlert("✅ Reporting Manager updated successfully.", "success");
      const modal = bootstrap.Modal.getInstance(document.getElementById('editRMModal'));
      modal.hide();
      loadReportingManagers();
    } else {
      const msg = await res.text();
      console.error("Update failed:", msg);
      showAlert("❌ Failed to update Reporting Manager.", "danger");
    }
  } catch (err) {
    console.error("Update error:", err);
    showAlert("❌ Error updating record.", "danger");
  }
}

// Show Bootstrap alert
function showAlert(message, type = 'success') {
  const alertBox = `
    <div class="alert alert-${type} alert-dismissible fade show" role="alert">
      ${message}
      <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
    </div>
  `;
  document.getElementById('alertPlaceholder').innerHTML = alertBox;
  setTimeout(() => {
    const alert = document.querySelector('.alert');
    if (alert) bootstrap.Alert.getOrCreateInstance(alert).close();
  }, 4000);
}

// Init
window.addEventListener('DOMContentLoaded', () => {
  loadDepartments();
  loadReportingManagers();
  document.getElementById('editRMForm').addEventListener('submit', updateReportingManager);
  document.getElementById('edit_dep_name').addEventListener('change', function () {
    const deptId = this.value;
    if (deptId) {
      loadEditEmployees(deptId);
    }
  });
  document.getElementById('confirmDeleteBtn').addEventListener('click', confirmDelete);
});


function scrollTable(direction) {
  const container = document.getElementById("scrollTableContainer").querySelector(".custom-scroll");
  const scrollAmount = 200;

  if (direction === "left") {
    container.scrollLeft -= scrollAmount;
  } else {
    container.scrollLeft += scrollAmount;
  }
}
