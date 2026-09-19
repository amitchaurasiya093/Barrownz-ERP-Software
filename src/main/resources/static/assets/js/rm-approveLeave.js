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

  document.addEventListener("DOMContentLoaded", () => {
    loadLeaveRequests();
});

//tab switching
document.getElementById("leaveRequestTab").addEventListener("click", () => {
    showTab("leaveRequestContent");
    loadLeaveRequests();
});

document.getElementById("forwardedLeavesTab").addEventListener("click", () => {
    showTab("forwardedLeavesContent");
    loadForwardedLeaves();
});

document.getElementById("myLeavesTab").addEventListener("click", () => {
    showTab("myLeavesContent");
    loadMyLeaves();
});

function showTab(tabId) {
    // hide all
    document.querySelectorAll(".leave-box").forEach(el => el.style.display = "none");
    document.getElementById(tabId).style.display = "block";

    // deactivate all
    document.querySelectorAll(".tab-button, .tab-button-2").forEach(el => el.classList.remove("active"));

    // activate correct
    if (tabId === "leaveRequestContent") document.getElementById("leaveRequestTab").classList.add("active");
    if (tabId === "forwardedLeavesContent") document.getElementById("forwardedLeavesTab").classList.add("active");
    if (tabId === "myLeavesContent") document.getElementById("myLeavesTab").classList.add("active");
}

//calling API

// RM Leave Requests (Pending)
async function loadLeaveRequests() {
    try {
        const res = await fetch("http://localhost:8080/api/rm/leaves/leave-all", {
            headers: getAuthHeaders()
        });
        if (!res.ok) throw new Error("Failed to fetch requests");

        const leaves = await res.json();
        const pending = leaves.filter(l => l.status === "PENDING");
        renderTable("#leaveRequestContent tbody", pending, "leaveRequest");
    } catch (err) {
        console.error("Error loading pending leaves", err);
    }
}

// RM Forwarded Leaves (Approved/Rejected)
async function loadForwardedLeaves() {
    try {
        const res = await fetch("http://localhost:8080/api/rm/leaves/leave-all", {
            headers: getAuthHeaders()
        });
        if (!res.ok) throw new Error("Failed to fetch forwarded");

        const leaves = await res.json();
        const processed = leaves.filter(l => l.status === "APPROVED" || l.status === "REJECTED");
        renderTable("#forwardedLeavesContent tbody", processed, "forwardedLeaves");
    } catch (err) {
        console.error("Error loading forwarded leaves", err);
    }
}

// Employee My Leaves
async function loadMyLeaves() {
    try {
        const res = await fetch("http://localhost:8080/api/leaves/my-leaves", {
            headers: getAuthHeaders()
        });
        if (!res.ok) throw new Error("Failed to fetch my leaves");

        const leaves = await res.json();
        renderTable("#myLeavesContent tbody", leaves, "myLeaves");
    } catch (err) {
        console.error("Error loading my leaves", err);
    }
}

//render table
function renderTable(selector, leaves, type) {
    const tbody = document.querySelector(selector);
    tbody.innerHTML = "";

    if (!leaves || leaves.length === 0) {
        tbody.innerHTML = `<tr><td colspan="12" class="text-center">No leave applications found</td></tr>`;
        return;
    }

    leaves.forEach(leave => {
        const row = document.createElement("tr");

        if (type === "leaveRequest") {
            row.innerHTML = `
                <td>${leave.status}</td>
                <td>${leave.employeeName ? `${leave.employeeName} (${leave.employeeCode})` : "N/A"}</td>
                <td>${leave.leaveTypeName || leave.leaveType || "N/A"}</td>
                <td>${leave.applyToName || leave.applyTo || "N/A"}</td>
                <td>${leave.fromDate || leave.startDate}</td>
                <td>${leave.toDate || leave.endDate}</td>
                <td>${leave.sessionFrom || leave.fromSession || "-"}</td>
                <td>${leave.sessionTo || leave.toSession || "-"}</td>
                <td>${leave.leaveQuantity || "-"}</td>
                <td>${leave.dateCreated || leave.appliedDate || "-"}</td>
                <td>${leave.attachmentPath ? `<a href="${leave.attachmentPath}" target="_blank">View</a>` : "N/A"}</td>
                <td>
                    <button class="btn btn-success btn-sm" onclick="updateLeaveStatus(${leave.leaveApplicationId}, 'APPROVED')">Approve</button>
                    <button class="btn btn-danger btn-sm" onclick="updateLeaveStatus(${leave.leaveApplicationId}, 'REJECTED')">Reject</button>
                </td>
            `;
        } else if (type === "forwardedLeaves") {
            row.innerHTML = `
                <td>${leave.status}</td>
                <td>${leave.employeeName ? `${leave.employeeName} (${leave.employeeCode})` : "N/A"}</td>
                <td>${leave.applyToName || leave.applyTo || "N/A"}</td>
                <td>${leave.leaveTypeName || leave.leaveType || "N/A"}</td>
                <td>${leave.fromDate || leave.startDate}</td>
                <td>${leave.toDate || leave.endDate}</td>
                <td>${leave.sessionFrom || leave.fromSession || "-"}</td>
                <td>${leave.sessionTo || leave.toSession || "-"}</td>
                <td>${leave.qty || leave.leaveQuantity || "-"}</td>
                <td>${leave.attachmentPath ? `<a href="${leave.attachmentPath}" target="_blank">View</a>` : "N/A"}</td>
                <td><span class="text-muted">N/A</span></td>
            `;
        } else if (type === "myLeaves") {
            row.innerHTML = `
                <td>${leave.status}</td>
                <td>${leave.leaveTypeName || leave.leaveType || "N/A"}</td>
                <td>${leave.fromDate || leave.startDate}</td>
                <td>${leave.toDate || leave.endDate}</td>
                <td>${leave.sessionFrom || leave.fromSession || "-"}</td>
                <td>${leave.sessionTo || leave.toSession || "-"}</td>
                <td>${leave.applyToName || leave.applyTo || "N/A"}</td>
        
                <td>${leave.ccEmployeeNames ? leave.ccEmployeeNames.join(", ") : "-"}</td>
                <td>${leave.qty || leave.leaveQuantity || "-"}</td>
                <td>${leave.dateCreated || leave.appliedDate || "-"}</td>
                <td><span class="text-muted">N/A</span></td>
                <td>${leave.actionByName}</td>
            `;
        }

        tbody.appendChild(row);
    });
}



//updating leave status
async function updateLeaveStatus(leaveId, newStatus) {

      let message = newStatus === "APPROVED"
                    ? "Are you sure you want to approve this leave?"
                    : "Are you sure you want to reject this leave?";

                    //show confirmation
                    if(!confirm(message)) {
                        return;
                    }
    try {
        const res = await fetch(`http://localhost:8080/api/rm/leaves/${leaveId}/status/${newStatus}`, {
            method: "PUT",
            headers: getAuthHeaders()
        });
        if (!res.ok) throw new Error("Update failed");

        alert(`Leave ${newStatus.toLowerCase()} successfully!`);
        loadLeaveRequests();
        loadForwardedLeaves();
    } catch (err) {
        console.error("Error updating leave", err);
        alert("Something went wrong while updating leave status!");
    }
}


