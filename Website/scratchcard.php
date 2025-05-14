<?php

include("includes/connection.php");
require("includes/function.php");
require("language/language.php");

if (!isset($_SESSION['user_id'])) {
    header( "Location:login.php");
    exit;
}

ini_set('display_errors', '1');
ini_set('display_startup_errors', '1');
error_reporting(E_ALL);

if(isset($_SESSION['user_id'])) {
    $qry = "SELECT * FROM tbl_users WHERE id='".$_SESSION['user_id']."'";
    $result = mysqli_query($mysqli, $qry);
    $row = mysqli_fetch_assoc($result);
}

$user_id = $_SESSION['user_id'];

$sql = "SELECT * FROM tbl_scratch WHERE u_id = ?";
$stmt = $mysqli->prepare($sql);

if (!$stmt) {
    die("Prepare failed: " . $mysqli->error);
}

$stmt->bind_param("i", $user_id);
$stmt->execute();
$result = $stmt->get_result();

$image_map = [
    1 => 'images/static/scratchCard/blue.png',
    2 => 'images/static/scratchCard/dark_blue.png',
    3 => 'images/static/scratchCard/dark_pink.png',
    4 => 'images/static/scratchCard/green.png',
    5 => 'images/static/scratchCard/orange.png',
    6 => 'images/static/scratchCard/pink.png',
    7 => 'images/static/scratchCard/red.png',
    8 => 'images/static/scratchCard/teal_green.png',
    9 => 'images/static/scratchCard/violet.png',
    10 => 'images/static/scratchCard/yellow.png',
];

$available_scratch_cards = [];
$already_scratched_cards = [];

while ($row = $result->fetch_assoc()) {
    if ($row['s_status'] == 1) {
        $available_scratch_cards[] = $row;
    } else {
        $already_scratched_cards[] = $row;
    }
}

$stmt->close();
$mysqli->close();
?>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Dynamic Scratch Card Grid</title>
   <link rel="stylesheet" href="assets/css/scratch.css">

</head>
<body>
    <div class="container">
    <div class="scratch-card-section">
    <h3>Available Scratch Cards</h3>
    <div class="scratch-card-grid" id="available-scratch-cards">
        <?php foreach ($available_scratch_cards as $card): ?>
            <div class="scratch-card-item" data-id="<?php echo $card['s_id']; ?>" data-status="<?php echo $card['s_status']; ?>">
                <img src="<?php echo $image_map[$card['s_colour']]; ?>" alt="Scratch Card">
                <div class="label available-label">Scratch Here</div>
            </div>
        <?php endforeach; ?>
    </div>
</div>

<div class="scratch-card-section">
    <h3>Already Scratched</h3>
    <div class="scratch-card-grid" id="already-scratched-cards">
        <?php foreach ($already_scratched_cards as $card): ?>
            <div class="scratch-card-item" data-id="<?php echo $card['s_id']; ?>" data-status="<?php echo $card['s_status']; ?>">
                <img src="<?php echo $image_map[$card['s_colour']]; ?>" alt="Scratch Card">
                <div class="label scratched-label">Scratched</div>
            </div>
        <?php endforeach; ?>
    </div>
</div>


    <!-- The Modal -->
    <div id="scratchCardModal" class="modal">
        <div class="modal-content">
            <div class="modal-header">
                <h2 id="modal-title"></h2>
                <span class="close">&times;</span>
            </div>
            <div id="modal-scratch-container">
                <canvas id="modal-scratch-card"></canvas>
                <div id="modal-description"></div>
            </div>
        </div>
        <div id="fireworks-container" class="hidden"></div>

    </div>
    </div>

    <script>
   function createFireworks() {
    var container = document.getElementById('fireworks-container');
    container.innerHTML = ''; // Clear previous fireworks

    for (var i = 0; i < 20; i++) {
        var firework = document.createElement('div');
        firework.className = 'firework';
        firework.style.width = Math.random() * 20 + 10 + 'px';
        firework.style.height = firework.style.width;
        firework.style.left = Math.random() * 100 + '%';
        firework.style.top = Math.random() * 100 + '%';
        firework.style.animationDuration = Math.random() * 0.5 + 0.5 + 's';
        firework.style.background = `radial-gradient(circle, #${Math.floor(Math.random()*16777215).toString(16)}, transparent)`;
        container.appendChild(firework);
    }

    // Hide fireworks after animation
    setTimeout(function() {
        container.innerHTML = '';
    }, 1000);
}

document.addEventListener("DOMContentLoaded", function() {
    var cards = <?php echo json_encode($available_scratch_cards); ?>;
    var imageMap = <?php echo json_encode($image_map); ?>;
    var autoRevealThreshold = 0.6;

    // Modal functionality
    var modal = document.getElementById("scratchCardModal");
    var modalContent = document.querySelector(".modal-content");
    var span = document.getElementsByClassName("close")[0];

    document.querySelectorAll('.scratch-card-item').forEach(function(item) {
        item.addEventListener('click', function() {
            var cardId = item.getAttribute('data-id');
            var cardData = cards.find(card => card.s_id == cardId);
            if (cardData) {
                openModal(cardData);
            }
        });
    });

    var revealed = false;

    span.onclick = function() {
        modal.style.display = "none";
        if (revealed) {
            setTimeout(function() {
                location.reload();
            }, 200);
        }
    }

    window.onclick = function(event) {
        if (event.target == modal) {
            modal.style.display = "none";
        }
        if (event.target == modal && revealed) {
            modal.style.display = "none";
            setTimeout(function() {
                location.reload();
            }, 200);
        }
    }

    function openModal(card) {
        var modalCanvas = document.getElementById('modal-scratch-card');
        var modalCtx = modalCanvas.getContext('2d');
        var coverImage = new Image();
        coverImage.src = imageMap[card.s_colour];

        modalCanvas.width = 150;
        modalCanvas.height = 150;

        coverImage.onload = function() {
            modalCtx.drawImage(coverImage, 0, 0, modalCanvas.width, modalCanvas.height);
        };

        var modalTitle = document.getElementById('modal-title');
        var modalDescription = document.getElementById('modal-description');

        modalTitle.textContent = ''; // Clear previous title
        modalDescription.innerHTML = ''; // Clear previous description

        modalDescription.style.visibility = 'hidden';
        modalTitle.style.visibility = 'hidden';

        modal.style.display = "flex";

        var isDrawing = false;

        // Mouse events
        modalCanvas.addEventListener('mousedown', startScratch);
        modalCanvas.addEventListener('mousemove', scratch);
        modalCanvas.addEventListener('mouseup', endScratch);

        // Touch events
        modalCanvas.addEventListener('touchstart', startScratch);
        modalCanvas.addEventListener('touchmove', scratch);
        modalCanvas.addEventListener('touchend', endScratch);

        function startScratch(e) {
            isDrawing = true;
            scratch(e);
        }

        function endScratch() {
            isDrawing = false;
            if (!revealed && isThresholdReached()) {
                autoReveal();
            }
        }

        function scratch(e) {
            if (!isDrawing) return;

            e.preventDefault(); // Prevent scrolling on touch

            var rect = modalCanvas.getBoundingClientRect();
            var x, y;
            if (e.type.startsWith('touch')) {
                var touch = e.touches[0] || e.changedTouches[0];
                x = touch.clientX - rect.left;
                y = touch.clientY - rect.top;
            } else {
                x = e.clientX - rect.left;
                y = e.clientY - rect.top;
            }

            var radius = 15;

            modalCtx.globalCompositeOperation = 'destination-out';
            modalCtx.beginPath();
            modalCtx.arc(x, y, radius, 0, 2 * Math.PI);
            modalCtx.fill();
        }

        function isThresholdReached() {
            var imageData = modalCtx.getImageData(0, 0, modalCanvas.width, modalCanvas.height);
            var totalPixels = imageData.width * imageData.height;
            var clearPixels = 0;

            for (var i = 0; i < imageData.data.length; i += 4) {
                if (imageData.data[i + 3] === 0) {
                    clearPixels++;
                }
            }

            var scratchedRatio = clearPixels / totalPixels;
            return scratchedRatio >= autoRevealThreshold;
        }

        function autoReveal() {
            revealed = true;
            modalCtx.clearRect(0, 0, modalCanvas.width, modalCanvas.height);
            modalDescription.style.visibility = 'visible';
            modalTitle.style.visibility = 'visible';
            // span.style.display = 'none';
            createFireworks();
            updateCardStatus(card.s_id);

            // Update modal content based on card type
            switch (card.s_type) {
                case 1:
                    modalTitle.textContent = 'You won!';
                    modalDescription.innerHTML = `<div>${card.s_name}</div><div>${card.s_name} added to your account</div>`;
                    break;
                case 2:
                    modalTitle.textContent = 'You won!';
                    modalDescription.innerHTML = `<div>${card.s_name}</div><button id="redeem-button">Redeem</button>`;
                    break;
                case 3:
                    modalTitle.textContent = 'Coupon Card!';
                    modalDescription.innerHTML = `<div>Coupon Code: ${card.s_code}</div><div>${card.s_name}</div><div>${card.s_desc}</div><button id="redeem-button">Redeem</button>`;
                    break;
            }
        }

        function updateCardStatus(s_id) {
            var xhr = new XMLHttpRequest();
            xhr.open('POST', 'update_scratchcard.php', true);
            xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
            xhr.onreadystatechange = function () {
                if (xhr.readyState === 4 && xhr.status === 200) {
                    console.log(xhr.responseText);
                }
            };
            xhr.send('s_id=' + encodeURIComponent(s_id));
        }
    }
});
</script>
  
</body>
</html>
