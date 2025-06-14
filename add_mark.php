<?php
header('Content-Type: application/json');
header('Access-Control-Allow-Origin: *');
header('Access-Control-Allow-Methods: POST');
header('Access-Control-Allow-Headers: Content-Type');

// استلام البيانات
$data = json_decode(file_get_contents('php://input'), true);

// التحقق من وجود البيانات المطلوبة
if (!isset($data['student_id']) || !isset($data['subject_id']) || !isset($data['mark_type'])) {
    echo json_encode([
        'success' => false,
        'message' => 'Missing required fields'
    ]);
    exit;
}

// استخراج البيانات
$student_id = $data['student_id'];
$subject_id = $data['subject_id'];
$mark_type = $data['mark_type'];
$mark_value = isset($data['mark_value']) ? $data['mark_value'] : '0';

// الاتصال بقاعدة البيانات
$conn = new mysqli('localhost', 'root', '', 'school_db');

// التحقق من الاتصال
if ($conn->connect_error) {
    echo json_encode([
        'success' => false,
        'message' => 'Database connection failed: ' . $conn->connect_error
    ]);
    exit;
}

// التحقق من عدم وجود علامة من نفس النوع للطالب والمادة
$check_sql = "SELECT * FROM marks WHERE student_id = ? AND subject_id = ? AND mark_type = ?";
$check_stmt = $conn->prepare($check_sql);
$check_stmt->bind_param("iis", $student_id, $subject_id, $mark_type);
$check_stmt->execute();
$result = $check_stmt->get_result();

if ($result->num_rows > 0) {
    echo json_encode([
        'success' => false,
        'message' => 'Mark type already exists for this student and subject'
    ]);
    exit;
}

// إضافة العلامة
$sql = "INSERT INTO marks (student_id, subject_id, mark_type, mark_value) VALUES (?, ?, ?, ?)";
$stmt = $conn->prepare($sql);
$stmt->bind_param("iiss", $student_id, $subject_id, $mark_type, $mark_value);

if ($stmt->execute()) {
    echo json_encode([
        'success' => true,
        'message' => 'Mark added successfully',
        'mark_id' => $conn->insert_id
    ]);
} else {
    echo json_encode([
        'success' => false,
        'message' => 'Error adding mark: ' . $stmt->error
    ]);
}

$stmt->close();
$conn->close();
?> 