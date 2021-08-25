
var myFlight = {};



// DOMContentLoader fires only when entire html page has been rendered
document.addEventListener("DOMContentLoaded", function () {

  // swal("hello");

  

  // LOAD DATA FROM BACKEND
  loadDataFromBackEnd();

  // ------------------------------------------------------------------------------------------------
  // xxxxxxxxxxxxxxxxxxxxxxxxx DELETES ENTRY WHEN ROW DELETE BUTTON IS CLICKED xxxxxxxxxxxxxxxxxxxxxxxxx
  $(document).ready(function () {
    $("body").on("click", ".icon-remove", function(){
        // alert("You deleted: " + $(this).closest('tr'));
      
        console.log($(this).closest('tr').index());
        console.log("Flight number to delete: " + $(this).closest('tr').find('td:eq(4)').text());
        console.log("YO");

        let fnClicked = $(this).closest('tr').find('td:eq(4)').text();
        // let fnClicked = document.querySelector(this).closest('tr').querySelector('td:eq(4)').text();

        


        deleteByButtonClick = {
          flightNumber: fnClicked,
        };
    
        var xhrPost = new XMLHttpRequest();
        xhrPost.open("DELETE", "http://localhost:8080/flight-api/flights");
    
        // when request comes back with a successful response
        xhrPost.onreadystatechange = function () {
          // if post is coming back with ready state 4
          if (xhrPost.readyState === 4) {     
            console.log("Delete Request successfully sent....");
 
            swal({
              position: 'top-end',
              icon: 'success',
              title: 'Record with Flight Number ' + fnClicked + ' was Deleted!',
              showConfirmButton: false
            })
          }
        };
        console.log("Sending to the server... " + deleteByButtonClick);
        xhrPost.send(JSON.stringify(deleteByButtonClick));

        // remove row from table
        $(this).closest('tr').remove();
        
    });

  });


  // ------------------------------------------------------------------------------------------------
  // xxxxxxxxxxxxxxxxxxxxxxxxx  WHEN ROW UPDATE BUTTON IS CLICKED xxxxxxxxxxxxxxxxxxxxxxxxx
  $(document).ready(function () {
    $("body").on("click", ".icon-update", function(){
        
        console.log($(this).closest('tr').index());
        console.log("Flight number to updated: " + $(this).closest('tr').find('td:eq(4)').text());
        console.log("YO");

        document.getElementById("flightNumberUpdated").value = $(this).closest('tr').find('td:eq(4)').text();

        // Scroll to Update Flight div
        $('html,body').animate({
          scrollTop: $("#updateFlightsDiv").offset().top},
          'slow');

    });
    });




  

  // ------------------------------------------------------------------
  // ------------------- FIND FLIGHT BY FLIGHT NUMBER -----------------
 document.getElementById("findByFN").addEventListener("submit", function (e) {

  e.preventDefault();   // method cancels the event if it is cancelable, meaning that the default action that belongs to the event will not occur.

  var findByFlightNumber = document.getElementById("flightNumberToFind").value;

  console.log("INPUT IS: " +  findByFlightNumber);

  if (findByFlightNumber.length <=0 || findByFlightNumber == "") {
    // alert("TRY AGAIN!");

    swal({
      icon: 'error',
      title: 'Oops...',
      text: 'Something Went Wrong!',
      footer: '<a href="">Why do I have this issue?</a>'
    })
    return;
  }

  findMyFlightFN = {
    flightNumber: findByFlightNumber,
  };

  console.log("JSON to send: " + findMyFlightFN[flightNumber]);

  // GET
  var myFlight = []; // store flights from a backend
  var xhr = new XMLHttpRequest();
  xhr.open("GET", "http://localhost:8080/flight-api/flight?flightNumber=".concat(findByFlightNumber));
  xhr.onreadystatechange = function () {
    if (xhr.readyState === 4) {
      JSON.parse(xhr.responseText).forEach((element) => {
        // iterate through array

        swal({
          position: 'top-end',
          icon: 'success',
          title: 'Record Was Found!',
          showConfirmButton: false
        })

        console.log("-------------");
        console.log("FLIGHT FOUND BY FN: " + xhr.responseText);
        console.log("ITS TYPE IS: " + typeof xhr.responseText);
        console.log("-------------");
        
        updateFNTable(element);

        // reset the form
        document.getElementById("flightNumberToFind").value = null;

      });
    }
  };

  xhr.send();

  console.log("Received from server... " + myFlight);



});


  // ------------------------------------------------------------------
  // --------------------- ADD NEW FLIGHT ------------------------
  // execute function when submit is pressed
  document
    .getElementById("new-flight")
    .addEventListener("submit", function (e) {
      e.preventDefault(); // prevent browser to taking us to another page
      // because by default 'form submition' loads a new page

      var fromAirport = document.getElementById("fromAirport").value;
      var toAirport = document.getElementById("toAirport").value;
      var departure = document.getElementById("departure").value;
      var arrival = document.getElementById("arrival").value;
      var flightNumber = document.getElementById("flightNumber").value;

      if (fromAirport.length <=0 || toAirport.length <= 0) {
        // alert("TRY AGAIN!");
    
        swal({
          icon: 'error',
          title: 'Oops...',
          text: 'Something Went Wrong!',
          footer: '<a href="">Why do I have this issue?</a>'
        })
        return;
      }

      // store data (var: doc.value)
      myFlight = {
        fromAirport: fromAirport,
        toAirport: toAirport,
        departure: departure,
        arrival: arrival,
        flightNumber: flightNumber,
      };

      // console.log(myFlight);
      // console.log("TYPE OF " + departure + " IS " + typeof departure);

      // ----------- LOGIC TO SEND SOMETHING TO BACKEND USING AJAX --------
      // send xhr request to the backend and save "myFlight" in CopyOnArrayList
      var xhrPost = new XMLHttpRequest();
      xhrPost.open("POST", "http://localhost:8080/flight-api/flights");

      // when request comes back with a successful response
      xhrPost.onreadystatechange = function () {
        // if post is coming back with ready state 4
        if (xhrPost.readyState === 4) {
          // update table
          updateTable(myFlight);


          swal({
            position: 'top-end',
            icon: 'success',
            title: 'Record Was Added!',
            showConfirmButton: false
          })
          

          // reset the form
          document.getElementById("fromAirport").value = null;
          document.getElementById("toAirport").value = null;
          document.getElementById("departure").value = null;
          document.getElementById("arrival").value = null;
          document.getElementById("flightNumber").value = null;
        }
      };

      // send it to the backend
      // this will take js myFlight object, convert it into json and send it into http request body,
      // so that the servlet can handle it
      console.log(
        "sending this stuff to the server -> " + JSON.stringify(myFlight)
      );
      
      xhrPost.send(JSON.stringify(myFlight));
    });

  // });

  // ------------------------------------------------------------------
  // --------------------- UPDATE FLIGHT ------------------------
  // execute function when submit is pressed
  document
    .getElementById("update-flight")
    .addEventListener("submit", function (e) {
      e.preventDefault(); // prevent browser to taking us to another page
      // because by default 'form submition' loads a new page

      var fromAirportUpdated = document.getElementById("fromAirportUpdated")
        .value;
      var toAirportUpdated = document.getElementById("toAirportUpdated").value;
      var departureUpdated = document.getElementById("departureUpdated").value;
      var arrivalUpdated = document.getElementById("arrivalUpdated").value;
      var flightNumberUpdated = document.getElementById("flightNumberUpdated")
        .value;

        if (fromAirportUpdated.length <=0 || toAirportUpdated.length <= 0) {
          // alert("TRY AGAIN!");
      
          swal({
            icon: 'error',
            title: 'Oops...',
            text: 'Something Went Wrong!',
            footer: '<a href="">Why do I have this issue?</a>'
          })
          return;
        }

      // store data (var: doc.value)
      myFlightUpdated = {
        fromAirport: fromAirportUpdated,
        toAirport: toAirportUpdated,
        departure: departureUpdated,
        arrival: arrivalUpdated,
        flightNumber: flightNumberUpdated,
      };

      
  
      var xhrPost = new XMLHttpRequest();
      xhrPost.open("PUT", "http://localhost:8080/flight-api/flights");

      // when request comes back with a successful response
      xhrPost.onreadystatechange = function () {
        if (xhrPost.readyState === 4) {

          swal({
            position: 'top-end',
            icon: 'success',
            title: 'Record Was Updated!',
            showConfirmButton: false
          })

          // need to reupload the database data
          loadDataFromBackEnd();

          // reset the form
          document.getElementById("fromAirportUpdated").value = null;
          document.getElementById("toAirportUpdated").value = null;
          document.getElementById("departureUpdated").value = null;
          document.getElementById("arrivalUpdated").value = null;
          document.getElementById("flightNumberUpdated").value = null;
        }
      };

      xhrPost.send(JSON.stringify(myFlightUpdated));

      
    });


    // ====================================================================
    // ------------------- DELETE FLIGHT BY FLIGHT NUMBER -----------------
    document.getElementById("deleteByFN").addEventListener("submit", function (e) {

      e.preventDefault();

      var deleteByFlightNumber = document.getElementById("flightNumberToDelete").value;

      if (deleteByFlightNumber.length <=0 ) {
        // alert("TRY AGAIN!");
    
        swal({
          icon: 'error',
          title: 'Oops...',
          text: 'Something Went Wrong!',
          footer: '<a href="">Why do I have this issue?</a>'
        })
        return;
      }
  
      deleteByFlightFN = {
        flightNumber: deleteByFlightNumber,
      };
  
      var xhrPost = new XMLHttpRequest();
      xhrPost.open("DELETE", "http://localhost:8080/flight-api/flights");
  
      // when request comes back with a successful response
      xhrPost.onreadystatechange = function () {
        // if post is coming back with ready state 4
        if (xhrPost.readyState === 4) {     
          console.log("Delete Request successfully sent....");

          swal({
            position: 'top-end',
            icon: 'success',
            title: 'Record with Flight Number *' + deleteByFlightNumber + ' was Deleted!',
            showConfirmButton: false
          })


          loadDataFromBackEnd();

          // reset the form
          document.getElementById("flightNumberToDelete").value = null;
        }
      };
      console.log("Sending to the server... " + deleteByFlightFN);
      xhrPost.send(JSON.stringify(deleteByFlightFN));
  
  
    });


});




// ********************** GET ALL DATA FROM BACKEND *********************
var loadDataFromBackEnd = function() {
  var flights = []; // store flights from a backend
  var xhr = new XMLHttpRequest();
  xhr.open("GET", "http://localhost:8080/flight-api/flights");
  xhr.onreadystatechange = function () {
    // when GET request comes back successfully
    // if comes back successfully
    if (xhr.readyState === 4) {
      // populate table  (parse (turn) json into array object)
      JSON.parse(xhr.responseText).forEach((element) => {
        // iterate through array
        // console.log("-------------");
        // console.log("FOUND: " + xhr.responseText);
        // console.log("-------------");

        flights.push(element);
        // console.log(flights);
        updateTable(element);
      });
    }
  };

  xhr.send();
};

/*
*
*
*                         HANDLE DATA IN TABLES 
*
*
*
*/


// =============================== CREATE ELEMENTS IN THE DOM ================================
var updateTable = function (flight) {
  // create table row
  var row = document.createElement("TR");

  // create columns with table data
  var fromAirportColumn = document.createElement("TD");
  var toAirportColumn = document.createElement("TD");
  var departureColumn = document.createElement("TD");
  var arrivalColumn = document.createElement("TD");
  var flightNumberColumn = document.createElement("TD");

  var edtCol = document.createElement("TD");    // ***************
  var rmvCol = document.createElement("TD");    // ***************

  // fill in inner text with flight data
  fromAirportColumn.innerText = flight.fromAirport;
  toAirportColumn.innerText = flight.toAirport;
  departureColumn.innerText = flight.departure;
  arrivalColumn.innerText = flight.arrival;
  flightNumberColumn.innerText = flight.flightNumber;

  // rmvCol.innerHTML = '<button class="btn-remove">Remove</button>';    // **************
  edtCol.innerHTML = '<ion-icon name="create-outline" class="icon-update"></ion-icon>';
  rmvCol.innerHTML = '<ion-icon name="trash-outline" class="icon-remove"></ion-icon>';
  // <ion-icon name="cut-outline"></ion-icon>

  // check if they're not empty
  if (
    fromAirportColumn.innerText.length > 0 &&
    toAirportColumn.innerText.length > 0 &&
    departureColumn.innerText.length > 0 &&
    arrivalColumn.innerText.length > 0 &&
    flightNumberColumn.innerText.length > 0
  ) {
    // store table row with table datas
    row.appendChild(fromAirportColumn);
    row.appendChild(toAirportColumn);
    row.appendChild(departureColumn);
    row.appendChild(arrivalColumn);
    row.appendChild(flightNumberColumn);

    row.appendChild(edtCol);    // **************
    row.appendChild(rmvCol);    // **************

    /* 
    */



    // append row to the table  (table is parent element of TR)
    document.getElementById("flight-table").appendChild(row);

  }
};

// =============================== TABLE FOR FOUND FLIGHT BY FLIGHT NUMBER ================================
var updateFNTable = function (flight) {
  // create table row
  var row = document.createElement("TR");

  // create columns with table data
  var fromAirportColumn = document.createElement("TD");
  var toAirportColumn = document.createElement("TD");
  var departureColumn = document.createElement("TD");
  var arrivalColumn = document.createElement("TD");
  var flightNumberColumn = document.createElement("TD");

  // fill in inner text with flight data
  fromAirportColumn.innerText = flight.fromAirport;
  toAirportColumn.innerText = flight.toAirport;
  departureColumn.innerText = flight.departure;
  arrivalColumn.innerText = flight.arrival;
  flightNumberColumn.innerText = flight.flightNumber;

  // check if they're not empty
  if (
    fromAirportColumn.innerText.length > 0 &&
    toAirportColumn.innerText.length > 0 &&
    departureColumn.innerText.length > 0 &&
    arrivalColumn.innerText.length > 0 &&
    flightNumberColumn.innerText.length > 0
  ) {
    // store table row with table datas
    row.appendChild(fromAirportColumn);
    row.appendChild(toAirportColumn);
    row.appendChild(departureColumn);
    row.appendChild(arrivalColumn);
    row.appendChild(flightNumberColumn);

    // append row to the table  (table is parent element of TR)
    document.getElementById("flight-table-by-fn").appendChild(row);
  }
};






// AOS.init({
//   delay: 200, // values from 0 to 3000, with step 50ms
//   duration: 2000, // values from 0 to 3000, with step 50ms
//   once: false, // whether animation should happen only once - while scrolling down
//   mirror: false, // whether elements should animate out while scrolling past them
// });

// function toggleNavbar(collapseID) {
//   document.getElementById(collapseID).classList.toggle("hidden");
//   document.getElementById(collapseID).classList.toggle("block");
// }





