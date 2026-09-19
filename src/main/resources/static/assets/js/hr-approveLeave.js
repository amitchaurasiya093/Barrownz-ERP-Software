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
//

//get user role from localStorage
const userRole = localStorage.getItem("role");

//load all leaves
async function loadAllLeaves() {
  try {
    const response = await fetch("http://localhost:8080/api/hr/leaves/leave-all", {
      method: "GET",
      headers: getAuthHeaders()
    });

    if (!response.ok) {
      throw new Error("Failed to fetch leave applications"); 
    }

    const leaves = await response.json();

    // split by status
    const pendingLeaves = leaves.filter(l => !l.status || l.status === "PENDING");
    const approvedLeaves = leaves.filter(l => l.status === "APPROVED");
    const rejectedLeaves = leaves.filter(l => l.status === "REJECTED");

    //HR → only Pending actions | Admin → all
    renderLeaveTable("#leaveApplicationTable tbody", pendingLeaves, true, "PENDING");
    renderLeaveTable("#approvedLeaveTable tbody", approvedLeaves, userRole === "Admin", "APPROVED");
    renderLeaveTable("#rejectedLeaveTable tbody", rejectedLeaves, userRole === "Admin", "REJECTED");

  } catch (error) {
    console.error("Error loading leaves: ", error);
  }
}

//render rows into given table
function renderLeaveTable(selector, leaves, showActions, statusType) {
  const tbody = document.querySelector(selector);
  tbody.innerHTML = "";

  if (!leaves || leaves.length === 0) {
    tbody.innerHTML = `<tr><td colspan="12" class="text-center">No leave applications found</td></tr>`;
    return;
  }

  leaves.forEach((leave) => {
    let actionButtons = "-";

    if (showActions) {
      if (userRole === "HR" && statusType === "PENDING") {
        actionButtons = `
          <button class="btn btn-success btn-sm me-1 approve-btn" data-leave-id="${leave.leaveApplicationId}">Approve</button>
          <button class="btn btn-danger btn-sm reject-btn" data-leave-id="${leave.leaveApplicationId}">Reject</button>
        `;
      } else if (userRole === "Admin") {
        if (statusType === "PENDING") {
          actionButtons = `
            <button class="btn btn-success btn-sm me-1 approve-btn" data-leave-id="${leave.leaveApplicationId}">Approve</button>
            <button class="btn btn-danger btn-sm reject-btn" data-leave-id="${leave.leaveApplicationId}">Reject</button>
          `;
        } else if (statusType === "APPROVED") {
          actionButtons = `
            <button class="btn btn-warning btn-sm override-reject-btn" data-leave-id="${leave.leaveApplicationId}">Override → Reject</button>
          `;
        } else if (statusType === "REJECTED") {
          actionButtons = `
            <button class="btn btn-primary btn-sm override-approve-btn" data-leave-id="${leave.leaveApplicationId}">Override → Approve</button>
          `;
        }
      } else if (userRole === "Employee" && leave.applyToId === loggedInUserId) {
        actionButtons = `
          <button class="btn btn-success btn-sm me-1 rm-approve-btn" data-leave-id="${leave.leaveApplicationId}">Approve</button>
          <button class="btn btn-danger btn-sm rm-reject-btn" data-leave-id="${leave.leaveApplicationId}">Reject</button>
        `;
      }
    }

    // ✅ Build row HTML completely in one string
    let rowHtml = `
      <td>${leave.status || "Pending"}</td>
      <td>${leave.employeeName ? `${leave.employeeName} (${leave.employeeCode})` : "-"}</td>
      <td>${leave.leaveTypeName || "-"}</td>
      <td>${leave.applyToName || "-"}</td>
      <td>${leave.fromDate || "-"}</td>
      <td>${leave.toDate || "-"}</td>
      <td>${leave.sessionFrom || "-"}</td>
      <td>${leave.sessionTo || "-"}</td>
      <td>${leave.qty || "-"}</td>
      <td>${leave.dateCreated ? leave.dateCreated.split("T")[0] : "N/A"}</td>
      <td>${leave.attachmentPath ? `<a href="/${leave.attachmentPath}" target="_blank">View</a>` : "N/A"}</td>
      <td>
        <button class="btn btn-sm text-white bg-primary view-details-btn" 
          data-leaves="${encodeURIComponent(JSON.stringify(leave))}">
          <i class="fa-solid fa-eye"></i>
        </button>
      </td>
    `;

    // Add Action By column only for Approved/Rejected
    if (statusType !== "PENDING") {
      rowHtml += `<td>${leave.actionByName || "-"}</td>`;
    }

    // Always add Action column
    rowHtml += `<td>${actionButtons}</td>`;

    const row = document.createElement("tr");
    row.innerHTML = rowHtml;
    tbody.appendChild(row);
  });

  // add listeners
  if (showActions) {
    document.querySelectorAll(".approve-btn").forEach((btn) => {
      btn.addEventListener("click", () => handleLeaveAction(btn.dataset.leaveId, "APPROVED"));
    });

    document.querySelectorAll(".reject-btn").forEach((btn) => {
      btn.addEventListener("click", () => handleLeaveAction(btn.dataset.leaveId, "REJECTED"));
    });

    document.querySelectorAll(".override-reject-btn").forEach((btn) => {
      btn.addEventListener("click", () => handleAdminOverride(btn.dataset.leaveId, "REJECTED"));
    });

    document.querySelectorAll(".override-approve-btn").forEach((btn) => {
      btn.addEventListener("click", () => handleAdminOverride(btn.dataset.leaveId, "APPROVED"));
    });

    // RM actions
   document.querySelectorAll(".rm-approve-btn").forEach((btn) => {
  btn.addEventListener("click", () => handleRMAction(btn.dataset.leaveId, "APPROVED"));
  });

document.querySelectorAll(".rm-reject-btn").forEach((btn) => {
  btn.addEventListener("click", () => handleRMAction(btn.dataset.leaveId, "REJECTED"));
});

  }

  //fill modal data
  // document.querySelectorAll(".view-details-btn").forEach((btn) => {
  //   btn.addEventListener("click", () => {
  //     const leave = JSON.parse(btn.dataset.leaves);
  //     populateLeaveDetailsModal(leave);
  //     const modal = new bootstrap.Modal(document.getElementById("leaveDetailsModal"));
  //     modal.show();
  //   });
  // });

 // Create modal instance once
const leaveDetailsModalEl = document.getElementById("leaveDetailsModal");
const leaveDetailsModal = new bootstrap.Modal(leaveDetailsModalEl);

// Event delegation for dynamically created buttons
document.addEventListener("click", function (e) {
  if (e.target.closest(".view-details-btn")) {
    const btn = e.target.closest(".view-details-btn");
    // const leave = JSON.parse(btn.dataset.leaves);
    const leave = JSON.parse(decodeURIComponent(btn.dataset.leaves));
    populateLeaveDetailsModal(leave);
    leaveDetailsModal.show();
  }
});

// Clear modal content + force backdrop cleanup on close
leaveDetailsModalEl.addEventListener("hidden.bs.modal", () => {
  document.getElementById("leaveDetailsBody").innerHTML = "";
  document.body.classList.remove("modal-open");  
  document.querySelectorAll(".modal-backdrop").forEach(el => el.remove());
});



}

//handle approve/reject for Pending (HR or Admin)
async function handleLeaveAction(leaveId, status) {
  const confirmMsg = `Are you sure you want to ${status.toLowerCase()} this leave?`;
  if (!confirm(confirmMsg)) {
    return;
  }

  try {
    let endpoint = "";

    if (userRole === "HR") {
      // HR API works only for Pending
      endpoint =
        status === "APPROVED"
          ? `http://localhost:8080/api/hr/leaves/${leaveId}/approve`
          : `http://localhost:8080/api/hr/leaves/${leaveId}/reject`;
    } else if (userRole === "Admin") {
      // Admin for Pending
      endpoint = `http://localhost:8080/api/admin/leaves/${leaveId}/status/${status}`;
    }

    const response = await fetch(endpoint, {
      method: "PUT",
      headers: getAuthHeaders()
    });

    if (!response.ok) {
      throw new Error("Failed to update leave status"); 
    }

    alert(`Leave ${status.toLowerCase()} successfully!`);
    loadAllLeaves(); // refresh tables
  } catch (error) {
    console.error("Error updating leave: ", error);
    alert("Error updating leave status");
  }
}

//handle Admin overrides
async function handleAdminOverride(leaveId, status) {
  const confirmMsg = `Are you sure you want to override this leave to ${status.toLowerCase()}?`;
  if (!confirm(confirmMsg)) {
    return;
  }

  try {
    const endpoint = `http://localhost:8080/api/admin/leaves/${leaveId}/status/${status}`;

    const response = await fetch(endpoint, {
      method: "PUT",
      headers: getAuthHeaders()
    });

    if (!response.ok) {
      throw new Error("Failed to override leave status");
    }

    alert(`Leave overridden to ${status.toLowerCase()} successfully!`);
    loadAllLeaves(); // refresh tables
  } catch (error) {
    console.error("Error overriding leave: ", error);
    alert("Error overriding leave status");
  }
}


//handle RM approve/reject
async function handleRMAction(leaveId, status) {
      const confirmMsg = `Are you sure you want to ${status.toLowerCase()} this leave as RM?`;
      if(!confirm(confirmMsg)) {
          return;
      }

      try {
         const endpoint = `http://localhost:8080/api/rm/leaves/${leaveId}/status/${status}`;

         const response = await fetch(endpoint, {
            method: "PUT",
            headers: getAuthHeaders()
         });

         if (!response.ok) {
          throw new Error("Failed to update leave status by RM");
        }
    
        alert(`Leave ${status.toLowerCase()} successfully by RM!`);
        loadAllLeaves(); // refresh tables
      } catch (error) {
        console.error("Error updating leave by RM: ", error);
        alert("Error updating leave status by RM");
      }
}

//function to populate modal
function populateLeaveDetailsModal(leave) {
  const tbody = document.getElementById("leaveDetailsBody");
  tbody.innerHTML = `
    <tr><th>Leave Type</th><td>${leave.leaveTypeName || "-"}</td></tr>
    <tr><th>From Date</th><td>${leave.fromDate || "-"}</td></tr>
    <tr><th>To Date</th><td>${leave.toDate || "-"}</td></tr>
    <tr><th>From Session</th><td>${leave.sessionFrom || "-"}</td></tr>
    <tr><th>To Session</th><td>${leave.sessionTo || "-"}</td></tr>  
    <tr><th>Reason</th>
     <td>
    <div style="max-height: 150px; overflow-y: auto; white-space: pre-wrap; word-break: break-word;">
      ${leave.reason || "-"}
    </div>
  </td>
    </tr>
    <tr><th>Qty</th><td>${leave.qty || "-"}</td></tr>
    <tr><th>Balance</th><td>${leave.balance || "N/A"}</td></tr>
    <tr><th>Attachment</th><td>${leave.attachmentPath ? `<a href="/${leave.attachmentPath}" target="_blank">View</a>` : "N/A"}</td></tr>
    <tr><th>Status</th><td><span class="badge bg-${leave.status === "APPROVED" ? "success" : leave.status === "REJECTED" ? "danger" : "warning"}">${leave.status || "Pending"}</span></td></tr>
  `;
}


//auto load
document.addEventListener("DOMContentLoaded", loadAllLeaves);
