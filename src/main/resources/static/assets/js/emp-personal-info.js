const API_BASE_URL = 'http://localhost:8080/api';
let currentPersonalInfo = null;

// Helper functions
function getAuthHeaders() {
    const token = localStorage.getItem('token');
    return token ? {
        "Authorization": `Bearer ${token}`,
        "Content-Type": "application/json"
    } : {};
}

function showAlert(message, type = 'success') {
    const alertDiv = document.getElementById('personalInfoAlert');
    alertDiv.innerHTML = `<div class="alert alert-${type}">${message}</div>`;
    setTimeout(() => alertDiv.innerHTML = '', 3000);
}

// Display data in UI
function displayPersonalInfo(data) {
    const fields = {
        'height': 'height',
        'weight': 'weight',
        'passport': 'passportNumber',
        'pan': 'panNumber',
        'aadhar': 'aadharNumber',
        'religion': 'religion',
        'marital-status': 'maritalStatus',
        'blood-group': 'bloodGroup',
        'shift': 'shift'
    };
    
    Object.entries(fields).forEach(([id, key]) => {
        document.getElementById(`display-${id}`).textContent = data?.[key] || '-';
    });
}

// Modal handling - completely simplified
function setupModal() {
    const modalEl = document.getElementById('employeeInfoModal');
    const modal = new bootstrap.Modal(modalEl);
    
    // Pre-fill form when modal shows
    modalEl.addEventListener('show.bs.modal', () => {
        if (currentPersonalInfo) {
            document.getElementById("height").value = currentPersonalInfo.height || '';
            document.getElementById("weight").value = currentPersonalInfo.weight || '';
            document.getElementById("passport").value = currentPersonalInfo.passportNumber || '';
            document.getElementById("pan").value = currentPersonalInfo.panNumber || '';
            document.getElementById("aadhar").value = currentPersonalInfo.aadharNumber || '';
            document.getElementById("religion").value = currentPersonalInfo.religion || '';
            document.getElementById("marital_status").value = currentPersonalInfo.maritalStatus || '';
            document.getElementById("blood_group").value = currentPersonalInfo.bloodGroup || '';
            document.getElementById("shift").value = currentPersonalInfo.shift || '';
        }
    });
    
    // Clear form when modal hides
    modalEl.addEventListener('hidden.bs.modal', () => {
        document.getElementById("employeeInfoForm").reset();
    });
    
    return modal;
}

// Fetch employee data
async function fetchPersonalInfo() {
    try {
        const empId = parseInt(localStorage.getItem('empId'));
        if (!empId) return;
        
        const response = await fetch(`${API_BASE_URL}/personal-info/employee/${empId}`, {
            headers: getAuthHeaders()
        });
        
        if (response.ok) {
            currentPersonalInfo = await response.json();
            displayPersonalInfo(currentPersonalInfo);
        }
    } catch (error) {
        console.error('Fetch error:', error);
    }
}

// Save employee data
async function savePersonalInfo(event) {
    event.preventDefault();
    
    try {
        const empId = parseInt(localStorage.getItem('empId'));
        if (!empId) return;
        
        const formData = {
            height: document.getElementById("height").value,
            weight: document.getElementById("weight").value,
            passportNumber: document.getElementById("passport").value,
            panNumber: document.getElementById("pan").value,
            aadharNumber: document.getElementById("aadhar").value,
            religion: document.getElementById("religion").value,
            maritalStatus: document.getElementById("marital_status").value,
            bloodGroup: document.getElementById("blood_group").value,
            shift: document.getElementById("shift").value,
            employeeId: empId
        };
        
        const response = await fetch(`${API_BASE_URL}/personal-info`, {
            method: "POST",
            headers: getAuthHeaders(),
            body: JSON.stringify(formData)
        });
        
        if (response.ok) {
            currentPersonalInfo = await response.json();
            displayPersonalInfo(currentPersonalInfo);
            showAlert('Saved successfully!');
            bootstrap.Modal.getInstance(document.getElementById('employeeInfoModal')).hide();
        }
    } catch (error) {
        console.error('Save error:', error);
        showAlert('Save failed', 'danger');
    }
}

// Initialize everything
document.addEventListener('DOMContentLoaded', async () => {
    if (!localStorage.getItem('token')) {
        window.location.href = '/login.html';
        return;
    }

     // Load initial data
    await fetchPersonalInfo();
    
    // Set up modal once
    const modal = setupModal();
    
    // Edit button click handler
    document.querySelector('[data-bs-target="#employeeInfoModal"]').addEventListener('click', () => {
        if (!modal._isShown) {
            modal.show();
        }
    });
    
    // Form submission
    document.getElementById("employeeInfoForm").addEventListener("submit", savePersonalInfo);
    
     // 4. Add this protection for the settings button
     const settingsBtn = document.querySelector('.btn-setting.btn-primary');
     if (settingsBtn) {
         // This ensures proper hover states
         settingsBtn.addEventListener('mouseenter', function() {
             this.style.backgroundColor = '';
             this.style.borderColor = '';
         });
         
         // Reset any potential modal interference
         const resetBtnStyles = () => {
             settingsBtn.style.backgroundColor = '';
             settingsBtn.style.borderColor = '';
             settingsBtn.style.color = '';
         };
         
         // Reset after modal operations
         document.getElementById('employeeInfoModal').addEventListener('hidden.bs.modal', resetBtnStyles);
         document.getElementById('employeeInfoModal').addEventListener('shown.bs.modal', resetBtnStyles);
     }
   
});
