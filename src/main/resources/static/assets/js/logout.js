console.log("hello");

const logout_Api = 'http://localhost:8080/api';

document.addEventListener('DOMContentLoaded', function () {
         const logoutBtn =  document.getElementById('logoutBtn');

         if(logoutBtn) {
            logoutBtn.addEventListener('click', async function () {
                  const token = localStorage.getItem("token");

                  if(!token) {
                      window.location.href = "/show-login";
                      return;
                  }

                  try {

                    await fetch(`${logout_Api}/auth/logout`, {
                        method: "POST",
                        headers: {
                            "Authorization": `Bearer ${token}`
                        }
                    });
                  }
                  catch (error) {
                      console.error("Logout request failed: ", error);
                  }
                   finally{
                    localStorage.clear();
                    sessionStorage.clear();

                    window.location.href = "/show-login";
                   }
            });
         }
});