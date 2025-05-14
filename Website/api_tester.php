<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>API Test Form</title>
</head>
<body>
    <form action="" method="post" id="apiTestForm">
        <label for="endpoint">Enter Endpoint:</label>
        <input type="text" id="endpoint" value="get_offers_id" name="endpoint" required><br><br>

        <label for="user_id">user_id:</label>
        <input type="text" id="user_id" name="user_id"><br><br>
        
        <label for="u_id">u_id:</label>
        <input type="text" id="u_id" name="u_id"><br><br>
        
        <label for="city_id">city_id:</label>
        <input type="text" id="city_id" name="city_id"><br><br>

        <label for="o_id">o_id:</label>
        <input type="text" id="o_id" name="o_id"><br><br>

        <input type="submit" value="Send">
    </form>

    <script>
        document.getElementById('apiTestForm').addEventListener('submit', function(e) {
            var endpoint = document.getElementById('endpoint').value;
            if (endpoint) {
                this.action = "https://prizex.wowcodes.in/seller/api.php?" + endpoint;
            } else {
                e.preventDefault();
                alert('Please enter an endpoint.');
            }
        });
    </script>
</body>
</html>
