<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
 


    <!--sidebar right start-->
<div class="sidebarRight">
        <div id="rightside-navigation">
            <div id="right-panel-tabs" role="tabpanel">

                <div class="tab-content">
                    <div role="tabpanel" class="tab-pane active" id="chat">


                        <h3 class="sidebar-title">online</h3>
                        <div class="list-contacts">
                  
                            
                            <c:forEach items="${loginEmpList}" var="loginEmp">
                            	<c:if test="${loginEmp.emp_no != 0}">
		                           	<div class="chatingOnOff" >
		                            	<a href="#" class="list-item">
			                                <div class="list-item-image">
			                                	<img id="pictureViewImg" style="width: 50px; height: 50px;"	src="${cp }/empController/profile?emp_id=${loginEmp.emp_id}" />
			                                    
			                                </div>
			                                <div class="list-item-content">
			                                    <h4>${loginEmp.ko_nm} ${loginEmp.po_nm}</h4>
			                                    <p>${loginEmp.dept_nm}</p>
			                                </div>
			                                <div class="item-status item-status-online"></div>
			                            </a>
		
			                        
		                            </div>
	                            </c:if>
                            </c:forEach>
                        </div>

                    </div>

                </div>
            </div>
        </div>
    </div>
    
    <!--sidebar right end-->
