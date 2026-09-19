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
/////


const STATUS_API = "http://localhost:8080/api/status";
let deleteId = null;

document.addEventListener("DOMContentLoaded", () => {
  loadStatus();

  const form = document.getElementById("priorityForm");
  const editForm = document.getElementById("editStatusForm");
  const deleteBtn = document.getElementById("confirmDeleteProject");

  //  Add New Status
  form.addEventListener("submit", function (e) {
    e.preventDefault();
    const name = document.getElementById("status_name").value.trim();
    if (!name) return showAlert("Status name is required", "error");

    fetch(STATUS_API, {
      method: "POST",
      headers: getAuthHeaders(),
      body: JSON.stringify({ statusName: name })
    })
      .then(res => res.json())
      .then(() => {
        form.reset();
        loadStatus();
        showAlert("Status added successfully!");
      })
      .catch(() => showAlert("Failed to add status", "error"));
  });

  // Update Status
  editForm.addEventListener("submit", function (e) {
    e.preventDefault();

    const id = document.getElementById("edit_priority_id").value;
    const name = document.getElementById("edit_status_name").value.trim();
    if (!name) return showAlert("Status name is required", "error");

    fetch(`${STATUS_API}/${id}`, {
      method: "PUT",
      headers: getAuthHeaders(),
      body: JSON.stringify({ statusName: name })
    })
      .then(res => res.json())
      .then(() => {
        const modal = bootstrap.Modal.getInstance(document.getElementById("editStatusModal"));
        modal.hide();
        loadStatus();
        showAlert("Status updated successfully!");
      })
      .catch(() => showAlert("Failed to update status", "error"));
  });

  //  Confirm Delete
  deleteBtn.addEventListener("click", () => {
    if (deleteId !== null) {
      fetch(`${STATUS_API}/${deleteId}`, {
        method: "DELETE",
        headers: getAuthHeaders()
      })
        .then(() => {
          const modal = bootstrap.Modal.getInstance(document.getElementById("deleteProjectModal"));
          modal.hide();
          loadStatus();
          showAlert("Status deleted successfully!", "success");
        })
        .catch(() => showAlert("Failed to delete status", "error"));
    }
  });
});

//  Load all status records
function loadStatus() {
  fetch(STATUS_API, {
      headers: getAuthHeaders()
  })
    .then(res => res.json())
    .then(data => {
      const tbody = document.getElementById("statusTableBody");

        // Destroy existing DataTable instance before replacing content
        if ($.fn.DataTable.isDataTable('#statusTable')) {
          $('#statusTable').DataTable().destroy();
        }

      tbody.innerHTML = "";

      data.forEach((status, index) => {
        tbody.innerHTML += `
          <tr>
            <td class="text-center">${index + 1}</td>
            <td class="text-center">${status.statusName}</td>
            <td class="text-center">
              <i class="fa-solid fa-pen-to-square text-primary me-2" style="cursor:pointer" onclick="editStatus(${status.statusId}, '${status.statusName}')"></i>
              <i class="fa-solid fa-trash text-danger" style="cursor:pointer" onclick="confirmDelete(${status.statusId})"></i>
            </td>
          </tr>
        `;
      });
       // Reinitialize DataTable after DOM update
       $('#statusTable').DataTable();
    })
    .catch(() => showAlert("Failed to load statuses", "error"));
}

//  Open Edit Modal with Data
function editStatus(id, name) {
  document.getElementById("edit_priority_id").value = id;
  document.getElementById("edit_status_name").value = name;

  const modal = new bootstrap.Modal(document.getElementById("editStatusModal"));
  modal.show();
}

// 🗑️ Trigger Delete Modal
function confirmDelete(id) {
  deleteId = id;
  const modal = new bootstrap.Modal(document.getElementById("deleteProjectModal"));
  modal.show();
}

//  Show Alert at Top Center
function showAlert(message, type = "success") {
  let alertBox = document.getElementById("alertBox");
  let alertMsg = document.getElementById("alertMessage");

  if (!alertBox) {
    alertBox = document.createElement("div");
    alertBox.id = "alertBox";
    alertBox.className = "alert alert-dismissible fade show position-fixed top-0 start-50 translate-middle-x mt-3";
    alertBox.style.zIndex = "9999";
    alertBox.innerHTML = `
      <strong id="alertMessage"></strong>
      <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
    `;
    document.body.appendChild(alertBox);
    alertMsg = document.getElementById("alertMessage");
  }

  alertBox.classList.remove("alert-success", "alert-danger", "d-none");
  alertBox.classList.add(type === "error" ? "alert-danger" : "alert-success");
  alertMsg.textContent = message;

  setTimeout(() => {
    alertBox.classList.add("d-none");
  }, 3000);
}
