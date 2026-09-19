// ---------------------------------------
//  ADMIN PAGE TOGGLE BUTTON JS START HERE
// ---------------------------------------

//  const toggleBtn = document.getElementById('toggleBtn');
//     const textOut = document.getElementById('textOut');
//     const textIn = document.getElementById('textIn');

//     let isIn = true;

//     toggleBtn.addEventListener('click', () => {
//       isIn = !isIn;

//       if (isIn) {
//         textOut.classList.remove('active');
//         textIn.classList.add('active');
//         toggleBtn.classList.add('in');
//       } else {
//         textIn.classList.remove('active');
//         textOut.classList.add('active');
//         toggleBtn.classList.remove('in');
//       }
//     });


document.addEventListener("DOMContentLoaded", function () {
  const toggleBtn = document.getElementById("toggleBtn");

  // Set default state as System In
  let isIn = true;
  toggleBtn.textContent = "System In";
  toggleBtn.classList.add("in");

  toggleBtn.addEventListener("click", function () {
    if (isIn) {
      toggleBtn.textContent = "System Out";
      toggleBtn.classList.remove("in");
      toggleBtn.classList.add("out");
    } else {
      toggleBtn.textContent = "System In";
      toggleBtn.classList.remove("out");
      toggleBtn.classList.add("in");
    }
    isIn = !isIn;
  });
});


// -------------------------------------
//  ADMIN PAGE TOGGLE BUTTON JS END HERE
// -------------------------------------


const cursorDot = document.querySelector('.cursor-dot');
const cursorOutline = document.querySelector('.cursor-outline');

let mouseX = 0, mouseY = 0;
let outlineX = 0, outlineY = 0;

// Update mouse coordinates
document.addEventListener('mousemove', e => {
  mouseX = e.clientX;
  mouseY = e.clientY;
  cursorDot.style.left = `${mouseX}px`;
  cursorDot.style.top = `${mouseY}px`;
});

// Animate outer ring with delay
function animate() {
  outlineX += (mouseX - outlineX) / 8;
  outlineY += (mouseY - outlineY) / 8;

  cursorOutline.style.left = `${outlineX}px`;
  cursorOutline.style.top = `${outlineY}px`;

  requestAnimationFrame(animate);
}

animate();

// document.addEventListener("DOMContentLoaded", function () {

//        const currentPath =  window.location.pathname;

//      const links = document.querySelectorAll('#sidebar-user .nav-link, #sidebar-task .nav-link');

//      links.forEach(link => {
//          const href =  link.getAttribute('href');  
         
//          if(href && currentPath.includes(href)) {
//              link.classList.add('active');

//            const sidebarUser =  document.getElementById('sidebar-user');
//            const sidebarTask =  document.getElementById('sidebar-task');

//            if(sidebarUser && !sidebarUser.classList.contains('show')) {
//             sidebarUser.classList.add('show');
//            }

//            if(sidebarTask && !sidebarTask.classList.contains('show')) {
//             sidebarTask.classList.add('show');
//            }


//           const parentLi = sidebarUser.closest('.nav-item');
//                 const parentTaskLi =  sidebarTask.closest('.nav-item');
//             if(parentLi) {
//               parentLi.classList.add('active');
//             }
//             if(parentTaskLi) {
//               parentTaskLi.classList.add('active');
//             }
//          }
//      });
// }); 

//js for making sidebar links active
document.addEventListener("DOMContentLoaded", function () {
  const currentPath = window.location.pathname;

  const links = document.querySelectorAll('#sidebar-user .nav-link, #sidebar-task .nav-link');

  links.forEach(link => {
      const linkPath = new URL(link.href).pathname;

      if (currentPath === linkPath) {
          link.classList.add('active');

          // Find the immediate parent collapse (like sidebar-user or sidebar-task)
          const parentCollapse = link.closest('.collapse');
          if (parentCollapse && !parentCollapse.classList.contains('show')) {
              parentCollapse.classList.add('show');
          }

          // Highlight the main nav item (e.g., li.nav-item > a > ul)
          const parentNavItem = parentCollapse.closest('.nav-item');
          if (parentNavItem) {
              parentNavItem.classList.add('active');
          }
      }
  });
});

// IN OUT CAPTION JS START HERE
document.addEventListener("DOMContentLoaded", function () {
  const profileImg = document.getElementById("navbarEmpImage");
  const dropdownMenu = document.getElementById("customDropdown");

  profileImg.addEventListener("click", function (e) {
      e.preventDefault();
      dropdownMenu.classList.toggle("show");
  });

  // Click outside to close
  document.addEventListener("click", function (event) {
      if (!event.target.closest(".custom-drop")) {
          dropdownMenu.classList.remove("show");
      }
  });
});
// IN OUT CAPTION JS END HERE



