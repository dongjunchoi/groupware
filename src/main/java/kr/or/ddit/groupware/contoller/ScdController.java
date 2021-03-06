package kr.or.ddit.groupware.contoller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import kr.or.ddit.common.model.PageVo;
import kr.or.ddit.groupware.model.CommCdVo;
import kr.or.ddit.groupware.model.ScdVo;
import kr.or.ddit.groupware.service.ScdService;

@RequestMapping("scd")
@Controller
public class ScdController {
	
	private static final Logger logger = LoggerFactory.getLogger(ScdController.class);
	
	@Resource(name = "scdService")
	private ScdService scdService;
	
	//메인페이지
	@RequestMapping("calendar")
	public String calendar(ScdVo scdVo, Model model) {

		Date date = new Date();

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		String start = sdf.format(date);
		List<ScdVo> scdList = scdService.selectAll(scdVo);
		logger.debug("scdList : {}", scdList);
		
		List<ScdVo> scdDivList = scdService.selectScdDivVo();
		logger.debug("scdDivList : {}", scdDivList);
		
		model.addAttribute("scdDivList", scdDivList);
		model.addAttribute("scdList", scdList);
		model.addAttribute("start", start);

		return "tiles.calendar.fullcalendar";
	}
	
	
	//���� �з� 
	@RequestMapping("calendarDiv")
	public String calendarDiv(ScdVo vo, int scd_div_no , Model model) {
		
		Date date = new Date();
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		String start = sdf.format(date);
		logger.debug("scd_div_no: {}", scd_div_no);
		
		List<ScdVo> scdList = scdService.selectScd(vo);
		
		logger.debug("scdList: {}", scdList);
		model.addAttribute("scdList",scdList);
		List<ScdVo> scdDivList = scdService.selectScdDivVo();
		logger.debug("scdDivList : {}", scdDivList);
		
		model.addAttribute("scdDivList", scdDivList);
		model.addAttribute("start", start);
		model.addAttribute("scd_div_no", scd_div_no);
		model.addAttribute("vo", vo);
		
		return "tiles.calendar.fullcalendar";
	}
	
	//등록 form으로 데이터 보내기
	@RequestMapping("registCalendarForm")
	public String calendar2(Date s_dt, Date e_dt,int emp_no, Model model) {
		
		model.addAttribute("s_dt", s_dt);
		model.addAttribute("e_dt", e_dt);
		model.addAttribute("emp_no", emp_no);
		logger.debug("emp_no:{}", emp_no );
		
		List<CommCdVo> commcdList = scdService.selectComm_cd();
		model.addAttribute("commcdList", commcdList);
		List<ScdVo> scdList = scdService.selectScdDivVo();
		model.addAttribute("scdList", scdList);
		return "tiles.calendar.registCalendar";
	}
	
	//일정등록
	@RequestMapping(path="registCalendar",method=RequestMethod.POST)
	public String registCalendar(ScdVo scdVo ,int emp_no) {
		
//		logger.debug("scdVo : {}", scdVo);
		
		scdService.registCalendar(scdVo);
		
		
		return "redirect:/scd/calendar?emp_no="+emp_no;
	}
	
	// 상세조회
	@RequestMapping("calendarView")
	public String calendarView(int scd_no, Model model) {
		
		ScdVo scdVo = scdService.scdView(scd_no);
//		logger.debug("scdVo :{}",scdVo);
		model.addAttribute("scdVo",scdVo);
		
		return "calendar/calendarView";
	}
	
	//수 정 form으로 데이터 보내기
	@RequestMapping(path="modifyCalendarForm",method=RequestMethod.POST)
	public String modifyCalendar(int scd_no, Model model) {
		ScdVo scdVo = scdService.scdView(scd_no);
//		logger.debug("scdVo :{}",scdVo);
		model.addAttribute("scdVo",scdVo);
		
		return "calendar/modifyCalendar";
	}
	
	//수정 완료
	@RequestMapping(path="modifyCalendar",method=RequestMethod.POST)
	public String modifyCalendar2(int scd_no,int scd_div_no, String s_dt2, String e_dt2, String plc, String title, String cont, int sta_cd ,int emp_no, Model model) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		Date s_dt =  sdf.parse(s_dt2);
//		logger.debug("s_dt :{}",s_dt);
		
		Date e_dt = sdf.parse(e_dt2);
				
		ScdVo scdVo2 = new ScdVo();
	
		scdVo2.setS_dt(s_dt);
		scdVo2.setE_dt(e_dt);
		scdVo2.setPlc(plc);
		scdVo2.setTitle(title);
		scdVo2.setCont(cont);
		scdVo2.setSta_cd(sta_cd);
		scdVo2.setScd_no(scd_no);
		scdVo2.setScd_div_no(scd_div_no);
//		logger.debug("scdVo :{}",scdVo);
		
		int updateCnt   = scdService.modifyCalendar(scdVo2);
		ScdVo scdVo = scdService.scdView(scd_no);
//		return "redirect:/scd/calendar?emp_no="+emp_no;
		
		model.addAttribute("scdVo",scdVo);
		return "calendar/calendarView";
	}
	//삭제 
	@RequestMapping(path="deleteCalendarAjax",method=RequestMethod.POST)
	public String deleteCalendarAjax(int scd_no,int emp_no ,Model model) {
		int deleteCnt = scdService.deleteCalendar(scd_no);
		
		model.addAttribute("deleteCnt",deleteCnt);
		
		return "jsonView";
	}
	//삭제 
	@RequestMapping(path="deleteCalendar",method=RequestMethod.POST)
	public String deleteCalendar(int scd_no,int emp_no ,Model model) {
		int deleteCnt = scdService.deleteCalendar(scd_no);
		
		
		return "redirect:/scd/calendar?emp_no="+emp_no;
	}
	
	
	
	
	
	
	
	//검색
	@RequestMapping(path="searchCalendar",method=RequestMethod.POST)
	public String searchCalendar(@RequestParam(defaultValue = "1") int page,
								 @RequestParam(defaultValue = "2") int pageSize, 
								 String data, Model model) {
		
//		logger.debug("data :{}", data);
		Map<String, Object> paramap = new HashMap<String, Object>();
		
		PageVo pagevo = new PageVo(page,pageSize);
//		logger.debug("pagevo :{}", pagevo);

		paramap.put("page", page);
		paramap.put("pageSize", pageSize);
		paramap.put("data", data);
		Map<String, Object> map = scdService.searchPagingCalendar(paramap);
		
		List<ScdVo> scdList= (List<ScdVo>)map.get("scdList");
		int scdCnt = (int)map.get("scdCnt");
		
		int pagination = (int) Math.ceil((double)scdCnt/pageSize);
		
		model.addAttribute("scdList", scdList);
		model.addAttribute("data", data);
		model.addAttribute("pagination", pagination);
		model.addAttribute("pageVo", pagevo);
		
		return "tiles.calendar.scdList";
	}
	
	//검색 페이징 처리
	@RequestMapping(path="searchCalendar",method=RequestMethod.GET)
	public String searchCalendar2(@RequestParam(defaultValue = "1") int page,
								  @RequestParam(defaultValue = "5") int pageSize,  String type, String keywords, int emp_no ,Model model) {
		
//		logger.debug("searchCalendar2() ============================= \n");
		
		
		Map<String, Object> paramap = new HashMap<String, Object>();
		PageVo pagevo = new PageVo(page,pageSize);
		paramap.put("page", page);
		paramap.put("pageSize", pageSize);
		paramap.put("emp_no", emp_no);
		logger.debug("pageSize : {}", pageSize);
		String searchType ="";
		// 카테고리에 따른 조건문 
			if(type.equals("u")) { //작성자 한글이름
				paramap.put("field", "e.ko_nm");
				paramap.put("data", keywords);
				searchType = "u";
			}else if(type.equals("t")) { //제목
				paramap.put("field", "title");
				paramap.put("data", keywords);
				searchType ="t";
			}else if(type.equals("s")) { // 상태 
				paramap.put("field", " c.cd_ko_nm");
				paramap.put("data", keywords);
				searchType = "s";
			}else if(type.equals("d")) {//일정타입
				paramap.put("field", "d.scd_div_nm");
				paramap.put("data", keywords);
				searchType = "d";
			}else {
				paramap.put("field", "e.ko_nm");
				paramap.put("data", "");
			}
		Map<String, Object> map = scdService.searchPagingCalendar(paramap);
		
		List<ScdVo> scdList= (List<ScdVo>)map.get("scdList");
		int scdCnt = (int)map.get("scdCnt");
		
//		logger.debug("scdCnt : {}", scdCnt);
		
		PageVo pageVo = (PageVo)map.get("pageVo");
		
		int pagination = (int) Math.ceil((double)scdCnt/pageSize);
		model.addAttribute("scdList", scdList);
		model.addAttribute("pagination", pagination);
		model.addAttribute("pageVo", pageVo);
		model.addAttribute("pageSize", pageSize);
		model.addAttribute("searchType",searchType );
		model.addAttribute("keywords",keywords );
		model.addAttribute("type",type );
		model.addAttribute("scdCnt",scdCnt );
		model.addAttribute("emp_no",emp_no );
		
		return "tiles.calendar.scdList";
	}
}
