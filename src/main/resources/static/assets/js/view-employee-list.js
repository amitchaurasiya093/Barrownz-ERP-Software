
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

document.addEventListener("DOMContentLoaded", function () {
  loadEmployees(); // Correct function call
});

async function loadEmployees() {
  try {
    const res = await fetch("http://localhost:8080/api/employees/profile", {
      headers: getAuthHeaders()
    });

    if (!res.ok) {
      throw new Error("Unauthorized or Error fetching data");
    }

    const emp = await res.json(); // single employee object
    const tbody = document.querySelector('#employeeTable tbody');
    if (!tbody) return;

    tbody.innerHTML = `
      <tr>
        <td>${emp.profile_picture ? `<img src="http://localhost:8080/uploads/${emp.profile_picture}" style="width:50px; height:50px; border-radius:50%; object-fit:cover;">` : '-'}</td>
        <td>${emp.emp_code || '-'}</td>
        <td>${emp.first_name || ''}</td>
        <td>${emp.last_name || ''}</td>
        <td>${emp.personal_email || '-'}</td>
        <td>${emp.joining_date || '-'}</td>
        <td>
  <i class="fa-solid fa-eye text-primary view-profile" style="cursor:pointer;" data-emp-id="${emp.empId}"></i>
</td>

      </tr>
    `;
  } catch (error) {
    console.error("Error:", error);
    alert("Unauthorized or error loading employee profile.");
  }
}

// Delegate click to dynamically added icons
document.addEventListener("click", function (e) {
  if (e.target.classList.contains("view-profile")) {
    const empId = e.target.getAttribute("data-emp-id");
    if (empId) {
      // Redirect to profile page with empId in query string
      window.location.href = `/viewProfile?empId=${empId}`;
    }
  }
});




function scrollTable(direction) {
  const container = document.getElementById("scrollTableContainer").querySelector(".custom-scroll");
  const scrollAmount = 200;
  container.scrollLeft += (direction === "left" ? -scrollAmount : scrollAmount);
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