function updateLink(idLink, idCombo){
    var a = document.getElementById(idLink);
    var combo = document.getElementById(idCombo);
    if(a && combo){
        a.href = combo.options[combo.selectedIndex].value;
    }
}

function updateEditLink(idLink, idCombo){
	var a = document.getElementById(idLink);
    var combo = document.getElementById(idCombo);
    if(a && combo){
        var langIndex = a.href.indexOf('lang=');
        if(langIndex != -1){
            a.href = a.href.replace(/(lang=.+&)|(lang=.+$)/, 'lang=' + combo.options[combo.selectedIndex].value + '&');
            if(a.href.charAt(a.href.length - 1) == '&')
                a.href = a.href.substr(0, a.href.length - 1);
        }
        else
            a.href = a.href + '&lang=' + combo.options[combo.selectedIndex].value;
    }
}