<?php include("language/language.php");
?>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Reviews</title>
    <link rel="stylesheet" href="style.css">
</head>
<style>
    body {
    font-family: Arial, sans-serif;
    margin: 0;
    padding: 0;
}

header {
    background-color: #333;
    color: #fff;
    padding: 20px;
    text-align: center;
}

h1 {
    margin: 0;
}

#reviews {
    padding: 20px;
}

.review {
    border: 1px solid #ccc;
    border-radius: 5px;
    padding: 10px;
    margin-bottom: 10px;
}

.review h2 {
    margin-top: 0;
}

.review p {
    margin-bottom: 0;
}

</style>
<body>
    <header>
        <h1><?php echo $footer_lang['footerReview']; ?></h1>
    </header>
    
    <section id="reviews">
        <!-- Reviews will be loaded dynamically using JavaScript -->
    </section>

    <script>
    
    document.addEventListener('DOMContentLoaded', function() {
    // Simulated reviews data
    const reviewsData = [
    { name: 'Divya', rating: 5, comment: 'I won an amazing prize! Thank you so much!' },
    { name: 'Jack', rating: 5, comment: 'Absolutely fantastic prizes! I\'m thrilled!' },
    { name: 'Chetan', rating: 4.5, comment: 'Great website! Won a prize I\'ve been dreaming of!' },
    { name: 'Sonali', rating: 5, comment: 'The prizes are incredible! I\'m overjoyed!' },
    { name: 'Preeti', rating: 4.8, comment: 'I won a prize beyond my expectations! Thank you!' },
    { name: 'Sneha', rating: 4, comment: 'Good selection of prizes. I\'m happy with my win!' },
    { name: 'Krishna', rating: 4.7, comment: 'Excellent website! Won something I really wanted!' },
    { name: 'Jayesh', rating: 5, comment: 'I won the grand prize! Unbelievable! Thank you!' },
    { name: 'Isabella', rating: 4.5, comment: 'The prizes are top-notch! I\'m impressed!' },
    { name: 'Peeter', rating: 4.3, comment: 'Great experience overall! Happy with my win!' }
];


    // Function to display reviews
    function displayReviews() {
        const reviewsContainer = document.getElementById('reviews');
        reviewsContainer.innerHTML = '';

        reviewsData.forEach(review => {
            const reviewElement = document.createElement('div');
            reviewElement.classList.add('review');
            reviewElement.innerHTML = `
                <h2>${review.name} - ${review.rating} stars</h2>
                <p>${review.comment}</p>
            `;
            reviewsContainer.appendChild(reviewElement);
        });
    }

    // Call the function to display reviews
    displayReviews();
});
</script>
</body>
</html>
