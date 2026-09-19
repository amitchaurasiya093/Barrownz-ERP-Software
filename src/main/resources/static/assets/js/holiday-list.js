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
  ////

  document.addEventListener("DOMContentLoaded", function () {
    fetch("http://localhost:8080/api/holidays", {
        headers: getAuthHeaders()
    })
      .then(response => {
        if (!response.ok) {
          throw new Error("Failed to fetch holiday data");
        }
        return response.json();
      })
      .then(data => {
        const tbody = document.getElementById("holidayTableBody");
        tbody.innerHTML = "";

        // Filter only Upcoming Holidays
        const upcomingHolidays = data.filter(holiday => holiday.status === 'Upcoming Holiday');

        // If no upcoming holidays
        if (upcomingHolidays.length === 0) {
          const row = document.createElement("tr");
          row.innerHTML = `<td colspan="4" class="text-center text-muted">No Upcoming Holidays</td>`;
          tbody.appendChild(row);
          return;
        }

        // Display only upcoming holidays
        upcomingHolidays.forEach((holiday, index) => {
          const row = document.createElement("tr");
          row.innerHTML = `
            <td class="text-center">${index + 1}</td>
            <td class="text-center">${holiday.title}</td>
            <td class="text-center">${holiday.date}</td>
            <td class="text-center">
              <button class="btn btn-sm rounded-5 btn-success">
                ${holiday.status}
              </button>
            </td>
          `;
          tbody.appendChild(row);
        });
      })
      .catch(error => {
        console.error("Error loading holidays:", error);
      });
  });