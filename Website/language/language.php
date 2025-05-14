<?php
include ('includes/connection.php');
// Start session to store language preference
session_start();

// Check if the user is logged in
if (isset($_SESSION['user_id'])) {
    // Fetch user data including language preference from the database
    $qry = "SELECT * FROM tbl_users WHERE id='" . $_SESSION['user_id'] . "'";
    $result = mysqli_query($mysqli, $qry);
    $session_row = mysqli_fetch_assoc($result);

    // Check if the user's language preference is set in the database
    if (isset($session_row['language']) && !empty($session_row['language'])) {
        // Include the language file based on the user's language preference from the database
        $current_script_path = __FILE__; // Get the full path of the current PHP script
        $language_file = dirname($current_script_path) . '/language_' . $session_row['language'] . '.php'; // Construct the full path to the language file


        // Debug statement
        if (file_exists($language_file)) {
            include_once ($language_file);
        } else {
            // Fallback to English or default language if the language file doesn't exist
            include_once ('language_en.php');
        }
    } else {
        // Fallback to English or default language if the user's language preference is not set in the database
        include_once ('language_en.php');
    }
} else {
    $language = "en";

    if (isset($_COOKIE["language"])) {
        $language = $_COOKIE["language"];
    }

    $current_script_path = __FILE__; // Get the full path of the current PHP script
    $language_file = dirname($current_script_path) . '/language_' . $language . '.php'; // Construct the full path to the language file


    // Debug statement
    if (file_exists($language_file)) {
        include_once ($language_file);
    } else {
        // Fallback to English or default language if the language file doesn't exist
        include_once ('language_en.php');
    }
}
?>