const apiUrl = "http://localhost:8080/api/departments";

window.onload = fetchDepartment;

let deptIdToDelete = null; // For delete modal

function fetchDepartment() {
  fetch(apiUrl, {
    headers: {
      'Authorization': `Bearer ${localStorage.getItem('token')}`
    }
  })
    .then(res => res.json())
    .then(data => {
      const list = document.getElementById("departmentList");

        // Destroy existing DataTable instance before replacing content
        if ($.fn.DataTable.isDataTable('#dept-list-table')) {
          $('#dept-list-table').DataTable().destroy();
        }


      list.innerHTML = "";

      data.forEach(dept => {
        const row = document.createElement("tr");
        row.innerHTML = `
          <td>${dept.deptId}</td>
          <td>${dept.dept_name}</td>
          <td>
            <i class="fa-solid fa-pen-to-square text-primary me-2"
               onclick="editDepartment(${dept.deptId}, '${dept.dept_name}')"
               style="cursor: pointer; font-size: 18px;"></i>
            <i class="fa-solid fa-trash text-danger"
               onclick="deleteDepartment(${dept.deptId})"
               style="cursor: pointer; font-size: 18px;"></i>
          </td>
        `;
        list.appendChild(row);
      });

      // Reinitialize DataTable after DOM update
      $('#dept-list-table').DataTable();

    })
    .catch(err => console.error("Fetch error:", err));
}

function saveDepartment(event) {
  event.preventDefault();

  const id = document.getElementById("dept_id").value;
  const dept_name = document.getElementById("dept_name").value.trim();

  if (!dept_name) {
    alert("Please enter department name");
    return;
  }

  const method = id ? "PUT" : "POST";
  const url = id ? `${apiUrl}/${id}` : apiUrl;
  const bodyData = id ? { deptId: parseInt(id), dept_name } : { dept_name };

  fetch(url, {
    method: method,
    headers: {
      "Content-Type": "application/json",
      "Authorization": `Bearer ${localStorage.getItem('token')}`
    },
    body: JSON.stringify(bodyData)
  })
    .then(response => {
      if (!response.ok) throw new Error("Failed to save");
      return response.json();
    })
    .then(() => {
      resetForm();
      fetchDepartment();
      if (!id) showAlert("✅ Department added successfully!", "success");
    })
    .catch(err => console.error("Save error:", err));
}

function resetForm() {
  document.getElementById("dept_id").value = "";
  document.getElementById("dept_name").value = "";
}

// Bootstrap DELETE confirmation modal
function deleteDepartment(id) {
  deptIdToDelete = id;
  const modal = new bootstrap.Modal(document.getElementById("deleteConfirmModal"));
  modal.show();
}

// Confirm delete button click
document.addEventListener("DOMContentLoaded", () => {
  document.getElementById("confirmDeleteBtn").addEventListener("click", () => {
    if (deptIdToDelete !== null) {
      fetch(`${apiUrl}/${deptIdToDelete}`, {
        method: "DELETE",
        headers: {
          "Authorization": `Bearer ${localStorage.getItem('token')}`
        }
      })
        .then(() => {
          fetchDepartment();
          deptIdToDelete = null;
          bootstrap.Modal.getInstance(document.getElementById("deleteConfirmModal")).hide();
          showAlert("🗑️ Department deleted successfully", "danger");
        })
        .catch(err => console.error("Delete error:", err));
    }
  });
});

// Edit modal show & populate
function editDepartment(id, name) {
  document.getElementById("edit_dept_id").value = id;
  document.getElementById("edit_dept_name").value = name;

  const editModal = new bootstrap.Modal(document.getElementById("editDepartmentModal"));
  editModal.show();
}

// Edit modal form submit
document.getElementById("editForm").addEventListener("submit", function (e) {
  e.preventDefault();

  const id = document.getElementById("edit_dept_id").value;
  const dept_name = document.getElementById("edit_dept_name").value.trim();

  if (!dept_name) return;

  fetch(`${apiUrl}/${id}`, {
    method: "PUT",
    headers: {
      "Content-Type": "application/json",
      "Authorization": `Bearer ${localStorage.getItem('token')}`
    },
    body: JSON.stringify({ deptId: parseInt(id), dept_name })
  })
    .then(response => {
      if (!response.ok) throw new Error("Failed to update");
      return response.json();
    })
    .then(() => {
      bootstrap.Modal.getInstance(document.getElementById("editDepartmentModal")).hide();
      fetchDepartment();
      showAlert("✅ Record Updated Successfully!", "success");
    })
    .catch(err => console.error("Update error:", err));
});

// Reusable alert message
function showAlert(message, type = "success") {
  const wrapper = document.createElement("div");
  wrapper.innerHTML = `
    <div class="alert alert-${type} alert-dismissible fade show" role="alert">
      ${message}
      <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
    </div>`;
  const placeholder = document.getElementById("alertPlaceholder");
  placeholder.innerHTML = "";
  placeholder.appendChild(wrapper);

  setTimeout(() => {
    const alertInstance = bootstrap.Alert.getOrCreateInstance(wrapper.querySelector(".alert"));
    if (alertInstance) alertInstance.close();
  }, 3000);
}
