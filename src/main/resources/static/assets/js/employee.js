//helper function for generating headers
function getAuthHeaders(isJson = false) {
  const token = localStorage.getItem('token');
  const headers = { 'Authorization': `Bearer ${token}` };
  if (isJson) headers['Content-Type'] = 'application/json';
  return headers;
}
///


const API = 'http://localhost:8080/api';

// Load dropdowns (Add or Edit)
async function loadDropdown(endpoint, selectorId, placeholder = 'Select', selectedValue = null) {
  try {
    const res = await fetch(`${API}/${endpoint}`, {
     headers: getAuthHeaders()    //added jwt token
    });
    const arr = await res.json();
    const sel = document.getElementById(selectorId);

    const fieldMap = {
      departments: { id: 'deptId', name: 'dept_name' },
      levels: { id: 'level_id', name: 'level' },
      shifts: { id: 'shift_id', name: 'shift_name' },
      roles: { id: 'role_id', name: 'role_name' }
    };

    const { id: idField, name: nameField } = fieldMap[endpoint];

    sel.innerHTML = `<option value="">-- ${placeholder} --</option>` + 
      arr.map(o => 
        `<option value="${o[idField]}" ${selectedValue == o[idField] ? 'selected' : ''}>${o[nameField]}</option>`
      ).join('');

  } catch (error) {
    console.error(`Error loading ${endpoint}:`, error);
  }
}


// Load managers by department for Add form
async function loadReportingManagersByDepartment(deptId) {
  try {
    const res = await fetch(`${API}/managers/by-department/${deptId}`, {
       headers: getAuthHeaders()    //added jwt token
    });
    const data = await res.json();

    const sel1 = document.getElementById('rep_name1');
    const sel2 = document.getElementById('rep_name2');

    sel1.innerHTML = `<option value="">-- Select Manager 1 --</option>`;
    sel2.innerHTML = `<option value="">-- Select Manager 2 --</option>`;

    data.forEach(rm => {
      if (rm.rm_name1) {
        sel1.innerHTML += `<option value="${rm.rm_id}">${rm.rm_name1}</option>`;
      }
      if (rm.rm_name2) {
        sel2.innerHTML += `<option value="${rm.rm_id}">${rm.rm_name2}</option>`;
      }
    });
  } catch (error) {
    console.error("Error loading managers:", error);
  }
}

// Load managers for edit form
async function loadReportingManagersByDepartment(deptId, sel1Id = 'rep_name1', sel2Id = 'rep_name2', selected1 = null, selected2 = null) {
  try {
    const res = await fetch(`${API}/managers/by-department/${deptId}`, {
        headers: getAuthHeaders()  //added jwt token
    });
    const data = await res.json();

    if (!Array.isArray(data)) {
      console.error("Expected array, got:", data);
      return;
    }

    const sel1 = document.getElementById(sel1Id);
    const sel2 = document.getElementById(sel2Id);

    sel1.innerHTML = `<option value="">-- Select Manager 1 --</option>`;
    sel2.innerHTML = `<option value="">-- Select Manager 2 --</option>`;

    data.forEach(rm => {
      if (rm.rm_name1) {
  sel1.innerHTML += `<option value="${rm.rm_id}" ${String(selected1) === String(rm.rm_id) ? 'selected' : ''}>${rm.rm_name1}</option>`;
}
if (rm.rm_name2) {
  sel2.innerHTML += `<option value="${rm.rm_id}" ${String(selected2) === String(rm.rm_id) ? 'selected' : ''}>${rm.rm_name2}</option>`;
}

    });

  } catch (error) {
    console.error("Error loading reporting managers:", error);
  }
}



async function loadAllDropdowns() {
  await Promise.all([
    loadDropdown('departments', 'dept', 'Department'),
    loadDropdown('levels', 'level', 'Level'),
    loadDropdown('shifts', 'shift', 'Shift'),
    loadDropdown('roles', 'role', 'Role')
  ]);
}

function resetForm() {
  const f = document.getElementById('employeeForm');
  f.reset();
  delete f.dataset.id;
}

// Build DTO object from form
function buildEmpFromForm() {
  const f = document.getElementById('employeeForm');
  const emp = {
    // emp_code: +f.empCode.value,
    first_name: f.firstName.value,
    last_name: f.lastName.value,
    dateOfBirth: f.dob.value,
    gender: f.gender.value,
    email: f.email.value,
    personal_email: f.personalEmail.value,
    address: f.address.value,
    contact: f.contact.value,
    joining_date: f.joiningDate.value,
    company: f.company.value,
    employee_status: f.employeeStatus.value,
    joining_status: f.joiningStatus.value,
    working_status: f.workingStatus.value,
    departmentId: f.dept.value ? parseInt(f.dept.value) : null,
    levelId: f.level.value ? parseInt(f.level.value) : null,
    shiftId: f.shift.value ? parseInt(f.shift.value) : null,
    roleId: f.role.value ? parseInt(f.role.value) : null,
    password: f.password.value  // Added password here
  };

  if (f.rep_name1.value) emp.reportingManager1Id = parseInt(f.rep_name1.value);
  if (f.rep_name2.value) emp.reportingManager2Id = parseInt(f.rep_name2.value);
  return emp;
}


// Submit New Form
async function submitForm(e) {
  e.preventDefault();
  const f = document.getElementById('employeeForm');
  const formData = new FormData();
  const emp = buildEmpFromForm();
  formData.append('employee', JSON.stringify(emp));

  const fileInput = f.querySelector('.file-upload');
  if (fileInput && fileInput.files.length > 0) {
    formData.append('image', fileInput.files[0]);
  }

  const id = f.dataset.id;
  const res = await fetch(id ? `${API}/employees/${id}` : `${API}/employees`, {
    method: id ? 'PUT' : 'POST',
    body: formData,
    headers: getAuthHeaders(false)  //added jwt token
  });

  if (res.ok) {
    resetForm();
    await loadEmployees();


  //   showSuccessAlert(id ? 'Employee updated successfully' : 'Employee added successfully');
  // } else {
  //   console.error('Error submitting form:', await res.text());
  // }

    Swal.fire({
      icon: 'success',
      title: 'Success!',
      text: id ? 'Employee updated successfully' : 'Employee added successfully',
      showConfirmButton: true,
      timer: 2000
    });
}
else {
  const errorMsg = await res.text();
  console.error('Error submitting form:', errorMsg);

  Swal.fire({
    icon: 'error',
    title: 'Oops..',
    text: errorMsg || 'Something went wrong!'
  })
}
}

// Load employees
async function loadEmployees() {

  // const token = localStorage.getItem('token');
  // console.log("JWT Token (loadEmployees):", token);

  const res = await fetch(`${API}/employees`, {
    headers: getAuthHeaders()   //added jwt token
  });
  const arr = await res.json();
  const tbody = document.querySelector('#employeeTable tbody');
  if (!tbody) return;

    // If DataTable exists, destroy it first
    if ($.fn.DataTable.isDataTable('#employeeTable')) {
      $('#employeeTable').DataTable().destroy();
    }

  tbody.innerHTML = arr.map(emp => `
    <tr>
      <td>
  ${
    emp.profile_picture
      ? `<img
            src="http://localhost:8080/uploads/${emp.profile_picture}"
            style="width:50px; height:50px; border-radius:50%; object-fit:cover; cursor:pointer;"
            class="img-fluid rounded-circle"
            onclick="showImageModal('http://localhost:8080/uploads/${emp.profile_picture}', '${emp.first_name}', '${emp.last_name}')">`
      : '-'
  }
</td>
 
      <td>${emp.empId || '-'}</td>
      <td>${emp.emp_code || '-'}</td>
      <td>${emp.first_name || ''}</td>
      <td>${emp.last_name || ''}</td>
      <td>${emp.dateOfBirth || '-'}</td>
      <td>${emp.gender || '-'}</td>
      <td>${emp.personal_email || '-'}</td>
      <td>${emp.email || '-'}</td>
      <td>${emp.address || '-'}</td>
      <td>${emp.contact || '-'}</td>
      <td>${emp.joining_date || '-'}</td>
      <td>${emp.levelName || '-'}</td>
      <td>${emp.company || '-'}</td>
      <td>${emp.shiftName || '-'}</td>
      <td>${emp.employee_status || '-'}</td>
      <td>${emp.joining_status || '-'}</td>
      <td>${emp.working_status || '-'}</td>
      <td>${emp.departmentName || '-'}</td>
      <td>${emp.reportingManager1Name || '-'}</td>
      <td>${emp.reportingManager2Name || '-'}</td>  
      <td>${emp.roleName || '-'}</td>
      <td>

  <i class="fa-solid fa-pen-to-square text-primary me-2 edit" data-id="${emp.empId}" style="cursor: pointer; font-size: 18px;"></i>
  <i class="fa-solid fa-trash text-danger delete" data-id="${emp.empId}" style="cursor: pointer; font-size: 18px;"></i>


      </td>
    </tr>
  `).join('');

  $('#employeeTable').DataTable();

  tbody.querySelectorAll('.edit').forEach(btn => {
    btn.addEventListener('click', () => fillForm(btn.dataset.id));
  });

  tbody.querySelectorAll('.delete').forEach(btn => {
  btn.addEventListener('click', () => {
    selectedDeleteEmpId = btn.dataset.id;
    const modal = new bootstrap.Modal(document.getElementById('deleteConfirmModal'));
    modal.show();
  });
});


}

// Fill Edit Modal
async function fillForm(id) {
  const res = await fetch(`${API}/employees/${id}`, {
    headers: getAuthHeaders()   //added jwt token
  });
  const emp = await res.json();

  const f = document.getElementById('editEmpForm');
  f.dataset.id = emp.empId;

  document.getElementById('emp_id_hidden').value = emp.empId;
  f.editempCode.value = emp.emp_code;
  f.editfirstName.value = emp.first_name;
  f.editlastName.value = emp.last_name;
  f.editdob.value = emp.dateOfBirth;
  f.editgender.value = emp.gender;
  f.editemail.value = emp.email;
  f.editpersonalEmail.value = emp.personal_email;
  f.editaddress.value = emp.address;
  f.editcontact.value = emp.contact;
  f.editjoiningDate.value = emp.joining_date;
  f.editcompany.value = emp.company;
  f.editemployeeStatus.value = emp.employee_status;
  f.editjoiningStatus.value = emp.joining_status;
  f.editworkingStatus.value = emp.working_status;

//   console.log("Selected Dept ID:", emp.departmentId);
// console.log("Selected Role ID:", emp.roleId);
// console.log("RM1:", emp.reportingManager1Id, "RM2:", emp.reportingManager2Id);


  // Load dropdowns with selected value
await loadDropdown('departments', 'editdept', 'Department', emp.departmentId || emp.department?.deptId);
// await loadDropdown('levels', 'editlevel', 'Level', emp.levelId || emp.level?.level_id);
await loadDropdown('levels', 'editlevel', 'Level', emp.emp_level?.level_id);
await loadDropdown('shifts', 'editshift', 'Shift', emp.shiftId || emp.shift?.shift_id);
await loadDropdown('roles', 'editrole', 'Role', emp.roleId || emp.role?.role_id);

if (emp.department?.deptId) {
  await loadReportingManagersByDepartment(
    emp.department.deptId,
    'editrep_name1',
    'editrep_name2',
    emp.reporting_manager1?.rm_id,
    emp.reporting_manager2?.rm_id
  );
} else {
  console.warn("⚠️ Department ID is missing for employee", emp);
}


  // Show modal
  const modal = new bootstrap.Modal(document.getElementById('editModal'));
  modal.show();
}



// Submit Edit Modal
document.getElementById('editEmpForm').addEventListener('submit', async function (e) {
  e.preventDefault();
  const id = document.getElementById('emp_id_hidden').value;
  const f = e.target;

  const emp = {
    emp_code: +f.editempCode.value,
    first_name: f.editfirstName.value,
    last_name: f.editlastName.value,
    dateOfBirth: f.editdob.value,
    gender: f.editgender.value,
    email: f.editemail.value,
    personal_email: f.editpersonalEmail.value,
    address: f.editaddress.value,
    contact: f.editcontact.value,
    joining_date: f.editjoiningDate.value,
    company: f.editcompany.value,
    employee_status: f.editemployeeStatus.value,
    joining_status: f.editjoiningStatus.value,
    working_status: f.editworkingStatus.value,
    departmentId: +f.editdept.value,
    levelId: +f.editlevel.value,
    shiftId: +f.editshift.value,
    roleId: +f.editrole.value,
    reportingManager1Id: f.editrep_name1.value ? +f.editrep_name1.value : null,
    reportingManager2Id: f.editrep_name2.value ? +f.editrep_name2.value : null
  };

  const formData = new FormData();
  formData.append('employee', JSON.stringify(emp));

  const res = await fetch(`${API}/employees/${id}`, {
    method: 'PUT',
    body: formData,
    headers: getAuthHeaders(false)  //added jwt token
  });

  if (res.ok) {
    bootstrap.Modal.getInstance(document.getElementById('editModal')).hide();
    await loadEmployees();
    
    // showSuccessAlert("Employee updated successfully");
    Swal.fire({
      icon: 'success',
      title: 'Success!',
      text: 'Employee updated successfully',
      showConfirmButton: true,
      timer: 2000
    })
  } else {
    const errorMsg = await res.text();
    console.error(errorMsg);

    Swal.fire({
      icon: 'error',
      title: 'Oops..',
      text: errorMsg || 'Something went wrong!'
    });
  }
});

function confirmDelete(empId) {
  selectedDeleteEmpId = empId;
  const modal = new bootstrap.Modal(document.getElementById('deleteConfirmModal'));
  modal.show();
}


// Delete
// document.getElementById('confirmDeleteBtn').addEventListener('click', async () => {
//   if (!selectedDeleteEmpId) return;

//   try {
//     const res = await fetch(`${API}/employees/${selectedDeleteEmpId}`, {
//       method: 'DELETE',
//       headers: getAuthHeaders()   
//     });

//     if (res.ok) {
      
//       showDangerAlert("Employee deleted successfully");
//       await loadEmployees();
//     } else {
//             showDangerAlert("Employee deleted successfully");
//       await loadEmployees();
//     }
//   } catch (err) {
//     console.error("Error during delete:", err);
//   } finally {
//     selectedDeleteEmpId = null;
//     const modalEl = document.getElementById('deleteConfirmModal');
//     if (bootstrap.Modal.getInstance(modalEl)) {
//       bootstrap.Modal.getInstance(modalEl).hide();
//     }
//   }
// });

document.getElementById('confirmDeleteBtn').addEventListener('click', async () => {
  if (!selectedDeleteEmpId) return;

  try {
    const res = await fetch(`${API}/employees/${selectedDeleteEmpId}`, {
      method: 'DELETE',
      headers: getAuthHeaders()
    });

    const message = await res.text();

    if (res.ok) {
      showSuccessAlert(message || "Employee deleted successfully");
      await loadEmployees();
    } else {
      showDangerAlert(message || "Employee is assigned in salary table, cannot be deleted");
    }
  } catch (err) {
    console.error("Error during delete:", err);
    showDangerAlert("Something went wrong while deleting employee");
  } finally {
    selectedDeleteEmpId = null;
    const modalEl = document.getElementById('deleteConfirmModal');
    if (bootstrap.Modal.getInstance(modalEl)) {
      bootstrap.Modal.getInstance(modalEl).hide();
    }
  }
});



// Alert
function showSuccessAlert(message) {
  const alert = document.createElement('div');
  alert.className = 'alert alert-success alert-dismissible fade show fixed-top mx-auto m-3';
  alert.role = 'alert';
  alert.style.width = 'fit-content';
  alert.innerHTML = `${message}
    <button type="button" class="btn-close" data-bs-dismiss="alert"></button>`;
  document.body.appendChild(alert);
  setTimeout(() => alert.remove(), 3000);
}

function showDangerAlert(message) {
  const alert = document.createElement('div');
  alert.className = 'alert alert-danger alert-dismissible fade show fixed-top mx-auto m-3';
  alert.role = 'alert';
  alert.style.width = 'fit-content';
  alert.innerHTML = `${message}
    <button type="button" class="btn-close" data-bs-dismiss="alert"></button>`;
  document.body.appendChild(alert);
  setTimeout(() => alert.remove(), 3000);
}

// On page load
document.addEventListener('DOMContentLoaded', () => {
  loadAllDropdowns();
  loadEmployees();

  document.getElementById('employeeForm').addEventListener('submit', submitForm);

  // Add Form - Department change listener
  document.getElementById('dept').addEventListener('change', (e) => {
    if (e.target.value) loadReportingManagersByDepartment(e.target.value);
  });

  //  Edit Form - Department change listener (Fix Added)
  document.getElementById('editdept').addEventListener('change', async (e) => {
    const deptId = e.target.value;
    if (deptId) {
      await loadReportingManagersByDepartment(
        deptId,
        'editrep_name1',
        'editrep_name2'
      );
    }
  });
});



// Optional scroll arrows
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

document.getElementById('employeeForm').addEventListener('submit', function(event) {
  event.preventDefault();
  if (validateForm()) {
      buildEmpFromForm(event); 
  }
});

function validateForm() {
  let isValid = true;
  
  // Clear previous error messages
  document.querySelectorAll('.error-message').forEach(el => el.remove());
  document.querySelectorAll('.is-invalid').forEach(el => el.classList.remove('is-invalid'));

  // Employee Code validation
  // const empCode = document.getElementById('empCode');
  // if (!empCode.value.trim()) {
  //     showError(empCode, 'Employee Code is required');
  //     isValid = false;
  // } else if (isNaN(empCode.value) || empCode.value <= 0) {
  //     showError(empCode, 'Employee Code must be a positive number');
  //     isValid = false;
  // }

  // First Name validation
  const firstName = document.getElementById('firstName');
  if (!firstName.value.trim()) {
      showError(firstName, 'First Name is required');
      isValid = false;
  } else if (!/^[a-zA-Z]+$/.test(firstName.value.trim())) {
      showError(firstName, 'First Name should contain only letters');
      isValid = false;
  }

  // Last Name validation
  const lastName = document.getElementById('lastName');
  if (!lastName.value.trim()) {
      showError(lastName, 'Last Name is required');
      isValid = false;
  } else if (!/^[a-zA-Z]+$/.test(lastName.value.trim())) {
      showError(lastName, 'Last Name should contain only letters');
      isValid = false;
  }

  // Date of Birth validation
  const dob = document.getElementById('dob');
  if (!dob.value) {
      showError(dob, 'Date of Birth is required');
      isValid = false;
  } else {
      const dobDate = new Date(dob.value);
      const today = new Date();
      const minAgeDate = new Date(today.getFullYear() - 18, today.getMonth(), today.getDate());
      
      if (dobDate > minAgeDate) {
          showError(dob, 'Employee must be at least 18 years old');
          isValid = false;
      }
  }

  // Gender validation
  const gender = document.getElementById('gender');
  if (!gender.value) {
      showError(gender, 'Please select a gender');
      isValid = false;
  }

  // Email validation
  const email = document.getElementById('email');
  if (!email.value.trim()) {
      showError(email, 'Email is required');
      isValid = false;
  } else if (!/^[a-zA-Z][a-zA-Z0-9._-]*@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$/.test(email.value.trim())) {
      showError(email, 'Please enter a valid email (must start with letter)');
      isValid = false;
  }

  // Personal Email validation
  const personalEmail = document.getElementById('personalEmail');
  if (personalEmail.value.trim() && !/^[a-zA-Z][a-zA-Z0-9._-]*@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$/.test(personalEmail.value.trim())) {
      showError(personalEmail, 'Please enter a valid personal email (must start with letter)');
      isValid = false;
  }

  // Address validation
  const address = document.getElementById('address');
  if (!address.value.trim()) {
      showError(address, 'Address is required');
      isValid = false;
  }

  // Contact validation
  const contact = document.getElementById('contact');
  if (!contact.value.trim()) {
      showError(contact, 'Mobile Number is required');
      isValid = false;
  } else if (!/^[0-9]{10,15}$/.test(contact.value.trim())) {
      showError(contact, 'Please enter a valid mobile number (10-15 digits)');
      isValid = false;
  }

  // Joining Date validation
const joiningDate = document.getElementById('joiningDate');
if (!joiningDate.value) {
    showError(joiningDate, 'Joining Date is required');
    isValid = false;
}

  // Employee Level validation
  const level = document.getElementById('level');
  if (!level.value) {
      showError(level, 'Please select an employee level');
      isValid = false;
  }

  // Company validation
  const company = document.getElementById('company');
  if (!company.value) {
      showError(company, 'Please select a company');
      isValid = false;
  }

  // Shift validation
  const shift = document.getElementById('shift');
  if (!shift.value) {
      showError(shift, 'Please select a shift');
      isValid = false;
  }

  // Employee Status validation
  const employeeStatus = document.getElementById('employeeStatus');
  if (!employeeStatus.value) {
      showError(employeeStatus, 'Please select an employee status');
      isValid = false;
  }

  // Joining Status validation
  const joiningStatus = document.getElementById('joiningStatus');
  if (!joiningStatus.value) {
      showError(joiningStatus, 'Please select a joining status');
      isValid = false;
  }

  // Working Status validation
  const workingStatus = document.getElementById('workingStatus');
  if (!workingStatus.value) {
      showError(workingStatus, 'Please select a working status');
      isValid = false;
  }

  // Department validation
  const dept = document.getElementById('dept');
  if (!dept.value) {
      showError(dept, 'Please select a department');
      isValid = false;
  }

  // Reporting Manager 1 validation
  const rep_name1 = document.getElementById('rep_name1');
  if (!rep_name1.value) {
      showError(rep_name1, 'Please select Reporting Manager 1');
      isValid = false;
  }

  // Role validation
  const role = document.getElementById('role');
  if (!role.value) {
      showError(role, 'Please select a role');
      isValid = false;
  }

  // Password validation
  const password = document.getElementById('password');
  if (!password.value) {
      showError(password, 'Password is required');
      isValid = false;
  } else if (password.value.length < 8) {
      showError(password, 'Password must be at least 8 characters long');
      isValid = false;
  } else if (!/(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[^A-Za-z0-9])/.test(password.value)) {
      showError(password, 'Password must contain at least one uppercase letter, one lowercase letter, one number, and one special character');
      isValid = false;
  }

  // Confirm Password validation
  const confirmPassword = document.getElementById('confirmPassword');
  if (!confirmPassword.value) {
      showError(confirmPassword, 'Please confirm your password');
      isValid = false;
  } else if (confirmPassword.value !== password.value) {
      showError(confirmPassword, 'Passwords do not match');
      isValid = false;
  }

  // Profile Picture validation (optional)
  const profileImageInput = document.getElementById('profileImageInput');
  if (profileImageInput.files.length > 0) {
      const file = profileImageInput.files[0];
      const validImageTypes = ['image/jpeg', 'image/png', 'image/gif'];
      
      if (!validImageTypes.includes(file.type)) {
          showError(profileImageInput, 'Please upload a valid image file (JPEG, PNG, GIF)');
          isValid = false;
      }
      
      if (file.size > 2 * 1024 * 1024) { // 2MB limit
          showError(profileImageInput, 'Image size should be less than 2MB');
          isValid = false;
      }
  }

  return isValid;
}

function showError(inputElement, message) {
  inputElement.classList.add('is-invalid');
  
  const errorElement = document.createElement('div');
  errorElement.className = 'error-message text-danger mt-1 small';
  errorElement.textContent = message;
  
  inputElement.parentNode.appendChild(errorElement);
}

// Image preview functionality
document.getElementById('profileImageInput').addEventListener('change', function(event) {
  const file = event.target.files[0];
  if (file) {
      const reader = new FileReader();
      reader.onload = function(e) {
          const preview = document.getElementById('imagePreview');
          preview.src = e.target.result;
          preview.style.display = 'block';
      };
      reader.readAsDataURL(file);
  }
});

//method to show Image Modal
function showImageModal(imageUrl, firstName, lastName) {
  document.getElementById("modalImage").src = imageUrl;
  document.getElementById("modalEmployeeName").textContent = `${firstName || ''} ${lastName || ''}`;

  const myModal = new bootstrap.Modal(document.getElementById('imageModal'));
  myModal.show();
}


