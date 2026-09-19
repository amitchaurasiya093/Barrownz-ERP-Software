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

const PRIORITY_API = "http://localhost:8080/api/priority";
let deleteId = null; // used for delete tracking

document.addEventListener("DOMContentLoaded", () => {
  loadPriorities();

  const form = document.getElementById("priorityForm");
  const editForm = document.getElementById("editPriorityForm");
  const deleteBtn = document.getElementById("confirmDeleteProject");

  //  Create new priority
  form.addEventListener("submit", function (e) {
    e.preventDefault();

    const name = document.getElementById("priority_name").value.trim();
    if (!name) return showAlert("Priority name is required.", "error");

    fetch(PRIORITY_API, {
      method: "POST",
      headers: getAuthHeaders(), 
      body: JSON.stringify({ priorityName: name })
    })
      .then(res => res.json())
      .then(() => {
        form.reset();
        loadPriorities();
        showAlert("Priority added successfully!");
      })
      .catch(() => showAlert("Failed to add priority.", "error"));
  });

  // ✅ Update priority
  editForm.addEventListener("submit", function (e) {
    e.preventDefault();

    const id = document.getElementById("edit_priority_id").value;
    const name = document.getElementById("edit_priority_name").value.trim();

    if (!name) return showAlert("Priority name is required.", "error");

    fetch(`${PRIORITY_API}/${id}`, {
      method: "PUT",
      headers: getAuthHeaders(),
      body: JSON.stringify({ priorityName: name })
    })
      .then(res => res.json())
      .then(() => {
        const modal = bootstrap.Modal.getInstance(document.getElementById("editPriorityModal"));
        modal.hide();
        loadPriorities();
        showAlert("Priority updated successfully!");
      })
      .catch(() => showAlert("Failed to update priority.", "error"));
  });

  // ✅ Confirm Delete
  deleteBtn.addEventListener("click", () => {
    if (deleteId !== null) {
      fetch(`${PRIORITY_API}/${deleteId}`, {
        method: "DELETE",
        headers: getAuthHeaders()
      })
        .then(() => {
          const modal = bootstrap.Modal.getInstance(document.getElementById("deleteProjectModal"));
          modal.hide();
          loadPriorities();
          showAlert("Priority deleted successfully!", "error");
        })
        .catch(() => showAlert("Failed to delete priority.", "error"));
    }
  });
});

//  Load all priorities
function loadPriorities() {
  fetch(PRIORITY_API, {
    headers: getAuthHeaders()
  })
    .then(res => res.json())
    .then(data => {
      const tbody = document.getElementById("priorityTableBody");

       // Destroy existing DataTable instance before replacing content
       if ($.fn.DataTable.isDataTable('#priorityTable')) {
        $('#priorityTable').DataTable().destroy();
      }

      tbody.innerHTML = "";

      data.forEach((p, index) => {
        tbody.innerHTML += `
          <tr>
            <td class="text-center">${index + 1}</td>
            <td class="text-center">${p.priorityName}</td>
            <td class="text-center">
              <i class="fa-solid fa-pen-to-square text-primary me-2" style="cursor:pointer" onclick="editPriority(${p.priorityId}, '${p.priorityName}')"></i>
              <i class="fa-solid fa-trash text-danger" style="cursor:pointer" onclick="confirmDelete(${p.priorityId})"></i>
            </td>
          </tr>
        `;
      });
        // Reinitialize DataTable after DOM update
        $('#priorityTable').DataTable();
    });
}

// ✏️ Populate Edit Modal
function editPriority(id, name) {
  document.getElementById("edit_priority_id").value = id;
  document.getElementById("edit_priority_name").value = name;

  const modal = new bootstrap.Modal(document.getElementById("editPriorityModal"));
  modal.show();
}

// ❌ Confirm delete modal trigger
function confirmDelete(id) {
  deleteId = id;
  const modal = new bootstrap.Modal(document.getElementById("deleteProjectModal"));
  modal.show();
}

// ✅ Bootstrap Alert
function showAlert(message, type = "success") {
  let alertBox = document.getElementById("alertBox");
  let alertMsg = document.getElementById("alertMessage");

  if (!alertBox) {
    alertBox = document.createElement("div");
    alertBox.id = "alertBox";
    alertBox.className = "alert alert-dismissible fade show position-fixed top-0 start-50 translate-middle-x mt-3";
    alertBox.style.zIndex = "9999";
    alertBox.innerHTML = `<strong id="alertMessage"></strong><button type="button" class="btn-close" data-bs-dismiss="alert"></button>`;
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
