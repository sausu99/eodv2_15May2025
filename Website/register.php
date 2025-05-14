<?php
// Include necessary files and start the session
include ("includes/connection.php");
include ("language/language.php");
session_start();

// Check if the user is already logged in
if (isset($_SESSION['user_email']) || isset($_SESSION['user_id'])) {
    header("Location: home.php");
    exit;
}

// Handle form submission
if ($_SERVER["REQUEST_METHOD"] == "POST") {
    // Get user input and perform basic validation (you should perform more extensive validation)
    $name = $_POST["name"];
    $email = $_POST["email"];
    $password = $_POST["password"];
    $phone = $_POST["phone"];
    $countryCode = $_POST["country_code"];

    // Perform registration logic, e.g., insert user data into the database
    // Ensure you hash the password securely before saving it in the database

    // Redirect the user to the login page after successful registration
    header("Location: login.php");
    exit;
}
?>

<!DOCTYPE html>
<html lang="en">

<head>
    <base href="/">
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="author" content="">
    <meta name="description" content="">
    <title><?php echo APP_NAME; ?> - Register</title>
    <link rel="icon" href="../images/profile.png" type="image/x-icon">

    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css"
        integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">

    <!-- Font Awesome CSS -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.1/css/all.min.css"
        integrity="sha384-GLhlTQ8iNl4Dk4Lr6ZYyFiS+9MAn1LQJL4A6U7Fm/6vO5eEL/JZ+gW+Xn2mi1f" crossorigin="anonymous">

    <!-- Your Custom CSS -->
    <link rel="stylesheet" type="text/css" href="assets/css/vendor.css">
    <link rel="stylesheet" type="text/css" href="assets/css/flat-admin.css">

    <!-- Theme -->
    <link rel="stylesheet" type="text/css" href="assets/css/theme/blue-sky.css">
    <link rel="stylesheet" href="assets/css/intlTelInput.css">

<script src="assets/js/jquery-3.4.1.min.js"></script>
<script src="assets/js/intlTelInput.min.js"></script>
    <style>
  .close-button-user {
    display: none;
  }
</style>
</head>

<body>
    <?php include("register_data.php"); ?>
</body>
</html>