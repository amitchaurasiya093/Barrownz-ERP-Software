const refresh_Api = 'http://localhost:8080/api';

let inactivityTimer;
let refreshInterval;

//getting token expiry from jwt
function parseJwt(token) {
      try {
         return JSON.parse(atob(token.split('.')[1]));
      } catch (e) {
          return null;
      }
}

//reset inactivity timer
function resetInactivityTimer() {
    clearTimeout(inactivityTimer);

    //logout after 30 minutes of inactivity
    inactivityTimer = setTimeout(() => {
          logoutUser();
    }, 30 * 60 * 1000);
}

//refresh token 
async function refreshTokenIfNeeded() {
      const token = localStorage.getItem("token");
      if(!token) return;

      const payload = parseJwt(token);
      if(!payload || !payload.exp) return;

      const expiryTime = payload.exp * 1000;
      const now = Date.now();

      if (expiryTime - now < 5 * 60 * 1000) {
        const res = await fetch(`${refresh_Api}/auth/refresh`, {
          method: "POST",
          headers: { "Authorization": `Bearer ${token}` }
        });
    
        if (res.ok) {
          const result = await res.text(); // your refresh endpoint returns just string
          localStorage.setItem("token", result.token);
          localStorage.setItem("role", result.role);
          localStorage.setItem("empCode", result.empCode);
          localStorage.setItem("empId", result.empId);
          console.log("Token refreshed");
        } else {
          logoutUser();
        }
      }
    }

    //auto logout
    function logoutUser() {
          localStorage.clear();
          window.location.href = "/login.html";
    }

    //tracking useractivity

    ["click", "mousemove", "keypress", "scroll"].forEach(evt => {
          window.addEventListener(evt, resetInactivityTimer);
    });

    //start inactivity + refresh interval on page load
    function initSessionManager() {
          resetInactivityTimer();
          clearInterval(refreshInterval);
          refreshInterval = setInterval(refreshTokenIfNeeded, 60 * 1000) //checking every minute
    }

    initSessionManager();
