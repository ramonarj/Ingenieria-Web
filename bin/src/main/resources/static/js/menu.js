$(document).ready(function(){	
	$('a').bind('click', function(){
		$('a').each(function(){
			$(this).parent().parent().parent().prop('id', '')
		})
		$(this).parent().parent().parent().prop('id', 'active')
	})
	
	$('div').each(function(){
		$(this).mouseleave(function(){
			$(this).prop('id', '')
		})
	})
})