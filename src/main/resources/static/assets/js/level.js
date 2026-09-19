
//helper function for generating headers
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



const apiUrl = "http://localhost:8080/api/levels";
let levelToDeleteId = null;

window.onload = function () {
    loadLevels();

    document.getElementById("addLevelForm").addEventListener("submit", saveLevel);
    document.getElementById("editLevelForm").addEventListener("submit", submitEditLevel);
    document.getElementById("confirmDeleteBtn").addEventListener("click", confirmDeleteLevel);
};

// ===================== INSERT =====================

function saveLevel(event) {
    event.preventDefault();

    const levelValue = parseFloat(document.getElementById("level_number").value);
    if (isNaN(levelValue)) {
        alert("Please enter a valid level number.");
        return;
    }

    const levelData = { level: levelValue };

    fetch(apiUrl, {
        method: "POST",
        // headers: { "Content-Type": "application/json" },
        headers: getAuthHeaders(),    //added jwt token
        body: JSON.stringify(levelData)
    })
        .then(response => {
            if (!response.ok) throw new Error("Failed to insert level");
            return response.json();
        })
        .then(() => {
            document.getElementById("addLevelForm").reset();
            loadLevels();
            showTopAlert("✅ Level added successfully!", "success");
        })
        .catch(error => {
            console.error("Insert error:", error);
            alert("Error inserting level");
        });
}

// ===================== LOAD =====================

function loadLevels() {
    fetch(apiUrl, {
        headers: getAuthHeaders(false)   //added jwt token
    })
        .then(res => res.json())
        .then(data => {
            const tbody = document.getElementById("levelTableBody");

              // Destroy existing DataTable instance before replacing content
           if ($.fn.DataTable.isDataTable('#level-list-table')) {
            $('#level-list-table').DataTable().destroy();
          }

            tbody.innerHTML = "";

            data.forEach(level => {
                const row = `<tr>
          <td>${level.level_id}</td>
          <td>${level.level}</td>
          <td>
            <i class="fa-solid fa-pen-to-square text-primary me-2"
               onclick="editLevel(${level.level_id})"
               style="cursor: pointer; font-size: 18px;"></i>
            <i class="fa-solid fa-trash text-danger"
               onclick="deleteLevel(${level.level_id})"
               style="cursor: pointer; font-size: 18px;"></i>
          </td>
        </tr>`;
                tbody.innerHTML += row;
            });

             // Reinitialize DataTable after DOM update
      $('#level-list-table').DataTable();

   
      

        })
        .catch(error => console.error("Load error:", error));
}

// ===================== EDIT =====================

function editLevel(id) {
    fetch(`${apiUrl}/${id}`, {
        headers: getAuthHeaders(false)
    })
        .then(res => res.json())
        .then(level => {
            document.getElementById("edit_level_id").value = level.level_id;
            document.getElementById("edit_level_number").value = level.level;

            const editModal = new bootstrap.Modal(document.getElementById("editLevelModal"));
            editModal.show();
        })
        .catch(error => {
            console.error("Fetch error:", error);
            alert("Failed to fetch level data");
        });
}

function submitEditLevel(event) {
    event.preventDefault();

    const id = document.getElementById("edit_level_id").value;
    const levelValue = parseFloat(document.getElementById("edit_level_number").value);

    if (isNaN(levelValue)) {
        alert("Please enter a valid number.");
        return;
    }

    const levelData = { level: levelValue };

    fetch(`${apiUrl}/${id}`, {
        method: "PUT",
        // headers: { "Content-Type": "application/json" },
        headers: getAuthHeaders(),
        body: JSON.stringify(levelData)
    })
        .then(response => {
            if (!response.ok) throw new Error("Failed to update");
            return response.json();
        })
        .then(() => {
            loadLevels();
            const modal = bootstrap.Modal.getInstance(document.getElementById("editLevelModal"));
            modal.hide();
            showTopAlert("✅ Level updated successfully!", "success");
        })
        .catch(error => {
            console.error("Update error:", error);
            alert("Error updating level");
        });
}

// ===================== DELETE =====================

function deleteLevel(id) {
    levelToDeleteId = id;
    const deleteModal = new bootstrap.Modal(document.getElementById("deleteConfirmModal"));
    deleteModal.show();
}

function confirmDeleteLevel() {
    if (!levelToDeleteId) return;

    fetch(`${apiUrl}/${levelToDeleteId}`, {
        method: "DELETE",
        headers: getAuthHeaders(false)
    })
        .then(response => {
            console.log("DELETE response status:", response.status);

            const deleteModal = bootstrap.Modal.getInstance(document.getElementById("deleteConfirmModal"));
            deleteModal.hide();

            levelToDeleteId = null;
            loadLevels();
            showTopAlert("✅ Level deleted successfully!", "danger");
        })
        .catch(error => {
            console.error("Delete error:", error);
            alert("Error deleting level");
        });
}

// ===================== ALERT =====================

function showTopAlert(message, type = "success") {
    const alertBox = document.getElementById("topAlert");
    const msg = document.getElementById("topAlertMessage");
    alertBox.classList.remove("alert-success", "alert-danger", "alert-warning", "alert-info");
    alertBox.classList.add(`alert-${type}`);
    msg.textContent = message;
    alertBox.classList.remove("d-none");

    setTimeout(() => {
        alertBox.classList.add("d-none");
    }, 3000);
}

function hideTopAlert() {
    document.getElementById("topAlert").classList.add("d-none");
}
