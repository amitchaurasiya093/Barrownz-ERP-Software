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
  ///

document.addEventListener("DOMContentLoaded", () => {
    fetch("http://localhost:8080/api/employees/birthdays/current-month", {
        headers: getAuthHeaders()
    })
        .then(response => {
            if (!response.ok) throw new Error("Failed to fetch birthdays");
            return response.json();
        })
        .then(data => {
            const container = document.getElementById("birthdayScroll");
            container.innerHTML = ""; 

            if (data.length === 0) {
                container.innerHTML =
                    `<div class="text-muted text-center">No birthdays this month 🎉</div>`;
                return;
            }

            data.forEach(emp => {
                const birthdayDiv = document.createElement("div");
                birthdayDiv.className = "border rounded p-2 mb-2";
                birthdayDiv.innerHTML = `
                    <div class="d-flex justify-content-between align-items-center">
                        <strong>${emp.name}</strong>
                        <span class="text-muted small">★ Upcoming Birthday</span>
                    </div>
                    <div class="text-muted mt-1">${emp.dob}</div>
                `;
                container.appendChild(birthdayDiv);
            });
        })
        .catch(error => {
            console.error("Error fetching birthdays:", error);
            document.getElementById("birthdayScroll").innerHTML =
                `<div class="text-danger text-center">Unable to load birthdays ❌</div>`;
        });
});
