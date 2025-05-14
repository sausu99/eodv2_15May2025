                let deferredPrompt;
                
                document.addEventListener('DOMContentLoaded', function () {
                    const installLink = document.getElementById('mobile-a');
                
                    // Register the service worker
                    if ('serviceWorker' in navigator) {
                        navigator.serviceWorker.register('/service-worker.js')
                            .then(function (registration) {
                                console.log('Service Worker registered with scope:', registration.scope);
                            })
                            .catch(function (error) {
                                console.error('Service Worker registration failed:', error);
                            });
                    } else {
                        console.warn('Service Workers are not supported in this browser.');
                    }
                
                    // Check if the user is on an iPhone
                    function isIPhone() {
                        return /iPhone|iPod/.test(navigator.userAgent) && !window.MSStream;
                    }
                
                    // Listen for the beforeinstallprompt event
                    window.addEventListener('beforeinstallprompt', function (e) {
                        if (isIPhone()) {
                            // Redirect to ios_download.php for iPhone users
                            window.location.href = '/ios_download.php';
                            return;
                        }
                
                        console.log('beforeinstallprompt event fired');
                        e.preventDefault(); // Prevent the mini-infobar from appearing on mobile
                        deferredPrompt = e; // Save the event
                        console.log('beforeinstallprompt event saved');
                        installLink.style.display = 'block'; // Show install link
                    });
                
                    // Handle the install link click
                    installLink.addEventListener('click', function (event) {
                        event.preventDefault(); // Prevent the default anchor behavior
                        console.log('Install link clicked');
                        if (deferredPrompt) {
                            deferredPrompt.prompt(); // Show the prompt
                            deferredPrompt.userChoice.then(function (choiceResult) {
                                if (choiceResult.outcome === 'accepted') {
                                    console.log('User accepted the install prompt');
                                } else {
                                    console.log('User dismissed the install prompt');
                                }
                                // Reset the deferred prompt
                                installLink.style.display = 'none'; // Optionally hide the install link
                            }).catch(function (error) {
                                console.error('Error showing install prompt:', error);
                            });
                        } else {
                            console.warn('No deferred prompt available.');
                        }
                    });
                });


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
