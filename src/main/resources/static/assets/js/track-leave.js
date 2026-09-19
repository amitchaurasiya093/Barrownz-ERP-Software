// -------------------------------
// Helper function for generating header
// -------------------------------
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
  
  // -------------------------------
  // Extract employeeId from JWT
  // -------------------------------
  function getEmployeeIdFromToken() {
    const token = localStorage.getItem("token");
    if (!token) return null;
  
    try {
      const payload = JSON.parse(atob(token.split(".")[1]));
      return payload.empId || payload.employeeId || null;
    } catch (e) {
      console.error("Error parsing JWT:", e);
      return null;
    }
  }
  
  // -------------------------------
  // Fetch and render leaves
  // -------------------------------
  async function loadLeaves() {
    const employeeId = getEmployeeIdFromToken();
    if (!employeeId) {
      console.error("Employee ID not found in token");
      return;
    }
  
    try {
      const res = await fetch(`/api/leaves/employee/${employeeId}`, {
        headers: getAuthHeaders()
      });
  
      if (!res.ok) {
        throw new Error("Failed to fetch leaves");
      }
  
      const leaves = await res.json();
  
      // containers
      const activeContent = document.getElementById("activeContent");
      const completedContent = document.getElementById("completedContent");
  
      activeContent.innerHTML = "";
      completedContent.innerHTML = "";
  
      leaves.forEach((leave) => {
        const wrapper = document.createElement("div");
        wrapper.className = "p-3 mb-3 border rounded shadow-sm";
  
        // Employee Info
        const empInfo = document.createElement("div");
        empInfo.className = "mb-2 d-flex align-items-center text-primary fw-semibold fs-5";
        empInfo.innerHTML = `<i class="fa-solid fa-user-tie me-2"></i> ${leave.employeeName || "N/A"} ${leave.employee?.last_name || ""}`;
  
        const empCode = document.createElement("div");
        empCode.className = "mb-2 d-flex align-items-center text-primary fw-semibold";
        empCode.innerHTML = `<i class="fa-solid fa-file-code me-2"></i> [${leave.employeeCode || "N/A"}]`;
  
        // Leave Info
        const leaveInfo = document.createElement("div");
        leaveInfo.className = "leave-desc text-dark";
        const days =
          (new Date(leave.toDate) - new Date(leave.fromDate)) / (1000 * 60 * 60 * 24) + 1;
        leaveInfo.innerHTML = `
          <p class="mb-1"><strong>${leave.leaveTypeName || "Leave"}:</strong> ${days} day(s)</p>
          <p class="mb-0 text-justify">
            <i class="fa-solid fa-envelope-open-text text-secondary me-2"></i>
            ${leave.reason || ""}
          </p>
        `;
  
        wrapper.appendChild(empInfo);
        wrapper.appendChild(empCode);
        wrapper.appendChild(leaveInfo);
  
        if (leave.status && leave.status.toLowerCase() === "pending") {
          activeContent.appendChild(wrapper);
        } else {
          completedContent.appendChild(wrapper);
        }
      });
    } catch (err) {
      console.error("Error loading leaves:", err);
    }
  }
  
  // -------------------------------
  // Tab switching
  // -------------------------------
  function setupTabs() {
    const activeTab = document.getElementById("activeTab");
    const completedTab = document.getElementById("completedTab");
    const activeContent = document.getElementById("activeContent");
    const completedContent = document.getElementById("completedContent");
  
    activeTab.addEventListener("click", () => {
      activeTab.classList.add("active");
      completedTab.classList.remove("active");
      activeContent.style.display = "block";
      completedContent.style.display = "none";
    });
  
    completedTab.addEventListener("click", () => {
      completedTab.classList.add("active");
      activeTab.classList.remove("active");
      completedContent.style.display = "block";
      activeContent.style.display = "none";
    });
  }
  
  // -------------------------------
  // Init
  // -------------------------------
  document.addEventListener("DOMContentLoaded", () => {
    setupTabs();
    loadLeaves();
  });
  