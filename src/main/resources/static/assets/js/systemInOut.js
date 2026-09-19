console.log("system");

// Helper function for generating headers
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

const API_BASE = "http://localhost:8080/api";
const toggleBtn = document.getElementById("toggleBtn");

 const alertBox =  document.getElementById("customAlert");

 //functiuon to show alert message
 function showSystemAlert(message, type) {
     alertBox.textContent = message;
     alertBox.className = "custom-alert show " + type;

     setTimeout(() => {
        alertBox.classList.remove("show");
     }, 3000);
 }

let isSystemIn = false;
const employeeId = localStorage.getItem("empId"); 

// updating button text and bg
function updateButtonUI() {
  if (isSystemIn) {
    toggleBtn.textContent = "System Out";
    toggleBtn.classList.add("out");   // red
  } else {
    toggleBtn.textContent = "System In";
    toggleBtn.classList.remove("out"); // green
  }
}

// --- Load status on page load ---
async function loadCurrentStatus() {
  try {
    const response = await fetch(`${API_BASE}/system/status/${employeeId}`, {
      headers: getAuthHeaders()
    });
    if (response.ok) {
      const status = await response.text();  
      isSystemIn = (status === "IN");
      updateButtonUI();
    } else {
      console.error("Failed to fetch system status");
    }
  } catch (err) {
    console.error("Error fetching system status:", err);
  }
}

// --- Toggle on click ---
toggleBtn.addEventListener("click", async () => {
  try {
    if (!isSystemIn) {
      // Mark System In
      const response = await fetch(`${API_BASE}/system/in/${employeeId}`, {
        method: "POST",
        headers: getAuthHeaders()
      });

      if (response.ok) {
        isSystemIn = true;
        updateButtonUI();
        // alert("System In marked successfully!");
        // showSystemAlert("System In marked successfully!", "success");
        swal.fire({
          title: "System In marked successfully",
          icon: "success",
          draggable: true
        });
      } else {
        const err = await response.text();
        // alert("Error (System In): " + err);
        // showSystemAlert("Error (System In): " + err, "error");
        swal.fire({
          title: "Error (System In): " + err,
          icon: "error",
          draggable: true
        });

      }

    } else {
      // Mark System Out
      const response = await fetch(`${API_BASE}/system/out/${employeeId}`, {
        method: "POST",
        headers: getAuthHeaders()
      });

      if (response.ok) {
        isSystemIn = false;
        updateButtonUI();
        // alert("System Out marked successfully!");
        // showSystemAlert("System Out marked successfully!", "error");
        swal.fire({
          title: "System Out marked successfully",
          icon: "success",
          draggable: true
        });
      } else {
        const err = await response.text();
        // alert("Error (System Out): " + err);
        // showSystemAlert("Error (System Out): " + err, "error");
        swal.fire({
          title: "Error (System Out): " + err,
          icon: "error",
          draggable: true
        });
      }
    }
  } catch (error) {
    console.error(error);
    // alert("Something went wrong.");
    showSystemAlert("Something went wrong.", "error");

  }
});


loadCurrentStatus();
