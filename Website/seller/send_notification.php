<?php
// Include necessary files and establish database connection
include("includes/connection.php");
include("push_notification.php"); // Include the file containing the function

// Provide test data
$userId = $_GET['userId'];
$title = $_GET['title'];
$body = $_GET['body'];
$action = $_GET['action'];

// Call the function
$result = sendFCMNotification($userId, $title, $body, $action, $mysqli);

// Check the result
if ($result !== false) {
    echo "Notification sent successfully.";
} else {
    echo "Failed to send notification. Check error logs for details.";
}
   
?>
