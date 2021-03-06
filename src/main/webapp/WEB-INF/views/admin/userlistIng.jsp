<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>


<link href='http://fonts.googleapis.com/css?family=Source+Sans+Pro:300,400,600,700,900,300italic,400italic,600italic,700italic,900italic' rel='stylesheet' type='text/css'>
<link href='http://fonts.googleapis.com/css?family=Open+Sans:400,700' rel='stylesheet' type='text/css'>
<script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.js"></script>
<script src="https://t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
<script src="../js/regular_expression.js"></script>

<script>
	//페이징처리
	function pagingUserAjax(page, pageSize) {
		$.ajax({
			url : "${cp}/empController/pagingUserAjaxHtml",
			data : "page=" + page + "&pageSize=" + pageSize,
			success : function(data) {
				var html = data.split("####################");
				$("#userTbody").html(html[0]);
				$("#pagination").html(html[1]);
			}
		});
	}
	
	//프로필 변경
	function readURL(input) {
		if (input.files && input.files[0]) {
			var reader = new FileReader();
			  
			reader.onload = function (e) {
				$('#pictureViewImg').attr('src', e.target.result);  
			}
			reader.readAsDataURL(input.files[0]);
		}
	}
	
	flag = false;
	
	$(function() {
		// picture input의 파일 변경시 이벤트
		$("#userinfo").on('change','#profile_nm',function(){
			   readURL(this);
			});
		
		//주소api
		// 주소 검색 버튼이 클릭되었을 때 다음주소 api 팝업을 연다
		$("#userinfo").on("click","#addrBtn", function() {
			new daum.Postcode({
				oncomplete : function(data) {

					$("#zipcode").val(data.zonecode); //우편번호
					$("#addr1").val(data.roadAddress); //도로주소
					
					// 사용자 편의성을 위해 상세주소 입력 input태그로 focus 설정 -  도로주소를 입력하면 커서가 상세주소로
					$("#addr2").focus();
				}
			}).open();
		})
			
		$("#selectnum").val(${pageVo.pageSize});
		
		pagingUserAjax(1, 15);
		
		//페이지 출력 갯수 변경
		$('#example_filter').on('change', function(){
			var pageSize = $("#selectnum").val();
			
			$.ajax({
				url : "${cp}/empController/pagingUserAjaxHtml",
				data : "page=" + 1 + "&pageSize=" + pageSize,
				success : function(data) {
					var html = data.split("####################");
					$("#userTbody").html(html[0]);
					$("#pagination").html(html[1]);
				}
			});
		});
		
		$('#example_length').val('${pageVo.pageSize}');
		
		//상세조회
		$("#example").on('click',".empinfo", function(){
			var emp_no = $(this).data("emp_no");
			$.ajax({
				url : "${cp}/empController/detailUserAjax",
				data : "emp_no=" + emp_no,
				success : function(data) {
					$("#userinfo").html(data);
				}
			});
		})
		
		
		status = 1
		
		//아이디 정규식
		$('#userinfo').keyup('#empid',function(){
			if(!idcheck()){return false;}
			if($('#empid').val() == ""){status = 1}
			$.ajax({
				url : '${cp}/empController/selectEmpidCheck',
				type : 'post',
				data : { "emp_id" : idvalue },
				success : function(res){
					if(res.emp_idCheck == 1){
						$('#idCk').html('중복된 id입니다.').css("color", "red");
						flag = false;
					}else{
						$('#idCk').text("✔").css("color", "green");
						flag = true;
					}
				},
				error : function(xhr){
					alert("상태 : " +xhr.status);
				}
			})
		})
		
		//비밀번호 정규식
		$('#userinfo').keyup('#pass',function(){
			if(!pwdcheck()){ return false; }
			if($('#pass').val() == ""){status = 1}
		})
		//비밀번호 일치여부
		$('#userinfo').keyup('#pass2',function(){
			if(!pwdcheck()){ return false; }
			if($('#pass2').val() == ""){status = 1}
		})
		//개인 전화번호 정규식
		$('#userinfo').keyup('#per_pn',function(){
			if(!phonecheck()){ return false; }
			if($('#per_pn').val() == ""){status = 1}
		})
		//부서 전화번호 정규식
		$('#userinfo').keyup('#dept_pn',function(){
			if(!deptphonecheck()){ return false; }
		})
		//개인 email 정규식
		$('#userinfo').keyup('#per_email',function(){
			if(!mailcheck()){ return false; }
		})
		//부서 email 정규식
		$('#userinfo').keyup('#dept_email',function(){
			if(!deptmailcheck()){ return false; }
		})
		
		//필수항목체크
		$('#userinfo').keyup('#zipcode',function(){
			if($('#zipcode').val() == ""){status = 1}
		})
		$('#userinfo').keyup('#addr1',function(){
			if($('#addr1').val() == ""){status = 1}
		})
		$('#userinfo').keyup('#addr2',function(){
			if($('#addr2').val() == ""){status = 1}
		})
		$('#userinfo').keyup('#hire_dt',function(){
			if($('#hire_dt').val() == ""){status = 1}
		})
		$('#userinfo').keyup('#serve_cd',function(){
			if($('#serve_cd').val() == ""){status = 1}
		})
		$('#userinfo').keyup('#po_cd',function(){
			if($('#po_cd').val() == ""){status = 1}
		})
		
		//신규등록jsp이동
		$('#userinfo').on('click','#registBtn',function(){
			var emp_no = $(this).data("emp_no");
			$.ajax({
				url : "/empController/empRegist",
				data : "emp_no=" + emp_no,
				method : "GET",
				success : function(data) {
					$("#userinfo").html(data);
				}
			});
		})
		
		//신규등록
		$('#userinfo').on('click', '#registSaveBtn', function(){
			var checkedValue = [];
			$("input[name='job_cd']:checked").each(function(index, item){
				checkedValue.push($(item).val());
			});
			  
			var form = $('#profilForm');
			//formdata 생성
            var formData = new FormData(form[0]);
			formData.append("file", $('input[name=profile]')[0].files[0]);
			
			$.ajax({
				url : "${cp}/empController/empRegist",
				enctype: 'multipart/form-data',
				data : formData,
				method : "POST",
				processData: false,
				contentType: false,
				success : function(data){
					if($('#empid').val() != "" && $('#pass').val() != "" && $('#pass2').val() != "" && $('#per_pn').val() != ""
							&& $('#zipcode').val() != "" && $('#addr1').val() != "" && $('#addr2').val() != "" 
							&& $('#hire_dt').val() != "" && $('#serve_cd').val() !== "" && $('#po_cd').val() != ""){
						status = 0;
					}
					if(status = 1){
						alert('필수항목 *에 대해 입렵해주세요');
					}else{
						pagingUserAjax(1,15);
						$('#userinfo').html(data);
					}
				},
				error : function(xhr){
					alert('상태 : ' +  xhr.status);
				}
			})
		})
		
		//정보 수정
		$('#userinfo').on('click','#modifyBtn',function(){
			var emp_no = $(this).data("emp_no");
			$.ajax({
				url : "${cp}/empController/userModifyAjax",
				data : "emp_no=" + emp_no,
				method : "GET",
				success : function(data) {
					$("#userinfo").html(data);
				}
			});
		})
		
		//정보 수정(save)
		$('#userinfo').on('click', '#modifySaveBtn', function(){
			var checkedValue = [];
			$("input[name='job_cd']:checked").each(function(index, item){
				checkedValue.push($(item).val());
			});
			  
			var form = $('#profilForm');
			//formdata 생성
            var formData = new FormData(form[0]);
			formData.append("file", $('input[name=profile]')[0].files[0]);
			
			$.ajax({
				url : "${cp}/empController/userModifyAjax",
				enctype: 'multipart/form-data',
				data : formData,
				method : "POST",
				processData: false,
				contentType: false,
				success : function(data){
					pagingUserAjax(1,15);
					$('#userinfo').html(data);
				},
				error : function(xhr){
					alert('상태 : ' +  xhr.status);
				}
			})
		
		})
		
		//사원 검색
// 		$('#schBtn').on('click',function(){
// 			var pageSize = $("#selectnum").val();
// 			var keyword =  $('#search').val();
// 			$.ajax({
// 				url : "${cp}/empController/pagingUserAjaxHtml",
// 				data : "pageSize=" + pageSize + "&keyword=" + keyword,
// 				method:'GET',
// 				success : function(data){
// 					var html = data.split("####################");
// 					$("#userTbody").html(html[0]);
// 					$("#pagination").html(html[1]);

// 					var userSearchCnt = $('#userSearchCnt').val();
// 					$('#userCnt').text(userSearchCnt);
// 				},
// 				error : function(xhr){
// 					alert('상태 : ' +  xhr.status);
// 				}
// 			})
// 		})
		
		
	})
	
	//사원 검색
	function searchKeyup(){
		var pageSize = $("#selectnum").val();
		var keyword =  $('#search').val();
		$.ajax({
			url : "${cp}/empController/pagingUserAjaxHtml",
			data : "pageSize=" + pageSize + "&keyword=" + keyword,
			method:'GET',
			success : function(data){
				var html = data.split("####################");
				$("#userTbody").html(html[0]);
				$("#pagination").html(html[1]);

				var userSearchCnt = $('#userSearchCnt').val();
				$('#userCnt').text(userSearchCnt);
			},
			error : function(xhr){
				alert('상태 : ' +  xhr.status);
			}
		})
	}
	
	//사원검색(enter이벤트)
// 	function enterkey() { 
// 		if (window.event.keyCode == 13) {
// 			var pageSize = $("#selectnum").val();
// 			var keyword =  $('#search').val();
// 			$.ajax({
// 				url : "${cp}/empController/pagingUserAjaxHtml",
// 				data : "pageSize=" + pageSize + "&keyword=" + keyword,
// 				method:'GET',
// 				success : function(data){
// 					var html = data.split("####################");
// 					$("#userTbody").html(html[0]);
// 					$("#pagination").html(html[1]);
					
// 					var userSearchCnt = $('#userSearchCnt').val();
// 					$('#userCnt').text(userSearchCnt);
// 				},
// 				error : function(xhr){
// 					alert('상태 : ' +  xhr.status);
// 				}
// 			})
// 		}
// 	}
	

</script>

<div id="container">
	<form id="frm">
		<input type="hidden" id="pageSize" name="pageSize">
		<input type="hidden" id="emp_id" name="emp_id" value="${detailUser.emp_id}">
		<input type="hidden" id="emp_no" name="emp_no" value="${detailUser.emp_no}">
	</form>

	<%@ include file="../../../common/include.jsp" %>
	
	<div class="row">
		<div class="col-md-7" id="userinformation" style="min-width: 950px;">
			<div class="panel panel-default">
				<div class="panel-heading">
					<h3 class="panel-title">사원리스트</h3>
					<div class="actions pull-right">
						<i class="fa fa-chevron-down"></i> <i class="fa fa-times"></i>
					</div>
				</div>
				<div class="panel-body" style="height: 770px;">
					<div class="panel-body">
						<div role="grid" id="example_wrapper"class="dataTables_wrapper form-inline no-footer">
							
							<div class="row">
								<div class="col-xs-4" style="float: left;">
									<label>총 검색결과 : <span id="userCnt">${userCnt}</span> 건</label>
								</div>
								<div class="col-xs-8">
										<div id="example_filter" class="dataTables_filter" style="float: right;">
											<!-- 페이지수 -->
											<select id="selectnum" name="example_length" aria-controls="example" class="form-control input-sm">
												<option	value="15">15</option>
												<option value="30">30</option>
												<option value="50">50</option>
												<option value="100">100</option>
											</select> 
											
											<!-- 검색 -->												
											<input id="search" onkeyup="searchKeyup()" type="search" class="form-control input-sm" aria-controls="example" placeholder="검색할 사원의 이름을 입력하세요.">
<!-- 											<input id="search" onkeyup="enterkey()" type="search" class="form-control input-sm" aria-controls="example" placeholder="검색할 사원의 이름을 입력하세요."> -->
<!-- 											<input type="button" id="schBtn" class="btn btn-default" value="검색"> -->
										 
									<form style="float:right; margin-left: 4px;" action="${cp}/empController/downExcel">
										<!-- 엑셀다운로드 -->
										<input type="submit" id="xlxsDownloadBtn" class="btn btn-default" value="엑셀 다운로드">
									</form>
									
									</div>
								</div>
							</div>
							<br>
							
							<div style="height: 615px; overflow: auto;">
							<table id="example"	class="table table-striped table-bordered dataTable no-footer" cellspacing="0" width="100%" aria-describedby="example_info"	style="width: 100%;">
								<thead>
									<tr role="row" >
										<th style="text-align: center;">아이디</th>
										<th style="text-align: center;">이름(한글)</th>
										<th style="text-align: center;">이름(영문)</th>
										<th style="text-align: center;">성별</th>
										<th style="text-align: center;">생년원일</th>
										<th style="text-align: center;">전화번호</th>
										<th style="text-align: center;">이메일</th>
										<th style="text-align: center;">직급</th>
									</tr>
								</thead>

								<tbody id="userTbody">
								</tbody>
							</table>
							</div>

							<div class="row">
								<div style="text-align: center;">
									<div class="dataTables_paginate paging_simple_numbers" id="example_paginate">
										<ul id="pagination" class="pagination"></ul>
									</div>
								</div>
							</div>
						</div>

					</div>
				</div>
			</div>
		</div>

<!-- 		<div class="col-md-4" id="userinfo" style="min-width: 550px; display: inline-block;"> -->
		<div class="col-md-5" id="userinfo" style="min-width: 550px;">
		
			<div class="panel panel-default">
				<div class="panel-heading">
					<h3 class="panel-title">상세정보</h3>
					<div class="actions pull-right">
						<i class="fa fa-chevron-down"></i> <i class="fa fa-times"></i>
					</div>
				</div>
				<div class="panel-body" style="height: 770px;">
					<div class="card">
						<div class="register-card-body" >

							<div class="input-group" style="float: left;">
								<div class="mailbox-attachments clearfix" style="text-align: center; width: 100%;">
									<div class="mailbox-attachment-icon has-img" id="pictureView" style="border: 1px solid green; height: 150px; width: 120px; margin: 0 auto;">
										<img id="pictureViewImg" style="width: 100%; height: 100%;"/>
									</div>
								</div>
							</div>
							
<!-- 							<div class="form-group row" style="float: left;"> -->
							<div class="form-group row" >
								<label class="col-sm-2" style="font-size: 0.9em; margin-top: 20px;"> 사원번호 </label>
								<div class="col-sm-2 input-group input-group-sm" style="margin-top: 20px;">
									<span class="input-group-append-sm">&nbsp;&nbsp;&nbsp;</span>
								</div>

								<label class="col-sm-2" style="font-size: 0.9em;"> 사원id </label>
								<div class="col-sm-2 input-group input-group-sm">
									<span class="input-group-append-sm">&nbsp;&nbsp;&nbsp;</span>
								</div>
								
								<label class="col-sm-2" style="font-size: 0.9em;"> 부서이름 </label>
								<div class="col-sm-2 input-group input-group-sm">
									<span class="input-group-append-sm">&nbsp;&nbsp;&nbsp;</span>
								</div>
								
								<label class="col-sm-2" style="font-size: 0.9em;"> 급여 </label>
								<div class="col-sm-2 input-group input-group-sm">
									<span class="input-group-append-sm">&nbsp;&nbsp;&nbsp;</span>
								</div>
								
							</div>
							
<!-- 							<div class="form-group row" style="float: right;" > -->
							<div class="form-group row">
								<label class="col-sm-2" style="font-size: 0.9em;"> 한글이름 </label>
								<div class="col-sm-4 input-group input-group-sm" style="float: left;">
									<span class="input-group-append-sm">&nbsp;&nbsp;&nbsp;</span>
								</div>
								
								<label class="col-sm-2" style="font-size: 0.9em;"> 영문이름 </label>
								<div class="col-sm-4 input-group input-group-sm">
									<span class="input-group-append-sm">&nbsp;&nbsp;&nbsp;</span>
								</div>
								
								<label class="col-sm-2" style="font-size: 0.9em;"> 생년월일 </label>
								<div class="col-sm-4 input-group input-group-sm" style="float: left;">
									<span class="input-group-append-sm">&nbsp;&nbsp;&nbsp;</span>
								</div>
							
								<label class="col-sm-2" style="font-size: 0.9em;"> 성별 </label>
								<div class="col-sm-4 input-group input-group-sm">
									<span class="input-group-append-sm">&nbsp;&nbsp;&nbsp;</span>
								</div>
								
								<label class="col-sm-2" style="font-size: 0.9em;"> 개인ph </label>
								<div class="col-sm-4 input-group input-group-sm" style="float: left;">
									<span class="input-group-append-sm">&nbsp;&nbsp;&nbsp;</span>
								</div>

								<label class="col-sm-2" style="font-size: 0.9em;"> 부서ph </label>
								<div class="col-sm-4 input-group input-group-sm">
									<span class="input-group-append-sm">&nbsp;&nbsp;&nbsp;</span>
								</div>
								
								<label class="col-sm-2" style="font-size: 0.9em;"> email </label>
								<div class="col-sm-4 input-group input-group-sm" style="float: left;">
									<span class="input-group-append-sm">&nbsp;&nbsp;&nbsp;</span>
								</div>

								<label class="col-sm-2" style="font-size: 0.9em;"> d_email </label>
								<div class="col-sm-4 input-group input-group-sm">
									<span class="input-group-append-sm">&nbsp;&nbsp;&nbsp;</span>
								</div>
							
								<label class="col-sm-2" style="font-size: 0.9em;"> 주소 </label>
								<div class="col-sm-10 input-group input-group-sm">
									<span class="input-group-append-sm"> &nbsp;&nbsp;&nbsp;</span>
								</div>
								
								<label class="col-sm-2" style="font-size: 0.9em;"> 입사일자 </label>
								<div class="col-sm-3 input-group input-group-sm" style="float: left;">
									<span class="input-group-append-sm">&nbsp;&nbsp;&nbsp;</span>
								</div>

								<label class="col-sm-2" style="font-size: 0.9em;"> 퇴사일자 </label>
								<div class="col-sm-3 input-group input-group-sm">
									<span class="input-group-append-sm">&nbsp;&nbsp;&nbsp;</span>
								</div>
								
								<label class="col-sm-2" style="font-size: 0.9em;"> 재직상태 </label>
								<div class="col-sm-2 input-group input-group-sm" style="float: left;">
									<span class="input-group-append-sm">&nbsp;&nbsp;&nbsp;</span>
								</div>

								<label class="col-sm-2" style="font-size: 0.9em;"> 직급 </label>
								<div class="col-sm-2 input-group input-group-sm"style="float: left;">
									<span class="input-group-append-sm">&nbsp;&nbsp;&nbsp;</span>
								</div>
								
								<label class="col-sm-2" style="font-size: 0.9em;"> 직무 </label>
								<div class="col-sm-2 input-group input-group-sm"style="float: left;">
									<span class="input-group-append-sm">&nbsp;&nbsp;&nbsp;</span>
								</div>
							</div>
							
							<br><br>
							<div class="card-footer">
								<div class="row" >
									<input type="button" id="registBtn" class="btn btn-default" style="float: right; margin-right: 20px;" value="신규" data-emp_no="${detailUser.emp_no}">
								</div>
							</div>
						</div>
						
					</div>
				</div>
			</div>
		</div>
		

	</div>


	<div class="row">
		<div class="col-md-12">
			<div class="panel panel-default">
				<div class="panel-heading">
					<h3 class="panel-title">Donut Chart</h3>
					<div class="actions pull-right">
						<i class="fa fa-chevron-down"></i> <i class="fa fa-times"></i>
					</div>
				</div>
				<div class="panel-body">
					<div id="donutchart" style="width: 1000px; height: 500px; float: left;"></div>
					부서별 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="checkbox" id="deptNm" class="js-switch" checked onchange="draw()" data-switchery="true" style="display: none;"><br><br>
					직급별 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="checkbox" id="poNm" class="js-switch" checked onchange="draw1()" data-switchery="true" style="display: none;"><br><br>
					성 별 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="checkbox" id="genderNm" class="js-switch" checked onchange="draw2()" data-switchery="true" style="display: none;"><br><br>
					재직상태 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="checkbox" id="serveNm" class="js-switch" checked onchange="draw3()" data-switchery="true" style="display: none;">
				</div>
			</div>
		</div>
	</div>

	<!-- 		</section> -->
	<!-- 	</section> -->
</div>

<script type="text/javascript">
$('#deptNm').prop('checked', true);
$('#poNm').prop('checked', false);
$('#genderNm').prop('checked', false);
$('#serveNm').prop('checked', false);
	
	google.charts.load("current", {
		packages : [ "corechart" ]
	});
	google.charts.setOnLoadCallback(drawChart);

	function draw() {
// 		if($('#deptNm').is(':checked') == true){
// 			$('#poNm').prop('checked',false);
// 			alert($('#poNm').is(':checked'))
// 			$('#genderNm').prop('checked',false);
// 		}
		google.charts.load("current", {
			packages : [ "corechart" ]
		});
		google.charts.setOnLoadCallback(drawChart);
	}
	function draw1() {
		google.charts.load("current", {
			packages : [ "corechart" ]
		});
		google.charts.setOnLoadCallback(drawChart);
	}
	function draw2() {
		google.charts.load("current", {
			packages : [ "corechart" ]
		});
		google.charts.setOnLoadCallback(drawChart);
	}
	function draw3() {
		google.charts.load("current", {
			packages : [ "corechart" ]
		});
		google.charts.setOnLoadCallback(drawChart);
	}
	
	function drawChart() {
		if ($('#deptNm').is(':checked') == true) {
			var data = google.visualization.arrayToDataTable([
					[ 'Task', 'Hours per Day' ], 
					<c:forEach items="${chartList}" var="chart" varStatus="status">
						['${chart.dept_nm}', ${chart.dept_cnt }]
						<c:if test="${fn:length(chartList) != status.count}">
							,
						</c:if>
					</c:forEach>
					]);
			var options = {
				title : '부서',
				pieHole : 0.4,
			};
		} else if ($('#poNm').is(':checked') == true) {
			var data = google.visualization.arrayToDataTable([
					[ 'Task', 'Hours per Day' ], 
					<c:forEach items="${chartList2}" var="chart" varStatus="status">
						['${chart.po_nm}', ${chart.po_cnt }]
						<c:if test="${fn:length(chartList2) != status.count}">
							,
						</c:if>
					</c:forEach>
					]);
			var options = {
				title : '직급',
				pieHole : 0.4,
			};
		} else if ($('#genderNm').is(':checked') == true ){
			var data = google.visualization.arrayToDataTable([
				[ 'Task', 'Hours per Day' ], 
				<c:forEach items="${chartList3}" var="chart" varStatus="status">
					['${chart.gender}', ${chart.gender_cnt }]
					<c:if test="${fn:length(chartList3) != status.count}">
						,
					</c:if>
				</c:forEach>
				]);
			var options = {
				title : '성별',
				pieHole : 0.4,
			};
		} else if($('#serveNm').is(':checked') == true){
			var data = google.visualization.arrayToDataTable([
				[ 'Task', 'Hours per Day' ], 
				<c:forEach items="${chartList4}" var="chart" varStatus="status">
					['${chart.serve_nm}', ${chart.serve_cnt }]
					<c:if test="${fn:length(chartList4) != status.count}">
						,
					</c:if>
				</c:forEach>
				]);
			var options = {
				title : '재직상태',
				pieHole : 0.4,
			};
		} else {
			var data = google.visualization.arrayToDataTable([
					[ 'Task', 'Hours per Day' ], ["",1]	]);
			var options = {
				title : 'nodata',
				pieHole : 0.4,
			};
		}
			

		var chart = new google.visualization.PieChart(document.getElementById('donutchart'));
		chart.draw(data, options);
	}
	 
</script>







