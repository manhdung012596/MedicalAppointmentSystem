// dashboard.js - Logic chart cho trang Tổng Quan

document.addEventListener('DOMContentLoaded', function () {
    // Pie Chart - Trạng thái lịch hẹn
    const statusCtx = document.getElementById('statusChart').getContext('2d');
    new Chart(statusCtx, {
        type: 'pie',
        data: {
            labels: ['Chờ xác nhận', 'Đã xác nhận', 'Hoàn thành', 'Đã hủy'],
            datasets: [{
                data: [40, 30, 20, 10], // Sau này thay bằng dữ liệu từ backend
                backgroundColor: ['#ffc107', '#28a745', '#007bff', '#dc3545'],
                hoverOffset: 10
            }]
        },
        options: {
            responsive: true,
            plugins: {
                legend: { position: 'top' },
                tooltip: { enabled: true }
            }
        }
    });

    // Doughnut Chart - Giới tính bệnh nhân
    const genderCtx = document.getElementById('genderChart').getContext('2d');
    new Chart(genderCtx, {
        type: 'doughnut',
        data: {
            labels: ['Nam', 'Nữ'],
            datasets: [{
                data: [60, 40], // Sau này thay bằng dữ liệu từ backend
                backgroundColor: ['#007bff', '#ff69b4'],
                hoverOffset: 10
            }]
        },
        options: {
            responsive: true,
            plugins: {
                legend: { position: 'top' }
            },
            cutout: '60%'  // Làm thành doughnut
        }
    });
});