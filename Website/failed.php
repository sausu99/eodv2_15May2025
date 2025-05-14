<?php
include('language/language.php');
include("includes/connection.php");
include('includes/function.php');
?>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <title><?php echo $client_lang['paymentFailed']; ?></title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <style>
        /* Custom styles */
        .container {
            text-align: center;
            margin-top: 50px;
        }
        .lottie-container {
            width: 150px; /* Set your desired width */
            height: 150px; /* Set your desired height */
            margin-bottom: 20px;
        }
        .thank-you-text {
            font-size: 24px;
            margin-bottom: 20px;
        }
    </style>
</head>

<body>
    <!-- Lottie animation container -->
    <div id="lottie-container" class="container lottie-container"></div>

    <!-- Your existing content -->
    <div class="container">
        <h1><?php echo $client_lang['paymentFailed']; ?></h1>
        <p class="thank-you-text"><?php echo $client_lang['paymentFailedReason']; ?></p>
        <?php if(!empty($ERROR)):?>
            <h3 class="text-danger"><?php echo $ERROR;?></h3>
        <?php endif;?>

        <a href="home.php" class="btn btn-primary"><?php echo $client_lang['goBackHome']; ?></a>
    </div>

    <!-- Lottie library -->
    <script src="https://cdnjs.cloudflare.com/ajax/libs/bodymovin/5.7.7/lottie.min.js"></script>

    <!-- Lottie animation script -->
    <script>
        // Replace 'failed.json' with the path to your Lottie animation JSON file
        const animationPath = 'failed.json';

        // Configure Lottie
        const animationContainer = document.getElementById('lottie-container');
        const animationOptions = {
            container: animationContainer,
            renderer: 'svg',
            loop: true,
            autoplay: true,
            path: animationPath,
        };

        // Load and play the animation
        const animation = lottie.loadAnimation(animationOptions);
    </script>

    <!-- Bootstrap and jQuery scripts -->
    <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.11.6/dist/umd/popper.min.js"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
</body>

</html>
<?php
include('includes/footer.php');
?>