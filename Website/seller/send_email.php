<?php
include("includes/connection.php");
  

// Function to send email notification
function sendEmailNotification($userId, $game_name, $new_status, $mysqli) {
    // Fetch user email
    $user_email_query = "SELECT name,email FROM tbl_users WHERE id = $userId";
    $user_email_result = mysqli_query($mysqli, $user_email_query);
    $user_email_row = mysqli_fetch_assoc($user_email_result);
    $user_email = $user_email_row['email'];
    $user_name = $user_email_row['name'];
    
    $query = "SELECT currency, admin_email FROM tbl_settings";
    $result = mysqli_query($mysqli, $query);
    $row = mysqli_fetch_assoc($result);
    $currency = $row['currency'];
    $admin_email = $row['admin_email'];

    // Email content
    $to = $user_email;
    $subject = "Bid Status Update for $game_name";
    $message = "Hi $user_name, Your bid for the Auction: $game_name is $new_status.";
    $headers = "From: $admin_email"; // Update with your email

    // Send email
    mail($to, $subject, $message, $headers);

    // Insert data into tbl_notification
    insertNotification($userId, $subject, $message, $mysqli);
}

// Function to insert data into tbl_notification
function insertNotification($userId, $subject, $message, $mysqli) {
$notification_query = "INSERT INTO tbl_notifications (u_id, tittle, body, action, status, time) 
                      VALUES ($userId, '$subject', '$message', 0, 2, NOW())";
                      
                      mysqli_query($mysqli, $notification_query);
}


?>
