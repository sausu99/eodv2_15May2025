<?php
include ("includes/connection.php");
include ("language/language.php");
?>

<!DOCTYPE html>
<html>

<head>
  <meta name="author" content="">
  <meta name="description" content="">
  <meta http-equiv="Content-Type" content="text/html;charset=UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title><?php echo APP_NAME; ?> - Forget Password</title>
  <link rel="stylesheet" type="text/css" href="assets/css/vendor.css">
  <link rel="stylesheet" type="text/css" href="assets/css/flat-admin.css">
  <link rel="icon" href="/images/profile.png" type="image/x-icon">

  <!-- Theme -->
  <link rel="stylesheet" type="text/css" href="assets/css/theme/blue-sky.css">
  <link rel="stylesheet" type="text/css" href="assets/css/theme/blue.css">
  <link rel="stylesheet" type="text/css" href="assets/css/theme/red.css">
  <link rel="stylesheet" type="text/css" href="assets/css/theme/yellow.css">

  <link rel="stylesheet" href="assets/css/home.css">
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
  <?php include ("forget-password_data.php"); ?>
</body>

</html>