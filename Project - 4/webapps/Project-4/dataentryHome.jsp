<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Data Entry User Home</title>
    <link href="style.css" rel="stylesheet">
</head>
<body style="background-color: #262626; color: #ffffff; font-family: 'Roboto', sans-serif;">
     
    <!-- Welcome Section -->
    <section class="welcome-section" style="text-align: center;">
        <h1 style="color: #4CAF50; font-size: 42px;">Welcome to the Fall 2023 Project 4 Enterprise System</h1>
        <h2 style="color: #2196F3; font-size: 38px;">Data Entry Application</h2>
        <p style="font-size: 18px;">You are connected to the Project 4 Enterprise System database as a data-entry-level user. Enter the data values in a form below to add a new record to the corresponding database table.</p>
    </section>
    <hr style="border: 1px solid #ffffff;">

    <!-- Supplier Record Insert Section -->
    <section class="data-entry" style="text-align: center;">
        <h3 style="color: #FFC107;">Suppliers Record Insert</h3>
        <form action="SuppliersInsertServlet" method="post" id="supplierForm">
            <input type="text" id="snum" name="snum" placeholder="Supplier Number" required style="font-size: 18px; background-color: #263238; color: #FFF;">
            <input type="text" id="sname" name="sname" placeholder="Supplier Name" required style="font-size: 18px; background-color: #263238; color: #FFF;">
            <input type="text" id="status" name="status" placeholder="Status" required style="font-size: 18px; background-color: #263238; color: #FFF;">
            <input type="text" id="city" name="city" placeholder="City" required style="font-size: 18px; background-color: #263238; color: #FFF;">
            <button type="submit" style="font-size: 18px; background-color: #FF5722; color: white;">Enter Supplier Record Into Database</button>
        </form>
        <button onclick="clearDataAndResults('supplierForm')" style="font-size: 18px; background-color: #FF5722; color: white;">Clear Data and Results</button>
    </section>

    <!-- Parts Record Insert Section -->
    <section class="data-entry" style="text-align: center;">
        <h3 style="color: #FFC107;">Parts Record Insert</h3>
        <form action="PartsInsertServlet" method="post" id="partsForm">
            <input type="text" id="pnum" name="pnum" placeholder="Part Number" required style="font-size: 18px; background-color: #263238; color: #FFF;">
            <input type="text" id="pname" name="pname" placeholder="Part Name" required style="font-size: 18px; background-color: #263238; color: #FFF;">
            <input type="text" id="color" name="color" placeholder="Color" required style="font-size: 18px; background-color: #263238; color: #FFF;">
            <input type="text" id="weight" name="weight" placeholder="Weight" required style="font-size: 18px; background-color: #263238; color: #FFF;">
            <input type="text" id="part_city" name="part_city" placeholder="City" required style="font-size: 18px; background-color: #263238; color: #FFF;">
            <button type="submit" style="font-size: 18px; background-color: #FF5722; color: white;">Enter Part Record Into Database</button>
        </form>
        <button onclick="clearDataAndResults('partsForm')" style="font-size: 18px; background-color: #FF5722; color: white;">Clear Data and Results</button>
    </section>

    <!-- Jobs Record Insert Section -->
    <section class="data-entry" style="text-align: center;">
        <h3 style="color: #FFC107;">Jobs Record Insert</h3>
        <form action="JobsInsertServlet" method="post" id="jobsForm">
            <input type="text" id="jnum" name="jnum" placeholder="Job Number" required style="font-size: 18px; background-color: #263238; color: #FFF;">
            <input type="text" id="jname" name="jname" placeholder="Job Name" required style="font-size: 18px; background-color: #263238; color: #FFF;">
            <input type="text" id="numworkers" name="numworkers" placeholder="Number of Workers" required style="font-size: 18px; background-color: #263238; color: #FFF;">
            <input type="text" id="job_city" name="job_city" placeholder="City" required style="font-size: 18px; background-color: #263238; color: #FFF;">
            <button type="submit" style="font-size: 18px; background-color: #FF5722; color: white;">Enter Job Record Into Database</button>
        </form>
        <button onclick="clearDataAndResults('jobsForm')" style="font-size: 18px; background-color: #FF5722; color: white;">Clear Data and Results</button>
    </section>

    <!-- Shipments Record Insert Section -->
    <section class="data-entry" style="text-align: center;">
        <h3 style="color: #FFC107;">Shipments Record Insert</h3>
        <form action="ShipmentsInsertServlet" method="post" id="shipmentsForm">
            <input type="text" id="snum_ship" name="snum_ship" placeholder="Supplier Number" required style="font-size: 18px; background-color: #263238; color: #FFF;">
            <input type="text" id="pnum_ship" name="pnum_ship" placeholder="Part Number" required style="font-size: 18px; background-color: #263238; color: #FFF;">
            <input type="text" id="jnum_ship" name="jnum_ship" placeholder="Job Number" required style="font-size: 18px; background-color: #263238; color: #FFF;">
            <input type="text" id="quantity" name="quantity" placeholder="Quantity" required style="font-size: 18px; background-color: #263238; color: #FFF;">
            <button type="submit" style="font-size: 18px; background-color: #FF5722; color: white;">Enter Shipment Record Into Database</button>
        </form>
        <button onclick="clearDataAndResults('shipmentsForm')" style="font-size: 18px; background-color: #FF5722; color: white;">Clear Data and Results</button>
    </section>

    <hr style="border: 1px solid #ffffff;">

    <!-- Results Section -->
    <section id="results-section" class="results-section" style="text-align: center;">
        <h3 style="color: #FFC107;">Execution Results:</h3>
        <% 
            String message = (String) request.getAttribute("message");
            if (message != null && !message.isEmpty()) {
                out.println("<p>" + message + "</p>");
            } else {
                out.println("<p>Enter a Query.</p>");
            }
        %>
    </section>

    <script>
        function clearDataAndResults(formId) {
            document.getElementById(formId).reset();
            var resultsSection = document.getElementById("results-section");
            if (resultsSection) {
                resultsSection.innerHTML = '<h3 style="color: #FFC107;">Execution Results:</h3><p>Enter a Query.</p>';
            }
        }
    </script>
</body>
</html>
