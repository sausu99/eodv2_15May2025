<?php
session_start();

// Unset and destroy all session variables
unset($_SESSION["user_id"]);
unset($_SESSION["user_email"]);
session_destroy();

// Clear remember_me_token cookie
if (isset($_COOKIE['remember_me_token'])) {
    unset($_COOKIE['remember_me_token']);
    setcookie('remember_me_token', '', time() - 3600, '/'); // set to expire in the past (delete cookie)
}

// Redirect to index.php or any desired page
echo "<script language=javascript>location.href='index.php';</script>";
?>
