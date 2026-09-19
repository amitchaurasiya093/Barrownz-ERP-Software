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

function getEmpId() {
      return localStorage.getItem('empId'); 
}

let currentEmployee = null;
let ccEmployeeIds = [];
let ccEmployeeNames = [];

document.addEventListener('DOMContentLoaded', () => {
       const empId = getEmpId();
       if(!empId) {
          console.warn('No empId in localstorage. Please login.');
          return;
       }

     const leaveTypeSelect =  document.getElementById('leave-type');
     const applyToSelect = document.getElementById('apply-to');
     const ccToInput = document.getElementById('cc-to');
     const ccModal = document.getElementById('ccModal');
     const ccModalBody = ccModal ? ccModal.querySelector('.modal-body tbody') : null;
     const saveCcBtn = document.getElementById('saveCcBtn');
     const attachmentInput = document.getElementById('attachment');
     const fromDateInput = document.getElementById('from-date');
     const toDateInput = document.getElementById('to-date');
     const nodInput = document.getElementById('NOD');
     const balanceInput = document.getElementById('balance');
     const applyLeaveForm = document.getElementById('applyLeaveForm');
     const sessionFromInput = document.getElementById('form-session');
     const sessionToInput = document.getElementById('to-session');

     //initialize
     loadEmployeeDetails(empId)
     .then(() => Promise.all([loadLeaveTypes(), loadEmployeesForCc()]))
     .catch(err => {
          console.error('Initialization error', err);
          Swal.fire('Error', 'Failed to initialize leave form.', 'error');
     });

    //calculate number of days
    [fromDateInput, toDateInput, sessionFromInput, sessionToInput].forEach(inp => {
         if(inp) inp.addEventListener('change', computeNOD);
    });

    //when leave changes. calculate leave balance
    if(leaveTypeSelect) {
        leaveTypeSelect.addEventListener('change', async () => {
            balanceInput.value = '';

            const leaveTypeId = leaveTypeSelect.value;
            if(!leaveTypeId) return;

            const empId = getEmpId();
            if(!empId) return;

            try {
                 const res = await fetch(`/api/leave-balances/${empId}`, {
                      headers: getAuthHeaders()
                 });
                 if(!res.ok) throw new Error('Failed to fetch leave balances');

                 const balances = await res.json();
                 // find balance for selected leave type
            const selectedBalance = balances.find(b => b.leaveType.leavetypeId == leaveTypeId);

            if (selectedBalance) {
                balanceInput.value = selectedBalance.balance.toFixed(2);
                
            } else {
                balanceInput.value = 0;
            }
            } catch (error) {
                console.error(err);
            balanceInput.value = 0;
            }
        });
    }

    //saving cc modal
    if(saveCcBtn) {
         saveCcBtn.addEventListener('click', () => {
              ccEmployeeIds = [];
              ccEmployeeNames = [];

              //get all checked inputs in the modal
              if(ccModalBody) {
                 ccModalBody.querySelectorAll('input[type="checkbox"]').forEach(chk => {
                     if(chk.checked) {
                        const empId = chk.value;
                        const empName = chk.dataset.name || chk.dataset.email || chk.nextSibling?.textContent?.trim() || empId;
                        ccEmployeeIds.push(parseInt(empId));
                        ccEmployeeNames.push(empName);
                    }
                 });
              }
              ccToInput.value = ccEmployeeNames.join(', ');
              //close modal
              const bsModal = bootstrap.Modal.getInstance(ccModal) || new bootstrap.Modal(ccModal);
              bsModal.hide();
         });
    }

    //submit form
     // Submit form
if (applyLeaveForm) {
    applyLeaveForm.addEventListener('submit', async (e) => {
        e.preventDefault();

        // build DTO
        const dto = {
            employeeId: parseInt(getEmpId()),
            leaveTypeId: parseInt(leaveTypeSelect.value),
            sessionFrom: sessionFromInput.value,
            sessionTo: sessionToInput.value,
            fromDate: fromDateInput.value,
            toDate: toDateInput.value,
            contactDetails: document.getElementById('contact').value,
            applyToId: parseInt(applyToSelect.value),
            ccEmployeeIds: ccEmployeeIds,
            reason: document.getElementById('description').value
        };

        // Validation
        if (!dto.leaveTypeId) {
            Swal.fire('Validation', 'Please select leave type', 'warning');
            return;
        }
        if (!dto.fromDate || !dto.toDate) {
            Swal.fire('Validation', 'Please select From and To dates', 'warning');
            return;
        }
        if (!dto.applyToId) {
            Swal.fire('Validation', 'Apply To is required', 'warning');
            return;
        }

        const leaveQty = parseFloat(nodInput.value) || 0;
        const selectedLeaveTypeText = leaveTypeSelect.options[leaveTypeSelect.selectedIndex].text.trim();
        const balance = parseFloat(balanceInput.value) || 0;

        // Only block Paid Leave if insufficient balance
        if (selectedLeaveTypeText === "Paid Leave") {
            if (balance <= 0) {
                Swal.fire({
                    icon: 'warning',
                    title: 'Insufficient Paid Leave',
                    text: 'You do not have balance for Paid Leave (PPL). Please choose Loss of Pay (LOP) leave instead.',
                });
                return; // block submission
            }
            if (leaveQty > balance) {
                Swal.fire({
                    icon: 'warning',
                    title: 'Insufficient Paid Leave',
                    text: `You are trying to apply ${leaveQty} days but your Paid Leave balance is ${balance}.`,
                });
                return; // block submission
            }
        }

        // Build form data
        const formData = new FormData();
        formData.append('leaveDto', JSON.stringify(dto));
        const file = attachmentInput?.files?.[0];
        if (file) formData.append('file', file);

        const headers = getAuthHeaders(false);

        try {
            const res = await fetch("/api/leaves", {
                method: 'POST',
                headers,
                body: formData
            });

            if (!res.ok) {
                const errorData = await res.json();
                // throw new Error(text || `HTTP ${res.status}`);
                Swal.fire({
                     icon: 'warning',
                     title: 'Leave Application Failed',
                     text: errorData.message || 'Something went wrong',
                });
                return;
            }

            const saved = await res.json();
            Swal.fire({ icon: 'success', title: 'Applied', text: 'Leave applied successfully!', timer: 3000, showConfirmButton: true });

            // Reset form
            applyLeaveForm.reset();
            ccEmployeeIds = [];
            ccEmployeeNames = [];
            ccToInput.value = '';
            nodInput.value = '';
            balanceInput.value = '';

            populateApplyTo(currentEmployee);

        } catch (err) {
            console.error('Apply leave failed', err);
            Swal.fire('Error', err.message || 'Failed to apply leave', 'error');
        }
    });
}


        //helper function
        //load details of current employee(to know wbout confirm/probation/ & reporting manager)

        async function loadEmployeeDetails(empId) {
            console.log("Fetching employee details for empId:", empId); //consoling debug 
            const res = await fetch(`/api/employees/${empId}`, {
                headers: getAuthHeaders()
            });

            if(!res.ok) {
                throw new Error('Failed to fetch employee details');
            }
            const emp = await res.json();
            currentEmployee = emp;

            populateApplyTo(emp);
        }

        // function populateApplyTo(emp) {
        //      applyToSelect.innerHTML = '<option value="" disabled selected>-Select-</option>';

        //      if(emp.reportingManager1Id && emp.reportingManager1Name) {
        //           applyToSelect.innerHTML += `
        //            <option value="${emp.reportingManager1Id}">${emp.reportingManager1Name}</option>
        //           `;
        //      }
        // }

        function populateApplyTo(emp) {
            applyToSelect.innerHTML = '<option value="" disabled selected>-Select-</option>';
        
            if(emp.reportingManager1Id && emp.reportingManager1Name) {
                // Instead of reportingManager1Id (rm_id), use reportingManager1EmployeeId
                applyToSelect.innerHTML += `
                <option value="${emp.reportingManager1Id}">${emp.reportingManager1Name}</option>
                `;
            }
        }
        

        //load leave types and filtering for probation employees
        async function loadLeaveTypes() {
            const res = await fetch('/api/leavetypes', {
                headers: getAuthHeaders()
            });
            if(!res.ok) {
                throw new Error("Failed to fetch leave types");
            }
            const types = await res.json();

            //will detect if probation/confirm
            const isProbation = currentEmployee && (String(currentEmployee.employee_status || '').toLowerCase().includes('provation'));
        
            // const allowedIfProbation = ['Loss of Pay/Leave without Pay/Unpaid Leave', 'Mark Attendance'];

            leaveTypeSelect.innerHTML = '<option value="" disabled selected>-Select Type-</option>';

            types.forEach(t => {
                const label = t.leaveType || '';
                const id = t.leavetypeId; 
                

                if(isProbation) {
                    if(!t.allowedForProbation) return;
                }
                const opt = document.createElement('option');
                opt.value = id;
                opt.textContent = label;
                leaveTypeSelect.appendChild(opt);
            });
        }

        // Load all employees into CC modal (table body)
        async function loadEmployeesForCc() {
            if (!ccModalBody) return;
            ccModalBody.innerHTML = ''; // clear

            const res = await fetch('/api/employees', { headers: getAuthHeaders() });
            if (!res.ok) {
                throw new Error('Failed to fetch employees for CC');
            }
            const list = await res.json();

            // Build table rows with checkbox
            list.forEach(emp => {
                const tr = document.createElement('tr');
                const tdName = document.createElement('td');
                const chk = document.createElement('input');
                chk.type = 'checkbox';
            
                // set value to employee id
                const id = emp.empId || emp.id || emp.emp_id;
                chk.value = id;
            
                // include empCode in dataset for reference if needed
                const code = emp.empCode || emp.emp_code || '';
                chk.dataset.name = `${emp.first_name || ''} ${emp.last_name || ''} (${code})`.trim() || emp.email || id;
            
                tdName.appendChild(chk);
            
                // show name + empCode next to checkbox
                const span = document.createTextNode(
                    ' ' + ((emp.first_name || '') + (emp.last_name ? ' ' + emp.last_name : '') + ` (${code})`).trim()
                );
                tdName.appendChild(span);
            
                const tdEmail = document.createElement('td');
                tdEmail.textContent = emp.email || emp.personal_email || '';
            
                tr.appendChild(tdName);
                tr.appendChild(tdEmail);
                ccModalBody.appendChild(tr);
            });
            
        }

        //helper function
        function mapSession(sessionStr) {
                if(!sessionStr) return 1;

                switch(sessionStr.toLowerCase()) {
                    case 'one': return 1;
                    case 'two': return 2;
                    case 'three': return 3;
                    case 'four': return 4;
                    default: return 1;
                }
        } 

        //function to display ApplyTo dropdown dynamically
         async function getMonthlyLeaveTotal(empId, month, year) {
               const res = await fetch(`/api/leaves/employees/${empId}/monthlyLeave?month=${month}&year=${year}`, {
                           headers: getAuthHeaders()  
               });

               if(!res.ok) throw new Error('Failed to fetch monthly leave');
               const data = await res.json();
               return data.totalLeave || 0;
         }

        // compute number of days inclusive (simple)
        function computeNOD() {
            const f = fromDateInput.value;
            const t = toDateInput.value;
            const fromSession = mapSession(sessionFromInput.value);
            const toSession = mapSession(sessionToInput.value);
        
            if (!f || !t || isNaN(fromSession) || isNaN(toSession)) {
                nodInput.value = '';
                return;
            }
        
            const d1 = new Date(f);
            const d2 = new Date(t);
        
            if (isNaN(d1.getTime()) || isNaN(d2.getTime())) {
                nodInput.value = '';
                return;
            }
            if (d2 < d1) {
                Swal.fire('Validation', '"To" date must be same or after "From" date', 'warning');
                nodInput.value = '';
                return;
            }
        
            const daysBetween = Math.floor((d2 - d1) / (1000 * 60 * 60 * 24));
        
            let total = 0;
        
            if (daysBetween === 0) {
                // same day leave
                total = (toSession - fromSession + 1) * 0.25;
            } else {
                // first day: fromSession → 4
                total += (4 - fromSession + 1) * 0.25;
        
                // last day: 1 → toSession
                total += toSession * 0.25;
        
                // full middle days
                if (daysBetween > 1) {
                    total += (daysBetween - 1);
                }
            }
        
            nodInput.value = total.toFixed(2);

            //auto set Apply To..
            async function autoSetApplyTo(total) {
                  
                const empId = getEmpId();
                if(!empId) return;
    
                const fromDate = new Date(fromDateInput.value);
                const month = fromDate.getMonth() + 1;
                const year = fromDate.getFullYear();
    
                try {
                     
                    const monthlyTotal = await getMonthlyLeaveTotal(empId, month, year);
                    const totalAfterThis = monthlyTotal + parseFloat(total.toFixed(2));
    
                    //decide RM
                    let applyToId = null;
                    let applyToName = '';
    
                    if(totalAfterThis <= 1.5) {
                         //RM
                         applyToId = currentEmployee.reportingManager1Id;
                         applyToName = currentEmployee.reportingManager1Name;
                    } else {
                         //admin
                         applyToId = 1;
                         applyToName = "Pushpendra D";
                    }
    
                    //update dropdown
                    if(applyToSelect) {
                          applyToSelect.innerHTML = `<option value="${applyToId}" selected>${applyToName}</option>`;
                    }
    
    
                } catch (error) {
                    console.error('Failed to fetch monthly leave', err);
                }
            }

           autoSetApplyTo(total);
        }
        
        
    });
