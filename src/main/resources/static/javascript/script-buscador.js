
function cargarDatos() {
    var juegos = [
        ]
    return juegos;
}

// Función para buscar productos en tiempo real
function buscarProductos() {
    var query = document.getElementById("query").value.trim().toLowerCase();
    var resultadosList = document.getElementById("resultados-list");
    var juegos = cargarDatos();

    // Filtra los juegos que coinciden con la consulta
    var juegosFiltrados = juegos.filter(function (juego) {
        return juego.toLowerCase().includes(query);
    });

    // Limpia el datalist
    resultadosList.innerHTML = "";

    // Agrega las opciones al datalist
    juegosFiltrados.forEach(function (juego) {
        var option = document.createElement("option");
        option.value = juego;
        resultadosList.appendChild(option);
    });
}

// Evento que se dispara cuando se carga la página
window.onload = function () {
    // Asigna la función buscarProductos al evento "input" del campo de búsqueda
    var inputElement = document.getElementById("query");
    inputElement.addEventListener("input", buscarProductos);
};
