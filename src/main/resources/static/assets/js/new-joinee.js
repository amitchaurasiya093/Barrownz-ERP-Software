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
    const tbody = document.querySelector("#new-joinees-body");

    fetch("http://localhost:8080/api/employees/get/new-joinees", {
        method: "GET",
        headers: getAuthHeaders()
    })
    .then(response => {
        if (!response.ok) {
            throw new Error("Failed to fetch new joinees");
        }
        return response.json();
    })
    .then(data => {
        tbody.innerHTML = ""; 
        if (data.length === 0) {
            tbody.innerHTML = `<tr><td colspan="2" class="text-center">No new joinees found</td></tr>`;
        } else {
            data.forEach(emp => {
                const row = `
                    <tr>
                        <td class="fw-bold text-uppercase" style="white-space: normal; word-break: break-word;">
                            (${emp.emp_code}) ${emp.first_name} ${emp.last_name}
                        </td>
                        <td class="text-end" style="white-space: normal;">
                            Joined on ${emp.joining_date}
                        </td>
                    </tr>
                `;
                tbody.innerHTML += row;
            });
        }
    })
    .catch(error => {
        console.error(error);
        tbody.innerHTML = `<tr><td colspan="2" class="text-center text-danger">Error loading data</td></tr>`;
    });
});

