<?php

if ($_SERVER["REQUEST_METHOD"] == "POST") {
    include_once 'service/PositionService.php';
    showPositions();
}

function showPositions() {
    $cs = new PositionService();
    $response = array("positions" => $cs->getAll());
    header('Content-type: application/json');
    echo json_encode($response);
}
