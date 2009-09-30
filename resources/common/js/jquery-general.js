function searchHandle(){
	$('#search').css('margin-top','-71px');
	$('#search').append('<div id="btn-link"><button>RICERCA</button></div>');
	
	$('#btn-link').css('padding-top','20px');
	
	visible = false;
	
	$('#btn-link BUTTON').click(function(){
		if (!visible) {
			$('#search').css('margin-top','-10px');
			$('#btn-link').css('padding-top','0px');
			visible = true;
		}
		else {
			$('#search').css('margin-top','-71px');
			$('#btn-link').css('padding-top','20px');
			visible = false;
		}
	});
}

function menuHandle(){
	$('#menu .sel').parent().find('UL').css('display','block');
	$('#menu .sel').attr('id','actual');
	
	$('#menu .main-item').parent().hover(function(){
		$('#menu .sel').removeClass('sel');
		
		if ($(this).find('.main-item').attr('id') != 'actual') {
			$('#actual').parent().find('UL').css('display','none');
		}
		
		$(this).find('.main-item').addClass('sel');
	}, function(){
		if ($(this).find('.main-item').attr('id') != 'actual') {
			$('#actual').parent().find('UL').css('display','block');
			$('#actual').addClass('sel');
			$(this).find('.main-item').removeClass('sel');
		}
	});
}

function imagesHandle(){
	$('.single IMG').each(function(){
		src = $(this).attr('src');
		href = $(this).parent().attr('href');
			
		if (src == href) $(this).parent().attr('class','thickbox').attr('rel','Fidesvita.org').wrap('<div class="photo"></div>');
		else {
			if ($(this).parent().attr('class') != "photo") $(this).parent().wrap('<div class="photo"></div>');
		}
	});
}

function iconsHandle(){
	$('.txt LI A').each(function(){
		str = $(this).attr('href');
		txt = $(this).html();
		
		if (str.match('.pdf')) $(this).attr('class','pdf');
		if (str.match('.doc')) $(this).attr('class','doc');
		if (str.match('.zip')) $(this).attr('class','zip');
		if (!str.match('fidesvita.org')) $(this).attr('class','ext');
		
		if (txt.match('GALLERY') || txt.match('gallery') || txt.match('Gallery')) $(this).attr('class','img');
	});
}

function yearShow(yclass){
	$('.rivista .item').hide();
	$('.'+yclass).show();
}

function yearPagination(){
	$('.rivista .item').hide();
	
	var yfirst = $('.rivista .item:first').attr('class').substr(6,4);
	var ylast = $('.rivista .item:last').attr('class').substr(6,4);
	
	$('.rivista').prepend('<div class="y-pagination"><ul></ul></div>');

	for (y = yfirst; y >= ylast; y--){
		if (y == yfirst) $('.y-pagination UL').append('<li class="sel"><a href="javascript:yearShow(\'y'+ y +'\')">'+ y +'</a></li>');
		else $('.y-pagination UL').append('<li><a href="javascript:yearShow(\'y'+ y +'\')">'+ y +'</a></li>');
	}
	
	yclass = 'y'+ yfirst;
	
	yearShow(yclass);
	
	$('.y-pagination A').click(function(){
		$('.y-pagination LI').removeClass('sel');
		$(this).parent().attr('class','sel');
	});
}

$(function() {
	
	// gestione immagini popup
	imagesHandle();
	
	// gestione icone
	iconsHandle();
	
	// gestione search box
	searchHandle();
	
	// gestione menu principale (effetto over) 
	menuHandle();
	
	$('.textbox').focus(function(){
		$(this).css('color','#2F2F2F');
		$(this).attr('value','');
	});
	
	$('.textbox').blur(function(){
		$(this).css('color','#C0C0C0');
	});
	
	var nfoto = $('#foto .item').size();
	var nmostre = $('#mostre .item').size();
	var allegato = $('#allegato P').size();
	
	if (nmostre == 0) $('#c-m2').hide();
	if (nfoto == 0) $('#c-m3').hide();
	if (!allegato) $('#f-m2').hide();
	
});