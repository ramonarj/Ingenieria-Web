$(document).ready(function(){
	
	document.getElementById("botonLogin").onclick = function()
	{
		var idBombero = document.getElementById("idBombero").value;
		var contraseñaBombero = document.getElementById("contraseñaBombero").value;
		if(idBombero != "" && contraseñaBombero != "")
		{
			document.location.href = "../templates/inicio";
			alert(document.location.href);
		}
	}
});
