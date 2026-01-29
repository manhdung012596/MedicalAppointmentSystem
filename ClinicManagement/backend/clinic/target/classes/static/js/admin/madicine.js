// medicines.js - Khởi tạo DataTables cho bảng quản lý thuốc

$(document).ready(function() {
    $('#medicinesTable').DataTable({
        "language": {
            "url": "//cdn.datatables.net/plug-ins/1.13.6/i18n/vi.json"  // Tiếng Việt
        },
        "order": [[0, "desc"]],  // Sort theo Mã Thuốc giảm dần
        "pageLength": 10,
        responsive: true
    });
});