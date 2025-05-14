<?php

// Check for share_id in the URL and store it
$share_id = isset($_GET['share_id']) ? $_GET['share_id'] : '';
?>
<link rel="stylesheet" href="assets/css/home.css">
<link rel="stylesheet" href="assets/css/login.css"> 

 
 <div class="app app-default user-control-main-container" id="login-main-container">
    <div class="app-container app-login">
      <div class="flex-center">
        <div class="app-body">
          <div class="app-block">
            <div class="app-form login-form box login-container">
            <h1><?php echo $client_lang["loginNow"]; ?></h1>
            <div class="close-button-user" id="login-close-button"><i class="fas fa-times "></i></div>
  
            <div class="clearfix"></div>
            <form action="login_db.php<?php echo $share_id ? '?share_id=' . $share_id : ''; ?>" method="post" id="form">
              <div class="input-group" style="border:0px;">
                <?php if (isset($_SESSION['msg'])) { ?>
                <div class="alert alert-danger alert-dismissible" role="alert"> <?php echo $client_lang[$_SESSION['msg']]; ?> </div>
                <?php unset($_SESSION['msg']); } ?>
              </div>

              <div class="input-group" id="email-login-container">
                <label for="email"><?php echo $client_lang["emailRequest"]; ?>:</label>
                <input type="text" name="email" id="email" value="" class="form-control" placeholder="<?php echo $client_lang["Email"]; ?>" aria-describedby="basic-addon1">
              </div>
             
              <div class="input-group" id="phone-login-container" style="display: none;">
                <label for="phone"><?php echo $client_lang["phoneRequest"]; ?>:</label>
                <input type="text" name="phone" id="phone" value="" class="form-control" placeholder="<?php echo $client_lang["phoneNumber"]; ?>" aria-describedby="basic-addon1">
                <input type="hidden" id="countryCode" name="countryCode">
              </div>

              <div class="input-group">
                <label for="password"><?php echo $client_lang["passwordRequest"]; ?>:</label>
                <input type="password" name="password" id="password" value="" class="form-control" placeholder="<?php echo $client_lang["Password"]; ?>" aria-describedby="basic-addon2">
              </div>
              <div>
              <input type="submit" class="btn btn-submit" value="<?php echo $client_lang["login"]; ?> →"><br>
              </div>
              <p id="continue-seperator">OR</p>
              <div id="email-login-method" style="display: none;" class="continue-style" role="button" tabindex="0" aria-label="<?php echo $client_lang["continue_email"]; ?>" ><i color="#213343" size="18"><svg xmlns="http://www.w3.org/2000/svg" fill="#192733" width="18" height="18" viewBox="0 0 20 20" aria-labelledby="icon-svg-title- icon-svg-desc-" role="img" class="sc-rbbb40-0 fLhyDr"><title>mail-fill</title><path d="M10 9.58c-1.62 0-10-4.76-10-4.76v-0.74c0-0.92 0.74-1.66 1.66-1.66h16.68c0.92 0 1.66 0.74 1.66 1.66l-0.020 0.84c0 0-8.28 4.66-9.98 4.66zM10 11.86c1.78 0 9.98-4.46 9.98-4.46l0.020 10c0 0.92-0.74 1.66-1.66 1.66h-16.68c-0.92 0-1.66-0.74-1.66-1.66l0.020-10c0 0 8.36 4.46 9.98 4.46z"></path></svg></ii<span>&nbsp;<?php echo $client_lang["continue_email"]; ?></span></div>
              <div id="phone-login-method" class="continue-style" role="button" tabindex="0" aria-label="<?php echo $client_lang["continue_email"]; ?>" ><i  color="#213343" size="18" ><svg xmlns="http://www.w3.org/2000/svg" xml:space="preserve" width="6.82666in" height="6.82666in" style="shape-rendering:geometricPrecision; text-rendering:geometricPrecision; image-rendering:optimizeQuality; fill-rule:evenodd; clip-rule:evenodd;     height: 24px; width: 24px; fill: #213343"
viewBox="0 0 6.82666 6.82666"
 xmlns:xlink="http://www.w3.org/1999/xlink">
 <defs>
  <style type="text/css">
   <![CDATA[
    .fil0 {fill:none}
    .fil2 {fill:#213343}
    .fil1 {fill:#213343}
   ]]>
  </style>
 </defs>
 <g id="Layer_x0020_1">
  <metadata id="CorelCorpID_0Corel-Layer"/>
  <g id="_491463032">
   <rect id="_491463320" class="fil0" width="6.82666" height="6.82666"/>
   <rect id="_491463128" class="fil0" x="0.853331" y="0.853331" width="5.12" height="5.12"/>
  </g>
  <g id="_491478824">
   <path id="_491463224" class="fil1" d="M0.908571 2.24032c0.0676417,1.38906 1.96802,3.15789 3.2551,3.56889 0.863717,0.275768 2.1483,-0.268205 1.63998,-0.776646l-0.802594 -0.802984c-0.122031,-0.122118 -0.319803,-0.107913 -0.439051,0.0112992l-0.460874 0.460728c-0.991472,-0.540205 -1.40748,-0.965465 -1.95219,-1.951l0.461587 -0.461201c0.119421,-0.119453 0.132945,-0.316929 0.0108228,-0.439051l-0.802921 -0.802949c-0.44937,-0.44937 -0.93548,0.667693 -0.909858,1.19292z"/>
   <path id="_491478584" class="fil2" d="M0.908571 2.24032l1.24037 0.51028 0.461587 -0.461201c0.119421,-0.119453 0.132945,-0.316929 0.0108228,-0.439051l-0.802921 -0.802949c-0.44937,-0.44937 -0.93548,0.667693 -0.909858,1.19292z"/>
   <path id="_491478176" class="fil2" d="M4.16367 5.8092c0.863717,0.275768 2.1483,-0.268205 1.63998,-0.776646l-0.802594 -0.802984c-0.122031,-0.122118 -0.319803,-0.107913 -0.439051,0.0112992l-0.460874 0.460728 0.0625433 1.1076z"/>
  </g>
 </g>
</svg></i><span ><?php echo $client_lang["continue_mobile"]; ?></span></div>

              <div class="login-extra">
                <p><?php echo $client_lang["newUser"]; ?>? <a href="/register.php" id="register-redirect-global-login"><?php echo $client_lang["register"]; ?></a></p><p><!--|</p><a href="/forget-password.php" id="forget-password-global-login"><?php echo $client_lang["forgotPassword"]; ?></a>-->
              </div>
            </form>
          </div>
        </div>
      </div>
    </div>
  </div>
</div>
<script>
     $(document).ready(function () {
        const emailLoginMethod = document.querySelector("#email-login-method");
        const phoneLoginMethod = document.querySelector("#phone-login-method");

        emailLoginMethod.addEventListener("click", ()=>{
            $("#email-login-container").css("display", "block");
            $("#phone-login-container").css("display", "none");
            phoneLoginMethod.style.display = "flex";
            emailLoginMethod.style.display = "none";
        });
        
        phoneLoginMethod.addEventListener("click", ()=>{
            $("#email-login-container").css("display", "none");
            $("#phone-login-container").css("display", "block");
            phoneLoginMethod.style.display = "none";
            emailLoginMethod.style.display = "flex";
        });

        $("#login-close-button").click(()=>{
            $("#login-main-container").css("display", "none");
        });;

      const input = document.querySelector("#phone");
      const iti = window.intlTelInput(input, {
        utilsScript: "assets/js/utils.js",
        initialCountry: "auto",
        geoIpLookup: function(callback) {
            fetch('https://ipinfo.io/json')
                .then(response => response.json())
                .then(data => callback(data.country))
                .catch(() => callback('us'));
        }
      });

      $('#form').on('submit', function(e) {
        e.preventDefault();

        var fullPhoneNumber = iti.getNumber();
        var countryCode = iti.getSelectedCountryData().dialCode;

        $('#fullPhoneNumber').val(fullPhoneNumber);
        $('#countryCode').val(countryCode);

        this.submit();
    });
    });
</script>