function conexion(){

    fetch("./data/data.xml")
    .then((response) => response.text())
    .then((data) =>{

        const parser = new DOMParser();
        const xml = parser.parseFromString(data, "application/xml");
        const alimentos = xml.getElementsByTagName("alimento");
        console.log(alimentos);
    })
};

conexion();