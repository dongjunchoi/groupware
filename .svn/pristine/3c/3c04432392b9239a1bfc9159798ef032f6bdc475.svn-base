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
    <!--[if lt IE 9]>
    <script src="assets/js/html5shiv.js"></script>
    <script src="assets/js/respond.min.js"></script>
    <![endif]-->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
    
    <!-- bar chart script -->
	<script src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.5.0/Chart.min.js"></script>
	
	<!-- Include fusioncharts core library -->
	<script type="text/javascript" src="https://cdn.fusioncharts.com/fusioncharts/latest/fusioncharts.js"></script>
	<!-- Include fusion theme -->
	<script type="text/javascript" src="https://cdn.fusioncharts.com/fusioncharts/latest/themes/fusioncharts.theme.fusion.js"></script>

	<!-- bar chart -->


<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.0.0/jquery.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery-modal/0.9.1/jquery.modal.min.js"></script>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/jquery-modal/0.9.1/jquery.modal.min.css" />

<script type="text/javascript">
	
	//페이징처리
	function pagingOnOffAjax(page, pageSize) {	
		
		var date1 = $("#DateSearching1").val();
		var date2 = $("#DateSearching2").val();
		
		$.ajax({
			url : "/empController/pagingOnOffDetailAjaxHtml",
			data : {
				"page" : page,
				"pageSize" : pageSize,
				"emp_no" :${emp_no},
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
	
	function dateSearching(){
		
		var data1 = $("#EmpSearching").val();
		var data2 = $("#DeptSearching").val();
		var date1 = $("#DateSearching1").val();
		var date2 = $("#DateSearching2").val();
		
		$.ajax({
			url : "/empController/pagingOnOffDetailAjaxHtml",
			type : "POST",
			data : {
				"page" : ${pageVo1.page},
				"pageSize" : 10,
				"emp_no" : ${emp_no},
				"emp_id" : "${S_USER.emp_id}",
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
		pagingOnOffAjax(${pageVo1.page}, 10);
		
		$("#ConditionResetBtn").on('click', function(){
			$("#EmpSearching").val("");
			$("#DeptSearching").val("");
			$("#DateSearching1").val("");
			$("#DateSearching2").val("");
			
			pagingOnOffAjax(${pageVo1.page}, 10);	
		})
		
		// 엑셀 다운로드
		$("#xlxsDownloadBtn").on('click', function(){
			$("#frm1").submit();
		})

	})
	
</script>


<style>
. dataTables_filter{

}

</style>
<div id="container">

	<form id="frm1" action="${cp}/empController/onoffDetailExcel">
		<input type="hidden" id="empid" name="emp_no" value="${emp_no }">
	</form>

	<div class="row">
		<div class="col-md-12">
			<div class="panel panel-default">
				<div class="panel-heading">
					<h3 class="panel-title">근태상세조회 (${onoffVo.dept_nm }부서 &nbsp; -
						&nbsp; ${onoffVo.ko_nm })</h3>
				</div>
				<div class="panel-body" style="height: 700px;">
					<div class="panel-body">
						<div role="grid" id="example_wrapper"
							class="dataTables_wrapper form-inline no-footer">




				<div class="row">
								<div class="col-xs-06">
									<div id="example_filter" class="dataTables_filter"
										style="float: left; padding-right: 15px;">

										<span id="searchDate"
											style="display: block; margin-left: 17px;"> 
											
											<label>기간:
												<input style="width: 150px;" id="DateSearching1" type="date"
													class="form-control input-sm" aria-controls="example"
													onchange="dateSearching()"> ~ 
												<input
													style="width: 150px;" id="DateSearching2" type="date"
													class="form-control input-sm" aria-controls="example"
													onchange="dateSearching()">
										   </label> 
										<input type="button" value="초기화" id="ConditionResetBtn"
											style="display: inline;">
										</span>
									</div>
									<!-- 엑셀 다운로드 -->
									<input type="button" id="xlxsDownloadBtn"
										class="btn btn-default" value="엑셀 다운로드"
										style="width: 130px; height: 37px; margin-left: 462px; min-width: 130px;">

									<!-- 엑셀 다운로드 끝 -->
								</div>
							</div>
							
							<div class="col-md-12"
								style="height: 550px; min-width: 1000px;">
							
							<table id="example"
								class="table table-hover"
								cellspacing="0" width="100%" aria-describedby="example_info"
								style="width: 100%;">
								<thead>
									<tr role="row">
										<!-- 										<th>사원번호</th> -->
										<th>아이디</th>
										<th>부서</th>
										<th>날짜</th>
										<th>이름</th>
										<th>영문이름</th>
										<th>요일</th>
										<th>출근</th>
										<th>퇴근</th>
										<th>지각</th>
									</tr>
								</thead>

								<tbody id="onoffTbody">
								</tbody>
							</table>
							</div>
							
							<div class="row">
								<!-- 								<div class="col-xs-6"></div> -->
								<div style="text-align: center;">
									<div class="dataTables_paginate paging_simple_numbers"
										id="example_paginate">
										<ul id="pagination" class="pagination">
										</ul>
									</div>
								</div>
								<form action="${cp}/empController/onoffDetailExcel?"
									style="padding-right: 15px;">
									<input type="hidden" value="${emp_no }" name="emp_no">

								</form>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>



<!-- bar chart Start -->

	<div class="row">
		<div class="col-md-12">
			<div class="panel panel-default">
				<div class="panel-body">
					<label style="margin-left : 375px; font-size :1.7em;">${onoffVo.ko_nm}님 근태그래프</label> 
								<br><br><br>
							
						<div id="chart-container">FusionCharts XT will load here!</div>
						</div>
				</div>
			</div>
		</div>
	</div>

<script type="text/javascript">

// Colunm Chart Start 
const chartData = [  
	<c:forEach items="${OnOffDataList}" var="onoff" varStatus="status">
		<c:if test="${status.index > 0 }">
			,
		</c:if>
			{
				"label" : "${onoff.date}",
 			     "value" : "${onoff.work_time }",
 			     "tooltext" : "${onoff.day }, {br} ${onoff.work_hour }"
			}
	</c:forEach>	
];		
//STEP 3 - Chart Configurations
const chartConfig = {
type: 'column2d',
renderAt: 'chart-container',
width: '100%',
height: '600',
dataFormat: 'json',
dataSource: {
    // Chart Configuration
    "chart": {
        "xAxisName": "최근 7일간 근무시간",
        "numberSuffix": "h",
        "theme": "fusion"
        },
    // Chart Data
    "data" : chartData
	}
};
FusionCharts.ready(function(){
var fusioncharts = new FusionCharts(chartConfig);
fusioncharts.render();
});
 
	

 </script> 

<!-- bar chart End -->






