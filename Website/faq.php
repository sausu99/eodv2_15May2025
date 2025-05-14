<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>FAQ - Answers to all your questions</title>
    <link rel="stylesheet" href="style.css">
    <style>
        body {
    font-family: Arial, sans-serif;
    margin: 0;
    padding: 20px;
}


section {
    margin-bottom: 20px;
}

h2 {
    color: #333;
}

/* Style the header with a grey background and some padding */
.header {
  overflow: hidden;
  background-color: #f1f1f1;
  padding: 20px 10px;
}

/* Style the header links */
.header a {
  float: left;
  color: black;
  text-align: center;
  padding: 12px;
  text-decoration: none;
  font-size: 18px;
  line-height: 25px;
  border-radius: 4px;
}

/* Style the logo link (notice that we set the same value of line-height and font-size to prevent the header to increase when the font gets bigger */
.header a.logo {
  font-size: 25px;
  font-weight: bold;
}

/* Change the background color on mouse-over */
.header a:hover {
  background-color: #ddd;
  color: black;
}

/* Style the active/current link*/
.header a.active {
  background-color: dodgerblue;
  color: white;
}

/* Float the link section to the right */
.header-right {
  float: right;
}

/* Add media queries for responsiveness - when the screen is 500px wide or less, stack the links on top of each other */
@media screen and (max-width: 500px) {
  .header a {
    float: none;
    display: block;
    text-align: left;
  }
  .header-right {
    float: none;
  }
}

.section-wrapper {
        max-width: 800px;
        margin: 0 auto;
        padding: 20px;
    }

    .section {
        margin-bottom: 40px;
        padding: 20px;
        border-radius: 8px;
        background-color: #f9f9f9;
        box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
    }

    .section h2 {
        color: #333;
        margin-bottom: 10px;
    }

    .section p {
        margin-bottom: 15px;
    }

    .section ul {
        list-style-type: disc;
        margin-left: 20px;
    }

    .cta-button {
        display: inline-block;
        padding: 10px 20px;
        background-color: dodgerblue;
        color: #fff;
        text-decoration: none;
        border-radius: 4px;
        transition: background-color 0.3s ease;
    }

    .cta-button:hover {
        background-color: #007bff;
    }

    .faq-wrapper {
        max-width: 800px;
        margin: 0 auto;
        padding: 20px;
    }

    .faq-section {
        margin-bottom: 40px;
    }

    .faq-item {
        margin-bottom: 20px;
    }

    .faq-question {
        position: relative;
        padding-right: 30px;
        cursor: pointer;
    }

    .faq-question::after {
        content: "\25B6"; /* Unicode for a right-pointing arrow */
        position: absolute;
        top: 50%;
        right: 0;
        transform: translateY(-50%);
    }

    .faq-answer {
        display: none;
        margin-top: 10px;
    }
    </style>
</head>
<body>
     <header class="header">
   
  <a href="index.php" class="logo">Frequently Asked Questions</a>
  <div class="header-right">
    <a href="#auctions">Auctions</a>
    <a href="#lottery">Lottery</a>
</div>
  </header>

  <main>
      <div class="faq-wrapper section-wrapper">
          <section id="auctions" class="auctions">
              <h2>Frequently Asked Questions</h2>
              <h3>Auction's FAQ</h3>
              <div class="faq-item">
                  <div class="faq-question">• How do I participate in an auction?</div>
                  <div class="faq-answer">To participate in an auction, simply follow the steps outlined on the auction page, including selecting a product, placing a bid, and completing the necessary payment.</div>
              </div>
              <div class="faq-item">
                  <div class="faq-question">• How do I purchase coins to particaipate in an auction?</div>
                  <div class="faq-answer">To purchase coins you can visit the coin shop inside the app.</div>
              </div>
              <div class="faq-item">
                  <div class="faq-question">• What all Payment Methods are available to purchase the coins?</div>
                  <div class="faq-answer">We have multiple payment gateway for you so that you can pay easily for coins.</div>
              </div>
               <div class="faq-item">
                  <div class="faq-question">• What is the validity of the coins?</div>
                  <div class="faq-answer">The coins you purchase doesn't expire.</div>
              </div>
               <div class="faq-item">
                  <div class="faq-question">• Can I ask for refund of unused coins?</div>
                  <div class="faq-answer">The coins you purchase can not be refunded.</div>
              </div>
              <div class="faq-item">
                  <div class="faq-question">• How do I see all the items available in the auctions?</div>
                  <div class="faq-answer">To participate in different items listed in the auctions, navigate to the homepage and there you can see all the items listed for the auction.</div>
              </div>
              
              <div class="faq-item">
                  <div class="faq-question">• How do I know if I've won an auction?</div>
                  <div class="faq-answer">If you win an auction, you will be notified via email or through the notification system within the auction platform. Additionally, you can check the auction results or your account dashboard for updates.</div>
              </div>
              
              <div class="faq-item">
                  <div class="faq-question">• Can I bid on multiple items simultaneously?</div>
                  <div class="faq-answer">Yes, you can bid on multiple items simultaneously as long as you have sufficient coins.</div>
              </div>
              
              <div class="faq-item">
                  <div class="faq-question">• What happens if I run out of coins?</div>
                  <div class="faq-answer">If you run out of coins, you won't be able to place bids in auctions until you purchase more coins from the coin shop.</div>
              </div>
              <div class="faq-item">
                  <div class="faq-question">• How long does an auction typically last?</div>
                  <div class="faq-answer">The duration of an auction can vary depending on the type of auction and the seller's preferences. Some auctions may last a few hours, while others may run for several days.</div>
              </div>
              
              <div class="faq-item">
                  <div class="faq-question">• Can I retract or cancel my bid?</div>
                  <div class="faq-answer">Bids placed in an auction are considered final and cannot be retracted or canceled. It's important to review your bids carefully before placing them to avoid any mistakes.</div>
              </div>
              
              <div class="faq-item">
                  <div class="faq-question">• Is there a limit to the number of bids I can place?</div>
                  <div class="faq-answer">No there is no limit on how many bids a user can place in any auction.</div>
              </div>
              
              <div class="faq-item">
                  <div class="faq-question">• How are auction winners determined?</div>
                  <div class="faq-answer">Auction winners are typically determined based on specific criteria outlined in the auction rules. For example, in a highest bid auction, the highest bid at the end of the auction period wins. In a lowest unique bid auction, the bidder with the lowest unique bid wins.</div>
              </div>
              
              <div class="faq-item">
                  <div class="faq-question">• What happens if there are technical issues during an auction?</div>
                  <div class="faq-answer">This usually did not occur but in case In the event of technical issues or disruptions during an auction,we may extend the auction duration or take other measures to ensure fairness and transparency. Users will typically be notified of any changes or updates regarding the auction.</div>
              </div>
              <!-- Add more FAQ items -->
          </section>
          <hr>
 <section id="lottery" class="lottery">
    <h3>Lottery's FAQ</h3>
        <div class="faq-item">
            <div class="faq-question">• How do I purchase lottery tickets?</div>
            <div class="faq-answer">To purchase lottery tickets, visit of our website or app and select the desired lottery game. Follow the prompts to choose your numbers or select a quick pick option, and then pay with coins to complete your purchase.</div>
        </div>
        
        <div class="faq-item">
            <div class="faq-question">• How are lottery winners determined?</div>
            <div class="faq-answer">Winning lottery numbers are determined through random drawing processes. The winning numbers are then announced publicly, and prizes are awarded to ticket holders who match the drawn numbers.</div>
        </div>

        <div class="faq-item">
            <div class="faq-question">• What happens if I win a lottery prize?</div>
            <div class="faq-answer">If you win a lottery prize, you will need to claim your prize.</div>
        </div>
        
         <div class="faq-item">
            <div class="faq-question">• What are the odds of winning the lottery?</div>
            <div class="faq-answer">The more tickets you purchase the more chances of you winning the lottery is possible.</div>
        </div>
        
        <div class="faq-item">
            <div class="faq-question">• Can I play the lottery in the App &amp; website?</div>
            <div class="faq-answer">Yes, you can play lottery based on your comfort.</div>
        </div>
              <!-- Add more FAQ items -->
          </section>
      </div>
  </main>

  <script>
  // JavaScript to toggle FAQ answers
  document.addEventListener('DOMContentLoaded', function() {
      const faqQuestions = document.querySelectorAll('.faq-question');
      faqQuestions.forEach(question => {
          question.addEventListener('click', () => {
              const answer = question.nextElementSibling;
              answer.style.display = answer.style.display === 'block' ? 'none' : 'block';
          });
      });
  });
  </script>
</body>
</html>
