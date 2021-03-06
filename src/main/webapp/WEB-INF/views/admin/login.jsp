<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<!DOCTYPE html>
<!--[if lt IE 7]>      <html class="no-js lt-ie9 lt-ie8 lt-ie7"> <![endif]-->
<!--[if IE 7]>         <html class="no-js lt-ie9 lt-ie8"> <![endif]-->
<!--[if IE 8]>         <html class="no-js lt-ie9"> <![endif]-->
<!--[if gt IE 8]><!-->
<html class="no-js">
<!--<![endif]-->

<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>SpaceLab</title>
    <meta name="description" content="">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no" />
    <!-- Favicon -->
    <link rel="shortcut icon" href="${cp}/assets/img/favicon.ico" type="image/x-icon">
    <!-- Bootstrap core CSS -->
    <link rel="stylesheet" href="${cp}/assets/plugins/bootstrap/css/bootstrap.min.css">
    <!-- Fonts from Font Awsome -->
    <link rel="stylesheet" href="${cp}/assets/css/font-awesome.min.css">
    <!-- CSS Animate -->
    <link rel="stylesheet" href="${cp}/assets/css/animate.css">
    <!-- Custom styles for this theme -->
    <link rel="stylesheet" href="${cp}/assets/css/main.css">
    <!-- Fonts -->
    <link href='http://fonts.googleapis.com/css?family=Source+Sans+Pro:300,400,600,700,900,300italic,400italic,600italic,700italic,900italic' rel='stylesheet' type='text/css'>
    <link href='http://fonts.googleapis.com/css?family=Open+Sans:400,700' rel='stylesheet' type='text/css'>
    <!-- Feature detection -->
    <script src="${cp}/assets/js/modernizr-2.6.2.min.js"></script>
    <!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
    <script src="${cp}/assets/js/html5shiv.js"></script>
    <script src="${cp}/assets/js/respond.min.js"></script>
    <![endif]-->
</head>
                                  
<body class="animated fadeIn">
	<img alt="로그인" style=" width: 50%; height: 940px; float: left;" src="${cp}/image/login.jpeg">
<%-- 	<img alt="로그인" style=" width: 200px; height: 100px; " src="${cp}/image/logo.jpg"> --%>
    <section id="login-container" >
		
        <div class="row">
            <div class="col-md-3" id="login-wrapper" style="position: absolute; left: 1200px; top: 200px;">
                <div class="panel panel-primary animated flipInY">
                    <div class="panel-heading">
                        <h3 class="panel-title">     
                           로그인
                        </h3>      
                    </div>
                    <div class="panel-body">
                       <p> 계정에 접속하려면 로그인 하십시오.</p>
                       
                        <form action="${cp}/empController/loginProcess" method="post" class="form-horizontal" role="form">
                            <div class="form-group">
                                <div class="col-md-12">
	                                <input type="text" class="form-control" id="email" name="emp_id" value="kby0210" placeholder="아이디를입력해주세요.">
<!--                                     <input type="text" class="form-control" id="emp_id" name="emp_id" placeholder="empid"> -->
                                    <i class="fa fa-user"></i>
                                </div>
                            </div>
                            <div class="form-group">
                               <div class="col-md-12">
                                    <input type="password" class="form-control" id="password" name="pass" value="1234" placeholder="비밀번호를입력해주세요.">                                   
                                    <i class="fa fa-lock"></i>
                                 
                                </div>
                            </div>
                            <br>
                            <div class="form-group">
                               <div class="col-md-12">
                               		<input type="submit" class="btn btn-primary btn-block" value="Sign in">
                                   <!--  <a href="index.jsp" class="btn btn-primary btn-block">Sign in</a> -->
<!--                                     <hr /> -->  
<!--                                     <a href="pages-sign-up.html" class="btn btn-default btn-block">Not a member? Sign Up</a> -->
                                </div>
                            </div>
                        </form>
                        
                    </div>
                </div>
            </div>
        </div>

    </section>
    <!--Global JS-->
    <script src="${cp}/assets/js/jquery-1.10.2.min.js"></script>
    <script src="${cp}/assets/plugins/bootstrap/js/bootstrap.min.js"></script>
    <script src="${cp}/assets/plugins/waypoints/waypoints.min.js"></script>
    <script src="${cp}/assets/js/application.js"></script>
</body>

</html>