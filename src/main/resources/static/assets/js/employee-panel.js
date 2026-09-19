//helper function
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

//fetching employee profile and set welcome name
document.addEventListener("DOMContentLoaded", function () {
    const token = localStorage.getItem("token");

    if (token) {
        fetch("http://localhost:8080/api/employees/profile", {
            method: "GET",
            headers: getAuthHeaders()
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error("Failed to fetch employee profile");
                }
                return response.json();
            })
            .then(employee => {
                const fullName = `${employee.first_name} ${employee.last_name}`;
                localStorage.setItem("employeeName", fullName);

                const heading = document.getElementById("welcomeHeading");
                if (heading) {
                    heading.textContent = `Welcome ${fullName}..!`;
                }
            })
            .catch(error => {
                console.error("Error loading employee data:", error);
            });
    } else {
        console.warn("JWT token not found. Please login first.");
    }




    ////script for showing new joinees 
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
                            (${emp.empCode}) ${emp.employeeName}
                        </td>
                        <td class="text-end" style="white-space: normal;">
                            Joined on ${emp.joinedOn}
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


   


