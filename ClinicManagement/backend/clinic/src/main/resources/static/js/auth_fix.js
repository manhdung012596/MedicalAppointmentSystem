document.addEventListener("DOMContentLoaded", function () {
    console.log("Auth Fix Script Loaded");

    // --- FUNCTION TO UPDATE UI ---
    function updateUI() {
        const userJson = localStorage.getItem("user");
        const userSection = document.getElementById("user-section");

        if (userJson && userSection) {
            const user = JSON.parse(userJson);
            const name = user.name || user.fullName || "Khách";

            // Keep only the hotline (first child) and add user info
            const hotline = userSection.firstElementChild.outerHTML;

            userSection.innerHTML = `
                ${hotline}
                <div class="dropdown">
                    <button class="btn btn-outline-primary dropdown-toggle" type="button" id="userDropdown" data-bs-toggle="dropdown" aria-expanded="false">
                        Xin chào, <b>${name}</b>
                    </button>
                    <ul class="dropdown-menu" aria-labelledby="userDropdown">
                        <li><a class="dropdown-item" href="#" id="logoutBtn">Đăng xuất</a></li>
                    </ul>
                </div>
            `;

            // Add Logout Event Listener
            document.getElementById("logoutBtn").addEventListener("click", function (e) {
                e.preventDefault();
                localStorage.removeItem("user");
                Swal.fire({
                    icon: 'success',
                    title: 'Đăng xuất thành công',
                    timer: 1500,
                    showConfirmButton: false
                }).then(() => {
                    window.location.reload();
                });
            });
        }
    }

    // Call updateUI on load
    updateUI();

    // --- HANDLE REGISTRATION ---
    const registerForm = document.getElementById("registerForm");
    if (registerForm) {
        registerForm.addEventListener("submit", function (e) {
            e.preventDefault(); // Stop default form submission

            const name = document.getElementById("registerName").value;
            const email = document.getElementById("registerEmail").value;
            const password = document.getElementById("registerPassword").value;
            const confirmPassword = document.getElementById("registerConfirmPassword").value;

            // Simple validation
            if (password !== confirmPassword) {
                Swal.fire({
                    icon: 'error',
                    title: 'Lỗi',
                    text: 'Mật khẩu xác nhận không khớp!'
                });
                return;
            }

            const data = {
                name: name,
                email: email,
                password: password,
                role: "PATIENT" // Default role
                // Add phone/address if fields exist, or leave null
            };

            fetch("/api/auth/register", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(data)
            })
                .then(async response => {
                    const text = await response.text();
                    if (response.ok) {
                        Swal.fire({
                            icon: 'success',
                            title: 'Thành công',
                            text: 'Đăng ký tài khoản thành công! Vui lòng đăng nhập.'
                        }).then(() => {
                            // Close register modal
                            const modalEl = document.getElementById('registerModal');
                            const modal = bootstrap.Modal.getInstance(modalEl);
                            if (modal) modal.hide();

                            // Open login modal
                            const loginModalEl = document.getElementById('loginModal');
                            const loginModal = new bootstrap.Modal(loginModalEl);
                            loginModal.show();
                        });
                    } else {
                        throw new Error(text || "Đăng ký thất bại");
                    }
                })
                .catch(error => {
                    console.error("Register Error:", error);
                    Swal.fire({
                        icon: 'error',
                        title: 'Lỗi',
                        text: error.message
                    });
                });
        });
    }

    // --- HANDLE LOGIN ---
    const loginForm = document.getElementById("loginForm");
    if (loginForm) {
        loginForm.addEventListener("submit", function (e) {
            e.preventDefault(); // Stop default form submission

            const email = document.getElementById("loginEmail").value;
            const password = document.getElementById("loginPassword").value;

            const data = {
                email: email,
                password: password
            };

            fetch("/api/auth/login", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(data)
            })
                .then(async response => {
                    if (response.ok) {
                        const userData = await response.json();

                        // Save user to localStorage
                        localStorage.setItem("user", JSON.stringify(userData));

                        Swal.fire({
                            icon: 'success',
                            title: 'Xin chào ' + (userData.name || userData.fullName || 'Bạn'),
                            text: 'Đăng nhập thành công!'
                        }).then(() => {
                            // Redirect based on role
                            if (userData.role === 'ADMIN') {
                                setTimeout(() => {
                                    window.location.href = '/admin/dashboard';
                                }, 1500);
                            } else if (userData.role === 'DOCTOR') {
                                setTimeout(() => {
                                    window.location.href = '/doctor/dashboard'; // Adjust URL if needed
                                }, 1500);
                            } else {
                                setTimeout(() => {
                                    window.location.reload(); // Reload page for patients/others
                                }, 1500);
                            }
                        });
                    } else {
                        throw new Error("Email hoặc mật khẩu không đúng!");
                    }
                })
                .catch(error => {
                    console.error("Login Error:", error);
                    Swal.fire({
                        icon: 'error',
                        title: 'Đăng nhập thất bại',
                        text: error.message
                    });
                });
        });
    }
});
