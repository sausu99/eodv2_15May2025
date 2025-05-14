<?php
include("includes/connection.php");


// Retrieve firebase token from tbl_settings
$settings_qry = "SELECT fcm_key FROM tbl_settings LIMIT 1";
$settings_result = mysqli_query($mysqli, $settings_qry);
$settings_row = mysqli_fetch_array($settings_result);
$fcm_keyy = $settings_row['fcm_key'];

// Define your API access key
define('API_ACCESS_KEY', $fcm_keyy);

// Define FCM endpoint URL
$fcmUrl = 'https://fcm.googleapis.com/fcm/send';

// Define headers for the HTTP request
$headers = array(
    'Authorization: key=' . API_ACCESS_KEY,
    'Content-Type: application/json'
);

// Function to send FCM notification
function sendFCMNotification($userId, $title, $body, $action, $mysqli) {
    // Retrieve FCM tokens for the user
    $tokens = array();
    $token_query = "SELECT fcm_token FROM tbl_users WHERE id = $userId";
    $token_result = mysqli_query($mysqli, $token_query);
    while ($row = mysqli_fetch_assoc($token_result)) {
        $tokens[] = $row['fcm_token'];
    }
    
    // Prepare FCM notification data
    $notification = [
        'sound' => 'default',
        'title' => $title,
        'body' => $body,
        '_displayInForeground'=> 'true',
        'channelId'=> 'default'
    ];

    $extraNotificationData = [
        'message' => $notification,
        'open_action' => $action,
        'fields' => '' // Adjust this according to your needs
    ];

    $fcmNotification = [
        'registration_ids' => $tokens,
        'data' => $extraNotificationData,
        'notification' => $notification
    ];

    // Send FCM notification
    $result = sendFCMNotificationToTokens($fcmNotification);
    
    // Insert notification data into tbl_notifications table
    $data = array(
        'u_id' => $userId,
        'tittle'  =>  $title,
        'body'  =>  $body,
        'action'  =>  $action,
        'status' => 1
    );
    $qry = Insert('tbl_notifications', $data);

    // Return result
    return $result;
}

// Function to send FCM notification
function sendFCMNotificationToTokens($fcmNotification) {
    global $fcmUrl, $headers;
    // Initialize curl
    $ch = curl_init();
    curl_setopt($ch, CURLOPT_URL, $fcmUrl);
    curl_setopt($ch, CURLOPT_POST, true);
    curl_setopt($ch, CURLOPT_HTTPHEADER, $headers);
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
    curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, false);
    curl_setopt($ch, CURLOPT_POSTFIELDS, json_encode($fcmNotification));

    // Execute curl and get result
    $result = curl_exec($ch);
    curl_close($ch);

    // Return result
    return $result;
}
?>