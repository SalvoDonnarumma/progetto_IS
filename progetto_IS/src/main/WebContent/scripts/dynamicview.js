function dynamicProductView(url){
	$.ajax({
		url : url,
		type: 'GET',
		contentType : 'application/json; charset=utf-8'
	}).done((response) => {
		response = JSON.parse(response);
		let contenutoHTML = "";
		
		if( response.length == 0 ){
			contenutoHTML +=  "<tr>";
			contenutoHTML +=  "<td colspan=\"6\">No products available</td>";
			contenutoHTML +=  "</tr>";
		} else {
				for(const bean of response){
					contenutoHTML += "<tr>";
					contenutoHTML +=	"<td>"+bean.code+"</td>";
					contenutoHTML +=	"<td>"+bean.categoria+"</td>";
					contenutoHTML +=	"<td>"+bean.name+"</td>";
					contenutoHTML +=	"<td>"+bean.price+"</td>";
					contenutoHTML +=	"<td>"+bean.description+"</td>";
					contenutoHTML += 	"<td><img style=\"width: 150px\" src='../getPicture?id="+bean.code+"\" onerror=\"this.src='img/nophoto.png'\">"+"</td>";
					contenutoHTML +=	"<td>"+bean.stats+"</td>"
					contenutoHTML +=	"<td><p>"+bean.taglie+"</p> </td>"
					contenutoHTML +=	"<td><a href=\"../product?fromStore=false&action=delete&id="+bean.code+"\">Cancella</a><br>";
					contenutoHTML +=	"<a href=\"../product?action=read&fromStore=modify&id="+bean.code+"\">Modifica</a> <br>";
					contenutoHTML +=	"</td>";
					contenutoHTML += "</tr>"; 
		      }   
		} 
		$("#products").empty();
		$("#products").append(contenutoHTML);
	});
} 

function eliminaRiga(button) {
  let row = button.parentNode.parentNode;
  let idProdotto = button.getAttribute("id");
  console.log(idProdotto);
  let pathArray = window.location.pathname.split('/');
  let contextPath = '/' + pathArray[1];
  let url = contextPath + "/RemoveProductFromCart";
  console.log(url);
  $.ajax({
    url: url,
    type: 'POST',
    data:  { id: idProdotto },
    success: function(response) {
      // Rimuovi la riga del prodotto dal carrello nell'interfaccia utente
      row.parentNode.removeChild(row);

	  dynamicCart(contextPath+"/CartServlet");
      // Aggiorna i totali
      totaleParziale();
    },
    error: function(xhr, status, error) {
      console.error(error);
    }
  });
}
