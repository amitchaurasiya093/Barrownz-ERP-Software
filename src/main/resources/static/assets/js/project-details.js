
//helper function for jwt headers
function getAuthHeaders(isJson = true) {
  const token = localStorage.getItem("token");
  const headers = { "Authorization": `Bearer ${token}` };
  if (isJson) headers["Content-Type"] = "application/json";
  return headers;
}
/////

document.addEventListener("DOMContentLoaded", () => {
  const form = document.getElementById("projectDetailsForm");
  const editForm = document.getElementById("editProjectForm");
  const teamDropdownList = document.getElementById("teamDropdownList");
  const rateInput = document.getElementById("project_rate");
  const manPerHourInput = document.getElementById("rate-per-hour");
  const rateCalcInput = document.getElementById("calcultion");
  const profitBox = document.getElementById("profitBox");

  const editModalEl = document.getElementById("editProjectModal");
  const editModal = new bootstrap.Modal(editModalEl);

// -------------------------------
// POPULATE DROPDOWNS ON PAGE LOAD
// -------------------------------
  populateDropdown("http://localhost:8080/api/projects", "project_name", "projectName", "projectId");         // Project dropdown
  populateDropdown("http://localhost:8080/api/clients", "client_name", "clientName", "clientId");             // Client dropdown
  populateDropdown("http://localhost:8080/api/types", "project_type", "typeName", "typeId");                  // Type dropdown
  populateDropdown("http://localhost:8080/api/priority", "project_priority", "priorityName", "priorityId");   // Priority dropdown
  populateDropdown("http://localhost:8080/api/status", "project_status", "statusName", "statusId");           // Status dropdown
  populateProjectLeaderDropdown();                                                                             // Project Leader dropdown
  populateRateTypeDropdown();                                                                                  // Rate Type (Currency)
  loadTeamCheckboxes();                                                                                        // Team member checkboxes
  loadProjectTable();                                                                                          // Project list table

  // ---------------------------------------
  // POPULATE GENERIC DROPDOWN WITH API DATA
  // ---------------------------------------
  async function populateDropdown(endpoint, id, label, value) {
    try {
      const res = await fetch(endpoint, {
        headers: getAuthHeaders(false)   //added jwt token
      });
      const data = await res.json();
      const select = document.getElementById(id);
      select.innerHTML = `<option value="" disabled selected>-Select-</option>`;
      data.forEach(item => {
        select.innerHTML += `<option value="${item[value]}">${item[label]}</option>`;
      });
    } catch (err) {
      console.error(`Error loading ${id}:`, err);
    }
  }

  // -------------------------------------------------
  // POPULATE PROJECTS LEADER DROPDOWN FOR CREATE FORM
  // -------------------------------------------------
  async function populateProjectLeaderDropdown() {
    try {
      const res = await fetch("http://localhost:8080/api/employees", {
          headers: getAuthHeaders(false)   //added jwt token
      });
      const data = await res.json();
      const select = document.getElementById("project_leader");
      select.innerHTML = `<option value="" disabled selected>-Select-</option>`;
      data.forEach(emp => {
        const fullName = `${emp.first_name} ${emp.last_name || ""}`.trim();
        if (emp.empId) {
          select.innerHTML += `<option value="${emp.empId}">${fullName}</option>`;
        }
      });
    } catch (err) {
      console.error("Error loading project leaders:", err);
    }
  }

  // -----------------------------------------
  // LOAD TEAM MEMBER CHECKBOX FOR CARETE FORM
  // -----------------------------------------
async function loadTeamCheckboxes() {
  try {
    const res = await fetch("http://localhost:8080/api/employees", {
         headers: getAuthHeaders(false)   //added jwt token
    });
    const employees = await res.json();
    teamDropdownList.innerHTML = "";

    employees.forEach(emp => {
      const fullName = `${emp.first_name} ${emp.last_name || ""}`.trim();
      if (!emp.empId) return;

      const li = document.createElement("li");
      li.innerHTML = `
        <div class="form-check ms-2">
          <input class="form-check-input team-checkbox" type="checkbox" value="${emp.empId}" id="emp_${emp.empId}">
          <label class="form-check-label" for="emp_${emp.empId}">${fullName}</label>
        </div>
      `;
      teamDropdownList.appendChild(li);
    });
  } catch (err) {
    console.error("Error loading team members:", err);
  }
}

// ---------------------
// SELECTED TEAM MEMBERS
// ---------------------
function getSelectedTeamMembers() {
  return Array.from(document.querySelectorAll(".team-checkbox:checked"))
    .map(cb => parseInt(cb.value))
    .filter(id => !isNaN(id));
}

// --------------------------------------------------------
// TEAM SELECTION + COST CALCULATION BASED ON INDIAN AMOUNT
// --------------------------------------------------------
let selectedTeamMemberIds = [];

// ----------------------------------------------------------------
// ONLY STORE SELECTED TEAM ON CHEAKBOX CLICK (NO CALCULATION HERE)
// ----------------------------------------------------------------
teamDropdownList.addEventListener("click", (e) => {
  if (!e.target.classList.contains("team-checkbox")) return;
  selectedTeamMemberIds = getSelectedTeamMembers();
});

// ------------------------------------------------------
// PERFORM CALCULATION WHEN HOUR/DAY DROPDOWN IS SELECTED
// ------------------------------------------------------
document.getElementById("hour_day").addEventListener("change", async () => {
  if (selectedTeamMemberIds.length === 0) {
    showAlert("⚠️ Please select team members first.", "warning");
    return;
  }

  const calcType = document.getElementById("hour_day").value;
  if (!calcType) return;

  const indianAmountValue = document.getElementById("indian").value.trim();
  const rate = parseFloat(indianAmountValue.replace(/[₹,\s]/g, '')) || 0;

  if (isNaN(rate) || rate <= 0) {
    showAlert("⚠️ Invalid or missing rate (₹ amount)", "warning");
    return;
  }

// --------------------------------
// VALIDATE REQUIRED PROJECT FIELDS
// --------------------------------
  const requiredIds = ["project_name", "client_name", "project_type", "project_priority", "project_status", "project_leader", "start_date", "end_date"];
  for (let id of requiredIds) {
    const val = document.getElementById(id).value;
    if (!val || val.trim() === "") return;
  }

  const payload = {
    projectId: parseInt(document.getElementById("project_name").value),
    clientId: parseInt(document.getElementById("client_name").value),
    typeId: parseInt(document.getElementById("project_type").value),
    priorityId: parseInt(document.getElementById("project_priority").value),
    statusId: parseInt(document.getElementById("project_status").value),
    startDate: document.getElementById("start_date").value,
    endDate: document.getElementById("end_date").value,
    submissionDate: document.getElementById("submit_date").value || null,
    projectLeaderId: parseInt(document.getElementById("project_leader").value),
    rate: rate,
    rateType: document.getElementById("rate_type").value,
    teamMemberIds: selectedTeamMemberIds,
    remarks: "",
    description: ""
  };

  try {
    const res = await fetch("http://localhost:8080/api/projectDetails/calculate-cost", {
      method: "POST",
      headers: getAuthHeaders(),  //added jwt token
      body: JSON.stringify(payload)
    });

    const result = await res.json();

    if (!res.ok) {
      showAlert(result.message || "❌ Project cost exceeds rate", "danger");
      return;
    }

  // ------------------------------------
  // SHOW RESULT BASED ON CALCULATON TYPE
  // ------------------------------------
    if (calcType === "Man Per Hour") {
      manPerHourInput.value = result.manPerHour?.toFixed(2);
    } else if (calcType === "Man Per Day") {
      manPerHourInput.value = (result.manPerHour * 9)?.toFixed(2);
    }

    rateCalcInput.value = result.rateCalculation?.toFixed(2);

    const profit = rate - result.rateCalculation;
    const profitPercentage = (profit / rate) * 100;
    profitBox.value = `${profit.toFixed(2)} (${profitPercentage.toFixed(0)}%)`;

  } catch (err) {
    console.error("❌ Error calculating cost:", err);
  }
});


// ----------------------------------------
// HANDLE FORM SUBMISSION TO CREATE PROJECT
// ----------------------------------------
form.addEventListener("submit", async (e) => {
  e.preventDefault();

  const selectedIds = getSelectedTeamMembers();

// -----------------------------------
// GET INR AMOUNT INSTEAD OF RAW INPUT
// -----------------------------------
  const indianAmountValue = document.getElementById("indian").value.trim();
  const numericRate = parseFloat(indianAmountValue.replace(/[₹,\s]/g, '')) || 0;

  const payload = {
    projectId: parseInt(document.getElementById("project_name").value),
    clientId: parseInt(document.getElementById("client_name").value),
    typeId: parseInt(document.getElementById("project_type").value),
    priorityId: parseInt(document.getElementById("project_priority").value),
    statusId: parseInt(document.getElementById("project_status").value),
    startDate: document.getElementById("start_date").value,
    endDate: document.getElementById("end_date").value,
    submissionDate: document.getElementById("submit_date").value || null,
    projectLeaderId: parseInt(document.getElementById("project_leader").value),
    rate: numericRate, 
    rateType: document.getElementById("rate_type").value,
    teamMemberIds: selectedIds,
    remarks: document.getElementById("remark").value,
    description: document.getElementById("description").value
  };

  try {
    const res = await fetch("http://localhost:8080/api/projectDetails", {
      method: "POST",
      headers: getAuthHeaders(),  //added jwt token
      body: JSON.stringify(payload)
    });

    if (res.ok) {
      showAlert("✅ Project Added Successfully.", "success");
      form.reset();
      loadProjectTable();
    } else {
      const msg = await res.text();
      alert("❌ " + msg);
    }
  } catch (err) {
    console.error("❌ Error saving project:", err);
  }
});


// ------------------------------------
// LOAD ALL PROJECT RECORDS OINTO TABLE
// ------------------------------------
async function loadProjectTable() {
    try {
      const res = await fetch("http://localhost:8080/api/projectDetails", {
          headers: getAuthHeaders(false)  //added jwt token
      });
      const data = await res.json();
      const tbody = document.getElementById("projectListBody");

        // Destroy existing DataTable instance before replacing content
        if ($.fn.DataTable.isDataTable('#project-details-table')) {
          $('#project-details-table').DataTable().destroy();
        }

      tbody.innerHTML = "";

data.forEach(project => {
  const tr = document.createElement("tr");

  const rate = project.rate || 0;
  const rateCalculation = project.rateCalculation || 0;
  const profit = rate - rateCalculation;
  const profitPercent = rate ? (profit / rate) * 100 : 0;

  tr.innerHTML = `
    <td class="text-center">${project.projectDetailsId}</td>
    <td class="text-center">${project.projectName}</td>
    <td class="text-center">${project.clientName}</td>
    <td class="text-center">${project.typeName}</td>
    <td class="text-center">${project.startDate}</td>
    <td class="text-center">${project.endDate}</td>
    <td class="text-center">${project.submissionDate}</td>
    <td class="text-center">${rate.toFixed(2)}</td>
    <td class="text-center">${project.rateType}</td>
    <td class="text-center">${project.priorityName}</td>
    <td class="text-center">${project.projectLeaderName}</td>
    <td class="text-center"><marquee>${project.teamMemberName?.join(", ") || '-'}</marquee></td>
    <td class="text-center">${project.manPerHour?.toFixed(2)}</td>
    <td class="text-center">${rateCalculation.toFixed(2)}</td>
    <td class="text-center">${profit.toFixed(2)} (${profitPercent.toFixed(0)}%)</td>
    <td class="text-center">${project.statusName}</td>
    <td class="text-center">${project.remarks}</td>
    <td class="text-center">${project.description}</td>
    <td class="text-center">
     
     
       <i class="fa-solid fa-pen-to-square text-primary me-2"
               onclick="openEditModal(${project.projectDetailsId})"
               style="cursor: pointer; font-size: 18px;"></i>

               <i class="fa-solid fa-trash text-danger"
               onclick="confirmDelete(${project.projectDetailsId})"
               style="cursor: pointer; font-size: 18px;"></i>

      </td>
  `;
  tbody.appendChild(tr);
});

  // Reinitialize DataTable after DOM update
  $('#project-details-table').DataTable();


    } catch (err) {
      console.error("❌ Failed to load project records:", err);
    }
  }



// handle update project form 

document.getElementById("editProjectForm").addEventListener("submit", async (e) => {
  e.preventDefault();

  const id = document.getElementById("edit_project_id").value;

  const teamIds = Array.from(document.querySelectorAll(".edit-team-checkbox:checked"))
    .map(cb => parseInt(cb.value))
    .filter(id => !isNaN(id));

  const payload = {
    projectId: parseInt(document.getElementById("edit_project_name").value),
    clientId: parseInt(document.getElementById("edit_client_name").value),
    typeId: parseInt(document.getElementById("edit_project_type").value),
    priorityId: parseInt(document.getElementById("edit_project_priority").value),
    statusId: parseInt(document.getElementById("edit_project_status").value),
    startDate: document.getElementById("edit_start_date").value,
    endDate: document.getElementById("edit_end_date").value,
    submissionDate: document.getElementById("edit_submit_date").value || null,
    projectLeaderId: parseInt(document.getElementById("edit_project_leader").value),
    rate: parseFloat(document.getElementById("edit_project_rate").value),
    rateType: document.getElementById("edit_rate_type").value,
    teamMemberIds: teamIds,
    remarks: document.getElementById("edit_remark").value,
    description: document.getElementById("edit_description").value
  };

  try {
    const res = await fetch(`http://localhost:8080/api/projectDetails/${id}`, {
      method: "PUT",
      headers: getAuthHeaders(),
      body: JSON.stringify(payload)
    });

    if (!res.ok) {
      const msg = await res.text();
      alert("❌ Update failed: " + msg);
      return;
    }

    // Success
    document.getElementById("editSuccessAlert").classList.remove("d-none");
    setTimeout(() => {
      bootstrap.Modal.getInstance(document.getElementById("editProjectModal")).hide();
      document.getElementById("editSuccessAlert").classList.add("d-none");
    }, 1500);

    loadProjectTable(); // reload table

  } catch (err) {
    console.error("❌ Error updating project:", err);
    alert("❌ Update failed.");
  }
});


  // -------------------------------------------------------
  // POPULATE CURRENCY (RATE TYPE) DROPDOWN FOR EXTERNAL API
  // -------------------------------------------------------

 async function populateRateTypeDropdown() {
    try {
      const res = await fetch('https://restcountries.com/v3.1/all?fields=currencies');
      const countries = await res.json();

      const currencyMap = new Map();
      countries.forEach(country => {
        if (country.currencies) {
          Object.entries(country.currencies).forEach(([code, currency]) => {
            if (!currencyMap.has(code)) {
              currencyMap.set(code, {
                name: currency.name || code,
                symbol: currency.symbol || ""
              });
            }
          });
        }
      });

      const rateTypeSelect = document.getElementById("rate_type");
      rateTypeSelect.innerHTML = `<option value="" disabled selected>-Select Amount Type-</option>`;
      currencyMap.forEach((info, code) => {
        const option = document.createElement("option");
        option.value = code;
        option.text = `${info.name} (${code}) ${info.symbol}`;
        rateTypeSelect.appendChild(option);
      });

      console.log("Currency dropdown populated.");

    } catch (err) {
      console.error("Currency load error:", err);
    }
  }
async function convertToINR() {
  const currencyCode = document.getElementById("rate_type").value;
  const amount = parseFloat(document.getElementById("project_rate").value);
  const indianBox = document.getElementById("indian");

  if (!currencyCode || isNaN(amount)) {
    indianBox.value = "";
    return;
  }

  try {
    const res = await fetch(`https://open.er-api.com/v6/latest/${currencyCode}`);
    const data = await res.json();

    if (data && data.result === "success" && data.rates && data.rates.INR) {
      const rate = data.rates.INR;
      const converted = amount * rate;
      indianBox.value = `₹ ${converted.toFixed(2)}`;
    } else {
      indianBox.value = "Conversion not available";
    }
  } catch (err) {
    console.error("Conversion API error:", err);
    indianBox.value = "Conversion failed";
  }
}

// -----------------------
// INITIALIZE ON PAGE LOAD
// -----------------------
  window.addEventListener("DOMContentLoaded", () => {
    console.log("Page loaded");
    populateRateTypeDropdown();

    document.getElementById("rate_type").addEventListener("change", convertToINR);
    document.getElementById("project_rate").addEventListener("input", convertToINR);
  });

});


 // ==========================
// Load Edit Modal and Prefill
// ==========================
async function openEditModal(projectId) {
  try {
    const res = await fetch(`http://localhost:8080/api/projectDetails/${projectId}`, {
      headers: getAuthHeaders(false)
    });
    if (!res.ok) throw new Error("Failed to fetch project.");
    const project = await res.json();

    // Populate all dropdowns with selected values
    await populateEditDropdown("http://localhost:8080/api/projects", "edit_project_name", "projectName", "projectId", project.projectName.projectId);
    await populateEditDropdown("http://localhost:8080/api/clients", "edit_client_name", "clientName", "clientId", project.client.clientId);
    await populateEditDropdown("http://localhost:8080/api/types", "edit_project_type", "typeName", "typeId", project.projectType.typeId);
    await populateEditDropdown("http://localhost:8080/api/priority", "edit_project_priority", "priorityName", "priorityId", project.projectPriority.priorityId);
    await populateEditDropdown("http://localhost:8080/api/status", "edit_project_status", "statusName", "statusId", project.project_status.statusId);
    await populateEditProjectLeaderDropdown(project.projectLeader.empId);
    await populateEditRateTypeDropdown(project.rateType);

    // Fill rest of the fields
    document.getElementById("edit_project_id").value = project.projectDetailsid;
    document.getElementById("edit_start_date").value = project.startDate;
    document.getElementById("edit_end_date").value = project.endDate;
    document.getElementById("edit_submit_date").value = project.submissionDate || "";

    document.getElementById("edit_project_rate").value = project.rate;
    document.getElementById("edit_rate_per_hour").value = project.manPerHour?.toFixed(2) || "";
    document.getElementById("edit_rate_calcultion").value = project.rateCalculation?.toFixed(2) || "";

    const profit = project.rate - project.rateCalculation;
    document.getElementById("edit_profitBox").value = profit.toFixed(2);

    document.getElementById("edit_remark").value = project.remarks || "";
    document.getElementById("edit_description").value = project.description || "";

    // Team checkboxes
    await populateEditTeamCheckboxes(project.teamMembers.map(emp => emp.empId));

    // Show modal
    const modal = new bootstrap.Modal(document.getElementById("editProjectModal"));
    modal.show();
  } catch (err) {
    console.error("Error loading project data for editing:", err);
    alert("❌ Failed to load project data.");
  }
}

// ==========================
// Populate Dropdown (Generic)
// ==========================
async function populateEditDropdown(endpoint, elementId, labelField, valueField, selectedValue) {
  try {
    const res = await fetch(endpoint, { headers: getAuthHeaders(false) });
    const data = await res.json();
    const select = document.getElementById(elementId);
    select.innerHTML = `<option value="" disabled>-Select-</option>`;
    data.forEach(item => {
      const value = item[valueField];
      const label = item[labelField];
      const selected = value === selectedValue ? "selected" : "";
      select.innerHTML += `<option value="${value}" ${selected}>${label}</option>`;
    });
  } catch (err) {
    console.error(`Error populating ${elementId}:`, err);
  }
}

// ==========================
// Populate Project Leader
// ==========================
async function populateEditProjectLeaderDropdown(selectedId) {
  try {
    const res = await fetch("http://localhost:8080/api/employees", {
      headers: getAuthHeaders(false)
    });
    const data = await res.json();
    const select = document.getElementById("edit_project_leader");
    select.innerHTML = `<option value="" disabled>-Select-</option>`;
    data.forEach(emp => {
      const fullName = `${emp.first_name} ${emp.last_name || ""}`.trim();
      const selected = emp.emp_id === selectedId ? "selected" : "";
      select.innerHTML += `<option value="${emp.emp_id}" ${selected}>${fullName}</option>`;
    });
  } catch (err) {
    console.error("Error loading project leaders:", err);
  }
}

// ==========================
// Populate Currency Dropdown
// ==========================
async function populateEditRateTypeDropdown(selectedCode) {
  try {
    const res = await fetch('https://restcountries.com/v3.1/all?fields=currencies');
    const countries = await res.json();

    const currencyMap = new Map();
    countries.forEach(country => {
      if (country.currencies) {
        Object.entries(country.currencies).forEach(([code, currency]) => {
          if (!currencyMap.has(code)) {
            currencyMap.set(code, {
              name: currency.name || code,
              symbol: currency.symbol || ""
            });
          }
        });
      }
    });

    const select = document.getElementById("edit_rate_type");
    select.innerHTML = `<option value="" disabled>-Select-</option>`;
    currencyMap.forEach((info, code) => {
      const selected = code === selectedCode ? "selected" : "";
      select.innerHTML += `<option value="${code}" ${selected}>${info.name} (${code}) ${info.symbol}</option>`;
    });
  } catch (err) {
    console.error("Currency load error:", err);
  }
}

// ==========================
// Populate Team Checkboxes
// ==========================
async function populateEditTeamCheckboxes(selectedIds = []) {
  try {
    const res = await fetch("http://localhost:8080/api/employees", {
      headers: getAuthHeaders(false)
    });
    const employees = await res.json();
    const container = document.getElementById("edit_team_dropdown_list");
    container.innerHTML = "";

    employees.forEach(emp => {
      const fullName = `${emp.first_name} ${emp.last_name || ""}`.trim();
      const checked = selectedIds.includes(emp.emp_id) ? "checked" : "";
      const div = document.createElement("div");
      div.className = "form-check";
      div.innerHTML = `
        <input class="form-check-input edit-team-checkbox" type="checkbox" value="${emp.emp_id}" id="edit_emp_${emp.emp_id}" ${checked}>
        <label class="form-check-label" for="edit_emp_${emp.emp_id}">${fullName}</label>
      `;
      container.appendChild(div);
    });
  } catch (err) {
    console.error("Error loading team members:", err);
  }
}

// ==========================
// Submit Updated Project
// ==========================
document.getElementById("editProjectForm").addEventListener("submit", async (e) => {
  e.preventDefault();
  const id = document.getElementById("edit_project_id").value;

  const teamIds = Array.from(document.querySelectorAll(".edit-team-checkbox:checked"))
    .map(cb => parseInt(cb.value))
    .filter(id => !isNaN(id));

  const payload = {
    projectId: parseInt(document.getElementById("edit_project_name").value),
    clientId: parseInt(document.getElementById("edit_client_name").value),
    typeId: parseInt(document.getElementById("edit_project_type").value),
    priorityId: parseInt(document.getElementById("edit_project_priority").value),
    statusId: parseInt(document.getElementById("edit_project_status").value),
    startDate: document.getElementById("edit_start_date").value,
    endDate: document.getElementById("edit_end_date").value,
    submissionDate: document.getElementById("edit_submit_date").value || null,
    projectLeaderId: parseInt(document.getElementById("edit_project_leader").value),
    rate: parseFloat(document.getElementById("edit_project_rate").value),
    rateType: document.getElementById("edit_rate_type").value,
    teamMemberIds: teamIds,
    remarks: document.getElementById("edit_remark").value,
    description: document.getElementById("edit_description").value
  };

  try {
    const res = await fetch(`http://localhost:8080/api/projectDetails/${id}`, {
      method: "PUT",
      headers: getAuthHeaders(),
      body: JSON.stringify(payload)
    });

    if (!res.ok) {
      const msg = await res.text();
      alert("❌ Update failed: " + msg);
      return;
    }

    // Success
    document.getElementById("editSuccessAlert").classList.remove("d-none");
    setTimeout(() => {
      bootstrap.Modal.getInstance(document.getElementById("editProjectModal")).hide();
      document.getElementById("editSuccessAlert").classList.add("d-none");
    }, 1500);

    loadProjectTable(); // refresh table
  } catch (err) {
    console.error("❌ Error updating project:", err);
    alert("❌ Update failed.");
  }
});


// --------------------------------
// SHOW ALERT MESSAGE JS START HERE
// --------------------------------
function showAlert(message, type = "success") {
  const alertPlaceholder = document.getElementById("alertPlaceholder");
  const wrapper = document.createElement("div");

  wrapper.innerHTML = `
    <div class="alert alert-${type} alert-dismissible fade show" role="alert">
      <strong>${type === "success" ? "Success!" : "Error!"}</strong> ${message}
      <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
    </div>
  `;

  alertPlaceholder.innerHTML = "";
  alertPlaceholder.append(wrapper);

  setTimeout(() => {
    const alert = bootstrap.Alert.getOrCreateInstance(wrapper.querySelector(".alert"));
    alert.close();
  }, 4000);
}

// -------------------------------
// SCROLL TABLE DATA JS START HERE
// -------------------------------
function scrollTable(direction) {
  const container = document.querySelector("#scrollTableContainer .custom-scroll");
  const scrollAmount = 200;
  if (container) {
    container.scrollLeft += direction === "left" ? -scrollAmount : scrollAmount;
  }
}
