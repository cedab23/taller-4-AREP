async function register() {
    var jockey = document.getElementById("jockey").value;
    var horse = document.getElementById("horse").value;
    var strat = document.getElementById("strat").value;


    if (!jockey || !horse || !strat) {
        alert("Se deben rellenar todos los campos");
        return;
    }

    var data = {
        jockey: jockey,
        horse: horse,
        strategy: strat
    };

    await sendRegister(data);

}

async function sendRegister(data) {
    var response = await fetch("http://localhost:5000/api/register", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(data)
    });
    if (!response.ok) {
        alert("Ocurrio un error inesperado en el registro, por favor intente de nuevo");
        return
    }
    var result = await response.json();
    alert(result.result);

}

async function getData() {
    var response = await fetch("http://localhost:5000/api/table", {
        method: "GET",
        headers: {
            "Content-Type": "application/json"
        }
    });
    if (!response.ok) {
        alert("Ocurrio un error inesperado en la tabla, por favor intente mas tarde");
        return
    }
    var result = await response.json();
    tableHorses(result);
}

function tableHorses(horseJockey) {
    const tabla = document.getElementById("horses");
    const tbody = tabla.querySelector("tbody");
    tbody.innerHTML = "";

    if (!horseJockey || horseJockey.length === 0) {
        tbody.innerHTML = "<tr><td colspan='3'>No hay caballos registrados.</td></tr>";
        return;
    }

    horseJockey.forEach(horse => {
        const row = document.createElement("tr");
        row.innerHTML = `
            <td>${horse.jockey}</td>
            <td>${horse.horse}</td>
            <td>${horse.strat || horse.strategy}</td>
        `;
        tbody.appendChild(row);
    });
}
