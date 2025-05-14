<?php include ("includes/connection.php"); // Database Connection
// include("includes/session_check.php"); //Verify if user is logged in to prevent unathorised access
include ("language/language.php"); // Multiple Language Implementation

// Check if user is already logged in with persistent cookie
if (isset($_COOKIE['remember_me_token']) && !isset($_SESSION['user_id'])) {
    $token = $_COOKIE['remember_me_token'];

    // Use prepared statements to prevent SQL injection
    $qry = $mysqli->prepare("SELECT * FROM tbl_users WHERE remember_me_token = ?");
    $qry->bind_param("s", $token);
    $qry->execute();
    $result = $qry->get_result();

    if ($result->num_rows > 0) {
        $row = $result->fetch_assoc();

        // Set session variables
        $_SESSION['user_id'] = $row['id'];
        $_SESSION['user_email'] = $row['email'];
        
        header("Location: " . $_SERVER['PHP_SELF']);



        // No redirection, just set the session variables and stay on the same page
    }
}


//Get file name
$currentFile = $_SERVER["SCRIPT_NAME"];
$parts = Explode('/', $currentFile);
$currentFile = $parts[count($parts) - 1];

if (isset($_SESSION['user_id'])) {
    $qry = "select language, image, name, wallet from tbl_users where id='" . $_SESSION['user_id'] . "'";

    $result = mysqli_query($mysqli, $qry);
    $session_row = mysqli_fetch_assoc($result);
    $language = $session_row['language'];
    $wallet = $session_row['wallet'];
    $logged = true;
} else {
    $language = "en";

    if (isset($_COOKIE["language"])) {
        $language = $_COOKIE["language"];
    }

    $logged = false;
}

$email_query = "SELECT admin_email, demo_access, app_logo FROM tbl_settings";
$email_result = mysqli_query($mysqli, $email_query);
$email_info = mysqli_fetch_assoc($email_result);
$app_logo = $email_info['app_logo'];

// Define the language array
$languages = array(
    array('code' => 'en', 'name' => 'English (US)', 'flag' => 'flag_en.svg'),
    array('code' => 'ar', 'name' => 'Arabic (عربى)', 'flag' => 'flag_ar.svg'),
    array('code' => 'bn', 'name' => 'Bangla (বাংলা)', 'flag' => 'flag_bn.svg'),
    array('code' => 'zh-CN', 'name' => 'Chinese (简体中文)', 'flag' => 'flag_zh-CN.svg'),
    array('code' => 'ja', 'name' => 'Japanese (日本語)', 'flag' => 'flag_ja.svg'),
    array('code' => 'ru', 'name' => 'Russian (Русский)', 'flag' => 'flag_ru.svg'),
    array('code' => 'pt-PT', 'name' => 'Portuguese (Portugal)', 'flag' => 'flag_pt.svg'),
    array('code' => 'it', 'name' => 'Italian (Italiano)', 'flag' => 'flag_it.svg'),
    array('code' => 'ko', 'name' => 'Korean (한국어)', 'flag' => 'flag_ko.svg'),
    array('code' => 'tr', 'name' => 'Turkish (Türkçe)', 'flag' => 'flag_tr.svg'),
    array('code' => 'pl', 'name' => 'Polish (Polski)', 'flag' => 'flag_pl.svg'),
    array('code' => 'de', 'name' => 'German (Deutsch)', 'flag' => 'flag_de.svg'),
    array('code' => 'es', 'name' => 'Spanish (Español)', 'flag' => 'flag_es.svg'),
    array('code' => 'fr', 'name' => 'French (Français)', 'flag' => 'flag_fr.svg'),
    array('code' => 'gr', 'name' => 'Greek (Ελληνικά)', 'flag' => 'flag_gr.svg'),
    array('code' => 'id', 'name' => 'Indonesian', 'flag' => 'flag_id.svg'),
    array('code' => 'pt-BR', 'name' => 'Português (Br)', 'flag' => 'flag_pt-BR.svg')
);


?>
<!DOCTYPE html>
<html>

<head>
    <base href="/">
    <meta http-equiv="Content-Type" content="text/html;charset=UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <link rel="stylesheet" type="text/css" href="assets/css/vendor.css">
    <link rel="stylesheet" type="text/css" href="assets/css/flat-admin.css">
     <link rel="stylesheet" type="text/css" href="assets/css/header.css" />

    <!-- Theme -->
    <link rel="stylesheet" type="text/css" href="assets/css/theme/blue-sky.css">
    <link rel="stylesheet" type="text/css" href="assets/css/theme/blue.css">
    <link rel="stylesheet" type="text/css" href="assets/css/theme/red.css">
    <link rel="stylesheet" type="text/css" href="assets/css/theme/yellow.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css" />

    <link rel="icon" href="/images/<?php echo $app_logo ?>" type="image/x-icon">

    <!-- Icon -->
    <link rel='stylesheet'
        href='https://cdn-uicons.flaticon.com/2.2.0/uicons-regular-straight/css/uicons-regular-straight.css'>
    <link rel='stylesheet'
        href='https://cdn-uicons.flaticon.com/2.2.0/uicons-solid-straight/css/uicons-solid-straight.css'>
    <link rel='stylesheet' href='https://cdn-uicons.flaticon.com/2.2.0/uicons-bold-rounded/css/uicons-bold-rounded.css'>
    <link rel='stylesheet'
        href='https://cdn-uicons.flaticon.com/2.2.0/uicons-solid-rounded/css/uicons-solid-rounded.css'>
    <link rel='stylesheet'
        href='https://cdn-uicons.flaticon.com/2.2.0/uicons-regular-rounded/css/uicons-regular-rounded.css'>
        <link rel="stylesheet" href="assets/css/intlTelInput.css">
    <script src='https://kit.fontawesome.com/a076d05399.js' crossorigin='anonymous'></script>


    <script src="assets/ckeditor/ckeditor.js"></script>
    <script src="assets/js/jquery-3.4.1.min.js"></script>
    <script src="assets/js/intlTelInput.min.js"></script>
</head>

<body>
    <?php

function getBaseUrl() {
    $protocol = (!empty($_SERVER['HTTPS']) && $_SERVER['HTTPS'] !== 'off' || $_SERVER['SERVER_PORT'] == 443) ? "https://" : "http://";
    $host = $_SERVER['HTTP_HOST'];
    return $protocol . $host;
}
    include("login_data.php");
    include("register_data.php");
    include("forget-password_data.php");

    // if ($logged)
    // {
    echo '<div class="topbar">';
    echo '    <div><iframe id="languageIframe" src="' . getBaseUrl() . '/language_popup.php' .'"></iframe></div>';
    echo '    <div class="email-area">';
    echo '        <h6><span>' . $header_lang['supportEmail'] . ':</span> <a href="mailto:' . $email_info['admin_email'] . '">' . $email_info['admin_email'] . '</a></h6>';
    echo '    </div>';
    echo '    <div class="topbar-right">';
    echo '        <ul class="topbar-right-list">';
    echo '            <li>';
    echo '                <button id="languageSelectButton" class="dropdown-toggle">';
    echo '                    <span>' . $header_lang['language'] . ':&nbsp;</span>';
    foreach ($languages as $lang) {
        if ($language == $lang['code']) {
            echo '                    <span class="langName">' . $lang['name'] . '</span>';
            echo '                    <img class="flag-icon" src="/images/static/flags/' . $lang['flag'] . '" alt="' . $lang['name'] . '">';
        }
    }
    echo '                </button>';
    echo '            </li>';
    echo '        </ul>';
    echo '    </div>';
    echo '</div>';
    // }
    ?>

    <div class="app app-default">
        <aside class="app-sidebar" id="sidebar">

            <?php
            if ($logged) {
                
                
                echo '<div class="sidebar-header">';
                  //<!--removed .php-->
                echo '    <div><a class="sidebar-brand" href="home"><img src="/images/' . APP_LOGO . '" alt="app logo" /></a></div>';
                
                
                
                echo '<div class="header">';
                echo '    <div>';
                echo '        <span>';
                echo '            <a href="recharge" id="recharge">'.$wallet.' '.$client_lang['coin'].'&nbsp;<i class="fa fa-wallet"></i></a>';
                echo '        </span>';
                echo '    </div>';
                echo '</div>';
                
                
                
                
                echo '    <div>';
                echo '        <div>';
                echo '            <ul class="nav navbar-nav navbar-right">';
                echo '                <li class="dropdown profile">';
                 //<!--removed .php-->
                echo '                    <a href="profile" class="dropdown-toggle" data-toggle="dropdown">';
                if (!empty($session_row['image'])) {
                    echo '                        <img class="profile-img" src="../seller/images/' . $session_row['image'] . '">';
                } else {
                    echo '                        <img class="profile-img" src="assets/images/'.$app_logo.'">';
                }
                echo '                        <div class="title"></div>';
                echo '                    </a>';
                echo '                    <div class="dropdown-menu dropdown-menu-right">';
                echo '                        <div class="profile-info">';
                echo '                            <a href="profile"><h4 class="username">' . htmlspecialchars($session_row['name']) . '</h4></a>';
                echo '                        </div>';
                echo '                        <ul class="action">';
               //  <!--removed .php-->
               // echo '                            <li><a href="recharge"><i class="fi fi-br-wallet"></i>' . $header_lang['recharge'] . ': ' . htmlspecialchars($session_row['wallet']) . '</a></li>';
                echo '                            <li><a href="profile"><i class="fi fi-br-user"></i>' . $header_lang['profile'] . '</a></li>';
                echo '                            <li><a href="refer"><i class="fi fi-br-users"></i>' . $client_lang['refer'] . '</a></li>';
                echo '                            <li><a href="bidding-history"><i class="fi fi-br-gavel"></i>' . $client_lang['bidding_history'] . '</a></li>';
                echo '                            <li><a href="ticket-purchases"><i class="fi fi-br-ticket"></i>' . $client_lang['ticket_purchases'] . '</a></li>';
                echo '                            <li><a href="investments"><i class="fa fa-piggy-bank"></i>' . $client_lang['my_investment'] . '</a></li>';
                echo '                            <li><a href="winnings"><i class="fi fi-br-trophy-star"></i>' . $header_lang['won'] . '</a></li>';
                echo '                            <li><a href="orders"><i class="fi fi-rr-truck-box"></i>' . $header_lang['order'] . '</a></li>';
                echo '                            <li><a href="logout"><i class="fi fi-br-sign-out-alt"></i>' . $header_lang['logout'] . '</a></li>';
                echo '                        </ul>';
                echo '                    </div>';
                echo '                </li>';
                echo '            </ul>';
                echo '        </div>';
                echo '    </div>';
                echo '    <button type="button" class="sidebar-toggle"><i class="fa fa-times"></i></button>';
                echo '</div>';
            } else {
                echo '<div class="header">';
                echo '    <div>';
                  //<!--removed .php-->
                echo '        <a href="home" class="logo">';
                echo '            <img src="/images/'.$app_logo.'" alt="Logo">&nbsp;&nbsp;';
                echo '            ' . APP_NAME;
                echo '        </a>';
                // echo '        <div class="hamburger" onclick="toggleMenu()">';
                // echo '            <i class="fi fi-rr-apps"></i>';
                // echo '        </div>';  <!--removed .php-->
                echo '        <span>';
                echo '<a id="mobile-a" href="download"><div> 
                            <img src="/images/static/phone.svg">
							    <div>'.$client_lang['mobile_app'].'</div>
                        </div></a>';
                echo '            <a href="#" id="loginBtn">'.$client_lang['login'].'</a>';
                echo '            <a href="#" id="registerBtn" class="active">'.$client_lang['register'].'</a>';
                echo '        </span>';
                echo '    </div>';
                echo '</div>';
            }
            ?>

            <div class="sidebar-menu">
                <ul class="sidebar-nav">

                    <li <?php if ($currentFile == "home.php" || $currentFile == "index.php") { ?>class="active" <?php } ?>> <a href="home">   <!--removed .php-->
                            <!-- <div class="icon"> <i class="fi fi-br-house-chimney"></i> </div> -->
                            <div class="title"><i
                                    class="fa fa-home fa-lg"></i>&nbsp;&nbsp;<?php echo $header_lang['home']; ?></div>
                        </a>
                    </li>
                    <!--removed .php-->
                    <li <?php if ($currentFile == "auctions.php") { ?>class="active" <?php } ?>> <a 
                            href="auctions">
                            <!-- <div class="icon"> <i class="fa fa-gamepad" aria-hidden="true"></i> </div> -->
                            <div class="title"><i
                                    class="fa fa-gavel fa-lg"></i>&nbsp;&nbsp;<?php echo $header_lang['auction']; ?></div>
                        </a>
                    </li>
                    <!-- 
          <li <?php if ($currentFile == "upcoming.php") { ?>class="active" <?php } ?>> <a href="upcoming.php">
              <div class="title"><i class="fi fi-sr-calendar-clock"></i>&nbsp;<?php echo $header_lang['upcoming']; ?></div>
            </a>
          </li>-->

                    <li <?php if ($currentFile == "lotteries.php") { ?>class="active" <?php } ?>> <a href="lotteries">   <!--removed .php-->
                            <div class="title"><i
                                    class="fas fa-ticket-alt"></i>&nbsp;&nbsp;<?php echo $header_lang['lottery']; ?></div>
                        </a>
                    </li>

                    <li <?php if ($currentFile == "shop.php") { ?>class="active" <?php } ?>> <a href="shop">   <!--removed .php-->
                            <div class="title"><i class="fa fa-shopping-basket fa-lg"></i>&nbsp;&nbsp;<?php echo $header_lang['shop']; ?>
                            </div>
                        </a>
                    </li>

                    <li <?php if ($currentFile == "plans.php") { ?>class="active" <?php } ?>> <a href="plans">   <!--removed .php-->
                            <div class="title"><i
                                    class="fa fa-piggy-bank fa-lg"></i>&nbsp;&nbsp;<?php echo $header_lang['invest']; ?></div>
                        </a>
                    </li>


                    <li <?php if ($currentFile == "winners.php") { ?>class="active" <?php } ?>> <a href="winners">  <!--removed .php-->
                            <div class="title"><i class="fa fa-trophy fa-lg"></i>&nbsp;&nbsp;<?php echo $header_lang['result']; ?></div>
                        </a>
                    </li>

                    <?php
                    if ($logged) {
                        echo '<li';
                        if ($currentFile == "recharge.php") { //removed .php
                            echo ' class="active"';
                        }
                        echo '>';
                        echo '    <a href="recharge">'; //removed .php
                        echo '        <div class="title" style="min-width: 90px;">';
                        echo '            <i class="fi fi-sr-marketplace-alt"></i>&nbsp;' . $footer_lang['footerBuy'];
                        echo '        </div>';
                        echo '    </a>';
                        echo '</li>';
                    }
                    ?>
                </ul>
            </div>

        </aside>
        <div class="app-container">
            <nav class="navbar navbar-default" id="navbar">
                <?php
                if ($logged) {
                    echo '<div class="container-fluid">';
                    echo '    <div class="navbar-collapse collapse in">';
                    echo '        <ul class="nav navbar-nav navbar-mobile">';
                    echo '            <li>';
                    echo '                <button type="button" class="sidebar-toggle"> <i class="fa fa-bars"></i> </button>';
                    echo '            </li>';
                    echo '<a href="#" id="loginBtn">Login</a>';
                    echo '            <li class="logo"> <a class="navbar-brand" href="#">&nbsp;&nbsp;';
                    echo '                    ' . APP_NAME . '';
                    echo '                </a> </li>';
                    echo '            <li>';
                    echo '                <button type="button" class="navbar-toggle">';
                    if (PROFILE_IMG) {
                        echo '                    <img class="profile-img" src="/seller/images/' . PROFILE_IMG . '">';
                    } else {
                        echo '                    <img class="profile-img" src="placeholder.jpg">';
                    }
                    echo '                </button>';
                    echo '            </li>';
                    echo '        </ul>';
                    echo '        <ul class="nav navbar-nav navbar-left">';
                    echo '            <li class="navbar-title">';
                    echo '                ' . APP_NAME . '';
                    echo '            </li>';
                    echo '        </ul>';
                    echo '    </div>';
                    echo '</div>';
                }
                ?>
            </nav>

            <script>
                $(document).ready(function () {
                    var button = document.getElementById("languageSelectButton");
                    var iframe = document.getElementById("languageIframe");

                    let showed = false;

                    function showIframe() {
                        if (!showed) {
                            iframe.style.display = "block";
                            showed = true;
                        }
                    }

                    function hideIframe() {
                        if (showed) {
                            iframe.style.display = "none";
                            showed = false;
                        }
                    }

                    function showButtonIframe() {
                        if (!showed) {
                            iframe.style.display = "block";
                            showed = true;
                        }
                        else if (showed) {
                            iframe.style.display = "none";
                            showed = false;
                        }
                    }

                    button.addEventListener("mouseover", showIframe);
                    button.addEventListener("click", showButtonIframe);

                    let unshownStatus = null;

                    button.addEventListener("mouseleave", function () {
                        unshownStatus = setTimeout(hideIframe, 200);
                    });

                    iframe.addEventListener("mouseleave", hideIframe);
                    iframe.addEventListener("mouseenter", function () {
                        if (unshownStatus != null) {
                            clearTimeout(unshownStatus);
                        }
                    });
                });

                try {

                const loginButton = document.querySelector("#loginBtn");

                loginButton.addEventListener("click", (e)=>{
                    e.preventDefault();

                    $("#login-main-container").css("display", "block");
                });

                const registerButton = document.querySelector("#registerBtn");

                registerButton.addEventListener("click", (e)=>{
                    e.preventDefault();

                    $("#register-main-container").css("display", "block");
                });

                const forgetPasswordLoginRedirect = document.querySelector("#forget-password-global-login");

                forgetPasswordLoginRedirect.addEventListener("click", (e)=>{
                    e.preventDefault();

                    $("#login-main-container").css("display", "none");
                    $("#forget-main-container").css("display", "block");
                });

                const registerLoginRedirect = document.querySelector("#register-redirect-global-login");

                registerLoginRedirect.addEventListener("click", (e)=>{
                    e.preventDefault();

                    $("#register-main-container").css("display", "block");
                    $("#login-main-container").css("display", "none");
                });

                const loginForgetRedirect = document.querySelector("#forget-password-redirect-global-login");

                loginForgetRedirect.addEventListener("click", (e)=>{
                    e.preventDefault();

                    $("#login-main-container").css("display", "block");
                    $("#forget-main-container").css("display", "none");
                });

                const loginRegisterRedirect = document.querySelector("#login-redirect-global-register");

                loginRegisterRedirect.addEventListener("click", (e)=>{
                    e.preventDefault();

                    $("#register-main-container").css("display", "none");
                    $("#login-main-container").css("display", "block");
                });
            }
            catch{}

            </script>