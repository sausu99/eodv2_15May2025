<?php
include("includes/header.php");
include("includes/session_check.php");
require("includes/function.php");
require("language/language.php");

if (isset($_SESSION['user_id'])) {
    $user_id = sanitize($_SESSION['user_id']);
    
    $qry = "SELECT * FROM tbl_users WHERE id='" . $user_id . "'";
    $result = mysqli_query($mysqli, $qry);
    $user = mysqli_fetch_assoc($result);
    
    $referral_code = $user['code'];
}
?>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Refer and Earn</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
    <style>
        .referral-container {
            max-width: 600px;
            margin: 0 auto;
            padding: 40px;
            background-color: #f5f5f5;
            border-radius: 15px;
            box-shadow: 0px 10px 30px rgba(0, 0, 0, 0.1);
            text-align: center;
            font-family: Arial, sans-serif;
        }

        .referral-header {
            font-size: 28px;
            font-weight: bold;
            color: #333;
            margin-bottom: 20px;
        }

        .referral-description {
            font-size: 16px;
            color: #666;
            margin-bottom: 30px;
        }

        .referral-code-box {
            background-color: #fff;
            padding: 20px;
            border: 2px dashed #007bff;
            font-size: 24px;
            font-weight: bold;
            color: #007bff;
            border-radius: 10px;
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 30px;
        }

        .copy-btn {
            background-color: #007bff;
            color: #fff;
            border: none;
            padding: 10px 20px;
            border-radius: 5px;
            cursor: pointer;
            font-size: 14px;
        }

        .refer-button {
            background-color: #28a745;
            color: #fff;
            padding: 15px 30px;
            font-size: 18px;
            font-weight: bold;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            transition: background-color 0.3s;
        }

        .refer-button:hover {
            background-color: #218838;
        }

        .referral-footer {
            margin-top: 40px;
            font-size: 14px;
            color: #777;
        }
    </style>
</head>
<body>

<div class="referral-container">
    <div class="referral-header"><?php echo $client_lang['refer']; ?></div>
    <div class="referral-description"><?php echo $client_lang['refer_description']; ?></div>

    <div class="referral-code-box">
        <span id="referral-code"><?php echo $referral_code; ?></span>
        <button class="copy-btn" onclick="copyReferralCode()"><?php echo $client_lang['copy_code']; ?></button>
    </div>

    <button class="refer-button" onclick="referNow()"><?php echo $client_lang['refer_now']; ?></button>

    <div class="referral-footer">
        <?php echo $client_lang['refer_footer']; ?>
    </div>
</div>

<script>
    function copyReferralCode() {
        var referralCode = document.getElementById('referral-code').textContent;
        var tempInput = document.createElement('input');
        tempInput.value = referralCode;
        document.body.appendChild(tempInput);
        tempInput.select();
        document.execCommand('copy');
        document.body.removeChild(tempInput);
        alert('<?php echo $client_lang['refer_copied']; ?>: ' + referralCode);
    }

    function referNow() {
        var referralCode = document.getElementById('referral-code').textContent;
        var shareUrl = "";  // Optionally, add the URL you want to share
        if (navigator.share) {
            navigator.share({
                title: '<?php echo $client_lang['refer_text1']; ?>',
                text: '<?php echo $client_lang['refer_text2']; ?> ' + referralCode + ' <?php echo $client_lang['refer_text3']; ?>',
                url: shareUrl,
            }).catch((error) => console.log('Error sharing:', error));
        } else {
            alert('Share functionality not supported on this browser. Copy your referral code and share it manually.');
        }
    }
</script>

</body>
</html>
