function conexion(){

    fetch("./data/data.xml")
    .then((response) => response.text())
    .then((data) =>{

        const parser = new DOMParser();
        const xml = parser.parseFromString(data, "application/xml");
        const alimentos = xml.getElementsByTagName("alimento");
        function filtrarNombre(nombreAlimento){
            for (let i = 0; i < alimentos.length; i++) {
                const alimento = alimentos[i].getElementsByTagName("nombre");
                const nombre = alimento[0].textContent;
                if (nombre === nombreAlimento) {
                    console.log("Nombre:", nombre);
                    console.log("Kcal:", alimentos[i].getElementsByTagName("kcal")[0].textContent);
                    console.log("Proteínas", alimentos[i].getElementsByTagName("proteinas")[0].textContent);
                    console.log("Carbohidratos", alimentos[i].getElementsByTagName("carbohidratos")[0].textContent);
                    console.log("Grasas:", alimentos[i].getElementsByTagName("grasas")[0].textContent)
                }
            }
        };

        function añadirAlimento(newNombre, newKcal, newProteinas, newCarbohidratos, newGrasas){
            const nuevoAlimento = xml.createElement("alimento");
            const id_alimento = xml.createElement("id_alimento");
            const nombre = xml.createElement("nombre");
            const kcal = xml.createElement("kcal");
            const proteinas = xml.createElement("proteinas");
            const carbohidratos = xml.createElement("carbohidratos");
            const grasas = xml.createElement("grasas");
            id_alimento.innerText(alimentos.length + 1);
            nombre.innerText(newNombre);
            kcal.innerText(newKcal);
            proteinas.innerText(newProteinas);
            carbohidratos.innerText(newCarbohidratos);
            grasas.innerText(newGrasas);
            nuevoAlimento.appendChild(id_alimento);
            nuevoAlimento.appendChild(nombre);
            nuevoAlimento.appendChild(kcal);
            nuevoAlimento.appendChild(proteinas);
            nuevoAlimento.appendChild(carbohidratos);
            nuevoAlimento.appendChild(grasas);
            alimentos.appendChild(nuevoAlimento);
        };

        filtrarNombre("Avena");
        añadirAlimento("Almendras Fritas", 600, 18.4, 40.3, 30.9);
        filtrarNombre("Almendras Fritas");
    })
};

conexion();