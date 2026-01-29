// appointment-schedule.js - DataTables + AJAX cập nhật trạng thái lịch hẹn

$(document).ready(function() {
    // Khởi tạo DataTables cho bảng
    $('#appointmentsTable').DataTable({
        "language": {
            "url": "//cdn.datatables.net/plug-ins/1.13.6/i18n/vi.json"
        },
        "order": [[2, "desc"]],  // Sort theo Ngày giờ giảm dần
        "pageLength": 10,
        responsive: true
    });

    // Xử lý thay đổi trạng thái bằng AJAX
    $('.status-select').on('change', function() {
        const appointmentId = $(this).data('id');
        const newStatus = $(this).val();
        const statusSpan = $('#status-' + appointmentId);

        // Hiển thị loading
        statusSpan.html('<span class="spinner-border spinner-border-sm"></span> Đang cập nhật...');

        $.ajax({
            url: '/doctor/appointments/' + appointmentId + '/update-status',
            type: 'POST',
            data: { status: newStatus },
            headers: {
                "Content-Type": "application/x-www-form-urlencoded",
                "X-CSRF-TOKEN": $("meta[name='_csrf']").attr("content")  // Nếu dùng CSRF
            },
            success: function(response) {
                // Cập nhật badge trạng thái mới
                let badgeClass = '';
                let text = '';
                switch (newStatus) {
                    case 'PENDING':
                        badgeClass = 'badge bg-warning text-dark';
                        text = 'Chờ xác nhận';
                        break;
                    case 'CONFIRMED':
                        badgeClass = 'badge bg-success';
                        text = 'Đã xác nhận';
                        break;
                    case 'COMPLETED':
                        badgeClass = 'badge bg-info';
                        text = 'Hoàn thành';
                        break;
                    case 'CANCELLED':
                        badgeClass = 'badge bg-danger';
                        text = 'Đã hủy';
                        break;
                }
                statusSpan.removeClass().addClass(badgeClass).text(text);
                alert('Cập nhật trạng thái thành công!');
            },
            error: function(xhr) {
                alert('Lỗi: ' + xhr.status + ' - ' + (xhr.responseText || 'Không thể cập nhật'));
                // Reload để khôi phục trạng thái cũ nếu lỗi
                location.reload();
            }
        });
    });
});