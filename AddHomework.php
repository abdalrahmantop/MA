<?php
header('Content-Type: application/json');
header('Access-Control-Allow-Origin: *');
header('Access-Control-Allow-Methods: POST');
header('Access-Control-Allow-Headers: Content-Type');

// Enable error reporting for debugging
error_reporting(E_ALL);
ini_set('display_errors', 1);

// Log the received data
$raw_data = file_get_contents('php://input');
error_log("Received data: " . $raw_data);

// Get POST data
$data = $_POST;

// Log the POST data
error_log("POST data: " . print_r($data, true));

// Validate required fields
if (!isset($data['subject_id']) || !isset($data['title']) || !isset($data['description']) || !isset($data['due_date'])) {
    echo json_encode([
        'success' => false,
        'message' => 'Missing required fields',
        'received_data' => $data
    ]);
    exit;
}

// Extract data
$subject_id = $data['subject_id'];
$title = $data['title'];
$description = $data['description'];
$due_date = $data['due_date'];
$file_path = isset($data['file_path']) ? $data['file_path'] : '';

// Database connection
$conn = new mysqli('localhost', 'root', '', 'school_db');

// Check connection
if ($conn->connect_error) {
    echo json_encode([
        'success' => false,
        'message' => 'Database connection failed: ' . $conn->connect_error
    ]);
    exit;
}

// Prepare and execute the SQL query
$sql = "INSERT INTO homework (subject_id, title, description, due_date, file_path) VALUES (?, ?, ?, ?, ?)";
$stmt = $conn->prepare($sql);

if (!$stmt) {
    echo json_encode([
        'success' => false,
        'message' => 'Error preparing statement: ' . $conn->error
    ]);
    exit;
}

$stmt->bind_param("issss", $subject_id, $title, $description, $due_date, $file_path);

if ($stmt->execute()) {
    echo json_encode([
        'success' => true,
        'message' => 'Homework added successfully',
        'homework_id' => $conn->insert_id
    ]);
} else {
    echo json_encode([
        'success' => false,
        'message' => 'Error adding homework: ' . $stmt->error
    ]);
}

$stmt->close();
$conn->close();
?> 