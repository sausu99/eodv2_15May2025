<?php

use PHPMailer\PHPMailer\PHPMailer;

use PHPMailer\PHPMailer\Exception;



require 'vendor/autoload.php';

include ("includes/connection.php");

session_start();



function SendEmailVerification($host, $username, $password, $port, $sEmail, $sName, $rEmail, $rName)

{

    $mail = new PHPMailer(true);



    try {

        $mail->SMTPDebug = 0;

        $mail->isSMTP();

        $mail->Host = $host;

        $mail->SMTPAuth = true;

        $mail->Username = $username;

        $mail->Password = $password;

        $mail->SMTPSecure = 'tls';

        $mail->Port = $port;



        $mail->setFrom($sEmail, $sName);

        $mail->addAddress($rEmail, $rName);



        $mail->isHTML(true);

        $mail->Subject = 'Verification Code';



        $verification_code = rand(100000, 999999);



        $_SESSION["verification_code"] = $verification_code;



        $mailContent = '

        <html>

        <head>

            <title>Verification Code</title>

            <style>

                body {

                    font-family: Arial, sans-serif;

                    background-color: #f4f4f4;

                    margin: 0;

                    padding: 0;

                }

                .container {

                    max-width: 600px;

                    margin: 0 auto;

                    background-color: #ffffff;

                    padding: 20px;

                    box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);

                }

                .header {

                    text-align: center;

                    padding: 10px 0;

                    border-bottom: 1px solid #dddddd;

                }

                .content {

                    margin: 20px 0;

                    text-align: center;

                }

                .footer {

                    text-align: center;

                    padding: 10px 0;

                    border-top: 1px solid #dddddd;

                    font-size: 12px;

                    color: #aaaaaa;

                }

            </style>

        </head>

        <body>

            <div class="container">

                <div class="header">

                    <h1>Email Verification</h1>

                </div>

                <div class="content">

                    <p>Thank you for registering with us.</p>

                    <p>Your verification code is:</p>

                    <h2>' . $verification_code . '</h2>

                    <p>Please enter this code in the application to verify your email address.</p>

                </div>

                <div class="footer">

                    <p>&copy; ' . date("Y") . ' Your Company. All rights reserved.</p>

                </div>

            </div>

        </body>

        </html>';



        $mail->Body = $mailContent;



        $mail->send();

        echo 'Verification email has been sent';

    } catch (Exception $e) {

        echo "Message could not be sent. Mailer Error: {$mail->ErrorInfo}";

    }

}



if (isset($_SESSION["create-password"])) {

    $password = $_POST["password"];

    $email = $_SESSION['email'];

    $query = "UPDATE `tbl_users` SET `password` = ? WHERE `email` = ?";



    $stmt = $mysqli->prepare($query);

    $stmt->bind_param("ss", $password, $email);

    $stmt->execute();



    if ($stmt->affected_rows > 0) {

        $qry = "SELECT * FROM tbl_users WHERE (email='" . $email . "' OR phone='" . $email . "')";

        $result = mysqli_query($mysqli, $qry);

        $row = mysqli_fetch_assoc($result);

        $_SESSION['user_id'] = $row['id'];

        $_SESSION['user_email'] = $row['email'];



        header("Location: home.php");

        exit;

    }

    else

    {

        header("Location: forget-password.php");

    }

}

else if (isset($_SESSION["verification_code"])) {

    $verificationCode = $_SESSION["verification_code"];



    if ($verificationCode == $_POST["verification"]) {

        $_SESSION["create-password"] = "true";

        header("Location: forget-password.php");

        exit;

    } else {

        $_SESSION["msg"] = "998";

        header("Location: forget-password.php");

        exit;

    }



} else {

    $email = filter_input(INPUT_POST, 'email', FILTER_SANITIZE_STRING);

    $phone = filter_input(INPUT_POST, 'phone', FILTER_SANITIZE_STRING);

    $password = filter_input(INPUT_POST, 'password', FILTER_SANITIZE_STRING);

    $countryCode = filter_input(INPUT_POST, "countryCode", FILTER_SANITIZE_STRING);



    $phone = preg_replace('/\D/', '', $phone);

    $countryCode = preg_replace('/\D/', '', $countryCode);

    if (strpos($phone, $countryCode) === 0) {

        $phone = substr($phone, strlen($countryCode));

    }

    $phone = ltrim($phone, '0');

    $fullPhoneNumber = $countryCode . $phone;

    $fullPhoneNumber = "+" . $fullPhoneNumber;



    if (($email == "") && ($phone == "")) {

        $_SESSION['msg'] = "1";

        header("Location:forget-password.php");

        exit;

    } else {

        $qry = "SELECT * FROM tbl_users WHERE (email='" . $email . "' OR phone='" . $phone . "')";

        $result = mysqli_query($mysqli, $qry);



        if (mysqli_num_rows($result) > 0) {

            $row_user = mysqli_fetch_assoc($result);



            $_SESSION['email'] = $row_user['email'];

            $name = $row_user["name"];

                

            $query = "SELECT `host`, `username`, `password`, `port`, `email`, `name` FROM `tbl_email` WHERE `id` = 1";

            $result = mysqli_query($mysqli, $query);



            $row = mysqli_fetch_assoc($result);

            $_SESSION["verify-code"] = "true";

            SendEmailVerification($row["host"], $row["username"], $row["password"], $row["port"], $row["email"], $row["name"], $row_user["email"], $name);

            header("Location: forget-password.php");

            exit;

        }

        else {

            $_SESSION['msg'] = "1";

            header("Location: forget-password.php");

            exit;

        }

    }

}