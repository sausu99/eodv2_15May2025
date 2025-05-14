<?php
include('includes/function.php');
include('language/language.php');

// Function to get image based on colour
function get_image_by_colour($colour) {
    switch ($colour) {
        case '1':
            return 'blue.png';
        case '2':
            return 'dark_blue.png';
        case '3':
            return 'dark_pink.png';
        case '4':
            return 'green.png';
        case '5':
            return 'orange.png';
        case '6':
            return 'pink.png';
        case '7':
            return 'red.png';
        case '8':
            return 'teal_green.png';
        case '9':
            return 'violet.png';
        case '10':
            return 'yellow.png';
        default:
            return 'default.png';
    }
}

// Get the scratch card ID from the URL
$s_id = isset($_GET['s_id']) ? intval($_GET['s_id']) : 0;

if ($s_id <= 0) {
    echo "Invalid scratch card.";
    exit;
}

// Fetch the scratch card details from the database
$query = "SELECT * FROM tbl_scratch WHERE s_id = $s_id";
$result = mysqli_query($mysqli, $query);

if (mysqli_num_rows($result) == 0) {
    echo "Scratch card not found.";
    exit;
}

$scratch_card = mysqli_fetch_assoc($result);

// Get the image file based on the colour
$image_file = get_image_by_colour($scratch_card['s_colour']);
?>

<head>
    <link rel="stylesheet" href="assets/css/scratch_detail.css">
    <title><?php echo htmlspecialchars($scratch_card['s_name']); ?></title>
</head>

<style>
    /* CSS for rounded corners specific to scratch card images */
    .scratch-card-container {
        position: relative;
        width: 300px; /* Adjust as needed */
        height: 400px; /* Adjust as needed */
    }
    .scratch-card {
        border-radius: 25px; /* Adjust the radius as needed */
        overflow: hidden;
    }
    canvas.scratch-overlay {
        border-radius: 25px; /* Adjust the radius as needed */
    }
</style>

<div class="container">
    <h2><?php echo htmlspecialchars($scratch_card['s_name']); ?></h2>
    <p><?php echo htmlspecialchars($scratch_card['s_desc']); ?></p>

    <div class="scratch-card-container">
        <div class="scratch-card">
            <canvas id="scratchCanvas" class="scratch-overlay"></canvas>
        </div>
    </div>
</div>

<script>
document.addEventListener("DOMContentLoaded", function() {
    const canvas = document.getElementById('scratchCanvas');
    const context = canvas.getContext('2d');

    const imageSrc = '<?php echo '/images/static/scratchCard/' . $image_file; ?>';

    const prizeImage = new Image();
    prizeImage.src = imageSrc;
    prizeImage.onload = function() {
        const width = prizeImage.width;
        const height = prizeImage.height;

        canvas.width = width;
        canvas.height = height;

        // Draw rounded rectangle and clip to it
        const radius = 25; // Adjust the radius as needed
        context.beginPath();
        context.moveTo(radius, 0);
        context.lineTo(width - radius, 0);
        context.quadraticCurveTo(width, 0, width, radius);
        context.lineTo(width, height - radius);
        context.quadraticCurveTo(width, height, width - radius, height);
        context.lineTo(radius, height);
        context.quadraticCurveTo(0, height, 0, height - radius);
        context.lineTo(0, radius);
        context.quadraticCurveTo(0, 0, radius, 0);
        context.closePath();
        context.clip();

        context.drawImage(prizeImage, 0, 0, width, height);
        context.globalCompositeOperation = 'destination-out';
    };

    let isDrawing = false;

    function getMousePos(canvas, event) {
        const rect = canvas.getBoundingClientRect();
        return {
            x: event.clientX - rect.left,
            y: event.clientY - rect.top
        };
    }

    function scratch(event) {
        const pos = getMousePos(canvas, event);
        context.beginPath();
        context.arc(pos.x, pos.y, 20, 0, 2 * Math.PI);
        context.fill();
    }

    canvas.addEventListener('mousedown', function(event) {
        isDrawing = true;
        scratch(event);
    });

    canvas.addEventListener('mousemove', function(event) {
        if (isDrawing) {
            scratch(event);
        }
    });

    canvas.addEventListener('mouseup', function() {
        isDrawing = false;
    });

    canvas.addEventListener('mouseout', function() {
        isDrawing = false;
    });
});
</script>
