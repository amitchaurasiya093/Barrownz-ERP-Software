// helper function
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

// basi api
const apiUrl = "http://localhost:8080/api/quotes";

// submit quote
document.addEventListener("DOMContentLoaded", () => {
    const quoteForm = document.getElementById("quoteForm");

    if (quoteForm) {
        quoteForm.addEventListener("submit", async function (e) {
            e.preventDefault();

            const quoteText = document.getElementById("quoteText").value.trim();
            const empCode = localStorage.getItem("empCode"); 

            if (!quoteText || !empCode) {
                alert("Quote text or employee code is missing.");
                return;
            }

            // Parse empCode to integer (important for backend)
            const parsedEmpCode = parseInt(empCode);

            // Ensure empCode is a valid number
            if (isNaN(parsedEmpCode)) {
                alert("Invalid employee code.");
                return;
            }

            const quoteData = {
                quoteText: quoteText,
                empCode: parsedEmpCode // Sending parsed integer empCode
            };

            try {
                const response = await fetch(`${apiUrl}`, {
                    method: "POST",
                    headers: getAuthHeaders(true),
                    body: JSON.stringify(quoteData)
                });

                if (response.ok) {
                    const savedQuote = await response.json();
                    displayQuote(savedQuote);
                    document.getElementById("quoteText").value = "";
                    // alert("Quote saved successfully.");

                    Swal.fire({
                        title: "Success!",
                        text: "Quote saved successfully.",
                        icon: "success",
                        confirmButtonText: "OK",
                        customClass: {
                              confirmButton: "btn-custom"
                        },
                        buttonsStyling: false
                    });

                    //close modal
                    const modalElement = document.getElementById("addQuoteModal");
                    const modalInstance = bootstrap.Modal.getOrCreateInstance(modalElement);
                    modalInstance.hide();
                } else {
                    const errorText = await response.text();
                    console.error("Status:", response.status);
                    console.error("Response text:", errorText);
                    alert(`Error saving quote: ${response.status} - ${errorText}`);
                }
            } catch (error) {
                console.error("Error:", error);
                // alert("An error occurred while saving the quote.");
                Swal.fire("Something went wrong. Try Again");
            }
        });

        loadAllQuotesAndShowLatest(); // Load latest quote 
    }
});

// load all quotes and display all
async function loadAllQuotesAndShowLatest() {
    try {
        const response = await fetch(apiUrl, {
            headers: getAuthHeaders(false)
        });

        if (response.ok) {
            const quotes = await response.json();
            if (quotes && quotes.length > 0) {
                // Sort quotes by createdDate (descending)
                quotes.sort((a, b) => new Date(b.createdDate) - new Date(a.createdDate));
                
                const latestQuote = quotes[0]; // Now truly the latest one
                displayQuote(latestQuote);
            } else {
                console.warn("No quotes found.");
            }
        } else {
            console.error("Failed to fetch quotes:", response.status);
        }
    } catch (error) {
        console.error("Error loading quotes:", error);
    }
}


// display quotes
function displayQuote(quote) {
    const container = document.getElementById("quoteContainer");
    if (!container || !quote) return;

    // Debug log
    console.log("Quote received:", quote);

    // Format date using quote.dateCreated (fallback to current date if missing)
    const createdDate = new Date(quote.dateCreated || new Date());
    const formattedDate = createdDate.toLocaleString('en-GB', {
        day: '2-digit',
        month: '2-digit',
        year: 'numeric',
        hour: '2-digit',
        minute: '2-digit',
        hour12: true
    });

    // Prepare employee full name
    let fullName = "Unknown Employee";

    if (quote.employee) {
        const firstName = quote.employee.first_name || '';
        const lastName = quote.employee.last_name || '';
        fullName = `${firstName} ${lastName}`.trim();
    } else if (quote.employeeName) {
        fullName = quote.employeeName;
    }

    // Render HTML
    container.innerHTML = `
        <hr>
        <p class="fs-5 text-dark fw-semibold fst-italic mt-4 mb-2 text-center">
            “${quote.quoteText || 'No quote available'}”
        </p>
        <p class="text-primary text-center">
            __(${quote.empCode || 'N/A'}) ${fullName}__
        </p>
        <div class="d-flex justify-content-between align-items-center mt-4 px-3">
            <button class="btn btn-primary text-white" data-bs-toggle="modal" data-bs-target="#addQuoteModal">New Quotes</button>
            <span class="fw-bold">Date - ${formattedDate}</span>
        </div>
    `;
}




