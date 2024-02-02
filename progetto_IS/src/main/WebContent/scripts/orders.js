let pathArray = window.location.pathname.split('/');
let contextPath = '/' + pathArray[1];
let url = contextPath + "/OttieniStoricoOrdini";
let pagina_corrente;
function dynamicOrdersView(url, page, itemsPerPage = 10) {
    $.ajax({
        url: url,
        type: 'GET',
        contentType: 'application/json; charset=utf-8'
    }).done((response) => {
        response = JSON.parse(response);
        const totalItems = response.length;
        const totalPages = Math.ceil(totalItems / itemsPerPage);
        const startIndex = (page - 1) * itemsPerPage;
        const endIndex = startIndex + itemsPerPage;
        const paginatedData = response.slice(startIndex, endIndex);

        let contenutoHTML = "";
        let contenutoHTML2 = "";

        if (paginatedData.length === 0) {
            contenutoHTML += "<span>Nessun ordine disponibile</span>";
        } else {
            for (const bean of paginatedData) {
                contenutoHTML += "<div class=\"order-card\">";
                contenutoHTML += "<h2 id=\"idOrdine\"> Ordine:#" + bean.idOrdine + "</h2>";
                contenutoHTML += "<p>Cliente:<div class=\"email\">" + bean.idUtente + "</div></p>";
                contenutoHTML += "<p>Data:<div class=\"data\">" + bean.data + "</div></p>";
                contenutoHTML += "<p>Stato:" + bean.stato + "</p>";
                contenutoHTML += "Cambia stato ordine: "
                contenutoHTML += "<select name=\"ruolo\" id=\"ruolo\" onChange=\"cambiaStato('" + bean.idOrdine + "', this.value)\">";
                contenutoHTML += "<option value=" + bean.stato + ">" + bean.stato + "</option>";
                if (bean.stato == "IN ELABORAZIONE")
                    contenutoHTML += "<option value=\"IN CONSEGNA\">IN CONSEGNA</option>";
                else if (bean.stato == "IN CONSEGNA")
                    contenutoHTML += "<option value=\"CONSEGNATO\">CONSEGNATO</option>";
                contenutoHTML += "</select>"
                contenutoHTML += "<p>Stato:" + bean.indirizzo + "</p>";
                contenutoHTML += "<p>Totale:" + bean.prezzototale.toFixed(2) + "&euro;</p>";
                contenutoHTML += "<p>Metodo di pagamento: Carta di credito</p>";
                if (bean.stato == "RIMOSSO");
                else
                    contenutoHTML += "	<p> <button id='" + bean.idOrdine + "' onclick=\"eliminaRiga(this, '" + bean.stato + "')\"> Per rimuovere l'ordine clicca qui!</button>";
                contenutoHTML += "</div>";
            }
            renderPagination(totalPages, page);
        }

        $("#orders").empty().append(contenutoHTML);
        $("#bottom").empty().append(contenutoHTML2);
    });
}

function renderPagination(totalPages, currentPage) {
    const pagination = $("#pagination");
    pagination.empty();

    for (let i = 1; i <= totalPages; i++) {
        const li = $("<li>").text(i).on('click', function () {
            dynamicOrdersView(url, i);
        });
        if (i === currentPage) {
            li.addClass('active');
        }
        pagination.append(li);
    }
    pagina_corrente = currentPage;
}

function dynamicOrdersUser(url){
	$.ajax({
		url : url,
		type: 'GET',
		contentType : 'application/json; charset=utf-8'
	}).done((response) => {
		response = JSON.parse(response);
		let contenutoHTML = "";
		
		contenutoHTML += "<h2 class=\"title\">I tuoi ordini</h2>";
        contenutoHTML += "<table>";
            		
		if( response.length == 0 ){
			contenutoHTML +=  "<span>Nessun ordine disponibile</span>";
		} else {
				for(const bean of response){
                	contenutoHTML += "<tr>";
                    contenutoHTML += "<th>Numero ordine</th>";
                    contenutoHTML += "<th>Data</th>";
                    contenutoHTML += "<th>Stato</th>";
                    contenutoHTML += "<th>Totale</th>";
                    contenutoHTML += "<th>Visualizza dettagli prodotti</th>";
               		contenutoHTML += "</tr>";
                	contenutoHTML += "<tr>";
                    contenutoHTML += "<td>[#"+bean.idOrdine+"]</td>";
                    contenutoHTML += "<td>["+bean.data+"]</td>";
                    contenutoHTML += "<td>["+bean.stato+"]</td>";
                    contenutoHTML += "<td>["+bean.prezzototale.toFixed(2)+"&euro;]</td>";
                    contenutoHTML += "<td> <a href=\"./productlist.jsp?id="+bean.idOrdine+"\"> Dettagli prodotti </a> </td>";
                	contenutoHTML += "</tr>";
		      }			
		      contenutoHTML += "</table>";
		} 
		$("#orders").empty();
		$("#orders").append(contenutoHTML);
	});
} 

function eliminaRiga(button, stato) {
	
  console.log(stato);
  if(stato == "IN CONSEGNA" || stato == "CONSEGNATO"){
	  alert("Non \u00E8 possibile rimuovere un ordine in consegna!");
  }	else {
  	let text = "Stai per cancellare l'ordine, procedere con l'operazione?";
  	let result = confirm(text);
  	if ( result ) {
    	let row = button.parentNode.parentNode;
  		let idOrdine = button.getAttribute("id");
  		let pathArray = window.location.pathname.split('/');
  		let contextPath = '/' + pathArray[1];
  		let url = contextPath + "/RemoveOrderServlet";
  		console.log(idOrdine);
  		$.ajax({
    		url: url,
    		type: 'POST',
    		data:  { idOrdine: idOrdine },
   	 		success: function(response) {
	 		dynamicOrdersView(contextPath+"/OttieniStoricoOrdini", pagina_corrente);
    	},
    		error: function(xhr, status, error) {
    	  	console.error(error);
    		}
 	 	});
  		} else {
    		alert("Hai annullato l'operazione!");
  	}
  }	
}

function cambiaStato(id, nuovoStato){
	let text = "Vuoi cambiare stato dell'ordine?";
  	let result = confirm(text);
  
  	console.log("id: "+id);
  	console.log("nuovo stato: "+nuovoStato);
  	if ( result ) {
  		let idOrdine = id;
  		let pathArray = window.location.pathname.split('/');
  		let contextPath = '/' + pathArray[1];
  		let url = contextPath + "/CambiaStatoOrdineServlet";
  	console.log(idOrdine);
  	$.ajax({
    	url: url,
    	type: 'POST',
    	data:  { idOrdine: idOrdine,
        		 stato: nuovoStato
        },
   	 	success: function(response) {
	 	dynamicOrdersView(contextPath+"/OttieniStoricoOrdini", pagina_corrente);
    },
    	error: function(xhr, status, error) {
    	  console.error(error);
    	}
 	 });
  	} else {
    	alert("Hai annullato l'operazione!");
  }	
}
