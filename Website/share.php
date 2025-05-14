<?php
// Include database connection or any required files
include("includes/connection.php");

// Start the session to make session variables accessible
session_start();

if (!isset($_SESSION['user_email'])) {
    $share_id_login = filter_input(INPUT_GET, 'share_id', FILTER_SANITIZE_STRING);
    header("Location: login.php?share_id=$share_id_login");
    exit;
}

// Check if share_id is provided in the URL
if(isset($_GET['share_id'])) {
    
    // Store the share_id in the session to retrieve it after login
    $_SESSION['share_id'] = $share_id;
    
    // Sanitize the input to prevent SQL injection
    $share_id = mysqli_real_escape_string($mysqli, $_GET['share_id']);

    // Query to search for the share_id in tbl_offers
    $query = "SELECT * FROM tbl_offers WHERE o_id = '$share_id'";
    $result = mysqli_query($mysqli, $query);

    // Check if the offer with the provided share_id exists
    if(mysqli_num_rows($result) > 0) {
        $row_item = mysqli_fetch_assoc($result);
        
        // Determine the redirect URL based on o_type value and embed the game_id parameter
        switch ($row_item['o_type']) {
            case 1:
            case 2:
            case 7:
            case 8:
            case 10:
            case 11:
                $redirect_url = 'auction/shared/' . $share_id;
                break;
            case 4:
            case 5:
                $redirect_url = 'lottery/shared/' . $share_id;
                break;
            default:
                $redirect_url = 'live.php';
        }

        // Redirect the user to the determined URL
        header("Location: $redirect_url");
        exit();
    } else {
        // Offer not found, handle error or redirect to a default page
        echo "The Shared item was not found.";
    }
} else {
    // Redirect if share_id is not provided
    header("Location: index.php"); // Redirect to homepage or another appropriate page
    exit();
}
?>
