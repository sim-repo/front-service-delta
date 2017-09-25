$(document).ready(function() {
			 
		 
	$('ul.art-vmenu li').click(function(e) { 
	  alert($(this).find("span.a").text());
	});
	
	
	$('.mi_cm_clickable').click(function(e) { 		
		$('#accordion').children().hide();		
		var at = $(this).attr('id');	
		$('.'+at).show();												 		
	});
	
	
	$(".search-form").submit(function(event) {
		enableSearchButton(false);
		event.preventDefault();
		searchViaAjax();
	});
	
	
	$(".tag-form").submit(function(event) {	
		
		var search = {}	
		search["clazz"]='LogBusMsg';
		search["tag"]='unknown';
		search["sqlTemplate"]=$(this).attr('id');	
		
		var inputs = $(this).find(':text')
		var params = {}
		inputs.each(function( index ) {		
			params[$(this).attr('id')] = $(this).val();							
		});
		search["params"] = params;	
		enableSearchButton(false);
		event.preventDefault();
		searchViaAjax2(search);
	});
	
	
	function searchViaAjax() {
		
		var search = {}
		search["username"] = $("#username").val();
		search["email"] = $("#email").val();
	
		$.ajax({
			type : "POST",
			contentType : "application/json",
			url : "search/api/getSearchResult",
			data : JSON.stringify(search),
			dataType : 'json',
			timeout : 100000,
			success : function(data) {
				console.log("SUCCESS: ", data);
				display(data);
			},
			error : function(e) {
				console.log("ERROR: ", e);
				display(e);
			},
			done : function(e) {
				console.log("DONE");
				enableSearchButton(true);
			}
		});
	}
	
	
	function searchViaAjax2(search) {		
		$.ajax({
			type : "POST",
			contentType : "application/json",
			url : "search/api/getSearchResult",
			data : JSON.stringify(search),
			dataType : 'json',
			timeout : 100000,
			success : function(data) {
				console.log("SUCCESS: ", data);
				display(data);
			},
			error : function(e) {
				console.log("ERROR: ", e);
				display(e);
			},
			done : function(e) {
				console.log("DONE");
				enableSearchButton(true);
			}
		});
	}
	
	
	function enableSearchButton(flag) {
		$("#btn-search").prop("disabled", flag);
	}
	
	
	function display(data) {
		var json = "<h4>Ajax Response</h4><pre>"
				+ JSON.stringify(data, null, 4) + "</pre>";	
		var json2 = JSON.stringify(data, null, 4);
		var result;	
		var json = $.parseJSON(json2);
		    $(json).each(function(i,val){
		       $.each(val,function(k,v){
		    	   result += k+" + "+ v;        
		      });
		    });	 	
	    var columns1 = $('#feedback').columns({
	    	data:json
	    });     
	}				 	
})