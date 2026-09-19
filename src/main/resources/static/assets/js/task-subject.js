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

const API = "http://localhost:8080/api/subjects";

let deleteSubjectId = null; // For storing ID to delete

document.addEventListener("DOMContentLoaded", function () {
  loadSubjects();

  // ADD NEW SUBJECT
  document.getElementById("taskSubjectForm").addEventListener("submit", function (e) {
    e.preventDefault();

    const name = document.getElementById("subject_name").value;

    const data = {
      subjectName: name
    };

    fetch(API, {
      method: "POST",
      headers: getAuthHeaders(),  //added jwt topken
      body: JSON.stringify(data)
    })
      .then(res => res.json())
      .then(() => {
        this.reset();
        loadSubjects();
        showAlert("Subject added successfully!");
      });
  });

  // UPDATE SUBJECT
  document.getElementById("editForm").addEventListener("submit", function (e) {
    e.preventDefault();

    const id = document.getElementById("edit_subject_id").value;
    const name = document.getElementById("edit_subject_name").value;

    const data = {
      subjectName: name
    };

    fetch(`${API}/${id}`, {
      method: "PUT",
      headers: getAuthHeaders(),
      body: JSON.stringify(data)
    })
      .then(res => res.json())
      .then(() => {
        const modal = bootstrap.Modal.getInstance(document.getElementById("editSubjectModal"));
        modal.hide();
        loadSubjects();
        showAlert("Subject updated successfully!");
      });
  });

  // CONFIRM DELETE
  document.getElementById("confirmDeleteBtn").addEventListener("click", () => {
    if (deleteSubjectId !== null) {
      fetch(`${API}/${deleteSubjectId}`, {
        method: "DELETE",
        headers: getAuthHeaders()
      })
        .then(() => {
          deleteSubjectId = null;
          loadSubjects();

          const modalEl = document.getElementById("deleteConfirmModal");
          const modal = bootstrap.Modal.getInstance(modalEl);
          modal.hide();

          showAlert("Subject deleted successfully!");
        });
    }
  });
});

// LOAD ALL SUBJECTS
function loadSubjects() {
  fetch(API, {
      headers: getAuthHeaders() //  Added auth headers
  })
    .then(res => res.json())
    .then(subjects => {
      const tableBody = document.getElementById("subjectTableBody");

        // Destroy existing DataTable instance before replacing content
        if ($.fn.DataTable.isDataTable('#subject-table')) {
          $('#subject-table').DataTable().destroy();
        }

      tableBody.innerHTML = "";

      subjects.forEach((subject, index) => {
        tableBody.innerHTML += `
          <tr>
            <td class="text-center">${index + 1}</td>
            <td class="text-center">${subject.subjectName}</td>
            <td class="text-center">
              <i class="fa-solid fa-pen-to-square text-primary me-2"
                 onclick='editSubject(${JSON.stringify(subject)})'
                 data-bs-toggle="modal" data-bs-target="#editSubjectModal"
                 style="cursor: pointer; font-size: 18px;"></i>
              <i class="fa-solid fa-trash text-danger"
                 onclick="openDeleteModal(${subject.subjectId})"
                 style="cursor: pointer; font-size: 18px;"></i>
            </td>
          </tr>
        `;
      });
       // Reinitialize DataTable after DOM update
       $('#subject-table').DataTable();
    });
}

// OPEN EDIT MODAL
function editSubject(subject) {
  document.getElementById("edit_subject_id").value = subject.subjectId;
  document.getElementById("edit_subject_name").value = subject.subjectName;
}

// OPEN DELETE MODAL
function openDeleteModal(id) {
  deleteSubjectId = id;
  const deleteModal = new bootstrap.Modal(document.getElementById("deleteConfirmModal"));
  deleteModal.show();
}

// SHOW BOOTSTRAP ALERT
function showAlert(message) {
  const alertBox = document.getElementById("alertBox");
  const alertMessage = document.getElementById("alertMessage");

  alertMessage.textContent = message;
  alertBox.classList.remove("d-none");

  // Auto-hide after 3 seconds
  setTimeout(() => {
    alertBox.classList.add("d-none");
  }, 3000);
}
