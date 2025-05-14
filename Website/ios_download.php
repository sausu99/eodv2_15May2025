<?php
    include("includes/connection.php");
    include("language/language.php");
    include('includes/function.php');
    
    $qry = "select app_logo from tbl_settings where id='1'";
    $result = mysqli_query($mysqli, $qry);
    $row = mysqli_fetch_assoc($result);
    $app_logo = $row['app_logo'];
?>

<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <link rel="manifest" href="manifest.json">
  <link rel="icon" href="/images/<?php echo $app_logo ?>" type="image/png">
  <title><?php echo $client_lang['download_app']; ?></title>
  <style>
    body {
      font-family: 'Arial', sans-serif;
      margin: 0;
      padding: 20px;
      background-color: #f9f9f9;
      color: #333;
    }
    h1 {
      text-align: center;
      color: #2c3e50;
    }
    .container {
      max-width: 600px;
      margin: 0 auto;
      background: #fff;
      padding: 20px;
      box-shadow: 0 4px 8px rgba(0,0,0,0.1);
      border-radius: 8px;
    }
    .app-image {
      display: block;
      margin: 20px auto;
      width: 150px;
      height: 150px;
      border-radius: 20px;
    }
    .instructions {
      margin-top: 20px;
    }
    .instruction-step {
      margin-bottom: 15px;
      display: flex;
      align-items: center;
    }
    .instruction-step img {
      margin-left: 10px;
    }
    .instruction-step strong {
      color: #2980b9;
    }
    .instruction-step span {
      margin-left: 10px;
    }
    .install-button {
      display: block;
      margin: 30px auto;
      padding: 10px 20px;
      background-color: #3498db;
      color: #fff;
      text-align: center;
      border: none;
      border-radius: 4px;
      cursor: pointer;
      font-size: 16px;
      transition: background-color 0.3s;
    }
    .install-button:hover {
      background-color: #2980b9;
    }
  </style>
<script>
  if ('serviceWorker' in navigator) {
    window.addEventListener('load', function() {
      navigator.serviceWorker.register('/service-worker.js').then(function(registration) {
        console.log('ServiceWorker registration successful with scope: ', registration.scope);
      }, function(err) {
        console.log('ServiceWorker registration failed: ', err);
      });
    });
  }
</script>
<script>
  let deferredPrompt;

  window.addEventListener('beforeinstallprompt', (event) => {
    // Prevent the default behavior of the browser
    event.preventDefault();
    // Stash the event so it can be triggered later.
    deferredPrompt = event;
    // Update UI notify the user they can install the PWA
    // Optionally, display a button or other UI element to trigger the install prompt
  });

  // Optionally, add an event listener to trigger the install prompt
  // For example, when the user clicks on a button
  installButton.addEventListener('click', (event) => {
    // Show the install prompt
    deferredPrompt.prompt();
    // Wait for the user to respond to the prompt
    deferredPrompt.userChoice
      .then((choiceResult) => {
        if (choiceResult.outcome === 'accepted') {
          console.log('User accepted the install prompt');
        } else {
          console.log('User dismissed the install prompt');
        }
        deferredPrompt = null;
      });
  });
</script>
</head>
<body>
  <div class="container">
    <h1><?php echo $client_lang['download_app']; ?></h1>
    <img src="/images/<?php echo $app_logo ?>" alt="App Image" class="app-image">
    <div class="instructions">
      <h2><?php echo $client_lang['download_app_how']; ?></h2>
      <div class="instruction-step">
        <strong><?php echo $client_lang['step_1']; ?>:</strong> <span><?php echo $client_lang['download_app_tap']; ?></span> <img src="/images/ios_share.png" height="30" width="30"> <span><?php echo $client_lang['download_app_tap1']; ?></span>
      </div>
      <div class="instruction-step">
        <strong><?php echo $client_lang['step_2']; ?>:</strong> <span><?php echo $client_lang['download_app_scroll']; ?></span>
      </div>
      <div class="instruction-step">
        <strong><?php echo $client_lang['step_3']; ?>:</strong> <span><?php echo $client_lang['download_app_tapadd']; ?></span>
      </div>
      <div class="instruction-step">
        <strong><?php echo $client_lang['step_4']; ?>:</strong> <span><?php echo $client_lang['download_app_launch']; ?></span>
      </div>
    </div>
  </div>
</body>
</html>
