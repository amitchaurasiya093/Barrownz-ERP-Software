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

const API = "http://localhost:8080/api/types";

let deleteTypeId = null; // Store delete type ID

document.addEventListener("DOMContentLoaded", function () {
  loadTypes();

  // ADD NEW TYPE
  document.getElementById("taskTypeForm").addEventListener("submit", function (e) {
    e.preventDefault();

    const name = document.getElementById("type_name").value;

    const data = {
      typeName: name
    };

    fetch(API, {
      method: "POST",
      headers: getAuthHeaders(),
      body: JSON.stringify(data)
    })
      .then(res => res.json())
      .then(() => {
        this.reset();
        loadTypes();
        showAlert("Task Type added successfully!");
      });
  });

  // UPDATE TYPE
  document.getElementById("editForm").addEventListener("submit", function (e) {
    e.preventDefault();

    const id = document.getElementById("edit_type_id").value;
    const name = document.getElementById("edit_tasktype").value;

    const data = {
      typeName: name
    };

    fetch(`${API}/${id}`, {
      method: "PUT",
      headers: getAuthHeaders(), 
      body: JSON.stringify(data)
    })
      .then(res => res.json())
      .then(() => {
        const modal = bootstrap.Modal.getInstance(document.getElementById("editTaskTypeModal"));
        modal.hide();
        loadTypes();
        showAlert("Task Type updated successfully!");
      });
  });

  // CONFIRM DELETE
  document.getElementById("confirmDeleteBtn").addEventListener("click", () => {
    if (deleteTypeId !== null) {
      fetch(`${API}/${deleteTypeId}`, {
        method: "DELETE",
        headers: getAuthHeaders()
      })
        .then(() => {
          deleteTypeId = null;
          loadTypes();

          const modalEl = document.getElementById("deleteConfirmModal");
          const modal = bootstrap.Modal.getInstance(modalEl);
          modal.hide();

          showAlert("Task Type deleted successfully!");
        });
    }
  });
});

// LOAD TYPES TO TABLE
function loadTypes() {
  fetch(API, {
    headers: getAuthHeaders()
  })
    .then(res => res.json())
    .then(types => {
      const tableBody = document.getElementById("typeTableBody");

        // Destroy existing DataTable instance before replacing content
        if ($.fn.DataTable.isDataTable('#task-type-table')) {
          $('#task-type-table').DataTable().destroy();
        }

      tableBody.innerHTML = "";

      types.forEach((type, index) => {
        tableBody.innerHTML += `
          <tr>
            <td class="text-center">${index + 1}</td>
            <td class="text-center">${type.typeName}</td>
            <td class="text-center">
              <i class="fa-solid fa-pen-to-square text-primary me-2"
                 onclick='editType(${JSON.stringify(type)})'
                 data-bs-toggle="modal" data-bs-target="#editTaskTypeModal"
                 style="cursor: pointer; font-size: 18px;"></i>
              <i class="fa-solid fa-trash text-danger"
                 onclick="openDeleteModal(${type.typeId})"
                 style="cursor: pointer; font-size: 18px;"></i>
            </td>
          </tr>
        `;
      });
       // Reinitialize DataTable after DOM update
       $('#task-type-table').DataTable();
    });
}

// OPEN EDIT MODAL
function editType(type) {
  document.getElementById("edit_type_id").value = type.typeId;
  document.getElementById("edit_tasktype").value = type.typeName;
}

// OPEN DELETE MODAL
function openDeleteModal(id) {
  deleteTypeId = id;
  const deleteModal = new bootstrap.Modal(document.getElementById("deleteConfirmModal"));
  deleteModal.show();
}

// BOOTSTRAP ALERT
function showAlert(message) {
  const alertBox = document.getElementById("alertBox");
  const alertMessage = document.getElementById("alertMessage");

  alertMessage.textContent = message;
  alertBox.classList.remove("d-none");

  // Auto hide after 3 seconds
  setTimeout(() => {
    alertBox.classList.add("d-none");
  }, 3000);
}
