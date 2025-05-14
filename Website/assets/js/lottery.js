const helperContainer = document.querySelector(".picker-style-helper");
const buyTicketButton = document.querySelector("#buy-ticket-submit-button");
let work = true;
const errorHandlerP = document.querySelector("#status-p-purchase");

function checkSelection()
{
    let enable = false;

    for (let i = 0; i < index; i++) {
        try {
            const normal = document.querySelectorAll(`#unique-${i} .silver`);
            const parent = document.querySelector(`#unique-${i}`);

            if ((parent != null) && ( normal.length < normal_ball_limit))
            {
                enable = false;
                break;
            }
            else
            {
                enable = true;
            }

        }
        catch { 
        }

        try {
            const premium = document.querySelectorAll(`#unique-${i} .gold`);
            const parent = document.querySelector(`#unique-${i}`);

            if ((parent != null) && ( premium.length < premium_ball_limit))
            {
                enable = false;
                break;
            }
            else
            {
                enable = true;
            }
        }
        catch { }
    }

    if (enable)
    {
        buyTicketButton.disabled = false;
        errorHandlerP.textContent = "";
    }
    else
    {
        errorHandlerP.textContent = selectTickets;
        errorHandlerP.className = "bad-status";
        buyTicketButton.disabled = true;
    }
}

function destroyMe(item) {
    if (work)
    {
        item.parentElement.remove();

        if (helperContainer.children.length == 1) {
            for (let i = 0; i < index; i++) {
                try {
                    document.querySelector(`.close-picker-button`).style.display = "none";
                }
                catch { }
            }
        }

        checkSelection();
    }
}

function getRandomInt(min, max) {
    min = Math.ceil(min);
    max = Math.floor(max);
    return Math.floor(Math.random() * (max - min + 1)) + min;
}

function deleteMe(item) {
    if (work)
    {
        const parentID = item.parentElement.getAttribute("data");

        try {
            for (let i = normal_ball_start; i <= normal_ball_end; i++) {
                document.querySelector(`#unique-${parentID} #unique-normal-${i}`).classList.remove("silver");
            }
        }
        catch {}

        try {
            for (let i = premium_ball_start; i <= premium_ball_end; i++) {
                document.querySelector(`#unique-${parentID} #unique-premium-${i}`).classList.remove("gold");
            }
        }
        catch {}

        checkSelection();
    }
}

function pickMe(item) {
    if (work)
    {
        const parentID = item.parentElement.getAttribute("data");

        for (let i = 0; i < 2; i++)
        {
            setTimeout(()=>{
                const random = [];
                try {
                    for (let i = normal_ball_start; i <= normal_ball_end; i++) {
                        document.querySelector(`#unique-${parentID} #unique-normal-${i}`).classList.remove("silver");
                    }
                }
                catch {}

                try {
                    for (let i = premium_ball_start; i <= premium_ball_end; i++) {
                        document.querySelector(`#unique-${parentID} #unique-premium-${i}`).classList.remove("gold");
                    }
                }
                catch {}

                try {
                    for (let i = 0; i < normal_ball_limit; i++) {
                        let randomNum = getRandomInt(normal_ball_start, normal_ball_end);
    
                        while (random.indexOf(randomNum) !== -1) {
                            randomNum = getRandomInt(normal_ball_start, normal_ball_end);
                        }
    
                        random.push(randomNum);
                        document.querySelector(`#unique-${parentID} #unique-normal-${randomNum}`).classList.add("silver");
                    }
                }
                catch {}

                try {
                    const pRandom = [];
    
                    for (let i = 0; i < premium_ball_limit; i++) {
                        let randomNum = getRandomInt(premium_ball_start, premium_ball_end);
    
                        while (pRandom.indexOf(randomNum) !== -1) {
                            randomNum = getRandomInt(premium_ball_start, premium_ball_end);
                        }
                        
                        pRandom.push(randomNum);
                        document.querySelector(`#unique-${parentID} #unique-premium-${randomNum}`).classList.add("gold");
                    } 
                }
                catch {}

                checkSelection();
            }, i * 50);
        }
    }
}

function selectOnlyMeNormal(item) {
    if (work)
    {
        if (item.classList.contains("silver")) {
            item.classList.remove("silver");
        }
        else {
            const parentID = item.getAttribute("data");
            const match = document.querySelectorAll(`#unique-${parentID} .silver`);
            
            if ((match == null) || (match.length < normal_ball_limit))
            {
                item.classList.add("silver");
            }
        }

        checkSelection();
    }
}

function selectOnlyMePremium(item) {
    if (work)
    {
        if (item.classList.contains("gold")) {
            item.classList.remove("gold");
        }
        else {
            const parentID = item.getAttribute("data");
            const match = document.querySelectorAll(`#unique-${parentID} .gold`);
            
            if ((match == null) || (match.length < premium_ball_limit))
            {
                item.classList.add("gold");
            }
        }

    checkSelection();
    }
}

$(document).ready(() => {
    const sliderContainer = document.querySelector("#slider-container");
    const slider = new PicturePreviewSlider(sliderContainer, sliderImages);

    const bidsSlider = document.querySelector("#animate-slider");
    setTimeout(() => {
        bidsSlider.style.width = bidsSlider.getAttribute("data") + "%";
    }, 100);

    const addMoreButton = document.querySelector("#add-button");
    const nTickets = document.querySelector("#n-tickets-summary");
    const totalPrice = document.querySelector("#total-price-summary");
    const calculatorTotal = document.querySelector("#calculate-tickets-summary");
    let numberOfTickets = 0;

    addMoreButton.addEventListener("click", () => {
        
        if (max_tickets > numberOfTickets)
        {
            numberOfTickets++
            
        data = {
            "request-type": "get-card",
            "normal_ball_start": normal_ball_start,
            "normal_ball_end": normal_ball_end,
            "premium_ball_start": premium_ball_start,
            "normal_ball_limit": normal_ball_limit,
            "premium_ball_limit": premium_ball_limit,
            "premium_ball_end": premium_ball_end,
            "index": index++
        };

        $.ajax({
            type: "post",
            url: "lottery_ajax.php",
            data: data,
            success: function (response) {
                $(".picker-style-helper").append(response);
                nTickets.textContent = helperContainer.children.length;
                totalPrice.textContent = perTicketPrice * helperContainer.children.length;
                calculatorTotal.textContent = `${perTicketPrice}x${helperContainer.children.length}`;

                document.querySelectorAll(".close-picker-button").forEach(element => {
                    element.style.display = "block";
                });

                if (helperContainer.children.length == 1)
                {
                    document.querySelector(".close-picker-button").style.display = "none";
                }

                checkSelection();
            },
            error: function (xhr, status, error) {
                alert("Something went wrong during process.");
            }
        });
        }
    });

    const deleteButton = document.querySelector(".delete-button");

    deleteButton.addEventListener("click", () => {
        const normal = document.querySelectorAll(".silver");
        const premium = document.querySelectorAll(".gold");

        normal.forEach(element => {
            element.classList.remove("silver");
        });

        premium.forEach(element => {
            element.classList.remove("gold");
        });

        checkSelection();
    });

    const quickPick = document.querySelector(".magic-button");

    quickPick.addEventListener("click", () => {
        for (let i = 0; i < index; i++) {
            try {
                document.querySelector(`#picker-${i}`).click();
            }
            catch { }
        }
    });

    nTickets.textContent = helperContainer.children.length;
    totalPrice.textContent = perTicketPrice * helperContainer.children.length;
    calculatorTotal.textContent = `${perTicketPrice}x${helperContainer.children.length}`;

    const previousPageBuyTicket = document.querySelector("#buy-ticket-button");
    const mainContainer = document.querySelector(".main-layout-handler-main-page");
    const secondaryContainer = document.querySelector(".secondary-page-handler");

    const ticketInput = document.querySelector("#ticket-input");
    const addTicket = document.querySelector("#add-ticket");
    const subtractTicket = document.querySelector("#subtract-ticket");

    addTicket.addEventListener("click", ()=>{
        if (Number(ticketInput.value) < Number(ticketInput.getAttribute("max")))
        {
            ticketInput.value = Number(ticketInput.value) + 1;
        }
    });

    subtractTicket.addEventListener("click", ()=>{
        if (Number(ticketInput.value) > 0)
        {
            ticketInput.value = Number(ticketInput.value) - 1;
        }
    });

    previousPageBuyTicket.addEventListener("click", ()=>{
        for (let i = 0; i < ticketInput.value; i++)
        {
            addMoreButton.click();
        }

        mainContainer.style.display = "none";
        secondaryContainer.style.display = "flex";
    });

    buyTicketButton.addEventListener("click", ()=>{
        const tickets = [];

        for (let i = 0; i < index; i++)
        {
            const balls = [];

            try {
                const premium = document.querySelectorAll(`#unique-${i} .gold`);
                if (premium.length > 0) balls.push(premium[0].textContent);
                if (premium.length > 1) balls.push(premium[1].textContent);
                if (premium.length > 2) balls.push(premium[2].textContent);
                if (premium.length > 3) balls.push(premium[3].textContent);
                if (premium.length > 4) balls.push(premium[4].textContent);
                if (premium.length > 5) balls.push(premium[5].textContent);
                if (premium.length > 6) balls.push(premium[6].textContent);
                if (premium.length > 7) balls.push(premium[7].textContent);
            }
            catch {}

            try {
                const normal = document.querySelectorAll(`#unique-${i} .silver`);

                if (normal.length > 0) balls.push(normal[0].textContent);
                if (normal.length > 1) balls.push(normal[1].textContent);
                if (normal.length > 2) balls.push(normal[2].textContent);
                if (normal.length > 3) balls.push(normal[3].textContent);
                if (normal.length > 4) balls.push(normal[4].textContent);
                if (normal.length > 5) balls.push(normal[5].textContent);
                if (normal.length > 6) balls.push(normal[6].textContent);
                if (normal.length > 7) balls.push(normal[7].textContent);
            }
            catch {}

            tickets.push(balls);
        }

        data = {"request-type": "insert-ticket", "tickets": tickets, "o_id": o_id, "ticket_price": perTicketPrice };

        $.ajax({
            type: "post",
            url: "lottery_ajax.php",
            data: data,
            success: function (response) {
                if (response.includes("334455-Done-Response"))
                {
                    errorHandlerP.textContent = successfulyOrderText;
                    errorHandlerP.className = "good-status"; 

                    const purchaseNotices = document.querySelectorAll(".purchase-done");

                    purchaseNotices.forEach((element)=>{
                        element.style.display = "block";
                    });
                    
                    // Redirect after 5 seconds
                    setTimeout(() => {
                        window.location.href = "ticket-purchases.php";
                    }, 4000);
                }
                else if (response.includes("MESSAGE-INSUFFICIENT-BALANCE-3322"))
                {
                    errorHandlerP.textContent = insufficientBalanceText;
                    errorHandlerP.className = "bad-status"; 
                }
                else {
                    window.location.href = "/login.php";
                }

                deleteButton.disabled = true;
                addMoreButton.disabled = true;
                buyTicketButton.disabled = true;
                work = false;
            },
            error: function (xhr, status, error) {
                alert("Something went wrong during process.");
            }
        });
    });

    checkSelection();
});
