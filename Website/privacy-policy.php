<?php 
include("includes/header.php");
require("includes/function.php");
require("language/language.php");

$qry="SELECT app_privacy_policy FROM tbl_settings where id='1'";
$result=mysqli_query($mysqli,$qry);
$settings_row=mysqli_fetch_assoc($result);
?>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><?php echo $client_lang['privacy_policy']; ?></title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/intl-tel-input@23.1.0/build/css/intlTelInput.css">
</head>
<body>
<?php echo $settings_row['app_privacy_policy'];?>
</body>
</html>
