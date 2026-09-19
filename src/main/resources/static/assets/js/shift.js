// -------------------------------
// Shift Page Integration JS Start
// -------------------------------

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


const shiftApiUrl = "http://localhost:8080/api/shifts";
let deleteShiftId = null;

window.onload = fetchShifts;

document.getElementById("editShiftForm").addEventListener("submit", updateShift);
document.getElementById("shiftForm").addEventListener("submit", saveShift);

// Collect form values for add
function collectShiftFormValues() {
  return {
    shift_name: document.getElementById("shiftname").value,
    shift_code: document.getElementById("shiftcode").value,
    login_time: document.getElementById("login").value,
    logout_time: document.getElementById("loginoff").value,
    shift_hours: document.getElementById("shifthours").value,
    saturday_off: getRadioValue("permanentOn"),
    sunday_off: getRadioValue("sundayStatus"),
    holiday_Off: getRadioValue("holidayStatus"),
    session1_in: document.getElementById("session1_in").value,
    session1_out: document.getElementById("session1_out").value,
    session2_in: document.getElementById("session2_in").value,
    session2_out: document.getElementById("session2_out").value,
    session3_in: document.getElementById("session3_in").value,
    session3_out: document.getElementById("session3_out").value,
    session4_in: document.getElementById("session4_in").value,
    session4_out: document.getElementById("session4_out").value
  };
}

function fetchShifts() {
  fetch(shiftApiUrl, {
      headers: getAuthHeaders(false)      // added jwt token
  }) 
    .then(res => res.json())
    .then(data => {
      const tableBody = document.getElementById("shiftList");

           // Destroy existing DataTable instance before replacing content
           if ($.fn.DataTable.isDataTable('#shift-list-table')) {
            $('#shift-list-table').DataTable().destroy();
          }
  

      tableBody.innerHTML = "";

      data.forEach(shift => {
        const row = document.createElement("tr");
        row.innerHTML = `
          <td class="text-center">${shift.shift_id}</td>
          <td class="text-center">${shift.shift_name}</td>
          <td class="text-center">${shift.shift_code}</td>
          <td class="text-center">${shift.login_time}</td>
          <td class="text-center">${shift.logout_time}</td>
          <td class="text-center">${shift.shift_hours}</td>
          <td class="text-center">${shift.saturday_off}</td>
          <td class="text-center">${shift.sunday_off}</td>
          <td class="text-center">${shift.holiday_Off}</td>
          <td class="text-center">${shift.session1_in}</td>
          <td class="text-center">${shift.session1_out}</td>
          <td class="text-center">${shift.session2_in}</td>
          <td class="text-center">${shift.session2_out}</td>
          <td class="text-center">${shift.session3_in}</td>
          <td class="text-center">${shift.session3_out}</td>
          <td class="text-center">${shift.session4_in}</td>
          <td class="text-center">${shift.session4_out}</td>
          <td class="text-center">
            <i class="fa-solid fa-pen-to-square text-primary me-2"
               onclick="editShift(${shift.shift_id})"
               style="cursor: pointer; font-size: 18px;"></i>
            <i class="fa-solid fa-trash text-danger"
               onclick="confirmDelete(${shift.shift_id})"
               style="cursor: pointer; font-size: 18px;"></i>
          </td>
        `;
        tableBody.appendChild(row);
      });

      // Reinitialize DataTable after DOM update
      $('#shift-list-table').DataTable();

    })
    .catch(err => console.error("Fetch error:", err));
}

function saveShift(event) {
  event.preventDefault();
  const shift = collectShiftFormValues();
  console.log("Shift data to save:", shift);

  fetch(shiftApiUrl, {
    method: "POST",
    headers: getAuthHeaders(),      //added jwt token
    body: JSON.stringify(shift)
  })
    .then(response => {
      if (!response.ok) throw new Error("Failed to save shift");
      return response.json();
    })
    .then(() => {
      document.getElementById("shiftForm").reset();
      fetchShifts();
      showAlert("✅ Shift added successfully!", "success");
    })
    .catch(err => {
      console.error("Save error:", err);
      showAlert("Error saving shift", "danger");
    });
}

function confirmDelete(id) {
  deleteShiftId = id;
  const modal = new bootstrap.Modal(document.getElementById("confirmDeleteModal"));
  modal.show();
}

function deleteShiftConfirmed() {
  if (!deleteShiftId) return;

  // fetch(`${shiftApiUrl}/${deleteShiftId}`, { 
  //   method: "DELETE", 
  //   headers: getAuthHeaders(false) })    
  //   .then(() => {
  //     fetchShifts();
  //     showAlert("✅ Shift deleted successfully!", "success");
  //   })
  //   .catch(err => {
  //     console.error("Delete error:", err);
  //     showAlert("Error deleting shift", "danger");
  //   });

  fetch(`${shiftApiUrl}/${deleteShiftId}`, { 
    method: "DELETE", 
    headers: getAuthHeaders(false) 
  })
  .then(response => {
    if (response.ok) {
        return response.text().then(msg => {
            fetchShifts();
            showAlert(" ✅ " + msg, "danger");
        });       
    }
      else if (response.status === 409) {
            return response.text().then(msg => {
                showAlert(" ⚠️ " + msg, "warning");
            });
      } else {
        return response.text().then(msg => {
          showAlert("❌ " + msg, "danger");
        });
      }
  })
  .catch(err => {
        console.error("Delete error: " + err);
        showAlert("Unexpected error while deleting shift", "danger");
  });

  const modal = bootstrap.Modal.getInstance(document.getElementById("confirmDeleteModal"));
  modal.hide();
}

function editShift(id) {
  fetch(`${shiftApiUrl}/${id}`, {
       headers: getAuthHeaders(false)
  }
  )
    .then(res => res.json())
    .then(shift => {
      const modal = document.getElementById("editModal");
      document.getElementById("shift_id_hidden").value = shift.shift_id;

      modal.querySelector("#shiftname").value = shift.shift_name;
      modal.querySelector("#shiftcode").value = shift.shift_code;
      modal.querySelector("#login").value = shift.login_time;
      modal.querySelector("#loginoff").value = shift.logout_time;
      modal.querySelector("#shifthours").value = shift.shift_hours;

      setRadioValue("permanentOn", shift.saturday_off, modal);
      setRadioValue("sundayStatus", shift.sunday_off, modal);
      setRadioValue("holidayStatus", shift.holiday_Off, modal);

      setSessionValue(1, shift.session1_in, shift.session1_out, modal);
      setSessionValue(2, shift.session2_in, shift.session2_out, modal);
      setSessionValue(3, shift.session3_in, shift.session3_out, modal);
      setSessionValue(4, shift.session4_in, shift.session4_out, modal);

      const bsModal = new bootstrap.Modal(modal);
      bsModal.show();
    })
    .catch(err => {
      console.error("Edit load error:", err);
      showAlert("Error loading shift for edit", "danger");
    });
}

function updateShift(event) {
  event.preventDefault();
  const modal = document.getElementById("editModal");

  const id = document.getElementById("shift_id_hidden").value;
  if (!id) return;

  const updatedShift = {
    shift_name: modal.querySelector("#shiftname").value,
    shift_code: modal.querySelector("#shiftcode").value,
    login_time: modal.querySelector("#login").value,
    logout_time: modal.querySelector("#loginoff").value,
    shift_hours: modal.querySelector("#shifthours").value,
    saturday_off: getRadioValue("permanentOn", modal),
    sunday_off: getRadioValue("sundayStatus", modal),
    holiday_Off: getRadioValue("holidayStatus", modal),
    session1_in: getSessionValue(1, true, modal),
    session1_out: getSessionValue(1, false, modal),
    session2_in: getSessionValue(2, true, modal),
    session2_out: getSessionValue(2, false, modal),
    session3_in: getSessionValue(3, true, modal),
    session3_out: getSessionValue(3, false, modal),
    session4_in: getSessionValue(4, true, modal),
    session4_out: getSessionValue(4, false, modal),
  };

  fetch(`${shiftApiUrl}/${id}`, {
    method: "PUT",
    headers: getAuthHeaders(),
    body: JSON.stringify(updatedShift)
  })
    .then(res => {
      if (!res.ok) throw new Error("Update failed");
      return res.json();
    })
    .then(() => {
      bootstrap.Modal.getInstance(modal).hide();
      fetchShifts();
      showAlert("✅ Shift Updated Successfully..", "success");
    })
    .catch(err => {
      console.error("Update error:", err);
      showAlert("Error updating shift", "danger");
    });
}

function getRadioValue(name, container = document) {
  const radios = container.querySelectorAll(`input[name='${name}']`);
  for (let radio of radios) {
    if (radio.checked) return radio.value;
  }
  return null;
}

function setRadioValue(name, value, container = document) {
  const radios = container.querySelectorAll(`input[name='${name}']`);
  radios.forEach(radio => {
    if (radio.value === value) {
      radio.checked = true;
    }
  });
}

function getSessionValue(sessionNum, isIn, container = document) {
  const row = container.querySelectorAll("#user-list-table tbody tr")[sessionNum - 1];
  return row?.querySelectorAll("input")[isIn ? 0 : 1].value || "";
}

function setSessionValue(sessionNum, inVal, outVal, container = document) {
  const row = container.querySelectorAll("#user-list-table tbody tr")[sessionNum - 1];
  row.querySelectorAll("input")[0].value = inVal;
  row.querySelectorAll("input")[1].value = outVal;
}

// function showAlert(message, type) {
//   document.querySelectorAll(".custom-alert").forEach(el => el.remove());

//   const alertBox = document.createElement("div");
//   alertBox.className = `alert alert-${type} alert-dismissible fade show custom-alert animate__animated animate__fadeInDown`;
//   alertBox.role = "alert";

//   alertBox.style.position = "fixed";
//   alertBox.style.top = "10px";
//   alertBox.style.left = "50%";
//   alertBox.style.transform = "translateX(-50%)";
//   alertBox.style.zIndex = "1055";
//   alertBox.style.width = "max-content";
//   alertBox.style.maxWidth = "90%";

//   alertBox.innerHTML = `
//     ${message}
//     <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
//   `;

//   document.body.appendChild(alertBox);

//   setTimeout(() => {
//     alertBox.classList.remove("animate__fadeInDown");
//     alertBox.classList.add("animate__fadeOutUp");
//     setTimeout(() => alertBox.remove(), 1000);
//   }, 3000);
// }

// ------------------------------
// Shift Page Scroll Bar JS Start
// ------------------------------

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

function scrollTable(direction) {
  const container = document.getElementById("scrollTableContainer").querySelector(".custom-scroll");
  const scrollAmount = 200;

  if (direction === "left") {
    container.scrollLeft -= scrollAmount;
  } else {
    container.scrollLeft += scrollAmount;
  }
}

// ----------------------------
// Shift Page Scroll Bar JS End
// ----------------------------
  