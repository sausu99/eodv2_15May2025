<?php
session_start();
include("includes/connection.php");

$language = isset($_POST['language']) ? $_POST['language'] : '';

// Check if the user is logged in
if (isset($_SESSION['user_id'])) {
    // Update the language in the database for the current user
    $user_id = $_SESSION['user_id'];
    $update_query = "UPDATE tbl_users SET language = '$language' WHERE id = '$user_id'";
    mysqli_query($mysqli, $update_query);

    // Optionally, you can update the language in the session variable as well
}

$_SESSION['language'] = $language;
setcookie("language", $language, time() + (30 * 24 * 60 * 60), "/");

?>
