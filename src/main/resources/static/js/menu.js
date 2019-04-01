$(document).ready(function(){	
	$('a').bind('click', function(){
		$('a:div not(#exit)').each(function(){
			$(this).parent().parent().parent().prop('id', '')
		})
		$(this).parent().parent().parent().prop('id', 'active')
	})
	
	$('div:not(#exit)').each(function(){
		$(this).mouseleave(function(){
			$(this).prop('id', '')
		})
	})
})