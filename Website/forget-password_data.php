<?php

// Check for share_id in the URL and store it

$share_id = isset($_GET['share_id']) ? $_GET['share_id'] : '';

if (isset($_SESSION["create-password"])) {

  $otp = 1;

} else if (isset($_SESSION["verify-code"])) {

  $otp = 2;

} else {

  $otp = 3;

}

?>



<link rel="stylesheet" href="assets/css/forget-password.css">

 <div class="app app-default user-control-main-container"  id="forget-main-container">

    <div class="app-container app-login">

      <div class="flex-center">

        <div class="app-body">

          <div class="app-block">

            <div class="app-form login-form box login-container">

            

            <?php

                                if ($otp == 3)

                                {

                            ?> 

                  <h1><?php echo $client_lang["forgetNow"]; ?></h1>                            <?php

                                }

                                else if ($otp == 2) {

                                    ?>

                  <h1>Verify Now</h1>

                      <?php

                                }

                            ?>

                            <?php

                              if ($otp == 1)

                              {

                                echo '<h1>CREATE PASSWORD</h1>';

                              }

                            ?>

                    <div class="close-button-user" id="forget-close-button"><i class="fas fa-times "></i></div>



            

  

            <div class="clearfix"></div>

            <form action="forgetpassword_db.php<?php echo $share_id ? '?share_id=' . $share_id : ''; ?>" method="post" id="form-forget">

              <div class="input-group" style="border:0px;">

                <?php if (isset($_SESSION['msg'])) { ?>

                <div class="alert alert-danger alert-dismissible" role="alert"> <?php echo $client_lang[$_SESSION['msg']]; ?> </div>

                <?php unset($_SESSION['msg']); } ?>

              </div>



              <?php

                if ($otp == 3)

                {

                  echo '<div class="input-group" id="email-forget-container">';

                  echo '<label for="email-forget">' . $client_lang["emailRequest"] . ':</label>';

                  echo '<input type="text" name="email" id="email-forget" value="" class="form-control" placeholder="' . $client_lang["Email"] . '" aria-describedby="basic-addon1">';

                  echo '</div>';



                  echo '<div class="input-group" id="phone-forget-container" style="display: none;">';

                  echo '<label for="phone-forget">' . $client_lang["phoneRequest"] . ':</label>';

                  echo '<input type="text" name="phone" id="phone-forget" value="" class="form-control" placeholder="' . $client_lang["phoneNumber"] . '" aria-describedby="basic-addon1">';

                  echo '<input type="hidden" id="country-code-forget" name="countryCode">';

                  echo '</div>';

                }

                else if ($otp == 2)

                {

                  echo '<div class="input-group" id="email-forget-container">';

                  echo '<label for="verification">Verification Code:</label>';

                  echo '<input type="text" name="verification" id="verification" value="" class="form-control" placeholder="Enter verification code." aria-describedby="basic-addon1">';

                  echo '</div>';

                }

                else 

                {

                  echo '<div class="input-group" id="email-forget-container">';

                  echo '<div class="password-container">';

                  echo '<label for="create-password">Create Password:</label>';

                  echo '<input type="password" name="create-password" id="create-password" value="" class="form-control password-input" placeholder="Enter new password." aria-describedby="basic-addon1">';                

                  echo '<button type="button" id="togglePassword" class="toggle-button">Show</button>';

                 echo ' </div>';

                 echo ' </div>';

                  

                  echo '<div class="input-group" id="email-forget-container">';

                  echo '<div class="password-container">';

                  echo '<label for="confirm-password">Confirm Password:</label>';

                  echo '<input type="password" name="confirm-password" id="confirm-password" value="" class="form-control password-input" placeholder="Enter confirm password." aria-describedby="basic-addon1">';

                  echo '<button type="button" id="togglePassword-1" class="toggle-button">Show</button>';

                  echo '</div>';

                  echo ' </div>';

                }

              ?>



              <?php

                if ($otp == 1)

                {

              ?>



              <script>

                document.getElementById('togglePassword').addEventListener('click', function (e) {

                  const passwordInput = document.getElementById('create-password');

                  const type = passwordInput.getAttribute('type') === 'password' ? 'text' : 'password';

                  passwordInput.setAttribute('type', type);



                  this.textContent = type === 'password' ? 'Show' : 'Hide';

              });



                document.getElementById('togglePassword-1').addEventListener('click', function (e) {

                    const passwordInput = document.getElementById('confirm-password');

                    const type = passwordInput.getAttribute('type') === 'password' ? 'text' : 'password';

                    passwordInput.setAttribute('type', type);



                    this.textContent = type === 'password' ? 'Show' : 'Hide';

                });

              </script>

              <?php

                }

              ?>



              <div>

                <?php

                  if ($otp == 3)

                  {

                ?>

                <input type="submit" class="btn btn-submit" id="forget-password-button" value="<?php echo $client_lang["forgetPassword"]; ?> →"><br>

                <?php

                  } else if ($otp == 2) {

                ?>

              <input type="submit" class="btn btn-submit" value="Verify Now →"><br>

              <?php

                  }

                  else {

                    echo '<input type="submit" class="btn btn-submit" value="Create Password →"><br>';

                  }

              ?>

            </div>

              

              <?php

              if ($otp == 3)

              {

                ?>

             <p id="continue-seperator">OR</p>

              <div id="email-login-method-forget" style="display: none;" class="continue-style" role="button" tabindex="0" aria-label="Continue with Email" ><i color="rgb(11, 105, 154)" size="18"><svg xmlns="http://www.w3.org/2000/svg" fill="rgb(11, 105, 154)" width="18" height="18" viewBox="0 0 20 20" aria-labelledby="icon-svg-title- icon-svg-desc-" role="img" class="sc-rbbb40-0 fLhyDr"><title>mail-fill</title><path d="M10 9.58c-1.62 0-10-4.76-10-4.76v-0.74c0-0.92 0.74-1.66 1.66-1.66h16.68c0.92 0 1.66 0.74 1.66 1.66l-0.020 0.84c0 0-8.28 4.66-9.98 4.66zM10 11.86c1.78 0 9.98-4.46 9.98-4.46l0.020 10c0 0.92-0.74 1.66-1.66 1.66h-16.68c-0.92 0-1.66-0.74-1.66-1.66l0.020-10c0 0 8.36 4.46 9.98 4.46z"></path></svg></ii<span>Continue with Email</span></div>

              <div id="phone-login-method-forget" class="continue-style" role="button" tabindex="0" aria-label="Continue with Email" ><i  color="rgb(11, 105, 154)" size="18" ><svg xmlns="http://www.w3.org/2000/svg" xml:space="preserve" width="6.82666in" height="6.82666in" style="shape-rendering:geometricPrecision; text-rendering:geometricPrecision; image-rendering:optimizeQuality; fill-rule:evenodd; clip-rule:evenodd;     height: 24px; width: 24px; fill: rgb(11, 105, 154);"

viewBox="0 0 6.82666 6.82666"

 xmlns:xlink="http://www.w3.org/1999/xlink">

 <defs>

  <style type="text/css">

   <![CDATA[

    .fil0 {fill:none}

    .fil2 {fill:rgb(11, 105, 154)}

    .fil1 {fill:rgb(11, 105, 154)}

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

</svg></i><span >Continue with Phone Number</span></div>



<script>

                  const emailLoginMethodForget = document.querySelector("#email-login-method-forget");

                  const phoneLoginMethodForget = document.querySelector("#phone-login-method-forget");



                  emailLoginMethodForget.addEventListener("click", ()=>{

                      $("#email-forget-container").css("display", "block");

                      $("#phone-forget-container").css("display", "none");

                      phoneLoginMethodForget.style.display = "flex";

                      emailLoginMethodForget.style.display = "none";

                  });

                  

                  phoneLoginMethodForget.addEventListener("click", ()=>{

                      $("#email-forget-container").css("display", "none");

                      $("#phone-forget-container").css("display", "block");

                      phoneLoginMethodForget.style.display = "none";

                      emailLoginMethodForget.style.display = "flex";

                  });

              </script>



                <div class="login-extra">

                                    <p><?php echo $client_lang["alreadyRegistered"]; ?>? <a

                                            href="/login.php" id="forget-password-redirect-global-login"><?php echo $client_lang["login"]; ?></a></p>

                                </div>

                                <?php

              }

                                ?>

            </form>

          </div>

        </div>

      </div>

    </div>

  </div>

</div>



<script src="assets/js/intlTelInput.min.js"></script>



<script>

    $(document).ready(function () {

        $("#forget-close-button").click(()=>{

            $("#forget-main-container").css("display", "none");

        });;



      const input = document.querySelector("#phone-forget");

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



      $('#form-forget').on('submit', function(e) {

        e.preventDefault();



        var fullPhoneNumber = iti.getNumber();

        var countryCode = iti.getSelectedCountryData().dialCode;



        $('#fullPhoneNumber').val(fullPhoneNumber);

        $('#country-code-forget').val(countryCode);



        document.querySelector("#forget-password-button").disabled = true;

        document.querySelector("#forget-password-button").value = "Please Wait";



        this.submit();

    });

    });

</script>