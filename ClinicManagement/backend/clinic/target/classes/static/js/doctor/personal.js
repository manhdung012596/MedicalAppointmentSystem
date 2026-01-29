// personal.js - Preview ảnh + show/hide password + loading khi submit

$(document).ready(function() {
    // Show/hide password
    const togglePassword = document.querySelector('#togglePassword');
    const password = document.querySelector('#newPassword');

    if (togglePassword && password) {
        togglePassword.addEventListener('click', function () {
            const type = password.getAttribute('type') === 'password' ? 'text' : 'password';
            password.setAttribute('type', type);
            this.querySelector('i').classList.toggle('fa-eye');
            this.querySelector('i').classList.toggle('fa-eye-slash');
        });
    }

    // Preview tên file khi chọn ảnh
    $('#avatarUpload').on('change', function(e) {
        const fileName = e.target.files[0]?.name || 'Chọn tệp';
        $('.custom-file-label').text(fileName);
    });

    // Disable button + loading khi submit
    $('form').on('submit', function() {
        $('#updateBtn').prop('disabled', true)
                       .html('<span class="spinner-border spinner-border-sm mr-2"></span> Đang cập nhật...');
    });
});