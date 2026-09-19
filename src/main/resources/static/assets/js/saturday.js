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

const sat_api = "http://localhost:8080/api/saturday";
let editId = null;

document.addEventListener("DOMContentLoaded", () => {
        fetchSaturdays();
});

//submit form
document.getElementById("saturdayForm").addEventListener("submit", async function (e) {
        e.preventDefault();

       const month = document.getElementById("month").value;
           const year = document.getElementById("year").value;
           const saturdayDate = document.getElementById("saturdayDate").value;

           if(!month || !year || !saturdayDate) {
              alert("Please fill all fields.");
              return;
           }

           const data = {
                date: saturdayDate,
                month: month,
                year: year
           };  

           try {

               const url = editId ? `${sat_api}/${editId}` : sat_api;
               const method = editId ? "PUT" : "POST";

             const res = await fetch(url, {
                    method: method, 
                    headers: getAuthHeaders(),
                    body: JSON.stringify(data)
               });

               if(res.ok) {
                  alert(editId ? "saturday updated successfully!" : "saturday saved successfully!");
                  document.getElementById("saturdayForm").reset();
                  editId = null;
                  document.querySelector("#saturdayForm button[type='submit']").textContent = "Generate and save";
                  fetchSaturdays();
               }
               else {
                const err = await res.json();
                alert("Error: " + (err.message || "unable to save"));
               }
           } 
           catch (error) {
               console.error("error saving saturday: " + error);
               alert("something went wrong!");
           }
});

//fetch list

async function fetchSaturdays() {
      try {
        const res = await fetch(sat_api, {
              method: "GET",
              headers: getAuthHeaders()
          });

          if(res.ok) {
            const result = await res.json();
            // renderSaturdayList(data);
            renderSaturdayList(result.data || []);
          }
          else {
            console.error("failed to fetch saturdays");
          }
      } 
      catch (error) {
          console.error("Error: " + error);
      }
}

//render list on table
function renderSaturdayList(list) {
       const tbody = document.getElementById("saturdayList");
       tbody.innerHTML = "";

       list.forEach(sat => {
          const tr =  document.createElement("tr");
          tr.innerHTML = `
               <td>${sat.id}</td>
               <td>${sat.date}</td>
               <td>${sat.monthDisplay}</td>
               <td>${sat.year}</td>
               <td>
               <i class="fa-solid fa-pen-to-square text-primary me-2" onclick="editSaturday(${sat.id}, '${sat.date}', ${sat.month}, ${sat.year})"></i>
                 <i class="fa-solid fa-trash text-danger" onclick="deleteSaturday(${sat.id})"></i>
               </td>

          `;
          tbody.appendChild(tr);
       });
}

// delete Saturday
async function deleteSaturday(id) {
    if (!confirm("Are you sure you want to delete this Saturday?")) return;
  
    try {
      const res = await fetch(`${sat_api}/${id}`, {
        method: "DELETE",
        headers: getAuthHeaders(false)
      });
  
      if (res.ok) {
        alert("Saturday deleted successfully!");
        fetchSaturdays();
      } else {
        alert("Failed to delete Saturday");
      }
    } catch (error) {
      console.error("Error deleting:", error);
    }
  }

  // edit Saturday
function editSaturday(id, date, month, year) {
    editId = id;
    document.getElementById("saturdayDate").value = date;
    document.getElementById("month").value = month;
    document.getElementById("year").value = year;
    document.querySelector("#saturdayForm button[type='submit']").textContent = "Update Saturday";
    window.scrollTo({ top: 0, behavior: 'smooth' });
  }

