<?php
if (isset($_SESSION["verification_code"]))
{
    $otp = true;
}
else {
    $otp = false;
}

ini_set('display_errors', '1');
ini_set('display_startup_errors', '1');
error_reporting(E_ALL);
?>

<link rel="stylesheet" href="assets/css/register.css">

<div class="app app-default user-control-main-container" id="register-main-container">
        <div class="app-container app-login">
            <div class="flex-center">
                <div class="app-body">
                    <div class="app-block">
                        <div class="app-form login-form box">
                            <div class="form-header">
                                <div class="app-brand"><img src="../images/<?php echo APP_LOGO; ?>" height="100px" width="100px"/></div>
                            </div>
                            <?php
                                if (!$otp)
                                {
                            ?> 
                            <h1><?php echo $client_lang["registerNow"]; ?></h1>        
                            <?php
                                }
                                else {
                                    ?>
                                <h1>VERIFY NOW</h1>                                            
                                    <?php
                                }
                            ?>

                        <div class="close-button-user" id="register-close-button"><i class="fas fa-times "></i></div>

                            <form action="register_db.php" method="post" id="form-register">
                                <?php if (!$otp) { ?>
                                <div class="form-group">
                                    <?php if (isset($_SESSION['msg'])) { ?>
                                        <div class="alert alert-danger" role="alert">
                                            <?php echo $client_lang[$_SESSION['msg']]; ?>
                                        </div>
                                        <?php unset($_SESSION['msg']);
                                    } ?>
                                </div>
                                <div class="form-group">
                                    <!--<label for="name-register"><?php echo $client_lang["nameRequest"]; ?>:</label>-->
                                    <input type="text" name="name" id="name-register" class="form-control"
                                        placeholder="<?php echo $client_lang["Name"]; ?>" required>
                                </div>
                                <div class="form-group">
                                    <!--<label for="email-register"><?php echo $client_lang["emailRegisterRequest"]; ?>:</label>-->
                                    <input type="text" name="email" id="email-register" class="form-control"
                                        placeholder="<?php echo $client_lang["Email"]; ?>" required>
                                </div>

                                <div class="form-group">
                                    <!--<label for="phone-register"><?php echo $client_lang["phoneRegisterRequest"]; ?>:</label>-->
                                    <input type="text" name="phone" id="phone-register" class="form-control"
                                        placeholder="<?php echo $client_lang["phoneNumber"]; ?>" required>
                                    <input type="hidden" id="country-code-register" name="countryCode">
                                </div>
                
                                <div class="form-group">
                                    <!--<label for="password-register"><?php echo $client_lang["createPassword"]; ?>:</label>-->
                                    <input type="password" name="password" id="password-register" class="form-control"
                                        placeholder="<?php echo $client_lang["Password"]; ?>" required>
                                </div>

                                <div class="form-group" id="referral-code-container" style="display: none;">
                                    <!--<label for="referral"><?php echo $client_lang["referralRequest"]; ?>:</label>-->
                                    <input type="text" name="referral" id="referral" class="form-control"
                                         placeholder="<?php echo $client_lang["referralRequest"]; ?>">
                                </div>

                                <div class="text-center">
                                    <button type="submit" class="btn btn-submit"><?php echo $client_lang["Register"]; ?>
                                        →</button>
                                    <br>
                                    <!--<a href="login.php" class="btn btn-register">Already have an account? Login</a>-->
                                </div>
                                <a href="#" id="referralCodeSelect"><?php echo $client_lang["IreferralCode"]; ?>?</a>
                                <div class="login-extra">
                                    <p><?php echo $client_lang["alreadyRegistered"]; ?>? <a
                                            href="/login.php" id="login-redirect-global-register"><?php echo $client_lang["login"]; ?></a></p>
                                </div>
                                <?php } 
                                else {
                                ?>
                                    <div class="form-group">
                                    <?php if (isset($_SESSION['msg'])) { ?>
                                        <div class="alert alert-danger" role="alert">
                                            <?php echo $client_lang[$_SESSION['msg']]; ?>
                                        </div>
                                        <?php unset($_SESSION['msg']);
                                    } ?>

                                    <div class="form-group">
                                    <label for="verification">Verification Code:</label>
                                    <input type="text" name="verification" id="verification" class="form-control"
                                        placeholder="Enter your verification code." required>
                                        <br>
                                        <br>

                                        <div class="text-center">
                                    <button type="submit" class="btn btn-submit">Verify Now
                                        →</button>
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

    <!-- Bootstrap JS and dependencies -->
    <script src="https://code.jquery.com/jquery-3.3.1.slim.min.js"
        integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo"
        crossorigin="anonymous"></script>

        <script>
    $(document).ready(function () {
        $("#register-close-button").click(()=>{
            $("#register-main-container").css("display", "none");
        });;

      const input = document.querySelector("#phone-register");
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

      $('#form-register').on('submit', function(e) {
        e.preventDefault();

        var fullPhoneNumber = iti.getNumber();
        var countryCode = iti.getSelectedCountryData().dialCode;

        $('#fullPhoneNumber').val(fullPhoneNumber);
        $('#country-code-register').val(countryCode);

        this.submit();
    });

    const referralCodeContainer = document.querySelector("#referral-code-container");
    const referralCodeButton = document.querySelector("#referralCodeSelect");

    referralCodeButton.addEventListener("click", (e)=>{
        e.preventDefault();
        if (referralCodeButton.textContent == "<?php echo $client_lang["IreferralCode"]; ?>?")
        {
            referralCodeButton.textContent = "<?php echo $client_lang["NotreferralCode"]; ?>?";
            referralCodeContainer.style.display = "block";
        }
        else
        {
            referralCodeButton.textContent = "<?php echo $client_lang["IreferralCode"]; ?>?";
            referralCodeContainer.style.display = "none";
        }
    });
});
</script>