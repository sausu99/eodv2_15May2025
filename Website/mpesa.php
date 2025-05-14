<?php
include_once('includes/header.php');
include("includes/session_check.php");

include_once('includes/function.php');
include_once('language/language.php');
include_once("includes/connection.php");


?>

<!DOCTYPE html>
<html lang="en">

<head>
    <title>MPESA Payment(Mozambique)</title>
    <style>
        body {
            font-family: 'Arial', sans-serif;
            margin: 0;
            padding: 0;
            background-color: #f4f4f4;
            /* Adding a light background color to the entire page */
        }

        .background-container {
            background-color: #fff;
            /* White background for the container */
            border-radius: 8px;
            /* Rounded corners for the container */
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
            /* A subtle shadow around the container */
            margin: 50px auto;
            /* Center the container with automatic horizontal margins */
            padding: 20px;
            width: 80%;
            /* Set the width of the container */
            max-width: 1200px;
            /* Maximum width of the container */
        }

        .item-details-container {
            display: flex;
            justify-content: space-between;
            border: 1px solid #ddd;
            border-radius: 8px;
        }

        .left-side {
            width: 50%;
        }

        .right-side {
            width: 50%;
            padding: 20px;
        }

        img {
            width: 50%;
            border-radius: 8px;
            margin-bottom: 20px;
        }

        h2 {
            font-size: 2em;
            margin-bottom: 10px;
        }

        p {
            font-size: 1.2em;
            color: #333;
        }

        .btn {
            padding: 10px 20px;
            font-size: 1.2em;
            border: none;
            border-radius: 5px;
            cursor: pointer;
        }

        .btn-success {
            background-color: #28a745;
            color: white;
        }

        .btn-block {
            display: block;
            width: 100%;
        }

        .form-check {
            margin-bottom: 10px;
        }

        .form-check-input {
            margin-right: 10px;
        }

        .form-check-label {
            font-size: 1.2em;
        }

        /* Page title style */
        h1 {
            text-align: center;
            color: #333;
        }


        .payment-methods {
            display: grid;
            grid-template-columns: repeat(auto-fill, minmax(150px, 1fr));
            gap: 10px;
            margin-bottom: 15px;
        }

        .payment-option {
            border: 1px solid #ddd;
            border-radius: 8px;
            background-color: #fff;
            box-shadow: 0px 0px 10px rgba(0, 0, 0, 0.1);
            transition: transform 0.3s ease;
            padding: 1px;
            justify-content: center;
        }

        .payment-option:hover {
            transform: translateY(-3px);
            box-shadow: 0px 5px 15px rgba(0, 0, 0, 0.2);
        }

        .payment-label {
            display: flex;
            align-items: center;
            justify-content: center;
            text-align: center;
            cursor: pointer;
        }

        .payment-logo-img {
            margin-bottom: 5px;
            height: 50px;
            width: auto;
        }

        .payment-name {
            font-size: 0.9em;
            text-transform: capitalize;
            margin-top: 5px;
        }

        .payment-selector {}

        /* Add the following code to highlight the selected payment option */
        .payment-option input[type="radio"]:checked+.payment-label {
            border: 2px solid #28a745;
            /* Adjust border style and color as needed */
        }

        /* Optional: Add a different style for the checked payment option */
        .payment-option input[type="radio"]:checked+.payment-label {
            background-color: #28a745;
            /* Change the background color when selected */
            color: white;
            /* Change the text color when selected */
        }
    </style>
</head>

<body>


    <div class="background-container">
        <!-- Container added here -->
        <h1>Mpesa (Mozambique)</h1> <!-- Page title -->
        <div class="alert alert-danger text-light " style="display: none;" id="info"></div>
        <div class="item-details-container">
            <div class="right-side">
                <form id="pmnt-form" action="payment-gateway/mpesa_mz/process_payment.php" method="post">

                    <input type="hidden" name="amount" id="amount" class="payment-selector"
                        value="<?php echo $_POST['amount']; ?>">
                    <input type="hidden" name="coin" id="coin" class="payment-selector"
                        value="<?php echo $_POST['coin']; ?>">
                    <input type="hidden" name="current_url" value="<?php echo $_POST['current_url']; ?>">
                    <hr>
                    <div class="payment-methods">
                        <label class="form-label">Phone Number:</label>
                        <input type="text" name="phone" class="form-control">
                    </div>




                    <button style="    width: 350px;" class="btn btn-success btn-lg btn-block" id="btn">Proceed</button>
            </div>
        </div>


        <script type="text/javascript" src="assets/js/vendor.js"></script>
        <script type="text/javascript" src="assets/js/app.js"></script>



        <script>

            $(function () {

                $("#pmnt-form").submit(function (e) {
                    e.preventDefault();
                    e.stopPropagation();


                    $("#btn").text("LOADING...")


                    var that = this;
                    var form = new FormData(this);
                    var url = this.getAttribute('action');



                    $.ajax({
                        type: "POST",
                        url: url,
                        data: form,
                        processData: false,
                        contentType: false,
                        success: function (response) {
                            if (response.includes("ok")) {

                                location.href = "thank_you.php";
                            } else {
                                $("#info").html(response);    $("#info").show();
                            }
                    $("#btn").text("Proceed");
                        },
                        error: function (errResponse) {
                            $("#info").html(errResponse);    $("#info").show();
                    $("#btn").text("Proceed");
                        }
                    });



                });//submit




            });



        </script>








    </div>
    <!-- ... your existing body content ... -->
</body>

</html>