<?php 
include ("includes/connection.php");
include ("language/language.php");

$settings_query = "SELECT app_link FROM tbl_settings";
$settings_result = mysqli_query($mysqli, $settings_query);
$settings_row = mysqli_fetch_assoc($settings_result);
$app_link = $settings_row['app_link'];

?>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><?php echo $client_lang['app_download']; ?></title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f4f4f4;
            margin: 0;
            padding: 0;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
        }
        .container {
            background-color: #fff;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 0 15px rgba(0, 0, 0, 0.1);
            text-align: center;
        }
        h1 {
            color: #333;
            margin-bottom: 20px;
        }
        .download-buttons {
            display: flex;
            justify-content: space-around;
            margin-top: 20px;
        }
        .download-buttons a {
            text-decoration: none;
            display: inline-block;
            margin: 0 10px;
        }
        .download-buttons img {
            width: 150px;
            height: auto;
            transition: transform 0.3s ease;
        }
        .download-buttons img:hover {
            transform: scale(1.1);
        }
    </style>
</head>
<body>
    <div class="container">
        <h1><?php echo $client_lang['app_download']; ?></h1>
        <p><?php echo $client_lang['choose_platform']; ?></p>
        <div class="download-buttons">
            <a href="<?php echo $app_link ?>" download>
                <img src="images/static/androidlogo.png" alt="Download for Android">
            </a>
            <a href="ios_download.php">
                <img src="images/static/ioslogo.png" alt="Download for iOS">
            </a>
        </div>
    </div>
</body>
</html>
