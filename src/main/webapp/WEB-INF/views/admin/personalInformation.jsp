<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@include file="/WEB-INF/views/common/common_lib.jsp"%>
 <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.js"></script>
 <script>
 	$(function(){
 		$('#scrollingModal').modal('show');
 		
 		$('#AgreeBtn').on('click',function(){
 			$('#frm').attr('method','POST');
 			$('#frm').submit();
 		})
 		
 		$('#cancelBtn').on('click',function(){
 			location.href="${cp}/empController/logout?emp_no=" + ${S_USER.emp_no};
 		})
 	})
 </script>
 
 
 <form action="${cp}/empController/AgreeModify" id="frm">
 	<input type="hidden" name="emp_no" value="${S_USER.emp_no}">
 </form>
 

<div class="modal fade" id="scrollingModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true" style="display: none;">
 
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-body modal-scroll">
                	<h2 class="blind">개인정보동의</h2>
                	<div class="join_content">
				<div class="join_form">
					<form id="join_form" method="GET" action="/user2/V2Join.nhn?m=begin">
						<input type="hidden" id="token_sjoin" name="token_sjoin" value="51kYbAJG0bMJ3332">
						<input type="hidden" id="langSelect" name="langSelect" value="ko_KR">
						<!-- 약관동의 -->
						<div class="terms_p">
							<ul class="terms_bx_list">
								<li class="terms_bx">
									<div class="terms_box" tabindex="0" id="divPrivacy">
										<!-- 개인정보 수집 및 이용에 대한 안내 -->
										<div class="policy_summary">
											<p class="policy_summary__text">개인정보보호법에 따라 개인정보 동의 하시는
												분께 수집하는 개인정보의 항목, 개인정보의 수집 및 이용목적, 개인정보의 보유 및 이용기간, 동의
												거부권 및 동의 거부 시 불이익에 관한 사항을 안내 드리오니 자세히 읽은 후 동의하여 주시기 바랍니다.</p>
										</div>
										<div class="article">
											<h3 class="article__title">1. 수집하는 개인정보</h3>
											<p class="article__text">이용자는 개인정보 동의를 하지 않으면,
												로그인이 불가하며 그룹웨어 이용을 할 수 없습니다. 이용자(사원)이 그룹웨어 이용을 위해 필요한
												최소한의 개인정보를 수집합니다.</p>
											<div class="clause">
												<h4 class="clause__title">개인정보 동의는 이용자로부터 수집하는 개인정보는 아래와 같습니다.</h4>
												<ul class="sections">
													<li class="sections__section">- 개인정보 동의시에 ‘아이디, 비밀번호,
														이름, 생년월일, 성별, 휴대전화번호 등’를 필수항목으로 수집합니다. 만약 이용자가 입력하는 생년월일이
														만14세 미만 아동일 경우에는 법정대리인 정보(법정대리인의 이름, 생년월일, 성별,
														중복가입확인정보(DI), 휴대전화번호)를 추가로 수집합니다. 그리고 선택항목으로 이메일 주소, 프로필
														정보를 수집합니다.</li>
												</ul>
												<h4 class="clause__title">서비스 이용 과정에서 이용자로부터 수집하는 개인정보는 아래와 같습니다.</h4>
												<p class="clause__text">그룹웨어 내의 개별 서비스 이용 
													과정에서 해당 서비스의 이용자에 한해 추가 개인정보 수집이 발생할 수 있습니다. 추가로 개인정보를
													수집할 경우에는 해당 개인정보 수집 시점에서 이용자에게 ‘수집하는 개인정보 항목, 개인정보의 수집 및
													이용목적, 개인정보의 보관기간’에 대해 안내 드리고 동의를 받습니다.</p>
												<h4 class="clause__title">서비스 이용 과정에서 IP 주소, 쿠키, 서비스 이용
													기록, 기기정보, 위치정보가 생성되어 수집될 수 있습니다. 또한 이미지 및 음성을 이용한 검색 서비스
													등에서 이미지나 음성이 수집될 수 있습니다.</h4>
												<p class="clause__text">
													구체적으로 1) 서비스 이용 과정에서 이용자에 관한 정보를 자동화된 방법으로 생성하여 이를
													저장(수집)하거나, <br> 2) 이용자 기기의 고유한 정보를 원래의 값을 확인하지 못 하도록
													안전하게 변환하여 수집합니다. 서비스 이용 과정에서 위치정보가 수집될 수 있으며,<br>
													네이버에서 제공하는 위치기반 서비스에 대해서는 '네이버 위치정보 이용약관'에서 자세하게 규정하고 있습니다.<br>
													이와 같이 수집된 정보는 개인정보와의 연계 여부 등에 따라 개인정보에 해당할 수 있고, 개인정보에 해당하지
													않을 수도 있습니다.
												</p>
											</div>
										</div>
										<div class="article">
											<h3 class="article__title">2. 수집한 개인정보의 이용</h3>
											<p class="article__text">그룹웨어 관련 제반 서비스(모바일 웹/앱 포함)의
												회원관리, 서비스 개발・제공 및 향상, 안전한 인터넷 이용환경 구축 등 아래의 목적으로만 개인정보를
												이용합니다.</p>
											<ul class="sections">
												<li class="sections__section">- 개인정보 동의 확인, 연령 확인 및
													법정대리인 동의 진행, 이용자 및 법정대리인의 본인 확인, 이용자 식별, 회원탈퇴 의사의 확인 등
													회원관리를 위하여 개인정보를 이용합니다.</li>
												<li class="sections__section">- 콘텐츠 등 기존 서비스 제공(광고 포함)에
													더하여, 인구통계학적 분석, 서비스 방문 및 이용기록의 분석, 개인정보 및 관심에 기반한 이용자간 관계의
													형성, 지인 및 관심사 등에 기반한 맞춤형 서비스 제공 등 신규 서비스 요소의 발굴 및 기존 서비스 개선
													등을 위하여 개인정보를 이용합니다.</li>
												<li class="sections__section">- 법령 및 그룹웨어 이용약관을 위반하는 회원에
													대한 이용 제한 조치, 부정 이용 행위를 포함하여 서비스의 원활한 운영에 지장을 주는 행위에 대한 방지 및
													제재, 계정도용 및 부정거래 방지, 약관 개정 등의 고지사항 전달, 분쟁조정을 위한 기록 보존, 민원처리
													등 이용자 보호 및 서비스 운영을 위하여 개인정보를 이용합니다.</li>
												<li class="sections__section">- 서비스 이용기록과 접속 빈도 분석, 서비스
													이용에 대한 통계, 서비스 분석 및 통계에 따른 맞춤 서비스 제공 및 광고 게재 등에 개인정보를
													이용합니다.</li>
												<li class="sections__section">- 보안, 프라이버시, 안전 측면에서 이용자가
													안심하고 이용할 수 있는 서비스 이용환경 구축을 위해 개인정보를 이용합니다.</li>
											</ul>
										</div>
										<div class="article">
											<h3 class="article__title">
												<strong>3. 개인정보의 보관기간</strong>
											</h3>
											<div class="clause large">
												<h4 class="clause__title">
													<strong>회사는 원칙적으로 이용자의 개인정보를 회원 탈퇴 시 지체없이 파기하고
														있습니다.</strong>
												</h4>
												<p class="clause__text">
													<strong>단, 이용자에게 개인정보 보관기간에 대해 별도의 동의를 얻은 경우, 또는
														법령에서 일정 기간 정보보관 의무를 부과하는 경우에는 해당 기간 동안 개인정보를 안전하게 보관합니다.</strong>
												</p>
											</div>
											<div class="clause">
												<h4 class="clause__title">이용자에게 개인정보 보관기간에 대해 회원가입 시 또는
													서비스 가입 시 동의를 얻은 경우는 아래와 같습니다.</h4>
												<ul class="sections">
													<li class="sections__section">- 부정 가입 및 이용 방지 <br>가입인증
														휴대전화번호 또는DI (만14세 미만의 경우 법정대리인DI) : 수집시점으로부터6개월 보관 <br>탈퇴한
														이용자의 휴대전화번호(복호화가 불가능한 일방향 암호화(해시처리)) : 탈퇴일로부터6개월 보관
													</li>
													<li class="sections__section">- QR코드 복구 요청 대응 <br>QR코드
														등록 정보:삭제 시점으로부터6개월 보관
													</li>
													<li class="sections__section">- 스마트플레이스 분쟁 조정 및 고객문의
														대응 <br>휴대전화번호:등록/수정/삭제 요청 시로부터 최대1년
													</li>
												</ul>
											</div>
											<div class="clause">
												<h4 class="clause__title">전자상거래 등에서의 소비자 보호에 관한 법률,
													전자금융거래법, 통신비밀보호법 등 법령에서 일정기간 정보의 보관을 규정하는 경우는 아래와 같습니다.
													네이버는 이 기간 동안 법령의 규정에 따라 개인정보를 보관하며, 본 정보를 다른 목적으로는 절대 이용하지
													않습니다.</h4>
												<ul class="sections">
													<li class="sections__section">- 전자상거래 등에서 소비자 보호에 관한
														법률 <br>계약 또는 청약철회 등에 관한 기록: 5년 보관 <br>대금결제 및 재화
														등의 공급에 관한 기록: 5년 보관 <br>소비자의 불만 또는 분쟁처리에 관한 기록: 3년 보관
													</li>
													<li class="sections__section">- 전자금융거래법 <br>
														전자금융에 관한 기록: 5년 보관
													</li>
													<li class="sections__section">- 통신비밀보호법 <br>
														로그인 기록: 3개월<br>
													<br>
													</li>
												</ul>
												<p class="clause__text">참고로 ‘개인정보 유효기간제’에 따라 1년간 서비스를 이용하지 않은 회원의 개인정보를 별도로 분리 보관하여 관리하고 있습니다.</p>
											</div>
										</div>
										<div class="article">
											<h3 class="article__title">4. 개인정보 수집 및 이용 동의를 거부할 권리</h3>
											<p class="article__text">이용자는 개인정보의 수집 및 이용 동의를 거부할 권리가
												있습니다. 로그인 시 수집하는 최소한의 개인정보, 즉, 필수 항목에 대한 수집 및 이용 동의를 거부하실
												경우, 로그인이 어려울 수 있습니다.</p>
										</div>
									</div></li>
							</ul>
						</div>
					</form>


				</div>
			</div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" id="cancelBtn">동의하지않음</button>
                    <button type="button" class="btn btn-primary" id="AgreeBtn">동의</button>
                </div>
            </div>
        </div>
    </div>
    <%@include file="/WEB-INF/views/common/global_lib.jsp"%>
