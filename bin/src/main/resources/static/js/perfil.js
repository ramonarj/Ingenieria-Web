$(document).ready(function(){
	
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
		$('#edit').addClass('active')
		$(this).removeClass('active')
		$('.placeholder').each(function(){
			$(this).removeClass('active')
		})
		$('.infoText').each(function(){
			$(this).addClass('active')
		})
	}
    $('#save').on('click', save)
})