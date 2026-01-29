// Admin Layout JS - Sidebar Toggle

document.addEventListener('DOMContentLoaded', function () {
    // Sidebar toggle for both admin and doctor layout
    const sidebar = document.querySelector('.main-sidebar');
    const toggleBtn = document.querySelector('.sidebar-toggle, [data-widget="pushmenu"]');
    const content = document.querySelector('.admin-content, .content-wrapper');

    if (toggleBtn && sidebar) {
        toggleBtn.addEventListener('click', function (e) {
            e.preventDefault();
            sidebar.classList.toggle('closed');
            if (content) {
                if (sidebar.classList.contains('closed')) {
                    content.classList.add('sidebar-closed');
                } else {
                    content.classList.remove('sidebar-closed');
                }
            }
        });
    }

    // Highlight active menu item for both admin and doctor layout
    const menuItems = document.querySelectorAll('.main-sidebar ul.nav-sidebar li');
    menuItems.forEach(item => {
        item.addEventListener('click', function () {
            menuItems.forEach(i => i.classList.remove('active'));
            this.classList.add('active');
        });
    });
});


