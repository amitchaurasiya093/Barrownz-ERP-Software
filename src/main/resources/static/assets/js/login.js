const API = 'http://localhost:8080/api';

document.getElementById('loginForm').addEventListener('submit', async function (e) {
      e.preventDefault();

     const empCode = document.getElementById('employeeCode').value;
       const password = document.getElementById('exampleInputPassword1').value;

            const response = await fetch(`${API}/auth/login`, {
                  method: 'POST',
                  headers: {
                         'Content-Type': 'application/json'
                  },
                  body: JSON.stringify({ empCode, password })
             });

             if(response.ok) {
                const result = await response.json();
                //
                console.log("Full login response: ", result);
                const token = result.token;
                const role = result.role;
               const empCode = result.empCode;
               const empId = result.empId;

                //stored token in localStorage
                localStorage.setItem('token', token);
                localStorage.setItem('role', role);
                localStorage.setItem('empCode', empCode);
                localStorage.setItem('empId', empId);
                console.log("Stored empId:", empId);


                if(role === 'Admin') {
                      window.location.href = '/dashboard/admin-panel.html';
                } else if(role === 'HR') {
                    window.location.href = '/dashboard/index.html';
                } else if(role === 'Employee') {
                    window.location.href = '/dashboard/employee-panel.html';
                } else {
                    alert('Unknown role, contact Admin.');
                }
                
                // window.location.href = '/index';
             } else {
                alert('Invalid credentials');
             }
});