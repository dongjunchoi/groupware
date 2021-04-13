<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>


<link href='http://fonts.googleapis.com/css?family=Source+Sans+Pro:300,400,600,700,900,300italic,400italic,600italic,700italic,900italic' rel='stylesheet' type='text/css'>
<link href='http://fonts.googleapis.com/css?family=Open+Sans:400,700' rel='stylesheet' type='text/css'>
<script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.js"></script>
<script src="https://t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>

 <!-- Favicon -->
    <link rel="shortcut icon" href="${cp}/assets/img/favicon.ico" type="image/x-icon">
    <!-- Bootstrap core CSS -->
    <link rel="stylesheet" href="${cp}/assets/plugins/bootstrap/css/bootstrap.min.css">
    <!-- Font Icons -->
    <link rel="stylesheet" href="${cp}/assets/css/font-awesome.min.css">
    <link rel="stylesheet" href="${cp}/assets/css/simple-line-icons.css">
    <!-- CSS Animate -->
    <link rel="stylesheet" href="${cp}/assets/css/animate.css">
    <!-- Switchery -->
    <link rel="stylesheet" href="${cp}/assets/plugins/switchery/switchery.min.css">
    <!-- Custom styles for this theme -->
    <link rel="stylesheet" href="${cp}/assets/css/main.css">
    <!-- Vector Map  -->
    <link rel="stylesheet" href="${cp}/assets/plugins/jvectormap/css/jquery-jvectormap-1.2.2.css">
    <!-- ToDos  -->
    <link rel="stylesheet" href="${cp}/assets/plugins/todo/css/todos.css">
    <!-- Morris  -->
    <link rel="stylesheet" href="${cp}/assets/plugins/morris/css/morris.css">
    <!-- Fonts -->
    <link href='http://fonts.googleapis.com/css?family=Source+Sans+Pro:300,400,600,700,900,300italic,400italic,600italic,700italic,900italic' rel='stylesheet' type='text/css'>
    <link href='http://fonts.googleapis.com/css?family=Open+Sans:400,700' rel='stylesheet' type='text/css'>
    <!-- Feature detection -->
    <script src="${cp}/assets/js/modernizr-2.6.2.min.js"></script>
    <!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->

    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>


<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.0.0/jquery.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery-modal/0.9.1/jquery.modal.min.js"></script>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/jquery-modal/0.9.1/jquery.modal.min.css" />

<script type="text/javascript">
	
	function pagingVacAjax(page, pageSize) {

		var date1 = $("#DateSearching1").val();
		var date2 = $("#DateSearching2").val();
		
		$.ajax({
			url : "/vacController/vacDetailAjaxHtml",
			tpye : "POST",
			data : {
				"page" : page,
				"pageSize" : pageSize,
				"emp_no" : ${empVo.emp_no},
				"date1" : date1,
				"date2" : date2
			},
			success : function(data) {
				var html = data.split("####################");
				$("#onoffTbody").html(html[0]);
				$("#pagination").html(html[1]);
			}
	});
}
	
	// 검색기능 ( 입사 날짜 )
	function dateSearching(){
		
		var data1 = $("#EmpSearching").val();
		var data2 = $("#DeptSearching").val();
		var date1 = $("#DateSearching1").val();
		var date2 = $("#DateSearching2").val();
		
		$.ajax({
			url : "/vacController/vacDetailAjaxHtml",
			tpye : "POST",
			data : {
				"page" : ${pageVo1.page},
				"pageSize" : 15,
				"emp_no" : ${empVo.emp_no},
				"date1" : date1,
				"date2" : date2

			},
			success : function(data) {
				var html = data.split("####################");
				$("#onoffTbody").html(html[0]);
				$("#pagination").html(html[1]);
			}
		});
		
	}
	
	$(function() {
		pagingVacAjax(${pageVo1.page}, 15);	
		
		$("#ConditionResetBtn").on('click', function(){
			$("#EmpSearching").val("");
			$("#DeptSearching").val("");
			$("#DateSearching1").val("");
			$("#DateSearching2").val("");
			
			pagingVacAjax(${pageVo1.page}, 15);	
		})
		
		$("#xlxsDownloadBtn").on('click', function(){
			$('#frm1').submit();
		})

	})
	
</script>

<div id="container">

	<form id="frm1" action="/vacController/vacDetUsedlistExcel">
		<input type="hidden" name="emp_no" value="${empVo.emp_no }">
	</form>
	
	<div class="row"> 
		<div class="col-md-12">
			<div class="panel panel-default">
				<div class="panel-heading">
					<h3 class="panel-title">연차상세조회 (${empVo.ko_nm }님)</h3>
				</div>
				<div class="panel-body" style="height: 700px;">
					<div class="panel-body">
						<div role="grid" id="example_wrapper"
							class="dataTables_wrapper form-inline no-footer">
							<div class="row">
								
								<div class="col-xs-06" >
									<span id="searchDate" style="display: block; margin-left:13px;"> 
														<label>기간:
														<input style="width: 150px;" id="DateSearching1" type="date"
														class="form-control input-sm" aria-controls="example" onchange="dateSearching()">											
														~ 
														<input style="width: 150px;" id="DateSearching2"
														type="date" class="form-control input-sm" aria-controls="example" onchange="dateSearching()">
												</label>
												<input type="button" value="초기화" id="ConditionResetBtn" style="display: inline;">
												<input type="button" id="xlxsDownloadBtn" class="btn btn-default" value="엑셀 다운로드" 
												style="width: 150px; float: right; margin-right:15px;">
											</span>
								</div>
							</div>
							
							<div class="col-md-12"
								style="height: 550px; min-width: 1000px;">
								
							<table id="example"	class="table table-striped table-bordered dataTable no-footer" cellspacing="0" width="100%" aria-describedby="example_info"	style="width: 100%;">
								<thead>
									<tr role="row">
<!-- 										<th>사원번호</th> -->
										<th>이름</th>
										<th>부서</th>
										<th>사용 기간</th>
										<th>사용 일수</th>
										<th>연차 분류</th>
									</tr>
								</thead>

								<tbody id="onoffTbody">
								</tbody>
							</table>
							</div>
							
							<div class="row">
<!-- 								<div class="col-xs-6"></div> -->
								<div style="text-align: center;">
									<div class="dataTables_paginate paging_simple_numbers" id="example_paginate">
										<ul id="pagination" class="pagination">
										</ul>
									</div>
								</div>	
							
							</div>
						</div>

					</div>
				</div>
			</div>
		</div>
			

	</div>
	<!-- 	</section> -->
</div>








