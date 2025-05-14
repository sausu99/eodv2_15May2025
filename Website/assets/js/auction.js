const sliderContainer = document.querySelector("#slider-container");

const slider = new PicturePreviewSlider(sliderContainer, sliderImages);

function openTab(evt, tabName) {
    var i, tabcontent, tablinks;
    tabcontent = document.getElementsByClassName("tabcontent");
    for (i = 0; i < tabcontent.length; i++) {
        tabcontent[i].style.display = "none";
    }
    tablinks = document.getElementsByClassName("tablinks");
    for (i = 0; i < tablinks.length; i++) {
        tablinks[i].className = tablinks[i].className.replace(" active", "");
    }
    document.getElementById(tabName).style.display = "block";
    evt.currentTarget.className += " active";
}

function filterCurrentUserBids(e) {
    var entries = document.getElementsByClassName('bid-entry');
    for (var i = 0; i < entries.length; i++) {
        if (entries[i].querySelector('.user-name').id != String(currentUser)) {
            if (e.checked)
            {
                entries[i].style.display = 'none';
            }
            else
            {
                entries[i].style.display = 'flex';
            }
        }
    }
}

$(document).ready(() => {
    document.querySelectorAll(".tablinks")[0].click();

    $('#place-bid-form').on('submit', function(event) {
        event.preventDefault();

        var form = $(this);
        var url = form.attr('action');

        $.ajax({
            type: form.attr('method'),
            url: url,
            data: form.serialize(),
            success: function(response) {
                $('#place-bid-message').text(response);
                document.querySelector('#place-bid-message').classList.add("good-state");
                
                // Refresh the page after 5 seconds
                setTimeout(() => {
                    location.reload();
                }, 5000); // 5000 milliseconds = 5 seconds
            },
            error: function(xhr, status, error) {
                $('#place-bid-message').text('An error occurred: ' + error);
                document.querySelector('#place-bid-message').classList.add("bad-state");
                
                // Refresh the page after 5 seconds
                setTimeout(() => {
                    location.reload();
                }, 5000); // 5000 milliseconds = 5 seconds
            }
        });
    });
});