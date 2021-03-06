package kr.or.ddit.groupware.contoller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import kr.or.ddit.common.model.PageVo;
import kr.or.ddit.groupware.model.BoardVo;
import kr.or.ddit.groupware.model.DeptVo;
import kr.or.ddit.groupware.model.EmpVo;
import kr.or.ddit.groupware.model.JobVo;
import kr.or.ddit.groupware.model.OnOffVo;
import kr.or.ddit.groupware.service.BoardService;
import kr.or.ddit.groupware.service.EmpService;
import kr.or.ddit.groupware.service.OnOffService;
import kr.or.ddit.groupware.service.VacationServiceI;
import kr.or.ddit.groupware.util.FileUtil;
import net.sf.jxls.exception.ParsePropertyException;
import net.sf.jxls.transformer.XLSTransformer;

@Controller
@RequestMapping("empController")
public class EmpController {

	private static final Logger logger = LoggerFactory.getLogger(EmpController.class);

	private static List<EmpVo> loginEmpList = new ArrayList<EmpVo>();
	
//	public static List<EmpVo> chatEmpList = new ArrayList<EmpVo>();
	public static List<Integer> checkEmpList = new ArrayList<Integer>();
	
	@Resource(name="empService")
	private EmpService empService;
	
	@Resource(name="boardService")
	private BoardService boardService;
	
	@Resource(name="onoffService")
	private OnOffService onoffService;
	
	@Resource(name="vacationService")
	private VacationServiceI vacService;
	
	//???????????? pw?????????
	@Inject
	BCryptPasswordEncoder pwdEncode;
	
	//???????????????
	@RequestMapping("loginView")
	public String loginView() {
		return "admin/login";
	}
	
	
	//?????????
	@RequestMapping(path="loginProcess", method = RequestMethod.POST)
	public String loginProcess(EmpVo empVo, HttpSession session, HttpServletRequest request , RedirectAttributes ra, Model model, PageVo pageVo) {
		
		EmpVo dbUser = empService.selectUser(empVo.getEmp_id());
		if(dbUser == null) {
			return "redirect:/empController/loginView";
		}
		logger.debug("dbUserPass:{}", dbUser.getPass());
		//????????? pw??????
		boolean pwdMatch = pwdEncode.matches(empVo.getPass(), dbUser.getPass());
		logger.debug("pwdMatch:{}", pwdMatch);
//		if(dbUser != null && empVo.getPass().equals(dbUser.getPass())) {
		if(dbUser != null && pwdMatch == true) {
			session.setAttribute("S_USER", dbUser);
			model.addAttribute("userList", empService.selectPagingUser(pageVo));
			
			
			List<BoardVo> board = boardService.selectEmpBoard(dbUser.getEmp_no());
			session.setAttribute("boardLeft", board);
			
			boolean loginFlag = true;
			
			EmpVo loginUser = empService.selectEmpDetail(dbUser.getEmp_no());
			
			for(int i = 0; i<loginEmpList.size();i++) {
				if(loginEmpList.get(i).getEmp_no()==loginUser.getEmp_no()) {
					loginFlag = false;
				}
			}
			if(loginFlag) {
				loginEmpList.add(loginUser);				
			}
			request.getServletContext().setAttribute("loginEmpList", loginEmpList);
			
			//?????????????????? ??????(140=????????? , 141=??????)
			if(dbUser.getPer_info_agr_cd() == 140) {
				return "admin/personalInformation";
			}else {
				ra.addAttribute("emp_no", dbUser.getEmp_no());
				return "redirect:/test/main";
			}
			
		}else {	
			return "redirect:/empController/loginView";
		}
	}
	
	//????????????
	@RequestMapping(path = "logout",method = RequestMethod.GET)
	public String logout(int emp_no, HttpSession session) {
		session.invalidate();
		for(int i = 0; i<loginEmpList.size(); i++) {
			if(loginEmpList.get(i).getEmp_no()==emp_no) {
				loginEmpList.remove(i);
				break;
			}
		}
		return "redirect:/empController/loginView";
	}
	
	//???????????? ?????? ????????????
	@RequestMapping(path = "AgreeModify", method = RequestMethod.POST)
	public String AgreeModify(Model model, int emp_no) {
		int agreeCnt = 0;
		
		logger.debug("AgreeModify() ================= ");
		logger.debug("emp_no : {}", emp_no);
		
		try {
			agreeCnt = empService.agreeModify(emp_no);
		} catch (Exception e) {
			agreeCnt = 0;
			e.printStackTrace();
		}
		
		if(agreeCnt == 1) {
			return "redirect:/test/main?emp_no=" + emp_no;
		}else {
			return "redirect:/empController/loginProcess";
		}
		
	}
	 
	//???????????? ????????? , chart api
	@RequestMapping("empinformation")
	public String empinformation(PageVo pageVo, Model model) {
	
		Map<String, Object> map = empService.selectPagingUser(pageVo);
		
		model.addAllAttributes(map);
		
		//????????? chart(?????????,count)
		model.addAttribute("chartList", empService.selectChartDept());
		//????????? chart(?????????,count)
		model.addAttribute("chartList2", empService.selectChartPo());
		//?????? chart(??????,count)
		model.addAttribute("chartList3", empService.selectChartGender());
		//????????? chart(????????????,count)
		model.addAttribute("chartList4", empService.selectChartServe());
		
		return "tiles.admin.userlist";	
	}
	
	//???????????? userCnt
	@RequestMapping("userCnt")
	public String userCnt(Model model) {
		model.addAttribute("cnt", empService.selectAllUserCount());
		
		return "jsonView";
	}
	
	//???????????? ?????????AjaxHtml
	@RequestMapping("pagingUserAjaxHtml")
	public String pagingUserAjaxHtml(@RequestParam(defaultValue = "1") int page,
									@RequestParam(defaultValue = "15") int pageSize, String keyword,
									Model model) {
		
		PageVo pageVo = new PageVo(page, pageSize);
		pageVo.setKeyword(keyword);
		logger.debug("keyword : {}",keyword);

		if(pageVo.getKeyword() != null) {
			model.addAllAttributes(empService.searchPagingEmp(pageVo));
		}else {
			model.addAllAttributes(empService.selectPagingUser(pageVo));

		}
		return "/admin/pagingUserAjaxHtml";
	}
	
	//???????????? ????????????Ajax
	@RequestMapping("detailUserAjax")
	public String detailUserAjax(int emp_no, Model model) {
		
		logger.debug("emp_no : {}", emp_no);
		
		EmpVo empList = new EmpVo();
		empList.setEmp_no(emp_no);
		
		EmpVo detailUser = empService.detailUserAjax(empList);
		
		logger.debug("empVo : {}", detailUser);
		
		model.addAttribute("detailUser", detailUser);
		
		return "/admin/detailUserAjaxHtml";
	}
	
	//???????????? ?????????????????????
	@RequestMapping(path = "userModifyAjax", method = RequestMethod.GET)
	public String userModifyAjax(EmpVo empVo, int emp_no, Model model){
		
		EmpVo empList = new EmpVo();
		empList.setEmp_no(emp_no);
		
		EmpVo detailUser = empService.detailUserAjax(empList);
		
		logger.debug("empVo : {}", detailUser);
		
		//?????????
		model.addAttribute("deptNm", empService.selectDeptNm());
		//??????
		model.addAttribute("genderNm", empService.selectGenderNm());
		//????????????
		model.addAttribute("serveNm", empService.selectServeNm());
		//??????
		model.addAttribute("poNm", empService.selectPoNm());
		//??????
		model.addAttribute("jobNm", empService.selectJobNm());
		
		model.addAttribute("detailUser", detailUser);
		return "/admin/userModifyAjaxHtml";
	}
	
	//??????????????????
	@RequestMapping(path = "userModifyAjax", method = RequestMethod.POST)
	public String userModifyAjax(EmpVo empVo,JobVo jobVo, int emp_no, Model model, MultipartHttpServletRequest request) {

		logger.debug("job:{}", empVo.getJob_cd());
		
		List<MultipartFile> fileList = request.getFiles("profile");
		MultipartFile profile = fileList.get(0);
		
		logger.debug("profile :{}", profile);
		
		if(profile.getSize() == 0) {
			EmpVo checkEmpVo = empService.selectEmpDetail(emp_no);
			if("".equals(checkEmpVo.getProfile_nm()) || checkEmpVo.getProfile_nm()==null) {
				empVo.setProfile_nm("");
		        empVo.setProfile_route("");
			}else {
		        empVo.setProfile_nm(empService.detailUserAjax(empVo).getProfile_nm());
		        empVo.setProfile_route(empService.detailUserAjax(empVo).getProfile_route());
			}
		}else {
			try {
				empVo.setProfile_nm(profile.getOriginalFilename());
				String fileExtension = FileUtil.getFileExtension(profile.getOriginalFilename());
				String realFileName = "d:/upload/"+UUID.randomUUID().toString()+fileExtension;
				empVo.setProfile_route(realFileName);
				profile.transferTo(new File(realFileName));
			} catch (IllegalStateException | IOException e1) {
				e1.printStackTrace();
			}
		}
		
		logger.debug("empVo1 : {}",empVo);
		
		//???????????? ??????(????????????)
		int deleteCnt = 0;
		try {
			deleteCnt = empService.modifyDelete(emp_no);
		} catch (Exception e) {
			deleteCnt = -1;
			e.printStackTrace();
		}
		
		//???????????? ??????(????????????)
		logger.debug("empVo2 : {}",empVo);
		jobVo.setEmp_no(emp_no);
		int insertCnt = 0;
		for(int i=0; i<empVo.getJob_cd().size(); i++) {
			
			jobVo.setJob_cd(empVo.getJob_cd().get(i));
			try {
				insertCnt = empService.modifyInsert(jobVo);
			} catch (Exception e) {
				insertCnt = 0;
				e.printStackTrace();
			}
		}
		
		logger.debug("empVo3 : {}",empVo);
		
		//???????????? ??????????????????
		
		int updatCnt = 0;
		try {
			if("".equals(empVo.getPass())) {
				EmpVo updateVo = empService.selectEmpDetail(emp_no);
				empVo.setPass(updateVo.getPass());
				updatCnt = empService.modifyUserAjax(empVo);
			}else {
				//pw?????????
				String inputPass = empVo.getPass();
				String pwd = pwdEncode.encode(inputPass);
				empVo.setPass(pwd);
				updatCnt = empService.modifyUserAjax(empVo);
			}
			
		}catch (Exception e) {
			updatCnt = 0;
			e.printStackTrace();
		}
		if(updatCnt == 1 && deleteCnt != -1 && insertCnt != 0) {
			model.addAttribute("detailUser", empService.detailUserAjax(empVo));
			return "/admin/detailUserAjaxHtml";
		}else {	
			model.addAttribute("detailUser", empService.detailUserAjax(empVo));
			return "/admin/userModifyAjaxHtml";
		}
		
	}
	
	//?????? ????????????
	public void download(HttpServletRequest request, HttpServletResponse response, Map<String, Object> bean,
			String fileName, String templateFile, String string) throws ParsePropertyException, InvalidFormatException {

		// ???????????? ???????????? bean??? ???????????? ????????? ?????????
        // fileName ??? ???????????? ????????? ???????????? ?????????
        // templateFile ??? ????????? ?????? ???????????????.
        
        // tempPath??? ????????? ??????????????? ???????????? ????????? ?????? ??????.
		String tempPath = request.getSession().getServletContext().getRealPath("/WEB-INF/excel");

		// ????????? ???????????? ????????? ???????????? ??????????????? ???????????? ?????? ?????? ????????? ?????? ?????? 
		try {

			InputStream is = new BufferedInputStream(new FileInputStream(tempPath + "\\" + templateFile));
			XLSTransformer xls = new XLSTransformer();

			Workbook workbook = (Workbook) xls.transformXLS(is, bean);

			response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + ".xlsx\"");

			OutputStream os = response.getOutputStream();

			workbook.write(os);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@RequestMapping(value = "downExcel")
    public void listExcel(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) throws Exception, Exception {

		// ?????? ????????? ????????????????????? ????????? ?????? ???????????? ???????????? ????????????.
		List<EmpVo> dataList = (List<EmpVo>) empService.selectAllEmp();
        
		// ?????? ???????????? ?????? ?????????.
        Map<String, Object> beans = new HashMap<String, Object>();
        beans.put("dataList", dataList);
        
        // ?????? ???????????? ???????????? ?????? ?????? ??????
        EmpController me = new EmpController();

        me.download(request, response, beans, "userinfo", "templateFile.xlsx", "");
    }
	
	//?????????
	@RequestMapping("profile")
	public void profile(HttpServletResponse resp, String emp_id, HttpServletRequest req) {
		resp.setContentType("image");
		
		// userid ??????????????? ???????????? userService ????????? ?????? ???????????? ?????? ?????? ????????? ??????
		// ?????? ???????????? ?????? ????????? ???????????? resp????????? outputStream?????? ?????? ??????
		
		EmpVo empVo = empService.selectUser(emp_id);
		
		String path = "";
		if(empVo.getProfile_route() == null) {
			//	/webapp/image ??????..
			path = req.getServletContext().getRealPath("/image/unknown.png");
		}else {
			path = empVo.getProfile_route();
		}
		
		logger.debug("path : {}", path);
		
		FileInputStream fis;
		
		try {
			fis = new FileInputStream(path);
			ServletOutputStream sos = resp.getOutputStream();
			
			byte[] buff = new byte[512];
			
			while(fis.read(buff) != -1) {
				sos.write(buff);
			}
			
			fis.close();
			sos.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//????????? ????????????
	@RequestMapping("profileDownload")
	public void profileDownload(String emp_id, HttpServletRequest req, HttpServletResponse resp) {
		
		EmpVo empVo = empService.selectUser(emp_id);

		String path = "";
		String filename = "";
		if(empVo.getProfile_route() == null) {
			//	/webapp/image ??????..
			path = req.getServletContext().getRealPath("/image/unknown.png");
			filename = "unknown.png";
		}else {
			path = empVo.getProfile_route();
			filename = empVo.getProfile_nm();
		}
		
		resp.setHeader("Content-Disposition", "attacment; filename=" + filename);
		
		logger.debug("path : {}", path);
		
		FileInputStream fis;
		try {
			fis = new FileInputStream(path);
			ServletOutputStream sos = resp.getOutputStream();
			
			byte[] buff = new byte[512];
			
			while(fis.read(buff) != -1) {
				sos.write(buff);
			}
			
			fis.close();
			sos.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//???????????? jsp??????
	@RequestMapping(path = "empRegist", method = {RequestMethod.GET})
	public String userRegistAjax(Model model) {
		//?????????
		model.addAttribute("deptNm", empService.selectDeptNm());
		//??????
		model.addAttribute("genderNm", empService.selectGenderNm());
		//????????????
		model.addAttribute("serveNm", empService.selectServeNm());
		//??????
		model.addAttribute("poNm", empService.selectPoNm());
		//??????
		model.addAttribute("jobNm", empService.selectJobNm());
		
		return "admin/userRegistAjaxHtml";
	}
	
	//???????????? save
	@RequestMapping(path = "empRegist", method = {RequestMethod.POST})
	public String userReistAjax(Model model, EmpVo empVo, JobVo jobVo, MultipartHttpServletRequest request) {
		List<MultipartFile> fileList = request.getFiles("profile");
		MultipartFile profile = fileList.get(0);
		
		if("".equals(profile.getOriginalFilename())) {
			empVo.setProfile_nm("");
			empVo.setProfile_route("");
		}else {
			try {
				empVo.setProfile_nm(profile.getOriginalFilename());
				String fileExtension = FileUtil.getFileExtension(profile.getOriginalFilename());
				String realFileName = "d:/upload/"+UUID.randomUUID().toString()+fileExtension;
				empVo.setProfile_route(realFileName);
				profile.transferTo(new File(realFileName));
			} catch (IllegalStateException | IOException e1) {
				e1.printStackTrace();
			}
		}
		
		//????????????
		int registCnt = 0;
		try {
			
			//pw?????????
			String inputPass = empVo.getPass();
			String pwd = pwdEncode.encode(inputPass);
			empVo.setPass(pwd);
			logger.debug("???????????? ???????????? : "+ empVo.getPass());
			
			registCnt = empService.registEmp(empVo);
		} catch (Exception e) {
			registCnt = 0;
			e.printStackTrace();
		}
		
		//????????????
		int j_registCnt = 0;
		if(empVo.getJob_cd() != null) {
		
			jobVo.setEmp_no(empService.selectEmpno());
			for(int i=0; i<empVo.getJob_cd().size(); i++) {
				
				jobVo.setJob_cd(empVo.getJob_cd().get(i));
				logger.debug("jobVo : {}", jobVo);
				try {
					j_registCnt = empService.modifyInsert(jobVo);
				} catch (Exception e) {
					j_registCnt = 0;
					e.printStackTrace();
				}
			}
			j_registCnt = 1;
		}
		
		if(registCnt == 1 || j_registCnt != 0) {
			int emp_no = empService.selectEmpno();
			return "redirect:/empController/detailUserAjax?emp_no="+emp_no;
		}else {
			return "redirect:/empController/empRegist";
		}
	}
	
	//????????????(id????????????)
	@RequestMapping("selectEmpidCheck")
	public String selectEmpidCheck(Model model,String emp_id) {
		model.addAttribute("emp_idCheck", empService.selectEmpidCount(emp_id));
		
		return "jsonView";
	}
	
	//?????? ????????? jsp??????
	@RequestMapping(path = "organizationChart")
	public String organizationChart(Model model) {
		model.addAttribute("deptList", empService.selectDept());
		logger.debug("deptVo : {}",empService.selectDept());
		model.addAttribute("organizationList", empService.selectOrganization());
		return "tiles.admin.organizationChart";
	}
	
	
	//?????? ????????? ????????????Ajax
	@RequestMapping("detailOrganizationAjaxHtml")
	public String detailUserAjax2(int emp_no, Model model) {
			
		logger.debug("emp_no : {}", emp_no);
		
		EmpVo empList = new EmpVo();
		empList.setEmp_no(emp_no);
		
		EmpVo detailUser = empService.detailUserAjax(empList);
		
		logger.debug("empVo : {}", detailUser);
		
		model.addAttribute("detailUser", detailUser);
		
		return "/admin/detailOrganizationAjaxHtml";
	}
	
	//????????????(?????????)
	@RequestMapping(path = "registDept", method = RequestMethod.POST)
	public String registDept(Model model, DeptVo deptVo) {
		int deptCnt = 0;
		
		try {
			deptCnt = empService.registDept(deptVo);
		} catch (Exception e) {
			deptCnt=0;
			e.printStackTrace();
		}
		
		if(deptCnt == 1) {
			return "redirect:/empController/organizationChart";
		}else {
			return "";
		}
	}
	
	//????????????(?????????)
	@RequestMapping(path = "deleteDept", method = RequestMethod.POST)
	public String deleteDept(Model model, DeptVo deptVo) {
		int deleteCnt = 0;
		try {
			deleteCnt = empService.deleteDept(deptVo);
		} catch (Exception e) {
			deleteCnt = -1;
			e.printStackTrace();
		}
		if(deleteCnt == 1) {
			return "redirect:/empController/organizationChart";
		}else {
			return"";
		}
			
	}
	
	//????????????(?????????)
	@RequestMapping(path = "modifyDept", method = RequestMethod.POST)
	public String modifyDept(Model model, DeptVo deptVo) {
		int modifyCnt = 0;
		try {
			modifyCnt = empService.modifyDept(deptVo);
		} catch (Exception e) {
			modifyCnt = 0;
		}
		
		if(modifyCnt == 1) {
			return "redirect:/empController/organizationChart";
		}else {
			return "";
		}
	}
	
	
	
	//----------------------????????????----------------------------------------------
	// ???????????? jsp??? ??????
	@RequestMapping("emponoff")
	public String emponoff(int emp_no, String emp_id, PageVo pageVo, Model model) {
		
		Map<String, Object> map = onoffService.selectOnOffPaging(pageVo);
		int pagination = (int) map.get("pagination");
		
		int attendCnt = onoffService.checkAttendCnt(emp_no);
		
		OnOffVo onVo = new OnOffVo(); 
		OnOffVo onVo1 = new OnOffVo(); 		
		
		try {
			onVo = onoffService.checkOffTime(emp_no);
			onVo1 = onoffService.selectWorktime(emp_no);
			model.addAttribute("offTime", onVo.getOffwork_time());
			model.addAttribute("workTime", onVo1.getAttend_time());
		} catch (Exception e) {
			model.addAttribute("hi", "-");
		}
		
		if(attendCnt == 0) {
			model.addAttribute("attendCnt", 0);
		}else {
			model.addAttribute("attendCnt", 1);
		}
		
		// Bar Graph Data
		model.addAttribute("OnOffDataList", onoffService.selectOnOffData(emp_no));
		model.addAttribute("workStatus", onoffService.selectWorkStatus());
		model.addAttribute("emp_no", emp_no);
		model.addAttribute("emp_id", emp_id);
		model.addAttribute("pagination", pagination);
		model.addAttribute("pageVo", pageVo);
		
		return "tiles.admin.onofflist";
	}
	
	// ?????? ????????? ?????????
	@RequestMapping(value="pagingOnOffAjaxHtml")		
	public String pagingOnOffAjaxHtml(@RequestParam(defaultValue = "1") int page,
									  @RequestParam(defaultValue = "15") int pageSize,
									  int emp_no, String emp_id,Model model, String data1, String data2,
									  String date1, String date2) {
		
		logger.debug("pagingOnOffAjaxHtml() ====================================== \n");
		
		Date today = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String today1 = sdf.format(today);
		
		logger.debug("date1 : {}, data2 : {}", date1, date2);
		
		
		String date11 = "";
		String date22 = "";
		
		if(date1 != null && date2 != null) {
			date11 = date1.replaceAll("-", "");
			date22 = date2.replaceAll("-", "");			
		}
	
		logger.debug("date11 : {}, data22 : {}", date11, date22);
		
		String datas1 = "%%";
		String datas2 = "%%";
		
		// ?????? ????????? ??? 
		Map<String, Object> map = new HashMap<>();
		map.put("page", page);
		map.put("pageSize", pageSize);
		map.put("emp_no", emp_no);
		
		PageVo pageVo = new PageVo(page, pageSize);
		
		// ?????? ????????? ??? ?????????
		Map<String, Object> map1 = new HashMap<>();
		map1.put("page", page);
		map1.put("pageSize", pageSize);
		
		if(data1 == null || data1.equals("")) {
			map1.put("data1", datas1);
		}else {
			map1.put("data1", "%" + data1 + "%");
		}
		
		if(data2 == null || data2.equals("")) {
			map1.put("data2", datas2);
		}else {
			map1.put("data2","%" + data2 + "%");			
		}
		
		// ??????
		if(date1 == null || date1.equals("") && date2 == null || date2.equals("")) {
			map1.put("date1", "19700101");
			map1.put("date2", today1);
		}else {
			map1.put("date1", date11);
			map1.put("date2", date22);
		}
		
		// ????????? ????????? ???
		Map<String, Object> map3 = new HashMap<>();
		map3.put("page", page);
		map3.put("pageSize", pageSize);
		map3.put("emp_no", emp_no);
		// ??????
		if(date1 == null || date1.equals("") && date2 == null || date2.equals("")) {
			map3.put("date1", "19700101");
			map3.put("date2", today1);
		}else {
			map3.put("date1", date11);
			map3.put("date2", date22);
		}
		
		// ????????? ????????? ???
		Map<String, Object> map4 = new HashMap<>();
		map4.put("emp_no", emp_no);
		// ??????
		if(date1 == null || date1.equals("") && date2 == null || date2.equals("")) {
			map4.put("date1", "19700101");
			map4.put("date2", today1);
		}else {
			map4.put("date1", date11);
			map4.put("date2", date22);
		}
		
		// ????????? ???
		Map<String, Object> map2 = new HashMap<>();
		
		if(data1 == null || data1.equals("")) {
			map2.put("data1", datas1);
		}else {
			map2.put("data1", "%" + data1 + "%");
		}
		if(data2 == null || data2.equals("")) {
			map2.put("data2", datas2);
		}else {
			map2.put("data2","%" + data2 + "%");			
		}
		
		// ??????
		if(date1 == null || date1.equals("") && date2 == null || date2.equals("")) {
			map2.put("date1", "19700101");
			map2.put("date2", today1);
		}else {
			map2.put("date1", date11);
			map2.put("date2", date22);
		}
		
		int attendCnt = 0;
		
		int pagingCnt = 0;
		
		// ????????????
		attendCnt = onoffService.checkAttendCnt(emp_no);
		if(emp_no == 0) {
			model.addAllAttributes(onoffService.selectOnOffPaging(pageVo));			
		}else {
			model.addAllAttributes(onoffService.selectOnOffDetailPaging(map));				
		}
		
		int detailPagingCnt = 0;
		
		if(emp_no == 0) {
			model.addAttribute("onofflist", onoffService.searchOnoffPaging(map1));
			pagingCnt = onoffService.searchPagingCnt(map2);	
			int pagination = (int)Math.ceil( (double)pagingCnt / pageSize);			
			model.addAttribute("pagination", pagination);
			model.addAttribute("pageVo", pageVo);
			
			model.addAttribute("data1", data1);
			model.addAttribute("data2", date2);
				
		}else {
			model.addAttribute("onoffDetaillist", onoffService.searchDetailPaging(map3));
			detailPagingCnt = onoffService.searchDetailPagingCnt(map4);
			int pagination = (int)Math.ceil( (double)detailPagingCnt / pageSize);
			model.addAttribute("pagination1", pagination);
			model.addAttribute("pageVo1", pageVo);
		
		}
		
		if(attendCnt == 0) {
			model.addAttribute("attendCnt", 0);
		}else {
			model.addAttribute("attendCnt", 1);
		}
		
		 
	
		return "ajax/pagingOnOffAjaxHtml";
	}
	
	// ?????? ???????????? 
	@RequestMapping("onoffDetail")
    public String OnOffDetail(String emp_id, int emp_no, Model model) {
		 
		logger.debug("OnOffDetail() ==================== ");
		
		PageVo pageVo = new PageVo(1, 10);
		
		logger.debug("emp_no : {}", emp_no);
		
		Map<String, Object> map1 = new HashMap<>();
		map1.put("emp_no", emp_no);
		map1.put("page", 1);
		map1.put("pageSize", 10);
		
		Map<String, Object> map = onoffService.selectOnOffDetailPaging(map1);
		
		int pagination = (int)map.get("pagination1");

		// Bar Graph Data
		model.addAttribute("OnOffDataList", onoffService.selectOnOffData(emp_no));
		model.addAttribute("onoffVo", onoffService.selectonoff(emp_no));
		model.addAttribute("onoffDetaillist", (List<OnOffVo>)map.get("onoffDetaillist")); 
		model.addAttribute("pageVo1", pageVo);
		model.addAttribute("pagination1", pagination);
		model.addAttribute("emp_no", emp_no);
		
		return "admin/onoffDetail";
    }
	
	// ?????? ???????????? ????????? ?????????
	@RequestMapping("pagingOnOffDetailAjaxHtml")
	public String pagingOnOffDetailAjaxHtml(@RequestParam(defaultValue = "1") int page,
									  @RequestParam(defaultValue = "10") int pageSize,
									  int emp_no, String date1, String date2,
									  Model model) {
			
		Date today = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String today1 = sdf.format(today);
		
		PageVo pageVo = new PageVo(page, pageSize);
		
		String date11 = "";
		String date22 = "";
		
		if(date1 != null && date2 != null) {
			date11 = date1.replaceAll("-", "");
			date22 = date2.replaceAll("-", "");			
		}
	
		logger.debug("date11 : {}, data22 : {}", date11, date22);

		Map<String, Object> map = new HashMap<>();
		map.put("page", page);
		map.put("pageSize", pageSize);
		map.put("emp_no", emp_no);
		map.put("pageVo", pageVo);
		
		int detailPagingCnt = 0;
		// ????????? ????????? ???
		Map<String, Object> map3 = new HashMap<>();
		map3.put("page", page);
		map3.put("pageSize", pageSize);
		map3.put("emp_no", emp_no);
		// ??????
		if(date1 == null || date1.equals("") && date2 == null || date2.equals("")) {
			map3.put("date1", "19700101");
			map3.put("date2", today1);
		}else {
			map3.put("date1", date11);
			map3.put("date2", date22);
		}
				
		// ????????? ????????? ???
		Map<String, Object> map4 = new HashMap<>();
		map4.put("emp_no", emp_no);
		// ??????
		if(date1 == null || date1.equals("") && date2 == null || date2.equals("")) {
			map4.put("date1", "19700101");
			map4.put("date2", today1);
		}else {
			map4.put("date1", date11);
			map4.put("date2", date22);
		}
		
		model.addAttribute("onoffDetaillist", onoffService.searchDetailPaging(map3));
		detailPagingCnt = onoffService.searchDetailPagingCnt(map4);
		int pagination = (int)Math.ceil( (double)detailPagingCnt / pageSize);
		model.addAttribute("pagination1", pagination);
		model.addAttribute("pageVo1", pageVo);
		
		logger.debug("pagination : {}", pagination);

		return "ajax/pagingOnOffDetailAjaxHtml";
	}

	// ?????? ?????? ??????
	@RequestMapping("onofflistExcel")
	public void onofflistExcel(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap)
			throws Exception, Exception {

		// ?????? ????????? ????????????????????? ????????? ?????? ???????????? ???????????? ????????????.
		List<OnOffVo> onoffList = onoffService.selectOnOfflist();

		// ?????? ???????????? ?????? ?????????.
		Map<String, Object> beans = new HashMap<String, Object>();
		beans.put("onoffList", onoffList);

		// ?????? ???????????? ???????????? ?????? ?????? ??????
		EmpController me = new EmpController();

		me.download(request, response, beans, "AllOnOffList", "onoffTemplate.xlsx", "");

	}

	// ???????????? ????????????
	@RequestMapping("onoffDetailExcel")
	public void onoffDetailExcel(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap,
			int emp_no) throws Exception, Exception {

		logger.debug("emp_no : {}", emp_no);

		// ?????? ????????? ????????????????????? ????????? ?????? ???????????? ???????????? ????????????.
		List<OnOffVo> onoffList = onoffService.selectOnOffDetaillist(emp_no);

		// ?????? ???????????? ?????? ?????????.
		Map<String, Object> beans = new HashMap<String, Object>();
		beans.put("onoffList", onoffList);

		// ?????? ???????????? ???????????? ?????? ?????? ??????
		EmpController me = new EmpController();

		me.download(request, response, beans, "PersonalOnOffList", "onoffTemplate.xlsx", "");

	}
	
//---------------------------------------------??????------------------------------------------
	@RequestMapping("empMyPage")
	public String myPage(Model model, int emp_no) {
		
		model.addAttribute("empDetail", empService.selectEmpDetail(emp_no));
		logger.debug("emp_no : {}", empService.selectEmpDetail(emp_no));
		return "tiles.emp.myPage";
	}
	
	//?????? ????????? jsp??????
		@RequestMapping(path = "empOrganizationChart")
		public String emporganizationChart(Model model) {
			model.addAttribute("deptList", empService.selectDept());
			logger.debug("deptVo : {}",empService.selectDept());
			model.addAttribute("organizationList", empService.selectOrganization());
			return "tiles.emp.empOrganizationChart";
		}
		
		
	//?????? ????????? ????????????Ajax
	@RequestMapping("empDetailOrganizationAjaxHtml")
	public String empdetailUserAjax2(int emp_no, Model model) {
				
		logger.debug("emp_no : {}", emp_no);
			
		EmpVo empList = new EmpVo();
		empList.setEmp_no(emp_no);
			
		EmpVo detailUser = empService.detailUserAjax(empList);
			
		logger.debug("empVo : {}", detailUser);
			
		model.addAttribute("detailUser", detailUser);
			
		return "/emp/empDetailOrganizationAjaxHtml";
	}
	
	//???????????? password??????
	@RequestMapping(path = "selectPass", method = RequestMethod.POST)
	public String selectPass(String emp_id, int emp_no, String pass, Model model) {

		EmpVo empVo = new EmpVo();
		empVo.setEmp_no(emp_no);
		empVo.setPass(pass);
		empVo.setEmp_id(emp_id);
		
		EmpVo empidCheck = empService.selectUser(empVo.getEmp_id());
		
		//????????? pw??????
		boolean pwdMatch = pwdEncode.matches(empVo.getPass(), empidCheck.getPass());
		model.addAttribute("pwdMatch", pwdMatch);

		logger.debug("empVo:{}",empService.selectPassCount(empVo));
		int passCnt = empService.selectPassCount(empVo);
		model.addAttribute("passCnt", passCnt);

		return "jsonView";
	}
	
	//??????jsp??????
	@RequestMapping("empModifyAjaxHtml")
	public String empModifyAjaxHtml(Model model, int emp_no, EmpVo empvo) {
		EmpVo empList = new EmpVo();
		empList.setEmp_no(emp_no);
		
		EmpVo detailUser = empService.detailUserAjax(empList);
		
		logger.debug("empVo : {}", detailUser);
		
		model.addAttribute("detailUser", detailUser);
		return "tiles.emp.empModifyAjaxHtml";
	}
	
	//password??????
	@RequestMapping("modifyEmp")
	public String modifyEmp(EmpVo empVo, Model model){
		
		int modifyCnt = 0;
		try {
			//pw?????????
			String inputPass = empVo.getPass();
			String pwd = pwdEncode.encode(inputPass);
			empVo.setPass(pwd);
			
			modifyCnt = empService.modifyEmp(empVo);
		} catch (Exception e) {
			modifyCnt = 0;
			e.printStackTrace();
		}
		
		if(modifyCnt == 1) {
			return"redirect:/empController/empMyPage?emp_no="+ empVo.getEmp_no();
		}else {
			return "redirect:/empController/empModifyAjaxHtml?emp_no="+ empVo.getEmp_no();
		}
	}
	
	
	
}
 