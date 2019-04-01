$(document).ready(function(){
	
	var phone = document.getElementsByName("phone")[0];
	var mail = document.getElementsByName("mail")[0];
	var address = document.getElementsByName("address")[0];
	
	var edit = function(){
		$('#save').addClass('active')
		$(this).removeClass('active')
		$('.placeholder').each(function(){
			$(this).addClass('active')
		})
		$('.infoText').each(function(){
			$(this).removeClass('active')
		})
	}
    $('#edit').on('click', edit)
    
    
    var save = function(){
    	//Comprobamos que ha introducido un formato correcto
    	if(phone.checkValidity() && mail.checkValidity() && address.checkValidity())
    	{
    		$('#edit').addClass('active')
    		$(this).removeClass('active')
    		$('.placeholder').each(function(){
    			$(this).removeClass('active')
    		})
    		$('.infoText').each(function(){
    			$(this).addClass('active')
    		})
    	}
	}
    $('#save').on('click', save)
})