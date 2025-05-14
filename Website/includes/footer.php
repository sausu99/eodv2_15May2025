<?php include ("includes/connection.php");

// Fetch Twilio SID and Auth Token from tbl_settings
$settings_query = "SELECT * FROM tbl_settings";
$settings_result = mysqli_query($mysqli, $settings_query);
$settings_row = mysqli_fetch_assoc($settings_result);

?>
<html lang="en">

<head>
  <link rel="manifest" href="../manifest.json">
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css"
    integrity="sha512-uT5i/JrVLg23c9Lls4t9gKVV1h/eUzI5xGmR0KLeEFe/eg8R0Zp3aiTt7Id3QROiLbhqf+/1SYU5JlfoAXtI1w=="
    crossorigin="anonymous" referrerpolicy="no-referrer" />
  <style>
    body {
      margin: 0;
      padding: 0;
      font-family: 'Arial', sans-serif;
    }

    .app-footer {
      background: #192733;
      padding: 45px 0 20px;
      font-size: 15px;
      line-height: 24px;
      color: #737373;
      width: 100%;
      color: white !important;
    }

    .app-footer h6 {
      font-size: 17px !important;
      font-weight: 600 !important;
      text-transform: uppercase;
    }

    .site-footer hr {
      border-top-color: #bbb;
      opacity: 0.5
    }

    .site-footer hr.small {
      margin: 20px 0
    }

    .site-footer h6 {
      color: #fff;
      font-size: 16px;
      text-transform: uppercase;
      margin-top: 5px;
      letter-spacing: 2px
    }

    .site-footer a {
      color: #737373;
    }

    .site-footer a:hover {
      color: #3366cc;
      text-decoration: none;
    }

    .footer-links {
      padding-left: 0;
      list-style: none
    }

    .footer-links li {
      display: block
    }

    .footer-links a {
      color: white;
      text-decoration: none;
      cursor: pointer;
      font-size: 14px;
      line-height: 1.5;
    }

    .footer-links a:active,
    .footer-links a:focus,
    .footer-links a:hover {
      text-decoration: underline;
    }

    .footer-links.inline li {
      display: inline-block
    }

    .site-footer .social-icons {
      text-align: right
    }

    .site-footer .social-icons a {
      width: 40px;
      height: 40px;
      line-height: 40px;
      margin-left: 6px;
      margin-right: 0;
      border-radius: 100%;
      background-color: #33353d
    }

    .copyright-text {
      margin: 0
    }

    @media (max-width:991px) {
      .site-footer [class^=col-] {
        margin-bottom: 30px
      }
    }

    @media (max-width:767px) {
      .site-footer {
        padding-bottom: 0
      }

      .site-footer .copyright-text,
      .site-footer .social-icons {
        text-align: center
      }
    }

    .social-icons {
      padding-left: 0;
      margin-bottom: 0;
      list-style: none
    }

    .social-icons li {
      display: inline-block;
      margin-bottom: 4px
    }

    .social-icons li.title {
      margin-right: 15px;
      text-transform: uppercase;
      color: #96a2b2;
      font-weight: 700;
      font-size: 13px
    }

    .social-icons a {
      background-color: #eceeef;
      color: #818a91;
      font-size: 16px;
      display: inline-block;
      line-height: 44px;
      width: 44px;
      height: 44px;
      text-align: center;
      margin-right: 8px;
      border-radius: 100%;
      -webkit-transition: all .2s linear;
      -o-transition: all .2s linear;
      transition: all .2s linear
    }

    .social-icons a:active,
    .social-icons a:focus,
    .social-icons a:hover {
      color: #fff;
      background-color: #29aafe
    }

    .social-icons.size-sm a {
      line-height: 34px;
      height: 34px;
      width: 34px;
      font-size: 14px
    }

    .social-icons a.facebook:hover {
      background-color: #3b5998
    }

    .social-icons a.twitter:hover {
      background-color: #00aced
    }

    .social-icons a.linkedin:hover {
      background-color: #007bb6
    }

    .social-icons a.dribbble:hover {
      background-color: #ea4c89
    }

    @media (max-width:767px) {
      .social-icons li.title {
        display: block;
        margin-right: 0;
        font-weight: 600
      }
    }
  </style>
</head>

<body>

  <div id="app">

    <footer class="app-footer">
      <div class="row">
        <div class="col-sm-12 col-md-6">
          <h6><?php echo $footer_lang['footerAbout']; ?></h6>
          <p class="text-justify"><?php echo APP_NAME; ?> <?php echo $footer_lang['footerDescription']; ?></p>
          <div id="app-install-buttons">
            <a href="<?php echo $settings_row['app_link']; ?>" target="_blank">
                <img
                    src="https://lh3.googleusercontent.com/q1k2l5CwMV31JdDXcpN4Ey7O43PxnjAuZBTmcHEwQxVuv_2wCE2gAAQMWxwNUC2FYEOnYgFPOpw6kmHJWuEGeIBLTj9CuxcOEeU8UXyzWJq4NJM3lg=s0"
                    alt="Google Play Store" class="install-button" style="width: 153px; height: 45px; border-radius: 6px;">
            </a>
            <!--Removed .php-->
            <a href="ios_download" id="pwa-install-button">
                <img src="/images/static/ioslogo.png" alt="ios App" class="pwa-install-button" style="width: 153px; height: 45px; border-radius: 6px;">
            </a>
            <script>
              window.addEventListener('load', () => {
                registerSW();
              });

              // Register the Service Worker
              async function registerSW() {
                if ('serviceWorker' in navigator) {
                  try {
                    await navigator
                      .serviceWorker
                      .register('serviceworker.js');
                  }
                  catch (e) {
                    console.log('SW registration failed');
                  }
                }
              }
            </script>

            <script>
              let deferredPrompt;
              const installButton = document.getElementById('pwa-install-button');

              window.addEventListener('beforeinstallprompt', (e) => {
                e.preventDefault();
                deferredPrompt = e;
                installButton.addEventListener('click', (e) => {
                  deferredPrompt.prompt();
                  deferredPrompt.userChoice.then((choiceResult) => {
                    if (choiceResult.outcome === 'accepted') {
                      console.log('User accepted the install prompt');
                    } else {
                      console.log('User dismissed the install prompt');
                    }
                    deferredPrompt = null;
                  });
                });
              });
            </script>
          </div>
        </div>

        <div class="col-xs-6 col-md-3">
          <h6><?php echo $footer_lang['footerSite']; ?></h6>
          <ul class="footer-links">
              <!--Removed .php-->
            <li><a href="home"><?php echo $footer_lang['footerDash']; ?></a></li>
            <li><a href="shop"><?php echo $footer_lang['footerBuy']; ?></a></li>
            <li><a href="reviews"><?php echo $footer_lang['footerReview']; ?></a></li>
            <li><a href="howitworks"><?php echo $footer_lang['footerHow']; ?></a></li>
            <li><a href="about-us"><?php echo $footer_lang['footerAboutUs']; ?></a></li>
            <li><a href="winners"><?php echo $footer_lang['footerWinners']; ?></a></li>
          </ul>
        </div>

        <div class="col-xs-6 col-md-3">
          <h6><?php echo $footer_lang['footerQuick']; ?></h6>
          <ul class="footer-links">
            <li><a href="privacy-policy"><?php echo $footer_lang['footerPrivacy']; ?></a></li>
            <li><a href="faq"><?php echo $footer_lang['footerFaq']; ?></a></li>
            <li><a href="cookie-policy"><?php echo $footer_lang['footerCookie']; ?></a></li>
            <li><a href="terms"><?php echo $footer_lang['footerTerms']; ?></a></li>
            <li><a href="cancelation-policy"><?php echo $footer_lang['footerRefund']; ?></a></li>
          </ul>
        </div>
      </div>
      <hr>
  </div>
  </footer>

  <div id="consume-space"></div>



  <script type="text/javascript" src="assets/js/vendor.js"></script>
  <script type="text/javascript" src="assets/js/app.js"></script>
</body>
<script>
  window.addEventListener('load', () => {
    registerSW();
  });

  // Register the Service Worker
  async function registerSW() {
    if ('serviceWorker' in navigator) {
      try {
        await navigator
          .serviceWorker
          .register('serviceworker.js');
      }
      catch (e) {
        console.log('SW registration failed');
      }
    }
  }
</script>

</html>
<?php
$email_query = "SELECT admin_email, demo_access FROM tbl_settings";
$email_result = mysqli_query($mysqli, $email_query);
$email_info = mysqli_fetch_assoc($email_result);
?>
