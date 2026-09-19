
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
  

const apiUrl = 'http://localhost:8080/api/leavetypes';

document.addEventListener("DOMContentLoaded", () => {
    loadLeaveTypes();

    document.getElementById("leaveTypeForm").addEventListener("submit", (e) => {
        e.preventDefault();
        addLeaveType();
    });

    document.getElementById("editForm").addEventListener("submit", (e) => {
        e.preventDefault();
        const id = document.getElementById("edit_dept_id").value;
        updateLeaveType(id);
    });

    document.getElementById("confirmDeleteBtn").addEventListener("click", () => {
        deleteLeaveType(confirmDeleteId);
    });
});

function loadLeaveTypes() {
    fetch(apiUrl, {
          headers: getAuthHeaders()   //added jwt token
    })
        .then(response => response.json())
        .then(data => {
            const tbody = document.getElementById("leaveTypeTableBody");

                  // Destroy existing DataTable instance before replacing content
           if ($.fn.DataTable.isDataTable('#level-list-table')) {
            $('#leaveTypesTable').DataTable().destroy();
          }

            tbody.innerHTML = "";

            data.forEach(leave => {
                const tr = document.createElement("tr");

                tr.innerHTML = `
                    <td class="text-center">${leave.leavetypeId}</td>
                    <td class="text-center">${leave.leaveType}</td>

                    <td class="text-center">${leave.allowedForProbation ? '✅ Yes' : '❌ No'}</td>
                    <td class="text-center">${leave.requiresBalance ? '✅ Yes' : '❌ No'}</td>
                    <td class="text-center">
                        <i class="fa-solid fa-pen-to-square text-primary me-2"
                            onclick="showEditModal(${leave.leavetypeId})"
                            style="cursor: pointer; font-size: 18px;"></i>
                        <i class="fa-solid fa-trash text-danger"
                            onclick="showDeleteModal(${leave.leavetypeId})"
                            style="cursor: pointer; font-size: 18px;"></i>
                    </td>
                `;

                tbody.appendChild(tr);
            });
             // Reinitialize DataTable after DOM update
      $('#leaveTypesTable').DataTable();

        });
}

function addLeaveType() {
    const leaveTypeInput = document.getElementById("leave_type").value;
    const allowedForProbation = document.getElementById("allowedForProbation").checked;
               const requiresBalance = document.getElementById("requiresBalance").checked;

    fetch(apiUrl, {
        method: 'POST',
        headers: getAuthHeaders(),  //added jwt token
        body: JSON.stringify({ leaveType: leaveTypeInput,
              allowedForProbation: allowedForProbation,
              requiresBalance: requiresBalance
         })
    })
        .then(res => res.json())
        .then(() => {
            resetForm();
            loadLeaveTypes();
            showAlert("Leave Type added successfully!", "success");
        });
}

function showEditModal(id) {
    fetch(`${apiUrl}/${id}`, {
        headers: getAuthHeaders()  //added jwt token
    })
        .then(res => res.json())
        .then(data => {
            document.getElementById("edit_dept_id").value = data.leavetypeId;
            document.getElementById("level_type").value = data.leaveType;
            document.getElementById("edit_requiresBalance").checked = data.requiresBalance === true; 
            
           document.getElementById("edit_allowedForProbation").checked = data.allowedForProbation === true;

            new bootstrap.Modal(document.getElementById('editLeveltypeModal')).show();
        });
}

function updateLeaveType(id) {
    const updatedLeaveType = {
        leaveType: document.getElementById("level_type").value,
        allowedForProbation: document.getElementById("edit_allowedForProbation").checked,
        requiresBalance: document.getElementById("edit_requiresBalance").checked 
    };

    fetch(`${apiUrl}/${id}`, {
        method: 'PUT',
        headers: getAuthHeaders(),  //get jwt token
        body: JSON.stringify(updatedLeaveType)
    })
        .then(res => res.json())
        .then(() => {
            loadLeaveTypes();
            document.getElementById("editForm").reset();
            bootstrap.Modal.getInstance(document.getElementById('editLeveltypeModal')).hide();
            showAlert("Record Updated Successfully!", "success");
        });
}

let confirmDeleteId = null;

function showDeleteModal(id) {
    confirmDeleteId = id;
    new bootstrap.Modal(document.getElementById('deleteConfirmModal')).show();
}

function deleteLeaveType(id) {
    fetch(`${apiUrl}/${id}`, {
        method: 'DELETE',
        headers: getAuthHeaders() //added jwt token
    })
        .then(() => {
            loadLeaveTypes();
            bootstrap.Modal.getInstance(document.getElementById('deleteConfirmModal')).hide();
            showAlert("Record Deleted Successfully!", "danger");
        });
}

function resetForm() {
    document.getElementById("leaveTypeForm").reset();
    document.getElementById("leavetype_id").value = "";
}

function showAlert(message, type = "success") {
    const wrapper = document.createElement("div");
    wrapper.innerHTML = `
      <div class="alert alert-${type} alert-dismissible fade show" role="alert">
        ${message}
        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
      </div>`;
    const placeholder = document.getElementById("alert-placeholder");
    placeholder.innerHTML = "";
    placeholder.appendChild(wrapper);
  
    setTimeout(() => {
      const alertInstance = bootstrap.Alert.getOrCreateInstance(wrapper.querySelector(".alert"));
      if (alertInstance) alertInstance.close();
    }, 3000);
  }
