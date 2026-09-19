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

document.addEventListener('DOMContentLoaded', () => {
    const form = document.getElementById('holidayForm');
    const titleInput = document.getElementById('holiday-title');
    const dateInput = document.getElementById('holiday-date');
    const statusInput = document.getElementById('holiday-status');
    const tableBody = document.getElementById("holidayTableBody");
  
    const editId = document.getElementById('edit-holiday-id');
    const editTitle = document.getElementById('edit-holiday-title');
    const editDate = document.getElementById('edit-holiday-date');
    const editStatus = document.getElementById('edit-holiday-status');
    let deleteId = null;
  
  // --------------------------
  // LOAD HOLIDAYS ON PAGE LOAD
  // --------------------------
    fetchAllHolidays();
  
  // -------------------------------------------------
  // AUTO-SET STATUS BASED ON DATE INPUT (INSERT FORM)
  // -------------------------------------------------
  
    dateInput.addEventListener('change', () => {
      statusInput.value = calculateStatus(dateInput.value);
    });
  
  // -----------------------------------------------
  // AUTO-SET STATUS BASED ON DATE INPUT (EDIT FORM)
  // -----------------------------------------------
    editDate.addEventListener('change', () => {
      editStatus.value = calculateStatus(editDate.value);
    });
  
  //-------------
  // ADD HOLIDAY
  // ------------
  
    form.addEventListener('submit', (e) => {
      e.preventDefault();
      const data = {
        title: titleInput.value,
        date: dateInput.value,
        status: statusInput.value
      };
  
      fetch('/api/holidays', {
        method: 'POST',
        headers: getAuthHeaders(),
        body: JSON.stringify(data)
      })
      .then(res => res.json())
      .then(() => {
        form.reset();
        fetchAllHolidays();
        showAlert('Holiday added successfully!');
      });
    });
  
  // -----------------------------
  // FETCH & DISPLAY ALL HOLIDAYS
  // ----------------------------- 
    function fetchAllHolidays() {
      fetch("/api/holidays/all", {
          headers: getAuthHeaders()
      })
        .then((response) => response.json())
        .then((data) => {
          tableBody.innerHTML = "";
          data.forEach((holiday) => {
            const row = document.createElement("tr");
            row.innerHTML = `
              <td class="text-center">${holiday.id}</td>
              <td class="text-center">${holiday.title}</td>
              <td class="text-center">${holiday.date}</td>
              <td class="text-center">
                <button class=" rounded-5 btn btn-sm ${holiday.status === "Upcoming Holiday" ? "btn-success" : "btn-danger"}">${holiday.status}</button>
              </td>
              <td class="text-center">
  
                  <i class="fa-solid fa-pen-to-square text-primary me-2" onclick="openEditModal(${holiday.id})" style="cursor: pointer; font-size: 18px;"></i>
                  <i class="fa-solid fa-trash text-danger" onclick="openDeleteModal(${holiday.id})" style="cursor: pointer; font-size: 18px;"></i>
              </td>
            `;
            tableBody.appendChild(row);
          });
        });
    }
  
  //---------------
  // UPDATE HOLIDAY
  // --------------
    document.getElementById('updateHolidayBtn').addEventListener('click', () => {
      const data = {
        title: editTitle.value,
        date: editDate.value,
        status: editStatus.value
      };
  
      fetch(`/api/holidays/${editId.value}`, {
        method: 'PUT',
        headers: getAuthHeaders(),
        body: JSON.stringify(data)
      })
      .then(res => res.json())
      .then(() => {
        bootstrap.Modal.getInstance(document.getElementById('editHolidayModal')).hide();
        fetchAllHolidays();
        showAlert('Holiday updated successfully!');
      });
    });
  
  // --------------
  // DELETE HOLIDAY
  // --------------
    document.getElementById('deleteHolidayBtn').addEventListener('click', () => {
      fetch(`/api/holidays/${deleteId}`, {
        method: 'DELETE',
        headers: getAuthHeaders() 
      })
      .then(() => {
        bootstrap.Modal.getInstance(document.getElementById('deleteConfirmModal')).hide();
        fetchAllHolidays();
        showAlert('Holiday deleted successfully!', 'danger');
      });
    });
  
  // ----------
  // SHOW ALERT
  // ----------
    function showAlert(message, type = 'success') {
      const alertHtml = `
        <div class="alert alert-${type} alert-dismissible fade show" role="alert">
          ${message}
          <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>`;
      document.getElementById('alertPlaceholder').innerHTML = alertHtml;
  
      setTimeout(() => {
        const alert = document.querySelector('.alert');
        if (alert) alert.remove();
      }, 3000);
    }
  
  // ------------------------------
  // CALCULATE STATUS BASED ON DATE
  // ------------------------------
    function calculateStatus(dateStr) {
      const date = new Date(dateStr);
      const today = new Date();
      today.setHours(0, 0, 0, 0);
      return date >= today ? "Upcoming Holiday" : "Ended Holiday";
    }
  
  // ----------------
  // EDIT MODEL LOGIC
  // ----------------
  
   window.openEditModal = function(id) {
    fetch(`/api/holidays/${id}`, {
      headers: getAuthHeaders()
    })
      .then(res => res.json())
      .then(h => {
        editId.value = h.id;
        editTitle.value = h.title;
        editDate.value = h.date;
        editStatus.value = h.status;
        new bootstrap.Modal(document.getElementById('editHolidayModal')).show();
      });
  };
  
  // ------------------
  // DELETE MODEL LOGIC
  // ------------------
    window.openDeleteModal = function(id) {
      deleteId = id;
      new bootstrap.Modal(document.getElementById('deleteConfirmModal')).show();
    };
  });

  console.log("hello");
  