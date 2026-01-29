// doctors.js - Khởi tạo DataTables cho bảng bác sĩ
$(document).ready(function() {
    $('#doctorsTable').DataTable({
        "language": {
            "url": "//cdn.datatables.net/plug-ins/1.13.6/i18n/vi.json"
        },
        "order": [[0, "desc"]],  // Sort theo Mã BS giảm dần
        "pageLength": 10,
        responsive: true
    });
});